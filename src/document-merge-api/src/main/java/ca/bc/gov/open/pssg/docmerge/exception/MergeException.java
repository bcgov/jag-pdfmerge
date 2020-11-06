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

	public MergeException(String message) {
		super(message);
	}

	public MergeException(String message, Throwable cause) {
		super(message, cause);
	}

}
