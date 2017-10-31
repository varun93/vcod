package com.i2india.Batch;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RunScheduler {

	@Autowired
	@Qualifier(value="jobLauncher")
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier(value="dataJob")
	private Job job;

	
	
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}



	public void setJob(Job job) {
		this.job = job;
	}



	public void run() {

		try {

			String dateParam = new Date().toString();
			JobParameters param = new JobParametersBuilder().addString("date",dateParam).toJobParameters();
			JobExecution execution = jobLauncher.run(job, param);
			System.out.println("Execution status is" + execution.getStatus());
			

		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}

	}
}