package com.i2india.Service;

import com.i2india.MessageUtils.Message;
import com.i2india.MessageUtils.SMSMessage;

public interface MessageService {

	public void sendMail(Message message) throws Exception;
	public void sendSMS(SMSMessage  message) throws Exception;
	
	
}
