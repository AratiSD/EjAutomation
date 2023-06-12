package com.ej.automation.sbi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ej.automation.sbi.entity.AdmCommTermMast;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface AdmCommTermMastRepository extends JpaRepository<AdmCommTermMast, String>{

	@Query(value = "SELECT amtm.amtDevcDesc FROM AdmCommTermMast actm LEFT JOIN AdmMntcTmtyMast amtm on actm.amtDevcType=amtm.amtDevcType WHERE actm.actTermName= :actTermName ")
	//@Query(value = "SELECT amtm.amt_devc_desc FROM adm_comm_term_mast actm LEFT JOIN adm_mntc_tmty_mast amtm on actm.amt_devc_type=amtm.amt_devc_type WHERE actm.act_term_name= :actTermName")
	public String findDeviceDescription(String actTermName);
	
	
	
}
