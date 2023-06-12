package com.ej.automation.sbi.model;

import java.sql.Timestamp;

public class TransactionForm {

	private String terminalId;
	private String startDate;
	private int startingSequenceNumber;
	private int endingSequenceNumber;
	private String missingSequenceNumber;
	private int totaltxnCount;
	private int financialtxnCount;
	private int nonFinancialtxnCount;
	private String downloadType;
	private String fileName;
	private long fileSize;
	private Timestamp sponDate;
	private String terminalName;
	
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public Timestamp getSponDate() {
		return sponDate;
	}
	public void setSponDate(Timestamp sponDate) {
		this.sponDate = sponDate;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long l) {
		this.fileSize = l;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getMissingSequenceNumber() {
		return missingSequenceNumber;
	}
	public void setMissingSequenceNumber(String missingSequenceNumber) {
		this.missingSequenceNumber = missingSequenceNumber;
	}
	public int getTotaltxnCount() {
		return totaltxnCount;
	}
	public void setTotaltxnCount(int totaltxnCount) {
		this.totaltxnCount = totaltxnCount;
	}
	public int getFinancialtxnCount() {
		return financialtxnCount;
	}
	public void setFinancialtxnCount(int financialtxnCount) {
		this.financialtxnCount = financialtxnCount;
	}
	public int getNonFinancialtxnCount() {
		return nonFinancialtxnCount;
	}
	public void setNonFinancialtxnCount(int nonFinancialtxnCount) {
		this.nonFinancialtxnCount = nonFinancialtxnCount;
	}
	public int getStartingSequenceNumber() {
		return startingSequenceNumber;
	}
	public void setStartingSequenceNumber(int startingSequenceNumber) {
		this.startingSequenceNumber = startingSequenceNumber;
	}
	public int getEndingSequenceNumber() {
		return endingSequenceNumber;
	}
	public void setEndingSequenceNumber(int endingSequenceNumber) {
		this.endingSequenceNumber = endingSequenceNumber;
	}
	public String getDownloadType() {
		return downloadType;
	}
	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}
	@Override
	public String toString() {
		return "TransactionForm [terminalId=" + terminalId + ", startDate=" + startDate + ", startingSequenceNumber="
				+ startingSequenceNumber + ", endingSequenceNumber=" + endingSequenceNumber + ", missingSequenceNumber="
				+ missingSequenceNumber + ", totaltxnCount=" + totaltxnCount + ", financialtxnCount="
				+ financialtxnCount + ", nonFinancialtxnCount=" + nonFinancialtxnCount + ", downloadType="
				+ downloadType + ", fileName=" + fileName + ", fileSize=" + fileSize + ", sponDate=" + sponDate
				+ ", terminalName=" + terminalName + "]";
	}
}
