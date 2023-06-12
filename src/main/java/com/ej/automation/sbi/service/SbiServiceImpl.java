package com.ej.automation.sbi.service;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import com.ej.automation.sbi.entity.AdmFlexSpltLog;
import com.ej.automation.sbi.makes.DieboldSplitter;
import com.ej.automation.sbi.makes.HitachiSplitter;
import com.ej.automation.sbi.makes.NcrSplitter;
import com.ej.automation.sbi.makes.PertoSplitter;
import com.ej.automation.sbi.makes.WincorSplitter;
import com.ej.automation.sbi.model.TransactionForm;
import com.ej.automation.sbi.repository.AdmCommTermMastRepository;
import com.ej.automation.sbi.repository.AdmFlexSplitLogRepository;
import com.ej.automation.sbi.util.CommonUtil;
import com.ej.automation.sbi.util.SbiDatabaseUtil;

@Service
public class SbiServiceImpl implements ISbiService {
	public static final Logger logger = LoggerFactory.getLogger(SbiServiceImpl.class);

	@Value("${banks}")
	private String banks;

	@Value("${SplitterJobPath}")
	private String splitterJobPath;
	//For Dev

	@Value("${sourcePath}")
	private String sourcePath;

	@Value("${splittedPtah}")
	private String splittedPtah;

	@Value("${nonSplittedPath}")
	private String nonSplittedPath;

	@Value("${countPath}")
	private String countFile;

	@Value("${EMAIL.HOST}")
	private String EMAIL_HOST;

	@Autowired
	private AdmCommTermMastRepository admCommTermMastRepository;

	@Autowired
	private AdmFlexSplitLogRepository admFlexSplitLogRepository;

	@Autowired
	private CommonUtil commonUtility;

	@Autowired
	private PertoSplitter pertoSplitter;

	@Autowired
	private NcrSplitter ncrSplitter;

	@Autowired
	private SbiDatabaseUtil databaseUtil;

	@Autowired
	private WincorSplitter wincorSplitter;

	@Autowired
	private HitachiSplitter hitachiSplitter;

	@Autowired
	private DieboldSplitter dieboldSplitter;

	public static Map<String, List<Map<String, String>>> transactionListMap = new HashMap<String, List<Map<String, String>>>();
	public static Map<String, TransactionForm> transactionFormMap = new HashMap<String, TransactionForm>();

	@Override
	public String sbiTest() {
		logger.info("Inside [SbiServiceImpl:sbiTest] ");
		return "Welcome SBI";
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public String splitFile() {
		String bankId = "";
		try {
			logger.info("Inside SplitFile");
			String value = "success";
			bankId = banks;
			logger.info("bankId:" + bankId);
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			Date yesterday = cal.getTime();

			SimpleDateFormat dateformat= new SimpleDateFormat("ddMMyy");
			String todayDate= dateformat.format(now);
			String yesterdayDate = dateformat.format(yesterday);

			String folderPath = splitterJobPath;

			//For Dev
			String uploadFolderPath=folderPath+File.separator+bankId+File.separator+todayDate+File.separator+"Upload";

			//For Prod
			//String uploadFolderPath=folderPath+File.separator+Constants.DECRYPTION_FOLDER+File.separator+bankId+File.separator+todayDate+File.separator+Constants.Upload;

			logger.info("uploadFolderPath   "+uploadFolderPath);

			//For Prod
			/*
			 * String sourcePath=folderPath+File.separator+Constants.DECRYPTION_FOLDER+File.
			 * separator+bankId+File.separator+yesterdayDate+
			 * File.separator+Constants.FILE_COMPRESSED+File.separator+Constants.
			 * DECRYPTION_OUTPUT_COMPLETE;
			 */


			logger.info("Source Path   "+sourcePath);

			Map<String,Long> hm=new HashMap<String,Long>();

			List<AdmFlexSpltLog> filesSplittedToday = admFlexSplitLogRepository.getFilesSplittedToday();
			logger.info("filesSplittedToday :: "+filesSplittedToday.toString());
			for (AdmFlexSpltLog admFlexSpltLog : filesSplittedToday) {
				String filename = admFlexSpltLog.getAfsFileName()!=null?admFlexSpltLog.getAfsFileName():"";
				filename=new File(filename).getName();
				String[] namelist=filename.split("_");
				long fileSize = admFlexSpltLog.getAfsFileSize();
				hm.put(namelist[0]+"-"+fileSize, fileSize);
			}
			logger.info("hm :: "+hm);

			try {
				//boolean terminalIdCheck = false;
				String msg = null;
				int unsplittedCount = 0, splittedCount =0, count = 0;
				long startTime = System.currentTimeMillis();
				ExecutorService taskExecutor = Executors.newFixedThreadPool(100);
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("Sheet 1");
				HSSFRow rowhead = sheet.createRow(0);
				short k = 0;

				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("S.No."));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Branch"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Date"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Time"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("ATM ID"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Terminal Name"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Transaction No"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Response Code"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Card No."));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Amount Entered"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Successful Transaction"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Failed Amount"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Error"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Error Details"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Transaction Description"));
				rowhead.createCell(k++).setCellValue(new HSSFRichTextString("Remarks"));

				String[] Paths = new String[] { sourcePath };
				for (String path : Paths) {
					File folder = new File(path);
					File[] listOfFiles = folder.listFiles();
					if (listOfFiles != null) {
						Arrays.sort(listOfFiles, new Comparator<File>() {
							public int compare(File f1, File f2) {
								return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
							}
						});

						for (File file : listOfFiles) {
							if(file.getName().toLowerCase().contains("ej") || file.getName().endsWith(".jrn") || file.getName().endsWith(".txt") || file.getName().toLowerCase().contains("edc")){
								long fileSize = file.length();
								String[] nameList = file.getName().split("_");
								String terminalName = nameList[0];
								if (nameList.length >= 2 && !(hm.containsKey(terminalName + "-" + fileSize))) {
									hm.put(terminalName + "-" + fileSize, fileSize);
									msg=makerIdentification(file, bankId, path, splittedPtah, nonSplittedPath,
											countFile, sheet, uploadFolderPath,terminalName);
									if(msg.contains("File Splitted successfully")) {
										++splittedCount;
									}
								}
								++count;
							}
						}
						logger.info("Count :: "+count);
					}
				}
				taskExecutor.shutdownNow();
				long endTime = System.currentTimeMillis();
				unsplittedCount  = count -splittedCount ;
				logger.info("Number of splitted files:" + splittedCount);
				logger.info("Number of non-splitted files:" + unsplittedCount);
				logger.info("End Time" + new Date());
				logger.info("Total Time Taken" + ((endTime - startTime) / 1000));
				logger.info("transactionFormMap.size() :: "+transactionFormMap.size());
				logger.info("createSplitterTransactionReport folderPath :: "+folderPath);

				if (transactionFormMap.size() > 0) {
					commonUtility.createSplitterTransactionReport(transactionFormMap, bankId, folderPath, splittedCount,
							unsplittedCount);
					databaseUtil.insertSplittedFileInfo(transactionFormMap);
					transactionFormMap.clear();
				}

				if (transactionListMap.size() > 0) {
					logger.info("Entering into for() to poupulate daily recon report");
					for (Entry<String, List<Map<String, String>>> entry : transactionListMap.entrySet()) {
						String filenameSplit = entry.getKey(); // .getName().split("_");
						String terminalName = filenameSplit.substring(0, 13);
						commonUtility.populateReconReportSheet(entry.getValue(), wb, terminalName);
					}
					commonUtility.sendDailyReconReport(folderPath, bankId, wb);
					transactionListMap.clear();
					logger.info("Exited from for() of  daily recon report");
				}

				File file = null;
				file = new File(countFile);
				FileWriter fw = new FileWriter(file);
				fw.write("Number of splitted files:" + splittedCount);
				fw.write(System.getProperty("line.separator"));
				fw.write("Number of non-splitted files:" + unsplittedCount);
				logger.info("Number of splitted files:" + splittedCount + " : " + "Number of non-splitted files:"
						+ unsplittedCount);
				fw.close();
			} catch (Exception e) {
				logger.error("#### Exception inside SpliiterJob()" + e.getMessage());
			}

			try {
				Thread.sleep(300); // delay time for file movement
			} catch (InterruptedException e) {
				logger.error("#### Exception inside Thread.sleep(300) :: " + e.getMessage());
			}
			return value;
		} catch (Exception ex) {
			logger.error("#### Exception inside splitFile :: " + ex.getMessage());
			return "Failed";
		}
	}

	@SuppressWarnings("static-access")
	public String makerIdentification(File currentFile, String bankId, String sourcePath, String splittedPath,
			String notSplittedFilePath, String countFile, HSSFSheet sheet, String uploadFolderPath, String terminalName) {
		logger.info("Inside makerIdentification");
		String maker = null;
		String message = "File Splitted successfully";
		if (currentFile.isFile()) {
			try {
				logger.info("currentFile : " + currentFile);
				if (currentFile.length() > 0) {
					maker = admCommTermMastRepository.findDeviceDescription(terminalName);
					logger.info("maker :: " + maker);
					if (maker != null && !maker.equals("")) {
						if (maker.equalsIgnoreCase("WINCOR")) {
							wincorSplitter.wincorSplitter(currentFile.getAbsolutePath(), bankId, terminalName, maker,
									sourcePath, splittedPath, notSplittedFilePath, countFile, sheet, uploadFolderPath);
						} else if (maker.equalsIgnoreCase("NCR")) {
							ncrSplitter.ncrSplitter(currentFile.getAbsolutePath(), bankId, terminalName, maker,
									sourcePath, splittedPath, notSplittedFilePath, countFile, sheet, uploadFolderPath);
						} else if (maker.equalsIgnoreCase("PERTO")) {
							pertoSplitter.pertoSplitter(currentFile.getAbsolutePath(), bankId, terminalName,
									maker, sourcePath, splittedPath, notSplittedFilePath, countFile, sheet, uploadFolderPath);
						} else if (maker.equalsIgnoreCase("HITACHI")) {
							hitachiSplitter.hitachiSplitter(currentFile.getAbsolutePath(), bankId, terminalName, maker,
									sourcePath, splittedPath, notSplittedFilePath, countFile, sheet, uploadFolderPath);
						}else if (maker.equalsIgnoreCase("DIEBOLD")) {
							dieboldSplitter.dieboldSplitter(currentFile.getAbsolutePath(), bankId, terminalName, maker,
									sourcePath, splittedPath, notSplittedFilePath, countFile, sheet, uploadFolderPath);
						}
					} else {
						commonUtility.moveFile(sourcePath + File.separator + currentFile.getName(),
								notSplittedFilePath + File.separator + currentFile.getName());
						message = "File Split failed";
					}
				} else {
					commonUtility.moveFile(sourcePath + File.separator + currentFile.getName(),
							notSplittedFilePath + File.separator + currentFile.getName());
					message = "File Split failed";	
				}
				Thread.sleep(50l);
			} catch (Exception e) {
				logger.error("#### Exception inside makerIdentification :: " + e.getMessage());
				message = "File Split failed";
				commonUtility.moveFile(sourcePath + File.separator + currentFile.getName(),
						notSplittedFilePath + File.separator + currentFile.getName());
			}
		}
		return message;
	}
}
