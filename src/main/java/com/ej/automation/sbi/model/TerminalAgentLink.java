package com.ej.automation.sbi.model;

public class TerminalAgentLink {
	private String faultCategoryId;
	private String majorMins;
	private String minorMins;
	private String criticalMins;
	private String serviceProviderId;
	private String serviceProviderAgentId;

	public String getFaultCategoryId() {
		return faultCategoryId;
	}

	public void setFaultCategoryId(String faultCategoryId) {
		this.faultCategoryId = faultCategoryId;
	}

	public String getMajorMins() {
		return majorMins;
	}

	public void setMajorMins(String majorMins) {
		this.majorMins = majorMins;
	}

	public String getMinorMins() {
		return minorMins;
	}

	public void setMinorMins(String minorMins) {
		this.minorMins = minorMins;
	}

	public String getCriticalMins() {
		return criticalMins;
	}

	public void setCriticalMins(String criticalMins) {
		this.criticalMins = criticalMins;
	}

	public String getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public String getServiceProviderAgentId()
	{
		return serviceProviderAgentId;
	}

	public void setServiceProviderAgentId(String serviceProviderAgentId)
	{
		this.serviceProviderAgentId = serviceProviderAgentId;
	}

}
