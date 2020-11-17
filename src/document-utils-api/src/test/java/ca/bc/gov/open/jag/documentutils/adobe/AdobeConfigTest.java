package ca.bc.gov.open.jag.documentutils.adobe;

import com.adobe.idp.dsc.clientsdk.ServiceClientFactory;
import com.adobe.livecycle.assembler.client.AssemblerServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdobeConfigTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withPropertyValues(
                    "docmerge.aem-endpoint=localhost",
                    "docmerge.aem-user=user",
                    "docmerge.aem-password=pwd"
            )
            .withUserConfiguration(AdobeConfig.class);

    @Test
    public void testConfiguration() {


        context.run(it -> {

            assertThat(it).hasSingleBean(ServiceClientFactory.class);
            assertThat(it).hasSingleBean(AssemblerServiceClient.class);

        });

    }

}
