package fr.mcc.ginco.cli;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.ISKOSExportService;
import fr.mcc.ginco.services.IThesaurusService;

@Service
public class CliExporter  {
	
	private static Logger log = LoggerFactory.getLogger(App.class);
	
	@Inject
	@Named("skosExportService")
	private ISKOSExportService skosExportService;
	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	
	public void export(String thesaurusId, String outputFile)
	{
		Thesaurus targetThesaurus = thesaurusService
				.getThesaurusById(thesaurusId);
		if (targetThesaurus!=null) {
			log.info("Skos exporting : "+targetThesaurus.getTitle());
			File skosFile = skosExportService.getSKOSExport(targetThesaurus);
			
			File dest = new File(outputFile);
			try {
				FileUtils.copyFile(skosFile, dest);
				log.info("Exported " + outputFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else 
		{
			throw new BusinessException("Unable to find thesaurus id "+thesaurusId, "unable-to-find-thesaurus");
		}
		
	}

}
