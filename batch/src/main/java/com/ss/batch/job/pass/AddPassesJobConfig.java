package com.ss.batch.job.pass;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;

//대량의 이용권을 사용자 그룹에 추가하고
//발송할 때 사용하는 Job, Step 구성

public class AddPassesJobConfig {

	
	// JOB을 생성하는 팩토리(클래스)를 생성한다.
	private final JobBuilderFactory jobBF;

	// Step을 생성할 수있는 팩토리 (배치 작업의 단계)
	private final StepBuilderFactory stepBF;

	//실제 처리하는 내용을 가진 tasklet 객체
	private final AddPassesTasklet taskletq;
	
	public AddPassesJobConfig(JobBuilderFactory jobBF, StepBuilderFactory stepBF, AddPassesTasklet tasklet) {
		this.jobBF = jobBF;
		this.stepBF = stepBF;
		this.taskletq = tasklet;
	}
	
	
	@Bean
	public Job addPassesJob() {
		
		return this.jobBF.get("addPassesJob")
				.start(addPassStep())
				.build();
	}

	@Bean
	public Step addPassStep() {

		return this.stepBF.get("addPassStep")
				.tasklet(taskletq)	//tasklet 방식 
				.build();
	}
	
	
}
