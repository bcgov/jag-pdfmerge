package ca.bc.gov.open.jag.documentutils.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError {

    private String error;

    private String message;

    private String detail;

    private String transactionId;

    @JsonCreator
    public ApiError(
            @JsonProperty("error") String error,
            @JsonProperty("message") String message,
            @JsonProperty("detail") String detail,
            @JsonProperty("transactionId") String transactionId) {
        this.error = error;
        this.message = message;
        this.detail = detail;
        this.transactionId = transactionId;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public String getTransactionId() {
        return transactionId;
    }

}
