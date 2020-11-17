package ca.bc.gov.open.jag.documentutils.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * Error object
 * 
 * @author shaunmillargov
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "message", "httpStatus" })
public class JSONError {

	@JsonProperty("message")
	private String message;

	@JsonProperty("httpStatus")
	private Integer httpStatus;
	
	public JSONError(String message, int code) {
		this.message = message; 
		this.httpStatus = code; 
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("httpStatus")
	public Integer getHttpStatus() {
		return httpStatus;
	}

	@JsonProperty("code")
	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

}
