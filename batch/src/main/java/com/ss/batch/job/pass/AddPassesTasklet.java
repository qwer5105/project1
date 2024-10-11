package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.UserGroupMappingEntity;
import com.ss.batch.modelMapper.PassModelMapper;
import com.ss.batch.repository.BulkPassRepository;
import com.ss.batch.repository.PassRepository;
import com.ss.batch.repository.UserGroupMappingRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class AddPassesTasklet implements Tasklet{

	private final PassRepository pr;
	private final BulkPassRepository bpr;
	private final UserGroupMappingRepository ugmr;
	
	public AddPassesTasklet(PassRepository pr, BulkPassRepository bpr, UserGroupMappingRepository ugmr) {
		this.pr = pr;
		this.bpr = bpr;
		this.ugmr = ugmr;
	}
	
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		
		final LocalDateTime startAt = LocalDateTime.now().minusDays(1);
	
		//아직 처리되지 않은 대량의 이용권 목록을 불러온다(읽기)
		List<BulkPassEntity> bulkPassEntities 
		= bpr.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startAt);
		
		//불러온 이용권 목록에서 그룹에 속한 사용자를 조회하여 이용권 추가 
		int count = 0;
		
		//user그룹에 속한 userId를 조회
		for(BulkPassEntity bulkPassEntity : bulkPassEntities) {
			
			List<String> userIds = ugmr.findByUserGroupId(bulkPassEntity.getUserGroupId())
			.stream() //리스트 등 컬렉션을 처리할 때 사용되는 메서드
			.map(UserGroupMappingEntity::getUserId)
			.collect(Collectors.toList());
			
			//userId별로 이용권 추가
			count += addPass(userIds, bulkPassEntity);
			bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
		}
		
		
		log.info("tasklet: execute 이용권:{}건 추가완료 startedAt: {}",count,startAt);
		
		
		return RepeatStatus.FINISHED;
	}


	private int addPass(List<String> userIds, BulkPassEntity bulkPassEntity) {

		List<PassEntity> passEntities = new ArrayList<PassEntity>();
		
		for(String userId : userIds) {
			PassEntity passEntity = PassModelMapper.toPassEntity(userId, bulkPassEntity);
			passEntities.add(passEntity);
		}
		
		return pr.saveAll(passEntities).size();
	}


	




}
