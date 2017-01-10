package fr.mcc.ginco.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import fr.mcc.ginco.services.IInfoService;

@Service("infoService")
public class InfoServiceImpl implements IInfoService{
	@Value("${documentation.url}")
	private String documentationUrl;
	
	@Override
	public String getDocumentationUrl() {
		return documentationUrl;
	}

}
