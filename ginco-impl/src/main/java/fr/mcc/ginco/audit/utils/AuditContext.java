package fr.mcc.ginco.audit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AuditContext {
	private static Logger logger = LoggerFactory.getLogger(AuditContext.class);
	private static final ThreadLocal<Boolean> CONTEXT = new ThreadLocal<Boolean>();

	/**
	 * Hide public constructor to force usage of static methods.
	 */
	private AuditContext() {
	}

	/**
	 * Enables audit
	 */
	public static void enableAudit() {
		CONTEXT.set(true);
		logger.info("Audit is enabled");
	}

	/**
	 * Disables audit
	 */
	public static void disableAudit() {
		CONTEXT.set(false);
		logger.info("Audit is disabled");
	}

	/**
	 * Gets audit status
	 * @return
	 */
	public static Boolean getAuditStatus() {
		if (CONTEXT.get() == null) {
			return true;
		} else {
			return CONTEXT.get();
		}
	}
}
