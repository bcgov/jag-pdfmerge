package ca.bc.gov.open.jag.documentutils.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;

import java.util.List;


@JsonPropertyOrder({ "options", "documents" })
public class DocMergeRequest {

	@JsonProperty("options")
	@Valid
	private Options options;
	
	@JsonProperty("documents")
	@Valid
	private List<Document> documents = null;

	@JsonProperty("options")
	public Options getOptions() {
		return options;
	}

	@JsonProperty("options")
	public void setOptions(Options options) {
		this.options = options;
	}

	@JsonProperty("documents")
	public List<Document> getDocuments() {
		return documents;
	}

	@JsonProperty("documents")
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

}
