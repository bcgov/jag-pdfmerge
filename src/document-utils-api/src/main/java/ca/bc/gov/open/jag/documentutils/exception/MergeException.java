package ca.bc.gov.open.jag.documentutils.exception;

/**
 * 
 * Custom exception for Merge Service. 
 * 
 * @author shaunmillargov
 *
 */
public class MergeException extends RuntimeException {

	private static final long serialVersionUID = 5873442413088571528L;

	public MergeException(String message) {
		super(message);
	}

	public MergeException(String message, Throwable cause) {
		super(message, cause);
	}

}
