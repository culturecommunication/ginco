package fr.mcc.ginco.audit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuditContext {
	private static Logger logger = LoggerFactory.getLogger(AuditContext.class);
	private static final ThreadLocal<Boolean> context = new ThreadLocal<Boolean>();
	public static void enableAudit()
	{
		context.set(true);
		logger.info("Audit is enabled");
	}
	
	public static void disableAudit()
	{
		context.set(false);
		logger.info("Audit is disabled");
	}
	
	public static Boolean getAuditStatus()
	{
		if (context.get() == null)
			return true;
			else 
		return context.get();
	}
}
