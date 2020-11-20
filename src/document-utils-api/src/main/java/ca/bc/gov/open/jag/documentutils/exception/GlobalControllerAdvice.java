package ca.bc.gov.open.jag.documentutils.exception;

import ca.bc.gov.open.jag.documentutils.Keys;
import ca.bc.gov.open.jag.documentutils.api.models.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.text.MessageFormat;

/**
 * Global Exception handler for rest controllers
 *
 * @author shaunmillargov
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    // Doc Merge validation errors
    private static String NO_HANDLER_ERROR = "Request URL does not exist";
    private static String UNKNOWN_ERROR = "Unexpected error occurred";
    private static String MISSING_REQUEST_BODY_ERROR = "Required data not found in the request body";


    @ExceptionHandler(MergeException.class)
    public ResponseEntity handleDigitalFormsException(MergeException e, WebRequest request) {

        logger.error("DocMerge Exception occurred", e);
        MDC.clear();

        return new ResponseEntity(new ApiError("MergeException",e.getMessage(), e.getDetails(), request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerException(NoHandlerFoundException e, WebRequest request) {
        logger.error("No Handler Found Exception occurred", e);
        MDC.clear();
        return new ResponseEntity(new ApiError("NO_HANDLER_ERROR","Request URL does not exist.", NO_HANDLER_ERROR, request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        logger.error("Http Message Not Readable Exception occurred", e);
        MDC.clear();
        return new ResponseEntity(new ApiError("MISSING_REQUEST_BODY_ERROR","Invalid payload.", MISSING_REQUEST_BODY_ERROR, request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleDefaultException(Exception e, WebRequest request) {
        logger.error("Exception occurred", e);
        MDC.clear();
        return new ResponseEntity(
                new ApiError(
                        "UNKNOWN_ERROR",
                        "Unknown exception while trying to merge documents.",
                        MessageFormat.format("Exception type: {0}", e.getClass().getName()), request.getHeader(Keys.TRANSACTION_ID)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException e, WebRequest request) {

        logger.error("Validation exception(s) occurred", e);

        MDC.clear();
        return new ResponseEntity(new ApiError("MethodArgumentNotValidException","Unknown exception while trying to merge documents.", e.getMessage(), request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.BAD_REQUEST);

    }

}
