package com.ss.batch.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.ss.batch.TestBatchConfig;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;
import com.ss.batch.job.pass.ExpiredPassJobConfig;

@SpringBootTest
@SpringBatchTest //batch tester annot
@ContextConfiguration(classes = {ExpiredPassJobConfig.class, TestBatchConfig.class})
public class ExpiredPassJobConfigTest {

	//테스트용 batch job 실행 유틸
	@Autowired
	private JobLauncherTestUtils jobLauncher;
	
	@Autowired
	private PassRepository passRepo;
	
	
	//데이터를 추가해서 만료시키는 메서드
	private void addPassEntities(int size) {
		
		final LocalDateTime now = LocalDateTime.now();
		final Random random = new Random();
		
		//가상의 PassEntity instance 'size'개 생성
		List<PassEntity> passEntities = new ArrayList<PassEntity>();
		
		for(int i=0; i<size; ++i) {
			PassEntity passEntity = new PassEntity();
			passEntity.setPack_seq(1L);
			passEntity.setUser_id("A" + 10000 + i);
			passEntity.setStatus(PassStatus.PROGRESSED);
			passEntity.setRemaining_count(random.nextInt(11));
			passEntity.setStarted_at(now.minusDays(60)); //60일전에 만들어진 Pass
			passEntity.setEnded_at(now.minusDays(1));
			passEntities.add(passEntity);
		}
		System.out.println("list: " + passEntities);
		
		List<PassEntity> saveResult = passRepo.saveAll(passEntities);
		System.out.println("saveResult: " + saveResult);
		
		
	}
	
	
	@Test
	public void test_expirePassStep() throws Exception {
		//given
		addPassEntities(10);
		
		//when
		JobExecution jobExec = jobLauncher.launchJob();
		JobInstance jobInstance = jobExec.getJobInstance();
		
		//then
		assertEquals(ExitStatus.COMPLETED, jobExec.getExitStatus());
		
		assertEquals("expiredPassJob", jobInstance.getJobName());
	}
	
	
	
	
}
