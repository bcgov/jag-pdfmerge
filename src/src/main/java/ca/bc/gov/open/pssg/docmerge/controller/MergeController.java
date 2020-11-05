package ca.bc.gov.open.pssg.docmerge.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.open.pssg.docmerge.exception.MergeException;
import ca.bc.gov.open.pssg.docmerge.model.JSONResponse;
import ca.bc.gov.open.pssg.docmerge.model.DocMergeRequest;
import ca.bc.gov.open.pssg.docmerge.model.DocMergeResponse;
import ca.bc.gov.open.pssg.docmerge.service.MergeService;
import ca.bc.gov.open.pssg.docmerge.utils.DocMergeConstants;
import ca.bc.gov.open.pssg.docmerge.utils.DocMergeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Main RESTful controller. 
 * 
 * @author shaunmillargov
 *
 */

@RestController
@Validated
public class MergeController {
	
	@Autowired
	private MergeService mergeService;
	
	private final Logger logger = LoggerFactory.getLogger(MergeController.class);

	@PostMapping(value = {"/merge/{correlationId}" }, 
			consumes = DocMergeConstants.JSON_CONTENT, 
			produces = DocMergeConstants.JSON_CONTENT)
	public ResponseEntity<JSONResponse<DocMergeResponse>> mergeDocumentPost(
			@PathVariable(value = "correlationId", required = true) String correlationId, 
			@Valid @RequestBody(required = true)  DocMergeRequest request)  {
		
		logger.info("Starting merge process...");
		
		try {
			
			DocMergeResponse mergResp = mergeService.mergePDFDocuments(request, correlationId);
			JSONResponse<DocMergeResponse> resp = new JSONResponse<>(mergResp);
			logger.info("Merge process complete.");
			return new ResponseEntity<>(resp, HttpStatus.OK);
			
		} catch (MergeException e) {
			
			e.printStackTrace();
			logger.error("Document Merge encountered an error " + e.getMessage());
			return new ResponseEntity<>(
					DocMergeUtils.buildErrorResponse(String.format(DocMergeConstants.NOT_PROCESSED_ERROR, correlationId), 404),
					HttpStatus.NOT_FOUND);
		}
	}

}
