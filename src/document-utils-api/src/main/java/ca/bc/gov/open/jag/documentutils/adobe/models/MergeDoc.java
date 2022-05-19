package ca.bc.gov.open.jag.documentutils.adobe.models;

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
	private String title;

	public MergeDoc(byte[] file, String title) {
		UUID uniqueKey = UUID.randomUUID();
		this.id = uniqueKey.toString();
		this.file = file; 
		this.title = title; 
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public byte[] getFile() { return file; }
	
	public void setFile(byte[] file) {
		this.file = file;
	}
	
}
