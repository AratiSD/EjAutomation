package com.ej.automation.sbi.model;

public class CashInfoForm {
	
	public CashInfoForm(int hopperPosition) {
		this.hopperPosition = "" + hopperPosition;
	}
	
	private String seqNo;
	private String terminalId;
	private String currencyId;
	private String denomination;
	private String balance;
	private String hopperPosition;
	private String rejectedBalance;
	private String updateDate;
	private String bankId;
	private String noOfNotes;
	private String cassetteType;
	private String terminalName;
	private String cassetteName;
	
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getHopperPosition() {
		return hopperPosition;
	}
	public void setHopperPosition(String hopperPosition) {
		this.hopperPosition = hopperPosition;
	}
	public String getRejectedBalance() {
		return rejectedBalance;
	}
	public void setRejectedBalance(String rejectedBalance) {
		this.rejectedBalance = rejectedBalance;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getNoOfNotes() {
		return noOfNotes;
	}
	public void setNoOfNotes(String noOfNotes) {
		this.noOfNotes = noOfNotes;
	}
	public String getCassetteType() {
		return cassetteType;
	}
	public void setCassetteType(String cassetteType) {
		this.cassetteType = cassetteType;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String toString()
	{
		return seqNo + " - " + terminalId + " - " + currencyId + " - " + denomination  + " - " + balance  + " - " + 
			hopperPosition  + " - " + rejectedBalance  + " - " + updateDate + " - " + bankId;
	}
	public String getCassetteName() {
		return cassetteName;
	}
	public void setCassetteName(String cassetteName) {
		this.cassetteName = cassetteName;
	}
}
