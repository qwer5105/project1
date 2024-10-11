package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ss.batch.entity.BookingEntity;
import com.ss.batch.entity.BookingStatus;
import com.ss.batch.entity.NotificationEntity;

@Configuration
public class SendNotificationClassJobConfig {

	private final int CHUNK_SIZE = 10;
	
	private final JobBuilderFactory jobBF;

	// Step을 생성할 수있는 팩토리 (배치 작업의 단계)
	private final StepBuilderFactory stepBF;

	// JPA와 데이터베이스를 연결 관리하는 객체
	private final EntityManagerFactory enMF;

	
	public SendNotificationClassJobConfig(JobBuilderFactory jobBF, StepBuilderFactory stepBF,
			EntityManagerFactory enMF) {
		this.jobBF = jobBF;
		this.stepBF = stepBF;
		this.enMF = enMF;
	}
	
	
	@Bean
	public Job sendNotificationClassJob() {
		
		return this.jobBF.get("sendNotificationClassJob")
				.start()//첫번째스텝
				.next()//두번째스텝
				.build();
	}
	
	
	@Bean
	public Step addNotificationStep() {
		//예약 정보를 가지고와서(input) 알람 정보를 만들어 줌(output)
		return this.stepBF.get("addNotificationStep")
				.<BookingEntity, NotificationEntity>chunk(CHUNK_SIZE)
				.reader(addNotificationItemReader())
				.processor()
				.writer()
				.build();
	}
	
	//스텝에서 실질적으로 데이터를 읽어오는 메서드
	@Bean
	public JpaPagingItemReader<BookingEntity> addNotificationItemReader() {
		
		return new JpaPagingItemReaderBuilder()
				.name("addNotificationItemReader")
				.entityManagerFactory(enMF)
				.pageSize(CHUNK_SIZE)
				.queryString("select b from BookingEntity b join b.userEntity where b.status = :status and b.startedAt <= :startedAt order by b.bookingSeq")//상태가 준비중이며, 10분 후 시작하는 예약을 찾아서
				.parameterValues(Map.of("status", BookingStatus.READY, "startedAt", LocalDateTime.now().plusMinutes(10)))
				.build();
	}
	
	
	
}
