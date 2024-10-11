package com.ss.batch.entity;

import java.io.Serializable;

public class UserGroupMappingId implements Serializable{

	//jpa에서 복합 키를 사용하려면 직렬화 인터페이스를 상속해야 한다
	
	private String userGroupId;
	private String userId;
}
