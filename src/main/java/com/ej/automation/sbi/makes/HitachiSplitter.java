package com.ej.automation.sbi.makes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
public class HitachiSplitter {
	public static final Logger logger = LoggerFactory.getLogger(HitachiSplitter.class);

	@Autowired
	private CommonUtil commonUtility;

	@Autowired
	private M24Utility m24Utility;

	@Autowired
	private SbiDatabaseUtil databaseUtil;

	@SuppressWarnings("static-access")
	public void hitachiSplitter(String currentFile, String bankId, String terminalId, String maker, String sourcePath,
			String destinationPath, String nonSplitPath, String countFile, HSSFSheet sheet, String uploadFolderPath) {

		String atmID=null;
		File currentFilePath = new File(currentFile);
		String transactionDate = null;
		String[] recordSplit = null;
		String inputDate = "";
		String dateFormat = "";
		Boolean fileExist = null;
		BigFile bigFile=null;
		int transactionCheck = 0;
		String transactionDatefile = null;
		String[] transactionDateSplit = null;
		StringBuilder readLineByLine = new StringBuilder();
		ArrayList<String> transactionDates = new ArrayList<String>();
		try {
			logger.info(" Inside hitachiSplitter filename : " + currentFile);
			BufferedWriter bufferedWriter = null;
			File outputFilename = null;
			bigFile = new BigFile(currentFile);
			Iterator<String> iterString = bigFile.iterator();
			List<String> outputFilesList = new ArrayList<String>();
			logger.info("::: SplitterJob inside hitachiSplitter Entering while loop");
			while (iterString.hasNext())
			{
				String readLine = iterString.next();
				if (readLine.contains("DATE") && readLine.contains("TIME") && readLine.contains("ATM ID"))
				{
					transactionCheck++;
					readLineByLine.append(readLine);
					readLineByLine.append(System.lineSeparator());
					if (iterString.hasNext())
						readLine = iterString.next();
					recordSplit = readLine.trim().split(" ");
					dateFormat="dd-MMM-yyyy";
					Date d= null;
					try{
						d = new SimpleDateFormat(dateFormat).parse(recordSplit[0]);
					}
					catch(Exception ex){
						dateFormat="MM/dd/yy";
					}
					if(d==null){
						dateFormat="MM/dd/yy";
					}
					transactionDate = commonUtility.getDate(recordSplit[0], dateFormat,0);
					atmID=recordSplit[recordSplit.length-1];
					transactionDatefile=commonUtility.getDateforFile(recordSplit[0], dateFormat,"ddMMyy");
					transactionDateSplit = transactionDate.split("-");
					fileExist = transactionDates.contains(recordSplit[0]);
					readLineByLine.append(readLine);
					if (!fileExist) {
						transactionDates.add(recordSplit[0]);
						if (transactionDateSplit.length >= 3) {
							readLineByLine.append(System.lineSeparator());
							outputFilename = commonUtility.folderCreation(destinationPath, bankId, maker, atmID+"_",
									transactionDatefile,  ".txt");
							if(!outputFilesList.contains(outputFilename.getAbsolutePath())){
								outputFilesList.add(outputFilename.getAbsolutePath());
							}
							//System.out.println(outputFilename);
							bufferedWriter = new BufferedWriter(new FileWriter(outputFilename, true));
							bufferedWriter.write(readLineByLine.toString());
							bufferedWriter.write(System.lineSeparator());
						}
					} else {
						if (transactionDateSplit.length >= 3) {
							bufferedWriter = new BufferedWriter(new FileWriter(outputFilename, true));
							bufferedWriter.write(System.lineSeparator());
							bufferedWriter.write(readLineByLine.toString());
							bufferedWriter.write(System.lineSeparator());
						}
					}
					readLineByLine.setLength(0);
					bufferedWriter.flush();
					bufferedWriter.close();
				}

				else if (Pattern.matches("\\-{12}[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}\\s[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}\\-{13}",readLine))
				{
					readLineByLine.append("--------------------------------------------------------------");
					readLineByLine.append("\n");
					readLineByLine.append(readLine.trim());
					readLineByLine.append("\n");
				}
				else
				{
					readLineByLine.append(readLine.trim());
					readLineByLine.append(System.lineSeparator());
				}
			}
			logger.info("::: SplitterJob inside hitachiSplitter Exited while loop");
			
			if (readLineByLine.toString().length() > 0 && transactionDates.size() > 0)
			{
				bufferedWriter = new BufferedWriter(new FileWriter(outputFilename, true));
				bufferedWriter.write(readLineByLine.toString());
				bufferedWriter.write(System.lineSeparator());
				readLineByLine.setLength(0);
				bufferedWriter.flush();
				bufferedWriter.close();
			}
			else{
				commonUtility.moveFile(sourcePath + File.separator + currentFilePath.getName(),
						nonSplitPath + File.separator + currentFilePath.getName());
			}
			
			for(String splittedFile : outputFilesList){
				String[] pathSplit = splittedFile.split("Splitted_Files");
				String relativePath = pathSplit[1];
				String absoluteUploadPath = uploadFolderPath+relativePath;
				String uploadFilePath = absoluteUploadPath.substring(0,absoluteUploadPath.lastIndexOf(File.separator));
				commonUtility.checkAndCreateFolders(uploadFilePath);
				commonUtility.moveFile(splittedFile,absoluteUploadPath);
			}
			
			if (transactionCheck == 0) {
				commonUtility.moveFile(sourcePath + File.separator + currentFilePath.getName(),
						nonSplitPath + File.separator + currentFilePath.getName());
			}
			logger.info("transactionCheck :: " + transactionCheck);
			logger.info("OutputFilename :: " +outputFilename);
			if (transactionCheck > 0) {
				bigFile.Close();
				inputDate = commonUtility.getDateforFile(recordSplit[0], dateFormat, "dd-MM-yyyy");
				transactionDate = commonUtility.getDateforFile(recordSplit[0], dateFormat, "ddMMyy");
				String mapKey = atmID + transactionDate;
				TransactionForm transactionform = commonUtility.getTransactions(outputFilename, transactionDate,
						inputDate, bankId, atmID, currentFile);
				if (transactionform != null)
					SbiServiceImpl.transactionFormMap.put(mapKey, transactionform);
				
				/*
				 * List<Map<String, String>> transactionList = hitachiReader(currentFile, atmID,
				 * "0", maker,bankId); if (transactionList.size() > 0)
				 * SbiServiceImpl.transactionListMap.put(mapKey, transactionList);
				 */
				
			}
			logger.info("::: SplitterJob Exited hitachiSplitter");
			
		} catch (Exception e) {
			commonUtility.moveFile(sourcePath + File.separator + currentFilePath.getName(),
					nonSplitPath + File.separator + currentFilePath.getName());
			bigFile.Close();
		}
		finally {
			bigFile.Close();
			transactionDates.clear();
			transactionDate = null;
			transactionDateSplit = null;
			recordSplit = null;
		}
	}


	@SuppressWarnings("static-access")
	public List<Map<String, String>> hitachiReader(String losgDistFile, String terminalName, String parsedLines,
			String maker,String banks) {
		List<Map<String, String>> transactionList = new ArrayList<Map<String, String>>();
		try {
			logger.info("::: Inside ncrReader with filename " + losgDistFile
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
			// String[] EJbuffer= losgFileData.split("\r");
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

			String transactionStart = "*TRANSACTION START*";
			String transactionEnd = "*PRIMARY CARD READER ACTIVATED*";
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
					}
					;
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

				// ashraf
				if (transactionParams.get("TXNREMK") == null && transactionParams.get("ERRORDESC") == null
						&& transactionParams.get("ERRORCODE") == null) {
					transactionParams.put("TXNREMK", "TRANSACTION SUCCESSFUL");
				}

				if (transactionParams.get("TXNREMK") == null && transactionParams.get("RESPCODE") != null) {
					if ((!transactionParams.get("RESPCODE").equals("000"))
							|| ((!transactionParams.get("RESPCODE").equals("001")))) {
						transactionParams.put("TXNREMK", "TRANSACTION FAILURE");
					}
				}
				// ashraf

				if (transactionParams.get("WITHDRAWAL") == null && transactionParams.get("AMOUNTENTERED") != null) {
					if (transactionParams.get("AMOUNTENTERED").length() > 0) {
						transactionParams.put("AMOUNTFAILED", transactionParams.get("AMOUNTENTERED"));
					}
				}

				if (transactionParams.get("ERRORCODE") != null) {
					String errorCode = transactionParams.get("ERRORCODE");
					if(transactionParams.get("ERRORCODE").contains("E") && (transactionParams.get("ERRORCODE").contains("*2") || transactionParams.get("ERRORCODE").contains("*3"))){
						if (errorCode.contains("M-13") || errorCode.contains("M-14") || errorCode.contains("M-18")
								|| errorCode.contains("M-19")) {
							transactionParams.put("TXNREMK", "SUSPECTED FRAUD");
							//commonUtility.notifyMSCTeam(transactionParams, banks, terminalName);// M13 , M14 , M18 and M19
						}
					}
				}
				transactionList.add(transactionParams);
			}

			logger.info("Populated transaction parameter list for terminalName ::" + terminalName);

			for (int j = 0; j < transactionList.size(); j++) {
				databaseUtil.addSchedulerEJTransactionDetails(terminalName, transactionList.get(j));
			}
			logger.info("Exiting ncrReader() ");
		} catch (Exception ex) {
			logger.error("#### Exception inside ncrReader :: "+ex.getMessage());
		}
		return transactionList;
	}
}
