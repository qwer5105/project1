package com.ss.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing // spring batch 기능을 활성화한다.
public class BatchApplication {

	// JOB을 생성하는 팩토리
	private final JobBuilderFactory jobBF;
	// Step을 생성할 수있는 팩토리 
	private final StepBuilderFactory stepBF;
	
	//생성자 주입 방식
	public BatchApplication(JobBuilderFactory jobBF, StepBuilderFactory stepBF) {
		this.jobBF = jobBF;
		this.stepBF = stepBF;
	}
	
	
	@Bean
	public Step passStep() {
		return this.stepBF.get("passStep").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("exec passStep()");	
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Job passJob() {
		// incrementer() 고유한 아이디를 생성하는 설정메서드
		// 매번 다른 ID를 Job실행시 생성해서 저장하는 역할
		// new RunIdIncrementer()
		return this.jobBF.get("passJob").incrementer(new RunIdIncrementer()).start(passStep()).build();
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

}
