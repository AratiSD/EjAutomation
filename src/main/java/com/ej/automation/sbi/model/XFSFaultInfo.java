package com.ej.automation.sbi.model;

public class XFSFaultInfo {
	
	private String shortName;
	private String faultId;
	private String healthFaultId;
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getFaultId() {
		return faultId;
	}
	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}
	public String getHealthFaultId() {
		return healthFaultId;
	}
	public void setHealthFaultId(String healthFaultId) {
		this.healthFaultId = healthFaultId;
	}
}
