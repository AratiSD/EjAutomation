package com.ej.automation.sbi.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBImpl {

	public Connection connection;
	public Statement statement;
	public CallableStatement cstmt = null;
	public PreparedStatement preStatement;
	public ResultSet resultSet;

	private Driver driver;
    
	
	String rrnNum = null;

	public DBImpl(Connection connection, String rrnNum) {
		this.connection = connection;
		this.rrnNum = rrnNum; 
	}
	
//	public DBImpl(String rrnNum) {
//		this.connection = getConnection();
//		this.rrnNum = rrnNum; 
//	}
	
	public void queryLog(String message)
	{
		if(Configuration.querylog)
		FileXLogger.logQueryTrace(rrnNum + " :: " + message);
	}

	/**
	 * Method to execute the query
	 * 
	 * @param query
	 */
	public ResultSet executeQuery(String query) {
		try {
			try {
				if (resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
			} catch (Exception e) {
				FileXLogger.logQueryError(e);
			}

			
			if (this.connection == null) {
				FileXLogger.logQueryTrace("connection null DBIMP--->>> " + query);
			}

			statement = this.connection.createStatement();
			long startdate = System.currentTimeMillis();
			resultSet = statement.executeQuery(query);
			long enddate = System.currentTimeMillis();
			if (Configuration.querylog) 
			{
				FileXLogger.logQueryTrace(query + " -> " + (enddate - startdate));
			}
		} catch (Exception e) {
			FileXLogger.logQueryError(e);
		}
		return resultSet;
	}
	
	public void executeQueryUsingPreparedStatment(String query, List<Object> objectList) {
		try {
			try {
				if (resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
			} catch (Exception e) {
				FileXLogger.logQueryError(e);
			}

			
			if (this.connection == null) {
				FileXLogger.logQueryTrace("connection null DBIMP--->>> " + query);
			}

			preStatement = this.connection.prepareStatement(query);
			int counter = 1;
			for(Object object : objectList)
			{
				preStatement.setObject(counter, object);
				counter++;
			}
			long startdate = System.currentTimeMillis();
			resultSet = preStatement.executeQuery();
			long enddate = System.currentTimeMillis();
			if (Configuration.querylog) {
				
				FileXLogger.logQueryTrace(query + " -> " + (enddate - startdate));
			}
		} catch (Exception e) {
			FileXLogger.logQueryError(e);
		}
	}

	/**
	 * Method to insert the record
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int updateSQL(String sql) throws SQLException 
	{
		try {
			long startdate = System.currentTimeMillis();
			statement = this.connection.createStatement();

			int ret = statement.executeUpdate(sql);
			try {
				statement.close();
			} catch (Exception e) {
				FileXLogger.logQueryError(e);
			}

			if (Configuration.querylog) {
				long enddate = System.currentTimeMillis();
				FileXLogger.logQueryTrace(sql + " -> " + ret + " " + (enddate - startdate));
			}
			return ret;
		} catch (final Exception e) {
			FileXLogger.logQueryError(e);
			return -1;
		}
	}

	/**
	 * Method to close the ResultSet and Statment
	 */
	public void closeResultSetAndStatment() {
		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
		} catch (Exception e) {
			FileXLogger.logQueryError(e);
		}
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (Exception e) {
			FileXLogger.logQueryError(e);
		}
	}

	/**
	 * Method to close the Database connection
	 */
	public void closeConnection() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
				this.connection = null;
			}
		} catch (Exception e) {
			FileXLogger.logQueryError(e);
		}
	}

	public int insertUsingPreparedStatement(String query, List<Object> objectList)throws SQLException, Exception
    {
    	int result = 0;
    	long startdate = System.currentTimeMillis();
    	try
    	{
    		preStatement = connection.prepareStatement(query);
        	int counter = 1;
        	if(objectList != null && objectList.size() != 0)
        	{
        		for(Object object : objectList)
            	{
            		preStatement.setObject(counter, object);
            		if(counter == objectList.size())
            		{
            			result=	preStatement.executeUpdate();
            			counter = 1;
            			continue;
            		}
            		counter++;
            	}	
        	}
        	else
        		result=	preStatement.executeUpdate();
    	}
    	catch(Exception e)       
    	{
    		FileXLogger.logQueryError(e);
    	}
    	finally
    	{
    		try
    		{
    			preStatement.close();
    		}
    		catch(Exception e)
    		{
    			FileXLogger.logQueryError(e);
    		}
    	}
    	if (Configuration.querylog) {
			long enddate = System.currentTimeMillis();
			FileXLogger.logQueryTrace(query + " -> " + (enddate - startdate));
		}
    	return result;
    }
	
	public int updateBatch(PreparedStatement preStatement) throws Exception 
	{
		int result = 0;
		
		try
		{
			preStatement.executeBatch();
			result = 1;
		}
		catch(Exception e)
		{
			FileXLogger.logQueryError(e);
		}
		
		return result;
	}
	
	public int insertUsingPreparedStatementList(String query, Object value)
    {
    	int result = 0;
    	try
    	{
    		PreparedStatement preparedStatment = null;
        	int executeCount=0;
    		preparedStatment = connection.prepareStatement(query);
    		
    		List<List<Object>> objectList = (List<List<Object>>) value;
        	for(Object object : objectList)
        	{
        		preparedStatment = assignListToPreparedStatement(preparedStatment, object);
        		preparedStatment.addBatch();
        		executeCount++;
        		if(executeCount == 1000)
        		{
        			preparedStatment.executeBatch();
        			executeCount = 0;
        		}
        	}
        	
        	if(executeCount != 0)
        	{
        		preparedStatment.executeBatch();
        	}
        	
        	preparedStatment.close();
        	result=1;
    	}
    	catch(Exception e)
    	{
    		FileXLogger.logQueryError(e);
    	}
    	return result;
    }
	
	private PreparedStatement assignListToPreparedStatement(PreparedStatement preparedStatement, Object objectList)
	{
		try
		{
			if(objectList != null)
			{
				List<Object> valueList = (List<Object>)objectList;
				int counter = 1;
				for(Object object : valueList)
				{
					preparedStatement.setObject(counter++, object);
				}
			}
		}
		catch(Exception e)
		{
			FileXLogger.logQueryError(e);
		}
		return preparedStatement;
	}
	
	
	/**
	 * Start of Database Methods Used to handle Database Related Operations
	 * @param token
	 * @return
	 */
//	private Connection getConnection()
//    {
//    	try
//    	{
//    		if(Configuration.jndiOrJdbc.equalsIgnoreCase("JNDI")){
//    			InitialContext context = new InitialContext();
//    			connection = ((DataSource)context.lookup(Configuration.jndiConnectionName)).getConnection();
//    		}else if(Configuration.jndiOrJdbc.equalsIgnoreCase("JDBC")){
//    			driver = (Driver)Class.forName(Configuration.dbDriver).newInstance();
//    			DriverManager.registerDriver(driver);
//    			connection = DriverManager.getConnection(Configuration.dbDataSrc, Configuration.dbUser, Configuration.dbPwd);
//    		}
//    	}
//    	catch(Exception e)
//    	{
//    		FileXLogger.logQueryError(e);
//    	}
//    	return connection;
//    }
	
	public void executeprocedureForMsSql(String procedureName, Map<String, Object> searchParam) 
	{
		try 
		{
			long startTime = System.currentTimeMillis();
			String paramCount =null;

			if(searchParam != null && searchParam.size()>0)
			{
				paramCount="(";
				for(int i=0;i<searchParam.size();i++)
				{
					paramCount=paramCount+"?,";
				}
				paramCount=paramCount.substring(0, paramCount.length()-1);
				paramCount=paramCount+")";
			}
			else
				paramCount="";
			cstmt = connection.prepareCall("{call "+procedureName+paramCount+ "}");
			if( searchParam != null && !searchParam.isEmpty()) 
			{
				for(String key : searchParam.keySet()) 
				{
					cstmt.setObject(key, searchParam.get(key));
				}
			} 
			boolean results = cstmt.execute();
		} 
		catch(Exception e) 
		{
			FileXLogger.logQueryError(e);
		}
	}
	
	public ResultSet executeProceduresForOracle(String procedureName,  Map<String, Object> hmParams)
	{
		ResultSet rs = null;
	    String paramCount =null;
		try {
			//String procedureParams = ResourceBundleUtil.getQueryBundle("OracleProc").getString(procedureName);
		//	String params[] = (procedureParams != null && !procedureParams.isEmpty()) ? procedureParams.split(",") : null; 
			
			ResourceBundle resourceBundle = ResourceBundle.getBundle("OracleProc");
			String procedureParams = resourceBundle.getString(procedureName);
			String params[] = (procedureParams != null && !procedureParams.isEmpty()) ? procedureParams.split(",") : null;
			
			if(params != null && params.length>0)
			{
				paramCount="(";
				for(int i=0; i< hmParams.size();i++)
					paramCount=paramCount+"?,";
				paramCount=paramCount.substring(0, paramCount.length()-1);
				paramCount=paramCount+")";
			}
			else
			{
				paramCount="";
			}
			cstmt = connection.prepareCall("{call "+procedureName+paramCount+ "}");
			int counter = 1;
			if(hmParams != null && params.length !=0)
			{
				for(String param : params) {
//						param = param.replaceAll("@", "").trim();
						cstmt.setObject(counter, hmParams.containsKey(param) ? hmParams.get(param) : "");
						counter++;
				}
			}
			//cstmt.registerOutParameter(counter, OracleTypes.CURSOR);
			int results = cstmt.executeUpdate();
			
		   /* if (results>0)
		    	rs = (ResultSet) cstmt.getObject(counter);*/
		           
	    
		}
		catch (Exception e) {
			FileXLogger.logQueryError(e);
		}
		
		return rs;
	}
}
