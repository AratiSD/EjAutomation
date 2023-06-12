package com.ej.automation.sbi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.OffsetDateTime;


@Entity
@Table(name="ADM_FLEX_TRCN_MAST")
public class AdmFlexTrcnMast implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="AFT_TERM_SQNO")
    private String aftTermSqno;

    @Column(name="ACT_TERM_ID")
    private String actTermId;

    @Column(name="ACB_BANK_ID")
    private String acbBankId;

    @Column(name="AFT_TERM_AGCT")
    private String aftTermAgct;

    @Column(name="AFT_TERM_AGVR")
    private String aftTermAgvr;

    @Column(name="AFT_TERM_BLSZ")
    private String aftTermBlsz;

    @Column(name="AFT_TERM_DBFL")
    private String aftTermDbfl;

    @Column(name="AFT_REMK")
    private String aftRemk;

    @Column(name="AFT_LAS_CON")
    private OffsetDateTime aftLasCon;

    @Column(name="AFT_CON_TIME")
    private OffsetDateTime aftConTime;

    @Column(name="AFT_ATM_KEY")
    private String aftAtmKey;

    @Column(name="AFT_ACUR_PWD")
    private String aftAcurPwd;

    @Column(name="AFT_HCUR_PWD")
    private String aftHcurPwd;

    @Column(name="AFT_SCHR_STAT")
    private String aftSchrStat;

    @Column(name="AFT_HOS_KEY")
    private String aftHosKey;

    @Column(name="AFT_APRE_PWD")
    private String aftAprePwd;

    @Column(name="AFT_TERM_STAT")
    private String aftTermStat;

    @Column(name="AFT_HPRE_PWD")
    private String aftHprePwd;

    @Column(name="AFT_STAT_NAME")
    private String aftStatName;

    @Column(name="AFT_FILE_FRMT")
    private String aftFileFrmt;

    @Column(name="AFT_TERM_ACTS")
    private String aftTermActs;

    @Column(name="AFT_CONN_IPPT")
    private String aftConnIppt;

    @Column(name="AFT_LMSG_TIME")
    private OffsetDateTime aftLmsgTime;

    @Column(name="TXNDATE")
    private Integer txndate;

    @Column(name="LAST_FILECOPY_TIMESTAMP")
    private String lastFilecopyTimestamp;

    @Column(name="RTC_REC_BYTES")
    private String rtcRecBytes;

    @Column(name="RTC_PULL_PATH")
    private String rtcPullPath;

    @Column(name="RTC_ROLLOUT_MODE")
    private String rtcRolloutMode;

    @Column(name="RTC_BACKUP_TIMESTAMP")
    private String rtcBackupTimestamp;

    @Column(name="RTC_PRIMARY_PATH")
    private String rtcPrimaryPath;

    @Column(name="RTC_BACKUP_PATH")
    private String rtcBackupPath;

    @Column(name="RTC_PARSED_TIME")
    private OffsetDateTime rtcParsedTime;

    @Column(name="RTC_PARSED_LINE")
    private String rtcParsedLine;

    @Column(name="RTC_CON_TIME")
    private OffsetDateTime rtcConTime;

    public String getAftTermSqno() {
        return aftTermSqno;
    }

    public void setAftTermSqno(final String aftTermSqno) {
        this.aftTermSqno = aftTermSqno;
    }

    public String getActTermId() {
        return actTermId;
    }

    public void setActTermId(final String actTermId) {
        this.actTermId = actTermId;
    }

    public String getAcbBankId() {
        return acbBankId;
    }

    public void setAcbBankId(final String acbBankId) {
        this.acbBankId = acbBankId;
    }

    public String getAftTermAgct() {
        return aftTermAgct;
    }

    public void setAftTermAgct(final String aftTermAgct) {
        this.aftTermAgct = aftTermAgct;
    }

    public String getAftTermAgvr() {
        return aftTermAgvr;
    }

    public void setAftTermAgvr(final String aftTermAgvr) {
        this.aftTermAgvr = aftTermAgvr;
    }

    public String getAftTermBlsz() {
        return aftTermBlsz;
    }

    public void setAftTermBlsz(final String aftTermBlsz) {
        this.aftTermBlsz = aftTermBlsz;
    }

    public String getAftTermDbfl() {
        return aftTermDbfl;
    }

    public void setAftTermDbfl(final String aftTermDbfl) {
        this.aftTermDbfl = aftTermDbfl;
    }

    public String getAftRemk() {
        return aftRemk;
    }

    public void setAftRemk(final String aftRemk) {
        this.aftRemk = aftRemk;
    }

    public OffsetDateTime getAftLasCon() {
        return aftLasCon;
    }

    public void setAftLasCon(final OffsetDateTime aftLasCon) {
        this.aftLasCon = aftLasCon;
    }

    public OffsetDateTime getAftConTime() {
        return aftConTime;
    }

    public void setAftConTime(final OffsetDateTime aftConTime) {
        this.aftConTime = aftConTime;
    }

    public String getAftAtmKey() {
        return aftAtmKey;
    }

    public void setAftAtmKey(final String aftAtmKey) {
        this.aftAtmKey = aftAtmKey;
    }

    public String getAftAcurPwd() {
        return aftAcurPwd;
    }

    public void setAftAcurPwd(final String aftAcurPwd) {
        this.aftAcurPwd = aftAcurPwd;
    }

    public String getAftHcurPwd() {
        return aftHcurPwd;
    }

    public void setAftHcurPwd(final String aftHcurPwd) {
        this.aftHcurPwd = aftHcurPwd;
    }

    public String getAftSchrStat() {
        return aftSchrStat;
    }

    public void setAftSchrStat(final String aftSchrStat) {
        this.aftSchrStat = aftSchrStat;
    }

    public String getAftHosKey() {
        return aftHosKey;
    }

    public void setAftHosKey(final String aftHosKey) {
        this.aftHosKey = aftHosKey;
    }

    public String getAftAprePwd() {
        return aftAprePwd;
    }

    public void setAftAprePwd(final String aftAprePwd) {
        this.aftAprePwd = aftAprePwd;
    }

    public String getAftTermStat() {
        return aftTermStat;
    }

    public void setAftTermStat(final String aftTermStat) {
        this.aftTermStat = aftTermStat;
    }

    public String getAftHprePwd() {
        return aftHprePwd;
    }

    public void setAftHprePwd(final String aftHprePwd) {
        this.aftHprePwd = aftHprePwd;
    }

    public String getAftStatName() {
        return aftStatName;
    }

    public void setAftStatName(final String aftStatName) {
        this.aftStatName = aftStatName;
    }

    public String getAftFileFrmt() {
        return aftFileFrmt;
    }

    public void setAftFileFrmt(final String aftFileFrmt) {
        this.aftFileFrmt = aftFileFrmt;
    }

    public String getAftTermActs() {
        return aftTermActs;
    }

    public void setAftTermActs(final String aftTermActs) {
        this.aftTermActs = aftTermActs;
    }

    public String getAftConnIppt() {
        return aftConnIppt;
    }

    public void setAftConnIppt(final String aftConnIppt) {
        this.aftConnIppt = aftConnIppt;
    }

    public OffsetDateTime getAftLmsgTime() {
        return aftLmsgTime;
    }

    public void setAftLmsgTime(final OffsetDateTime aftLmsgTime) {
        this.aftLmsgTime = aftLmsgTime;
    }

    public Integer getTxndate() {
        return txndate;
    }

    public void setTxndate(final Integer txndate) {
        this.txndate = txndate;
    }

    public String getLastFilecopyTimestamp() {
        return lastFilecopyTimestamp;
    }

    public void setLastFilecopyTimestamp(final String lastFilecopyTimestamp) {
        this.lastFilecopyTimestamp = lastFilecopyTimestamp;
    }

    public String getRtcRecBytes() {
        return rtcRecBytes;
    }

    public void setRtcRecBytes(final String rtcRecBytes) {
        this.rtcRecBytes = rtcRecBytes;
    }

    public String getRtcPullPath() {
        return rtcPullPath;
    }

    public void setRtcPullPath(final String rtcPullPath) {
        this.rtcPullPath = rtcPullPath;
    }

    public String getRtcRolloutMode() {
        return rtcRolloutMode;
    }

    public void setRtcRolloutMode(final String rtcRolloutMode) {
        this.rtcRolloutMode = rtcRolloutMode;
    }

    public String getRtcBackupTimestamp() {
        return rtcBackupTimestamp;
    }

    public void setRtcBackupTimestamp(final String rtcBackupTimestamp) {
        this.rtcBackupTimestamp = rtcBackupTimestamp;
    }

    public String getRtcPrimaryPath() {
        return rtcPrimaryPath;
    }

    public void setRtcPrimaryPath(final String rtcPrimaryPath) {
        this.rtcPrimaryPath = rtcPrimaryPath;
    }

    public String getRtcBackupPath() {
        return rtcBackupPath;
    }

    public void setRtcBackupPath(final String rtcBackupPath) {
        this.rtcBackupPath = rtcBackupPath;
    }

    public OffsetDateTime getRtcParsedTime() {
        return rtcParsedTime;
    }

    public void setRtcParsedTime(final OffsetDateTime rtcParsedTime) {
        this.rtcParsedTime = rtcParsedTime;
    }

    public String getRtcParsedLine() {
        return rtcParsedLine;
    }

    public void setRtcParsedLine(final String rtcParsedLine) {
        this.rtcParsedLine = rtcParsedLine;
    }

    public OffsetDateTime getRtcConTime() {
        return rtcConTime;
    }

    public void setRtcConTime(final OffsetDateTime rtcConTime) {
        this.rtcConTime = rtcConTime;
    }

}
