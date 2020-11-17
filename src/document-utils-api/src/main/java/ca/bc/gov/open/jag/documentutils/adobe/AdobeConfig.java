package ca.bc.gov.open.jag.documentutils.adobe;

import ca.bc.gov.open.jag.documentutils.config.ConfigProperties;
import ca.bc.gov.open.jag.documentutils.utils.DocMergeConstants;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactory;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactoryProperties;
import com.adobe.livecycle.assembler.client.AssemblerServiceClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(ConfigProperties.class)
public class AdobeConfig {

    private final ConfigProperties configProperties;

    public AdobeConfig(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public ServiceClientFactory serviceClientFactory() {

        Properties connectionProps = new Properties();
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_DEFAULT_SOAP_ENDPOINT, configProperties.getAemEndpoint());
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_TRANSPORT_PROTOCOL, ServiceClientFactoryProperties.DSC_SOAP_PROTOCOL);
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_SERVER_TYPE, DocMergeConstants.DSC_SERVER_TYPE_JBOSS);
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_USERNAME, configProperties.getAemUser());
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_PASSWORD, configProperties.getAemPassword());

        return ServiceClientFactory.createInstance(connectionProps);
    }

    @Bean
    public AssemblerServiceClient assemblerClient(ServiceClientFactory serviceClientFactory) {
        return new AssemblerServiceClient(serviceClientFactory);
    }

}
