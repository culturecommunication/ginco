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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;

import fr.mcc.ginco.audit.utils.AuditContext;
import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.helpers.ThesaurusHelper;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * Implementation of the SKOS thesaurus import service
 * 
 */
@Transactional
@Service("skosImportService")
public class SKOSImportServiceImpl implements ISKOSImportService {

	private static Logger logger = LoggerFactory.getLogger(SKOSImportServiceImpl.class);

	@Inject
	private IThesaurusDAO thesaurusDAO;

	@Inject
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;

	@Inject
	@Named("thesaurusHelper")
	private ThesaurusHelper thesaurusHelper;

	@Inject
	@Named("skosThesaurusBuilder")
	private ThesaurusBuilder thesaurusBuilder;

	@Inject
	@Named("skosConceptsBuilder")
	private ConceptsBuilder conceptsBuilder;

	@Inject
	@Named("skosArraysBuilder")
	private ThesaurusArraysBuilder arraysBuilder;
	
	@Inject
	@Named("skosImportUtils")
	private SKOSImportUtils skosImportUtils;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.imports.ISKOSImportService#importSKOSFile(java.lang.String,
	 * java.lang.String, java.io.File)
	 */
	@Override
	public Map<Thesaurus, Set<Alignment>> importSKOSFile(String fileContent,
			String fileName, File tempDir) {
		AuditContext.disableAudit();
		Map<Thesaurus, Set<Alignment>> res = new HashMap<Thesaurus, Set<Alignment>>();
		Set<Alignment> bannedAlignments = new HashSet<Alignment>();
		Thesaurus thesaurus = null;

		URI fileURI = writeTempFile(fileContent, fileName, tempDir);
		try {
			// Reader init
			Model model = ModelFactory.createDefaultModel();
			OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
			
			InputStream in = FileManager.get().open(fileURI.toString());
			model.read(in, null);
			
			InputStream inOnt = FileManager.get().open(fileURI.toString());
			ontModel.read(inOnt, null);
			// Getting thesaurus
			Resource thesaurusSKOS = getSKOSThesaurus(model);
			if (thesaurusSKOS == null) {
				logger.error("no thesaurus found");
			} else {
				if (thesaurusDAO.getById(thesaurusSKOS.getURI()) != null) {
					throw new BusinessException(
							"Trying to import an existing thesaurus "
									+ thesaurusSKOS.getURI(),
							"import-already-existing-thesaurus");
				}
				thesaurus = thesaurusBuilder.buildThesaurus(thesaurusSKOS,
						model);

				thesaurusDAO.update(thesaurus);

				// Set default version history
				ThesaurusVersionHistory defaultVersion = thesaurusHelper
						.buildDefaultVersion(thesaurus);
				Set<ThesaurusVersionHistory> versions = new HashSet<ThesaurusVersionHistory>();
				versions.add(defaultVersion);
				thesaurusVersionHistoryDAO.update(defaultVersion);
			}

			List<Resource> skosConcepts = skosImportUtils.getSKOSRessources(
					model, SKOS.CONCEPT);
			bannedAlignments = conceptsBuilder.buildConcepts(thesaurus,
					skosConcepts);
			conceptsBuilder.buildConceptsAssociations(thesaurus, skosConcepts, skosImportUtils.getBroaderTypeProperty(ontModel), skosImportUtils.getRelatedTypeProperty(ontModel));
			conceptsBuilder.buildConceptsRoot(thesaurus, skosConcepts);
			Map<String, ThesaurusArray> builtArrays = new HashMap<String, ThesaurusArray>();
			arraysBuilder.buildArrays(thesaurus, model, builtArrays);
			arraysBuilder.buildChildrenArrays(thesaurus, model, builtArrays);

			res.put(thesaurus, bannedAlignments);

		} catch (JenaException je) {
			logger.error("Error reading RDF", je);
			throw new BusinessException("Error reading imported file :"
					+ je.getMessage(), "import-unable-to-read-file", je);
		} finally {
			deleteTempFile(fileURI);
		}
		AuditContext.enableAudit();
		return res;
	}

	/**
	 * Gets the thesaurus resource from the model, returning the first one only
	 * 
	 * @param model
	 * @return
	 */
	private Resource getSKOSThesaurus(Model model) {
		SimpleSelector schemeSelector = new SimpleSelector(null, null,
				(RDFNode) null) {
			public boolean selects(Statement s) {
				if (s.getObject().isResource()) {
					return s.getObject().asResource()
							.equals(SKOS.CONCEPTSCHEME);
				} else {
					return false;
				}
			}
		};

		StmtIterator iter = model.listStatements(schemeSelector);

		Resource thesaurusSKOS = null;
		if (iter.hasNext()) {
			Statement s = iter.next();
			thesaurusSKOS = s.getSubject().asResource();
		}
		return thesaurusSKOS;
	}

	private void deleteTempFile(URI fileURI) {
		File f = new File(fileURI);
		f.delete();
	}

	private URI writeTempFile(String fileContent, String fileName, File tempDir) {
		logger.debug("Writing temporary file for import");

		File file;
		try {
			file = File.createTempFile("skosimport", ".tmp", tempDir);
			logger.debug("Filename : " + file.getName());
			FileUtils.write(file,fileContent,"UTF-8");
		} catch (IOException e) {
			throw new BusinessException(
					"Error storing temporarty file for import " + fileName,
					"import-unable-to-write-temporary-file", e);
		}
		return file.toURI();
	}
}
