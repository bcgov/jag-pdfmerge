package ca.bc.gov.open.pssg.docmerge.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * Custom exception for Merge Service. 
 * 
 * @author shaunmillargov
 *
 */
public class MergeException extends Exception {

	private static final long serialVersionUID = 5873442413088571528L;

	private final HttpStatus httpStatus;

	public MergeException(String message, HttpStatus status) {
		super(message);
		this.httpStatus = status;
	}

	public MergeException(String message, HttpStatus status, Throwable cause) {
		super(message, cause);
		this.httpStatus = status;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
