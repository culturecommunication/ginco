package fr.mcc.ginco.ark;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import fr.mcc.ginco.IIdentifierProvider;
import fr.mcc.ginco.log.Log;
import org.slf4j.Logger;

@Service("simpleArkProviderImpl")
public class SimpleArkProviderImpl implements IIdentifierProvider {
	
	private @Value("${application.ark.nma}") String nma;
	private @Value("${application.ark.naan}") String naan;
	private @Log Logger logger;

	@Override
	public String getArkId(Class clazz) {
		//First implementation : independent from the class given in parameter
		//Always return ARK String randomly generated
		String arkId=null;
		try {
			UUID nq = UUID.randomUUID();
			arkId = new URL(nma + "/ark:/"+ naan + "/" + nq.toString()).toString();
	        logger.info("Generating ARK Id  | Value generated : "+ arkId);
	    } catch (MalformedURLException malformedURLException) {
	        malformedURLException.printStackTrace();
	        logger.error("Error creating ARK Id | Value generated : "+ arkId);
	    }
		return arkId;
	}	
}