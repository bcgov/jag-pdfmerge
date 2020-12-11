package ca.bc.gov.open.jag.documentutils.adobe;

import com.adobe.idp.dsc.clientsdk.ServiceClientFactory;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactoryProperties;
import com.adobe.livecycle.assembler.client.AssemblerServiceClient;
import com.adobe.livecycle.docconverter.client.DocConverterServiceClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(AemProperties.class)
public class AdobeConfig {

    private final AemProperties aemProperties;

    public AdobeConfig(AemProperties aemProperties) {
        this.aemProperties = aemProperties;
    }

    @Bean
    @Scope("request")
    public ServiceClientFactory serviceClientFactory() {

        Properties connectionProps = new Properties();
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_DEFAULT_SOAP_ENDPOINT, aemProperties.getEndpoint());
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_TRANSPORT_PROTOCOL, ServiceClientFactoryProperties.DSC_SOAP_PROTOCOL);
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_SERVER_TYPE, AdobeKeys.DSC_SERVER_TYPE_JBOSS);
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_USERNAME, aemProperties.getUsername());
        connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_PASSWORD, aemProperties.getPassword());

        return ServiceClientFactory.createInstance(connectionProps);
    }

    @Bean
    @Scope("request")
    public AssemblerServiceClient assemblerClient(ServiceClientFactory serviceClientFactory) {
        return new AssemblerServiceClient(serviceClientFactory);
    }

    @Bean
    @Scope("request")
    public DocConverterServiceClient docConverterServiceClient(ServiceClientFactory serviceClientFactory) {
        return new DocConverterServiceClient(serviceClientFactory);
    }

    @Bean
    public DocumentBuilderFactory documentBuilderFactory() {
        // Create DocumentBuilderFactory and DocumentBuilder objects
        return DocumentBuilderFactory.newInstance();
    }

    @Bean
    public TransformerFactory transformerFactory() {
        return TransformerFactory.newInstance();
    }

}
