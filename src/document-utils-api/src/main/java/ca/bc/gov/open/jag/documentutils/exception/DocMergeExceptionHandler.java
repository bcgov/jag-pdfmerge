package ca.bc.gov.open.jag.documentutils.exception;

import ca.bc.gov.open.jag.documentutils.Keys;
import ca.bc.gov.open.jag.documentutils.model.ApiError;
import ca.bc.gov.open.jag.documentutils.utils.DocMergeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Global Exception handler for rest controllers
 *
 * @author shaunmillargov
 */
@ControllerAdvice
public class DocMergeExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(DocMergeExceptionHandler.class);

    @ExceptionHandler(MergeException.class)
    public ResponseEntity handleDigitalFormsException(MergeException e, WebRequest request) {

        logger.error("DocMerge Exception occurred", e);
        MDC.clear();

        return new ResponseEntity(new ApiError("MergeException","Unknown exception while trying to merge documents.", e.getMessage(), request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerException(NoHandlerFoundException e, WebRequest request) {
        logger.error("No Handler Found Exception occurred", e);
        MDC.clear();
        return new ResponseEntity(new ApiError("NO_HANDLER_ERROR","Unknown exception while trying to merge documents.", DocMergeConstants.NO_HANDLER_ERROR, request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        logger.error("Http Message Not Readable Exception occurred", e);
        MDC.clear();
        return new ResponseEntity(new ApiError("MISSING_REQUEST_BODY_ERROR","Invalid payload.", DocMergeConstants.MISSING_REQUEST_BODY_ERROR, request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleDefaultException(Exception e, WebRequest request) {
        logger.error("Exception occurred", e);
        MDC.clear();
        return new ResponseEntity(new ApiError("UNKNOWN_ERROR","Unknown exception while trying to merge documents.", DocMergeConstants.UNKNOWN_ERROR, request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException e, WebRequest request) {

        logger.error("Validation exception(s) occurred", e);

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        StringBuffer buffer = new StringBuffer();
        int c = 0;
        for (Entry<String, String> entry : errors.entrySet()) {
            if (c > 0) buffer.append(", ");
            buffer.append((entry.getKey() + ": " + entry.getValue()));
            c++;
        }
        ;

        MDC.clear();
        return new ResponseEntity(new ApiError("MethodArgumentNotValidException","Unknown exception while trying to merge documents.", buffer.toString(), request.getHeader(Keys.TRANSACTION_ID)), HttpStatus.BAD_REQUEST);

    }

}
