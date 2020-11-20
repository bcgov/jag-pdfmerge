package ca.bc.gov.open.jag.documentutils.adobe;

import ca.bc.gov.open.jag.documentutils.adobe.models.MergeDoc;
import ca.bc.gov.open.jag.documentutils.exception.DocumentParserException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.util.LinkedList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DDXServiceImplTest {

    public DDXServiceImpl sut;

    @Mock
    private DocumentBuilderFactory documentBuilderFactoryMock;

    @Mock
    private DocumentBuilder documentBuilderMock;

    @Mock
    private Document documentMock;

    @Mock
    private Element elementMock;

    @Mock
    private TransformerFactory transformerFactoryMock;

    @Mock
    private Transformer transformerMock;

    @BeforeEach
    public void beforeEach() throws ParserConfigurationException {

        MockitoAnnotations.initMocks(this);

        Mockito.when(documentBuilderMock.newDocument()).thenReturn(documentMock);

        Mockito.when(documentMock.createElement(Mockito.anyString())).thenReturn(elementMock);

        Mockito.when(documentMock.getDocumentURI()).thenReturn("success");
        Mockito.when(documentMock.getTextContent()).thenReturn("test");

        sut = new DDXServiceImpl(documentBuilderFactoryMock, transformerFactoryMock);

    }

    @Test
    @DisplayName("ok: should create Merged DDX")
    public void shouldCreateMergeDDX() throws ParserConfigurationException {

        Mockito.when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);

        LinkedList<MergeDoc> pageList = new LinkedList<>();
        MergeDoc mergeDoc = new MergeDoc("test".getBytes());
        pageList.add(mergeDoc);

        Document actual = sut.createMergeDDX(pageList, true);

        Assertions.assertEquals("success", actual.getDocumentURI());

    }

    @Test
    @DisplayName("exception: with ParserConfigurationException should rethrow DocumentParserException")
    public void withParserConfigurationExceptionShouldRethrowDocumentParserException() throws ParserConfigurationException {

        Mockito.when(documentBuilderFactoryMock.newDocumentBuilder()).thenThrow(new ParserConfigurationException("random"));

        LinkedList<MergeDoc> pageList = new LinkedList<>();
        MergeDoc mergeDoc = new MergeDoc("test".getBytes());
        pageList.add(mergeDoc);

        Assertions.assertThrows (DocumentParserException.class, () ->  sut.createMergeDDX(pageList, true));

    }

    @Test
    @DisplayName("ok: should convert to DDX")
    public void shouldConvertToDDX() throws ParserConfigurationException, TransformerConfigurationException {

        Mockito.when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);
        Mockito.when(transformerFactoryMock.newTransformer()).thenReturn(transformerMock);


        LinkedList<MergeDoc> pageList = new LinkedList<>();
        MergeDoc mergeDoc = new MergeDoc("test".getBytes());
        pageList.add(mergeDoc);

        Document documentToConvert = sut.createMergeDDX(pageList, true);

        Assertions.assertDoesNotThrow(() -> sut.convertDDX(documentToConvert));

    }

    @Test
    @DisplayName("exception: when ConversionException should throw ParserConfigurationException")
    public void withConversionExceptionShouldRethrowParserConfigurationException() throws ParserConfigurationException, TransformerConfigurationException {

        Mockito.when(documentBuilderFactoryMock.newDocumentBuilder()).thenReturn(documentBuilderMock);
        Mockito.when(transformerFactoryMock.newTransformer()).thenThrow(new TransformerConfigurationException("random"));

        LinkedList<MergeDoc> pageList = new LinkedList<>();
        MergeDoc mergeDoc = new MergeDoc("test".getBytes());
        pageList.add(mergeDoc);

        Document documentToConvert = sut.createMergeDDX(pageList, true);

        Assertions.assertThrows(DocumentParserException.class, () -> sut.convertDDX(documentToConvert));

    }

}
