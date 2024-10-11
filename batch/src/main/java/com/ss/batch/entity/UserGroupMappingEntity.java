package com.ss.batch.entity;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class)//복합 키(Id) 설정
public class UserGroupMappingEntity extends BaseEntity{

	@Id
	private String userGroupId;
	
	@Id
	private String userId;
	
	private String userGroupName;
	private String description;
}
