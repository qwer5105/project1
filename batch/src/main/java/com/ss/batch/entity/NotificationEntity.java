package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "notification")
public class NotificationEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationSeq;
	
	private String uuid;	//카카오톡
	private NotificationEvent event;	//수업 전 알림을 보내기 위한 이벤트
	private String text;	//알림내용
	private Boolean sent;	//발송 여부
	private LocalDateTime sentAt;	//발송 시간
	
	
}
