package ca.bc.gov.open.jag.documentutils.service;

import ca.bc.gov.open.jag.documentutils.model.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.model.DocMergeResponse;
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




