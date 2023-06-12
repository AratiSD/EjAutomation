package com.ej.automation.sbi.model;

public class ServiceProviderInfo {

	private String serviceProviderId;
	private String serviceProviderTypeId;
	private String bankId;
	private String smsFlag;
	private String emailFlag;

	public String getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public String getServiceProviderTypeId() {
		return serviceProviderTypeId;
	}

	public void setServiceProviderTypeId(String serviceProviderTypeId) {
		this.serviceProviderTypeId = serviceProviderTypeId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(String emailFlag) {
		this.emailFlag = emailFlag;
	}

}
