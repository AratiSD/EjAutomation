package com.ej.automation.sbi.makes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class DieboldSplitter {
	public static final Logger logger = LoggerFactory.getLogger(DieboldSplitter.class);


	@Autowired
	private CommonUtil commonUtility;

	@Autowired
	private M24Utility m24Utility;

	@Autowired
	private SbiDatabaseUtil databaseUtil;

	@SuppressWarnings("static-access")
	public void dieboldSplitter(String currentFile, String bankId, String terminalId, String maker, String sourcePath,
			String destinationPath, String nonSplitPath, String countFile, HSSFSheet sheet, String uploadFolderPath) 
	{
		File currentFilePath=new File(currentFile);
	  	boolean atmIDcheck=true;
	  	String atmID = null;
		int count=1;
		String transactionDate = null;
		String transactionDatefile=null;
		BigFile bigFile=null;
		ArrayList<String> transactionDates = new ArrayList<String>();
		String[] transactionRecord = null;
		String[] transactionRecordSplit = null;
		StringBuilder readLineByLine1 = new StringBuilder();
		int transactionCheck = 0;
		String inputDate = "";
		String dateFormat = "";
		try 
		{
			logger.info(" Inside dieboldSplitter filename : " + currentFile);
			int start, end;
			boolean presenceofdate=false;
			BufferedWriter bufferedWriter = null;
			StringBuilder readLineByLine = new StringBuilder();
			File outputFilename = null;
			int dateField = 0;
			boolean fileExist = false;
			bigFile = new BigFile(currentFile);
			List<String> outputFilesList = new ArrayList<String>();
			logger.info("::: SplitterJob inside dieboldSplitter Entering for loop");
			for (String line : bigFile) 
			{
				count=count+1;
				
				start=readLineByLine1.length();
				readLineByLine1.append(line);
				end=readLineByLine1.length();
				if (line.toString().contains("DATEe") && line.toString().contains("TIMEe") && line.toString().contains("ATM ID")) 
				{
					transactionRecord = readLineByLine1.toString().split("a");
					if (transactionRecord.length != 0) 
					{
						for (dateField = 0; dateField < transactionRecord.length; dateField++) 
						{
							transactionRecord[dateField] = transactionRecord[dateField].trim();
							if (transactionRecord[dateField].contains("DATE") && transactionRecord[dateField].contains("TIME") && transactionRecord[dateField].contains("ATM ID")) 
							{
								presenceofdate=true;
								dateField = dateField + 1;
								transactionCheck++;
								break;
							}
						}
						if(!presenceofdate)
						{
							readLineByLine1.delete(start,end);
							transactionRecord=null;
							continue;	
						}
					}
					
					transactionRecord[dateField] = transactionRecord[dateField].trim();				
					
					if(transactionRecord[dateField].contains("d"))
						transactionRecord[dateField]=transactionRecord[dateField].replace("d","");
					if(transactionRecord[dateField].contains("e3s("))
						transactionRecord[dateField]=transactionRecord[dateField].replace("e3s(","");
					if(transactionRecord[dateField].contains("s("))
						transactionRecord[dateField]=transactionRecord[dateField].replace("s(","");
					if(transactionRecord[dateField].contains("e3"))
						transactionRecord[dateField]=transactionRecord[dateField].replace("e3"," ");
					if(transactionRecord[dateField].contains("e4"))
						transactionRecord[dateField]=transactionRecord[dateField].replace("e4"," ");
					if(transactionRecord[dateField].contains("e5"))
						transactionRecord[dateField]=transactionRecord[dateField].replace("e5"," ");
					if(transactionRecord[dateField].contains("e6"))
						transactionRecord[dateField]=transactionRecord[dateField].replace("e6"," ");
					
					transactionRecordSplit = transactionRecord[dateField].toString().split(" ");					
					transactionDate = null;
					
					if(atmIDcheck)
					{
						atmID=	transactionRecordSplit[transactionRecordSplit.length-1];
						atmIDcheck=false;
					}
					
					if(presenceofdate)
					{
						dateFormat="dd-MMM-yyyy";
						Date d= null;
						try{
							d = new SimpleDateFormat(dateFormat).parse(transactionRecordSplit[0]);
						}
						catch(Exception ex){
							dateFormat="MM/dd/yy";
						}
						if(d==null){
							dateFormat="MM/dd/yy";
						}
					transactionDatefile=commonUtility.getDateforFile(transactionRecordSplit[0], dateFormat,"ddMMyy");
					if(transactionDatefile==null)
					{
						transactionDatefile="error";
						bufferedWriter.write(System.lineSeparator());
						bufferedWriter.write("***********************************");
						bufferedWriter.write(System.lineSeparator());
					}
					fileExist = transactionDates.contains(transactionRecordSplit[0]);
	
					if (!fileExist) 
					{
						transactionDates.add(transactionRecordSplit[0]);
						if(!transactionDatefile.equalsIgnoreCase("error"))
						{
							outputFilename = commonUtility.folderCreation(destinationPath,
									bankId,maker,atmID+"_", transactionDatefile, ".txt");
							if(!outputFilesList.contains(outputFilename.getAbsolutePath())){
								outputFilesList.add(outputFilename.getAbsolutePath());
							}
							//System.out.println(outputFilename);
							bufferedWriter = new BufferedWriter(new FileWriter(outputFilename, true));
							bufferedWriter.write(readLineByLine.toString());
							bufferedWriter.write(System.lineSeparator());
						}
					} 
					else 
					{
						if(!transactionDatefile.equalsIgnoreCase("error"))
						{
							/*
							 * outputFilename =commonUtility.folderCreation(destinationPath,
							 * bankId,maker,transactionDatefile,atmID, ".txt");
							 */
							bufferedWriter = new BufferedWriter(new FileWriter(outputFilename, true));
							bufferedWriter.write(System.lineSeparator());
							bufferedWriter.write("***********************************");
							bufferedWriter.write(System.lineSeparator());
						}
					}
	
					for (int i = 0; i < transactionRecord.length; i++) 
					{
						if (!transactionRecord[i].isEmpty()) 
						{
							if(transactionRecord[i].contains("d") && transactionRecord[i].length()==1)
								transactionRecord[i]=transactionRecord[dateField].replace("d","");
							if(transactionRecord[i].contains("d  s("))
								transactionRecord[i]=transactionRecord[i].replace("d  s(", "");
							if(transactionRecord[i].contains("ddds("))
								transactionRecord[i]=transactionRecord[i].replace("ddds(", "");
							if(transactionRecord[i].contains("ddds'e4"))
								transactionRecord[i]=transactionRecord[i].replace("ddds'e4", "");
							if(transactionRecord[i].contains("dde3s("))
								transactionRecord[i]=transactionRecord[i].replace("dde3s(", "");
							if(transactionRecord[i].contains("s'e4"))
								transactionRecord[i]=transactionRecord[i].replace("s'e4", "");
							if(transactionRecord[i].contains("ds' "))
								transactionRecord[i]=transactionRecord[i].replace("ds' ", "");
							if(transactionRecord[i].contains("s' "))
								transactionRecord[i]=transactionRecord[i].replace("s' ", "");
							if(transactionRecord[i].contains("dds(e3"))
								transactionRecord[i]=transactionRecord[i].replace("dds(e3", "");
							if(transactionRecord[i].contains("ds(e3"))
								transactionRecord[i]=transactionRecord[i].replace("ds(e3", "");
							if(transactionRecord[i].contains("de3s("))
								transactionRecord[i]=transactionRecord[i].replace("de3s(", "");
							if(transactionRecord[i].contains("s(e3"))
								transactionRecord[i]=transactionRecord[i].replace("s(e3", "");
							if(transactionRecord[i].contains("e3s("))
								transactionRecord[i]=transactionRecord[i].replace("e3s(", "");
							if(transactionRecord[i].contains("s("))
								transactionRecord[i]=transactionRecord[i].replace("s(","");
							if(transactionRecord[i].contains("de3"))
								transactionRecord[i]=transactionRecord[i].replace("de3", " ");
							if(transactionRecord[i].contains("de4"))
								transactionRecord[i]=transactionRecord[i].replace("de4", " ");
							if(transactionRecord[i].contains("dd"))
								transactionRecord[i]=transactionRecord[i].replace("dd","");
							if(transactionRecord[i].contains("d "))
								transactionRecord[i]=transactionRecord[i].replace("d ","");
							if(transactionRecord[i].contains("e?"))
								transactionRecord[i]=transactionRecord[i].replace("e?", " ");
							if(transactionRecord[i].contains("e:"))
								transactionRecord[i]=transactionRecord[i].replace("e:", " ");
							if(transactionRecord[i].contains("e<"))
								transactionRecord[i]=transactionRecord[i].replace("e<", " ");
							if(transactionRecord[i].contains("e3"))
								transactionRecord[i]=transactionRecord[i].replace("e3", " ");
							if(transactionRecord[i].contains("e4"))
								transactionRecord[i]=transactionRecord[i].replace("e4", " ");
							if(transactionRecord[i].contains("e5"))
								transactionRecord[i]=transactionRecord[i].replace("e5", " ");
							if(transactionRecord[i].contains("e6"))
								transactionRecord[i]=transactionRecord[i].replace("e6", " ");
							if(transactionRecord[i].contains("e7"))
								transactionRecord[i]=transactionRecord[i].replace("e7", " ");
							if(transactionRecord[i].contains("e8"))
								transactionRecord[i]=transactionRecord[i].replace("e8", " ");
							if(transactionRecord[i].contains("e9"))
								transactionRecord[i]=transactionRecord[i].replace("e9", " ");
							if(transactionRecord[i].contains("dDATE"))
								transactionRecord[i]=transactionRecord[i].replace("dDATE", "DATE");
	
							bufferedWriter.write(transactionRecord[i].toString());
							bufferedWriter.write(System.lineSeparator());
						}
					}
					
					bufferedWriter.flush();
					readLineByLine1.setLength(0);
					transactionRecord = null;
					transactionDate = null;
					}
					else
					{
						readLineByLine1.delete(start,end);
					}
					presenceofdate=false;
				} 
				else 
				{
					readLineByLine1.append(System.lineSeparator());
					readLineByLine1.append("***********************************");
					readLineByLine1.append(System.lineSeparator());
				}
			}
			logger.info("::: SplitterJob inside dieboldSplitter Exited for loop");
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
				inputDate = commonUtility.getDateforFile(transactionRecordSplit[0], dateFormat, "dd-MM-yyyy");
				transactionDate = commonUtility.getDateforFile(transactionRecordSplit[0], dateFormat, "ddMMyy");
				String mapKey = atmID + transactionDate;
				TransactionForm transactionform = commonUtility.getTransactions(outputFilename, transactionDate,
						inputDate, bankId, atmID, currentFile);
				if (transactionform != null)
					SbiServiceImpl.transactionFormMap.put(mapKey, transactionform);
				
				/*
				 * List<Map<String, String>> transactionList = dieboldReader(currentFile, atmID,
				 * "0", maker,bankId); if (transactionList.size() > 0)
				 * SbiServiceImpl.transactionListMap.put(mapKey, transactionList);
				 */
			}
			logger.info("::: SplitterJob Exited dieboldSplitter");
		} 
		catch (Exception e) 
		{
			logger.error("#### Exception inside dieboldSplitter :: " +e.getMessage());
			e.printStackTrace();
			commonUtility.moveFile(sourcePath + File.separator + currentFilePath.getName(),
					nonSplitPath + File.separator + currentFilePath.getName());
			bigFile.Close();
		} 
		finally
		{	
			bigFile.Close();
			transactionDates.clear();
			transactionDate = null;
			transactionRecord = null;
			transactionRecordSplit = null;
		}
     }

	@SuppressWarnings("static-access")
	public List<Map<String, String>> dieboldReader(String losgDistFile, String terminalName, String parsedLines,
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
