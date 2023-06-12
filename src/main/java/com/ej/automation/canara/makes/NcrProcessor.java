package com.ej.automation.canara.makes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ej.automation.canara.model.RenamedFileInfo;
import com.ej.automation.canara.service.CanaraServiceImpl;
import com.ej.automation.canara.util.CommonUtil;

public class NcrProcessor {
	
	private File sourceFile;
	private String terminalName;
	private String terminalID;
	private String bankID;
	private String maker;
	private String splitterJobPath;
	
	public NcrProcessor(File sourceFile, String terminalName, String terminalID, String maker, String splitterJobPath, String bankID) {
		this.sourceFile=sourceFile;
		this.terminalName=terminalName;
		this.terminalID=terminalID;
		this.maker=maker;
		this.splitterJobPath=splitterJobPath;
		this.bankID=bankID;
	
	}
	

	public void rename() {
		// TODO Auto-generated method stub
		try {
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date yesterday = cal.getTime();
			String todayDateString= new SimpleDateFormat("ddMMyyyy").format(now);
			String fileName= sourceFile.getName();
			String[] fileNameSplit = fileName.split("______");
			if(fileNameSplit.length>1) {
				String fileDateString = fileNameSplit[1];
				fileDateString = fileDateString.substring(fileDateString.lastIndexOf("."));
				Date fileDate = new SimpleDateFormat("yyyyMMdd").parse(fileDateString);
				long fileSize = sourceFile.length();
				String destinationFileName = new SimpleDateFormat("ddMMyy").format(fileDate) + terminalName +".txt";
				String rootFolder= splitterJobPath;
				
				
				String yesterdayString = new SimpleDateFormat("ddMMyyyy").format(yesterday);
				if(fileDate.compareTo(yesterday)==0) {
					/*
					 * String destinationPath= rootFolder + File.separator + "Decryption" +
					 * File.separator + bankID + File.separator + "RenamedFiles" + File.separator +
					 * todayDateString + File.separator + yesterdayString+"(FSS)"+File.separator;
					 */
					String destinationPath= getDestinationPath("CurrentDay");
					CommonUtil.FileCopy(sourceFile.getAbsolutePath(), destinationPath+destinationFileName);
				}
				else {
					String destinationPath = getDestinationPath("Backdated");
					CommonUtil.FileCopy(sourceFile.getAbsolutePath(), destinationPath+destinationFileName);
				}
				String mapKey = terminalName + new SimpleDateFormat("ddMMyy").format(fileDate);
				RenamedFileInfo renamedFileInfo = populateRenamedFileInfo(fileSize, fileDate, now, fileName);
				if(renamedFileInfo!=null) {
					CanaraServiceImpl.RenamedFileInfoMap.put(mapKey, renamedFileInfo);
				}
			}
		}catch(Exception e) {
			
		}
	}
	
	private RenamedFileInfo populateRenamedFileInfo( long fileSize, Date fileDate, Date now, String fileName) {
		// TODO Auto-generated method stub
		try {
			RenamedFileInfo renamedFileInfo = new RenamedFileInfo();
			renamedFileInfo.setTerminalId(terminalID);
			renamedFileInfo.setTerminalName(terminalName);
			renamedFileInfo.setEjRenamedDate(new SimpleDateFormat("dd-MM-yyyy").format(fileDate));
			renamedFileInfo.setFileName(fileName);
			renamedFileInfo.setFileSize(fileSize);
			renamedFileInfo.setRenamedOn(new Timestamp(System.currentTimeMillis()));
			return renamedFileInfo;
		}catch(Exception e) {
			return null;
		}
		
	}


	public String getDestinationPath(String mode) {
		try {
			byte[] readBuffer;
			
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date yesterday = cal.getTime();
			String todayDateString= new SimpleDateFormat("ddMMyyyy").format(now);
			String yesterdayString = new SimpleDateFormat("ddMMyyyy").format(yesterday);
			
			String rootFolder= splitterJobPath;
			
			String folderNamesUsedRecord = rootFolder + File.separator + "Decryption" + File.separator + bankID + File.separator
					+ "RenamedFiles" + File.separator + todayDateString + File.separator+"folderNamesUsedRecord.txt"; 
			File folderNamesUsedRecordFile = new File(folderNamesUsedRecord);
			if(!folderNamesUsedRecordFile.exists()) {
				folderNamesUsedRecordFile.createNewFile();
			}
			int fileSize = Integer.parseInt(String.valueOf(new File(folderNamesUsedRecord).length()));
			final FileInputStream fis = new FileInputStream(folderNamesUsedRecord);
			readBuffer = new byte[fileSize];
			String folderNamesUsed="";
			while (fis.read(readBuffer) != -1) {
				folderNamesUsed = new String(readBuffer, StandardCharsets.UTF_8);
				break;
			}
			fis.close();
			
			if(mode.equals("CurrentDay")) {
				int i=0;
				Boolean folderDecided = false;
				while(!folderDecided) {
					String destinationPath= rootFolder + File.separator + "Decryption" + File.separator + bankID + File.separator
							+ "RenamedFiles" + File.separator + todayDateString + File.separator + yesterdayString+"(FSS)"+
							(i>0?"-"+i:"") + File.separator;
					if(!folderNamesUsed.contains(destinationPath+"---closed")) {
						folderDecided = true;
						if(!folderNamesUsed.contains(destinationPath+"---open")) {
							
							BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(folderNamesUsedRecordFile, true));
							bufferedWriter.write(destinationPath+"---open");
							bufferedWriter.write(System.lineSeparator());
							bufferedWriter.flush();
							bufferedWriter.close();
						}
					}
				}
			}
			else if(mode.equals("BackDated")) {
				int i=0;
				Boolean folderDecided = false;
				while(!folderDecided) {
					String destinationPath= rootFolder + File.separator + "Decryption" + File.separator + bankID + File.separator
							+ "RenamedFiles" + File.separator + todayDateString + File.separator + yesterdayString+"(Backdated)"+
							(i>0?"-"+i:"") + File.separator;
					if(!folderNamesUsed.contains(destinationPath+"---closed")) {
						folderDecided = true;
						if(!folderNamesUsed.contains(destinationPath+"---open")) {
							
							BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(folderNamesUsedRecordFile, true));
							bufferedWriter.write(destinationPath+"---open");
							bufferedWriter.write(System.lineSeparator());
							bufferedWriter.flush();
							bufferedWriter.close();
						}
					}
				}
			}
			String[] folderNames= folderNamesUsed.split(System.lineSeparator());
			for(String folderName : folderNames) {
				
			}
		}catch(Exception e) {
			
		}
		return null;
	}

}
