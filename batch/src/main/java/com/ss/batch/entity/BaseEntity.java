package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
//Entity가 생성되거나 수정될 때 자동으로 생성일, 수정일을 기록할 수 있도록 필요한 작업을 수행
public abstract class BaseEntity { 
	
	//abstract class : baseEntity 객체는 인스턴스(또는 테이블)을 생성하지 않음
	//상속받은 Entity class는 아래 컬럼을 기본적으로 가짐

	@CreatedDate
	@Column(name = "create_at",nullable = false, updatable = false)
	private LocalDateTime createAt;
	
	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;
	
	
	
}
