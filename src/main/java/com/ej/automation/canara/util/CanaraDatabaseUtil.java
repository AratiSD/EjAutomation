package com.ej.automation.canara.util;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ej.automation.canara.entity.AdmFlexRnmdLog;
import com.ej.automation.canara.model.RenamedFileInfo;
import com.ej.automation.sbi.entity.AdmFlexEjtxnSchLog;
import com.ej.automation.sbi.entity.AdmFlexSpltLog;
import com.ej.automation.sbi.model.RRN;
import com.ej.automation.sbi.model.TransactionForm;
import com.ej.automation.sbi.repository.AdmFlexEjtxnSchLogRepository;
import com.ej.automation.canara.repository.AdmFlexRnmdLogRepository;
import com.ej.automation.canara.repository.AdmFlexSplitLogRepository;

@Component
public class CanaraDatabaseUtil {
	public static final Logger logger = LoggerFactory.getLogger(CanaraDatabaseUtil.class);
		
	@Autowired
	private AdmFlexSplitLogRepository admFlexSplitLogRepository;
	
	@Autowired
	private AdmFlexEjtxnSchLogRepository admFlexEjtxnSchLogRepository;
	
	@Autowired
	private AdmFlexRnmdLogRepository admFlexRnmdLogRepository;
	
	public void insertSplittedFileInfo(Map<String, TransactionForm> form) {
		try {
			logger.info("inside insertSplittedFileInfo ");
			List<AdmFlexSpltLog> admFlexSpltLogList = new ArrayList<AdmFlexSpltLog>();
			for (Entry<String, TransactionForm> entry : form.entrySet()) {
				AdmFlexSpltLog admFlexSpltLog = new AdmFlexSpltLog();
				admFlexSpltLog.setAFS_SPLT_ID(RRN.genRRN());
				admFlexSpltLog.setAfsTermId(entry.getValue().getTerminalId());
				admFlexSpltLog.setAfsTermName(entry.getValue().getTerminalName());
				admFlexSpltLog.setAfsStrtTxn(String.valueOf(entry.getValue().getStartingSequenceNumber()));
				admFlexSpltLog.setAfsEndTxn(String.valueOf(entry.getValue().getEndingSequenceNumber()));
				admFlexSpltLog.setAfsEjspDate(entry.getValue().getStartDate());
				//admFlexSpltLog.setAfsSponDate(LocalDate.now());
				admFlexSpltLog.setAfsSponDate(entry.getValue().getSponDate());
				admFlexSpltLog.setAfsFileName(entry.getValue().getFileName());
				admFlexSpltLog.setAfsFileSize(entry.getValue().getFileSize());
				admFlexSpltLog.setAfsFinCnt(entry.getValue().getFinancialtxnCount());
				admFlexSpltLog.setAfsNnfnCnt(entry.getValue().getNonFinancialtxnCount());
				admFlexSpltLog.setAfsTotCnt(entry.getValue().getTotaltxnCount());
				admFlexSpltLog.setAfsMsngTxn(entry.getValue().getMissingSequenceNumber());
				admFlexSpltLogList.add(admFlexSpltLog);
			}
			admFlexSplitLogRepository.saveAll(admFlexSpltLogList);
		} catch (Exception ex) {
			logger.error("#### Exception inside insertSplittedFileInfo :: "+ex.getMessage());
		}
	}
	
	public void insertRenamedFileInfo(Map<String, RenamedFileInfo> renamedFileInfoMap) {
		
		try {
			logger.info("inside insertRenamedFileInfo");
			List<AdmFlexRnmdLog> admFlexRnmdLogList = new ArrayList<AdmFlexRnmdLog>();
			for(Entry<String, RenamedFileInfo> entry : renamedFileInfoMap.entrySet()) {
				AdmFlexRnmdLog admFlexRnmdLog = new AdmFlexRnmdLog();
				admFlexRnmdLog.setAfsRnmdId(RRN.genRRN());
				admFlexRnmdLog.setAfsEjrnDate(entry.getValue().getEjRenamedDate());
				admFlexRnmdLog.setAfsFileName(entry.getValue().getFileName());
				admFlexRnmdLog.setAfsFileSize(entry.getValue().getFileSize());
				admFlexRnmdLog.setAfsRnonDate(entry.getValue().getRenamedOn());
				admFlexRnmdLog.setAfsTermId(entry.getValue().getTerminalId());
				admFlexRnmdLog.setAfsTermName(entry.getValue().getTerminalName());
				admFlexRnmdLogList.add(admFlexRnmdLog);
			}
			admFlexRnmdLogRepository.saveAll(admFlexRnmdLogList);
		} catch(Exception e) {
			
		}
	}

	public void addSchedulerEJTransactionDetails(String terminalID, Map<String, String> transactionParams) {
		try {
			AdmFlexEjtxnSchLog admFlexEjtxnSchLog = new AdmFlexEjtxnSchLog();
			admFlexEjtxnSchLog.setAfeEjtxnId(RRN.genRRN());
			admFlexEjtxnSchLog.setAfeTxnNo(transactionParams.get("TXN NO"));
			admFlexEjtxnSchLog.setAfeWdrlAmt(transactionParams.get("WITHDRAWAL"));
			admFlexEjtxnSchLog.setAfeCardNo(transactionParams.get("CARDNUMBER"));
			admFlexEjtxnSchLog.setAfeTxnDate(transactionParams.get("DATE"));
			admFlexEjtxnSchLog.setAfeTxnTime(transactionParams.get("TIME"));
			admFlexEjtxnSchLog.setAfeTermName(transactionParams.get("ATM ID"));
			admFlexEjtxnSchLog.setAfeBrchName(transactionParams.get("BRANCH"));
			admFlexEjtxnSchLog.setAfeRespCode(transactionParams.get("RESPCODE"));
			admFlexEjtxnSchLog.setAfeErrCode(transactionParams.get("ERRORCODE"));
			admFlexEjtxnSchLog.setAfeAmtEnt(transactionParams.get("AMOUNTENTERED"));
			admFlexEjtxnSchLog.setAfeTxnRemk(transactionParams.get("TXNREMK"));
			admFlexEjtxnSchLog.setAfeTxnDesc(transactionParams.get("TXNDESC"));
			admFlexEjtxnSchLog.setAfeAmtFail(transactionParams.get("AMOUNTFAILED"));
			admFlexEjtxnSchLog.setAfeErrDesc(transactionParams.get("ERRORDESC"));
			admFlexEjtxnSchLog.setAfeLogTime(OffsetDateTime.now());
			admFlexEjtxnSchLogRepository.save(admFlexEjtxnSchLog);
		} catch (Exception ex) {
			logger.error("#### Exception inside AddSchedulerEJTransactionDetails :: " +ex.getMessage());
		}
	}
}
