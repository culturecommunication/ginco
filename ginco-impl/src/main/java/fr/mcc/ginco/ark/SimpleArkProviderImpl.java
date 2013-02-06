package fr.mcc.ginco.ark;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import fr.mcc.ginco.IIdentifierProvider;
import fr.mcc.ginco.log.Log;
import org.slf4j.Logger;

/**
 * First implementation : independent from the class given in parameter
 * Always return ARK String randomly generated
 */
@Service("simpleArkProviderImpl")
public class SimpleArkProviderImpl implements IIdentifierProvider {
	
	@Value("${application.ark.nma}") private String nma;
	@Value("${application.ark.naan}") private String naan;
	@Log  private Logger logger;

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.IIdentifierProvider#getArkId(java.lang.Class)
	 */
	@Override
	public String getArkId(Class clazz) {
		String arkId=null;
		try {
			UUID nq = UUID.randomUUID();
			arkId = new URL(nma + "/ark:/"+ naan + "/" + nq.toString()).toString();
	        logger.info("Generating ARK Id  | Value generated : "+ arkId);
	    } catch (MalformedURLException malformedURLException) {
	        logger.error("Error creating ARK Id | Value generated : "+ arkId, malformedURLException);
	    }
		return arkId;
	}	
}