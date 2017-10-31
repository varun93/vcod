package com.i2india.Batch;

import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.i2india.MessageUtils.Message;
import com.i2india.Service.MessageService;

@Component
public class MailTasklet implements Tasklet {


	@Value("${sendgrid.sender_name}")
	private String sender_name;
	@Value("sendergrid.sender_email")
	private String sender_email ;
	
	private MessageService messageService;


	//UserService userService;

	//	public void setUserService(UserService userService)
	//	{
	//		this.userService = userService;
	//	}

	
	
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();
		//String userID = (String) parameters.get("userID");
		//User user = userService.getUserByID(userID);

		String email = (String) parameters.get("email");

		Message message = new Message();

		message.setSenderName("varun");
		message.setSenderEmail("1hvarun@gmail.com");
		message.setContent("Hey this is a sample message");
		message.setReceiverName("varun");//message.setReceiverName(user.getUsername());
		message.setReceiverEmail(email);//message.setReceiverEmail(user.getEmail());
		message.setSubject("Welcome User");

		try
		{
			messageService.sendMail(message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return RepeatStatus.FINISHED;
		}

		return RepeatStatus.FINISHED;

	}


	//
	//	private MailSender mailSender;
	//
	//
	//
	//	@Override
	//	public RepeatStatus execute(StepContribution contribution,
	//			ChunkContext chunkContext) throws Exception {
	//
	//		Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();
	//		String email = (String) parameters.get("email");
	//		
	//				
	//		SimpleMailMessage msg = new SimpleMailMessage();
	//		
	//		System.out.println("emailId is !!!!" + email);
	//		
	//		 msg.setTo(email);
	//		 //userService.loadUserByEmail(email);//autowired from userService and cached
	//	        msg.setText("Dear customer welcome");
	//	        try{
	//	            this.mailSender.send(msg);
	//	        }
	//	        catch(MailException ex) {
	//	        	System.err.println(ex.getMessage());            
	//	            return RepeatStatus.FINISHED;
	//	            
	//	        }
	//
	//		return RepeatStatus.FINISHED;
	//	}
	//


}
