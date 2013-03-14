/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.imports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOSManager;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;

@Transactional
@Service("skosImportService")
public class SKOSImportServiceImpl implements ISKOSImportService {

	@Log
	private Logger logger;

	@Inject
	@Named("thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

	@Inject
	@Named("skosThesaurusBuilder")
	private ThesaurusBuilder thesaurusBuilder;

	@Override
	public Thesaurus importSKOSFile(String fileContent, String fileName,
			File tempDir) throws BusinessException {
		// Store in temp repo
		URI fileURI = writeTempFile(fileContent, fileName, tempDir);
		Thesaurus thesaurus = null;
		// First create a new SKOSManager
		try {
			SKOSManager manager = new SKOSManager();
			SKOSDataset dataset = manager.loadDataset(fileURI);

			Set<SKOSConceptScheme> allSchemes = dataset.getSKOSConceptSchemes();
			for (SKOSConceptScheme scheme : allSchemes) {
				logger.debug("Vocabulary: " + scheme.getURI());
				Set<SKOSAnnotation> annotations = scheme
						.getSKOSAnnotations(dataset);
				thesaurus = thesaurusBuilder.buildThesaurus(scheme.getURI()
						.toString(), annotations);
				thesaurusDAO.update(thesaurus);				
			}

			for (SKOSConcept concept : dataset.getSKOSConcepts()) {
				logger.debug("Concept: " + concept.getURI());
			}

		} catch (SKOSCreationException e) {
			logger.error("Error creating SKOS elements", e);
		} finally {
			deleteTempFile(fileName);
		}
		return thesaurus;
	}

	private void deleteTempFile(String initialFileName) {
		String name = getTempFileName(initialFileName);
		File f = new File(name);
		f.delete();
	}

	private URI writeTempFile(String fileContent, String fileName, File tempDir)
			throws BusinessException {
		logger.debug("Writing temporary file for import");
		String prefix = fileName.substring(0, fileName.lastIndexOf("."));
		logger.debug("Filename : " + prefix);
		File file;
		try {
			file = File.createTempFile(prefix, ".tmp", tempDir);

			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(fileContent);
			fileWriter.close();
		} catch (IOException e) {
			throw new BusinessException(
					"Error storing temporarty file for import " + prefix,
					"import-unable-to-write-temporary-file");
		}
		return file.toURI();
	}

	private String getTempFileName(String initialFileName) {
		String prefix = initialFileName.substring(0,
				initialFileName.lastIndexOf("."));
		return prefix.concat(".tmp");

	}
}
