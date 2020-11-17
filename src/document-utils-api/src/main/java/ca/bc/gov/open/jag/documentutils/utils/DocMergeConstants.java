package ca.bc.gov.open.jag.documentutils.utils;


/**
 * 
 * Digital Forms Constants. 
 * 
 * @author shaunmillargov
 *
 */
public final class DocMergeConstants {
	
	private DocMergeConstants() {}
    
    public static final String JSON_RESPONSE_SUCCESS = "success";
    public static final String JSON_RESPONSE_FAIL = "fail";
    
    public static final String NOT_PROCESSED_ERROR = "Request cannot be processed. See logging for correlation id %s";
	
	// rest response media type
	public static final String JSON_CONTENT = "application/json";
	
	// mime types
	public static final String PDF_MIME_TYPE = "application/pdf"; 
	
	//DDX related constants
	public static final String DDX_ELEMENT_PDF = "PDF";
	public static final String DDX_ELEMENT_DDX = "DDX"; 
	
	public static final String DDX_OUTPUT_NAME = "out.pdf";
	public static final String DDX_OUTPUT_ATTRIBUTE = "result";
	public static final String DDX_NAMESPACE = "http://ns.adobe.com/DDX/1.0/";
	public static final String DDX_NAMESPACE_ATTRIBUTE = "xmlns";
	public static final String DDX_SOURCE_ATTRIBUTE = "source";
	
	// AEM connection server types 
	public static final String DSC_SERVER_TYPE_JBOSS = "JBoss";
	
	// Doc Merge validation errors
	public static final String NO_HANDLER_ERROR = "Request URL does not exist";
	public static final String UNKNOWN_ERROR = "Unexpected error occured";
	public static final String MISSING_PARAMS_ERROR = "Missing request params";
	public static final String MISSING_REQUEST_BODY_ERROR = "Required data not found in the request body";
 
}
