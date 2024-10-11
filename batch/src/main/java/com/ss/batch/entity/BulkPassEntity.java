package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "bulk_pass")
public class BulkPassEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bulkPassSeq;
	
	private Long packageSeq;
	private String userGroupId;
	
	@Enumerated(EnumType.STRING)
	private BulkPassStatus status;
	
	private Integer count;
	
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;

}



