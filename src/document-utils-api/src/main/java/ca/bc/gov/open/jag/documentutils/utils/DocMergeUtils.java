package ca.bc.gov.open.jag.documentutils.utils;

import ca.bc.gov.open.jag.documentutils.model.JSONError;
import ca.bc.gov.open.jag.documentutils.model.JSONResponse;

/**
 * 
 * @author shaunmillargov
 *
 */
public class DocMergeUtils {

	

	private  DocMergeUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> JSONResponse<T> buildErrorResponse(String errorMessage, int statusCode) {
		JSONResponse<T> errorResp = new JSONResponse<>();
		errorResp.setResp(DocMergeConstants.JSON_RESPONSE_FAIL);
		JSONError error = new JSONError(errorMessage, statusCode);
		errorResp.setError(error);
		return errorResp;
	}

}
