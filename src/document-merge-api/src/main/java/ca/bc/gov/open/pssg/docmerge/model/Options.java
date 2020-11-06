package ca.bc.gov.open.pssg.docmerge.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Basic option flags for merge process. 
 * 
 * @author shaunmillargov
 *
 */
@JsonPropertyOrder({ "forcePDFAOnLoad", "createToC" })
public class Options {
	
	@NotNull
	@Pattern(regexp = "^true$|^false$", message = "forcePDFAOnLoad - allowed input: true or false")
	@JsonProperty("forcePDFAOnLoad")
	private String forcePDFAOnLoad;

	@NotNull
	@Pattern(regexp = "^true$|^false$", message = "createToC - allowed input: true or false")
	@JsonProperty("createToC")
	private String createToC;

	@JsonProperty("forcePDFAOnLoad")
	public boolean getForcePDFAOnLoad() {
		return Boolean.parseBoolean(forcePDFAOnLoad);
	}

	@JsonProperty("forcePDFAOnLoad")
	public void setForcePDFAOnLoad(String forcePDFAOnLoad) {
		this.forcePDFAOnLoad = forcePDFAOnLoad;
	}

	@JsonProperty("createToC")
	public boolean getCreateToC() {
		return Boolean.parseBoolean(createToC);
	}

	@JsonProperty("createToC")
	public void setCreateToC(String createToC) {
		this.createToC = createToC;
	}
	
	
	
}
