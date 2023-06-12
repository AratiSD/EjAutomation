package com.ej.automation.sbi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;


@Entity
@Table(name="ADM_FLEX_EJTXN_SCH_LOG")
public class AdmFlexEjtxnSchLog {

    @Id
    @Column(name="AFE_EJTXN_ID")
    private String afeEjtxnId;

    @Column(name="AFE_TXN_NO")
    private String afeTxnNo;

    @Column(name="AFE_WDRL_AMT")
    private String afeWdrlAmt;

    @Column(name="AFE_CARD_NO")
    private String afeCardNo;

    @Column(name="AFE_TERM_NAME")
    private String afeTermName;

    @Column(name="AFE_TXN_DATE")
    private String afeTxnDate;

    @Column(name="AFE_TXN_TIME")
    private String afeTxnTime;

    @Column(name="AFE_BRCH_NAME")
    private String afeBrchName;

    @Column(name="AFE_RESP_CODE")
    private String afeRespCode;

    @Column(name="AFE_ERR_CODE")
    private String afeErrCode;

    @Column(name="AFE_AMT_ENT")
    private String afeAmtEnt;

    @Column(name="AFE_LOG_TIME")
    private OffsetDateTime afeLogTime;

    @Column(name="AFE_TXN_DESC")
    private String afeTxnDesc;

    @Column(name="AFE_TXN_REMK")
    private String afeTxnRemk;

    @Column(name="AFE_AMT_FAIL")
    private String afeAmtFail;

    @Column(name="AFE_ERR_DESC")
    private String afeErrDesc;

    public String getAfeEjtxnId() {
        return afeEjtxnId;
    }

    public void setAfeEjtxnId(final String afeEjtxnId) {
        this.afeEjtxnId = afeEjtxnId;
    }

    public String getAfeTxnNo() {
        return afeTxnNo;
    }

    public void setAfeTxnNo(final String afeTxnNo) {
        this.afeTxnNo = afeTxnNo;
    }

    public String getAfeWdrlAmt() {
        return afeWdrlAmt;
    }

    public void setAfeWdrlAmt(final String afeWdrlAmt) {
        this.afeWdrlAmt = afeWdrlAmt;
    }

    public String getAfeCardNo() {
        return afeCardNo;
    }

    public void setAfeCardNo(final String afeCardNo) {
        this.afeCardNo = afeCardNo;
    }

    public String getAfeTermName() {
        return afeTermName;
    }

    public void setAfeTermName(final String afeTermName) {
        this.afeTermName = afeTermName;
    }

    public String getAfeTxnDate() {
        return afeTxnDate;
    }

    public void setAfeTxnDate(final String afeTxnDate) {
        this.afeTxnDate = afeTxnDate;
    }

    public String getAfeTxnTime() {
        return afeTxnTime;
    }

    public void setAfeTxnTime(final String afeTxnTime) {
        this.afeTxnTime = afeTxnTime;
    }

    public String getAfeBrchName() {
        return afeBrchName;
    }

    public void setAfeBrchName(final String afeBrchName) {
        this.afeBrchName = afeBrchName;
    }

    public String getAfeRespCode() {
        return afeRespCode;
    }

    public void setAfeRespCode(final String afeRespCode) {
        this.afeRespCode = afeRespCode;
    }

    public String getAfeErrCode() {
        return afeErrCode;
    }

    public void setAfeErrCode(final String afeErrCode) {
        this.afeErrCode = afeErrCode;
    }

    public String getAfeAmtEnt() {
        return afeAmtEnt;
    }

    public void setAfeAmtEnt(final String afeAmtEnt) {
        this.afeAmtEnt = afeAmtEnt;
    }

    public OffsetDateTime getAfeLogTime() {
        return afeLogTime;
    }

    public void setAfeLogTime(final OffsetDateTime afeLogTime) {
        this.afeLogTime = afeLogTime;
    }

    public String getAfeTxnDesc() {
        return afeTxnDesc;
    }

    public void setAfeTxnDesc(final String afeTxnDesc) {
        this.afeTxnDesc = afeTxnDesc;
    }

    public String getAfeTxnRemk() {
        return afeTxnRemk;
    }

    public void setAfeTxnRemk(final String afeTxnRemk) {
        this.afeTxnRemk = afeTxnRemk;
    }

    public String getAfeAmtFail() {
        return afeAmtFail;
    }

    public void setAfeAmtFail(final String afeAmtFail) {
        this.afeAmtFail = afeAmtFail;
    }

    public String getAfeErrDesc() {
        return afeErrDesc;
    }

    public void setAfeErrDesc(final String afeErrDesc) {
        this.afeErrDesc = afeErrDesc;
    }

}
