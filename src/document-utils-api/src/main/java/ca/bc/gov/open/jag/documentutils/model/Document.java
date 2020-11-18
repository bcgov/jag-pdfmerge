package ca.bc.gov.open.jag.documentutils.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

@JsonPropertyOrder({ "id", "docType", "order", "data" })
public class Document {

	@NotNull
	@JsonProperty("id")
	private String id;
	
	@NotNull
	@JsonProperty("order")
	private int order;
	
	@NotNull
	@JsonProperty("data")
	private String data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
