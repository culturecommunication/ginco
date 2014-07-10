package fr.mcc.ginco.security;

import org.owasp.csrfguard.log.ILogger;
import org.owasp.csrfguard.log.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSRFGuardLogger implements ILogger {

	private static final long serialVersionUID = -6227944540521232578L;
	
	private Logger logger  = LoggerFactory.getLogger(CSRFGuardLogger.class);

	@Override
	public void log(String msg) {
		logger.info(msg);
	}

	@Override
	public void log(LogLevel level, String msg) {
		switch (level) {
		case Trace:
			logger.trace(msg);
			break;
		case Debug:
			logger.debug(msg);
			break;
		case Info:
			logger.info(msg);
			break;
		case Warning:
			logger.warn(msg);
			break;
		case Error:
			logger.error(msg);
			break;
		case Fatal:
			logger.error(msg);
			break;
		default:
			throw new RuntimeException("unsupported log level " + level);
		}

	}

	@Override
	public void log(Exception exception) {
		logger.error(exception.getLocalizedMessage(), exception);
	}

	@Override
	public void log(LogLevel level, Exception exception) {
		switch (level) {
		case Trace:
			logger.trace(exception.getLocalizedMessage(), exception);
			break;
		case Debug:
			logger.debug(exception.getLocalizedMessage(), exception);
			break;
		case Info:
			logger.info(exception.getLocalizedMessage(), exception);
			break;
		case Warning:
			logger.warn(exception.getLocalizedMessage(), exception);
			break;
		case Error:
			logger.error(exception.getLocalizedMessage(), exception);
			break;
		case Fatal:
			logger.error(exception.getLocalizedMessage(), exception);
			break;
		default:
			throw new RuntimeException("unsupported log level " + level);

		}
	}

}
