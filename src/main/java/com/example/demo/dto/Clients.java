package com.example.demo.dto;

public class Clients {
	private int id;
	private String sender_details;
	private String message;
	private String submission_date;
	private String contacts;
	private String ipAddress;
	private String txPort;
	private String systemId;
	private String password;
	private String systemType;
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTxPort() {
		return txPort;
	}
	public void setTxPort(String txPort) {
		this.txPort = txPort;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSender_details() {
		return sender_details;
	}
	public void setSender_details(String sender_details) {
		this.sender_details = sender_details;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubmission_date() {
		return submission_date;
	}
	public void setSubmission_date(String submission_date) {
		this.submission_date = submission_date;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	

}
