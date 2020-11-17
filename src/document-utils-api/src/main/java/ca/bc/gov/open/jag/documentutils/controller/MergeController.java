package ca.bc.gov.open.jag.documentutils.controller;

import javax.validation.Valid;

import ca.bc.gov.open.jag.documentutils.exception.MergeException;
import ca.bc.gov.open.jag.documentutils.model.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.model.DocMergeResponse;
import ca.bc.gov.open.jag.documentutils.model.JSONResponse;
import ca.bc.gov.open.jag.documentutils.service.MergeService;
import ca.bc.gov.open.jag.documentutils.utils.DocMergeConstants;
import ca.bc.gov.open.jag.documentutils.utils.DocMergeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

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

	private final MergeService mergeService;
	
	private final Logger logger = LoggerFactory.getLogger(MergeController.class);

	public MergeController(MergeService mergeService) {
		this.mergeService = mergeService;
	}

	@PostMapping(value = {"/merge/{correlationId}" }, 
			consumes = DocMergeConstants.JSON_CONTENT,
			produces = DocMergeConstants.JSON_CONTENT)
	public ResponseEntity<JSONResponse<DocMergeResponse>> mergeDocumentPost(
			@PathVariable(value = "correlationId", required = true) String correlationId, 
			@Valid @RequestBody(required = true) DocMergeRequest request)  {
		
		logger.info("Starting merge process...");

		try {
			
			DocMergeResponse mergResp = mergeService.mergePDFDocuments(request, correlationId);
			JSONResponse<DocMergeResponse> resp = new JSONResponse<>(mergResp);
			logger.info("Merge process complete.");
			return new ResponseEntity<>(resp, HttpStatus.OK);
			
		} catch (MergeException e) {

			logger.error(MessageFormat.format("Document Merge encountered an error: {0}", e.getMessage()), e);
			return new ResponseEntity<>(
					DocMergeUtils.buildErrorResponse(String.format(DocMergeConstants.NOT_PROCESSED_ERROR, correlationId), 500),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
