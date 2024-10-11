package com.ss.batch.modelMapper;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;

//BulkPassEntity -> PassEntity로 변환

public class PassModelMapper {
	
	public static PassEntity toPassEntity(String userId, BulkPassEntity bulkPassEntity) {
		
		PassEntity passEntity = new PassEntity();
		passEntity.setPack_seq(bulkPassEntity.getPackageSeq());
		passEntity.setUser_id(userId);
		passEntity.setStatus(PassStatus.READY);
		passEntity.setRemaining_count(bulkPassEntity.getCount());
		passEntity.setStarted_at(bulkPassEntity.getStartedAt());
		passEntity.setEnded_at(bulkPassEntity.getEndedAt());
		
		return passEntity;
	}
}


/*
 * 수동으로 매칭해주기 귀찮다면
 * mapstruct : 엔티티끼리 데이터를 매칭해서 저장할 때
 * 자동으로 매칭시켜주는 라이브러리
 * 
 * implementation 'org.mapstruct:mapstruct:1.4.2.Final' 
 * annotationProcessor 'org.mapstruct:mapstruct-processor:1.4.2.Final'
 * 
 * 
 * */