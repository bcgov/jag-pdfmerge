package ca.bc.gov.open.jag.documentutils.controller;

import ca.bc.gov.open.jag.documentutils.api.DocumentController;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeResponse;
import ca.bc.gov.open.jag.documentutils.api.models.Document;
import ca.bc.gov.open.jag.documentutils.api.models.Options;
import ca.bc.gov.open.jag.documentutils.adobe.AemService;
import ca.bc.gov.open.jag.documentutils.exception.MergeException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentControllerTest {


    public DocumentController sut;

    @Mock
    private AemService aemServiceMock;

    @BeforeEach
    public void beforeEach() throws MergeException {

        MockitoAnnotations.initMocks(this);

        DocMergeResponse documentMerged = new DocMergeResponse();
        String document = "the document";
        documentMerged.setDocument(document);
        documentMerged.setMimeType("test");

        Mockito
                .doReturn(documentMerged)
                .when(aemServiceMock).mergePDFDocuments(
                ArgumentMatchers.argThat(x -> x.getOptions().isCreateToC() == true), Mockito.anyString());

        Mockito
                .doThrow(MergeException.class)
                .when(aemServiceMock).mergePDFDocuments(
                ArgumentMatchers.argThat(x -> x.getOptions().isCreateToC() == false), Mockito.anyString());

        sut = new DocumentController(aemServiceMock);
    }

    @Test
    @DisplayName("200: with valid document should merge documents")
    public void itShouldMergeDocuments() {

        @Valid DocMergeRequest request = new DocMergeRequest();
        List<Document> documents = new ArrayList<>();

        Document document1 = new Document();
        document1.setIndex(1);
        document1.setData("data");
        documents.add(document1);

        Document document2 = new Document();
        document2.setIndex(1);
        document2.setData("data");
        documents.add(document2);

        request.setDocuments(documents);

        Options options = new Options();
        options.setForcePDFAOnLoad(true);
        options.setCreateToC(true);
        request.setOptions(options);

        ResponseEntity<DocMergeResponse> actual = sut.mergeDocumentPost("id", "clientId", request);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals("the document", actual.getBody().getDocument());
        Assertions.assertEquals("test", actual.getBody().getMimeType());

    }

}

