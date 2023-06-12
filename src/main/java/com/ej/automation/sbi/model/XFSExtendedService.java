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

public class XFSExtendedService extends BaseService
{
	public XFSExtendedService(Connection connection, String rrnNum) {
		super(connection,rrnNum);
	}
	
	public XFSExtendedService(String rrnNum) {
		super(rrnNum);
	}
	
	public Map<String, String> getTicketFaultMonitorByFaultId(SimpleDateFormat simpleDateFormat, String bankId, String terminalId)
	{
		HashMap<String, String> penaltyCount = null;
		
		/*String query = "SELECT AMF_FALT_ID, COUNT(1) FROM ADM_MNTC_FMIN_DETL WHERE ACT_TERM_ID='" + terminalId + "' " +
				"AND AMF_FALT_OCTM >= DATEADD(DAY,-2,GETDATE()) GROUP BY FAULT_ID";*/
		
		String query = Configuration.queryBundle.getString("get.faultMonitor.info.faultCount");
		
		/*String query = "SELECT AMF_FALT_ID, COUNT(1) FROM ADM_MNTC_FMIN_DETL WHERE ACB_BANK_ID = ? AND ACT_TERM_ID = ? " +
				"AND AMF_FALT_OCTM BETWEEN ? AND ? GROUP BY AMF_FALT_ID";*/
		
		try
		{
			dbimp.resultSet = null;
			List<Object> objectList = new ArrayList<Object>();
			Calendar calendar =Calendar.getInstance();
			calendar.add(Calendar.DATE, -2);
			
			objectList.add(bankId);
			objectList.add(terminalId);
			objectList.add(Timestamp.valueOf(simpleDateFormat.format(calendar.getTime())));
			objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
			
			dbimp.executeQueryUsingPreparedStatment(query, objectList);
			penaltyCount = assignToHashMapForFaultMonitor(dbimp.resultSet);
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		finally {
			dbimp.closeResultSetAndStatment();
		}
		return penaltyCount;
	}
	
	private HashMap<String, String> assignToHashMapForFaultMonitor(ResultSet resultSet1)
	{
		HashMap<String, String> penaltyCount = null;
		
		try
		{
			if(resultSet1 != null && resultSet1.next())
			{
				penaltyCount = new HashMap<String, String>();
				do
				{
					penaltyCount.put(resultSet1.getString(1), resultSet1.getString(2));
				}while(resultSet1.next());
					
			}
		}
		
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		
		return penaltyCount;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the List of all open faults from the fault monitor info
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId, String terminalId
	 ********************************************************************************************************************/
	public HashMap<String, String> getFaultMonitorInfo(String bankId, String terminalId)
	{
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.faultMonitor.info.openFault"));
		query = query.append(" AND ACT_TERM_ID='" + terminalId + "' AND ACB_BANK_ID='" + bankId + "'");
		
		/*String query = "SELECT  AMF_FALT_ID FROM ADM_MNTC_FMIN_DETL "
				+ " WHERE ACT_TERM_ID='" + terminalId + "' AND ACB_BANK_ID='"
				+ bankId + "'" + " AND AMF_FALT_STAT = 'O'";*/

		HashMap<String, String> faultMonitorInfo = null;

		try {
			dbimp.executeQuery(query.toString());
			faultMonitorInfo = assignToFaultMonitorInfoForm(dbimp.resultSet);
		} catch (Exception ex) {
			FileXLogger.logXFSError(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}
		return faultMonitorInfo;
	}
	
	/***********************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to String(Fault Monitor Info) 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 **********************************************************************************************/
	private HashMap<String, String> assignToFaultMonitorInfoForm(ResultSet resultSet1)
	{
		HashMap<String, String> faultMonitorForm = null;

		if (resultSet1 != null) {
			faultMonitorForm = new HashMap<String, String>();
			try {
				String form = null;
				while (resultSet1.next()) {
					form = resultSet1.getString(1);
					faultMonitorForm.put(form, form);
					form = null;
				}
			} catch (Exception ex) {
				FileXLogger.logXFSError(ex);
			}
		}

		return faultMonitorForm;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the List of all open tickets
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId, String terminalId
	 ********************************************************************************************************************/
	public List<String> getTicketEscalationInfo(String bankId, String terminalId)
	{
		List<String> ticketEscalationInfoList = null;

		try {
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.ticketEscalation.openTicket"));
			query = query.append(" AND ACB_BANK_ID = '" + bankId + "' AND ACT_TERM_ID = '" + terminalId + "'");
			
			/*String query = "SELECT DISTINCT(AMF_FALT_ID) FROM ADM_MNTC_TEIN_DETL WHERE "
					+ "ACB_BANK_ID = '" + bankId + "' AND ACT_TERM_ID = '"
					+ terminalId + "' " + "AND AMT_TCKT_STAT = 'O'";*/
			dbimp.executeQuery(query.toString());
			ticketEscalationInfoList = assignTicketEscalationToList(dbimp.resultSet);
		} catch (Exception ex) {
			FileXLogger.logXFSError(ex);
		} finally {
			dbimp.closeResultSetAndStatment();
		}

		return ticketEscalationInfoList;
	}
	
	/***********************************************************************************************
	 * Purpose in brief : Method to assign the values from the resultset to String(Tickets) 
	 * Written by : Vijayarumugam K 
	 * Last Modified : 16 - Jan - 2015 
	 * Arguments passed : ResultSet resultSet1
	 **********************************************************************************************/
	private List<String> assignTicketEscalationToList(ResultSet resultSet1)
	{
		List<String> ticketEscalationInfoList = null;

		if (resultSet1 != null) {
			ticketEscalationInfoList = new ArrayList<String>();
			try {
				while (resultSet1.next()) {
					ticketEscalationInfoList.add(resultSet1.getString(1));
				}
			} catch (Exception ex) {
				FileXLogger.logXFSError(ex);
			}
		}
		return ticketEscalationInfoList;
	}
	
	
	/***********************************************************************************************
	 * Purpose in brief : Method to update the ticket remainder
	 * Written by : Vijayarumugam K 
	 * Last Modified : 04 - May - 2015 
	 * Arguments passed : String bankId, String terminalId, StringBuffer queryBuffer
	 **********************************************************************************************/
	private void updateTicketReminderByTerminalAndFault(String bankId, String terminalId, StringBuffer queryBuffer)
	{
		StringBuffer query = null;
		List<Object> objectList = new ArrayList<Object>();
		
		try
		{
			query = new StringBuffer(Configuration.queryBundle.getString("update.faultReminder.info"));
			query = query.append(" AND (" + queryBuffer + ")");
			
			objectList.add("0");
			objectList.add(bankId);
			objectList.add(terminalId);
			objectList.add("1");
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);	
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the Check and insert/update the terminal down time information
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId, String terminalId
	 ********************************************************************************************************************/
	public void updateTermianlDownTimeBasedOnTerminalStatus(SimpleDateFormat simpleDateFormat, String terminalId, String terminalName, String terminalStatus, String dateId)
	{
		try
		{
			if(terminalStatus.equalsIgnoreCase("O"))
				checkAndInsertTerminalDownTimeInfo(simpleDateFormat, terminalId, terminalName, "ATM OUT OF SERVICE", "ATM_OUT", dateId);
			if(terminalStatus.equalsIgnoreCase("S"))
				checkAndInsertTerminalDownTimeInfo(simpleDateFormat, terminalId, terminalName, "SUPERVISOR MODE ENTERED", "ATM_SERVICE", dateId);
			if(terminalStatus.equalsIgnoreCase("D"))
				checkAndInsertTerminalDownTimeInfo(simpleDateFormat, terminalId, terminalName, "AGENT DISCONNECTED", "ATM_DOWN", dateId);
			else
				updateTerminalDownTimeInfo(terminalId, terminalStatus);
		}
		catch(Exception e)
		{
			FileXLogger.logXFSError(e);
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to update the Terminal down time information
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String terminalId, String terminalStatus
	 ********************************************************************************************************************/
	public void updateTerminalDownTimeInfo(String terminalId, String terminalStatus)
	{
		String reason = null;

		if (terminalStatus.equalsIgnoreCase("I"))
			reason = "'ATM OUT OF SERVICE', 'SUPERVISOR MODE ENTERED'";
		if (terminalStatus.equalsIgnoreCase("C"))
			reason = "'AGENT DISCONNECTED'";
		if (terminalStatus.equalsIgnoreCase("O"))
			reason = "'SUPERVISOR MODE ENTERED'";

		if (reason != null) {
			/*StringBuffer updateQuery = new StringBuffer(
					"UPDATE ADM_MNTC_TRDT_DETL WITH (UPDLOCK) SET AMT_TERM_DNET=GETDATE(), "
							+ "AMT_TERM_DNMN=DATEDIFF(ss,AMT_TERM_DNST,GETDATE()) WHERE ACT_TERM_ID='"
							+ terminalId + "' " + " AND REASON IN ( " + reason
							+ " ) AND AMT_TERM_DNET IS NULL");*/
			
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuffer updateQuery = new StringBuffer(Configuration.queryBundle.getString("update.terminal.downtime.info"));
			updateQuery = updateQuery.append(" WHERE ACT_TERM_ID='" + terminalId + "' " + " AND AMT_TERM_RESN IN ( " + reason + " ) AND AMT_TERM_DNET IS NULL");
			
			/*StringBuffer updateQuery = new StringBuffer(
					"UPDATE ADM_MNTC_TRDT_DETL SET AMT_TERM_DNET = SYSDATE, "
							+ "AMT_TERM_DNMN = (TO_DATE(TO_CHAR(SYSDATE, 'DD-MM-YYYY hh24:mi:ss'),  " +
									"'DD-MM-YYYY hh24:mi:ss') - TO_DATE(TO_CHAR(AMT_TERM_DNST, 'DD-MM-YYYY hh24:mi:ss'),  " +
									"'DD-MM-YYYY hh24:mi:ss')) * (24*60*60)  WHERE ACT_TERM_ID='"
							+ terminalId + "' " + " AND AMT_TERM_RESN IN ( " + reason
							+ " ) AND AMT_TERM_DNET IS NULL");*/
			try {
				//dbimp.updateSQL(updateQuery.toString());
				dbimp.insertUsingPreparedStatement(updateQuery.toString(), objectList);
			} catch (Exception ex) {
				FileXLogger.logXFSError(ex);
			}
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to close the tickets and faults for the terminal
	Written by				: Vijayarumugam K
	Last Modified			: 16 - Jan - 2015
	Arguments passed		: String bankId, String terminalId, Map<String, String> closeTicketMap
	 ********************************************************************************************************************/
	public boolean closeTicket(SimpleDateFormat simpleDateFormat, String bankId, String terminalId, Map<String, String> closeTicketMap, List<String> closeFaultMonitor)
	{
		boolean counter = false;
		StringBuffer queryBuffer = null;
		
		queryBuffer = new StringBuffer("AMF_FALT_ID IN (");

		for (String closeTicket : closeTicketMap.keySet()) {
			if (counter)
				queryBuffer.append(",");

			queryBuffer.append("'" + closeTicketMap.get(closeTicket) + "'");
			counter = true;

		}
		queryBuffer.append(")");
		
		if (counter) {
			try {

				if (!queryBuffer.toString().equals("AMF_FALT_ID IN()")) 
				{
					StringBuffer query = null;
					List<Object> objectList = new ArrayList<Object>();
					objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
					
					query = new StringBuffer(Configuration.queryBundle.getString("update.ticketEscalation.info.1"));
					query = query.append(" WHERE ACT_TERM_ID='" + terminalId + "' AND AMT_TCKT_STAT='O' AND AMT_TCKT_CLTM IS NULL AND (" + queryBuffer + ")");
					
					dbimp.insertUsingPreparedStatement(query.toString(), objectList);
					
					objectList = null;
					query = null;
					objectList = new ArrayList<Object>();
					
					query = new StringBuffer(Configuration.queryBundle.getString("update.ticketEscalation.info.1.2"));
					query = query.append(" AND ACT_TERM_ID = '" + terminalId + "' AND AMT_TCKT_STAT='O' AND " + queryBuffer);
					
					dbimp.insertUsingPreparedStatement(query.toString(), objectList);
					
					objectList = null;
					query = null;
					objectList = new ArrayList<Object>();
					
					query = new StringBuffer(Configuration.queryBundle.getString("update.ticketEscalation.info.2"));
					query = query.append(" WHERE " + "ACT_TERM_ID = '" + terminalId + "' AND AMT_TCKT_STAT='O' AND " + queryBuffer + " AND AMT_TCKT_INDR IS NULL");
					
					dbimp.insertUsingPreparedStatement(query.toString(), objectList);

					objectList = null;
					query = null;
					objectList = new ArrayList<Object>();
					objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
					
					query = new StringBuffer(Configuration.queryBundle.getString("update.ticketEscalation.info.3"));
					query = query.append(" WHERE ACT_TERM_ID='" + terminalId + "' AND AMT_TCKT_STAT='O' AND (" + queryBuffer + ")");
					
					dbimp.insertUsingPreparedStatement(query.toString(), objectList);
					
					updateTicketReminderByTerminalAndFault(bankId, terminalId, queryBuffer);
					
					queryBuffer = null;
					
					closeFaults(simpleDateFormat, terminalId, closeFaultMonitor);
				}
				return true;
			} catch (Exception ex) {
				FileXLogger.logXFSError(ex);
				return false;
			}
		} 
		else
		{
			closeFaults(simpleDateFormat, terminalId, closeFaultMonitor);
			return true;
		}
	}
	
	private void closeFaults(SimpleDateFormat simpleDateFormat, String terminalId, List<String> closeFaultMonitor)
	{
		
		try 
		{
			StringBuffer queryBuffer = new StringBuffer("AMF_FALT_ID IN (");
			boolean counter = false;
			for(String faultToClose : closeFaultMonitor)
			{
				if (counter)
					queryBuffer.append(",");

				queryBuffer.append("'" + faultToClose + "'");
				counter = true;
			}
			
			queryBuffer.append(")");
			
			if (!queryBuffer.toString().equals("AMF_FALT_ID IN ()")) 
			{
				List<Object> objectList = new ArrayList<Object>();
				objectList.add(Timestamp.valueOf(simpleDateFormat.format(new Date())));
				
				StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("update.faultMonitor.info"));
				query = query.append(" WHERE ACT_TERM_ID='" + terminalId + "' AND AMF_FALT_STAT='O' AND (" + queryBuffer + ")");
				
				dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			}
		}  
		catch (Exception e) 
		{
			FileXLogger.logXFSError(e);
		}
	}
	
	public List<Integer> getCashInfoByDenominatonNotZero(String bankId, String terminalId)
	{
		List<Integer> cashInfoList = null;
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.cash.info.byDenomination"));
		query = query.append(" AND ACB_BANK_ID='" + bankId + "' AND ACT_TERM_ID='" + terminalId + "'");
		
		/*String query = "SELECT AMC_TERM_HPPS FROM ADM_MNTC_CSIN_DETL WHERE ACB_BANK_ID='"
				+ bankId + "' AND ACT_TERM_ID='" + terminalId + "' "
				+ "AND AMC_TERM_DENM != 0";*/
		try {
			dbimp.resultSet = null;
			dbimp.executeQuery(query.toString());
			if (dbimp.resultSet != null) {
				cashInfoList = new ArrayList<Integer>();
				while (dbimp.resultSet.next()) {
					cashInfoList.add(dbimp.resultSet.getInt(1));
				}
			}
		} catch (Exception ex) {
			FileXLogger.logXFSError(ex);
		}
		return cashInfoList;
	}
	
	
	public int updateSQL(String sql)
	{
		try 
		{
			return dbimp.updateSQL( sql);
		}
		catch (Exception ex) 
		{
			FileXLogger.logXFSError(ex);
			return -1;
		}
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to add the XFS message details
	Written by				: Sivakaran B
	Last Modified			: 06 - Nov - 2015
	Arguments passed		: ReceiverForm
	********************************************************************************************************************/
	public int addXfsMessage(ReceiverForm receiverForm)
	{
		int result =0 ;
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("ADD.XFS.MSG"));
			
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(receiverForm.getSequenceNumber());
			objectList.add(receiverForm.getTerminalId());
			objectList.add(receiverForm.getXfsMessage().toString());
			objectList.add(Timestamp.valueOf(receiverForm.getEventOccurredTime()));
			objectList.add(receiverForm.getTerminalNode());
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return result;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the XFS message details
	Written by				: Sivakaran B
	Last Modified			: 06 - Nov - 2015	
	Return					: List of ReceiverForm
	 ********************************************************************************************************************/
	public List<ReceiverForm> getXfsMessage(String terminalId)
	{
		List<ReceiverForm> receiverFormList = null;
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.XFS.MSGS"));
			
			if(terminalId != null && !terminalId.equals(""))
				query.append(" WHERE ACT_TERM_ID = '"+terminalId+"'");
			
			query.append(" ORDER BY AMX_OCUR_TIME");
			
			 
			dbimp.preStatement = dbimp.connection.prepareStatement(query.toString());
			dbimp.resultSet = null;
			dbimp.executeQuery(query.toString());
			ReceiverForm receiverForm = null;
			if(dbimp.resultSet != null)
			{
				int count = 0;
				receiverFormList = new ArrayList<ReceiverForm>();
				while(dbimp.resultSet.next())
				{
					count++;
					receiverForm = new ReceiverForm();
					receiverForm.setSequenceNumber(dbimp.resultSet.getString(1));
					receiverForm.setTerminalId(dbimp.resultSet.getString(2));
					receiverForm.setXfsMessage(dbimp.resultSet.getString(3));
					receiverForm.setEventOccurredTime(dbimp.resultSet.getString(4));
					receiverForm.setMessageReceivedTime(dbimp.resultSet.getString(5));
					receiverForm.setTerminalNode(dbimp.resultSet.getString(6));
					
					receiverFormList.add(receiverForm);
					receiverForm = null;
				}
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return receiverFormList;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the XFS message details
	Written by				: Sivakaran B
	Last Modified			: 06 - Nov - 2015	
	Return					: List of ReceiverForm
	 ********************************************************************************************************************/
	public int deleteXfsMessage(List<ReceiverForm> receiverFormList)
	{
		int result = 0;
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("DELETE.XFS.MSGS"));
			dbimp.preStatement = dbimp.connection.prepareStatement(query.toString());
			
			if(receiverFormList != null && receiverFormList.size() > 0)
			{
				for(ReceiverForm receiverForm : receiverFormList)
				{
					dbimp.preStatement.setObject(1, receiverForm.getSequenceNumber());
					dbimp.preStatement.addBatch();
				}
				result = dbimp.updateBatch(dbimp.preStatement);
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return result;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get all Terminal Nodes 
	Written by				: Sivakaran B
	Last Modified			: 06 - Nov - 2015
	********************************************************************************************************************/
	public Hashtable<String, String> getTerminalNodeDetails()
	{
		Hashtable<String, String> terminalNodeDetails = null;
		try{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.TERM.NODE.DETAILS"));
			dbimp.resultSet = null;
			dbimp.executeQuery(query.toString());
			if(dbimp.resultSet != null)
			{
				terminalNodeDetails = new Hashtable<String, String>();
				while(dbimp.resultSet.next())
				{
					terminalNodeDetails.put(dbimp.resultSet.getString(1), dbimp.resultSet.getString(2));					
				}
			}
		}catch (Exception ex) {
			FileXLogger.logXFSError(ex);
		}
		finally{
			dbimp.closeResultSetAndStatment();
		}
		return terminalNodeDetails;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get all Terminal Nodes 
	Written by				: Sivakaran B
	Last Modified			: 06 - Nov - 2015
	********************************************************************************************************************/
	public Hashtable<String, FilexTerminalForm> getTerminalIpStationDetails()
	{
		Hashtable<String, FilexTerminalForm> terminalIpStationDetails = null;
		try{
			FilexTerminalForm filexTerminalForm = null;
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.TERM.IP.STATION.DETAILS"));
			dbimp.resultSet = null;
			dbimp.executeQuery(query.toString());
			if(dbimp.resultSet != null)
			{
				terminalIpStationDetails = new Hashtable<String, FilexTerminalForm>();
				while(dbimp.resultSet.next())
				{
					filexTerminalForm = new FilexTerminalForm();
					filexTerminalForm.setTerminalId(dbimp.resultSet.getString("ACT_TERM_ID"));
					filexTerminalForm.setTerminalName(dbimp.resultSet.getString("ACT_TERM_NAME"));
					filexTerminalForm.setTerminalIp(dbimp.resultSet.getString("ACT_TERM_IP"));
					filexTerminalForm.setStation(dbimp.resultSet.getString("AFT_STAT_NAME"));
					terminalIpStationDetails.put(filexTerminalForm.getTerminalName(), filexTerminalForm);
					filexTerminalForm = null;
				}
			}
		}catch (Exception ex) {
			FileXLogger.logXFSError(ex);
		}
		finally{
			dbimp.closeResultSetAndStatment();
		}
		return terminalIpStationDetails;
	}
	
	/********************************************************************************************************************
	Purpose in brief		: Method to get the terminal Id of old XFS message details
	Written by				: Sivakaran B
	Last Modified			: 09 - Nov - 2015	
	Return					: ReceiverForm
	 ********************************************************************************************************************/
	public String getOldMessageTerminalId()
	{
		String terminalId = null;
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.TERM.ID.BASED.ON.TIME"));
			dbimp.resultSet = null;
			dbimp.executeQuery(query.toString());
			if(dbimp.resultSet != null && dbimp.resultSet.next())
			{
				terminalId = dbimp.resultSet.getString(1);
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		finally{
			dbimp.closeResultSetAndStatment();
		}
		return terminalId;
	}
	
	public int deleteTerminalDeviceDetailInfo(String rrnNum, String terminalId)
	{
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("delete.terminal.device.details"));
			query = query.append(" WHERE ACT_TERM_ID = '"+ terminalId  + "'");
			/*StringBuffer query = new StringBuffer("DELETE FROM ADM_MNTC_TRDD_DETL WHERE ACT_TERM_ID = '"+ terminalId  + "'");*/
			
			dbimp.updateSQL(query.toString());
			return 0;	
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
			return -1;
		}
	}
	
	public int deleteTerminalDeviceDetailInfo(String rrnNum, String terminalId, String deviceName)
	{
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("delete.terminal.device.details"));
			query = query.append(" WHERE ACT_TERM_ID = '"+ terminalId  + "' AND AMT_DEVC_NAME ='" + deviceName + "'");
			/*StringBuffer query = new StringBuffer("DELETE FROM ADM_MNTC_TRDD_DETL WHERE ACT_TERM_ID = '"+ terminalId  + "'");*/
			
			dbimp.updateSQL(query.toString());
			return 0;	
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
			return -1;
		}
	}
	
	public int addDeviceDetailInfo(String rrnNum, TerminalDeviceDetailInfo termDevDetail)
	{
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("insert.device.details.info"));
			/*query = query.append("'" + RRN.genRRN() + "', '" + termDevDetail.getTerminalId() + "','" + termDevDetail.getDeviceName() + "','" + 
					termDevDetail.getDeviceStatus() + "','" + termDevDetail.getHealthStatus() + "','" + 
					termDevDetail.getFinalDeviceName() + "','" + termDevDetail.getBankId() + "')");*/
			
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(RRN.genRRN());
			objectList.add(termDevDetail.getTerminalId());
			objectList.add(termDevDetail.getDeviceName());
			objectList.add(termDevDetail.getDeviceStatus());
			objectList.add(termDevDetail.getHealthStatus());
			objectList.add(termDevDetail.getFinalDeviceName());
			objectList.add(termDevDetail.getBankId());
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			return 0;
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
			return -1;
		}
	}
	
	/*Cash Monitoring Methods - starts*/
	public void saveCashInfo(Map<String, CashInfoForm> cashInfo)
	{
		Set<String> cashInfoSet = cashInfo.keySet();
		CashInfoForm cashInfoForm = null;
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("insert.cash.info.xfs"));
		/*StringBuffer query = new StringBuffer("INSERT INTO ADM_MNTC_CSIN_DETL (AMC_TERM_CSSQ ,ACT_TERM_ID ,AMC_TERM_CRID, AMC_TERM_DENM ,AMC_TERM_BALE" +
				" ,AMC_TERM_HPPS ,AMC_TERM_RJBL ,ACB_BANK_ID, AMC_TERM_NONT, AMC_TERM_CSTP, ACT_TERM_NAME) VALUES ( ? ,? ,? ,? ,? ,? ,? ,?, ?, ?, ?)");*/
		try
		{
			FileXLogger.logXFSTrace(" SAVE CASH INFO :: " + query.toString());
			dbimp.preStatement = dbimp.connection.prepareStatement(query.toString());
			for(String position : cashInfoSet)
			{
				//log.info(" INSERT position :::  " + position);
				cashInfoForm = cashInfo.get(position);
				
				//log.info(" INSERT CASH INFO FORM SET :::: " + cashInfoForm.toString());
				/*dbimp.preStatement.setString(1, RRN.genRRN());
				dbimp.preStatement.setString(2, cashInfoForm.getTerminalId());
				dbimp.preStatement.setString(3, cashInfoForm.getCurrencyId());
				dbimp.preStatement.setString(4, cashInfoForm.getDenomination());
				dbimp.preStatement.setString(5, cashInfoForm.getBalance());
				dbimp.preStatement.setString(6, cashInfoForm.getHopperPosition());
				dbimp.preStatement.setString(7, cashInfoForm.getRejectedBalance());
				dbimp.preStatement.setString(8, cashInfoForm.getBankId());
				dbimp.preStatement.setString(9, cashInfoForm.getNoOfNotes());
				dbimp.preStatement.setString(10, cashInfoForm.getCassetteType());
				dbimp.preStatement.setString(11, cashInfoForm.getTerminalName());*/
				
				List<Object> objectList = new ArrayList<Object>();
				objectList.add(RRN.genRRN());
				objectList.add(cashInfoForm.getTerminalId());
				objectList.add(cashInfoForm.getCurrencyId());
				objectList.add(cashInfoForm.getDenomination());
				objectList.add(cashInfoForm.getBalance());
				objectList.add(cashInfoForm.getHopperPosition());
				objectList.add(cashInfoForm.getRejectedBalance());
				objectList.add(cashInfoForm.getBankId());
				objectList.add(cashInfoForm.getNoOfNotes());
				objectList.add(cashInfoForm.getCassetteType());
				objectList.add(cashInfoForm.getTerminalName());
				objectList.add(cashInfoForm.getCassetteName());
				
				dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
	}
	
	public void updateCashInfo(Map<String, CashInfoForm> cashInfo)
	{
		Set<String> cashInfoSet = cashInfo.keySet();
		CashInfoForm cashInfoForm = null;
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("update.cash.info.xfs"));
		
		/*StringBuffer query = new StringBuffer("UPDATE ADM_MNTC_CSIN_DETL SET AMC_TERM_CRID = ?, AMC_TERM_DENM = ?, AMC_TERM_BALE = ?, AMC_TERM_RJBL = ?, " +
				"AMC_TERM_NONT = ?, AMC_TERM_CSTP = ? WHERE ACB_BANK_ID = ? AND ACT_TERM_ID = ? AND AMC_TERM_HPPS = ?");*/
		
		try
		{
			//dbimp.preStatement = dbimp.connection.prepareStatement(query.toString());
			for(String position : cashInfoSet)
			{
				cashInfoForm = cashInfo.get(position);
				/*dbimp.preStatement.setString(1, cashInfoForm.getCurrencyId());
				dbimp.preStatement.setString(2, cashInfoForm.getDenomination());
				dbimp.preStatement.setString(3, cashInfoForm.getBalance());
				dbimp.preStatement.setString(4, cashInfoForm.getRejectedBalance());
				dbimp.preStatement.setString(5, cashInfoForm.getNoOfNotes());
				dbimp.preStatement.setString(6, cashInfoForm.getCassetteType());
				dbimp.preStatement.setString(7, cashInfoForm.getBankId());
				dbimp.preStatement.setString(8, cashInfoForm.getTerminalId());
				dbimp.preStatement.setString(9, cashInfoForm.getHopperPosition());*/
				
				List<Object> objectList = new ArrayList<Object>();
				objectList.add(cashInfoForm.getCurrencyId());
				objectList.add(cashInfoForm.getDenomination());
				objectList.add(cashInfoForm.getBalance());
				objectList.add(cashInfoForm.getRejectedBalance());
				objectList.add(cashInfoForm.getNoOfNotes());
				objectList.add(cashInfoForm.getCassetteType());
				objectList.add(cashInfoForm.getCassetteName());
				objectList.add(cashInfoForm.getBankId());
				objectList.add(cashInfoForm.getTerminalId());
				objectList.add(cashInfoForm.getHopperPosition());
				
				dbimp.insertUsingPreparedStatement(query.toString(), objectList);
				
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
	}
	
	public void deleteCashInfo(Map<String, CashInfoForm> cashInfo)
	{
		Set<String> cashInfoSet = cashInfo.keySet();
		CashInfoForm cashInfoForm = null;
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("delete.cash.info"));
		/*StringBuffer query = new StringBuffer("DELETE FROM ADM_MNTC_CSIN_DETL WHERE ACB_BANK_ID = ? AND ACT_TERM_ID = ? AND AMC_TERM_HPPS = ?");*/
		try
		{
			//dbimp.preStatement = dbimp.connection.prepareStatement(query.toString());
			for(String position : cashInfoSet)
			{
				cashInfoForm = cashInfo.get(position);
				/*dbimp.preStatement.setString(1, cashInfoForm.getBankId());
				dbimp.preStatement.setString(2, cashInfoForm.getTerminalId());
				dbimp.preStatement.setString(3, cashInfoForm.getHopperPosition());*/
				
				List<Object> objectList = new ArrayList<Object>();
				objectList.add(cashInfoForm.getBankId());
				objectList.add(cashInfoForm.getTerminalId());
				objectList.add(cashInfoForm.getHopperPosition());
				dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
	}
	
	public Map<String, String> getCashInformation(String bankId, String terminalId)
	{
		Map<String, String> cashPosition = null;
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("get.cash.info.xfs"));
		query = query.append(" WHERE ACB_BANK_ID='" + bankId + "' AND ACT_TERM_ID='" + terminalId +  "'");
		
		/*String query = "SELECT AMC_TERM_HPPS, AMC_TERM_NONT FROM ADM_MNTC_CSIN_DETL " +
				"WHERE ACB_BANK_ID='" + bankId + "' AND ACT_TERM_ID='" + terminalId +  "'";*/
		
		try
		{
			dbimp.resultSet = null;
			dbimp.executeQuery(query.toString());
			if(dbimp.resultSet != null)
			{
				cashPosition = new HashMap<String, String>();
				while(dbimp.resultSet.next())
					cashPosition.put(dbimp.resultSet.getString(1), dbimp.resultSet.getString(2));
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return cashPosition;
	}
	/*Cash Monitoring Methods - ends*/
	
	
	/*SNMP methods starts*/
	
	/**
	 * @purpose This method is used to get all the devicemodels that are linked with the terminals
	 * @author Sivakaran.B
	 * @date 07-03-2016
	 * @ModifiedBy : @ModifiedDate    
	 * @param 
	 * @return Map<String, String> (Key - terminalid-devicemodelid, Value - deviceIp)
	 * @throws 
	 */
	public Map<String, String> getTerminalIdAndDeviceIP(String deviceCategory)
	{
		Map<String, String> terminalIdAndDeviceIP = null;
		try
		{
			String query = Configuration.queryBundle.getString("GET.TERM.ID.DEV.IP");
			
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(deviceCategory);
			
			dbimp.resultSet = null;
			dbimp.executeQueryUsingPreparedStatment(query, objectList);
			
			if(dbimp.resultSet != null)
			{
				terminalIdAndDeviceIP = new HashMap<String, String>();
				while(dbimp.resultSet.next())
				{
					terminalIdAndDeviceIP.put(dbimp.resultSet.getString("ACT_TERM_ID")+"-"+dbimp.resultSet.getString("AMD_DVMO_ID"), dbimp.resultSet.getString("AMT_DEVC_IP"));
				}
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return terminalIdAndDeviceIP;
	}
	
	/**
	 * @purpose This method is used to get all the devicemodels that are linked with the terminals
	 * @author Sivakaran.B
	 * @date 07-03-2016
	 * @ModifiedBy : @ModifiedDate    
	 * @param 
	 * @return Map<String, String> (Key - terminalid-devicemodelid, Value - deviceIp)
	 * @throws 
	 */
	public List<String> getAllBaseOID(String deviceCategory, String oidStatus)
	{
		List<String> baseIODs = null;
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.ALL.BASE.OID"));
			
			if(oidStatus != null && !oidStatus.equalsIgnoreCase("ALL"))
			{
				query.append(" AND AMD_OID_STAT IN ('V','R')");
			}
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(deviceCategory);
			
			dbimp.resultSet = null;
			dbimp.executeQueryUsingPreparedStatment(query.toString(), objectList);
			
			if(dbimp.resultSet != null)
			{
				baseIODs = new ArrayList<String>();
				while(dbimp.resultSet.next())
				{
					baseIODs.add(dbimp.resultSet.getString("AMD_OID_BASE"));
				}
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return baseIODs;
	}
	
	public boolean checkOidAvailability(String oid, String terminalId)
	{
		boolean availabilityFlag =false;
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.OID.STATUS"));
			
			dbimp.resultSet = null;
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(oid);
			objectList.add(terminalId);
			
			dbimp.executeQueryUsingPreparedStatment(query.toString(), objectList);
			
			if(dbimp.resultSet != null)
			{
				while(dbimp.resultSet.next())
				{
					availabilityFlag = true;
					break;
				}
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return availabilityFlag;
	}
	
	public int addOidStatus(UPSMonitoringBean upsMonitoringBean)
	{
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("ADD.OID.STATUS"));
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(upsMonitoringBean.getSeqNo());
			objectList.add(upsMonitoringBean.getTermDevcSeqNo());
			objectList.add(upsMonitoringBean.getSubOID());
			objectList.add(upsMonitoringBean.getOidDesc());
			objectList.add(upsMonitoringBean.getOidStatus());
			objectList.add(upsMonitoringBean.getTerminalId());
			objectList.add(upsMonitoringBean.getDeviceCatId());
			objectList.add(upsMonitoringBean.getDeviceId());
			objectList.add(upsMonitoringBean.getDeviceModelId());
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			return 0;
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
			return -1;
		}
	}
	
	public int updateOidStatus(String oid, String status, String terminalId)
	{
		try
		{
			StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("UPDATE.OID.STATUS"));
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(status);
			objectList.add(oid);
			objectList.add(terminalId);
			
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
			return 0;
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
			return -1;
		}
	}
	
	public int updateUserDefinedValues(String userDefinedField, String value, String deviceIp)
	{
		try
		{
			StringBuffer query = new StringBuffer("update ADM_MNTC_TRDM_MAST SET ");
			
			if(userDefinedField != null && userDefinedField.equals("1"))
				query.append("AMU_USDF_VAL1 = '"+value+"%'");
			else if(userDefinedField != null && userDefinedField.equals("2"))
				query.append("AMU_USDF_VAL2 = '"+value+"'");
			else if(userDefinedField != null && userDefinedField.equals("3"))
				query.append("AMU_USDF_VAL3 = '"+value+"'");
			else if(userDefinedField != null && userDefinedField.equals("4"))
				query.append("AMU_USDF_VAL4 = '"+value+"'");
			else if(userDefinedField != null && userDefinedField.equals("5"))
				query.append("AMU_USDF_VAL5 = '"+value+"'");
			else 
				query = null;
			
			if(query != null)
			{
				query.append(" WHERE AMT_DEVC_IP = '"+deviceIp+"'");
				dbimp.updateSQL(query.toString());
			}
			return 0;
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
			return -1;
		}
	}
	
	public Hashtable<String, DeviceOIDLinkBean> getDeviceOIDLinkDetails()
	{
		Hashtable<String, DeviceOIDLinkBean> deviceOIDLinkdetails = null;
		try
		{
			DeviceOIDLinkBean deviceOIDLinkBean =null;
			String query = Configuration.queryBundle.getString("GET.DEVICE.OID.LINK.DETAILS");
			
			dbimp.resultSet = null;
			dbimp.executeQuery(query);
			
			if(dbimp.resultSet != null)
			{
				deviceOIDLinkdetails = new Hashtable<String, DeviceOIDLinkBean>();
				while(dbimp.resultSet.next())
				{
					deviceOIDLinkBean = new DeviceOIDLinkBean();
					deviceOIDLinkBean.setSeqNo(dbimp.resultSet.getString("AMD_SEQ_NO"));
					deviceOIDLinkBean.setDeviceCatId(dbimp.resultSet.getString("ACD_DECA_ID"));
					deviceOIDLinkBean.setDeviceId(dbimp.resultSet.getString("AMS_DEVC_ID"));
					deviceOIDLinkBean.setDeviceModelId(dbimp.resultSet.getString("AMD_DVMO_ID"));
					deviceOIDLinkBean.setBaseOid(dbimp.resultSet.getString("AMD_OID_BASE"));
					deviceOIDLinkBean.setSubOid(dbimp.resultSet.getString("AMD_OID_SUB"));
					deviceOIDLinkBean.setOidDesc(dbimp.resultSet.getString("AMD_OID_DESC"));
					deviceOIDLinkBean.setOidStat(dbimp.resultSet.getString("AMD_OID_STAT"));
					deviceOIDLinkBean.setUserDefinedField(dbimp.resultSet.getString("AMD_USDF_VAL"));
					deviceOIDLinkdetails.put(deviceOIDLinkBean.getDeviceModelId()+"-"+deviceOIDLinkBean.getSubOid(), deviceOIDLinkBean);
					deviceOIDLinkBean = null;
				}
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return deviceOIDLinkdetails;
	}
	
	public Hashtable<String, TerminalDeviceModelLinkBean> getTerminalDeviceModelLinkDetails()
	{
		Hashtable<String, TerminalDeviceModelLinkBean> terminaleDeviceLinkdetails = null;
		try
		{
			TerminalDeviceModelLinkBean terminalDeviceModelLinkBean =null;
			String query = Configuration.queryBundle.getString("GET.TERM.DEVICE.MODEL.LINK.DETAILS");
			
			dbimp.resultSet = null;
			dbimp.executeQuery(query);
			
			if(dbimp.resultSet != null)
			{
				terminaleDeviceLinkdetails = new Hashtable<String, TerminalDeviceModelLinkBean>();
				while(dbimp.resultSet.next())
				{
					terminalDeviceModelLinkBean = new TerminalDeviceModelLinkBean();
					
					terminalDeviceModelLinkBean.setSeqNo(dbimp.resultSet.getString("AMT_SEQ_NO"));
					terminalDeviceModelLinkBean.setDeviceCatId(dbimp.resultSet.getString("ACD_DECA_ID"));
					terminalDeviceModelLinkBean.setDeviceId(dbimp.resultSet.getString("AMS_DEVC_ID"));
					terminalDeviceModelLinkBean.setDeviceModelId(dbimp.resultSet.getString("AMD_DVMO_ID"));
					terminalDeviceModelLinkBean.setTerminalId(dbimp.resultSet.getString("ACT_TERM_ID"));
					terminalDeviceModelLinkBean.setDeviceIp(dbimp.resultSet.getString("AMT_DEVC_IP"));
					terminalDeviceModelLinkBean.setUserdefinedVal1(dbimp.resultSet.getString("AMU_USDF_VAL1"));
					terminalDeviceModelLinkBean.setUserdefinedVal2(dbimp.resultSet.getString("AMU_USDF_VAL2"));
					terminalDeviceModelLinkBean.setUserdefinedVal3(dbimp.resultSet.getString("AMU_USDF_VAL3"));
					terminalDeviceModelLinkBean.setUserdefinedVal4(dbimp.resultSet.getString("AMU_USDF_VAL4"));
					terminalDeviceModelLinkBean.setUserdefinedVal5(dbimp.resultSet.getString("AMU_USDF_VAL5"));
					terminalDeviceModelLinkBean.setBankId(dbimp.resultSet.getString("ACB_BANK_ID"));
					
					terminaleDeviceLinkdetails.put(terminalDeviceModelLinkBean.getTerminalId()+"-"+terminalDeviceModelLinkBean.getDeviceIp(), terminalDeviceModelLinkBean);
					terminalDeviceModelLinkBean = null;
				}
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return terminaleDeviceLinkdetails;
	}
	
	public String getFaultIdOfDeviceModelStatus(String seqNo, String value)
	{
		String faultId = null;
		try
		{
			String query = Configuration.queryBundle.getString("GET.FALT.ID.OF.DEVC.MODEL.STATUS");
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(seqNo);
			objectList.add(value);
			
			dbimp.resultSet = null;
			dbimp.executeQueryUsingPreparedStatment(query, objectList);
			
			if(dbimp.resultSet != null && dbimp.resultSet.next())
			{
				faultId = dbimp.resultSet.getString("AMF_FALT_ID");
			}
		}
		catch (Exception e) 
		{
			FileXLogger.logXFSError(e);
		}
		return faultId;
	}
	/*SNMP methods end*/

	public Map<String, String> getCashAcceptorInformation(String bankId,String terminalId) {
		Map<String, String> cashPosition = null;
		
		StringBuffer deleteUniversalCasette = new StringBuffer(Configuration.queryBundle.getString("DELETE.UNIVERSAL.CASETTE.INFO.XFS"));
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.CASH.ACCEPTOR.INFO.XFS"));
		
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(terminalId);
			objectList.add("Universal Casette");
			dbimp.preStatement = dbimp.connection.prepareStatement(deleteUniversalCasette.toString());
			dbimp.insertUsingPreparedStatement(deleteUniversalCasette.toString(), objectList);
			dbimp.resultSet = null;
			objectList = new ArrayList<Object>();
			objectList.add(bankId);
			objectList.add(terminalId);
			dbimp.executeQueryUsingPreparedStatment(query.toString(), objectList);
			if(dbimp.resultSet != null)
			{
				cashPosition = new HashMap<String, String>();
				while(dbimp.resultSet.next())
					cashPosition.put(dbimp.resultSet.getString(1), dbimp.resultSet.getString(2));
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return cashPosition;
	}

	public String getDenominationForCasette(StringBuffer casetteName,String terminalId) {
		String denomination = null;
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("GET.DENOMINATION.FOR.CASETTE"));
		
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(casetteName.toString());
			objectList.add(terminalId);
			dbimp.resultSet = null;
			dbimp.executeQueryUsingPreparedStatment(query.toString(), objectList);
			if(dbimp.resultSet != null)
			{
				while(dbimp.resultSet.next())
					denomination = dbimp.resultSet.getString(1);
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
		return denomination;
	}

	public void saveCashAcceptorInfo(Map<String, CashInfoForm> cashInfoMap) {
		Set<String> cashInfoSet = cashInfoMap.keySet();
		CashInfoForm cashInfoForm = null;
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("INSERT.CASH.ACCEPTOR.INFO.XFS"));
		List<CashInfoForm> cashInfoForms = new ArrayList<CashInfoForm>();
		try
		{
			FileXLogger.logXFSTrace(" SAVE CASH INFO :: " + query.toString());
			for(String position : cashInfoSet)
			{
				cashInfoForm = cashInfoMap.get(position);
				cashInfoForms.add(cashInfoForm);
				List<Object> objectList = new ArrayList<Object>();
				objectList.add(RRN.genRRN());
				objectList.add(cashInfoForm.getTerminalId());
				objectList.add(cashInfoForm.getCurrencyId());
				objectList.add(cashInfoForm.getDenomination());
				objectList.add(cashInfoForm.getBalance());
				objectList.add(cashInfoForm.getHopperPosition());
				objectList.add(cashInfoForm.getRejectedBalance());
				objectList.add(cashInfoForm.getBankId());
				objectList.add(cashInfoForm.getNoOfNotes());
				objectList.add(cashInfoForm.getCassetteType());
				objectList.add(cashInfoForm.getTerminalName());
				objectList.add(cashInfoForm.getCassetteName());
				
				dbimp.insertUsingPreparedStatement(query.toString(), objectList);
				
			}
			/*dbimp.assignValuesAndInsertUsingPreparedStatement(query.toString(), cashInfoForms);*/
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
	
	}
	/*@Override
	public void iterateObject(PreparedStatement preparedStatement,Object object)
	{
		try
		{
			CashInfoForm cashInfoForm =(CashInfoForm)object;
			
			preparedStatement.setString(1,RRN.genRRN());
			preparedStatement.setString(2,replaceNullWithQuotes(cashInfoForm.getTerminalId()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getCurrencyId()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getDenomination()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getBalance()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getHopperPosition()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getRejectedBalance()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getBankId()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getNoOfNotes()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getTerminalName()));
			preparedStatement.setString(3,replaceNullWithQuotes(cashInfoForm.getCassetteName()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
    }
	private static String replaceNullWithQuotes(String aString)
	{
		return aString == null ? "" : aString;
	}*/
	public void updateCashAcceptorInfo(
			Map<String, CashInfoForm> updateCashInfoMap) {

		Set<String> cashInfoSet = updateCashInfoMap.keySet();
		CashInfoForm cashInfoForm = null;
		
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("UPDATE.CASH.ACCEPTOR.INFO.XFS"));
		
		/*StringBuffer query = new StringBuffer("UPDATE ADM_MNTC_CSIN_DETL SET AMC_TERM_CRID = ?, AMC_TERM_DENM = ?, AMC_TERM_BALE = ?, AMC_TERM_RJBL = ?, " +
				"AMC_TERM_NONT = ?, AMC_TERM_CSTP = ? WHERE ACB_BANK_ID = ? AND ACT_TERM_ID = ? AND AMC_TERM_HPPS = ?");*/
		
		try
		{
			//dbimp.preStatement = dbimp.connection.prepareStatement(query.toString());
			for(String position : cashInfoSet)
			{
				cashInfoForm = updateCashInfoMap.get(position);
				/*dbimp.preStatement.setString(1, cashInfoForm.getCurrencyId());
				dbimp.preStatement.setString(2, cashInfoForm.getDenomination());
				dbimp.preStatement.setString(3, cashInfoForm.getBalance());
				dbimp.preStatement.setString(4, cashInfoForm.getRejectedBalance());
				dbimp.preStatement.setString(5, cashInfoForm.getNoOfNotes());
				dbimp.preStatement.setString(6, cashInfoForm.getCassetteType());
				dbimp.preStatement.setString(7, cashInfoForm.getBankId());
				dbimp.preStatement.setString(8, cashInfoForm.getTerminalId());
				dbimp.preStatement.setString(9, cashInfoForm.getHopperPosition());*/
				
				List<Object> objectList = new ArrayList<Object>();
				objectList.add(cashInfoForm.getCurrencyId());
				objectList.add(cashInfoForm.getDenomination());
				objectList.add(cashInfoForm.getBalance());
				objectList.add(cashInfoForm.getRejectedBalance());
				objectList.add(cashInfoForm.getNoOfNotes());
				objectList.add(cashInfoForm.getCassetteType());
				objectList.add(cashInfoForm.getCassetteName());
				objectList.add(cashInfoForm.getBankId());
				objectList.add(cashInfoForm.getTerminalId());
				objectList.add(cashInfoForm.getHopperPosition());
				
				dbimp.insertUsingPreparedStatement(query.toString(), objectList);
				
			}
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
	
	}

	public void deleteCashAcceptorInfo(String bankId,String terminalId) {
		StringBuffer query = new StringBuffer(Configuration.queryBundle.getString("DELETE.CASH.ACCEPTOR.INFO"));
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(bankId);
			objectList.add(terminalId);
			dbimp.insertUsingPreparedStatement(query.toString(), objectList);
		}
		catch(Exception ex)
		{
			FileXLogger.logXFSError(ex);
		}
	
	}
}
