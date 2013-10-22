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
package fr.mcc.ginco.imports.ginco.idgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.result.bean.GincoExportedBranch;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.log.Log;

/**
 * This class generate new ids for all items (concepts, terms, notes, relations,
 * etc.) included in {@GincoExportedBranch} object
 * 
 */
@Component("gincoConceptBranchIdGenerator")
public class GincoConceptBranchIdGenerator {

	@Inject
	@Named("gincoTermIdGenerator")
	private GincoTermIdGenerator gincoTermIdGenerator;

	@Inject
	@Named("gincoConceptIdGenerator")
	private GincoConceptIdGenerator gincoConceptIdGenerator;

	@Inject
	@Named("gincoNoteIdGenerator")
	private GincoNoteIdGenerator gincoNoteIdGenerator;

	@Inject
	@Named("gincoRelationshipIdGenerator")
	private GincoRelationshipIdGenerator gincoRelationshipIdGenerator;
	
	@Inject
	@Named("gincoAlignmentIdGenerator")
	private GincoAlignmentIdGenerator gincoAlignmentIdGenerator;

	private Map<String, String> idMapping = new HashMap<String, String>();


	@Log
	private Logger logger;

	public void resetIdsForExportedBranch(
			GincoExportedBranch branchToUpdate) {
		logger.debug("Branch import : generating new ids for elements to be imported");
		
		logger.debug("Branch import : generating new ids for root concept");
		String newRootConceptId = gincoConceptIdGenerator.getIdForConcept(
				branchToUpdate.getRootConcept().getIdentifier(), idMapping);
		branchToUpdate.getRootConcept().setIdentifier(newRootConceptId);

		logger.debug("Branch import : generating new ids for all concepts");
		for (ThesaurusConcept concept : branchToUpdate.getConcepts()) {
			String newConceptId = gincoConceptIdGenerator.getIdForConcept(
					concept.getIdentifier(), idMapping);
			concept.setIdentifier(newConceptId);
		}	

		logger.debug("Branch import : generating new ids for concept notes");
		Map<String, JaxbList<Note>> updatedConceptNotes = gincoNoteIdGenerator
				.getNotesWithNewIds(branchToUpdate.getConceptNotes(), idMapping);
		branchToUpdate.getConceptNotes().clear();
		branchToUpdate.getConceptNotes().putAll(updatedConceptNotes);

		logger.debug("Branch import : generating new ids for terms");
		List<ThesaurusTerm> updatedTerms = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm term : branchToUpdate.getTerms()) {
			term.setIdentifier(gincoTermIdGenerator.getIdForTerm(
					term.getIdentifier(), idMapping));	
			 
			 term.getConcept().setIdentifier(
					gincoConceptIdGenerator.getIdForConcept(term.getConcept()
							.getIdentifier(), idMapping));
			 updatedTerms.add(term);
		}	

		logger.debug("Branch import : generating new ids for term notes");
		Map<String, JaxbList<Note>> updatedTermNotes = gincoNoteIdGenerator
				.getNotesWithNewIds(branchToUpdate.getTermNotes(), idMapping);
		branchToUpdate.getTermNotes().clear();
		branchToUpdate.getTermNotes().putAll(updatedTermNotes);
		
		logger.debug("Branch import : generating new ids for hierarchical relationships");
		Map<String, JaxbList<ConceptHierarchicalRelationship>> updatedRelations = gincoRelationshipIdGenerator
				.getIdsForHierarchicalRelations(
						branchToUpdate.getHierarchicalRelationship(), idMapping);
		branchToUpdate.getHierarchicalRelationship().clear();
		branchToUpdate.getHierarchicalRelationship().putAll(updatedRelations);
		
		logger.debug("Branch import : generating new ids for alignments");
		Map<String, JaxbList<Alignment>> updateAlignments = gincoAlignmentIdGenerator.getIdsForAlignments(branchToUpdate.getAlignments(), idMapping);
		branchToUpdate.getAlignments().clear();
		branchToUpdate.getAlignments().putAll(updateAlignments);

	
	}
}
