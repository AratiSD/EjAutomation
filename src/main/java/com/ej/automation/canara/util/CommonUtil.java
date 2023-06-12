package com.ej.automation.canara.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ej.automation.sbi.model.BigFile;
import com.ej.automation.sbi.model.Constants;
import com.ej.automation.sbi.model.SendEmailReport;
import com.ej.automation.sbi.model.TransactionDescriptor;
import com.ej.automation.sbi.model.TransactionForm;

@Component
public class CommonUtil {

	public static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	@Value("${MSC.ALERT.RECIPIENTS}")
	private String MSC_ALERT_RECIPIENTS;

	@Value("${MSC.ALERT.FROM}")
	private String MSC_ALERT_FROM;

	@Value("${EMAIL.HOST}")
	private String EMAIL_HOST;
	
	@Value("${SPLITTER.REPORT.RECIPIENTS}")
	private String SPLITTER_REPORT_RECIPIENTS;
	
	@Value("${SPLITTER.REPORT.FROM}")
	private String SPLITTER_REPORT_FROM;

	@Value("${SPLITTER.REPORT.SUBJECT}")
	private String SPLITTER_REPORT_SUBJECT;
	
	@Value("${SPLITTER.REPORT.BODY}")
	private String SPLITTER_REPORT_BODY;
	
	@Value("${RECON.REPORT.RECIPIENTS}")
	private String RECON_REPORT_RECIPIENTS;
	
	@Value("${RECON.REPORT.FROM}")
	private String RECON_REPORT_FROM;
	
	@Value("${DAILY.RECON.REPORT.SUBJECT}")
	private String DAILY_RECON_REPORT_SUBJECT;
	
	@Value("${DAILY.RECON.REPORT.BODY}")
	private String DAILY_RECON_REPORT_BODY;
	
	@Value("${EJDescriptorFileName}")
	private String ejDescriptorFileName;
	
	public static enum Operation {
		FoundOnSameLine, SetValueIfFound, FoundOnDifferentLine
	}

	public static String getDate(String transactionDate, String type, int count) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
		try {
			Date parseDate = simpleDateFormat.parse(transactionDate);
			String formattedDate = new SimpleDateFormat("yyyy-MMMM-dd").format(parseDate);
			return formattedDate;
		} catch (Exception e) {
			logger.error("Exception inside [CommonUtility:getDate] : " + e.getMessage());
		} finally {
			simpleDateFormat = null;
		}
		return null;
	}

	public static String getDateforFile(String transactionDate, String type, String formatForFileName) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
		try {
			Date parseDate = simpleDateFormat.parse(transactionDate);
			String formattedDate = new SimpleDateFormat(formatForFileName).format(parseDate);
			return formattedDate;
		} catch (Exception e) {
			logger.error("Exception inside [CommonUtility:getDateforFile] : " + e.getMessage());
		} finally {
			simpleDateFormat = null;
		}
		return null;
	}

	public static File folderCreation(String filePath1, String bankId1, String maker1, String... params) {
		logger.info("Inside folderCreation with file path :: " + filePath1);
		File fileName = null;

		try {
			checkAndCreateFolders(filePath1);
			params[0] = params[0].replaceAll("/", "");
			filePath1 = filePath1 + File.separator + params[0] + params[1] + params[2];
			fileName = checkAndCreateFile(filePath1);
		} catch (Exception ex) {
			logger.error("Exception inside folderCreation :: " + ex.getMessage());
		}
		return fileName;
	}

	private static synchronized File checkAndCreateFile(String fileName) {
		File file = null;
		try {
			file = new File(fileName);
			logger.info("Inside checkAndCreateFile File name :: "+fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception ex) {
			logger.error("Exception inside checkAndCreateFile :: " + ex.getMessage());
		}
		return file;
	}

	public static synchronized void checkAndCreateFolders(String folderName) {
		logger.info("Inside checkAndCreateFolders Folder path "+folderName);
		try {
			File folder = new File(folderName);
			if (!folder.exists())
				folder.mkdirs();
		} catch (Exception ex) {
			logger.error("Exception inside checkAndCreateFolders :: " + ex.getMessage());
		}
	}

	public static boolean moveFile(String sourcePath, String targetPath) {
		boolean fileMoved = true;
		try {
			checkAndCreateFolders(targetPath);
			FileCopy(sourcePath, targetPath);
		} catch (Exception e) {
			logger.error("Exception inside moveFile :: " + e.getMessage());
			fileMoved = false;
		}
		return fileMoved;
	}

	public static synchronized void FileCopy(String Source, String Destination) {
		try {
			logger.info("Inside FileCopy ");
			File target = new File(Destination);
			if (target.exists())
				target.delete();
			FileInputStream fin = new FileInputStream(Source);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Destination));
			byte[] bytesIn = new byte[4096];
			int read = 0;
			while ((read = fin.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
			bos.close();
			fin.close();
		} catch (Exception ex) {
			logger.error("Exception inside FileCopy :: " + ex.getMessage());
		}
	}

	public static TransactionForm getTransactions(File fileEntry, String transactionDate, String inputDate,
			String bankId, String terminalId, String sourcreFile) {
		try {
			logger.info("::: SplitterJob inside getTransactions() with filename: " + fileEntry
					+ " transactionDate: " + transactionDate + " inputDate: " + inputDate + " bankId: " + bankId);
			int startSequence = 0;
			int nextSequence = 0;
			int totalSequenceCount = 0;
			int financialCount = 0;
			int nonFinancialCount = 0;
			boolean firstFlag = true;
			String transactionDesc = "";
			String transactionNumber = "";
			BigFile bigFile;
			StringBuffer missingSequence = new StringBuffer();
			TransactionForm form = new TransactionForm();
			int errTxnNo = 0;
			if (fileEntry != null) {

				if (fileEntry.getName().contains(transactionDate)) {
					bigFile = new BigFile(fileEntry.getAbsolutePath());
					CopyOnWriteArrayList<String> arraylist = new CopyOnWriteArrayList<String>();
					for (String string : bigFile) {
						arraylist.add(string);
					}
					ListIterator<String> listIterator = arraylist.listIterator();

					while (listIterator.hasNext()) {
						String string = listIterator.next();
						if (string.contains("RECORD NO") || (string.contains("TXN") && string.contains("NO"))) {
							//logger.info("lineNo ::" + string);
							transactionNumber = string.replace("RECORD", "").replace("TXN", "").replace("NO", "")
									.replace("NUMBER", "").replace(".", "").replace(":", "").trim();
							int txnNumber = 0;
							if (validateNumber(transactionNumber))
								txnNumber = Integer
										.parseInt(transactionNumber.equalsIgnoreCase("") ? "0" : transactionNumber);
							//logger.info("txnNumber :: " + txnNumber);
							if (txnNumber != 0) {
								errTxnNo = txnNumber;
								if (listIterator.hasNext()) {
									string = listIterator.next().trim();
									if (string.equalsIgnoreCase("")) {
										string = listIterator.next().trim();
									}
									if (string.contains("RESPONSE CODE") || string.contains("RESP.CODE")) {
										if (listIterator.hasNext()) {
											string = listIterator.next().trim();
											if (string.equalsIgnoreCase("")) {
												string = listIterator.next().trim();
											}
											transactionDesc = string;
										}
									} else {
										if (string.contains("REFERENCE")) {
											if (listIterator.hasNext()) {
												string = listIterator.next().trim();
												if (string.contains("RESPONSE CODE") || string.contains("RESP.CODE")) {
													if (listIterator.hasNext()) {
														string = listIterator.next().trim();
														if (string.equalsIgnoreCase("")) {
															string = listIterator.next().trim();
														}
														transactionDesc = string;
													}
												}
											}
										} else
											transactionDesc = string;
									}
									if (firstFlag) {
										startSequence = txnNumber;
										firstFlag = false;
										nextSequence = startSequence + 1;
										totalSequenceCount = totalSequenceCount + 1;
										if (transactionDesc != null && (transactionDesc.contains("DEPOSIT")
												|| transactionDesc.contains("WDL")
												|| transactionDesc.contains("WITHDRAWAL"))) {
											financialCount = financialCount + 1;
											transactionDesc = null;
										} else {
											nonFinancialCount = nonFinancialCount + 1;
											transactionDesc = null;
										}
										if (nextSequence == 10000) {
											nextSequence = 1;
										}

									} else {
										totalSequenceCount = totalSequenceCount + 1;
										if (transactionDesc != null && (transactionDesc.contains("DEPOSIT")
												|| transactionDesc.contains("WDL")
												|| transactionDesc.contains("WITHDRAWAL"))) {
											financialCount = financialCount + 1;
											transactionDesc = null;
										} else {
											nonFinancialCount = nonFinancialCount + 1;
											transactionDesc = null;
										}
										if (nextSequence == txnNumber) {
											nextSequence = nextSequence + 1;
										} else {
											int diff = txnNumber - nextSequence;
											for (int j = 0; j < diff + 1; j++) {
												if (nextSequence != txnNumber) {
													missingSequence.append(nextSequence + ",");
													nextSequence = nextSequence + 1;
												} else {
													nextSequence = nextSequence + 1;
												}
											}
											if (nextSequence == 10000)
												nextSequence = 1;
										}
									}
								}
							}
						}
					}
					form.setStartingSequenceNumber(startSequence);
					form.setEndingSequenceNumber(nextSequence - 1);
					form.setTotaltxnCount(totalSequenceCount);
					form.setTerminalId(terminalId);
					form.setStartDate(inputDate);
					form.setFinancialtxnCount(financialCount);
					form.setNonFinancialtxnCount(nonFinancialCount);
					form.setFileName(sourcreFile);
					
					File file=new File(sourcreFile);
					form.setFileSize(file.length());
					
					String[] namelist = file.getName().split("_");
					form.setTerminalName(namelist[0].trim());
					
					form.setSponDate(new Timestamp(System.currentTimeMillis()));
					
					if (missingSequence.toString().contains(",")) {
					String missingSequenceNumber = missingSequence.toString().substring(0, missingSequence.toString().length() - 1);
					 if(missingSequenceNumber.length()>3900){
					 missingSequenceNumber = missingSequenceNumber.substring(0,3900);
						 missingSequenceNumber = missingSequenceNumber.substring(0,missingSequenceNumber.lastIndexOf(','));
						 missingSequenceNumber += "...";
						 }
						form.setMissingSequenceNumber(missingSequenceNumber);
					} else {
					form.setMissingSequenceNumber("0");
					}
					bigFile.Close();
				}
			}
			startSequence = 0;
			nextSequence = 0;
			totalSequenceCount = 0;
			financialCount = 0;
			firstFlag = true;
			nonFinancialCount = 0;
			missingSequence = new StringBuffer();
			logger.info("::: SplitterJob inside getTransactions() exited with form : " + form);
			return form;
		} catch (Exception e) {
			logger.error("Exception inside getTransactions :: " + e.getMessage());
			return null;
		}
	}

	private static boolean validateNumber(String transactionNumber) {
		boolean flag = false;
		Pattern pat = Pattern.compile("[,\\d]+");
		Matcher m = pat.matcher(transactionNumber);
		flag = m.matches();
		return flag;
	}

	public void notifyMSCTeam(Map<String, String> transactionParams, String BankId, String terminalName) {
		try {
			StringBuffer mailbody = new StringBuffer();

			mailbody.append("<html>");
			mailbody.append("<head>");
			mailbody.append("</head>");
			mailbody.append("<body>");
			mailbody.append("<div class=\"section-1\">");
			mailbody.append("<h4>");
			mailbody.append("Dear Team,");
			mailbody.append("</h4>");
			mailbody.append("<h4>");
			mailbody.append(
					"An issue has been identified for the terminal <b></b>. Please refer the details given below");
			mailbody.append("</h4>");
			mailbody.append("</div>");
			mailbody.append("<div class=\"amount-2\">");
			mailbody.append("<table style=\"border-collapse: collapse;\">");
			mailbody.append("<thead style=\"background-color: #31849B; color: white;\">");
			mailbody.append("<tr>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Bank ID</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Branch</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Date</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Time</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">ATM ID</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">FILEX Terminal ID</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Transaction Number</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Response code</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Card Number</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Amt entered</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Successful Amt</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Failed Amt</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Error</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Error Detail</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">Transaction Description</th>");
			mailbody.append(
					"<th class=\"th-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">remark</th>");
			mailbody.append("</tr>");
			mailbody.append("</thead>");
			mailbody.append("<tbody>");
			mailbody.append("<tr>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(BankId);
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("BRANCH") != null ? transactionParams.get("BRANCH") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;min-width:50px;\">");
			mailbody.append(transactionParams.get("DATE") != null ? transactionParams.get("DATE") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;min-width:50px;\">");
			mailbody.append(transactionParams.get("TIME") != null ? transactionParams.get("TIME") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("ATM ID") != null ? transactionParams.get("ATM ID") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(terminalName);
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("TXN NO") != null ? transactionParams.get("TXN NO") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("RESPCODE") != null ? transactionParams.get("RESPCODE") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("CARDNUMBER") != null ? transactionParams.get("CARDNUMBER") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(
					transactionParams.get("AMOUNTENTERED") != null ? transactionParams.get("AMOUNTENTERED") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("WITHDRAWAL") != null ? transactionParams.get("WITHDRAWAL") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("AMOUNTFAILED") != null ? transactionParams.get("AMOUNTFAILED") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("ERRORCODE") != null ? transactionParams.get("ERRORCODE") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("ERRORDESC") != null ? transactionParams.get("ERRORDESC") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("TXNDESC") != null ? transactionParams.get("TXNDESC") : "");
			mailbody.append("</td>");
			mailbody.append(
					"<td class=\"td-data\" style=\"border: 0.5px solid black;font-size: 12px;padding: 5px 7px;font-weight: 500;letter-spacing: 0.5px;\">");
			mailbody.append(transactionParams.get("TXNREMK") != null ? transactionParams.get("TXNREMK") : "");
			mailbody.append("</td>");
			mailbody.append("</tr>");
			mailbody.append("</tbody>");
			mailbody.append("</table>");
			mailbody.append("</div>");
			mailbody.append("<div class=\"footer\">");
			mailbody.append("<h4>Regards,</h4>");
			mailbody.append("<h5>EJ Team</h5>");
			mailbody.append("<small>Please note this is a system generated email ,So please do not reply</small>");
			mailbody.append("</div>");
			mailbody.append("</body>");
			mailbody.append("</html>");
			String RecipientList = MSC_ALERT_RECIPIENTS;
			String Sender = MSC_ALERT_FROM;
			String mailHost = EMAIL_HOST;
			String body = mailbody.toString();

			int result = SendEmailReport.sendEmail(mailHost, Sender, // from
					RecipientList, // to
					"Issue Detected in EJ for an ATM in " + BankId, // sub
					body// body
			);
			logger.info("Alert mail for Terminal :: " + terminalName + " Status :: " + result);
		} catch (Exception e) {
			logger.error("Exception inside notifyMSCTeam :: " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public static void populateReconReportSheet(List<Map<String, String>> transactionList, HSSFWorkbook wb,
			String terminalName) {
		try {
			//logger.info("Inside PopulateReconReportSheet()");
			int sn = wb.getNumberOfSheets();
			HSSFSheet sheet = wb.getSheetAt(sn - 1);
			int rownum = sheet.getLastRowNum();
			int i = 0;
			for (Map<String, String> reconMap : transactionList) {
				HSSFRow row = null;
				if (rownum < 65500 && rownum >= 0) {
					row = sheet.createRow(++rownum);
				} else {
					int SheetNumber = wb.getNumberOfSheets();
					sheet = wb.createSheet("sheet " + (++SheetNumber));
					rownum = sheet.getLastRowNum();
					row = sheet.createRow(++rownum);
				}
				short j = 0;
				// inserting data in the first row
				row.createCell(j++).setCellValue(new HSSFRichTextString(Integer.toString(i + 1)));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("BRANCH")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("DATE")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("TIME")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("ATM ID")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(terminalName));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("TXN NO")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("RESPCODE")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("CARDNUMBER")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("AMOUNTENTERED")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("WITHDRAWAL")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("AMOUNTFAILED")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("ERRORCODE")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("ERRORDESC")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("TXNDESC")));
				row.createCell(j++).setCellValue(new HSSFRichTextString(reconMap.get("TXNREMK")));
				i++;
				// logger.info("exiting PopulateReconReportSheet()");
			}
		} catch (Exception ex) {
			logger.error("#### Exception inside PopulateReconReportSheet :: " + ex.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public Hashtable<String, List<TransactionDescriptor>> setTransactionDescriptors(String bank) {
		//logger.info("inside setTransactionDescriptors");
		Hashtable<String, List<TransactionDescriptor>> transactionDescriptorTable = new Hashtable<String, List<TransactionDescriptor>>();

		List<TransactionDescriptor> transactionDescriptors = new ArrayList<TransactionDescriptor>();
		try {
			String EJDescriptorFileName = ejDescriptorFileName+File.separator + bank + ".xls";
			// String
			// EJDescriptorFileName=Constants.HOME_DIR+"EJDescriptors/"+bankId+".xls";
			//logger.error("EJDescriptorFileName ::"+EJDescriptorFileName);
			InputStream inputfile = new FileInputStream(EJDescriptorFileName);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputfile);

			int sheetCount = hssfWorkbook.getNumberOfSheets();
			for (int j = 0; j < sheetCount; j++) {
				HSSFSheet sheet = hssfWorkbook.getSheetAt(j);
				String sheetname = hssfWorkbook.getSheetName(j);
				int rowCount = sheet.getPhysicalNumberOfRows();
				for (int i = 2; i < rowCount; i++) {
					TransactionDescriptor transactionDescriptor = new TransactionDescriptor();
					HSSFRow row = sheet.getRow(i);
					HSSFCell parameterCell = row.getCell((short) 1);
					HSSFCell operationCell = row.getCell((short) 2);
					HSSFCell containsCell = row.getCell((short) 3);
					HSSFCell excludesCell = row.getCell((short) 4);
					HSSFCell containsEitherCell = row.getCell((short) 5);
					HSSFCell splitOnCell = row.getCell((short) 6);
					HSSFCell foundAtIndexCell = row.getCell((short) 7);
					HSSFCell foundOnLineCell = row.getCell((short) 8);
					HSSFCell trimMultipleSpacesCell = row.getCell((short) 9);
					HSSFCell replaceStringCell = row.getCell((short) 10);
					HSSFCell replaceWithCell = row.getCell((short) 11);
					HSSFCell substringStartCell = row.getCell((short) 12);
					HSSFCell substringEndCell = row.getCell((short) 13);
					HSSFCell valueCell = row.getCell((short) 14);
					HSSFCell dontWriteIfExistCell = row.getCell((short) 15);

					if (parameterCell != null)
						if (parameterCell.getCellType() == 1) {
							String parameter = parameterCell.getRichStringCellValue().toString();
							transactionDescriptor.setParameter(parameter);
						}
					if (operationCell != null)
						if (operationCell.getCellType() == 1) {
							String operation = operationCell.getRichStringCellValue().toString();
							transactionDescriptor.setOperation(operation);
						}
					if (containsCell != null)
						if (containsCell.getCellType() == 1) {
							String containsString = containsCell.getRichStringCellValue().toString();
							String[] contains = getStringArray(containsString);
							transactionDescriptor.setContains(contains);
						}
					if (excludesCell != null)
						if (excludesCell.getCellType() == 1) {
							String excludesString = excludesCell.getRichStringCellValue().toString();
							String[] excludes = getStringArray(excludesString);
							transactionDescriptor.setExcludes(excludes);
						}
					if (containsEitherCell != null)
						if (containsEitherCell.getCellType() == 1) {
							String containsEitherString = containsEitherCell.getRichStringCellValue().toString();
							String[] containsEither = getStringArray(containsEitherString);
							transactionDescriptor.setContainsEither(containsEither);
						}
					if (splitOnCell != null)
						if (splitOnCell.getCellType() == 1) {
							String splitOn = splitOnCell.getRichStringCellValue().toString().replace("\"", "");
							transactionDescriptor.setSplitOn(splitOn);
						}
					if (foundAtIndexCell != null)
						if (foundAtIndexCell.getCellType() == 0) {
							int foundAtIndex = (int) foundAtIndexCell.getNumericCellValue();
							transactionDescriptor.setFoundAtIndex(foundAtIndex);
						}
					if (foundOnLineCell != null)
						if (foundOnLineCell.getCellType() == 0) {
							int foundOnLine = (int) foundOnLineCell.getNumericCellValue();
							transactionDescriptor.setFoundOnLine(foundOnLine);
						}
					if (trimMultipleSpacesCell != null)
						if (trimMultipleSpacesCell.getCellType() == 1) {
							String trimMultipleSpacesString = trimMultipleSpacesCell.getRichStringCellValue()
									.toString();
							boolean trimMultipleSpaces = trimMultipleSpacesString.equalsIgnoreCase("Yes") ? true
									: false;
							transactionDescriptor.setTrimMultipleSpaces(trimMultipleSpaces);
						}
					if (replaceStringCell != null)
						if (replaceStringCell.getCellType() == 1) {
							String replaceString = replaceStringCell.getRichStringCellValue().toString().replace("\"",
									"");
							transactionDescriptor.setReplaceThis(replaceString);
						}
					if (replaceWithCell != null)
						if (replaceWithCell.getCellType() == 1) {
							String replaceWith = replaceWithCell.getRichStringCellValue().toString().replace("\"", "");
							transactionDescriptor.setReplaceWith(replaceWith);
						}
					if (substringStartCell != null)
						if (substringStartCell.getCellType() == 0) {
							int substringStart = (int) substringStartCell.getNumericCellValue();
							transactionDescriptor.setSubstringStart(substringStart);
						}
					if (substringEndCell != null)
						if (substringEndCell.getCellType() == 0) {
							int substringEnd = (int) substringEndCell.getNumericCellValue();
							transactionDescriptor.setSubstringEnd(substringEnd);
						}
					if (valueCell != null)
						if (valueCell.getCellType() == 1) {
							String value = valueCell.getRichStringCellValue().toString().replace("\"", "");
							transactionDescriptor.setValue(value);
						}
					if (dontWriteIfExistCell != null)
						if (dontWriteIfExistCell.getCellType() == 1) {
							String dontWriteIfExistString = dontWriteIfExistCell.getRichStringCellValue().toString();
							boolean dontWriteIfExist = dontWriteIfExistString.equalsIgnoreCase("Yes") ? true : false;
							transactionDescriptor.setDontWriteIfExists(dontWriteIfExist);
						}
					if (transactionDescriptor.getOperation() != null && transactionDescriptor.getParameter() != null)
						transactionDescriptors.add(transactionDescriptor);
				}
				transactionDescriptorTable.put(sheetname, transactionDescriptors);
			}
			inputfile.close();
		} catch (Exception ex) {
			logger.error("#### Exception inside SetTransactionDescriptors :: " + ex.getMessage());
		}
		return transactionDescriptorTable;
	}

	private static String[] getStringArray(String input) {
		String[] strings = input.split(",");
		for (int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].replace("\"", "");
		}
		return strings;
	}

	public static String cleanTargetLine(TransactionDescriptor transactionDescriptor, String value) {
		Boolean trimMultipleSpaces = transactionDescriptor.isTrimMultipleSpaces();
		if (trimMultipleSpaces != null) {
			if (trimMultipleSpaces == true)
				value = value.trim().replaceAll(" +", " ");
		}
		return value;
	}

	public static Boolean checkConditions(TransactionDescriptor transactionDescriptor, String transactionLine) {
		boolean conditionsMet = true;
		String[] containsList = transactionDescriptor.getContains();
		String[] excludesList = transactionDescriptor.getExcludes();
		String[] containsEitherList = transactionDescriptor.getContainsEither();
		if (containsList != null) {
			Boolean contains = containsWords(transactionLine, containsList);
			conditionsMet = conditionsMet ? contains : conditionsMet;
		}
		if (excludesList != null) {
			Boolean excludes = excludesWords(transactionLine, excludesList);
			conditionsMet = conditionsMet ? excludes : conditionsMet;
		}
		if (containsEitherList != null) {
			Boolean containsEither = containsEitherWords(transactionLine, containsEitherList);
			conditionsMet = conditionsMet ? containsEither : conditionsMet;
		}
		return conditionsMet;
	}

	public static String cleanTransactionLine(TransactionDescriptor transactionDescriptor, String transactionLine) {
		Boolean trimMultipleSpaces = transactionDescriptor.isTrimMultipleSpaces();
		String replace = transactionDescriptor.getReplaceThis();
		String with = transactionDescriptor.getReplaceWith();
		if (trimMultipleSpaces != null) {
			if (trimMultipleSpaces == true)
				transactionLine = transactionLine.trim().replaceAll(" +", " ");
		}
		if (replace != null && with != null) {
			transactionLine = transactionLine.replaceAll(replace, with);
		}
		return transactionLine;
	}

	public static String cleanTransactionValue(TransactionDescriptor transactionDescriptor, String value) {
		int start = transactionDescriptor.getSubstringStart();
		int end = transactionDescriptor.getSubstringEnd();
		if (end != 0)
			value = value.substring(start, end);
		else
			value = value.substring(start);
		return value;
	}

	private static Boolean excludesWords(String inputString, String[] items) {
		boolean found = true;
		for (String item : items) {
			if (inputString.contains(item)) {
				found = false;
				break;
			}
		}
		return found;
	}

	public static boolean containsWords(String inputString, String[] items) {
		boolean found = true;
		for (String item : items) {
			if (!inputString.contains(item)) {
				found = false;
				break;
			}
		}
		return found;
	}

	public static boolean containsEitherWords(String inputString, String[] items) {
		boolean found = false;
		for (String item : items) {
			if (inputString.contains(item)) {
				found = true;
				break;
			}
		}
		return found;
	}

	@SuppressWarnings("static-access")
	public static Map<String, String> applyTransformations(List<String> transaction, int index,
			Map<String, String> transactionParams, TransactionDescriptor transactionDescriptor) {
		try {
			String splitOn = "", parameter = "", value = "";
			Boolean conditionsMet;
			int foundAtIndex, foundOnLine;
			String operationString = transactionDescriptor.getOperation();
			Operation operation = Operation.valueOf(operationString);
			String transactionLine = transaction.get(index);
			Boolean dontWriteIfExist = (Boolean) transactionDescriptor.isDontWriteIfExists();
			switch (operation) {
			case FoundOnSameLine:
				splitOn = transactionDescriptor.getSplitOn();
				foundAtIndex = transactionDescriptor.getFoundAtIndex();
				parameter = transactionDescriptor.getParameter();
				transactionLine = cleanTransactionLine(transactionDescriptor, transactionLine);
				conditionsMet = checkConditions(transactionDescriptor, transactionLine);
				if (conditionsMet) {
					if (splitOn != null) {
						String[] lineSplit = transactionLine.split(splitOn);
						try {
						value = lineSplit[foundAtIndex].trim();
						}
						catch(ArrayIndexOutOfBoundsException ex) {
							logger.error("#### Exception inside applyTransformations FoundOnSameLine :: " + ex.getMessage());
							value="";
						}
					} else if (splitOn == null) {
						value = transactionLine;
					}
					value = cleanTransactionValue(transactionDescriptor, value);
					if (dontWriteIfExist != null) {
						if (dontWriteIfExist) {
							if (transactionParams.get(parameter) != null) {
								break;
							}
						}
					}
					transactionParams.put(parameter, value);
				}
				break;
			case SetValueIfFound:
				parameter = transactionDescriptor.getParameter();
				transactionLine = cleanTransactionLine(transactionDescriptor, transactionLine);
				conditionsMet = checkConditions(transactionDescriptor, transactionLine);
				value = transactionDescriptor.getValue();
				if (conditionsMet && value != null) {
					if (dontWriteIfExist != null) {
						if (dontWriteIfExist) {
							if (transactionParams.get(parameter) != null) {
								break;
							}
						}
					}
					transactionParams.put(parameter, value);
				}
				break;
			case FoundOnDifferentLine:
				splitOn = transactionDescriptor.getSplitOn();
				foundAtIndex = transactionDescriptor.getFoundAtIndex();
				parameter = transactionDescriptor.getParameter();
				foundOnLine = transactionDescriptor.getFoundOnLine();
				transactionLine = cleanTransactionLine(transactionDescriptor, transactionLine);
				conditionsMet = checkConditions(transactionDescriptor, transactionLine);
				if (conditionsMet) {
					value = transaction.get(index);
					if (foundOnLine != 0) {
						if (transaction.size() >= foundOnLine + index) {
							value = transaction.get(foundOnLine + index);
						}
					}
					value = cleanTargetLine(transactionDescriptor, value);
					if (splitOn != null) {
						String[] lineSplit = value.split(splitOn);
						value = lineSplit[foundAtIndex];
					}
					value = value.trim();
					value = cleanTransactionValue(transactionDescriptor, value);
					if (dontWriteIfExist != null) {
						if (dontWriteIfExist) {
							if (transactionParams.get(parameter) != null) {
								break;
							}
						}
					}
					transactionParams.put(parameter, value);
				}
				break;
			}
		} catch (Exception e) {
			logger.error("#### Exception inside applyTransformations :: " + e.getMessage());
		}
		return transactionParams;
	}

	// generate 6 digit random number
	public static String generateSixDigitRRN() {
		Random randomNumber = new Random();
		String randomString = "";
		int n = (int) (000000 + randomNumber.nextFloat() * 900000);
		randomString = String.valueOf(n);
		return randomString;
	}
	
	@SuppressWarnings("unused")
	public void sendDailyReconReport(String folderPath, String bankId, HSSFWorkbook wb) {
		try {
			logger.info("Inside SendDailyReconReport");
			String pathName = folderPath + Constants.FILE_SEPARATOR + Constants.DECRYPTION_FOLDER
					+ Constants.FILE_SEPARATOR + bankId + Constants.FILE_SEPARATOR + "Reports"
					+ Constants.FILE_SEPARATOR + "Recon Report" + Constants.FILE_SEPARATOR;
			logger.info("pathName :: "+pathName);
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
			Date d = new Date();
			SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyy");
			if (new File(pathName).exists()) {
				File path = new File(pathName);
				File[] files = path.listFiles();
				if (files != null) {
					for (File f : files) {
						if (f.isFile() && f.getName().startsWith("DailyReconReport_" + dateformat.format(d))
								&& (f.getName().endsWith(".xls") || f.getName().endsWith(".zip"))) {
							f.delete();
						}
					}
				}
			}
			/*
			 * String outFilePath = folderPath + Constants.FILE_SEPARATOR +
			 * Constants.DECRYPTION_FOLDER + Constants.FILE_SEPARATOR + bankId +
			 * Constants.FILE_SEPARATOR + "Reports" + Constants.FILE_SEPARATOR +
			 * "Recon Report" + Constants.FILE_SEPARATOR;
			 */
			if (!new File(pathName).exists()) {
				new File(pathName).mkdirs();
			}
			String outFileName = pathName + "DailyReconReport_" + df.format(d) + ".xls";
			OutputStream fileOut = new FileOutputStream(outFileName);
			logger.info("Excel File has been created successfully.");
			wb.write(fileOut);
			fileOut.close();
			String RecipientList = RECON_REPORT_RECIPIENTS;
			String Sender = RECON_REPORT_FROM;
			String mailHost = EMAIL_HOST;
			String subject = DAILY_RECON_REPORT_SUBJECT;
			String body = DAILY_RECON_REPORT_BODY;
			int result = SendEmailReport.sendEmailWithAttachMent(mailHost, outFileName, // filename
					Sender, // from
					RecipientList, // to
					subject, // sub
					body// body
			);
		} catch (Exception ex) {
			logger.error("#### Exception inside SendDailyReconReport :: "+ex.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createSplitterTransactionReport(Map<String, TransactionForm> transactionForm, String bankID,
			String folderPath, int splittedCount, int unsplittedCount) {
		try {
			logger.info("Inside createSplitterTransactionReport()");
			HSSFWorkbook wb = new HSSFWorkbook();
			// creates an excel file at the specified location
			HSSFSheet sheet = wb.createSheet("Sheet 1");
			HSSFRow rowSplitCount = sheet.createRow(0);
			// creating cell by using the createCell() method and setting the values to the
			// cell by using the setCellValue() method
			short k = 0;
			rowSplitCount.createCell((short) 2).setCellValue(new HSSFRichTextString("Splitted Count"));
			rowSplitCount.createCell((short) 3).setCellValue(new HSSFRichTextString(Integer.toString(splittedCount)));
			rowSplitCount.createCell((short) 5).setCellValue(new HSSFRichTextString("Unsplitted Count"));
			rowSplitCount.createCell((short) 6).setCellValue(new HSSFRichTextString(Integer.toString(unsplittedCount)));

			HSSFRow rowhead = sheet.createRow(2);
			HSSFCellStyle style = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setBoldweight((short) 2);
			style.setFont(font);
			style.setFillBackgroundColor(HSSFColor.DARK_TEAL.index);
			style.setFillForegroundColor(HSSFColor.BLUE.index);
			// creating cell by using the createCell() method and setting the values to the
			// cell by using the setCellValue() method
			k = 0;
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("S.No"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Terminal Name"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Transaction Date"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Starting sequence number"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Ending Sequence Number"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Missing Sequence Number"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Financial Transaction Count"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("NonFinancial Transaction Count"));
			rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Total Transaction Count"));
			logger.info("Inside CreateSplitterTransactionReport() row headers created ");
			for (short j = 1; j < k; j++)
				rowhead.getCell(j).setCellStyle(style);
			short i = 0;
			// for( i<transactionForm.size();i++){
			for (Entry<String, TransactionForm> entry : transactionForm.entrySet()) {
				HSSFRow row = sheet.createRow(i + 3);
				short j = 0;
				// inserting data in the first row
				row.createCell(j++).setCellValue(new HSSFRichTextString(Integer.toString(i + 1)));
				row.createCell(j++).setCellValue(new HSSFRichTextString(entry.getValue().getTerminalId()));
				row.createCell(j++).setCellValue(new HSSFRichTextString(entry.getValue().getStartDate()));
				row.createCell(j++).setCellValue(
						new HSSFRichTextString(Integer.toString(entry.getValue().getStartingSequenceNumber())));
				row.createCell(j++).setCellValue(
						new HSSFRichTextString(Integer.toString(entry.getValue().getEndingSequenceNumber())));
				row.createCell(j++).setCellValue(new HSSFRichTextString(entry.getValue().getMissingSequenceNumber()));
				row.createCell(j++).setCellValue(
						new HSSFRichTextString(Integer.toString(entry.getValue().getFinancialtxnCount())));
				row.createCell(j++).setCellValue(
						new HSSFRichTextString(Integer.toString(entry.getValue().getNonFinancialtxnCount())));
				row.createCell(j++).setCellValue(new HSSFRichTextString(Integer.toString(entry.getValue()
						.getTotaltxnCount())));
				i++;
			}
			String pathName = folderPath + Constants.FILE_SEPARATOR + Constants.DECRYPTION_FOLDER
					+ Constants.FILE_SEPARATOR + bankID + Constants.FILE_SEPARATOR + "Reports"
					+ Constants.FILE_SEPARATOR + "Splitter Reports" + Constants.FILE_SEPARATOR;
			logger.info("createSplitterReport folderPath ::" +pathName);
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
			Date d = new Date();
			if (new File(pathName).exists()) {
				File path = new File(pathName);
				File[] files = path.listFiles();
				if (files != null) {
					for (File f : files) {
						SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyy");
						if (f.isFile() && f.getName().startsWith("SplitterReport_" + dateformat.format(d))
								&& (f.getName().endsWith(".xls") || f.getName().endsWith(".zip"))) {
							f.delete();
						}
					}
				}
			}
			/*
			 * String outFilePath = folderPath + Constants.FILE_SEPARATOR +
			 * Constants.DECRYPTION_FOLDER + Constants.FILE_SEPARATOR + bankID +
			 * Constants.FILE_SEPARATOR + "Reports" + Constants.FILE_SEPARATOR +
			 * "Splitter Reports" + Constants.FILE_SEPARATOR;
			 */
			if (!new File(pathName).exists()) {
				new File(pathName).mkdirs();
			}
			String outFileName = pathName + "SplitterReport_" + df.format(d) + ".xls";
			OutputStream fileOut = new FileOutputStream(outFileName);
			logger.info("Excel File has been created successfully.");
			logger.info(outFileName + "Splitter Report has been created successfully.");
			wb.write(fileOut);
			fileOut.close();
			String RecipientList = SPLITTER_REPORT_RECIPIENTS;
			String Sender = SPLITTER_REPORT_FROM;
			String mailHost = EMAIL_HOST;
			String subject = SPLITTER_REPORT_SUBJECT;
			String body = SPLITTER_REPORT_BODY;

			int result = SendEmailReport.sendEmailWithAttachMent(mailHost, outFileName, // filename
					Sender, // from
					RecipientList, // to
					subject, // sub
					body// body
			);
		} catch (Exception ex) {
			logger.error("#### Exception inside CreateSplitterTransactionReport :: "+ex.getMessage());
		}
	}
	
	public Map<String,String> parseTransactionParameters(List<String> transaction, String make, String bankId){
		Map<String,String> transactionParams = new HashMap<String,String>();
		Hashtable<String,List<TransactionDescriptor>> transactionDescriptorsMap = setTransactionDescriptors(bankId);
		List<TransactionDescriptor> transactionDescriptors = transactionDescriptorsMap.get(make);
		try{
			for(int i=0;i<transaction.size();i++){
				
	            for(TransactionDescriptor transactionDescriptor : transactionDescriptors){
	            	transactionParams = applyTransformations(transaction,i,transactionParams,transactionDescriptor);
	            }
			}
		}catch(Exception ex){
			logger.error("#### Exception inside parseTransactionParameters :: " +ex.getMessage());
		}
		return transactionParams;
	}
}
