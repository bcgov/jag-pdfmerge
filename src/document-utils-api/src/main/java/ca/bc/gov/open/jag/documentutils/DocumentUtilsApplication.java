package ca.bc.gov.open.jag.documentutils;

import ca.bc.gov.open.jag.documentutils.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class DocumentUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentUtilsApplication.class, args);
	}

}
