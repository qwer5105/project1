package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "pass_entity")
public class PassEntity extends BaseEntity{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pass_seq;
	
	private Long pack_seq;
	private String user_id;
	
	//private String status;
	@Enumerated(EnumType.STRING)//열거형을 상수형 정수가 아닌 문자열로 DB에 저장되도록
	private PassStatus status;
	
	
	private Integer remaining_count;
	private LocalDateTime started_at;
	private LocalDateTime ended_at;
	private LocalDateTime expired_at;
	
	
	
	
}
