package com.i2india.MessageUtils;
import java.io.Serializable;

public class SMSMessage implements Serializable {

	
	private static final long serialVersionUID = 5445700103284704159L;
	private String recipient_number;
	private String message;
	private String delivery_report_url;
	private String scheduled_time;
	private String unicode;
	private String flash;
	private String message_id;
	private String type;
	
	
	public SMSMessage(String recipient_number, String message,
			String delivery_report_url, String scheduled_time, String unicode,
			String flash, String message_id, String type) {
		this.recipient_number = recipient_number;
		this.message = message;
		this.delivery_report_url = delivery_report_url;
		this.scheduled_time = scheduled_time;
		this.unicode = unicode;
		this.flash = flash;
		this.message_id = message_id;
		this.type = type;
	}


	public SMSMessage() {
		// TODO Auto-generated constructor stub
	}


	public String getRecipient_number() {
		return recipient_number;
	}


	public void setRecipient_number(String recipient_number) {
		this.recipient_number = recipient_number;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getDelivery_report_url() {
		return delivery_report_url;
	}


	public void setDelivery_report_url(String delivery_report_url) {
		this.delivery_report_url = delivery_report_url;
	}


	public String getScheduled_time() {
		return scheduled_time;
	}


	public void setScheduled_time(String scheduled_time) {
		this.scheduled_time = scheduled_time;
	}


	public String getUnicode() {
		return unicode;
	}


	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}


	public String getFlash() {
		return flash;
	}


	public void setFlash(String flash) {
		this.flash = flash;
	}


	public String getMessage_id() {
		return message_id;
	}


	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	





}