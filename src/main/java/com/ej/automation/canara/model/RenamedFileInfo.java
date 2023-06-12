package com.ej.automation.canara.model;

import java.sql.Timestamp;


public class RenamedFileInfo {
	private String ejRenamedDate;
    private String fileName;
    private long fileSize;
	private String terminalId;
    private String terminalName;
    private Timestamp renamedOn;
	
    public String getEjRenamedDate() {
		return ejRenamedDate;
	}
	public void setEjRenamedDate(String ejRenamedDate) {
		this.ejRenamedDate = ejRenamedDate;
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
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public Timestamp getRenamedOn() {
		return renamedOn;
	}
	public void setRenamedOn(Timestamp renamedOn) {
		this.renamedOn = renamedOn;
	}
    
    
}
