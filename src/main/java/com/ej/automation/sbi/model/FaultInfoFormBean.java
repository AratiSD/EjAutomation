package com.ej.automation.sbi.model;

public class FaultInfoFormBean {

	private String faultId;
	private String faultDesc;
	private String faultCategoryId;
	private String severity;
	private String healthFaultId;

	public String getFaultId() {
		return faultId;
	}

	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}

	public String getFaultDesc() {
		return faultDesc;
	}

	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}

	public String getFaultCategoryId() {
		return faultCategoryId;
	}

	public void setFaultCategoryId(String faultCategoryId) {
		this.faultCategoryId = faultCategoryId;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getHealthFaultId() {
		return healthFaultId;
	}

	public void setHealthFaultId(String healthFaultId) {
		this.healthFaultId = healthFaultId;
	}

}
