package com.ss.batch.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;
import com.ss.batch.entity.UserGroupMappingEntity;
import com.ss.batch.job.pass.AddPassesTasklet;


@ExtendWith(MockitoExtension.class)
public class AddPassTaskletTest {

	// batch 작업과 관련한 객체
	// 배치 작업의 각 단계에서 얼마나 많은 작업을 했는지
	// 어떤 처리가 성공했는지 등을 기록하는 클래스
	@Mock
	private StepContribution stepContirbution;
	
	//청크 단위로 처리 될때 사용하는 클래스
	@Mock
	private ChunkContext chunkContext;
	
	
	//데이터베이스 관련 객체
	@Mock //가상 객체
	private PassRepository passRepository;
	
	@Mock
	private BulkPassRepository bulkPassRepository;
	
	@Mock
	private UserGroupMappingRepository groupRepo;
	
	@InjectMocks //테스트를 위해 생성한 가상 객체를 주입
	private AddPassesTasklet addpassesTasklet;
	
	
	
	@Test
	public void test_execute() throws Exception {
		
		//given
		Long packageSeq = 1L;
		String userGroupId = "GROUP";
		String userId = "A1000000";
		Integer count = 10;
		LocalDateTime now = LocalDateTime.now();
		
		
		BulkPassEntity bulkPassEntity = new BulkPassEntity();
		bulkPassEntity.setPackageSeq(packageSeq);
		bulkPassEntity.setUserGroupId(userGroupId);
		bulkPassEntity.setStatus(BulkPassStatus.READY);
		bulkPassEntity.setStartedAt(now);
		bulkPassEntity.setEndedAt(now.plusDays(60));
		bulkPassEntity.setCount(count);
		
		
		UserGroupMappingEntity userGroupEntity = new UserGroupMappingEntity();
		userGroupEntity.setUserGroupId(userGroupId);
		userGroupEntity.setUserId(userId);
		
		//when
		//when() from org.mockito.Mockito.when : 검색을 했을 때 데이터가 있으면
		// 그 데이터를 thenReturn()으로 전달
		// eq(): 만약 일치가 되지않으면 검색 안함, 일치하면 검색하도록 설정
		// any(): 특정 타입의 인자가 무엇이든 상관없이 메서드 실행
		when(
			bulkPassRepository
			.findByStatusAndStartedAtGreaterThan(eq(BulkPassStatus.READY), any()))
			.thenReturn(List.of(bulkPassEntity));
		
		//그룹에 속한 그룹ID에 사용자 찾기
		when(groupRepo.findByUserGroupId(eq(userGroupId)))
			.thenReturn(List.of(userGroupEntity));
		
		//PassModelMapper를 이용해서 벌크->패스 엔티티로 변환
		//배치 작업 시 현재 작업에 대한 상태와 정보를 전달하는 역할
		RepeatStatus repeatStatus = addpassesTasklet.execute(stepContirbution, chunkContext);
		
		//then
		assertEquals(RepeatStatus.FINISHED, repeatStatus);

		ArgumentCaptor<List> passEntitiesCaptor = ArgumentCaptor.forClass(List.class);
		
		verify(passRepository, times(1)).saveAll(passEntitiesCaptor.capture());
        List<PassEntity> passEntities = passEntitiesCaptor.getValue();
        System.out.println(passEntities);
        
        assertEquals(1, passEntities.size());
        PassEntity passEntity = passEntities.get(0);
        assertEquals(packageSeq, passEntity.getPack_seq());
        assertEquals(userId, passEntity.getUser_id());
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(count, passEntity.getRemaining_count());
		
	}
}
