package com.i2india.Batch;

import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.i2india.MessageUtils.SMSMessage;
import com.i2india.Service.MessageService;

@Component
public class SMSTasklet implements Tasklet {


	@Value("${sendgrid.sender_name}")
	private String sender_name;
	@Value("sendergrid.sender_email")
	private String sender_email ;

	private MessageService messageService;

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();
		//String userID = (String) parameters.get("userID");
		//User user = userService.getUserByID(userID);

		SMSMessage message = new SMSMessage();
		try
		{
			messageService.sendSMS(message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return RepeatStatus.FINISHED;
		}

		return RepeatStatus.FINISHED;
	}


	
}
