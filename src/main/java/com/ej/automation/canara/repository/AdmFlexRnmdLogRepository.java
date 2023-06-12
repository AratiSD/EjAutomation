package com.ej.automation.canara.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ej.automation.canara.entity.AdmFlexRnmdLog;
import com.ej.automation.sbi.entity.AdmFlexSpltLog;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface AdmFlexRnmdLogRepository extends JpaRepository<AdmFlexRnmdLog, String>{
	
	/*
	 * @Query(value =
	 * "SELECT * from ADM_FLEX_SPLT_LOG ad WHERE TO_CHAR(ad.AFS_SPON_DATE,'DD-MON-YY') = TO_CHAR(CURRENT_TIMESTAMP,'DD-MON-YY')"
	 * , nativeQuery = true) public List<AdmFlexSpltLog> getFilesSplittedToday();
	 */
	@Query(value = "SELECT * from ADM_FLEX_RNMD_LOG ad WHERE TO_CHAR(ad.AFS_RNON_DATE,'DD-MON-YY') = TO_CHAR(CURRENT_TIMESTAMP,'DD-MON-YY')", nativeQuery = true)
	public List<AdmFlexRnmdLog> getFilesRenamedToday();

}