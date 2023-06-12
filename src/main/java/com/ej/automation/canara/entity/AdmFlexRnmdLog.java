package com.ej.automation.canara.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="ADM_FLEX_RNMD_LOG")
public class AdmFlexRnmdLog implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="AFS_RNMD_ID", length=12)
    private String afsRnmdId;
	
	@Column(name="AFS_EJRN_DATE", length = 20)
    private String afsEjrnDate;

    @Column(name="AFS_FILE_NAME", length = 100)
    private String afsFileName;

    @Column(name="AFS_FILE_SIZE", length = 100)
    private long afsFileSize;
	
	@Column(name="AFS_TERM_ID", length = 50)
    private String afsTermId;
	
	@Column(name="AFS_TERM_NAME", length = 50)
    private String afsTermName;

    @Column(name="AFS_RNON_DATE")
    private Timestamp afsRnonDate;

	public String getAfsRnmdId() {
		return afsRnmdId;
	}

	public void setAfsRnmdId(String afsRnmdId) {
		this.afsRnmdId = afsRnmdId;
	}

	public String getAfsEjrnDate() {
		return afsEjrnDate;
	}

	public void setAfsEjrnDate(String afsEjrnDate) {
		this.afsEjrnDate = afsEjrnDate;
	}

	public String getAfsFileName() {
		return afsFileName;
	}

	public void setAfsFileName(String afsFileName) {
		this.afsFileName = afsFileName;
	}

	public long getAfsFileSize() {
		return afsFileSize;
	}

	public void setAfsFileSize(long afsFileSize) {
		this.afsFileSize = afsFileSize;
	}

	public String getAfsTermId() {
		return afsTermId;
	}

	public void setAfsTermId(String afsTermId) {
		this.afsTermId = afsTermId;
	}

	public String getAfsTermName() {
		return afsTermName;
	}

	public void setAfsTermName(String afsTermName) {
		this.afsTermName = afsTermName;
	}

	public Timestamp getAfsRnonDate() {
		return afsRnonDate;
	}

	public void setAfsRnonDate(Timestamp afsRnonDate) {
		this.afsRnonDate = afsRnonDate;
	}
}
