package ca.bc.gov.open.pssg.docmerge.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.adobe.idp.Document;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactory;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactoryProperties;
import com.adobe.livecycle.assembler.client.AssemblerOptionSpec;
import com.adobe.livecycle.assembler.client.AssemblerResult;
import com.adobe.livecycle.assembler.client.AssemblerServiceClient;
import com.adobe.livecycle.docconverter.client.ConversionException;
import com.adobe.livecycle.docconverter.client.DocConverterServiceClient;
import com.adobe.livecycle.docconverter.client.PDFAConversionOptionSpec;
import com.adobe.livecycle.docconverter.client.PDFAConversionResult;

import ca.bc.gov.open.pssg.docmerge.config.ConfigProperties;
import ca.bc.gov.open.pssg.docmerge.exception.MergeException;
import ca.bc.gov.open.pssg.docmerge.model.MergeDoc;
import ca.bc.gov.open.pssg.docmerge.model.DocMergeRequest;
import ca.bc.gov.open.pssg.docmerge.model.DocMergeResponse;
import ca.bc.gov.open.pssg.docmerge.utils.DDXUtils;
import ca.bc.gov.open.pssg.docmerge.utils.PDFBoxUtilities;
import ca.bc.gov.open.pssg.docmerge.utils.DocMergeConstants;

/**
 * 
 * PDF Merge service. 
 * 
 * @author shaunmillargov
 *
 */
@Service
public class MergeServiceImpl implements MergeService {

	private final Logger logger = LoggerFactory.getLogger(MergeServiceImpl.class);
	
	@Autowired
	private ConfigProperties properties;

	@Override
	public DocMergeResponse mergePDFDocuments(DocMergeRequest request, String correlationId) throws MergeException {
		
		DocMergeResponse resp = new DocMergeResponse();
		
		try {
			
			logger.info("Calling mergePDFDocuments...");
			
			// Set AEM connection properties, SOAP mode. 
			// Properties are fetched from either OpenShift Secrets or if running locally, ENV VARIABLES. 
			Properties connectionProps = new Properties();
			connectionProps.setProperty(ServiceClientFactoryProperties.DSC_DEFAULT_SOAP_ENDPOINT, properties.getAemEndpoint());
			connectionProps.setProperty(ServiceClientFactoryProperties.DSC_TRANSPORT_PROTOCOL, ServiceClientFactoryProperties.DSC_SOAP_PROTOCOL);
			connectionProps.setProperty(ServiceClientFactoryProperties.DSC_SERVER_TYPE, DocMergeConstants.DSC_SERVER_TYPE_JBOSS);
			connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_USERNAME, properties.getAemUser());
			connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_PASSWORD, properties.getAemPassword());

			// Create a ServiceClientFactory instance
			ServiceClientFactory sFactory = ServiceClientFactory.createInstance(connectionProps);

			// Create an AssemblerServiceClient object
			AssemblerServiceClient assemblerClient = new AssemblerServiceClient(sFactory);
			
			LinkedList<MergeDoc> pageList=new LinkedList<MergeDoc>();
			
			// Sort the document based on placement id in the event they are mixed. lowest to highest 
			Collections.sort(request.getDocuments(), new Comparator<ca.bc.gov.open.pssg.docmerge.model.Document>() {
			    public int compare(ca.bc.gov.open.pssg.docmerge.model.Document d1, ca.bc.gov.open.pssg.docmerge.model.Document d2) {
			        return d1.getOrder().compareTo(d2.getOrder());
			    }
			});
			
			// For each document, check if XFA and convert to PDF/A if requested via option. 
			for (ca.bc.gov.open.pssg.docmerge.model.Document doc: request.getDocuments()) {
				
				byte[] thisDoc = Base64Utils.decode(doc.getData().getBytes()); 
				
				if ( request.getOptions().getForcePDFAOnLoad() && PDFBoxUtilities.isPDFXfa(thisDoc)) {
					logger.info("forcePDFA is on and document, order " + doc.getOrder() + ", is XFA. Converting to PDF/A..."); 
					
					//call PDF/A transformation 
					thisDoc = createPDFADocument(thisDoc, sFactory);
				}
				
				pageList.add( new MergeDoc( thisDoc));
				logger.info("Loaded page " + doc.getOrder());
			}
			
			// Use DDXUtils to Dynamically generate the DDX file sent to AEM. 
			org.w3c.dom.Document aDDx = DDXUtils.createMergeDDX(pageList, request.getOptions().getCreateToC());
			logger.info("Dynamically generated DDX : " + DDXUtils.DDXDocumentToString(aDDx));
			Document myDDX = DDXUtils.convertDDX(aDDx);
			
			// Create a Map object to store the PDF source documents
			Map<String, Object> inputs = new HashMap<String, Object>();
			Iterator<MergeDoc> it = pageList.iterator();
		    while(it.hasNext()) {
		      MergeDoc pageElement = (MergeDoc)it.next();
			  Document pageDocument = new Document(pageElement.getFile());
			  inputs.put(pageElement.getId(), (Object)pageDocument);
		    }
		    
		    // Create an AssemblerOptionsSpec object
 			AssemblerOptionSpec assemblerSpec = new AssemblerOptionSpec();
 			assemblerSpec.setFailOnError(false);

 			// Submit the job to Assembler service
 			AssemblerResult jobResult = assemblerClient.invokeDDX(myDDX, inputs, assemblerSpec);
 			Map<String, Document> allDocs = jobResult.getDocuments();

 			// Retrieve the result PDF document from the Map object
 			Document outDoc = null;
 			
 			// Iterate through the map object to retrieve the result PDF document
			for (Iterator i = allDocs.entrySet().iterator(); i.hasNext();) {
				
				// Retrieve the Map object’s value
				Map.Entry e = (Map.Entry) i.next();

				// Get the key name as specified in the DDX document
				String keyName = (String) e.getKey();
				if (keyName.equalsIgnoreCase(DocMergeConstants.DDX_OUTPUT_NAME)) {
					
					Object o = e.getValue();
					outDoc = (Document) o;

					// Save the result PDF file (debugging)
					//File myOutFile = new File("C:\\Users\\176899\\workspaces\\neon\\pdfmerge\\AEMMergeTest\\output\\out.pdf");
					//outDoc.copyToFile(myOutFile);
					
					resp.setDocument( Base64Utils.encodeToString( IOUtils.toByteArray(outDoc.getInputStream())));					
				}
			}
			
			resp.setMimeType(DocMergeConstants.PDF_MIME_TYPE);
			
		} catch (Exception e) {
			
			logger.error("Failure at mergeDocuments. Reason: " + e.getMessage());
			e.printStackTrace();
			throw new MergeException(e.getMessage(), e);
		}
		
		return resp;
	}
	
	/**
	*
	* Transforms PDF (XFA or otherwise) to PDF/A type (most basic). 
	*
	* @param inputFile
	* @return
	* @throws ConversionException
	* @throws IOException
	*/
	
	private byte[] createPDFADocument(byte[] inputFile, ServiceClientFactory factory) throws ConversionException, IOException {
		
		// Create a DocConverterServiceClient object
		DocConverterServiceClient docConverter = new DocConverterServiceClient(factory);
		Document inDoc = new Document(inputFile);
	
		// Create a PDFAConversionOptionSpec object and set tracking information
		PDFAConversionOptionSpec spec = new PDFAConversionOptionSpec();
		
	    // AEM logging level - suggest turning this off unless issues need debugging on AEM side. 
		//spec.setLogLevel("INFO");
		//spec.setLogLevel("FINE");
	
		 // Convert the PDF document to a PDF/A document
		PDFAConversionResult result = docConverter.toPDFA(inDoc, spec);
	
		 // Save the PDF/A file
		Document pdfADoc = result.getPDFADocument();
		return IOUtils.toByteArray(pdfADoc.getInputStream());
	}
	
}
