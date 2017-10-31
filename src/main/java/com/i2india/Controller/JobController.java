package com.i2india.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JobController {

	private static final Logger logger = Logger.getLogger(JobController.class);
	

	@Autowired 
	private JobLauncher jobLauncher;
	@Autowired
	@Qualifier("mailJob")
	private Job job;
	
	@Autowired
	@Qualifier("dataJob")
	private Job dataJob;
	
	
	
	@Value("${sendgrid.api.user}")
	private String sendgridApiUser;

	@Value("${sendgrid.api.key}")
	private String sendgridApiKey;
      

	public void setLauncher(JobLauncher launcher) {
		this.jobLauncher = launcher;
	}

	public void setJob(Job job) {
		this.job = job;
	}///

	@RequestMapping(value="/mail")
	public @ResponseBody String sendProcess()
	{
       
		
		//do all the validation 
	
		System.out.println("Sendgrid Api User is" + sendgridApiUser);
		System.out.println("Sendgrid Api Key is" + sendgridApiKey);
		
		Map<String,JobParameter> parameters = new HashMap<String,JobParameter>();
		
	
	     parameters.put("email", new JobParameter("hegdevarun93@gmail.com"));
		//parameters.put("userID",new JobParameter("12345"));

		try {
			jobLauncher.run(job, new JobParameters(parameters));
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "job already running";
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "job restarting";
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "jon alredy completed";
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed for unknown reasons";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "failure";
		}

		return "success";
	}

	
	
	@RequestMapping(value="/fetch")
	public @ResponseBody String dataFetch()
	{
		Map<String,JobParameter> parameters = new HashMap<String,JobParameter>();
		
		
		parameters.put("date", new JobParameter(new Date()));

		try {
			jobLauncher.run(dataJob, new JobParameters(parameters));
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "job already running";
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "job restarting";
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "jon alredy completed";
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed for unknown reasons";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "failure";
		}

		return "success";
	}
	
}
