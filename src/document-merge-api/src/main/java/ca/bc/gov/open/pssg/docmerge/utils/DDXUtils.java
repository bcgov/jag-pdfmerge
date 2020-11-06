package ca.bc.gov.open.pssg.docmerge.utils;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ca.bc.gov.open.pssg.docmerge.model.MergeDoc;



/**
 * A set of DDX utils. 
 * 
 * @author shaunmillargov
 *
 */
public class DDXUtils {

	private final static Logger logger = LoggerFactory.getLogger(DDXUtils.class);
	
	/**
	 * Creates a Merge DDX document for Assembler using an org.w3c.dom.Document object
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static Document createMergeDDX(LinkedList<MergeDoc> pageList, boolean addToC) throws Exception {

		Document document = null;
		try {
			
			// Create DocumentBuilderFactory and DocumentBuilder objects
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			// Create a new Document object
			document = builder.newDocument();
			
			// Create the root element and append it to the XML DOM
			Element root = (Element) document.createElement(DocMergeConstants.DDX_ELEMENT_DDX);
			root.setAttribute(DocMergeConstants.DDX_NAMESPACE_ATTRIBUTE, DocMergeConstants.DDX_NAMESPACE);
			document.appendChild(root);
			
			// Create the output element
			Element PDFs = (Element) document.createElement(DocMergeConstants.DDX_ELEMENT_PDF);
			PDFs.setAttribute(DocMergeConstants.DDX_OUTPUT_ATTRIBUTE, DocMergeConstants.DDX_OUTPUT_NAME);
			root.appendChild(PDFs);
			
			// Add ToC?
			if (addToC) {
				logger.info("Adding table of contents...");
				Element ToC = (Element) document.createElement("TableOfContents");
				ToC.setAttribute("bookmarkTitle", "Table Of Contents");
				PDFs.appendChild(ToC);
			}
			
			// Add each pageId element to the DDX
			for (int i = 0; i < pageList.size(); i++) {
				Element PDF = (Element) document.createElement(DocMergeConstants.DDX_ELEMENT_PDF);
				PDF.setAttribute(DocMergeConstants.DDX_SOURCE_ATTRIBUTE, pageList.get(i).getId());
				if (addToC) {
					PDF.setAttribute("bookmarkTitle", "Document " + (i + 1)); 
					PDF.setAttribute("includeInTOC", "true");
				}
				PDFs.appendChild(PDF);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error at DDxUtils.createMergeDDX: " + e.getMessage());
			throw e;
		}

		return document;
	}
	
	/**
	 * 
	 * Converts between org.w3c.dom.Document and com.adobe.idp.Document types. 
	 * 
	 * @param ddx
	 * @return
	 * @throws Exception 
	 */
	public static com.adobe.idp.Document convertDDX(Document ddx) throws Exception {
		
		byte[] mybytes = null;
		
		try {
			// Create a Java Transformer object
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer transForm = transFact.newTransformer();
			
			// Create a Java ByteArrayOutputStream object
			ByteArrayOutputStream myOutStream = new ByteArrayOutputStream();
			
			// Create a Java Source object
			javax.xml.transform.dom.DOMSource myInput = new DOMSource(ddx);
			
			// Create a Java Result object
			javax.xml.transform.stream.StreamResult myOutput = new StreamResult(myOutStream);
			
			// Populate the Java ByteArrayOutputStream object
			transForm.transform(myInput, myOutput);
			
			// Get the size of the ByteArrayOutputStream buffer
			int myByteSize = myOutStream.size();
			
			// Allocate myByteSize to the byte array
			mybytes = new byte[myByteSize];
			
			// Copy the content to the byte array
			mybytes = myOutStream.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error at DDxUtils.convertDDX: " + e.getMessage());
			throw e;
		}
		
		// Create a com.adobe.idp.Document object and copy the
		// contents of the byte array
		com.adobe.idp.Document myDocument = new com.adobe.idp.Document(mybytes);
		return myDocument;

	}
	
	/**
	 * 
	 * @param ddx
	 * @return
	 * @throws TransformerException
	 */
	public static String DDXDocumentToString(Document ddx) throws Exception {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;

		try {

			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(ddx);
			StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			return result.getWriter().toString();

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			logger.error("Error at DDxUtils.DDXDocumentToString: " + e.getMessage());
			throw e;
		}
	}
	
}
