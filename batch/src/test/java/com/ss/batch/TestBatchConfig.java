package com.ss.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableAutoConfiguration//Springboot의 자동설정기능을 활성(DB, web, security)
@EnableBatchProcessing//대용량 데이터 처리하는 배치작업 설정기능을 활성
@EntityScan("com.ss.batch.entity")
@EnableJpaRepositories("com.ss.batch.repository")
@EnableTransactionManagement
public class TestBatchConfig {

}
