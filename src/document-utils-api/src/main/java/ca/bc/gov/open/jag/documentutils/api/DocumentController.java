package ca.bc.gov.open.jag.documentutils.api;

import ca.bc.gov.open.jag.documentutils.Keys;
import ca.bc.gov.open.jag.documentutils.adobe.AemService;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeRequest;
import ca.bc.gov.open.jag.documentutils.api.models.DocMergeResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
@Scope("request")
public class DocumentController {

	private final AemService aemService;
	
	private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	public DocumentController(AemService aemService) {
		this.aemService = aemService;
	}

	@PostMapping(value = {"/merge" },
			consumes = MediaTypes.APPLICATION_JSON,
			produces = MediaTypes.APPLICATION_JSON)
	@RolesAllowed({Keys.API_ROLE_NAME})
	public ResponseEntity<DocMergeResponse> mergeDocumentPost(
			@RequestHeader(value = "X-Correlation-ID", required = true) String correlationId,
			@RequestHeader(value = "X-Client-ID", required = true) String clientId,
			@Valid @RequestBody(required = true) DocMergeRequest request)  {
		
		logger.info("Starting merge process...");

		MDC.put(Keys.CORRELATION_ID, correlationId);
		MDC.put(Keys.CLIENT_ID, clientId);
		MDC.put(Keys.OPERATION, "MergeDocuments");

		ResponseEntity result = ResponseEntity.ok(aemService.mergePDFDocuments(request, correlationId));

		MDC.remove(Keys.CORRELATION_ID);
		MDC.remove(Keys.CLIENT_ID);
		MDC.remove(Keys.OPERATION);

		return result;

	}

}
