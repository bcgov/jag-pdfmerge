package ca.bc.gov.open.jag.documentutils.exception;

import ca.bc.gov.open.jag.documentutils.Keys;
import ca.bc.gov.open.jag.documentutils.api.models.ApiError;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GlobalControllerAdviceTest {

    private static final String TRANSACTION_ID = "id";
    public GlobalControllerAdvice sut;

    @Mock
    private WebRequest webRequestMock;
    @Mock
    private BindingResult bindingResultMock;
    @Mock
    private MethodParameter parameterMock;
    @Mock
    private Executable executableMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.initMocks(this);

        sut = new GlobalControllerAdvice();

    }

    @Test
    @DisplayName("500: with merge exception should return ApiError")
    public void testHandleMergeException() {

        Mockito.when(webRequestMock.getHeader(Mockito.eq(Keys.CORRELATION_ID))).thenReturn(TRANSACTION_ID);

        MergeException mergeException = new MergeException("random exception", new ParserConfigurationException("parser error"));
        ResponseEntity<ApiError> actual = sut.handleDigitalFormsException(mergeException, webRequestMock);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(TRANSACTION_ID, actual.getBody().getTransactionId());
        Assertions.assertEquals("parser error", actual.getBody().getDetail());
        Assertions.assertEquals("MergeException", actual.getBody().getError());
        Assertions.assertEquals("random exception", actual.getBody().getMessage());

    }

    @Test
    @DisplayName("404: with NoHandlerException should return ApiError")
    public void testHandleNoHandlerException() {

        Mockito.when(webRequestMock.getHeader(Mockito.eq(Keys.CORRELATION_ID))).thenReturn(TRANSACTION_ID);

        HttpHeaders header = new HttpHeaders();
        header.add("X-TransactionId", "test");
        NoHandlerFoundException handlerException = new NoHandlerFoundException("POST", "/get/acdc", header);
        ResponseEntity<ApiError> actual = sut.handleNoHandlerException(handlerException, webRequestMock);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        Assertions.assertEquals(TRANSACTION_ID, actual.getBody().getTransactionId());
        Assertions.assertEquals("Request URL does not exist", actual.getBody().getDetail());
        Assertions.assertEquals("NO_HANDLER_ERROR", actual.getBody().getError());
        Assertions.assertEquals("Request URL does not exist.", actual.getBody().getMessage());

    }

    @Test
    @DisplayName("400: with HttpMessageNotReadableException should return ApiError")
    public void testhandleHttpMessageNotReadableException() {

        Mockito.when(webRequestMock.getHeader(Mockito.eq(Keys.CORRELATION_ID))).thenReturn(TRANSACTION_ID);

        HttpHeaders header = new HttpHeaders();
        header.add("X-TransactionId", "test");
        HttpInputMessage httpInputMessage = new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                return null;
            }

            @Override
            public HttpHeaders getHeaders() {
                return null;
            }
        };

        HttpMessageNotReadableException messageNotReadableException = new HttpMessageNotReadableException("random", httpInputMessage);
        ResponseEntity<ApiError> actual = sut.handleHttpMessageNotReadableException(messageNotReadableException, webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(TRANSACTION_ID, actual.getBody().getTransactionId());
        Assertions.assertEquals("Required data not found in the request body", actual.getBody().getDetail());
        Assertions.assertEquals("MISSING_REQUEST_BODY_ERROR", actual.getBody().getError());
        Assertions.assertEquals("Invalid payload.", actual.getBody().getMessage());

    }

    @Test
    @DisplayName("500: with HttpMessageNotReadableException should return ApiError")
    public void testhandleException() {

        Mockito.when(webRequestMock.getHeader(Mockito.eq(Keys.CORRELATION_ID))).thenReturn(TRANSACTION_ID);

        NullPointerException exception = new NullPointerException("test");
        ResponseEntity<ApiError> actual = sut.handleDefaultException(exception, webRequestMock);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(TRANSACTION_ID, actual.getBody().getTransactionId());
        Assertions.assertEquals("Exception type: java.lang.NullPointerException", actual.getBody().getDetail());
        Assertions.assertEquals("UNKNOWN_ERROR", actual.getBody().getError());
        Assertions.assertEquals("Unknown exception while trying to merge documents.", actual.getBody().getMessage());

    }

    @Test
    @DisplayName("400: with MethodArgumentNotValidException should return ApiError")
    public void testhandleValidationExceptions() {

        Mockito.when(webRequestMock.getHeader(Mockito.eq(Keys.CORRELATION_ID))).thenReturn(TRANSACTION_ID);

        List<ObjectError> errors = new ArrayList<>();
        FieldError error = new FieldError("param", "first", "message");
        errors.add(error);
        Mockito.when(bindingResultMock.getErrorCount()).thenReturn(2);
        Mockito.when(bindingResultMock.getAllErrors()).thenReturn(errors);

        Mockito.when(parameterMock.getParameterIndex()).thenReturn(0);
        Mockito.when(parameterMock.getExecutable()).thenReturn(executableMock);
        Mockito.when(executableMock.toGenericString()).thenReturn("string");

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameterMock, bindingResultMock);
        ResponseEntity<ApiError> actual = sut.handleValidationExceptions(exception, webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(TRANSACTION_ID, actual.getBody().getTransactionId());
        Assertions.assertEquals("Validation failed for argument [0] in string with 2 errors: [Field error in object 'param' on field 'first': rejected value [null]; codes []; arguments []; default message [message]] ", actual.getBody().getDetail());
        Assertions.assertEquals("MethodArgumentNotValidException", actual.getBody().getError());
        Assertions.assertEquals("Unknown exception while trying to merge documents.", actual.getBody().getMessage());

    }


}
