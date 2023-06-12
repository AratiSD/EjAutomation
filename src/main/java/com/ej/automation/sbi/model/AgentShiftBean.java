package com.ej.automation.sbi.model;

import java.util.Calendar;

public class AgentShiftBean 
{

	private String serviceProviderId;
	private String serviceProviderTypeId;
	private String agentId;
	private String shiftStartTime;
	private int majorMin;
	private int minorMin;
	private int criticalMin;
	private Calendar minorEstimatedDateTime;
	private Calendar majorEstimatedDateTime;
	private Calendar criticalEstimatedDateTime;
	
	private boolean nightFlag = false;
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getShiftStartTime() {
		return shiftStartTime;
	}
	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}
	public int getMajorMin() {
		return majorMin;
	}
	public void setMajorMin(int majorMin) {
		this.majorMin = majorMin;
	}
	public int getCriticalMin() {
		return criticalMin;
	}
	public void setCriticalMin(int criticalMin) {
		this.criticalMin = criticalMin;
	}
	public int getMinorMin() {
		return minorMin;
	}
	public void setMinorMin(int minorMin) {
		this.minorMin = minorMin;
	}
	public Calendar getMinorEstimatedDateTime() {
		return minorEstimatedDateTime;
	}
	public void setMinorEstimatedDateTime(Calendar minorEstimatedDateTime) {
		this.minorEstimatedDateTime = minorEstimatedDateTime;
	}
	public Calendar getMajorEstimatedDateTime() {
		return majorEstimatedDateTime;
	}
	public void setMajorEstimatedDateTime(Calendar majorEstimatedDateTime) {
		this.majorEstimatedDateTime = majorEstimatedDateTime;
	}
	public Calendar getCriticalEstimatedDateTime() {
		return criticalEstimatedDateTime;
	}
	public void setCriticalEstimatedDateTime(Calendar criticalEstimatedDateTime) {
		this.criticalEstimatedDateTime = criticalEstimatedDateTime;
	}
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
	public boolean isNightFlag() {
		return nightFlag;
	}
	public void setNightFlag(boolean nightFlag) {
		this.nightFlag = nightFlag;
	}
	
}
