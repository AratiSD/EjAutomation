package com.ej.automation.sbi.model;

import java.io.FileWriter;


public class Constants 
{
	public static String NODE; 

    public static ThreadGroup threadGroup;
    public static boolean    fileLogEnabled;
    public static boolean    consoleLogEnabled;

    public static String ParentStationCheck = null;
    public static String NetworkCheck = null;
    /** Constant static variable */
    public final static boolean TCP_NODELAY = true; /** Enable Nagle's algorithm */
    public final static boolean SO_KEEPALIVE = false;   /** Enable socket keepalive */

    public static int MIN_WAIT;                                     /** Minimum thread sleep time in milliseconds */
    public final static int MAX_WAIT = 2000;
    public final static int AGENT_FS_WATCHER_RESET_WAIT = 300000;
    
    public static String CONF_FILE = "Message24.conf";  /** Application conf file path */
    public static String INFO_FILE = "Message24.info";      /** Application info file path */
    public static String DB_FILE = "Message24.db";      // DB File changes Raghavendran

    public static int LOG_SIZE;                                         /** Maximum log file size in kilobyes */
    public static int TIME_OUT                                          /** Timeout value in milliseconds */;
    public static int NO_OF_TXNS;                                   /** Transactions per processor */
    public static int CUT_OVER_TIME;                            /** Cut-over time in 24 hour format */
    public static int CUT_OVER_MINUTES;                         /** Cut-over minutes in 60 hour format */
    public static int CUT_OVER_SECONDS;                         /** Cut-over seconds in 60 hour format */
    public static int INITIAL_PROCESSOR;                    /** Maximum Initial processors */
    public static String LOG_DIR;                                       /** Log/audit/suspect file location */
    public static String EJPATH;
    
    public static String BIZ_DATE;                                  /** Business date */
    public static String HOME_DIR;                                  /** Application home directory */
    public static long LOG_FILE_CTR;                           /** Log file unique number in the business date */
    public static long AUDIT_FILE_CTR;
    public static String DDMMYY;                                    /** Used for DCBL API */
    public static int EJFILE_MINUTES;
	public static int EJFILE_SECONDS;
	public static int EJFILE_TIME;
	
    /** Global variable */
    public static boolean cutover;
    public static boolean running;
    
	public static String MAIL_ID = null;
	
    public static final String ADMTOKEN_NAME			=  "ADM";
    
    public static final String SPDH_HEADER				= "SE";
	public static final String XFS_HEADER				= "XF";
	public static final String SNMP_HEADER				= "SN";
	public static final String ECHO_HEADER				= "EH";	
	public static final String FILE_SEPARATOR 			= "/";
	public static final String AGENT_CONNECTED 			= "UP";
	public static final String AGENT_NOT_CONNECTED 		= "DOWN";
	public static final String ENCRYPTION_FOLDER  	    = "Encryption";
	public static final String DECRYPTION_FOLDER        = "Decryption";
	public static final String ENCRYPTION_INPUT_FOLDER  = "EJournal";
	public static final String FILE_SEARCH_FOLDER		= "ListFiles";
	public static final String FILE_COMPRESSED			= "Files";
	public static final String PROCTECTED_FILE			= "ProctectedFiles";
	public static final String DECRYPTION_OUTPUT_COMPLETE = "Completed";
	public static final String CONFIG_FOLDER			="Config";
	public static final String SYS_INFO_FOLDER		= "SystemInfo";
	public static final String INPROGRESS			= "Inprogress";
	public static final String BACKUP_FOLDER 			= "EJbackup";
	public static final String ERROR_FILES   			= "Error_files";
	
	public static final String IMAGE_COPIER		= "ImageCopier";
	public static final String ATM_SCREENSHOT		= "ATMScreenShot";
	
	public static boolean bizDateChanged = false;
	//public static FileWriter fileXlogWriter;
	//public static FileWriter xfsLogWriter;
	//public static FileWriter snmpLogWriter;
	public static FileWriter fssConnectLogWriter;
	
	public static final String branch_system			= "BranchDistribution";
	public static final String CAMERA_HEADER= "CM";
	public static final String LIVEFOLDER			= "Live";
	public static final String LIVEFOLDERBACKUP			= "LiveBackup";
	public static final String DISPUTED_IMAGES_FOLDER = "Disputed Images";
	public static final String REALTIME_COLLECTION_HEADER = "RT";
	public static final String Upload = "Upload";
	
}
