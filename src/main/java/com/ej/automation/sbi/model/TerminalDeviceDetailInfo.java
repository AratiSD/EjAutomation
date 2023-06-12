package com.ej.automation.sbi.model;

public class TerminalDeviceDetailInfo {
	
	private String terminalId;
	private String deviceName;
	private String deviceStatus;
	private String healthStatus;
	private String finalDeviceName;
	private String bankId;
	
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public String getHealthStatus() {
		return healthStatus;
	}
	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}
	public String getFinalDeviceName() {
		return finalDeviceName;
	}
	public void setFinalDeviceName(String finalDeviceName) {
		this.finalDeviceName = finalDeviceName;
	}
	public TerminalDeviceDetailInfo() {
		this.deviceStatus = "-";
		this.healthStatus = "-";
		this.finalDeviceName = "-";
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

}
