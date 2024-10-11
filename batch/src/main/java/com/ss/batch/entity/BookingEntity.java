package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "booking")
public class BookingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingSeq;
	
	private Long passSeq;	//어떤 이용권과 연결되어있는 예약인지
	private String userId;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	
	private boolean usedPass;	//이용권 사용 여부
	private boolean attended;	//참석 여부(통계 분석용)
	
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime cancelledAt;

	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private UserEntity userEntity;
	
	
}
