package fr.mcc.ginco.cli;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.ISKOSExportService;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IMistralRevService;
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

	@Inject
	@Named("mistralRevService")
	private IMistralRevService mistralRevService;

	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;

	@Value("${ginco.default.language}")
	private String defaultLang;

	public void exportSKOS(String thesaurusId, String outputFile)
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

	public void exportRevisions(String thesaurusId, String outputFile, long timestamp){
		Thesaurus targetThesaurus = thesaurusService
				.getThesaurusById(thesaurusId);
		if (targetThesaurus != null) {
			log.info("Revisions exporting : "+targetThesaurus.getTitle());
			Language lang = languagesService.getLanguageById(defaultLang);

			File dest = new File(outputFile);
			try {
				File resFile = mistralRevService.getRevisions(targetThesaurus, timestamp, lang);
				FileUtils.copyFile(resFile, dest);
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
