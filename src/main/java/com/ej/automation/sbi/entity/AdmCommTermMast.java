package com.ej.automation.sbi.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ADM_COMM_TERM_MAST")
public class AdmCommTermMast implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name ="ACT_TERM_ID", length = 16)
    private String actTermId;

    @Column(name ="ACT_TERM_NAME", length = 16)
    private String actTermName;

    @Column(name ="ACT_TERM_IP", length = 15)
    private String actTermIp;

    @Column(name ="AMT_DEVC_TYPE", length = 2)
    private String amtDevcType;

    @Column(name ="ACT_TERM_NWMD", length = 35)
    private String actTermNwmd;

    @Column(name ="ACT_TERM_CONA", length = 35)
    private String actTermCona;

    @Column(name ="ACT_TERM_COEM", length = 35)
    private String actTermCoem;

    @Column(name ="ACT_TERM_GAWA", length = 20)
    private String actTermGawa;

    @Column(name ="ACT_TERM_BAND", length = 50)
    private String actTermBand;

    @Column(name ="ACT_TERM_MSBY", length = 25)
    private String actTermMsby;

    @Column(name ="ACC_COUN_ID", length = 6)
    private String accCounId;

    @Column(name ="ACS_STAT_ID", length = 6)
    private String acsStatId;

    @Column(name ="ACC_CITY_ID", length = 6)
    private String accCityId;

    @Column(name ="ACB_BRCH_ID", length = 12)
    private String acbBrchId;

    @Column(name ="ACR_REGN_ID", length = 6)
    private String acrRegnId;

    @Column(name ="ACB_BANK_ID", length = 12)
    private String acbBankId;

    @Column(name ="ACT_TERM_ADD", length = 200)
    private String actTermAdd;

    @Column(name ="ACT_TERM_POCD", length = 6)
    private String actTermPocd;

    @Column(name ="ACT_TERM_ONFL", length = 1)
    private String actTermOnfl;

    @Column(name ="ACT_TERM_INDT")
    private OffsetDateTime actTermIndt;

    @Column(name ="ACT_TERM_SICD", length = 15)
    private String actTermSicd;

    @Column(name ="ACT_TERM_STAT", length = 1)
    private String actTermStat;

    @Column(name ="AMT_TERM_BNFL", length = 1)
    private String amtTermBnfl;

    @Column(name ="ACT_TERM_NETWORK", length = 20)
    private String actTermNetwork;

    @Column(name ="ACT_TERM_ZONE", length = 20)
    private String actTermZone;

    @Column(name ="ACT_TERM_RMK", length = 500)
    private String actTermRmk;

    @Column(name ="ACT_NODAL_BRCH_ID", length = 12)
    private String actNodalBrchId;

    @Column(name ="ACT_USR_FLD1", length = 25)
    private String actUsrFld1;

    @Column(name ="ACT_USR_FLD2", length = 25)
    private String actUsrFld2;

    @Column(name ="ACT_USR_FLD3", length = 25)
    private String actUsrFld3;

    @Column(name ="ACT_USR_FLD4", length = 25)
    private String actUsrFld4;

    @Column(name ="ACT_USR_FLD5", length = 25)
    private String actUsrFld5;

    @Column(name ="ACT_CREATED_BY", length = 35)
    private String actCreatedBy;

    @Column(name ="ACT_TERM_LTTM")
    private LocalDate actTermLttm;

    @Column(name ="ACT_TERM_LONGI", length = 20)
    private String actTermLongi;

    @Column(name ="ACT_TERM_LATTI", length = 20)
    private String actTermLatti;

    @Column(name ="LAST_ADMN_TRXN")
    private OffsetDateTime lastAdmnTrxn;

    @Column(name ="ACT_TERM_LNTM")
    private LocalDate actTermLntm;

    @Column(name ="ACT_TERM_THLD")
    private Integer actTermThld;

    @Column(name ="AMT_SERV_STARTTIME", length = 10)
    private String amtServStarttime;

    @Column(name ="AMT_SERV_ENDTIME", length = 10)
    private String amtServEndtime;

    @Column(name ="ACT_MKCK_STATUS", length = 2)
    private String actMkckStatus;

    @Column(name ="CREATED_BY", length = 20)
    private String createdBy;

    @Column(name ="CREATED_TIME")
    private LocalDate createdTime;

    @Column(name ="MODIFIED_BY", length = 20)
    private String modifiedBy;

    @Column(name ="REMARKS", length = 100)
    private String remarks;

    @Column(name ="MODIFIED_TIME")
    private LocalDate modifiedTime;

    @Column(name ="LAST_FILECOPY_TEMP_TIMESTAMP")
    private String lastFilecopyTempTimestamp;

    @Column(name ="ACT_TERM_CAM2_TIMESTP")
    private String actTermCam2Timestp;

    @Column(name ="ACT_TERM_CAM3_TIMESTP")
    private String actTermCam3Timestp;

    @Column(name ="ACT_TERM_CAM1_TIMESTP")
    private String actTermCam1Timestp;

    @Column(name ="ACT_TERM_CAM4_TIMESTP", length = 200)
    private String actTermCam4Timestp;

    public String getActTermId() {
        return actTermId;
    }

    public void setActTermId(final String actTermId) {
        this.actTermId = actTermId;
    }

    public String getActTermName() {
        return actTermName;
    }

    public void setActTermName(final String actTermName) {
        this.actTermName = actTermName;
    }

    public String getActTermIp() {
        return actTermIp;
    }

    public void setActTermIp(final String actTermIp) {
        this.actTermIp = actTermIp;
    }

    public String getAmtDevcType() {
        return amtDevcType;
    }

    public void setAmtDevcType(final String amtDevcType) {
        this.amtDevcType = amtDevcType;
    }

    public String getActTermNwmd() {
        return actTermNwmd;
    }

    public void setActTermNwmd(final String actTermNwmd) {
        this.actTermNwmd = actTermNwmd;
    }

    public String getActTermCona() {
        return actTermCona;
    }

    public void setActTermCona(final String actTermCona) {
        this.actTermCona = actTermCona;
    }

    public String getActTermCoem() {
        return actTermCoem;
    }

    public void setActTermCoem(final String actTermCoem) {
        this.actTermCoem = actTermCoem;
    }

    public String getActTermGawa() {
        return actTermGawa;
    }

    public void setActTermGawa(final String actTermGawa) {
        this.actTermGawa = actTermGawa;
    }

    public String getActTermBand() {
        return actTermBand;
    }

    public void setActTermBand(final String actTermBand) {
        this.actTermBand = actTermBand;
    }

    public String getActTermMsby() {
        return actTermMsby;
    }

    public void setActTermMsby(final String actTermMsby) {
        this.actTermMsby = actTermMsby;
    }

    public String getAccCounId() {
        return accCounId;
    }

    public void setAccCounId(final String accCounId) {
        this.accCounId = accCounId;
    }

    public String getAcsStatId() {
        return acsStatId;
    }

    public void setAcsStatId(final String acsStatId) {
        this.acsStatId = acsStatId;
    }

    public String getAccCityId() {
        return accCityId;
    }

    public void setAccCityId(final String accCityId) {
        this.accCityId = accCityId;
    }

    public String getAcbBrchId() {
        return acbBrchId;
    }

    public void setAcbBrchId(final String acbBrchId) {
        this.acbBrchId = acbBrchId;
    }

    public String getAcrRegnId() {
        return acrRegnId;
    }

    public void setAcrRegnId(final String acrRegnId) {
        this.acrRegnId = acrRegnId;
    }

    public String getAcbBankId() {
        return acbBankId;
    }

    public void setAcbBankId(final String acbBankId) {
        this.acbBankId = acbBankId;
    }

    public String getActTermAdd() {
        return actTermAdd;
    }

    public void setActTermAdd(final String actTermAdd) {
        this.actTermAdd = actTermAdd;
    }

    public String getActTermPocd() {
        return actTermPocd;
    }

    public void setActTermPocd(final String actTermPocd) {
        this.actTermPocd = actTermPocd;
    }

    public String getActTermOnfl() {
        return actTermOnfl;
    }

    public void setActTermOnfl(final String actTermOnfl) {
        this.actTermOnfl = actTermOnfl;
    }

    public OffsetDateTime getActTermIndt() {
        return actTermIndt;
    }

    public void setActTermIndt(final OffsetDateTime actTermIndt) {
        this.actTermIndt = actTermIndt;
    }

    public String getActTermSicd() {
        return actTermSicd;
    }

    public void setActTermSicd(final String actTermSicd) {
        this.actTermSicd = actTermSicd;
    }

    public String getActTermStat() {
        return actTermStat;
    }

    public void setActTermStat(final String actTermStat) {
        this.actTermStat = actTermStat;
    }

    public String getAmtTermBnfl() {
        return amtTermBnfl;
    }

    public void setAmtTermBnfl(final String amtTermBnfl) {
        this.amtTermBnfl = amtTermBnfl;
    }

    public String getActTermNetwork() {
        return actTermNetwork;
    }

    public void setActTermNetwork(final String actTermNetwork) {
        this.actTermNetwork = actTermNetwork;
    }

    public String getActTermZone() {
        return actTermZone;
    }

    public void setActTermZone(final String actTermZone) {
        this.actTermZone = actTermZone;
    }

    public String getActTermRmk() {
        return actTermRmk;
    }

    public void setActTermRmk(final String actTermRmk) {
        this.actTermRmk = actTermRmk;
    }

    public String getActNodalBrchId() {
        return actNodalBrchId;
    }

    public void setActNodalBrchId(final String actNodalBrchId) {
        this.actNodalBrchId = actNodalBrchId;
    }

    public String getActUsrFld1() {
        return actUsrFld1;
    }

    public void setActUsrFld1(final String actUsrFld1) {
        this.actUsrFld1 = actUsrFld1;
    }

    public String getActUsrFld2() {
        return actUsrFld2;
    }

    public void setActUsrFld2(final String actUsrFld2) {
        this.actUsrFld2 = actUsrFld2;
    }

    public String getActUsrFld3() {
        return actUsrFld3;
    }

    public void setActUsrFld3(final String actUsrFld3) {
        this.actUsrFld3 = actUsrFld3;
    }

    public String getActUsrFld4() {
        return actUsrFld4;
    }

    public void setActUsrFld4(final String actUsrFld4) {
        this.actUsrFld4 = actUsrFld4;
    }

    public String getActUsrFld5() {
        return actUsrFld5;
    }

    public void setActUsrFld5(final String actUsrFld5) {
        this.actUsrFld5 = actUsrFld5;
    }

    public String getActCreatedBy() {
        return actCreatedBy;
    }

    public void setActCreatedBy(final String actCreatedBy) {
        this.actCreatedBy = actCreatedBy;
    }

    public LocalDate getActTermLttm() {
        return actTermLttm;
    }

    public void setActTermLttm(final LocalDate actTermLttm) {
        this.actTermLttm = actTermLttm;
    }

    public String getActTermLongi() {
        return actTermLongi;
    }

    public void setActTermLongi(final String actTermLongi) {
        this.actTermLongi = actTermLongi;
    }

    public String getActTermLatti() {
        return actTermLatti;
    }

    public void setActTermLatti(final String actTermLatti) {
        this.actTermLatti = actTermLatti;
    }

    public OffsetDateTime getLastAdmnTrxn() {
        return lastAdmnTrxn;
    }

    public void setLastAdmnTrxn(final OffsetDateTime lastAdmnTrxn) {
        this.lastAdmnTrxn = lastAdmnTrxn;
    }

    public LocalDate getActTermLntm() {
        return actTermLntm;
    }

    public void setActTermLntm(final LocalDate actTermLntm) {
        this.actTermLntm = actTermLntm;
    }

    public Integer getActTermThld() {
        return actTermThld;
    }

    public void setActTermThld(final Integer actTermThld) {
        this.actTermThld = actTermThld;
    }

    public String getAmtServStarttime() {
        return amtServStarttime;
    }

    public void setAmtServStarttime(final String amtServStarttime) {
        this.amtServStarttime = amtServStarttime;
    }

    public String getAmtServEndtime() {
        return amtServEndtime;
    }

    public void setAmtServEndtime(final String amtServEndtime) {
        this.amtServEndtime = amtServEndtime;
    }

    public String getActMkckStatus() {
        return actMkckStatus;
    }

    public void setActMkckStatus(final String actMkckStatus) {
        this.actMkckStatus = actMkckStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(final LocalDate createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(final LocalDate modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getLastFilecopyTempTimestamp() {
        return lastFilecopyTempTimestamp;
    }

    public void setLastFilecopyTempTimestamp(final String lastFilecopyTempTimestamp) {
        this.lastFilecopyTempTimestamp = lastFilecopyTempTimestamp;
    }

    public String getActTermCam2Timestp() {
        return actTermCam2Timestp;
    }

    public void setActTermCam2Timestp(final String actTermCam2Timestp) {
        this.actTermCam2Timestp = actTermCam2Timestp;
    }

    public String getActTermCam3Timestp() {
        return actTermCam3Timestp;
    }

    public void setActTermCam3Timestp(final String actTermCam3Timestp) {
        this.actTermCam3Timestp = actTermCam3Timestp;
    }

    public String getActTermCam1Timestp() {
        return actTermCam1Timestp;
    }

    public void setActTermCam1Timestp(final String actTermCam1Timestp) {
        this.actTermCam1Timestp = actTermCam1Timestp;
    }

    public String getActTermCam4Timestp() {
        return actTermCam4Timestp;
    }

    public void setActTermCam4Timestp(final String actTermCam4Timestp) {
        this.actTermCam4Timestp = actTermCam4Timestp;
    }

}
