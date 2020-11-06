package ca.bc.gov.open.pssg.docmerge;

import ca.bc.gov.open.pssg.docmerge.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = "ca.bc.gov.open.pssg.docmerge")
@EnableConfigurationProperties(ConfigProperties.class)
public class DocumentMergeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentMergeServiceApplication.class, args);
	}

}
