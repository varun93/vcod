package com.i2india.serviceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.i2india.MessageUtils.Message;
import com.i2india.MessageUtils.SMSMessage;
import com.i2india.MessageUtils.SendGridParameter;
import com.i2india.Service.MessageService;

@Service
public class MessageServiceImpl implements MessageService{

	Logger logger = Logger.getLogger("myLogger");
	private RestTemplate restTemplate = new RestTemplate();

	@Value("${sendgrid.api.user}")
	private String sendgridApiUser;

	@Value("${sendgrid.api.key}")
	private String sendgridApiKey;

	@Override
	public void sendMail(Message message) throws Exception {

		try
		{
			MultiValueMap<String, Object> vars = new LinkedMultiValueMap<String, Object>();
			
			
			vars.add(SendGridParameter.API_USER, "vinay.rao");
			vars.add(SendGridParameter.API_KEY, "Vinayrao@321");
			vars.add(SendGridParameter.SENDER_NAME, message.getSenderName());
			vars.add(SendGridParameter.SENDER_EMAIL, message.getSenderEmail());
			vars.add(SendGridParameter.SUBJECT, message.getSubject());
			vars.add(SendGridParameter.TEXT, message.getContent());
			vars.add(SendGridParameter.RECEIVER_EMAIL, message.getReceiverEmail());
			vars.add(SendGridParameter.RECEIVER_NAME, message.getReceiverName());
            
			
			restTemplate.postForLocation(SendGridParameter.URL, vars);

		}
		
		catch (Exception ex) {
			logger.error(ex);
			throw new Exception(ex);
		}
		
		
	}

	@Override
	public void sendSMS(SMSMessage message) throws Exception{
		// TODO Auto-generated method stub
		
	}


}
