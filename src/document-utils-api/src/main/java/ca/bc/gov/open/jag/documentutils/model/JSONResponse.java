package ca.bc.gov.open.jag.documentutils.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.open.jag.documentutils.utils.DocMergeConstants;


/**
 * 
 * JSON Enveloped Response Type (Variable object payload) 
 * 
 * @author shaunmillargov
 *
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "resp", "data", "error" })
public class JSONResponse<T> {
	
	@JsonProperty("data")
	private T data; 

	@JsonProperty("resp")
	private String resp = DocMergeConstants.JSON_RESPONSE_SUCCESS; 
	
	@JsonProperty("error")
	private JSONError error;
	
	public JSONResponse() {}
	
	public JSONResponse(T data){
		this.data = data; 
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@JsonProperty("resp")
	public String getResp() {
		return resp;
	}

	@JsonProperty("resp")
	public void setResp(String resp) {
		this.resp = resp;
	}

	@JsonProperty("error")
	public JSONError getError() {
		return error;
	}

	@JsonProperty("error")
	public void setError(JSONError error) {
		this.resp = DocMergeConstants.JSON_RESPONSE_FAIL; 
		this.error = error;
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}
	
}
