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
package fr.mcc.ginco.audit;

import java.util.List;
import java.util.Set;

import fr.mcc.ginco.audit.csv.JournalEventsEnum;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.ThesaurusTerm;

/**
 * Exposes methods to build revision lines for shared events between the
 * different revision export types
 * 
 */
public interface RevisionLineBuilder {

	/**
	 * Builds the basic common informations of a revision line
	 * 
	 * @param event
	 * @param gincoRevEntity
	 * @return
	 */
	RevisionLine buildLineBase(JournalEventsEnum event,
			GincoRevEntity gincoRevEntity);

	/**
	 * Builds the revision line for the event of term creation
	 * 
	 * @param revisionData
	 * @return
	 */
	RevisionLine buildTermAddedLine(Object[] revisionData);

	/***
	 * Builds the revision line for the event of term role change (preferred/non
	 * preferred)
	 * 
	 * @param revisionData
	 * @return
	 */
	RevisionLine buildTermRoleChangedLine(Object[] revisionData);

	/**
	 * Builds the list of revision lines for the event of term lexical value
	 * change
	 * 
	 * @param revisionData
	 * @param oldLexicalValue
	 * @return
	 */
	List<RevisionLine> buildTermLexicalValueChangedLine(Object[] revisionData,
			String oldLexicalValue);

	/**
	 * Builds the list of revision lines for the event of term attachment to a
	 * concept change
	 * 
	 * @param revisionData
	 * @param preferredTerm preferred term of the term concept in the previous version
	 * @return
	 */
	List<RevisionLine> buildTermAttachmentChangedLine(Object[] revisionData, ThesaurusTerm preferredTerm);

	/**
	 * Builds the list of revision lines for the event of a concept hierarchy
	 * change
	 * 
	 * @param revisionData
	 * @param oldGenericConceptIds
	 * @param languageId
	 * @return
	 */
	List<RevisionLine> buildConceptHierarchyChanged(Object[] revisionData,
			Set<String> oldGenericConceptIds, String languageId);

}
