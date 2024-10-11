package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;

@Configuration
public class ExpiredPassJobConfig {

	// 데이터를 한꺼번에 처리할 수있는 사이즈
	private final int CHUNK_SIZE = 5;

	// BatchApplication.java
	// JOB을 생성하는 팩토리(클래스)를 생성한다.
	private final JobBuilderFactory jobBF;

	// Step을 생성할 수있는 팩토리 (배치 작업의 단계)
	private final StepBuilderFactory stepBF;

	// JPA와 데이터베이스를 연결 관리하는 객체
	private final EntityManagerFactory enMF;

	public ExpiredPassJobConfig(JobBuilderFactory jobBF, StepBuilderFactory stepBF, EntityManagerFactory enMF) {
		this.jobBF = jobBF;
		this.stepBF = stepBF;
		this.enMF = enMF;
	}

	// JOB
	// 배치 작업을 말하고 여러개의 step을 가질 수있다.
	// 실행시 여러 step순서대로 처리를 한다.
	@Bean
	public Job expiredPassJob() {

		return this.jobBF.get("expiredPassJob") // 배치 작업의 이름
				.start(expiredPassStep()) // 시작하는 메서드(step)
				.build(); // JOB생성
	}

	// STEP
	//
	// <PassEntity, PassEntity> 입,출력 데이터 타입(DB에서 데이터를 읽어 올 때)
	// 첫번째 제네릭 : DB에서 데이터를 읽어올 때
	// 두번째 제네릭 : DB에서 데이터를 처리(수정,저장)할 때
	@Bean
	public Step expiredPassStep() {
		return this.stepBF.get("expiredPassStep")
				.<PassEntity, PassEntity>chunk(CHUNK_SIZE) // 데이터 처리 개수 단위
				.reader(expiredPassItemReader()) // 읽어오기
				.processor(expiredPassItemProcessor()) // 데이터 처리
				.writer(expiredPassItemWriter()) // 저장
				.build();

	}

	// CursorItemReader(하나씩) vs PagingItemReader(chunk_size만큼)
	@Bean
	@StepScope // step이 실행 될 때마다 새로운 객체를 생성하는 annot
	public JpaCursorItemReader<PassEntity> expiredPassItemReader() {

		return new JpaCursorItemReaderBuilder<PassEntity>()
				.name("expiredPassItemReader") // 아이템리더의 이름
				.entityManagerFactory(enMF) // JPA를 통해서 데이터베이스에 연결하고 데이터베이스 관리
				.queryString("select p from PassEntity p where p.status = :status and p.ended_at <= :ended_at") // JPQL쿼리
				// Map.of() : Map 객체 생성
				.parameterValues(Map.of("status", PassStatus.PROGRESSED, "ended_at", LocalDateTime.now()))
				.build();
	}

	@Bean
	public ItemProcessor<PassEntity, PassEntity> expiredPassItemProcessor() {

//		return new ItemProcessor<PassEntity, PassEntity>() { //인터페이스라 익명클래스로 만들어 반환
//
//			@Override
//			public PassEntity process(PassEntity item) throws Exception {
//
//				item.setStatus(PassStatus.EXPIRED); //Pass상태를 만료로 변경
//				item.setExpired_at(LocalDateTime.now());
//				
//				return item;
//			}
//			
//		};

		//람다식으로 표현할 수 있다
		return passEntity -> {
			passEntity.setStatus(PassStatus.EXPIRED);
			passEntity.setExpired_at(LocalDateTime.now());
			return passEntity;
		};

	}
	
	
	@Bean
	public JpaItemWriter<PassEntity> expiredPassItemWriter() {
		
//		JpaItemWriter<PassEntity> writer = new JpaItemWriter<PassEntity>();
//		writer.setEntityManagerFactory(enMF);
//		
//		return writer;
		
		//람다식
		return new JpaItemWriterBuilder<PassEntity>()
                .entityManagerFactory(enMF)
                .build();
		
	}
	
	
	
	
	
	

}
