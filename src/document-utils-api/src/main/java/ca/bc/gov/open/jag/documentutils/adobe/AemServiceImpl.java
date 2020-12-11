package ca.bc.gov.open.jag.documentutils.adobe;

import ca.bc.gov.open.jag.documentutils.adobe.models.MergeDoc;
import ca.bc.gov.open.jag.documentutils.api.MediaTypes;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeResponse;
import ca.bc.gov.open.jag.documentutils.exception.MergeException;
import ca.bc.gov.open.jag.documentutils.utils.PDFBoxUtilities;
import com.adobe.idp.Document;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactory;
import com.adobe.livecycle.assembler.client.AssemblerOptionSpec;
import com.adobe.livecycle.assembler.client.AssemblerResult;
import com.adobe.livecycle.assembler.client.AssemblerServiceClient;
import com.adobe.livecycle.assembler.client.OperationException;
import com.adobe.livecycle.docconverter.client.ConversionException;
import com.adobe.livecycle.docconverter.client.DocConverterServiceClient;
import com.adobe.livecycle.docconverter.client.PDFAConversionOptionSpec;
import com.adobe.livecycle.docconverter.client.PDFAConversionResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Scope("request")
public class AemServiceImpl implements AemService {

    private final Logger logger = LoggerFactory.getLogger(AemServiceImpl.class);

    private final ServiceClientFactory serviceClientFactory;
    private final AssemblerServiceClient assemblerClient;
    private final DocConverterServiceClient docConverterServiceClient;
    private final DDXService ddxService;

    public AemServiceImpl(
            ServiceClientFactory serviceClientFactory,
            AssemblerServiceClient assemblerClient,
            DocConverterServiceClient docConverterServiceClient,
            DDXService ddxService) {
        this.serviceClientFactory = serviceClientFactory;
        this.assemblerClient = assemblerClient;
        this.docConverterServiceClient = docConverterServiceClient;
        this.ddxService = ddxService;
    }

    @Override
    public DocMergeResponse mergePDFDocuments(DocMergeRequest request, String correlationId) {

        DocMergeResponse resp = new DocMergeResponse();
        resp.setMimeType(MediaTypes.APPLICATION_PDF);

        logger.info("Calling mergePDFDocuments...");

        // Sort the document based on placement id in the event they are mixed. lowest to highest
        LinkedList<MergeDoc> pageList = convertToLinkedList(request);

        // Use DDXUtils to Dynamically generate the DDX file sent to AEM.
        Document myDDX = convertToDDxDocument(request, pageList);

        // Create a Map object to store the PDF source documents
        Map<String, Object> inputs = pageList.stream()
                .collect(Collectors.toMap(MergeDoc::getId, doc -> new Document(doc.getFile())));

        Map<String, Document> allDocs = getAssemblerJobMap(myDDX, inputs);

        // Iterate through the map object to retrieve the result PDF document
        resp.setDocument(allDocs.entrySet().stream()
                .filter(mapEntry -> mapEntry.getKey().equalsIgnoreCase(AdobeKeys.DDX_OUTPUT_NAME))
                .map(mapEntry -> buildOutputDocument((Document) mapEntry.getValue()))
                .findFirst().get());

        return resp;

    }

    private Map<String, Document> getAssemblerJobMap(Document myDDX, Map<String, Object> inputs) {

        AssemblerOptionSpec assemblerSpec = new AssemblerOptionSpec();
        assemblerSpec.setFailOnError(false);

        try {
            AssemblerResult jobResult = assemblerClient.invokeDDX(myDDX, inputs, assemblerSpec);
            return jobResult.getDocuments();
        } catch (OperationException e) {
            logger.error("Exception while assembling documents", e);
            throw new MergeException("Exception while assembling documents", e);
        }

    }

    private Document convertToDDxDocument(DocMergeRequest request, LinkedList<MergeDoc> pageList) {
        org.w3c.dom.Document aDDx = ddxService.createMergeDDX(pageList, request.getOptions().isCreateToC());
        return ddxService.convertDDX(aDDx);
    }

    private LinkedList<MergeDoc> convertToLinkedList(DocMergeRequest request) {
        request
                .getDocuments()
                .sort(Comparator.comparing(ca.bc.gov.open.jag.documentutils.api.models.Document::getIndex));

        return request.getDocuments().stream()
                .map(doc -> buildMergeDoc(doc, request))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private MergeDoc buildMergeDoc(ca.bc.gov.open.jag.documentutils.api.models.Document doc, DocMergeRequest request) {

        byte[] docBytes = Base64Utils.decode(doc.getData().getBytes());

        if (request.getOptions().isForcePDFAOnLoad() && PDFBoxUtilities.isPDFXfa(docBytes)) {
            logger.info("forcePDFA is on and document, order {}, is XFA. Converting to PDF/A...", doc.getIndex());
            return new MergeDoc(createPDFADocument(docBytes));
        }

        logger.info("Loaded page {}", doc.getIndex());
        return new MergeDoc(docBytes);
    }

    private String buildOutputDocument(Document document) {
        try {
            return Base64Utils.encodeToString(IOUtils.toByteArray(document.getInputStream()));
        } catch (IOException e) {
            logger.error("Error creating pdf a ", e);
            throw new MergeException("Error creating a pdf", e);
        }
    }

    /**
     * Transforms PDF (XFA or otherwise) to PDF/A type (most basic).
     *
     * @param inputFile
     * @return
     * @throws ConversionException
     * @throws IOException
     */

    private byte[] createPDFADocument(byte[] inputFile) {

        Document inDoc = new Document(inputFile);

        // Create a PDFAConversionOptionSpec object and set tracking information
        PDFAConversionOptionSpec spec = new PDFAConversionOptionSpec();

        try {
            PDFAConversionResult result = docConverterServiceClient.toPDFA(inDoc, spec);
            // Save the PDF/A file
            Document pdfADoc = result.getPDFADocument();
            return IOUtils.toByteArray(pdfADoc.getInputStream());
        } catch (ConversionException | IOException e) {
            logger.error("Error while converting document tp PDFA", e);
            throw new MergeException("Error while converting document tp PDFA", e);
        }


    }

}
