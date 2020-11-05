pdf_merge_request object 
------------------------

options: 
	forcePDFAOnload: 	Forces XFA document types to PDF/A prior to merge. AEM cannot merge XFA documents (at present). 
	createToC: 			Add table of contents to output. 
	
documents: 
	id: 				Document ID. Currently not used by may be used in Future as Bookmark label. 
	docType: 			Must be 'pdf'. No other document types supported yet. Future versions may support MS Word, etc. 
	order: 				Output order. Starts with 1. Second document is 2, and on.  
	data: 				Base64 encoded document. 	
	

pdf_merge_response_success object (enveloped)
--------------------------------------------

resp: success 
data
    document: Base64 encoded reponse
    mimeType: Mime type of output
}


pdf_merge_response_failure object (enveloped)
--------------------------------------------

resp: fail 
error: 
    message: Reason for failure
    httpStatus: Http status of response. 
}

	