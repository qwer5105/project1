package com.ss.batch.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ss.batch.entity.PackageEntity;

public interface PackageRepository extends JpaRepository<PackageEntity, Long>{

	List<PackageEntity> findByCreateAtAfter(LocalDateTime dateTime, Pageable page);

	
	@Modifying//update 또는 delete로 데이터 수정이 됨을 알려주는 annot
	@Transactional
	@Query(value = "update PackageEntity p set p.count = :count, p.period = :period where p.packSeq = :packSeq")
	//Query는 java EntityClass 기준(not table)
	int updateCountAndPeriod(Long packSeq, Integer count, Integer period);
	//int updateCountAndPeriod(@Param("packSeq") Long packSeq, Integer count, Integer period);

	
	
	
	
	
	
}
