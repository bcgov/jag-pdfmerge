package ca.bc.gov.open.jag.documentutils.model;

import java.util.UUID;

/**
 * 
 * Represents a single input document for merging.
 * 
 * @author shaunmillargov
 *
 */
public class MergeDoc {
	
	private String id; 
	private byte[] file;
	private String errorCd = null;
	private String errorMsg = null;

	public MergeDoc(byte[] file) {
		UUID uniqueKey = UUID.randomUUID();
		this.id = uniqueKey.toString();
		this.file = file; 
	}
	
	public String getId() {
		return id;
	}
	
	public byte[] getFile() {
		return file;
	}
	
	public String getErrorCd() {
		return errorCd;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
