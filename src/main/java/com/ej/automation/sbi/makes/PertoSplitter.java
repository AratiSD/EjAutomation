package com.ej.automation.sbi.makes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ej.automation.sbi.model.BigFile;
import com.ej.automation.sbi.model.M24Utility;
import com.ej.automation.sbi.model.TransactionForm;
import com.ej.automation.sbi.service.SbiServiceImpl;
import com.ej.automation.sbi.util.CommonUtil;
import com.ej.automation.sbi.util.SbiDatabaseUtil;

@Component
public class PertoSplitter {
	public static final Logger logger = LoggerFactory.getLogger(PertoSplitter.class);

	@Autowired
	private CommonUtil commonUtility;

	@Autowired
	private SbiDatabaseUtil databaseUtil;

	@SuppressWarnings("static-access")
	public void pertoSplitter(String currentFile, String bankId, String terminalId, String maker, String sourcePath,
			String splittedFilePath, String notSplittedFilePath, String countFile, HSSFSheet sheet, String uploadFolderPath) {
		String atmID = "";
		File currentFilePath = new File(currentFile);
		BigFile bigFile = null;
		String transactionDate = "";
		String inputDate = "";
		int transactionCheck = 0;
		String[] recordSplit = null;
		String rawDate = "";
		String transactionDatefile = "";
		Boolean fileExist = null;
		StringBuilder readLineByLine = new StringBuilder();
		ArrayList<String> transactionDates = new ArrayList<String>();
		try {
			logger.info("filename :: "+currentFile);
			BufferedWriter bufferedWriter = null;
			File outputFilename = null;
			bigFile = new BigFile(currentFile);
			Iterator<String> iterString = bigFile.iterator();
			List<String> outputFilesList = new ArrayList<String>();
			while (iterString.hasNext()) {
				String string = iterString.next();
				string = string.trim();
				readLineByLine.append("\t");
				if (string.contains("ATM ID") && string.contains("DATE") && string.contains("TIME")) {
					transactionCheck++;
					readLineByLine.append(string);
					readLineByLine.append(System.lineSeparator());
					if (iterString.hasNext()) {
						string = iterString.next();
						string = string.trim();
						readLineByLine.append("\t");
					}
					recordSplit = string.trim().replaceAll(" +", " ").split(" ");
					rawDate = recordSplit[0];
					if (rawDate.length() > 8) {
						transactionDatefile =commonUtility.getDateforFile(rawDate, "dd-MMM-yyyy", "ddMMyy");
					} else {
						transactionDatefile = commonUtility.getDateforFile(rawDate, "MM/dd/yy", "ddMMyy");
					}
					atmID = recordSplit[recordSplit.length - 1];
					fileExist = transactionDates.contains(recordSplit[0]);
					if (!fileExist) {
						transactionDates.add(rawDate);
					}
					readLineByLine.append(string);
					readLineByLine.append(System.lineSeparator());
				} else if (string.contains("AVAIL BAL")) {
					readLineByLine.append(string);
					readLineByLine.append("\n");
					readLineByLine.append("********************************************");
					readLineByLine.append(System.lineSeparator());
					readLineByLine.append(System.lineSeparator());
				} else {
					readLineByLine.append(string);
					readLineByLine.append("\n");
				}
			}
			logger.info("Inside pertoSplitter Exited while loop");
			if (readLineByLine.toString().length() > 0 && transactionDates.size() > 0) {
				String finalSplittedFilePath = splittedFilePath;
				logger.info("FinalSplittedFilePath :: " +finalSplittedFilePath);
				outputFilename = commonUtility.folderCreation(finalSplittedFilePath, bankId, maker, atmID + "_", transactionDatefile,".txt");
				logger.info("Perto Splitter file path :: "+outputFilename);
				bufferedWriter = new BufferedWriter(new FileWriter(outputFilename, true));
				if(!outputFilesList.contains(outputFilename.getAbsolutePath())){
					outputFilesList.add(outputFilename.getAbsolutePath());
				}
				bufferedWriter.write(readLineByLine.toString());
				bufferedWriter.write(System.lineSeparator());
				readLineByLine.setLength(0);
				bufferedWriter.flush();
				bufferedWriter.close();
			} else {
				commonUtility.moveFile(sourcePath + File.separator + currentFilePath.getName(),
						notSplittedFilePath + File.separator + currentFilePath.getName());
			}

			for(String splittedFile : outputFilesList){
				String[] pathSplit = splittedFile.split("Splitted_Files");
				String relativePath = pathSplit[1];
				String absoluteUploadPath = uploadFolderPath+relativePath;
				String uploadFilePath = absoluteUploadPath.substring(0,absoluteUploadPath.lastIndexOf(File.separator));
				commonUtility.checkAndCreateFolders(uploadFilePath);
				commonUtility.moveFile(splittedFile,absoluteUploadPath);
			}

			// ashraf
			if (transactionCheck == 0) {
				File file1 = new File(currentFile);
				commonUtility.moveFile(currentFile, notSplittedFilePath + File.separator + file1.getName());
			}
			logger.info("transactionCheck :: " + transactionCheck);
			logger.info("OutputFilename :: " +outputFilename);
			if (transactionCheck > 0) {
				bigFile.Close();
				if (rawDate.length() > 8) {
					inputDate = commonUtility.getDateforFile(rawDate, "dd-MMM-yyyy", "dd-MM-yyyy");
					transactionDate = commonUtility.getDateforFile(rawDate, "dd-MMM-yyyy", "ddMMyy");
				} else {
					inputDate = commonUtility.getDateforFile(rawDate, "MM/dd/yy", "dd-MM-yyyy");
					transactionDate = commonUtility.getDateforFile(rawDate, "MM/dd/yy", "ddMMyy");
				}
				String mapKey = atmID + transactionDate;
				TransactionForm transactionform = commonUtility.getTransactions(outputFilename, transactionDate, inputDate,
						bankId, atmID, currentFile);
				if (transactionform != null)
					SbiServiceImpl.transactionFormMap.put(mapKey, transactionform);
				
				List<Map<String, String>> transactionList = pertoReader(currentFile, atmID, "0", maker, bankId);
				if (transactionList.size() > 0)
					SbiServiceImpl.transactionListMap.put(mapKey, transactionList);
			}
			logger.info("Exited pertoSplitter");
		} catch (Exception e) {
			logger.error("#### Exception inside pertoSplitter :: " +e.getMessage());
			commonUtility.moveFile(sourcePath + File.separator + currentFilePath.getName(),
					notSplittedFilePath + File.separator + currentFilePath.getName());
			bigFile.Close();
		} finally {
			bigFile.Close();
			transactionDates.clear();
			transactionDate = null;
			transactionDatefile = null;
			recordSplit = null;
		}
	}

	@SuppressWarnings("static-access")
	public List<Map<String, String>> pertoReader(String losgDistFile, String terminalName, String parsedLines,
			String maker, String banks) {
		List<Map<String, String>> transactionList = new ArrayList<Map<String, String>>();
		M24Utility m24Utility = null;
		try {
			logger.info("Inside pertoReader with filename " + losgDistFile
					+ " terminalName " + terminalName + " maker " + maker);
			byte[] readBuffer;
			String losgFileData = "";
			int fileSize = Integer.parseInt(String.valueOf(new File(losgDistFile).length()));

			final FileInputStream fis = new FileInputStream(losgDistFile);
			readBuffer = new byte[fileSize];
			while (fis.read(readBuffer) != -1) {
				losgFileData = m24Utility.ba2s(readBuffer);
				break;
			}
			fis.close();
			String[] EJbuffer = null;
			if (losgFileData.contains("\n")) {
				EJbuffer = losgFileData.split("\n");
			} else {
				EJbuffer = losgFileData.split("\r");
			}

			int parsedUntill;
			if (parsedLines == null || parsedLines == "") {
				parsedUntill = 0;
			} else {
				parsedUntill = Integer.parseInt(parsedLines);
			}
			int i = parsedUntill;
			String transactionStart = "<-- Transaction start -->";
			String transactionEnd = "*** Transaction End ***";
			List<List<String>> transactions = new ArrayList<List<String>>();
			while (i < EJbuffer.length) {
				if (EJbuffer[i].contains(transactionStart)) {
					List<String> temp = new ArrayList<String>();
					while (!EJbuffer[i - 1].contains(transactionEnd) && i < EJbuffer.length) {
						if (EJbuffer[i].contains(transactionEnd)) {
							parsedUntill = i;
							transactions.add(temp);
							i++;
							break;
						}
						temp.add(EJbuffer[i]);
						i++;
					};
				} else {
					i++;
				}
				if (i < EJbuffer.length) {
					if (EJbuffer[i].contains(transactionStart) && EJbuffer[i - 1].contains(transactionEnd)) {
						i++;
					}
				}
			}
			for (int j = 0; j < transactions.size(); j++) {
				Map<String, String> transactionParams = new HashMap<String, String>();
				List<String> transaction = transactions.get(j);
				transactionParams = commonUtility.parseTransactionParameters(transaction, maker, banks);

				if (transactionParams.get("TXNREMK") != null
						&& transactionParams.get("TXNREMK") == "TRANSACTION SUCCESSFUL"
						&& transactionParams.get("ERRORCODE") == null) {
					transactionParams.put("TXNREMK", "TRANSACTION SUCCESSFUL");
				}
				if (transactionParams.get("WITHDRAWAL") == null && transactionParams.get("AMOUNTENTERED") != null) {
					if (transactionParams.get("AMOUNTENTERED").length() > 0) {
						transactionParams.put("AMOUNTFAILED", transactionParams.get("AMOUNTENTERED"));
					}
				}
				if (transactionParams.get("ERRORCODE") != null) {
					String errorCode = transactionParams.get("ERRORCODE");
					if (errorCode.contains("M-13") || errorCode.contains("M-14") || errorCode.contains("M-18")) {
						transactionParams.put("TXNREMK", "SUSPECTED FRAUD");
						//commonUtility.notifyMSCTeam(transactionParams, bankId, terminalName);// M13 , M14 , M18
					}
				}
				transactionList.add(transactionParams);
			}
			logger.info("Populated transaction parameter list for terminalName ::" + terminalName);
			for (int j = 0; j < transactionList.size(); j++) {
				databaseUtil.addSchedulerEJTransactionDetails(terminalName, transactionList.get(j));
			}
			logger.info("Exiting pertoReader() ");
		} catch (Exception ex) {
			logger.error("#### Exception inside pertoReader :: "+ex.getMessage());
		}
		return transactionList;
	}
}
