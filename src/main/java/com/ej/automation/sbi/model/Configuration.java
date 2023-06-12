package com.ej.automation.sbi.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

public class Configuration {

	public static boolean querylog;
	public static boolean traceLog;
	public static boolean errorLog;
	//public static String banks;
	public static String ticketIdPrefix = null;
	public static String dbType = null;
	public static boolean xfsFlag;
	public static boolean snmpFlag;
	public static String dbDataSrc = null;
	public static String dbDriver = null;	
	public static String dbUser = null;
	public static String dbPwd = null;
	public static String jndiConnectionName = null;
	public static String jndiOrJdbc = null;
	public static boolean ejSplitterFlag;
	public static String ejPath;
	public static String ejSplitterPath;
	public static boolean decryptionFlag;

	XFSExtendedService service = null;
	
	/*Xfs related variables- starts*/
	public static Hashtable<String, ServiceProviderInfo> serviceProviderInfo = null;
	public static Hashtable<String, ServiceProviderShiftInfo> srvcProvShiftInfo = null;
	public static Hashtable<String, ServiceProviderAgentInfo> sericeProviderAgent;
	public static Hashtable<String, FaultInfoFormBean> faultInfoBean = null;
	public static String dateTable = null;
	public static Hashtable<String, XFSFaultInfo> xfsFaultInfo = null;
	public static ResourceBundle xfsBundle = null;
	public static ResourceBundle queryBundle = null;
	public static ResourceBundle emailBundle = null;
	public static Hashtable<String, String> terminalNodeDetails;
	public static Hashtable<String, FilexTerminalForm> terminalIpStationDetails;
	/*END*/
	
	/*SNMP related objects*/
	public static Hashtable<String, DeviceOIDLinkBean> deviceOIDLinkdetails;
	public static Hashtable<String, TerminalDeviceModelLinkBean> terminaldeviceModelLinkdetails;
	/*END*/
	
	/*Crypto related variables - For FileX*/
	public static String TRAN_KEY_FILE_LOCATION="";
	public static String TRAN_KEY_FILE_PASSWORD="";
	public static String TRAN_KEY_STORE_TYPE="";
	public static String SEC_KEY_NAME="";
	public static String SEC_KEY_PASS="";
	public static String xfsApiReceiverName = "";
	/*END*/
	
	/* Redis DB connection API */
	public static String noOfSentinels = null;
	public static Set<String> sentinels = null;
	public static String MASTER_NAME = null;
	public static String REDIS_KEY = null;
	public static String CONNECTION_QUERY_PROCEDURE = null;
	public static String RAW_FILE_RENAME_BANK = null;
	/* Redis DB connection ends */
	
	
	public static String fssConnectStation = null;
	
	//For Image picker 
	public static String icFilePath = null;
	//public static Hashtable<String, String> deviceDateFrmt;
	
	public static String SplitterJobPath = null;
	public static Hashtable<String,List<TransactionDescriptor>> transactionDescriptorTable = null;
	
	public Configuration()
	{
		load();
	}

	@Value("${banks}")
	public static String banks;
	
	public void load() 
	{
		try 
		{
			ResourceBundle resourceBundle = null;
			resourceBundle = ResourceBundle.getBundle("config");
			xfsBundle = ResourceBundle.getBundle("XFSMessage");
			
			emailBundle= ResourceBundle.getBundle("Email");
			Configuration.querylog = resourceBundle.getString("querylog").equals("true");
			Configuration.traceLog = resourceBundle.getString("traceLog").equals("true");
			Configuration.errorLog = resourceBundle.getString("errorLog").equals("true");
			Configuration.banks = resourceBundle.getString("banks");
			Configuration.ticketIdPrefix = resourceBundle.getString("ticketIdPrefix");
			Configuration.dbType = resourceBundle.getString("DB");
			//Configuration.xfsFlag = resourceBundle.getString("xfsFlag").equals("true");
			Configuration.snmpFlag = resourceBundle.getString("snmpFlag").equals("true");
			Configuration.TRAN_KEY_FILE_LOCATION= resourceBundle.getString("TRAN_KEY_FILE_LOCATION");
			Configuration.TRAN_KEY_FILE_PASSWORD= resourceBundle.getString("TRAN_KEY_FILE_PASSWORD");
			Configuration.TRAN_KEY_STORE_TYPE= resourceBundle.getString("TRAN_KEY_STORE_TYPE");
			Configuration.SEC_KEY_NAME= resourceBundle.getString("SEC_KEY_NAME");
			Configuration.SEC_KEY_PASS= resourceBundle.getString("SEC_KEY_PASS");
			Configuration.CONNECTION_QUERY_PROCEDURE= resourceBundle.getString("CONNECTION_QUERY_PROCEDURE");
			Configuration.RAW_FILE_RENAME_BANK= resourceBundle.getString("RAW_FILE_RENAME_BANK");
			Configuration.SplitterJobPath = resourceBundle.getString("SplitterJobPath");
		     Constants.TIME_OUT = Integer.parseInt (resourceBundle.getString("Timeout (Milliseconds)"));
	         Constants.MIN_WAIT = Integer.parseInt (resourceBundle.getString ("Interval Time (Milliseonds)"));
	         //Constants.BIZ_DATE = resourceBundle.getString("Business Date");
	         String  cutOverTime = resourceBundle.getString ("Cut-over Time (246060 Hour format)");
	         Constants.CUT_OVER_TIME = Integer.parseInt (cutOverTime.substring(0,2));
	         Constants.CUT_OVER_MINUTES = Integer.parseInt(String.valueOf(cutOverTime).substring(2,4));
	         Constants.CUT_OVER_SECONDS = Integer.parseInt(String.valueOf(cutOverTime).substring(4,6));
	         Constants.NODE =resourceBundle.getString("Node");
	         Constants.HOME_DIR = resourceBundle.getString("Home Directory");
	           
	         //To update bussiness date after file load
	        Date date = new Date();
	        DateFormat istFormat = new SimpleDateFormat("dd/MM/yyyy");
	        Constants.BIZ_DATE = istFormat.format(date);
			   
			if(Configuration.dbType.equalsIgnoreCase("ORACLE"))
				queryBundle = ResourceBundle.getBundle("Oracle");
			else if(Configuration.dbType.equalsIgnoreCase("MSSQL"))
				queryBundle = ResourceBundle.getBundle("MSSql");
			
			jndiOrJdbc = resourceBundle.getString("jndiOrJdbc");
			
			if(Configuration.jndiOrJdbc.equalsIgnoreCase("JNDI"))
			{
				jndiConnectionName = resourceBundle.getString("jndiConnectionName");
			}
			else if(Configuration.jndiOrJdbc.equalsIgnoreCase("JDBC"))
			{
				if(Configuration.dbType.equalsIgnoreCase("MSSQL"))
				{
				dbDataSrc = resourceBundle.getString("db.datasource");
				dbPwd = resourceBundle.getString("db.user.password");
				dbUser = resourceBundle.getString("db.user.id");
				dbDriver = resourceBundle.getString("db.driver");
				}
				else if(Configuration.dbType.equalsIgnoreCase("ORACLE"))
				{
					dbDataSrc = resourceBundle.getString("oracle.db.datasource");
					dbPwd = resourceBundle.getString("oracle.db.user.password");
					dbUser = resourceBundle.getString("oracle.db.user.id");
					dbDriver = resourceBundle.getString("oracle.db.driver");
				}
			}
			
			fssConnectStation = resourceBundle.getString("FSSCONNECT_HTTP_STATION");
			Constants.MAIL_ID = resourceBundle.getString("mailId"); 
			ejSplitterFlag    = resourceBundle.getString("EJSPLITTER_FLAG").equals("true");;
			decryptionFlag    = resourceBundle.getString("DECRYPTION_FLAG").equals("true");;
			
			
			loadAllInitialConfiguration(Configuration.banks);
			
			//sentinelPool();
			Configuration.noOfSentinels =resourceBundle.getString("noOfSentinels");
			Configuration.MASTER_NAME =resourceBundle.getString("sentinelMaster");
			Configuration.REDIS_KEY =resourceBundle.getString("redisKey");
			String pwd="kingslayer";
			sentinels = new HashSet<String>();
//   			for(int sentinelCount=0;sentinelCount<Integer.parseInt(noOfSentinels.trim());sentinelCount++)
//   			{
//   				sentinels.add(new HostAndPort(resourceBundle.getString("sentinel"+(sentinelCount+1)+"Ip"), Integer.parseInt(resourceBundle.getString("sentinel"+(sentinelCount+1)+"Port"))).toString());
//   			}
//   			GenericObjectPoolConfig gopc = new GenericObjectPoolConfig();
//			gopc.setMaxIdle(20);
//			gopc.setMinIdle(10);
//			gopc.setMaxTotal(40);
//   			RedisAPI.jsp = new JedisSentinelPool(Configuration.MASTER_NAME, Configuration.sentinels, gopc, pwd);
   			
   		  //Device Date Format for Image Copier
	          // deviceDateFrmt=new Hashtable<String, String>();
	          // deviceDateFrmt.put("NR", resourceBundle.getString("NCR"));
	          // deviceDateFrmt.put("WN", resourceBundle.getString("WINCOR"));
	          // deviceDateFrmt.put("DI", resourceBundle.getString("DIEBOLD"));
	          // deviceDateFrmt.put("HY", resourceBundle.getString("Hyosung"));
	           //End
		} 
		catch (Exception e) 
		{
			
		}
	}
	
	private void loadAllInitialConfiguration(String bankId)
	{
		try
		{
			service = new XFSExtendedService(RRN.generateSixDigitRRN());
			//For url.properties
	        //  Constants.urlPath = resourceBundle.getString("url"); 
			//String ejFilePath [] = service.getEJFilePath();
			FilexAdditionalBankForm form = service.getFilexAddBAnkInfo();
			transactionDescriptorTable = EJParsing.SetTransactionDescriptors(bankId);
			
			if(form != null)
			{
				ejPath = form.getEjPath();
				ejSplitterPath = form.getEjSplitterPath();
				
				if(form.getXfsFlag() == null || form.getXfsFlag().equals("0"))
				{
					xfsFlag = false;
				}
				else
				{
					xfsFlag = true;
				}
			}
			
			System.out.println("Configuration xfsFlag : " + Configuration.xfsFlag);
			if(Configuration.xfsFlag)
			{
				serviceProviderInfo = service.getServiceProviderInfo(bankId);
				srvcProvShiftInfo = service.getServiceProviderShiftInfo(bankId);
				sericeProviderAgent = service.getServiceProviderAgentInfo(bankId);
				faultInfoBean = service.getFaultInfo();
				Calendar currentDay = Calendar.getInstance();
				dateTable = service.getDateTable(currentDay.get(Calendar.DATE), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.YEAR));
				xfsFaultInfo = service.getXFSFaultInfo();
				terminalNodeDetails = service.getTerminalNodeDetails();
				terminalIpStationDetails = service.getTerminalIpStationDetails();
				//deviceOIDLinkdetails = service.getDeviceOIDLinkDetails();
				//terminaldeviceModelLinkdetails = service.getTerminalDeviceModelLinkDetails();
			}
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			if (service.dbimp.connection != null) 
			{
				try
				{
					service.dbimp.closeConnection();
				} 
				catch (final Exception e)
				{
					
				}
			}
			service = null;
		}
	}
	
//	private void sentinelPool() throws JedisConnectionException
//	{
//		
//	}
	
}
