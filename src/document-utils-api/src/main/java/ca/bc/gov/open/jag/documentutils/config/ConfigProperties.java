package ca.bc.gov.open.jag.documentutils.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * Externalize configuration for easy access to properties either as OpenShift Secrets or local Env variables. 
 * 
 * @author shaunmillargov
 *
 */
@ConfigurationProperties(prefix = "docmerge")
public class ConfigProperties {

	private String serviceApiVersion; 
	private boolean serviceSwaggerEnabled;
	private String aemEndpoint; 
	private String aemUser;
	private String aemPassword;
	
	public String getServiceApiVersion() {
		return serviceApiVersion;
	}

	public void setServiceApiVersion(String serviceApiVersion) {
		this.serviceApiVersion = serviceApiVersion;
	}

	public boolean isServiceSwaggerEnabled() {
		return serviceSwaggerEnabled;
	}

	public void setServiceSwaggerEnabled(boolean serviceSwaggerEnabled) {
		this.serviceSwaggerEnabled = serviceSwaggerEnabled;
	}

	public String getAemEndpoint() {
		return aemEndpoint;
	}

	public void setAemEndpoint(String aemEndpoint) {
		this.aemEndpoint = aemEndpoint;
	}

	public String getAemUser() {
		return aemUser;
	}

	public void setAemUser(String aemUser) {
		this.aemUser = aemUser;
	}

	public String getAemPassword() {
		return aemPassword;
	}

	public void setAemPassword(String aemPassword) {
		this.aemPassword = aemPassword;
	}
	
}
