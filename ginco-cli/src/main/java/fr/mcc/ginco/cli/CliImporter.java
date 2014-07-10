package fr.mcc.ginco.cli;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.imports.ISKOSImportService;
import fr.mcc.ginco.solr.IThesaurusIndexerService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Cli importer service.
 */
@Service
public class CliImporter {

	@Inject
	@Named("skosImportService")
	private ISKOSImportService skosImportService;

	@Inject
	@Named("thesaurusIndexerService")
	private IThesaurusIndexerService thesaurusIndexerService;

	/**
	 * SKOS import from file.
	 *
	 * @param inputFile
	 */
	public void importSkos(String inputFile) {
		File inputF = new File(inputFile);
		File tempDir = new File("/tmp");
		try {

			Map<Thesaurus, Set<Alignment>> importResult = skosImportService.
					importSKOSFile(FileUtils.readFileToString(inputF), "tmpSkos.rdf", tempDir);
			
			Thesaurus thesaurus = importResult.keySet().iterator().next();
			thesaurusIndexerService.indexThesaurus(thesaurus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
