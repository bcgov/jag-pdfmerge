package ca.bc.gov.open.jag.documentutils.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;


/**
 * Basic option flags for merge process. 
 * 
 * @author shaunmillargov
 *
 */
@JsonPropertyOrder({ "forcePDFAOnLoad", "createToC", "forceEvenPageCount" })
public class Options {

	@NotNull
	@JsonProperty("forcePDFAOnLoad")
	private boolean forcePDFAOnLoad;

	@NotNull
	@JsonProperty("createToC")
	private boolean createToC;
	
	@NotNull
	@JsonProperty("forceEvenPageCount")
	private boolean forceEvenPageCount;
	

	public boolean isForcePDFAOnLoad() {
		return forcePDFAOnLoad;
	}

	public void setForcePDFAOnLoad(boolean forcePDFAOnLoad) {
		this.forcePDFAOnLoad = forcePDFAOnLoad;
	}

	public boolean isCreateToC() {
		return createToC;
	}

	public void setCreateToC(boolean createToC) {
		this.createToC = createToC;
	}
	
	public boolean isForceEvenPageCount() {
		return forceEvenPageCount;
	}

	public void setForceEvenPageCount(boolean forceEvenPageCount) {
		this.forceEvenPageCount = forceEvenPageCount;
	}
}
