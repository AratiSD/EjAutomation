package com.ej.automation.sbi.model;

//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileXLogger 
{
	public static Logger redisLog = LogManager.getLogger("com.src.redis.output");
	
	public static Logger xfsTraceLog = LogManager.getLogger("com.src.xfs.output"); 
	public static Logger xfsErrorLog = LogManager.getLogger("com.src.xfs.error");
	
	public static Logger echoLog = LogManager.getLogger("com.src.echo.output"); 
	
	public static Logger queryTraceLog = LogManager.getLogger("com.src.query.output"); 
	public static Logger queryErrorLog = LogManager.getLogger("com.src.query.error");
	
	public static Logger messageLog = LogManager.getLogger("com.src.req.msg");
	
	public static Logger m24TraceLog = LogManager.getLogger("com.src.m24.output");
	public static Logger m24ErrorLog = LogManager.getLogger("com.src.m24.error");
	
	public static Logger commonTraceLog = LogManager.getLogger("com.src.common.output");
	public static Logger commonErrorLog = LogManager.getLogger("com.src.common.error");
	
	public static Logger ejmoveLog = LogManager.getLogger("com.src.ejmove.output");
	public static Logger ejmoveErrorLog = LogManager.getLogger("com.src.ejmove.error");
	
	public static Logger ejAutomationLog = LogManager.getLogger("com.src.ejautomation.output");
	public static Logger ejAutomationErrorLog = LogManager.getLogger("com.src.ejautomation.error");
	
	public static void logRedisTrace(String message)
	{
		redisLog.info(message);
	}
	
	public static void logXFSTrace(String message)
	{
		xfsTraceLog.info(message);
	}
	public static void logXFSError(Exception e)
	{
		if(e!=null)
		{
			if(e.fillInStackTrace()!=null)
			{
				xfsErrorLog.info(e.fillInStackTrace());
			}
			if(e.getStackTrace()!=null)
			{
				for(int i=0;i<e.getStackTrace().length;i++)
				{
					xfsErrorLog.info(e.getStackTrace()[i]);				
				}
			}				
		}		
	}
	
	
	
	public static void logEchoTrace(String message)
	{
		echoLog.info(message);
	}
	
	
	
	public static void logQueryTrace(String message)
	{
		queryTraceLog.info(message);
	}
	public static void logQueryError(Exception e)
	{
		if(e!=null)
		{
			if(e.fillInStackTrace()!=null)
			{
				queryErrorLog.info(e.fillInStackTrace());
			}
			if(e.getStackTrace()!=null)
			{
				for(int i=0;i<e.getStackTrace().length;i++)
				{
					queryErrorLog.info(e.getStackTrace()[i]);				
				}
			}				
		}		
	}
	
	
	public static void logMessageLog(String message)
	{
		messageLog.info(message);
	}
	
	
	
	public static void logM24Trace(String message)
	{
		m24TraceLog.info(message);
	}
	
	public static void logM24Error(Exception e) 
	{

		if(e!=null)
		{
			if(e.fillInStackTrace()!=null)
			{
				m24ErrorLog.info(e.fillInStackTrace());
			}
			if(e.getStackTrace()!=null)
			{
				for(int i=0;i<e.getStackTrace().length;i++)
				{
					m24ErrorLog.info(e.getStackTrace()[i]);				
				}
			}				
		}		
	}
	
	
	
	public static void logCommonTrace(String message)
	{
		commonTraceLog.info(message);
	}
	
	public static void logCommonError(Exception e) 
	{
		if(e!=null)
		{
			if(e.fillInStackTrace()!=null)
			{
				commonErrorLog.info(e.fillInStackTrace());
			}
			if(e.getStackTrace()!=null)
			{
				for(int i=0;i<e.getStackTrace().length;i++)
				{
					commonErrorLog.info(e.getStackTrace()[i]);				
				}
			}				
		}
	}
	
	
	
	
	public static void logEjMoveTrace(String message)
	{
		ejmoveLog.info(message);
	}
	
	public static void logEjMoveError(Exception e) 
	{

		if(e!=null)
		{
			if(e.fillInStackTrace()!=null)
			{
				ejmoveErrorLog.info(e.fillInStackTrace());
			}
			if(e.getStackTrace()!=null)
			{
				for(int i=0;i<e.getStackTrace().length;i++)
				{
					ejmoveErrorLog.info(e.getStackTrace()[i]);				
				}
			}				
		}		
	}
	
	public static void logEjAutomationTrace(String message){
		ejAutomationLog.info(message);
	}
	
	public static void logEjAutomationError(Exception e){
		if(e!=null){
			if(e.fillInStackTrace()!=null){
				ejAutomationErrorLog.info(e.fillInStackTrace());
			}
			if(e.getStackTrace()!=null){
				for(int i=0;i<e.getStackTrace().length;i++){
					ejAutomationErrorLog.info(e.getStackTrace()[i]);
				}
			}
		}
	}
	
	
}
