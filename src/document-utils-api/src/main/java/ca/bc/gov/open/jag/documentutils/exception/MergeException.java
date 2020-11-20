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

	private String details;

	public MergeException(String message, Throwable cause) {
		super(message, cause);
		this.details = cause.getMessage();
	}

	public String getDetails() {
		return details;
	}
}
