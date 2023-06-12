package com.ej.automation.sbi.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="ADM_FLEX_SPLT_LOG")
public class AdmFlexSpltLog implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="AFS_SPLT_ID", length=12)
    private String AFS_SPLT_ID;

    @Column(name="AFS_TERM_ID", length = 50)
    private String afsTermId;

    @Column(name="AFS_STRT_TXN", length = 10)
    private String afsStrtTxn;

    @Column(name="AFS_END_TXN")
    private String afsEndTxn;

    @Column(name="AFS_EJSP_DATE", length = 20)
    private String afsEjspDate;

    @Column(name="AFS_FILE_NAME", length = 100)
    private String afsFileName;

    @Column(name="AFS_FILE_SIZE", length = 100)
    private long afsFileSize;

    @Column(name="AFS_TERM_NAME", length = 50)
    private String afsTermName;

    @Column(name="AFS_TRGT_FLNM", length = 100)
    private String afsTrgtFlnm;

    @Column(name="AFS_SPON_DATE")
    private Timestamp afsSponDate;
    
	/*
	 * "AFS_MSNG_TXN" VARCHAR2(4000 BYTE),      "AFS_FIN_CNT" VARCHAR2(10 BYTE), 
	 *     "AFS_NNFN_CNT" VARCHAR2(10 BYTE),      "AFS_TOT_CNT" VARCHAR2(10 BYTE), 
	 */
    @Column(name="AFS_MSNG_TXN", length = 4000)
    private String afsMsngTxn;
    
    @Column(name="AFS_FIN_CNT")
    private int afsFinCnt;
    
    @Column(name="AFS_NNFN_CNT")
    private int afsNnfnCnt;
    
    @Column(name="AFS_TOT_CNT")
    private int afsTotCnt;

    public String getAFS_SPLT_ID() {
		return AFS_SPLT_ID;
	}

	public void setAFS_SPLT_ID(String aFS_SPLT_ID) {
		AFS_SPLT_ID = aFS_SPLT_ID;
	}

	public String getAfsTermId() {
        return afsTermId;
    }

    public void setAfsTermId(final String afsTermId) {
        this.afsTermId = afsTermId;
    }

    public String getAfsStrtTxn() {
        return afsStrtTxn;
    }

    public void setAfsStrtTxn(final String afsStrtTxn) {
        this.afsStrtTxn = afsStrtTxn;
    }

    public String getAfsEndTxn() {
        return afsEndTxn;
    }

    public void setAfsEndTxn(final String afsEndTxn) {
        this.afsEndTxn = afsEndTxn;
    }

    public String getAfsEjspDate() {
        return afsEjspDate;
    }

    public void setAfsEjspDate(final String afsEjspDate) {
        this.afsEjspDate = afsEjspDate;
    }

    public String getAfsFileName() {
        return afsFileName;
    }

    public void setAfsFileName(final String afsFileName) {
        this.afsFileName = afsFileName;
    }

    public long getAfsFileSize() {
        return afsFileSize;
    }

    public void setAfsFileSize(final long afsFileSize) {
        this.afsFileSize = afsFileSize;
    }

    public String getAfsTermName() {
        return afsTermName;
    }

    public void setAfsTermName(final String afsTermName) {
        this.afsTermName = afsTermName;
    }

    public String getAfsTrgtFlnm() {
        return afsTrgtFlnm;
    }

    public void setAfsTrgtFlnm(final String afsTrgtFlnm) {
        this.afsTrgtFlnm = afsTrgtFlnm;
    }

	public Timestamp getAfsSponDate() {
		return afsSponDate;
	}

	public void setAfsSponDate(Timestamp afsSponDate) {
		this.afsSponDate = afsSponDate;
	}

	public String getAfsMsngTxn() {
		return afsMsngTxn;
	}

	public void setAfsMsngTxn(String afsMsngTxn) {
		this.afsMsngTxn = afsMsngTxn;
	}

	public int getAfsFinCnt() {
		return afsFinCnt;
	}

	public void setAfsFinCnt(int afsFinCnt) {
		this.afsFinCnt = afsFinCnt;
	}

	public int getAfsNnfnCnt() {
		return afsNnfnCnt;
	}

	public void setAfsNnfnCnt(int afsNnfnCnt) {
		this.afsNnfnCnt = afsNnfnCnt;
	}

	public int getAfsTotCnt() {
		return afsTotCnt;
	}

	public void setAfsTotCnt(int afsTotCnt) {
		this.afsTotCnt = afsTotCnt;
	}

	@Override
	public String toString() {
		return "AdmFlexSpltLog [AFS_SPLT_ID=" + AFS_SPLT_ID + ", afsTermId=" + afsTermId + ", afsStrtTxn=" + afsStrtTxn
				+ ", afsEndTxn=" + afsEndTxn + ", afsEjspDate=" + afsEjspDate + ", afsFileName=" + afsFileName
				+ ", afsFileSize=" + afsFileSize + ", afsTermName=" + afsTermName + ", afsTrgtFlnm=" + afsTrgtFlnm
				+ ", afsSponDate=" + afsSponDate + ", afsMsngTxn=" + afsMsngTxn + ", afsFinCnt=" + afsFinCnt
				+ ", afsNnfnCnt=" + afsNnfnCnt + ", afsTotCnt=" + afsTotCnt + "]";
	}
}
