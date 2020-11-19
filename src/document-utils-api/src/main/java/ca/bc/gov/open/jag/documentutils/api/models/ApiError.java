package ca.bc.gov.open.jag.documentutils.api.models;

public class ApiError {

    private String error;

    private String message;

    private String detail;

    private String transactionId;

    public ApiError(String error, String message, String detail, String transactionId) {
        this.error = error;
        this.message = message;
        this.detail = detail;
        this.transactionId = transactionId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

}
