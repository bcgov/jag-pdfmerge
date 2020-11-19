package ca.bc.gov.open.jag.documentutils.api;

import ca.bc.gov.open.jag.documentutils.Keys;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeResponse;
import ca.bc.gov.open.jag.documentutils.adobe.MergeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
@Api(tags = "Document Api")
public class DocumentController {

	private final MergeService mergeService;
	
	private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	public DocumentController(MergeService mergeService) {
		this.mergeService = mergeService;
	}

	@ApiOperation("Merge a collection of PDF Document")
	@PostMapping(value = {"/merge" },
			consumes = MediaTypes.APPLICATION_JSON,
			produces = MediaTypes.APPLICATION_JSON)
	public ResponseEntity<DocMergeResponse> mergeDocumentPost(
			@RequestHeader(value = "X-TransactionId", required = true) String transactionId,
			@Valid @RequestBody(required = true) DocMergeRequest request)  {
		
		logger.info("Starting merge process...");

		MDC.put(Keys.TRANSACTION_ID, transactionId);

		ResponseEntity result = ResponseEntity.ok(mergeService.mergePDFDocuments(request, transactionId));

		MDC.remove(Keys.TRANSACTION_ID);

		return result;

	}

}
