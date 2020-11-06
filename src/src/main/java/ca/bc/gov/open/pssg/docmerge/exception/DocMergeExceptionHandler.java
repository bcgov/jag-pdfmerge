package ca.bc.gov.open.pssg.docmerge.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

import ca.bc.gov.open.pssg.docmerge.model.JSONResponse;
import ca.bc.gov.open.pssg.docmerge.utils.DocMergeConstants;
import ca.bc.gov.open.pssg.docmerge.utils.DocMergeUtils;

/**
 * Global Exception handler for rest controllers
 * 
 * @author shaunmillargov
 */
@ControllerAdvice
public class DocMergeExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(DocMergeExceptionHandler.class);

	@ExceptionHandler(MergeException.class)
	public ResponseEntity<JSONResponse<String>> handleDigitalFormsException(MergeException e,
			WebRequest request) {
		logger.error("DocMerge Exception occurred", e);
		MDC.clear();
		return new ResponseEntity<>(DocMergeUtils.buildErrorResponse(e.getMessage(), e.getHttpStatus().value()),
				e.getHttpStatus());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<JSONResponse<String>> handleNoHandlerException(NoHandlerFoundException e,
			WebRequest request) {
		logger.error("No Handler Found Exception occurred", e);
		MDC.clear();
		return new ResponseEntity<>(DocMergeUtils.buildErrorResponse(DocMergeConstants.NO_HANDLER_ERROR,
				HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<JSONResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
			WebRequest request) {
		logger.error("Http Message Not Readable Exception occurred", e);
		MDC.clear();
		return new ResponseEntity<>(DocMergeUtils.buildErrorResponse(
				DocMergeConstants.MISSING_REQUEST_BODY_ERROR, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<JSONResponse<String>> handleDefaultException(Exception e, WebRequest request) {
		logger.error("Exception occurred", e);
		MDC.clear();
		return new ResponseEntity<>(DocMergeUtils.buildErrorResponse(DocMergeConstants.UNKNOWN_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<JSONResponse<String>> handleValidationExceptions(MethodArgumentNotValidException e) {
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
	    	if (c > 0 ) buffer.append(", ");
	        buffer.append((entry.getKey() + ": " + entry.getValue()));
	        c++;
	    };
		
		MDC.clear();
		return new ResponseEntity<>(DocMergeUtils.buildErrorResponse(buffer.toString(),
				HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
	}

}
