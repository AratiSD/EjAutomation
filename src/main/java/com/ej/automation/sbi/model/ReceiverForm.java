package com.ej.automation.sbi.model;

public class ReceiverForm {
	
	private String sequenceNumber;
	private String terminalId;
	private String xfsMessage;
	private String eventOccurredTime;
	private String messageReceivedTime;
	private String terminalNode;
	
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getXfsMessage() {
		return xfsMessage;
	}
	public void setXfsMessage(String xfsMessage) {
		this.xfsMessage = xfsMessage;
	}
	public String getEventOccurredTime() {
		return eventOccurredTime;
	}
	public void setEventOccurredTime(String eventOccurredTime) {
		this.eventOccurredTime = eventOccurredTime;
	}
	public String getMessageReceivedTime() {
		return messageReceivedTime;
	}
	public void setMessageReceivedTime(String messageReceivedTime) {
		this.messageReceivedTime = messageReceivedTime;
	}
	public String getTerminalNode() {
		return terminalNode;
	}
	public void setTerminalNode(String terminalNode) {
		this.terminalNode = terminalNode;
	}
}
