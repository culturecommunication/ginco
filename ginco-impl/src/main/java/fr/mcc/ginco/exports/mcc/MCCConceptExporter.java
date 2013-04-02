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
package fr.mcc.ginco.exports.mcc;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.services.IAssociativeRelationshipService;
import fr.mcc.ginco.services.INoteService;

/**
 * This component gives methods to export concepts and its related objects (notes, associations, etc.) to MCC Custom Export Format
 * 
 */
@Component("mccConceptExporter")
public class MCCConceptExporter {
	
	@Inject
	@Named("noteService")
	private INoteService noteService;
	
	@Inject
	@Named("associativeRelationshipService")
	private IAssociativeRelationshipService associativeRelationshipService;

	
	
	/**
	 * This method gets all the parents of a concept in a JaxbList object
	 * @param thesaurusConcept
	 * @return JaxbList<String> parentIds : A JaxbList that contains the ids of parent concepts
	 */
	public JaxbList<String> getExportHierarchicalConcepts(ThesaurusConcept thesaurusConcept) {
		JaxbList<String> parentIds = new JaxbList<String>();
		for (ThesaurusConcept thesaurusParentConcept : thesaurusConcept.getParentConcepts()) {
			parentIds.getList().add(thesaurusParentConcept.getIdentifier());
		}
		return parentIds;
	}
	
	/**
	 * This method gets all the notes of a concept in a JaxbList object
	 * @param thesaurusConcept
	 * @return JaxbList<Note> conceptNotes : A JaxbList of notes
	 */
	public JaxbList<Note> getExportConceptNotes(ThesaurusConcept thesaurusConcept) {
		List<Note> notes = noteService.getConceptNotePaginatedList(thesaurusConcept.getIdentifier(), 0, noteService.getConceptNoteCount(thesaurusConcept.getIdentifier()).intValue());
		JaxbList<Note> conceptNotes = new JaxbList<Note>();
		for (Note note : notes) {
			conceptNotes.getList().add(note);
		}
		return conceptNotes;
	}
	
	/**
	 * This method gets all the associative relations for a concept in a JaxbList object
	 * @param thesaurusConcept
	 * @return JaxbList<String> associatedConceptsIds : A JaxbList that contains the ids of associated concepts
	 */
	public JaxbList<String> getExportAssociativeRelationShip(ThesaurusConcept thesaurusConcept) {
		List<String> associations = associativeRelationshipService.getAssociatedConceptsId(thesaurusConcept);
		JaxbList<String> associatedConceptsIds = new JaxbList<String>();
		for (String associatedConceptId : associations) {
			associatedConceptsIds.getList().add(associatedConceptId);
		}
		return associatedConceptsIds;
	}




}
