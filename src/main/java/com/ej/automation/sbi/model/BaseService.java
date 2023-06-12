package com.ej.automation.sbi.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BaseService 
{
	public Connection connection;
	//public ResourceBundle Configuration.queryBundle = null;

	public DBImpl dbimp;
	
	
	public String rrnNum = null;
	
	public BaseService(Connection connection, String rrnNum) 
	{
		dbimp=new DBImpl(connection, rrnNum);
		this.rrnNum = rrnNum;
		/*if(Configuration.dbType.equalsIgnoreCase("ORACLE"))
			Configuration.queryBundle = ResourceBundle.getBundle("Oracle");
		else if(Configuration.dbType.equalsIgnoreCase("MSSQL"))
			Configuration.queryBundle = ResourceBundle.getBundle("MSSql");*/
	}
	
	public BaseService(String rrnNum) 
	{
		//dbimp=new DBImpl(rrnNum);
		this.rrnNum = rrnNum;
		/*if(Configuration.dbType.equalsIgnoreCase("ORACLE"))
			Configuration.queryBundle = ResourceBundle.getBundle("Oracle");
		else if(Configuration.dbType.equalsIgnoreCase("MSSQL"))
			Configuration.queryBundle = ResourceBundle.getBundle("MSSql");*/
	}
	
	public void errorLog(Exception ex)
	{
		FileXLogger.logM24Error(ex);
	}
	
	public void snmpErrorLog(Exception ex)
	{
		FileXLogger.logCommonError(ex);
	}
	
	public void traceLog(String message)
	{
		FileXLogger.logM24Trace(message);
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the Terminal ID 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String terminalId
	 ********************************************************************************************************************/
	public TerminalFormBean getTerminal(String terminalId)
	{
		
		StringBuffer query = null;
		TerminalFormBean terminalFormBean = null;
		
		query = new StringBuffer(Configuration.queryBundle.getString("get.terminal.info"));
		query = query.append(" WHERE ACB_BANK_ID IN " + Configuration.banks + " AND " 
					+ "ACT_TERM_ID = '" + terminalId.trim() + "'");
				
		/*query = "SELECT ACT_TERM_ID, ACR_REGN_ID, ACB_BRCH_ID, ACC_CITY_ID,ACS_STAT_ID, ACC_COUN_ID,"
				+ "ACB_BANK_ID, ACT_TERM_NAME FROM ADM_COMM_TERM_MAST WHERE ACB_BANK_ID IN " + Configuration.banks + " AND "
				+ "ACT_TERM_NAME = '" + terminalId.trim() + "'";*/
		
		try {
			dbimp.executeQuery(query.toString());
			terminalFormBean = assignToTerminalFormBean(dbimp.resultSet);

		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}
		
		if(terminalFormBean != null)
		{
			query = null;
			query = new StringBuffer(Configuration.queryBundle.getString("get.monitoring.terminal.info"));
			query = query.append(" WHERE ACB_BANK_ID='" + terminalFormBean.getBankId().trim() + "' " +
					"AND ACT_TERM_ID='" + terminalFormBean.getTerminalId().trim() + "'");
			
			try 
			{
				dbimp.executeQuery(query.toString());
				if(dbimp.resultSet != null)
				{
					dbimp.resultSet.next();
					terminalFormBean.setNightStartTime(dbimp.resultSet.getString(2));
					terminalFormBean.setNightEndTime(dbimp.resultSet.getString(3));
					
					return terminalFormBean;
				}
				else 
					return null;

			} catch (Exception ex) 
			{
				errorLog(ex);
				return null;
			} 
			finally 
			{
				dbimp.closeResultSetAndStatment();
			}
		}
		else
			return null;
		

	}
	
	/****************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to TerminalFormBean 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 ***************************************************************************************/
	private TerminalFormBean assignToTerminalFormBean(ResultSet resultSet1) 
	{
		TerminalFormBean form = null;

		try {
			if (resultSet1 != null && resultSet1.next()) {
				form = new TerminalFormBean();
				form.setTerminalId(resultSet1.getString(1));
				form.setRegionId(resultSet1.getString(2));
				form.setBranchId(resultSet1.getString(3));
				form.setCityId(resultSet1.getString(4));
				form.setStateId(resultSet1.getString(5));
				form.setCountryId(resultSet1.getString(6));
				form.setBankId(resultSet1.getString(7));
				form.setTerminalName(resultSet1.getString(8));
				form.setTerminalLocation(resultSet1.getString(9));
			}
		} catch (Exception ex) {
			errorLog(ex);
		}

		return form;
	}
	
	/***************************************************************************
	 * Purpose in brief : Method to get the List of all Faults 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed :
	 **************************************************************************/
	public Hashtable<String, FaultInfoFormBean> getFaultInfo() 
	{
		String query = Configuration.queryBundle.getString("get.fault.info");
		/*String query = "SELECT  AMF_FALT_ID, AMF_FALT_DESC, AMF_FACT_ID, AMF_FALT_SRVT, AMF_FALT_HLID FROM ADM_MNTC_FAIN_MAST";*/

		Hashtable<String, FaultInfoFormBean> faultMap = null;

		try {
			dbimp.resultSet = dbimp.executeQuery(query);
			faultMap = assignToHashMapFaultInfoForm(dbimp.resultSet);
		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}
		return faultMap;
	}

	
	/********************************************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to HashTable<FaultFormBean> with fault id as key 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 *******************************************************************************************************************/
	private Hashtable<String, FaultInfoFormBean> assignToHashMapFaultInfoForm(
			ResultSet resultSet1) {
		Hashtable<String, FaultInfoFormBean> faultInfo = new Hashtable<String, FaultInfoFormBean>();

		try {
			FaultInfoFormBean form = null;
			while (resultSet1.next()) {
				form = new FaultInfoFormBean();
				form.setFaultId(resultSet1.getString(1));
				form.setFaultDesc(resultSet1.getString(2));
				form.setFaultCategoryId(resultSet1.getString(3));
				form.setSeverity(resultSet1.getString(4));
				form.setHealthFaultId(resultSet1.getString(5));

				faultInfo.put(form.getFaultId().trim(), form);
				form = null;
			}
		} catch (Exception ex) {
			errorLog(ex);
		}
		return faultInfo;
	}
	
	/***************************************************************************
	 * Purpose in brief : Method to get the Date Id for the Date sent
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : int day, int month, int year
	 **************************************************************************/
	public String getDateTable(int day, int month, int year)
	{
		String dateId = null;
		try 
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.date.info"));
			query = query.append(" WHERE ACD_DATE_DOMN = '" + day
					+ "' AND ACD_DATE_MOYR = '" + (month+1)
					+ "' AND ACD_DATE_YEAR = '" + year + "'");
			
			/*String query = "SELECT ACD_DATE_ID FROM ADM_COMM_DATE_CNFG "
					+ " WHERE ACD_DATE_DOMN = '" + day
					+ "' AND ACD_DATE_MOYR = '" + (month+1)
					+ "' AND ACD_DATE_YEAR = '" + year + "'";*/

			dbimp.executeQuery(query.toString());
			if(dbimp.resultSet != null)
			{
				dbimp.resultSet.next();
				dateId = dbimp.resultSet.getString(1) ;
			}
		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}
		return dateId;
	}
	
	/******************************************************************************************************
	 * Purpose in brief : Method to get the List of all active service providers  for the provided bank id. 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : String bankId
	 ******************************************************************************************************/
	public Hashtable<String, ServiceProviderInfo> getServiceProviderInfo(String bankId)
	{
		Hashtable<String, ServiceProviderInfo> srvcProviderInfo = null;

		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.serviceprovider.info"));
		query = query.append(" WHERE ACB_BANK_ID IN " + bankId + " AND AVS_SVPI_STAT = '1'");
		
		/*String query = "SELECT  AVS_SVPI_ID, AVS_SPTP_ID, ACB_BANK_ID,"
				+ "AVS_SVPI_SMFG, AVS_SVPI_EMFG FROM ADM_VDMG_SVPI_MAST WHERE ACB_BANK_ID IN "
				+ bankId + " AND AVS_SVPI_STAT = '1'";*/
		try {
			dbimp.executeQuery(query.toString());
			srvcProviderInfo = assignToServiceProviderInfoBean(dbimp.resultSet);
		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}

		return srvcProviderInfo;
	}
	
	/******************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to ServiceProviderInfo 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 *****************************************************************************************/
	private Hashtable<String, ServiceProviderInfo> assignToServiceProviderInfoBean(ResultSet resultSet1)
	{
		Hashtable<String, ServiceProviderInfo> srvcProviderInfo = null;

		if (resultSet1 != null) {
			srvcProviderInfo = new Hashtable<String, ServiceProviderInfo>();
			try {
				ServiceProviderInfo form = null;
				while (resultSet1.next()) {
					form = new ServiceProviderInfo();
					form.setServiceProviderId(resultSet1.getString(1));
					form.setServiceProviderTypeId(resultSet1.getString(2));
					form.setBankId(resultSet1.getString(3));
					form.setSmsFlag(resultSet1.getString(4));
					form.setEmailFlag(resultSet1.getString(5));

					srvcProviderInfo.put(form.getServiceProviderId().trim(),
							form);
					form = null;
				}
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
		return srvcProviderInfo;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the List of all Service Provider shift 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId
	 ********************************************************************************************************************/
	public Hashtable<String, ServiceProviderShiftInfo> getServiceProviderShiftInfo(String bankId)
	{
		Hashtable<String, ServiceProviderShiftInfo> srvcShiftInfo = null;

		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.serviceprovider.shift.info"));
		query = query.append(" WHERE ACB_BANK_ID IN " + bankId);
		
		/*String query = "SELECT  AVS_SHFT_ID, AVS_SHFT_STTM, AVS_SHFT_ENTM "
				+ " FROM ADM_VDMG_SPSF_MAST WHERE ACB_BANK_ID IN " + bankId + "";*/
		try {
			dbimp.executeQuery(query.toString());
			srvcShiftInfo = assignToServiceProviderShiftForm(dbimp.resultSet);
		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}
		return srvcShiftInfo;
	}
	
	/***********************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to ServiceProviderShiftInfo 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 **********************************************************************************************/
	private Hashtable<String, ServiceProviderShiftInfo> assignToServiceProviderShiftForm(ResultSet resultSet1)
	{
		Hashtable<String, ServiceProviderShiftInfo> srvcShiftForm = new Hashtable<String, ServiceProviderShiftInfo>();

		if (resultSet1 != null) {
			try {
				ServiceProviderShiftInfo form = null;
				while (resultSet1.next()) {
					form = new ServiceProviderShiftInfo();
					form.setShiftId(resultSet1.getString(1));
					form.setStartTime(resultSet1.getString(2));
					form.setEndTime(resultSet1.getString(3));
					srvcShiftForm.put(form.getShiftId(), form);
					form = null;
				}
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
		return srvcShiftForm;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the List of all Service Provider Agent 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId
	 ********************************************************************************************************************/
	public Hashtable<String, ServiceProviderAgentInfo> getServiceProviderAgentInfo(String bankId)
	{
		/*StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.serviceprovider.agent.info"));
		query = query.append(" WHERE ACB_BANK_ID IN " + bankId + " AND AVS_SPAG_ESLV = '1'");*/
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.serviceprovider.agent.info"));
		query = query.append(" WHERE ACB_BANK_ID IN " + bankId);
		
		/*String query = "SELECT AVS_SHFT_ID, AVS_SPAG_MAIL, AVS_SPAG_MBNO, AVS_SPAG_ID, AVS_SVPI_ID "
				+ "FROM ADM_VDMG_SPAG_MAST WHERE "
				+ "ACB_BANK_ID IN " + bankId + " AND AVS_SPAG_ESLV = '1'";*/

		Hashtable<String, ServiceProviderAgentInfo> serviceProviderAgentMap = null;

		try {
			dbimp.executeQuery(query.toString());
			serviceProviderAgentMap = assignServiceProviderAgentInfoMap(dbimp.resultSet);
		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}

		return serviceProviderAgentMap;
	}
	
	/***********************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to ServiceProviderAgent 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 **********************************************************************************************/
	private Hashtable<String, ServiceProviderAgentInfo> assignServiceProviderAgentInfoMap(ResultSet resultSet1)
	{
		Hashtable<String, ServiceProviderAgentInfo> serviceProviderAgentMap = null;

		try {
			if (resultSet1 != null) {
				serviceProviderAgentMap = new Hashtable<String, ServiceProviderAgentInfo>();
				ServiceProviderAgentInfo serviceProviderAgent = null;
				/*String services = null;*/
				while (resultSet1.next()) 
				{
					serviceProviderAgent = new ServiceProviderAgentInfo();
					serviceProviderAgent.setShiftId(resultSet1.getString(1));
					serviceProviderAgent.setEmailId(resultSet1.getString(2));
					serviceProviderAgent.setMobileNumber(resultSet1.getString(3));
					serviceProviderAgent.setServiceProviderAgentId(resultSet1.getString(4));
					serviceProviderAgent.setServiceProviderId(resultSet1.getString(5));
					/*services = resultSet1.getString(6);
					boolean serviceDays[] = new boolean[7];
					for(int inc = 0; inc < services.length(); inc++)
					{
						if(services.substring(inc, inc+1).equalsIgnoreCase("Y"))
							serviceDays[inc] = true;
						else
							serviceDays[inc] = false;
					}

					serviceProviderAgent.setServiceDays(serviceDays);*/
					

					serviceProviderAgentMap.put(serviceProviderAgent.getServiceProviderAgentId(), serviceProviderAgent);
					serviceProviderAgent = null;
				}
			}
		} catch (Exception ex) {
			errorLog(ex);
		}

		return serviceProviderAgentMap;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the List of all Service Provider Agent linked for the Terminal 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId, String terminalId
	 ********************************************************************************************************************/
	public Map<String, List<TerminalAgentLink>> getTerminalAgentLink(String bankId, String terminalId)
	{
		Map<String, List<TerminalAgentLink>> terminalAgentLinkMap = null;
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.terminal.agent.link.info"));
		query = query.append(" WHERE ACB_BANK_ID = '" + bankId + "' AND ACT_TERM_ID = '" + terminalId + "'");
		
		/*String query = "SELECT AVS_SVPI_ID, AMF_FACT_ID, AVT_MINR_MINS, AVT_MAJR_MINS, AVT_CRCL_MINS FROM "
				+ "ADM_VDMG_TALK_MAST WHERE ACB_BANK_ID = '" + bankId + "' AND ACT_TERM_ID = '" + terminalId + "'";*/
		try {
			dbimp.executeQuery(query.toString());
			terminalAgentLinkMap = assignTerminalAgentLink(dbimp.resultSet);

		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}
		return terminalAgentLinkMap;
	}
	
	/***********************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to Terminal Agent Link 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 **********************************************************************************************/
	private HashMap<String, List<TerminalAgentLink>> assignTerminalAgentLink(ResultSet resultSet1)
	{
		HashMap<String, List<TerminalAgentLink>> terminalAgentLinkMap = null;

		try {
			if (resultSet1 != null) {
				terminalAgentLinkMap = new HashMap<String, List<TerminalAgentLink>>();
				List<TerminalAgentLink> terminalAgentLinkList = null;
				TerminalAgentLink terminalAgentLink = null;

				while (resultSet1.next()) 
				{
					terminalAgentLink = new TerminalAgentLink();
					terminalAgentLink.setServiceProviderId(resultSet1.getString(1));
					terminalAgentLink.setFaultCategoryId(resultSet1.getString(2));
					terminalAgentLink.setMinorMins(resultSet1.getString(3));
					terminalAgentLink.setMajorMins(resultSet1.getString(4));
					terminalAgentLink.setCriticalMins(resultSet1.getString(5));
					terminalAgentLink.setServiceProviderAgentId(resultSet1.getString(6));
					
					if(terminalAgentLinkMap.get(terminalAgentLink.getFaultCategoryId()) != null)
						terminalAgentLinkList = terminalAgentLinkMap.get(terminalAgentLink.getFaultCategoryId());
					else
						terminalAgentLinkList = new ArrayList<TerminalAgentLink>();
					
					terminalAgentLinkList.add(terminalAgentLink);
					terminalAgentLinkMap.put(terminalAgentLink.getFaultCategoryId(), terminalAgentLinkList);
					terminalAgentLinkList = null;
					terminalAgentLink = null;
				}
			}
		} catch (Exception ex) {
			errorLog(ex);
		}
		return terminalAgentLinkMap;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to check and insert the terminal down time 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String terminalId, String terminalName, String reason, String downType, String dateId
	 ********************************************************************************************************************/
	public void checkAndInsertTerminalDownTimeInfo(SimpleDateFormat simpleDateFormat, String terminalId, String terminalName, 
			String reason, String downType, String dateId)
	{

		if (!checkForTerminalDownTimeInfo(terminalId, reason)) 
		{
			/*StringBuffer insertQuery = new StringBuffer(
					"INSERT INTO ADM_MNTC_TRDT_DETL ( AMT_TERM_DNSQ, "
							+ "ACT_TERM_ID,AMT_TERM_DNST,AMT_TERM_DNTP,ACD_DATE_ID,AMT_TERM_DNMN,AMT_TERM_RESN, ACT_TERM_NAME"
							+ ") VALUES('" + RRN.genRRN() + "', " + "'" + terminalId + "'" + ",GETDATE(),'" + downType
							+ "'," + dateId + ",'0','" + reason + "','" + terminalName + "')");*/
			
			StringBuffer insertQuery = new StringBuffer(Configuration.queryBundle.getString("insert.terminal.downtime.info"));
			
			/*StringBuffer insertQuery = new StringBuffer(
					"INSERT INTO ADM_MNTC_TRDT_DETL ( AMT_TERM_DNSQ, "
							+ "ACT_TERM_ID,AMT_TERM_DNST,AMT_TERM_DNTP,ACD_DATE_ID,AMT_TERM_DNMN,AMT_TERM_RESN, ACT_TERM_NAME"
							+ ") VALUES(?, ?, ?, ?, ?, ?, ?, ?)");*/
			
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(RRN.genRRN());
			objectList.add(terminalId);
			objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
			objectList.add(downType);
			objectList.add(dateId);
			objectList.add(0);
			objectList.add(reason);
			objectList.add(terminalName);
			
			try 
			{
				dbimp.insertUsingPreparedStatement(insertQuery.toString(), objectList);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the list of all terminal down time count for the terminal and reason
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String terminalId, String reason
	 ********************************************************************************************************************/
	private boolean checkForTerminalDownTimeInfo(String terminalId, String reason)
	{
		boolean flag = false;

		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.terminal.downtime.info"));
		query = query.append(" WHERE " + "ACT_TERM_ID='" + terminalId + "' AND AMT_TERM_RESN = '" + reason + "' AND AMT_TERM_DNET IS NULL");
		
		/*StringBuffer query = new StringBuffer(
				"SELECT COUNT(1) FROM ADM_MNTC_TRDT_DETL WHERE "
						+ "ACT_TERM_ID='" + terminalId
						+ "' AND AMT_TERM_RESN = '" + reason
						+ "' AND AMT_TERM_DNET IS NULL");*/
		try {
			dbimp.executeQuery(query.toString());
			if (dbimp.resultSet != null) {
				dbimp.resultSet.next();
				if (Integer.parseInt(dbimp.resultSet.getString(1)) > 0)
					flag = true;
				else
					flag = false;
			}
		} catch (Exception ex) {
			errorLog(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}
		return flag;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to update the status of the terminal 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId, String terminalId, String terminalStatus
	 ********************************************************************************************************************/
	public void updateTerminalStatus(String bankId, String terminalId, String terminalStatus)
	{
		List<Object> objectList = new ArrayList<Object>();
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("update.terminal.info"));
		query = query.append("AMT_TERM_STFG = '" + terminalStatus + "' WHERE ACT_TERM_ID='" 
				+ terminalId + "' AND ACB_BANK_ID='" + bankId + "'");
		
		/*String query = "UPDATE ADM_MNTC_TMSU_MAST SET "
				+ "AMT_TERM_STFG = '" + terminalStatus + "' WHERE ACT_TERM_ID='" 
				+ terminalId + "' AND ACB_BANK_ID='" + bankId + "'";*/

		try {
			//dbimp.updateSQL(query);
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
		} catch (Exception ex) {
			errorLog(ex);
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to generate the faults 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: Map<String, FaultMonitorInfoBean> faultMonitorInfoMap
	 ********************************************************************************************************************/
	public void generteFaultMonitorInfo(
			Map<String, FaultMonitorInfoBean> faultMonitorInfoMap) {
		if (faultMonitorInfoMap != null) 
		{
			String query = Configuration.queryBundle.getString("insert.faultMonitor.info");
			
			/*String query = "INSERT INTO ADM_MNTC_FMIN_DETL " +
								"(AMF_FALT_SQNO, ACT_TERM_ID, AMF_FALT_ID, AMF_FALT_DESC," +
								"AMF_FALT_OCTM,	AMF_FALT_STAT, AMF_FALT_LSTM, ACB_BANK_ID," +
								"ACC_COUN_ID," +
								"ACS_STAT_ID, ACC_CITY_ID, ACR_REGN_ID, ACB_BRCH_ID, ACD_DATE_ID, AMF_FACT_ID, ACT_TERM_NAME)" +
								" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/
			
			FaultMonitorInfoBean faultMonitorInfo = null;
			List<Object> objectList = null;
			for (String faultId : faultMonitorInfoMap.keySet()) 
			{
				faultMonitorInfo = faultMonitorInfoMap.get(faultId);
				objectList = new ArrayList<Object>();
				try 
				{
					objectList.add(RRN.genRRN());
					objectList.add(faultMonitorInfo.getTerminalId());
					objectList.add(faultMonitorInfo.getFaultId());
					objectList.add(faultMonitorInfo.getFaultDescription());
					objectList.add(faultMonitorInfo.getFaultOccuredTime());
					objectList.add(faultMonitorInfo.getFaultStatus());
					objectList.add(faultMonitorInfo.getFaultOccuredTime());
					objectList.add(faultMonitorInfo.getBankId());
					objectList.add(faultMonitorInfo.getCountryId());
					objectList.add(faultMonitorInfo.getStateId());
					objectList.add(faultMonitorInfo.getCityId());
					objectList.add(faultMonitorInfo.getRegionId());
					objectList.add(faultMonitorInfo.getBranchId());
					objectList.add(faultMonitorInfo.getDateId());
					objectList.add(faultMonitorInfo.getFaultCategoryId());
					objectList.add(faultMonitorInfo.getTerminalName());
					dbimp.insertUsingPreparedStatement(query, objectList);
				} 
				catch (Exception ex) 
				{
					errorLog(ex);
				}
				faultMonitorInfo = null;
				objectList = null;
			}
		}
	}

	/********************************************************************************************************************
	Purpose in brief		: Method to generate the faults remarks
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: Map<String, String> faultRemarkInfo
	 ********************************************************************************************************************/
	public void generteFaultRemarkInfo(SimpleDateFormat simpleDateFormat, String bankId, String terminalId, Map<String, String> faultRemarkInfo, Map<String, String> penaltyCount) {
		if (faultRemarkInfo != null) 
		{
			StringBuffer insertQuery = new StringBuffer(Configuration.queryBundle.getString("insert.faultRemark.info"));	
			
			Set<String> insertQuerySet = faultRemarkInfo.keySet();
			List<Object> objectList = null;
			for (String faultId : insertQuerySet) {
				try 
				{
					objectList = new ArrayList<Object>();
					objectList.add(bankId);
					objectList.add(terminalId);
					objectList.add(faultRemarkInfo.get(faultId));
					objectList.add("AUTO :: " + (Integer.parseInt(penaltyCount.get(faultId)) + 1) + " TICKETS GENERATED WITHIN 48HRS  WITH SAME FAULT");
					objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
					objectList.add(RRN.genRRN());
					dbimp.insertUsingPreparedStatement(insertQuery.toString(), objectList);
				} catch (Exception ex) {
					errorLog(ex);
				}
			}
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to check whether supervisory ticket available. If available make other tickets to inprogress. 
							  If not make the tickets to earlier call status 
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: SimpleDateFormat simpleDateFormat, String bankId, String terminalId, Map<String, 
								List<String>> ticketMap
	 ********************************************************************************************************************/
	public boolean checkAndUpdateSuperviosrOpenTicket(SimpleDateFormat simpleDateFormat, String bankId,
			String terminalId, Map<String, List<String>> ticketMap) {
		boolean supervisoryFlag = false;
		StringBuffer query = null;

		List<String> list = null;

		//list = ticketMap.get("NDCS01");
		list = ticketMap.get("OSSIUSEN0");

		try {
			if (list != null && list.size() != 0) // Supervisory Mode
			{
				StringBuffer earlier_call_status;
				StringBuffer updateQuery;
				StringBuffer ticket_id;

				query = new StringBuffer(Configuration.queryBundle.getString("get.ticketEscalation.supervisoryTicket"));
				query = query.append(" AND ACT_TERM_ID = '" + terminalId + "'");
				
				/*query = new StringBuffer(
						"SELECT ACT_TERM_ID, AMT_TCKT_CLST, AMT_TCKT_NO, AMT_TCKT_IPTM FROM ADM_MNTC_TEIN_DETL WHERE ACT_TERM_ID = '"
								+ terminalId
								+ "' AND AMT_TCKT_STAT = 'O' AND AMF_FALT_ID NOT IN ('NDCS011' ,'DDCSUP1', 'DDCSUP2', 'DDCSUP3')");*/

				dbimp.executeQuery(query.toString());

				Set<String> inprogressSet = null;
				Set<String> inprogressWithTimeSet = null;

				if (dbimp.resultSet != null) {
					inprogressSet = new TreeSet<String>();
					inprogressWithTimeSet = new TreeSet<String>();

					while (dbimp.resultSet.next()) {
						try {
							earlier_call_status = new StringBuffer(
									dbimp.resultSet.getString(2));
							ticket_id = new StringBuffer(dbimp.resultSet
									.getString(3));

							if (!earlier_call_status.toString().trim().equals(
									"INPROGRESS")) {
								if (!(dbimp.resultSet.getString(4) != null))
									inprogressWithTimeSet.add(ticket_id
											.toString());
								else
									inprogressSet.add(ticket_id.toString());
							} else
								continue;

							earlier_call_status = null;
							ticket_id = null;
						} catch (Exception ex) {
							errorLog(ex);
						}
					}
					StringBuffer allTicketId = new StringBuffer();
					boolean flag = true;
					for (String ticketIds : inprogressSet) {
						if (flag)
							allTicketId = allTicketId.append("'" + ticketIds
									+ "'");
						else
							allTicketId = allTicketId.append(",'" + ticketIds
									+ "'");
						flag = false;
					}
					if (!allTicketId.toString().equals("")) {
						List<Object> objectList = new ArrayList<Object>();
						
						updateQuery = new StringBuffer(Configuration.queryBundle.getString("update.ticketescalation.superviosry.info1"));
						updateQuery = updateQuery.append(" WHERE ACT_TERM_ID='"
										+ terminalId + "' AND AMT_TCKT_NO IN("
										+ allTicketId.toString() + ")"
										+ " AND AMT_TCKT_IPTM IS NOT NULL");
						
						/*updateQuery = new StringBuffer(
								"UPDATE ADM_MNTC_TEIN_DETL SET AMT_TCKT_CLST='INPROGRESS', "
										+ "AMT_TCKT_ECST=AMT_TCKT_CLST WHERE ACT_TERM_ID='"
										+ terminalId + "' AND AMT_TCKT_NO IN("
										+ allTicketId.toString() + ")"
										+ " AND AMT_TCKT_IPTM IS NOT NULL");*/

						//dbimp.updateSQL(updateQuery.toString());
						dbimp.insertUsingPreparedStatement(updateQuery.toString(), objectList);
					}

					flag = true;
					allTicketId = new StringBuffer("");
					for (String ticketIds : inprogressWithTimeSet) {
						if (flag)
							allTicketId = allTicketId.append("'" + ticketIds
									+ "'");
						else
							allTicketId = allTicketId.append(",'" + ticketIds
									+ "'");
						flag = false;
					}
					if (!allTicketId.toString().equals("")) {
						
						List<Object> objectList = new ArrayList<Object>();
						objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
						
						updateQuery = new StringBuffer(Configuration.queryBundle.getString("update.ticketescalation.superviosry.info2"));
						updateQuery = updateQuery.append(" WHERE ACT_TERM_ID='"
										+ terminalId + "' AND AMT_TCKT_NO IN("
										+ allTicketId.toString() + ") "
										+ "AND AMT_TCKT_IPTM IS NULL");
						
						/*updateQuery = new StringBuffer(
								"UPDATE ADM_MNTC_TEIN_DETL SET AMT_TCKT_CLST='INPROGRESS', "
										+ "AMT_TCKT_ECST=AMT_TCKT_CLST, AMT_TCKT_IPTM = ? WHERE ACT_TERM_ID='"
										+ terminalId + "' AND AMT_TCKT_NO IN("
										+ allTicketId.toString() + ") "
										+ "AND AMT_TCKT_IPTM IS NULL");*/
						dbimp.insertUsingPreparedStatement(updateQuery.toString(), objectList);
						//dbimp.updateSQL(updateQuery.toString());
					}

				}
				supervisoryFlag = true;
			} else // Normal Mode
			{
				query = null;
				
				query = new StringBuffer(Configuration.queryBundle.getString("get.ticketEscalation.inprogressTicket"));
				query = query.append(" AND ACT_TERM_ID = '" + terminalId + "'");
				
				/*query = new StringBuffer(
						"SELECT ACT_TERM_ID, AMT_TCKT_NO, AMT_TCKT_ECST FROM ADM_MNTC_TEIN_DETL WHERE ACT_TERM_ID = '"
								+ terminalId
								+ "' AND AMT_TCKT_STAT = 'O' AND AMT_TCKT_CLST = 'INPROGRESS'");*/

				dbimp.executeQuery(query.toString());

				StringBuffer updateQuery;

				Set<String> inprogressSet = null;

				if (dbimp.resultSet != null) {
					inprogressSet = new TreeSet<String>();
					while (dbimp.resultSet.next())
						inprogressSet.add(new String(dbimp.resultSet
								.getString(2)));

					StringBuffer allTicketId = new StringBuffer();
					boolean flag = true;
					for (String ticketIds : inprogressSet) {
						if (flag)
							allTicketId = allTicketId.append("'" + ticketIds
									+ "'");
						else
							allTicketId = allTicketId.append(",'" + ticketIds
									+ "'");
						flag = false;
					}
					
					List<Object> objectList = new ArrayList<Object>();
					/*objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));*/

					if (!allTicketId.toString().equals("")
							&& allTicketId.length() > 0) {
						/*updateQuery = new StringBuffer(
								"UPDATE ADM_MNTC_TEIN_DETL WITH (UPDLOCK) SET "
										+ "AMT_TCKT_INDR = DATEDIFF ( SS , AMT_TCKT_IPTM , GETDATE() ) WHERE "
										+ "ACT_TERM_ID = '"
										+ terminalId
										+ "' AND AMT_TCKT_STAT='O' AND AMT_TCKT_NO IN ("
										+ allTicketId.toString() + ")"
										+ " AND AMT_TCKT_INDR IS NULL");*/
						
						/*updateQuery = new StringBuffer(
								"UPDATE ADM_MNTC_TEIN_DETL SET "
										+ "AMT_TCKT_INDR = ((SYSDATE - AMT_TCKT_IPTM) * 86400) WHERE "
										+ "ACT_TERM_ID = '"
										+ terminalId
										+ "' AND AMT_TCKT_STAT='O' AND AMT_TCKT_NO IN ("
										+ allTicketId.toString() + ")"
										+ " AND AMT_TCKT_INDR IS NULL");*/
						
						//SELECT  (TO_DATE(TO_CHAR(SYSDATE, 'DD-MM-YYYY hh24:mi:ss'),  'DD-MM-YYYY hh24:mi:ss') - TO_DATE(TO_CHAR(AMF_FALT_OCTM, 'DD-MM-YYYY hh24:mi:ss'),  'DD-MM-YYYY hh24:mi:ss')) * (24*60*60)  FROM ADM_MNTC_FMIN_DETL
						
						updateQuery = new StringBuffer(Configuration.queryBundle.getString("update.ticketescalation.superviosry.info3"));
						updateQuery = updateQuery.append("WHERE " + "ACT_TERM_ID = '" + terminalId + "' AND AMT_TCKT_STAT='O' AND AMT_TCKT_NO IN (" + allTicketId.toString() + ")" + " AND AMT_TCKT_INDR IS NULL");
						
						/*updateQuery = new StringBuffer(
								"UPDATE ADM_MNTC_TEIN_DETL SET "
										+ "AMT_TCKT_INDR = (TO_DATE(TO_CHAR(SYSDATE, 'DD-MM-YYYY hh24:mi:ss'),  " +
												"'DD-MM-YYYY hh24:mi:ss') - TO_DATE(TO_CHAR(AMT_TCKT_IPTM, " +
												"'DD-MM-YYYY hh24:mi:ss'),  'DD-MM-YYYY hh24:mi:ss')) * (24*60*60)" +
										" WHERE "
										+ "ACT_TERM_ID = '"
										+ terminalId
										+ "' AND AMT_TCKT_STAT='O' AND AMT_TCKT_NO IN ("
										+ allTicketId.toString() + ")"
										+ " AND AMT_TCKT_INDR IS NULL");*/

						//dbimp.updateSQL(updateQuery.toString());
						dbimp.insertUsingPreparedStatement(updateQuery.toString(), objectList);

						updateQuery = null;
						updateQuery = new StringBuffer(Configuration.queryBundle.getString("update.ticketescalation.superviosry.info4"));
						updateQuery = updateQuery.append("WHERE " + "ACT_TERM_ID = '" + terminalId + "' AND AMT_TCKT_STAT='O' AND AMT_TCKT_NO IN (" + allTicketId.toString() + ")");
						
						/*updateQuery = new StringBuffer(
								"UPDATE ADM_MNTC_TEIN_DETL SET AMT_TCKT_CLST = AMT_TCKT_ECST "
										+ "WHERE "
										+ "ACT_TERM_ID = '"
										+ terminalId
										+ "' AND AMT_TCKT_STAT='O' AND AMT_TCKT_NO IN ("
										+ allTicketId.toString() + ")" + "");*/
						
						dbimp.insertUsingPreparedStatement(updateQuery.toString(), objectList);
					}
				}
				supervisoryFlag = false;
			}
		} catch (Exception ex) {
			errorLog(ex);
		}
		return supervisoryFlag;
	}
	
	
	/********************************************************************************************************************
	Purpose in brief		: Method to save the Cash details for the terminals in hopper wise. 
	Written by				: Vijayarumugam K
	Last Modified			: 23 - Mar - 2015
	Arguments passed		: CashInfoForm cashInfo
	 ********************************************************************************************************************/
	public void saveCashInfo(CashInfoForm cashInfoForm)
	{
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("insert.cash.info"));
		
		/*StringBuffer query = new StringBuffer("INSERT INTO ADM_MNTC_CSIN_DETL (AMC_TERM_CSSQ ,ACT_TERM_ID ,AMC_TERM_CRID ,AMC_TERM_DENM ,AMC_TERM_BALE" +
				" ,AMC_TERM_HPPS ,AMC_TERM_RJBL ,ACB_BANK_ID ,AMC_TERM_NONT, AMC_TERM_CSTP, ACT_TERM_NAME) VALUES (? ,?, ? ,? ,? ,? ,? ,? ,?, ? ,?)");*/
		try
		{
			List<Object> objectList = null;
			objectList = new ArrayList<Object>();
			
			objectList.add(RRN.genRRN());
			objectList.add(cashInfoForm.getTerminalId());
			objectList.add(cashInfoForm.getCurrencyId());
			objectList.add(cashInfoForm.getDenomination());
			objectList.add(cashInfoForm.getBalance());
			objectList.add(cashInfoForm.getHopperPosition());
			if(cashInfoForm.getRejectedBalance() != null)
				objectList.add(cashInfoForm.getRejectedBalance());
			else
				objectList.add("");
			objectList.add(cashInfoForm.getBankId());
			objectList.add(cashInfoForm.getNoOfNotes());
			objectList.add(cashInfoForm.getCassetteType());
			objectList.add(cashInfoForm.getTerminalName());
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			
			objectList = null;
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to update the Cash details for the terminals in hopper wise. 
	Written by				: Vijayarumugam K
	Last Modified			: 23 - Mar - 2015
	Arguments passed		: Map<String, CashInfoForm> cashInfo
	 ********************************************************************************************************************/
	public void updateCashInfo(CashInfoForm cashInfoForm)
	{
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("update.cash.info"));
		
		/*StringBuffer query = new StringBuffer("UPDATE ADM_MNTC_CSIN_DETL SET AMC_TERM_CRID = ?, AMC_TERM_DENM = ?, AMC_TERM_BALE = ?, AMC_TERM_RJBL = ?, " +
				"AMC_TERM_NONT = ?, AMC_TERM_CSTP = ? WHERE ACB_BANK_ID = ? AND ACT_TERM_ID = ? AND AMC_TERM_HPPS = ?");*/
		try
		{
			List<Object> objectList = null;
			objectList = new ArrayList<Object>();
			
			objectList.add(cashInfoForm.getCurrencyId());
			objectList.add(cashInfoForm.getDenomination());
			objectList.add(cashInfoForm.getBalance());
			if(cashInfoForm.getRejectedBalance() != null)
				objectList.add(cashInfoForm.getRejectedBalance());
			else
				objectList.add("");
			objectList.add(cashInfoForm.getNoOfNotes());
			objectList.add(cashInfoForm.getCassetteType());
			objectList.add(cashInfoForm.getBankId());
			objectList.add(cashInfoForm.getTerminalId());
			objectList.add(cashInfoForm.getHopperPosition());
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to delete the Cash details for the terminals in hopper wise. 
	Written by				: Vijayarumugam K
	Last Modified			: 23 - Mar - 2015
	Arguments passed		: CashInfoForm cashInfo
	 ********************************************************************************************************************/
	public void deleteCashInfo(CashInfoForm cashInfoForm)
	{
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("delete.cash.info"));
		/*StringBuffer query = new StringBuffer("DELETE FROM ADM_MNTC_CSIN_DETL WHERE ACB_BANK_ID = ? AND ACT_TERM_ID = ? AND AMC_TERM_HPPS = ?");*/
		try
		{
			List<Object> objectList = null;
			objectList = new ArrayList<Object>();
			
			objectList.add(cashInfoForm.getBankId());
			objectList.add(cashInfoForm.getTerminalId());
			objectList.add(cashInfoForm.getHopperPosition());
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			
			objectList = null;
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the Hopper position of the Cash details  
	Written by				: Vijayarumugam K
	Last Modified			: 23 - Mar - 2015
	Arguments passed		: String bankId, String terminalId
	 ********************************************************************************************************************/
	public List<Integer> getHopperPositionInCashInfo(String bankId, String terminalId)
	{
		List<Integer> cashInfoList = null;
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.cash.info"));
		query = query.append(" WHERE ACB_BANK_ID ='" + bankId + "' AND ACT_TERM_ID = '" + terminalId +  "'");
		
		/*String query = "SELECT AMC_TERM_HPPS FROM ADM_MNTC_CSIN_DETL WHERE ACB_BANK_ID ='" + bankId + "' AND ACT_TERM_ID = '" + terminalId +  "'";*/
		
		try
		{
			dbimp.resultSet = null;
			dbimp.executeQuery(query.toString());
			if(dbimp.resultSet != null)
			{
				cashInfoList = new ArrayList<Integer>();
				while(dbimp.resultSet.next())
					cashInfoList.add(dbimp.resultSet.getInt(1));
			}
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		return cashInfoList;
	}
	
	private void insertEmailInformation(String bankId, String terminalName, String terminalLocation, String ticketId, String faultDescription, String faultOpenTime, String estimatedCloseTime, String emailId)
	{
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("insert.email.info"));
		
		StringBuffer body = new StringBuffer("Dear Sir/Madam, <BR> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;New Ticket assigned to you, details given below <BR><BR>");
		body = body.append("<table border=1>");
		body = body.append("<tr>");
		body = body.append("<td>");
		body = body.append("Ticket Id");
		body = body.append("</td>");
		body = body.append("<td>");
		body = body.append(ticketId);
		body = body.append("</td>");
		body = body.append("</tr>");
		body = body.append("<tr>");
		body = body.append("<td>");
		body = body.append("Terminal Name");
		body = body.append("</td>");
		body = body.append("<td>");
		body = body.append(terminalName);
		body = body.append("</td>");
		body = body.append("</tr>");
		body = body.append("<tr>");
		body = body.append("<td>");
		body = body.append("Terminal Address");
		body = body.append("</td>");
		body = body.append("<td>");
		body = body.append(terminalLocation);
		body = body.append("</td>");
		body = body.append("</tr>");
		body = body.append("<tr>");
		body = body.append("<td>");
		body = body.append("Fault Description");
		body = body.append("</td>");
		body = body.append("<td>");
		body = body.append(faultDescription);
		body = body.append("</td>");
		body = body.append("</tr>");
		body = body.append("<tr>");
		body = body.append("<td>");
		body = body.append("Ticket Open Time");
		body = body.append("</td>");
		body = body.append("<td>");
		body = body.append(faultOpenTime);
		body = body.append("</td>");
		body = body.append("</tr>");
		body = body.append("<tr>");
		body = body.append("<td>");
		body = body.append("Estimated Close Time");
		body = body.append("</td>");
		body = body.append("<td>");
		body = body.append(estimatedCloseTime);
		body = body.append("</td>");
		body = body.append("</tr>");
		body = body.append("</table>");
		body = body.append("<BR>Regards,<BR>FSS ATM Monitoring");
		
		/*INSERT INTO ADM_COMM_ALEM_MAST(ACA_ALEM_SQNO, ACA_ALEM_STAT, ACB_BANK_ID, ACA_ALEM_SUB, ACA_ALEM_MSG, ACA_ALEM_EID) VALUES(?, ?, ?, ?, ?, ?)*/
		
		try
		{
			List<Object> objectList = null;
			objectList = new ArrayList<Object>();
			
			objectList.add(RRN.genRRN());
			objectList.add("N");
			objectList.add(bankId);
			objectList.add("[Ticket id:" + ticketId + "] - Your new Ticket");
			objectList.add(body.toString());
			objectList.add(emailId);
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			
			objectList = null;
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to generate the Tickets
	Written by				: Vijayarumugam K
	Last Modified			: 19 - Aug - 2015
	Arguments passed		: SimpleDateFormat simpleDateFormat, TerminalFormBean terminalFormBean,
			Map<String, List<String>> ticketEscalationInfo,  Map<String, String> closeTicketMap
	 ********************************************************************************************************************/
	public Map<String, String> generteTickets(SimpleDateFormat simpleDateFormat, TerminalFormBean terminalFormBean,  
			Map<String, List<String>> ticketEscalationInfo, 
			Map<String, String> closeTicketMap, Map<String, String> penaltyCount)
	{
		traceLog("GENERATE TICKETS :: ");
		Map<String, String> faultRemarkInfo = null;
		
		try
		{
			Set<String> faultCategoryIdSet = ticketEscalationInfo.keySet();
			List<String> faultIdList = null;
			
			Set<String> closeSet = closeTicketMap.keySet();
			for (String closeFault : closeSet) {
				for (String faultCategoryId : faultCategoryIdSet) {
					faultIdList = ticketEscalationInfo.get(faultCategoryId);
					if (faultIdList.contains(closeFault)) {
						faultIdList.remove(closeFault);
						ticketEscalationInfo.put(faultCategoryId, faultIdList);
						break;
					}
				}
			}
			
			FaultInfoFormBean faultInfo = null;
			Map<String, List<TerminalAgentLink>> terminalAgentLinkMap = getTerminalAgentLink(terminalFormBean.getBankId(), 
					terminalFormBean.getTerminalId());
			traceLog("TERMINAL AGENT LINK MAP :: " + terminalAgentLinkMap.toString());
			/***
			 * Check Whether Terminal is linked with Fault category and Agent
			 */
			if(terminalAgentLinkMap != null && terminalAgentLinkMap.size() != 0)
			{
				AgentShiftBean agentShiftBean = null;
				for (String faultCategoryId : faultCategoryIdSet) 
				{
					List<TerminalAgentLink> terminalAgentLinkList = terminalAgentLinkMap.get(faultCategoryId);
					
					/**
					 * Check whether particular fault category is linked with terminal id and agent
					 */
					if(terminalAgentLinkList != null && terminalAgentLinkList.size() != 0)
					{
						faultIdList = ticketEscalationInfo.get(faultCategoryId);
						
						agentShiftBean = getAgentFromList(terminalFormBean, terminalAgentLinkList);
						traceLog("AGENT SHIFT BEAN DETAILS :: " + agentShiftBean.getAgentId());
						String rootCauseId = null;
						for (String faultId : faultIdList) 
						{
							if (Configuration.faultInfoBean.get(faultId.toString().trim()) != null) 
							{
								faultInfo = Configuration.faultInfoBean.get(faultId.toString().trim());
								
								if(!(faultInfo.getSeverity().equalsIgnoreCase("0")))
								{
									if (faultInfo.getFaultCategoryId() != null && faultInfo.getFaultCategoryId()
											.equals("COF"))
										rootCauseId = "MSCCOF30";
									else
										rootCauseId = "MSCCOF29";
									
									String ticket = Configuration.ticketIdPrefix + RRN.genUniqueId();
									
									traceLog("FAULT ID :: " + faultInfo.getFaultId() + " :: TICKET ID:: " + ticket);
									
									String query = Configuration.queryBundle.getString("insert.ticketEscalation.info");
									
									List<Object> objectList = new ArrayList<Object>();
									String ticketId = RRN.genRRN();
									objectList.add(ticketId);
									objectList.add(ticket);
									objectList.add(terminalFormBean.getTerminalId());
									objectList.add(terminalFormBean.getBankId());
									objectList.add(faultInfo.getFaultId());
									objectList.add(faultInfo.getFaultDesc());
									objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
									//objectList.add(serviceProviderAgentInfo.getServiceProviderAgentId());
									objectList.add(agentShiftBean.getAgentId());
									objectList.add("N");
									objectList.add("N");
									objectList.add("Y");
									objectList.add(Configuration.dateTable);
									/*objectList.add(srvcProviderInfo.getServiceProviderId());
									objectList.add(srvcProviderInfo.getServiceProviderTypeId());*/
									
									objectList.add(agentShiftBean.getServiceProviderId());
									objectList.add(agentShiftBean.getServiceProviderTypeId());
									
									objectList.add(faultInfo.getFaultCategoryId());
									objectList.add(terminalFormBean.getCountryId());
									objectList.add(terminalFormBean.getStateId());
									objectList.add(terminalFormBean.getCityId());
									objectList.add(terminalFormBean.getRegionId());
									objectList.add(terminalFormBean.getBranchId());
									objectList.add("SYSTEM");
									objectList.add(rootCauseId);
									
									if(faultInfo.getSeverity().equalsIgnoreCase("1"))
										objectList.add(Timestamp.valueOf(simpleDateFormat.format(agentShiftBean.getMinorEstimatedDateTime().getTime())));
									else if(faultInfo.getSeverity().equalsIgnoreCase("2"))
										objectList.add(Timestamp.valueOf(simpleDateFormat.format(agentShiftBean.getMajorEstimatedDateTime().getTime())));
									else if(faultInfo.getSeverity().equalsIgnoreCase("3"))
										objectList.add(Timestamp.valueOf(simpleDateFormat.format(agentShiftBean.getCriticalEstimatedDateTime().getTime())));
									
									objectList.add(terminalFormBean.getTerminalName());
									objectList.add("");
									objectList.add("N");
									objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
									
									dbimp.insertUsingPreparedStatement(query, objectList);
									
									/*if(faultInfo.getSeverity().equalsIgnoreCase("1"))
										insertEmailInformation(terminalFormBean.getBankId(), terminalFormBean.getTerminalName(), 
											terminalFormBean.getTerminalLocation() , ticketId, faultInfo.getFaultDesc(), 
											(Timestamp.valueOf(simpleDateFormat.format(new Date()))).toString(), 
											(Timestamp.valueOf(simpleDateFormat.format(agentShiftBean.getMinorEstimatedDateTime().getTime()))).toString(), Configuration.sericeProviderAgent.get(agentShiftBean.getAgentId()).getEmailId());
									else if(faultInfo.getSeverity().equalsIgnoreCase("2"))
										insertEmailInformation(terminalFormBean.getBankId(), terminalFormBean.getTerminalName(), 
											terminalFormBean.getTerminalLocation() , ticketId, faultInfo.getFaultDesc(), 
											(Timestamp.valueOf(simpleDateFormat.format(new Date()))).toString(), 
											(Timestamp.valueOf(simpleDateFormat.format(agentShiftBean.getMajorEstimatedDateTime().getTime()))).toString(), Configuration.sericeProviderAgent.get(agentShiftBean.getAgentId()).getEmailId());
									else if(faultInfo.getSeverity().equalsIgnoreCase("3"))
										insertEmailInformation(terminalFormBean.getBankId(), terminalFormBean.getTerminalName(), 
												terminalFormBean.getTerminalLocation() , ticketId, faultInfo.getFaultDesc(), 
												(Timestamp.valueOf(simpleDateFormat.format(new Date()))).toString(), 
												(Timestamp.valueOf(simpleDateFormat.format(agentShiftBean.getCriticalEstimatedDateTime().getTime()))).toString(), Configuration.sericeProviderAgent.get(agentShiftBean.getAgentId()).getEmailId());
									*/
									
									if(penaltyCount != null && penaltyCount.get(faultInfo.getFaultId()) != null)
									{
										if(!(faultRemarkInfo != null))
											faultRemarkInfo = new HashMap<String, String>();
										
										faultRemarkInfo.put(faultInfo.getFaultId(), ticket);
									}
								}
							}
						}
					}
					else
					{
						traceLog("FAULT CATEGORY NOT LINKED WITH SERVICE PROVIDER AGENT FOR THIS TERMINAL");
						continue;
					}
				}	
			}
			else
				traceLog("TERMINAL AGENT NOT LINKED WITH FAULTS");
			
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		return faultRemarkInfo;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to AgentId and Shift Start Time.
	Written by				: Vijayarumugam K
	Last Modified			: 19 - Aug - 2015
	Arguments passed		: TerminalFormBean terminalFormBean,  List<TerminalAgentLink> terminalAgentLinkList
	 ********************************************************************************************************************/
	private AgentShiftBean getAgentFromList(TerminalFormBean terminalFormBean, List<TerminalAgentLink> terminalAgentLinkList)
	{
		traceLog("GET AGENT FROM LIST");
		AgentShiftBean agentShiftBean = null;
		try
		{
			/***
			 * If only one agent is linked with the fault category IF BLOCK
			 */
			
			TerminalAgentLink terminalAgentLinkInfo = null;
			
			Calendar terminalStartTime = Calendar.getInstance();
			Calendar terminalEndTime = Calendar.getInstance();
			
			Calendar nextTerminalWorkingHours = null;
			
			Calendar currentTime = Calendar.getInstance();
			
			if(terminalFormBean.getNightStartTime() != null && terminalFormBean.getNightEndTime() != null)
			{
				traceLog("NIGHT FLAG ENABLED " + terminalFormBean.getNightStartTime() + " :: " + terminalFormBean.getNightEndTime());
				String startTime = terminalFormBean.getNightStartTime();
				String endTime = terminalFormBean.getNightEndTime();
				
				terminalStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime.substring(0, startTime.indexOf(":"))));
				terminalStartTime.set(Calendar.MINUTE, Integer.parseInt(startTime.substring(startTime.indexOf(":")+1, startTime.length())));
				
				terminalEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTime.substring(0, endTime.indexOf(":"))));
				terminalEndTime.set(Calendar.MINUTE, Integer.parseInt(endTime.substring(endTime.indexOf(":")+1, endTime.length())));
				
				nextTerminalWorkingHours = Calendar.getInstance();
				
				if(currentTime.before(terminalEndTime))
					nextTerminalWorkingHours = terminalEndTime;
				else
				{
					if(terminalStartTime.after(terminalEndTime))
						terminalEndTime.add(Calendar.DATE, 1);
					
					if(nextTerminalWorkingHours.after(terminalStartTime) && nextTerminalWorkingHours.before(terminalEndTime))
						nextTerminalWorkingHours = terminalEndTime;
					else
						nextTerminalWorkingHours = null;
				}
			}
			
			if(terminalAgentLinkList.size() == 1)
			{
				traceLog("ONLY ONE AGENT IS LINKED");
				terminalAgentLinkInfo = terminalAgentLinkList.get(0);
				agentShiftBean = setValuesToAgentShiftBean(terminalAgentLinkInfo.getServiceProviderId(), terminalAgentLinkInfo.getServiceProviderAgentId(), 
						Integer.parseInt(terminalAgentLinkInfo.getMinorMins()), Integer.parseInt(terminalAgentLinkInfo.getMajorMins()), Integer.parseInt(terminalAgentLinkInfo.getCriticalMins()));
				if(nextTerminalWorkingHours != null)
				{
					agentShiftBean.setNightFlag(true);
					agentShiftBean.setShiftStartTime(terminalFormBean.getNightEndTime());
				}
				
				agentShiftBean = calucateEstimateTime(agentShiftBean, terminalStartTime, terminalEndTime);
			}
			else
			{
				traceLog("MORE THAN ONE AGENT IS LINKED");
				agentShiftBean = getAgentCurrentAlternateShift(terminalFormBean, terminalAgentLinkList, nextTerminalWorkingHours, terminalStartTime, terminalEndTime);
				agentShiftBean = calucateEstimateTime(agentShiftBean, terminalStartTime, terminalEndTime);
			}
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		return agentShiftBean;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to calculate Estimated Time for the tickets
	Written by				: Vijayarumugam K
	Last Modified			: 19 - Aug - 2015
	Arguments passed		: AgentShiftBean agentShiftBean, Calendar terminalNightStartTime, Calendar terminalNightEndTime
	********************************************************************************************************************/
	private AgentShiftBean calucateEstimateTime(AgentShiftBean agentShiftBean, Calendar terminalNightStartTime, Calendar terminalNightEndTime)
	{
		try
		{
			Calendar minorEstimatedDate = Calendar.getInstance();
			Calendar majorEstimatedDate = Calendar.getInstance();
			Calendar criticalEstimatedDate = Calendar.getInstance();
			
			boolean incrementFlag = false;
			
			if(agentShiftBean.getShiftStartTime() != null)
			{
				minorEstimatedDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(agentShiftBean.getShiftStartTime().substring(0, agentShiftBean.getShiftStartTime().indexOf(":"))));
				minorEstimatedDate.set(Calendar.MINUTE, Integer.parseInt(agentShiftBean.getShiftStartTime().substring(agentShiftBean.getShiftStartTime().indexOf(":") + 1, agentShiftBean.getShiftStartTime().length())));
				
				if(minorEstimatedDate.before(majorEstimatedDate))
				{
					minorEstimatedDate.add(Calendar.DATE, 1);
					incrementFlag = true;
				}
				
				majorEstimatedDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(agentShiftBean.getShiftStartTime().substring(0, agentShiftBean.getShiftStartTime().indexOf(":"))));
				majorEstimatedDate.set(Calendar.MINUTE, Integer.parseInt(agentShiftBean.getShiftStartTime().substring(agentShiftBean.getShiftStartTime().indexOf(":") + 1, agentShiftBean.getShiftStartTime().length())));
				
				criticalEstimatedDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(agentShiftBean.getShiftStartTime().substring(0, agentShiftBean.getShiftStartTime().indexOf(":"))));
				criticalEstimatedDate.set(Calendar.MINUTE, Integer.parseInt(agentShiftBean.getShiftStartTime().substring(agentShiftBean.getShiftStartTime().indexOf(":") + 1, agentShiftBean.getShiftStartTime().length())));
				
				if(incrementFlag)
				{
					majorEstimatedDate.set(Calendar.DATE, 1);
					criticalEstimatedDate.set(Calendar.DATE, 1);
				}
			}
			
			
			minorEstimatedDate.add(Calendar.MINUTE, agentShiftBean.getMinorMin());
			majorEstimatedDate.add(Calendar.MINUTE, agentShiftBean.getMajorMin());
			criticalEstimatedDate.add(Calendar.MINUTE, agentShiftBean.getCriticalMin());
			
			long difference =0l;
			
			Calendar currentTime = Calendar.getInstance();
			
			if(!agentShiftBean.isNightFlag() && (currentTime.after(terminalNightStartTime) && currentTime.before(terminalNightStartTime)))
				difference = (terminalNightEndTime.getTimeInMillis() - terminalNightStartTime.getTimeInMillis())/1000;
			
			
			minorEstimatedDate.add(Calendar.SECOND, Integer.parseInt(difference+""));
			majorEstimatedDate.add(Calendar.SECOND, Integer.parseInt(difference+""));
			criticalEstimatedDate.add(Calendar.SECOND, Integer.parseInt(difference+""));
			
			agentShiftBean.setMinorEstimatedDateTime(minorEstimatedDate);
			agentShiftBean.setMajorEstimatedDateTime(majorEstimatedDate);
			agentShiftBean.setCriticalEstimatedDateTime(criticalEstimatedDate);
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		return agentShiftBean;
	}
	
	
	/********************************************************************************************************************
	Purpose in brief		: Method to Current/Alternate Shift Id - AgentId and Shift Start Time
	Written by				: Vijayarumugam K
	Last Modified			: 19 - Aug - 2015
	Arguments passed		: TerminalFormBean terminalFormBean, List<TerminalAgentLink> terminalAgentLinkList
	 ********************************************************************************************************************/
	private AgentShiftBean getAgentCurrentAlternateShift(TerminalFormBean terminalFormBean, List<TerminalAgentLink> terminalAgentLinkList, Calendar nextTerminalWorkingHours, Calendar terminalStartTime, 
			Calendar terminalEndTime)
	{
		AgentShiftBean agentShiftBean = null;
		
		try
		{
			ServiceProviderAgentInfo serviceProviderAgent = null;
			
			/***
			 * This Fault Category has more than one Agent for this terminal
			 */
			for(TerminalAgentLink terminalAgentLink : terminalAgentLinkList)
			{
				serviceProviderAgent = Configuration.sericeProviderAgent.get(terminalAgentLink.getServiceProviderAgentId());
				if(serviceProviderAgent != null)
				{
					traceLog("GETTING CURRENT SHIFT AGENT");
					agentShiftBean = getCurrentShiftAgent(terminalFormBean, terminalAgentLink, serviceProviderAgent, nextTerminalWorkingHours);
					if(agentShiftBean != null)
						break;
				}
			}
			
			if(!(agentShiftBean != null))
			{
				traceLog("CURRENT TIME NO AGENT. GETTING ALTERNATE AGENT");
				agentShiftBean = getAlternateShiftAgent(terminalFormBean, terminalAgentLinkList, nextTerminalWorkingHours, terminalStartTime, terminalEndTime);
			}
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		return agentShiftBean;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to Current Shift Id - AgentId and Shift Start Time
	Written by				: Vijayarumugam K
	Last Modified			: 19 - Aug - 2015
	Arguments passed		: TerminalAgentLink terminalAgentLink, ServiceProviderAgentInfo serviceProviderAgent, 
							  Calendar terminalStartTime, Calendar terminalEndTime
	 ********************************************************************************************************************/
	
	private AgentShiftBean getCurrentShiftAgent(TerminalFormBean terminalFormBean, TerminalAgentLink terminalAgentLink, 
			ServiceProviderAgentInfo serviceProviderAgent, Calendar nextTerminalWorkingHours)
	{
		traceLog("GET CURRENT SHIFT AGENT");
		AgentShiftBean agentShiftBean = null;
		try
		{
			ServiceProviderShiftInfo serviceProviderShiftInfo = null;
			String shiftStartTime = null;
			String shiftEndTime = null;
			
			/***
			 * Check whether current time is configured in the Shift
			 */
			serviceProviderShiftInfo = Configuration.srvcProvShiftInfo.get(serviceProviderAgent.getShiftId());
			shiftStartTime = serviceProviderShiftInfo.getStartTime();
			shiftEndTime = serviceProviderShiftInfo.getEndTime();
			
			if(nextTerminalWorkingHours != null)
			{
				Calendar shiftStartingTime = Calendar.getInstance();
				shiftStartingTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(shiftStartTime.substring(0, shiftStartTime.indexOf(":"))));
				shiftStartingTime.set(Calendar.MINUTE, Integer.parseInt(shiftStartTime.substring(shiftStartTime.indexOf(":") + 1, shiftStartTime.length())));
				
				Calendar shiftEndingTime = Calendar.getInstance();
				
				shiftEndingTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(shiftEndTime.substring(0, shiftEndTime.indexOf(":"))));
				shiftEndingTime.set(Calendar.MINUTE, Integer.parseInt(shiftEndTime.substring(shiftEndTime.indexOf(":") + 1, shiftEndTime.length())));
				
				if(Integer.parseInt(shiftStartTime.replace(":", "")) > Integer.parseInt(shiftEndTime.replace(":", "")))
					shiftEndingTime.add(Calendar.DATE, 1);
				
				
				//if(shiftStartingTime.after(nextTerminalWorkingHours) && shiftEndingTime.before(nextTerminalWorkingHours))
				/***
				 * CASE4
				 */
				if(nextTerminalWorkingHours.after(shiftStartingTime) && nextTerminalWorkingHours.after(shiftEndingTime))
					return null;
			}
			
			shiftStartTime = shiftStartTime.replace(":", "");
			shiftEndTime = shiftEndTime.replace(":", "");
				
			if(checkCurrentShift(Integer.parseInt(shiftStartTime), Integer.parseInt(shiftEndTime)))
			{
				traceLog("CURRENT SHIFT :: " + terminalAgentLink.getServiceProviderAgentId());
				agentShiftBean = new AgentShiftBean();

				agentShiftBean = setValuesToAgentShiftBean(terminalAgentLink.getServiceProviderId(), terminalAgentLink.getServiceProviderAgentId(), 
						Integer.parseInt(terminalAgentLink.getMinorMins()), Integer.parseInt(terminalAgentLink.getMajorMins()), Integer.parseInt(terminalAgentLink.getCriticalMins()));
				
				Calendar currentTime = Calendar.getInstance();
				
				if(currentTime.before(nextTerminalWorkingHours))
				{
					traceLog("CURRENT SHIFT NIGHT FLAG ENABLE");
					agentShiftBean.setShiftStartTime(terminalFormBean.getNightEndTime());
					agentShiftBean.setNightFlag(true);
				}
			}
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		return agentShiftBean;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get Alternate Shift Id - AgentId and Shift Start Time
	Written by				: Vijayarumugam K
	Last Modified			: 19 - Aug - 2015
	Arguments passed		: List<TerminalAgentLink> terminalAgentLinkList, Calendar nextTerminalWorkingHours
	 ********************************************************************************************************************/
	private AgentShiftBean getAlternateShiftAgent(TerminalFormBean terminalFormBean, List<TerminalAgentLink> terminalAgentLinkList, Calendar nextTerminalWorkingHours, Calendar terminalStartTime, 
			Calendar terminalEndTime)
	{
		traceLog("GET ALTERNATE SHIFT AGENT");
		AgentShiftBean agentShiftBean = null;
		
		try
		{
			ServiceProviderShiftInfo serviceProviderShiftInfo = null;
			Calendar currentDate = Calendar.getInstance();
			
			long dateDifference = 0;
			long tempDateDifference = 0;
			String serviceProviderAgentId = "";
			String shiftStartTime = "";
			ServiceProviderAgentInfo serviceProviderAgent = null;
			
			/***
			 * No shift is configured for this time.
			 * load next consecutive shift
			 */
			for(TerminalAgentLink terminalAgentLink : terminalAgentLinkList)
			{
				serviceProviderAgent = Configuration.sericeProviderAgent.get(terminalAgentLink.getServiceProviderAgentId());
				
				serviceProviderShiftInfo = Configuration.srvcProvShiftInfo.get(serviceProviderAgent.getShiftId());
				
				Calendar shiftCalendar = Calendar.getInstance();
				shiftCalendar.set(Calendar.HOUR, Integer.parseInt(serviceProviderShiftInfo.getStartTime().substring(0, serviceProviderShiftInfo.getStartTime().indexOf(":"))));
				shiftCalendar.set(Calendar.MINUTE, Integer.parseInt(serviceProviderShiftInfo.getStartTime().substring(serviceProviderShiftInfo.getStartTime().indexOf(":") + 1, serviceProviderShiftInfo.getStartTime().length())));
				
				if(!(nextTerminalWorkingHours != null))
				{
					dateDifference = shiftCalendar.getTimeInMillis() - currentDate.getTimeInMillis();
					if(dateDifference >=0 && currentDate.after(shiftCalendar))
					{
						if(tempDateDifference == 0 || tempDateDifference > dateDifference)
						{
							tempDateDifference = dateDifference;
							serviceProviderAgentId = terminalAgentLink.getServiceProviderAgentId();
							shiftStartTime = serviceProviderShiftInfo.getStartTime();
						}
					}
					else
					{
						if(tempDateDifference == 0)
						{
							tempDateDifference = dateDifference;
							serviceProviderAgentId = terminalAgentLink.getServiceProviderAgentId();
							shiftStartTime = serviceProviderShiftInfo.getStartTime();
						}
					}
				}
				else
				{
					Calendar shiftEndCalendar = Calendar.getInstance();
					shiftEndCalendar.set(Calendar.HOUR, Integer.parseInt(serviceProviderShiftInfo.getEndTime().substring(0, serviceProviderShiftInfo.getEndTime().indexOf(":"))));
					shiftEndCalendar.set(Calendar.MINUTE, Integer.parseInt(serviceProviderShiftInfo.getEndTime().substring(serviceProviderShiftInfo.getEndTime().indexOf(":") + 1, serviceProviderShiftInfo.getEndTime().length())));
					
					if(Integer.parseInt(serviceProviderShiftInfo.getStartTime().replace(":", "")) > Integer.parseInt(serviceProviderShiftInfo.getEndTime().replace(":", "")))
						shiftEndCalendar.add(Calendar.DATE, 1);
					
					if(shiftCalendar.after(nextTerminalWorkingHours) && shiftEndCalendar.before(nextTerminalWorkingHours))
						continue;
					else
					{
						dateDifference = shiftCalendar.getTimeInMillis() - currentDate.getTimeInMillis();
						if(tempDateDifference == 0 || tempDateDifference > dateDifference && ((currentDate.after(shiftCalendar)) && (currentDate.before(shiftEndCalendar))))
						{
							tempDateDifference = dateDifference;
							serviceProviderAgentId = terminalAgentLink.getServiceProviderAgentId();
							shiftStartTime = serviceProviderShiftInfo.getStartTime();
							
						}
					}
				}
			}	
			
			for(TerminalAgentLink terminalAgentLink : terminalAgentLinkList)
			{
				if(serviceProviderAgentId.equals(terminalAgentLink.getServiceProviderAgentId()))
				{
					traceLog("ALTERNATE SHIFT AGENT :: " + terminalAgentLink.getServiceProviderAgentId());
					agentShiftBean = new AgentShiftBean();
					agentShiftBean = setValuesToAgentShiftBean(terminalAgentLink.getServiceProviderId(), terminalAgentLink.getServiceProviderAgentId(), 
							Integer.parseInt(terminalAgentLink.getMinorMins()), Integer.parseInt(terminalAgentLink.getMajorMins()), Integer.parseInt(terminalAgentLink.getCriticalMins()));
					
					Calendar shiftTime = Calendar.getInstance();
					shiftTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(shiftStartTime.substring(0, shiftStartTime.indexOf(":"))));
					shiftTime.set(Calendar.MINUTE, Integer.parseInt(shiftStartTime.substring(shiftStartTime.indexOf(":") + 1, shiftStartTime.length())));
					
					if(shiftTime.after(terminalStartTime) && shiftTime.before(terminalEndTime))
					{
						traceLog("ALTERNATE SHIFT NIGHT FLAG ENABLED");
						agentShiftBean.setShiftStartTime(terminalFormBean.getNightEndTime());
						agentShiftBean.setNightFlag(true);
					}
					else
					{
						traceLog("ALTERNATE SHIFT WITH CURRENT SHIFT TIME");
						agentShiftBean.setShiftStartTime(shiftStartTime);
						agentShiftBean.setNightFlag(false);
					}
					break;
				}
			}
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		return agentShiftBean;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to assign values to AgentShiftBean
	Written by				: Vijayarumugam K
	Last Modified			: 19 - Aug - 2015
	Arguments passed		: String serviceProviderId, String agentId, int minorMinutes, int majorMinutes, int criticalMinutes
	 ********************************************************************************************************************/
	
	private AgentShiftBean setValuesToAgentShiftBean(String serviceProviderId, String agentId, int minorMinutes, int majorMinutes, int criticalMinutes)
	{
		AgentShiftBean agentShiftBean = new AgentShiftBean();
		
		try
		{
			ServiceProviderInfo serviceProviderInfo = Configuration.serviceProviderInfo.get(serviceProviderId);
			agentShiftBean.setServiceProviderId(serviceProviderInfo.getServiceProviderId());
			agentShiftBean.setServiceProviderTypeId(serviceProviderInfo.getServiceProviderTypeId());
			
			agentShiftBean.setAgentId(agentId);
			agentShiftBean.setCriticalMin(criticalMinutes);
			agentShiftBean.setMajorMin(majorMinutes);
			agentShiftBean.setMinorMin(minorMinutes);
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		return agentShiftBean;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to Check the Current time with the Shift timings.
	Written by				: Vijayarumugam K
	Last Modified			: 12 - Aug - 2015
	Arguments passed		: int shiftStartTime, int shiftEndTime
	 ********************************************************************************************************************/
	private boolean checkCurrentShift(int shiftStartTime, int shiftEndTime)
	{
		boolean resultFlag = false;
		
		try
		{
			Calendar calendar = Calendar.getInstance();
			
			int hours = calendar.get(Calendar.HOUR);
			int minutes = calendar.get(Calendar.MINUTE);
			int am = calendar.get(Calendar.AM_PM);
			
			if(am == 1)
				hours = hours + 12;
			
			String currentTimeStr = null; 
				
			if(minutes < 10)
				currentTimeStr = hours + "0" + minutes;
			else
				currentTimeStr = hours + "" + minutes;
				
			int currentTime = Integer.parseInt(currentTimeStr);
			
			
			if(shiftStartTime < shiftEndTime)
			{
				if(shiftStartTime <= currentTime && currentTime <= shiftEndTime)
					resultFlag = true;
			}
			else
			{
				if((shiftStartTime < currentTime && currentTime > shiftEndTime) || 
					(shiftStartTime > currentTime && currentTime < shiftEndTime))
					resultFlag = true;
			}
			
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		
		traceLog("CURRENT SHIFT FLAG :: " + resultFlag);
		return resultFlag;
		
	}
	
	/**
	 * Method to get the XFS_FAULT_INFO
	 * @return
	 */
	public Hashtable<String, XFSFaultInfo> getXFSFaultInfo()
	{
		String query = Configuration.queryBundle.getString("get.xfs.fault.info");
		Hashtable<String, XFSFaultInfo> xfsFaultInfo = null;

		try
		{
			dbimp.executeQuery(query);
			xfsFaultInfo = assignToXFSFaultInfoForm(dbimp.resultSet);
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		finally
		{
			dbimp.closeResultSetAndStatment();
		}
		return xfsFaultInfo;
	}
	
	/**
	 * Method for assigning the values to form..
	 * @param resultSet1
	 * @return
	 */
	private Hashtable<String, XFSFaultInfo> assignToXFSFaultInfoForm(ResultSet resultSet1)
	{
		Hashtable<String, XFSFaultInfo> xfsFaultInfo = new Hashtable<String, XFSFaultInfo>();
		try
		{
			XFSFaultInfo form = null;
			while(resultSet1.next())
			{
				form = new XFSFaultInfo();
				form.setShortName(resultSet1.getString(1));
				form.setFaultId(resultSet1.getString(2));
				form.setHealthFaultId(resultSet1.getString(3));
				xfsFaultInfo.put(form.getShortName().toUpperCase().trim(), form);
				form = null;
			}
		}
		catch(Exception ex)
		{
			errorLog(ex);
		}
		return xfsFaultInfo;
	}
	// EJ file stored path  Configuration
	public FilexAdditionalBankForm getFilexAddBAnkInfo(){
		FilexAdditionalBankForm form = null;
		try {
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.FILEX.ADD.INFO"));
			dbimp.executeQuery(query.toString());
			if(dbimp.resultSet!=null){
				form = new FilexAdditionalBankForm();
				if(dbimp.resultSet.next()){
					//ejFilePath[0]=dbimp.resultSet.getString(1);//EJ file path
					//ejFilePath[1]=dbimp.resultSet.getString(2);//EJ Splitter destination path
					
					form.setEjPath(dbimp.resultSet.getString("AFB_FILE_PATH"));//EJ file path
					form.setEjSplitterPath(dbimp.resultSet.getString("AFB_EJSP_PATH"));//EJ Splitter destination path
					form.setXfsFlag(dbimp.resultSet.getString("AFB_XFS_FLAG"));//XFS Flag
				}
			}
		
		} catch (Exception e) {
			errorLog(e);
		}
		 finally {
				dbimp.closeResultSetAndStatment();
			}
		
		return form;
	}
	
}
