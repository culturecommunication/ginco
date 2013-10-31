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
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentConcept;
import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IAlignmentDAO;
import fr.mcc.ginco.dao.IAssociativeRelationshipDAO;
import fr.mcc.ginco.dao.IExternalThesaurusDAO;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.INodeLabelDAO;
import fr.mcc.ginco.dao.INoteDAO;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.helpers.ThesaurusHelper;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * Implementation of the SKOS thesaurus import service
 *
 */
@Transactional
@Service("skosImportService")
public class SKOSImportServiceImpl implements ISKOSImportService {

	@Log
	private Logger logger;

	@Inject
	@Named("thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Inject
	@Named("thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;

	@Inject
	@Named("associativeRelationshipDAO")
	private IAssociativeRelationshipDAO associativeRelationshipDAO;

	@Inject
	@Named("noteDAO")
	private INoteDAO noteDAO;

	@Inject
	@Named("thesaurusArrayDAO")
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@Inject
	@Named("nodeLabelDAO")
	private INodeLabelDAO nodeLabelDAO;

	@Inject
	@Named("thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;

	@Inject
	@Named("thesaurusHelper")
	private ThesaurusHelper thesaurusHelper;

	@Inject
	@Named("skosThesaurusBuilder")
	private ThesaurusBuilder thesaurusBuilder;

	@Inject
	@Named("skosConceptBuilder")
	private ConceptBuilder conceptBuilder;

	@Inject
	@Named("skosConceptNoteBuilder")
	private ConceptNoteBuilder conceptNoteBuilder;

	@Inject
	@Named("skosTermBuilder")
	private TermBuilder termBuilder;

	@Inject
	@Named("skosArrayBuilder")
	private ThesaurusArrayBuilder arrayBuilder;

	@Inject
	@Named("skosNodeLabelBuilder")
	private NodeLabelBuilder nodeLabelBuilder;

	@Inject
	@Named("skosAlignmentBuilder")
	private AlignmentBuilder alignmentBuilder;

	@Inject
	@Named("alignmentDAO")
	private IAlignmentDAO alignmentDAO;

	@Inject
	@Named("alignmentConceptDAO")
	private IGenericDAO<AlignmentConcept, Integer> alignmentConceptDAO;

	@Inject
	@Named("externalThesaurusDAO")
	private IExternalThesaurusDAO externalThesaurusDAO;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.mcc.ginco.imports.ISKOSImportService#importSKOSFile(java.lang.String,
	 * java.lang.String, java.io.File)
	 */
	@Override
	public Map<Thesaurus,Set<Alignment>> importSKOSFile(String fileContent, String fileName,
			File tempDir) throws BusinessException {

		Map<Thesaurus,Set<Alignment>> res = new HashMap<Thesaurus,Set<Alignment>>();
		Set<Alignment> bannedAlignments = new HashSet<Alignment>();
		Thesaurus thesaurus = null;

		URI fileURI = writeTempFile(fileContent, fileName, tempDir);
		try {
			// Reader init
			Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open(fileURI.toString());
			model.read(in, null);

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

				//Set default version history
				ThesaurusVersionHistory defaultVersion = thesaurusHelper.buildDefaultVersion(thesaurus);
				Set<ThesaurusVersionHistory> versions = new HashSet<ThesaurusVersionHistory>();
				versions.add(defaultVersion);
				thesaurusVersionHistoryDAO.update(defaultVersion);
			}

			List<Resource> skosConcepts = getSKOSRessources(model, SKOS.CONCEPT);
			bannedAlignments = buildConcepts(thesaurus, skosConcepts);
			buildConceptsAssociations(thesaurus, skosConcepts);
			buildConceptsRoot(thesaurus, skosConcepts);
			buildArrays(thesaurus, model);
			buildChildrenArrays(thesaurus, model);

			res.put(thesaurus, bannedAlignments);

		} catch (JenaException je) {
			throw new BusinessException("Error reading imported file :"+je.getMessage(),
					"import-unable-to-read-file", je);
		} finally {
			deleteTempFile(fileURI);
		}
		return res;
	}

	/**
	 * Builds the thesaurus arrays from the model
	 *
	 * @param thesaurus
	 * @param model
	 */
	private void buildArrays(Thesaurus thesaurus, Model model) {
		List<Resource> skosCollections = getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			ThesaurusArray array = arrayBuilder.buildArray(skosCollection,
					model, thesaurus);
			thesaurusArrayDAO.update(array);
			NodeLabel nodeLabel = nodeLabelBuilder.buildNodeLabel(skosCollection, thesaurus, array);
			nodeLabelDAO.update(nodeLabel);
		}
	}

	/**
	 * Builds thesaurus array relations from the model
	 *
	 * @param thesaurus
	 * @param model
	 */

	private void buildChildrenArrays(Thesaurus thesaurus, Model model){
		List<Resource> skosCollections = getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			List<ThesaurusArray> childrenArrays = arrayBuilder.getChildrenArrays(skosCollection, thesaurus);
			for (ThesaurusArray childrenArray : childrenArrays){
				thesaurusArrayDAO.update(childrenArray);
			}
		}
	}

	/**
	 * Launch the calculation of the root concepts and set it
	 *
	 * @param thesaurus
	 * @param skosConcepts
	 */
	private void buildConceptsRoot(Thesaurus thesaurus,
			List<Resource> skosConcepts) {
		for (Resource skosConcept : skosConcepts) {
			// Root calculation
			ThesaurusConcept concept = conceptBuilder.buildConceptRoot(
					skosConcept, thesaurus);
			thesaurusConceptDAO.update(concept);
		}
	}

	/**
	 * Builds the parent/child and relationship associations
	 *
	 * @param thesaurus
	 * @param skosConcepts
	 */
	private void buildConceptsAssociations(Thesaurus thesaurus,
			List<Resource> skosConcepts) {
		List<AssociativeRelationship> allRelations = new ArrayList<AssociativeRelationship>();
		for (Resource skosConcept : skosConcepts) {
			ThesaurusConcept concept = conceptBuilder
					.buildConceptHierarchicalRelationships(skosConcept,
							thesaurus);
			thesaurusConceptDAO.update(concept);

			Set<AssociativeRelationship> associativeRelationships = conceptBuilder
					.buildConceptAssociativerelationship(skosConcept, thesaurus);
			for (AssociativeRelationship relation : associativeRelationships) {
				Boolean isRelationAdded = false;
				for (AssociativeRelationship existedRelation : allRelations){
						if (existedRelation.getConceptLeft().equals(relation.getConceptRight())
								&& existedRelation.getConceptRight().equals(relation.getConceptLeft())
								|| existedRelation.equals(relation)){
							isRelationAdded = true;
						}
				}
				if (!isRelationAdded) {
					allRelations.add(relation);
					associativeRelationshipDAO.update(relation);
				}
			}
		}
	}

	/**
	 * Builds the concept with minimal informations and it's terms and notes
	 *
	 * @param thesaurus
	 * @param skosConcepts
	 */
	private Set<Alignment> buildConcepts(Thesaurus thesaurus, List<Resource> skosConcepts) {
		Set<Alignment> bannedAlignments = new HashSet<Alignment>();
		for (Resource skosConcept : skosConcepts) {
			// Minimal concept informations
			ThesaurusConcept concept = conceptBuilder.buildConcept(skosConcept,
					thesaurus);

			thesaurusConceptDAO.update(concept);
			ThesaurusTerm preferredTerm = null;

			// Concept terms
			List<ThesaurusTerm> terms = termBuilder.buildTerms(skosConcept,
					thesaurus, concept);
			for (ThesaurusTerm term : terms) {
				if (term.getPrefered())
					preferredTerm = term;
				thesaurusTermDAO.update(term);
			}

			// Concept notes
			List<Note> conceptNotes = conceptNoteBuilder.buildConceptNotes(
					skosConcept, concept,preferredTerm, thesaurus);
			for (Note conceptNote : conceptNotes) {
				noteDAO.update(conceptNote);
			}

			// Concept alignments
			List<Alignment> alignments = alignmentBuilder.buildAlignments(skosConcept, concept);
			for (Alignment alignment: alignments) {
				if (alignment.getExternalTargetThesaurus() != null){
					externalThesaurusDAO.update(alignment.getExternalTargetThesaurus());
				}
				if (alignment.getInternalTargetThesaurus() != null
						|| alignment.getExternalTargetThesaurus() != null){
					for (AlignmentConcept alignmentConcept : alignment.getTargetConcepts()){
						alignmentConceptDAO.update(alignmentConcept);
					}
					alignmentDAO.update(alignment);
				} else {
					bannedAlignments.add(alignment);
				}

			}
		}
		return bannedAlignments;
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

	/**
	 * Gets the list of resources from the gien model
	 *
	 * @param model
	 * @param resource
	 * @return
	 */
	private List<Resource> getSKOSRessources(Model model,
			final Resource resource) {
		SimpleSelector schemeSelector = new SimpleSelector(null, null,
				(RDFNode) null) {
			public boolean selects(Statement s) {
				if (s.getObject().isResource()) {
					return s.getObject().asResource().equals(resource);
				} else {
					return false;
				}
			}
		};
		StmtIterator iter = model.listStatements(schemeSelector);

		List<Resource> skosRessources = new ArrayList<Resource>();
		while (iter.hasNext()) {
			Statement s = iter.next();
			skosRessources.add(s.getSubject().asResource());
		}
		return skosRessources;
	}

	private void deleteTempFile(URI fileURI) {
		File f = new File(fileURI);
		f.delete();
	}

	private URI writeTempFile(String fileContent, String fileName, File tempDir)
			throws BusinessException {
		logger.debug("Writing temporary file for import");

		File file;
		try {
			file = File.createTempFile("skosimport", ".tmp", tempDir);
			logger.debug("Filename : " + file.getName());
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(fileContent);
			fileWriter.close();
		} catch (IOException e) {
			throw new BusinessException(
					"Error storing temporarty file for import " + fileName,
					"import-unable-to-write-temporary-file", e);
		}
		return file.toURI();
	}
}
