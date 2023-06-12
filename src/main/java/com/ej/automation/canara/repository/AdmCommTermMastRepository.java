package com.ej.automation.canara.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ej.automation.sbi.entity.AdmCommTermMast;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface AdmCommTermMastRepository extends JpaRepository<AdmCommTermMast, String>{

	@Query(value = "SELECT actm.act_term_id, actm.act_term_name, amtm.amt_Devc_Desc FROM Adm_Comm_Term_Mast actm LEFT JOIN Adm_Mntc_Tmty_Mast amtm on actm.amt_Devc_Type=amtm.amt_Devc_Type ")
	//@Query(value = "SELECT amtm.amt_devc_desc FROM adm_comm_term_mast actm LEFT JOIN adm_mntc_tmty_mast amtm on actm.amt_devc_type=amtm.amt_devc_type WHERE actm.act_term_name= :actTermName")
	public List<Object[]> findDeviceDescription();
	
	
	
}
