package ca.bc.gov.open.jag.documentutils.adobe;

import ca.bc.gov.open.jag.documentutils.api.models.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeResponse;
import ca.bc.gov.open.jag.documentutils.exception.MergeException;

/**
 * 
 * PDF Merging Service Interface 
 * 
 * @author shaunmillargov
 *
 */
public interface MergeService {

	public DocMergeResponse mergePDFDocuments(DocMergeRequest request, String correlationId) throws MergeException;
	
}




