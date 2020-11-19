package ca.bc.gov.open.jag.documentutils.swagger2;

import com.adobe.idp.dsc.clientsdk.ServiceClientFactory;
import com.adobe.livecycle.assembler.client.AssemblerServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import springfox.documentation.spring.web.plugins.Docket;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SwaggerConfigTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withPropertyValues(
                    "docmerge.service-swagger-enabled=true"
            )
            .withUserConfiguration(SwaggerConfig.class);

    @Test
    public void testConfiguration() {


        context.run(it -> {

            assertThat(it).hasSingleBean(Docket.class);

        });

    }
}
