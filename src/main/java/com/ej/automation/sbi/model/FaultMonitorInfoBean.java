package com.ej.automation.sbi.model;

import java.sql.Timestamp;

public class FaultMonitorInfoBean {
	
	private String bankId;
	private String terminalName;
	private String terminalId;
	private String countryId;
	private String stateId;
	private String cityId;
	private String branchId;
	private String regionId;
	private String faultId;
	private String faultDescription;
	private Timestamp faultOccuredTime;
	private String faultStatus;
	private String dateId;
	private String faultCategoryId;
	
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getFaultId() {
		return faultId;
	}
	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}
	public String getFaultDescription() {
		return faultDescription;
	}
	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}
	public Timestamp getFaultOccuredTime() {
		return faultOccuredTime;
	}
	public void setFaultOccuredTime(Timestamp faultOccuredTime) {
		this.faultOccuredTime = faultOccuredTime;
	}
	public String getFaultStatus() {
		return faultStatus;
	}
	public void setFaultStatus(String faultStatus) {
		this.faultStatus = faultStatus;
	}
	public String getDateId() {
		return dateId;
	}
	public void setDateId(String dateId) {
		this.dateId = dateId;
	}
	public String getFaultCategoryId() {
		return faultCategoryId;
	}
	public void setFaultCategoryId(String faultCategoryId) {
		this.faultCategoryId = faultCategoryId;
	}
	

}
