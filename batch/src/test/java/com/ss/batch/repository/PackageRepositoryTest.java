package com.ss.batch.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.ss.batch.entity.PackageEntity;



//@ActiveProfiles("")
// 테스트 서버 환경을 설정 (application-test.properties, application-test.yml)
// dev: 개발중, 로컬DB, 디버그 모드
// prod: 실제 서비스, 성능 최적화, 보안관련 설정
// test: 테스트환경
//@ActiveProfiles({"dev", "test"}) 두가지 이상의 환경을 동시에 설정

@SpringBootTest
public class PackageRepositoryTest {

	@Autowired
	private PackageRepository packRepo;
	
	@Test
	public void test_template() {
		
		//given
		
		//when
		
		//then
	}
	
	
	@Test
	public void test_save() {
		//given
		PackageEntity packEnt = new PackageEntity();
		packEnt.setPackName("바디 챌린지 pt 12w");
		packEnt.setPeriod(84);
		
		//when
		packRepo.save(packEnt);
		
		//then
		assertNotNull(packEnt.getPackSeq()); //null이 아닌지 확인
		
	}
	
	
	@Test
	public void test_findByCreateAtAfter() {
		//given
		LocalDateTime dateTime =LocalDateTime.now().minusMinutes(1); 
		
		PackageEntity pack1 = new PackageEntity();
		pack1.setPackName("학생 전용 3개월");
		pack1.setPeriod(90);
		
		PackageEntity pack2 = new PackageEntity();
		pack2.setPackName("학생 전용 6개월");
		pack2.setPeriod(180);
		
		packRepo.save(pack1);
		packRepo.save(pack2);

		//when
		PageRequest page = PageRequest.of(0, 1, Sort.by("packSeq").descending());
		//EntityClass 필드명과 맞춰준다
		
		List<PackageEntity> result = packRepo.findByCreateAtAfter(dateTime, page);
		
		//then
		System.out.println("resultSize: " + result.size());
		assertEquals(1, result.size());
			
		assertEquals(pack2.getPackSeq(), result.get(0).getPackSeq());
		
	}
	
	
	@Test
	public void test_updateCountAndPeriod() {
		
		//given
		PackageEntity pack3 = new PackageEntity();
		pack3.setPackName("바디 프로필 이벤트 4개월");
		pack3.setPeriod(90);
		packRepo.save(pack3);
		System.out.println("given: " + pack3);
		
		//when
		pack3.setCount(30);
		pack3.setPeriod(110);
		packRepo.save(pack3);
		System.out.println("when: " + pack3);
		
		final PackageEntity updated = packRepo.findById(pack3.getPackSeq()).get();
		
		//then
		System.out.println("then: " + updated);
		assertEquals(30, updated.getCount());
		assertEquals(120, updated.getPeriod());
	
	}
	
	
	@Test
	public void test_delete() {
		
		//given
		PackageEntity pack4 = new PackageEntity();
		pack4.setPackName("제거할 패키지");
		pack4.setCount(1);
		
		PackageEntity newPack4 = packRepo.save(pack4);
		
		//when
		packRepo.deleteById(newPack4.getPackSeq());
		
		//then
		assertTrue(packRepo.findById(newPack4.getPackSeq()).isEmpty());
		
	}
	
	
	
	
}
