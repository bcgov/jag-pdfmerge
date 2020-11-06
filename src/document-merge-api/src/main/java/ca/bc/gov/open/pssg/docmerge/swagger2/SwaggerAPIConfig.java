package ca.bc.gov.open.pssg.docmerge.swagger2;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;

import springfox.documentation.swagger2.web.Swagger2Controller;

/**
 * 
 * Do not tamper with this class. 
 * 
 * Forces application/json content-type due to problem with Swagger 2.9.2 when calling http(s)://host/digitalforms/v2/api-docs 
 * @See https://github.com/springfox/springfox/issues/1835
 *
 */
@Configuration
public class SwaggerAPIConfig {

	private static final List<MediaType> SWAGGER_MEDIA_TYPES = Arrays.asList(MediaType.APPLICATION_JSON,
			MediaType.valueOf("application/hal+json"));

	/**
	 * Avoid swagger api being mapped to XML with jackson xml on the class path
	 *
	 * @param manager     Using the ready built content negotiation manager instead
	 *                    of
	 *                    {@link org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer}
	 *                    to not mess with other configurations
	 *                    
	 * @param swaggerPath The path on which the swagger specs are served
	 */
	@SuppressWarnings("el-syntax")
	@Autowired
	public void addSpringfoxContentNegotiation(ContentNegotiationManager manager,
			@Value("${springfox.documentation.swagger.v2.path:" + Swagger2Controller.DEFAULT_URL
					+ "}") String swaggerPath) {
		manager.getStrategies().add(0, webRequest -> {
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			if (request != null && request.getServletPath().startsWith(swaggerPath)) {
				return SWAGGER_MEDIA_TYPES;
			}
			return ContentNegotiationStrategy.MEDIA_TYPE_ALL_LIST;
		});
	}
}
