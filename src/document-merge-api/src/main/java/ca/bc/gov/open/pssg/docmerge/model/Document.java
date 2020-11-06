package ca.bc.gov.open.pssg.docmerge.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "docType", "order", "data" })
public class Document {

	@NotNull
	@JsonProperty("id")
	private String id;
	
	@NotNull
	@JsonProperty("docType")
	private String docType;
	
	@NotNull
	@JsonProperty("order")
	private Integer order;
	
	@NotNull
	@JsonProperty("data")
	private String data;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("docType")
	public String getDocType() {
		return docType;
	}

	@JsonProperty("docType")
	public void setDocType(String docType) {
		this.docType = docType;
	}

	@JsonProperty("order")
	public Integer getOrder() {
		return order;
	}

	@JsonProperty("order")
	public void setOrder(Integer order) {
		this.order = order;
	}

	@JsonProperty("data")
	public String getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(String data) {
		this.data = data;
	}

}
