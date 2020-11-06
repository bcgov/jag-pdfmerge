package ca.bc.gov.open.pssg.docmerge.swagger2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.base.Predicates;

import ca.bc.gov.open.pssg.docmerge.config.ConfigProperties;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * Swagger2 config
 * 
 * Note the use of the ConditionOnProperty for enabling / disabling Swagger2 from properties file.  
 * 
 * @author shaunmillargov
 *
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "docmerge",  name = "service-swagger-enabled")
@EnableConfigurationProperties(ConfigProperties.class)
public class SwaggerConfig implements WebMvcConfigurer {
	
	@Autowired
	private ConfigProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    @Bean
    public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("ca.bc.gov.open.pssg.docmerge"))
                .paths(Predicates.not(PathSelectors.regex("/\\*\\*/.*/\\*\\*"))) // Ignore default path mappings
                .build();
    }

    private ApiInfo getApiInfo() {

        return new ApiInfoBuilder()
                .title("Document Merge RESTful Service")
                .description("Document Merge API")
                .version(properties.getServiceApiVersion())
                .build();
    }
}
