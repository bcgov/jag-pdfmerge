package ca.bc.gov.open.jag.documentutils.controller;

import ca.bc.gov.open.jag.documentutils.model.*;
import ca.bc.gov.open.jag.documentutils.service.MergeService;
import ca.bc.gov.open.jag.documentutils.exception.MergeException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MergeControllerTest {


    public MergeController sut;

    @Mock
    private MergeService mergeServiceMock;

    @BeforeAll
    public void beforeAll() throws MergeException {

        MockitoAnnotations.initMocks(this);

        DocMergeResponse documentMerged = new DocMergeResponse();
        String document = "the document";
        documentMerged.setDocument(document);
        documentMerged.setMimeType("test");

        Mockito
                .doReturn(documentMerged)
                .when(mergeServiceMock).mergePDFDocuments(
                ArgumentMatchers.argThat(x -> x.getOptions().getCreateToC() == true), Mockito.anyString());

        Mockito
                .doThrow(MergeException.class)
                .when(mergeServiceMock).mergePDFDocuments(
                ArgumentMatchers.argThat(x -> x.getOptions().getCreateToC() == false), Mockito.anyString());

        sut = new MergeController(mergeServiceMock);
    }

    @Test
    @DisplayName("200: with valid document should merge documents")
    public void itShouldMergeDocuments() {

        @Valid DocMergeRequest request = new DocMergeRequest();
        List<Document> documents = new ArrayList<>();

        Document document1 = new Document();
        document1.setOrder(1);
        document1.setId("id1");
        document1.setData("data");
        document1.setDocType("pdf");
        documents.add(document1);

        Document document2 = new Document();
        document2.setOrder(1);
        document2.setId("id1");
        document2.setData("data");
        document2.setDocType("pdf");
        documents.add(document2);

        request.setDocuments(documents);

        Options options = new Options();
        options.setForcePDFAOnLoad("true");
        options.setCreateToC("true");
        request.setOptions(options);

        ResponseEntity<JSONResponse<DocMergeResponse>> actual = sut.mergeDocumentPost("id", request);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertNull(actual.getBody().getError());
        Assertions.assertEquals("success", actual.getBody().getResp());
        Assertions.assertEquals("the document", actual.getBody().getData().getDocument());
        Assertions.assertEquals("test", actual.getBody().getData().getMimeType());

    }

    @Test
    @DisplayName("500: with ANY exception should return 500")
    public void itShouldNotMergeWithInvalidDocuments() {


        @Valid DocMergeRequest request = new DocMergeRequest();
        List<Document> documents = new ArrayList<>();

        Document document1 = new Document();
        document1.setOrder(1);
        document1.setId("id1");
        document1.setData("data");
        document1.setDocType("pdf");
        documents.add(document1);

        Document document2 = new Document();
        document2.setOrder(1);
        document2.setId("id1");
        document2.setData("not good data");
        document2.setDocType("pdf");
        documents.add(document2);

        request.setDocuments(documents);

        Options options = new Options();
        options.setForcePDFAOnLoad("true");
        options.setCreateToC("false");
        request.setOptions(options);

        ResponseEntity<JSONResponse<DocMergeResponse>> actual = sut.mergeDocumentPost("id", request);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(500, actual.getBody().getError().getHttpStatus());
        Assertions.assertEquals("Request cannot be processed. See logging for correlation id id", actual.getBody().getError().getMessage());

    }

}

