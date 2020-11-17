package ca.bc.gov.open.jag.documentutils.api;

import ca.bc.gov.open.jag.documentutils.exception.MergeException;
import ca.bc.gov.open.jag.documentutils.model.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.model.DocMergeResponse;
import ca.bc.gov.open.jag.documentutils.model.JSONResponse;
import ca.bc.gov.open.jag.documentutils.service.MergeService;
import ca.bc.gov.open.jag.documentutils.utils.DocMergeConstants;
import ca.bc.gov.open.jag.documentutils.utils.DocMergeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@RequestMapping("document")
public class DocumentController {

	private final MergeService mergeService;
	
	private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	public DocumentController(MergeService mergeService) {
		this.mergeService = mergeService;
	}

	@PostMapping(value = {"/merge" },
			consumes = DocMergeConstants.JSON_CONTENT,
			produces = DocMergeConstants.JSON_CONTENT)
	public ResponseEntity<JSONResponse<DocMergeResponse>> mergeDocumentPost(
			@RequestHeader(value = "X-TransactionId", required = true) String transactionId,
			@Valid @RequestBody(required = true) DocMergeRequest request)  {
		
		logger.info("Starting merge process...");

		try {
			
			DocMergeResponse mergResp = mergeService.mergePDFDocuments(request, transactionId);
			JSONResponse<DocMergeResponse> resp = new JSONResponse<>(mergResp);
			logger.info("Merge process complete.");
			return new ResponseEntity<>(resp, HttpStatus.OK);
			
		} catch (MergeException e) {

			logger.error(MessageFormat.format("Document Merge encountered an error: {0}", e.getMessage()), e);
			return new ResponseEntity<>(
					DocMergeUtils.buildErrorResponse(String.format(DocMergeConstants.NOT_PROCESSED_ERROR, transactionId), 500),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
