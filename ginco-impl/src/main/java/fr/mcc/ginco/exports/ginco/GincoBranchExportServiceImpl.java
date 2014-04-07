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
package fr.mcc.ginco.exports.ginco;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.exports.IGincoBranchExportService;
import fr.mcc.ginco.exports.IGincoExportServiceUtil;
import fr.mcc.ginco.exports.result.bean.GincoExportedBranch;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.services.ICustomConceptAttributeTypeService;
import fr.mcc.ginco.services.ICustomTermAttributeTypeService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.ThesaurusConceptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service("gincoBranchExportService")
public class GincoBranchExportServiceImpl implements IGincoBranchExportService {

	private Logger logger = LoggerFactory.getLogger(GincoBranchExportServiceImpl.class);

	@Inject
	private GincoConceptExporter gincoConceptExporter;

	@Inject
	private IGincoExportServiceUtil gincoExportServiceUtil;

	@Inject
	private GincoTermExporter gincoTermExporter;

	@Inject
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	private IThesaurusTermService thesaurusTermService;

	@Inject
	private ICustomTermAttributeTypeService customTermAttributeTypeService;

	@Inject
	private ICustomConceptAttributeTypeService customConceptAttributeTypeService;

	@Inject
	private GincoAttributesExporter gincoAttributesExporter;


	@Override
	public String getBranchExport(ThesaurusConcept conceptBranchToExport)
			throws TechnicalException {
		String conceptId = conceptBranchToExport.getIdentifier();
		GincoExportedBranch branchToExport = new GincoExportedBranch();

		List<ThesaurusConcept> childrenConcepts = thesaurusConceptService.getRecursiveChildrenByConceptId(conceptId);
		logger.debug("Getting all children recursively for exporting branch with root concept : " + conceptId);

		List<ThesaurusConcept> allConcepts = new ArrayList<ThesaurusConcept>();
		allConcepts.addAll(childrenConcepts);
		allConcepts.add(conceptBranchToExport);

		branchToExport.setRootConcept(conceptBranchToExport);
		branchToExport.setConcepts(childrenConcepts);

		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
		for (ThesaurusConcept concept : allConcepts) {
			for (ThesaurusTerm term : thesaurusTermService.getTermsByConceptId(concept.getIdentifier())) {
				gincoTermExporter.addExportedTerms(branchToExport, term);
			}
		}


		for (ThesaurusTerm term : terms) {
			JaxbList<Note> termNotes = gincoTermExporter
					.getExportTermNotes(term);
			if (termNotes != null && !termNotes.isEmpty()) {
				branchToExport.getTermNotes().put(
						term.getIdentifier(), termNotes);
			}
		}

		for (ThesaurusConcept concept : allConcepts) {
			JaxbList<Note> conceptNotes = gincoConceptExporter
					.getExportConceptNotes(concept);
			if (conceptNotes != null && !conceptNotes.isEmpty()) {
				branchToExport.getConceptNotes().put(
						concept.getIdentifier(), conceptNotes);
			}
			JaxbList<CustomConceptAttribute> conceptAttributes = gincoAttributesExporter
					.getExportedConceptAttributes(concept);
			branchToExport.getConceptAttributes().put(concept.getIdentifier(), conceptAttributes);
		}

		//Exporting hierarchical relationships only for children with parents in the branch
		List<String> conceptsIds = ThesaurusConceptUtils.getIdsFromConceptList(allConcepts);
		for (ThesaurusConcept concept : childrenConcepts) {
			JaxbList<ConceptHierarchicalRelationship> parentConceptHierarchicalRelationship = gincoConceptExporter
					.getExportHierarchicalConcepts(concept);
			if (parentConceptHierarchicalRelationship != null
					&& !parentConceptHierarchicalRelationship.isEmpty()) {

				List<ConceptHierarchicalRelationship> availableParents = new ArrayList<ConceptHierarchicalRelationship>();
				availableParents.addAll(parentConceptHierarchicalRelationship.getList());
				for (ConceptHierarchicalRelationship conceptHierarchicalRelationship : availableParents) {
					if (!conceptsIds.contains(conceptHierarchicalRelationship.getIdentifier().getParentconceptid())) {
						parentConceptHierarchicalRelationship.getList().remove(conceptHierarchicalRelationship);
					}
				}
				branchToExport.getHierarchicalRelationship().put(
						concept.getIdentifier(),
						parentConceptHierarchicalRelationship);
			}
		}
		// Exporting alignments
		JaxbList<Alignment> alignments = gincoConceptExporter.getExportAlignments(conceptBranchToExport);
		if (alignments != null && !alignments.isEmpty()) {
			branchToExport.getAlignments().put(
					conceptBranchToExport.getIdentifier(), alignments);
		}

		branchToExport.setTermAttributeTypes(customTermAttributeTypeService
				.getAttributeTypesByThesaurus(conceptBranchToExport.getThesaurus()));
		branchToExport
				.setConceptAttributeTypes(customConceptAttributeTypeService
						.getAttributeTypesByThesaurus(conceptBranchToExport.getThesaurus()));

		return gincoExportServiceUtil.serializeBranchToXmlWithJaxb(branchToExport);
	}
}