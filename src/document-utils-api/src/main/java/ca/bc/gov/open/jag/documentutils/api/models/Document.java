package ca.bc.gov.open.jag.documentutils.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({ "index", "data", "title" })
public class Document {
	
	@NotNull
	@JsonProperty("index")
	private int index;
	
	@NotNull
	@JsonProperty("data")
	private String data;
	
	@JsonProperty("title")
	private String title;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
