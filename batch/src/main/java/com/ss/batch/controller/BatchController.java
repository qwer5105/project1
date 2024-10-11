package com.ss.batch.controller;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

public class BatchController {

	//Spring batch
	// - 대량의 데이터를 처리하는 프레임워크
	// - 세가지 처리 단계(step)
	// - 1. 읽기 : 파일, 데이터베이스 등에서 데이터를 읽는다
	// - 2. 처리 : 읽은 데이터를 필요한 형태로 가공
	// - 3. 쓰기 : 처리 된 데이터를 파일이나 데이터베이스로 저장
	//  데이터가 없을 때까지 과정을 무한 반복(job)
	//  메타데이터: 스텝마다 작업을 기록하는 테이블(job repository)
	//  chunk : 데이터를 여러개 묶어서 처리하는 단위, 메모리를 효율적으로 사용
	//  tasklet : 단일 작업

	// 배치프로그램
	// - 대량의 데이터를 처리하는 작업을 자동화하는 프로그램
	
	// Scheduler
	// 일정한 시간 간격으로 반복 수행하거나
	// 특정한 시간에 예약한 작업을 실행해주는 시스템
	
	
	//@EnableBatchProcessing at ~~~Application.java
	
	
	
	
	
}
