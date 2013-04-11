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
package fr.mcc.ginco.audit.csv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import fr.mcc.ginco.audit.RevisionLine;
import fr.mcc.ginco.audit.RevisionLineBuilder;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;

/**
 * Implementation of {@link RevisionLineBuilder} for CSV export
 *
 */
@Service("journalLineBuilder")
public class JournalLineBuilder implements RevisionLineBuilder {

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.csv.RevisionLineBuilder#buildLineBase(fr.mcc.ginco.audit.csv.JournalEventsEnum, fr.mcc.ginco.beans.GincoRevEntity)
	 */
	public JournalLine buildLineBase(JournalEventsEnum event,
			GincoRevEntity gincoRevEntity) {
		JournalLine journal = new JournalLine();
		journal.setEventType(event);
		journal.setEventDate(gincoRevEntity.getRevisionDate());
		journal.setAuthorId(gincoRevEntity.getUsername());
		return journal;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.csv.RevisionLineBuilder#buildTermAddedLine(java.lang.Object[])
	 */
	@Override
	public RevisionLine buildTermAddedLine(Object[] revisionData) {
		JournalLine journalLine = buildLineBase(
				JournalEventsEnum.THESAURUSTERM_CREATED,
				(GincoRevEntity) revisionData[1]);
		ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
		journalLine.setTermId(term.getIdentifier());
		journalLine.setNewLexicalValue(term.getLexicalValue());
		if (term.getConcept() != null) {
			journalLine.setConceptId(term.getConcept().getIdentifier());
		}
		return journalLine;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.csv.RevisionLineBuilder#buildTermRoleChangedLine(java.lang.Object[])
	 */
	@Override
	public RevisionLine buildTermRoleChangedLine(Object[] revisionData) {
		JournalLine journal = buildLineBase(
				JournalEventsEnum.THESAURUSTERM_ROLE_UPDATE,
				(GincoRevEntity) revisionData[1]);
		ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
		journal.setTermId(term.getIdentifier());
		journal.setTermRole(term.getPrefered() ? "TP" : "TNP");
		journal.setConceptId(term.getConcept().getIdentifier());
		return journal;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.csv.RevisionLineBuilder#buildTermLexicalValueChangedLine(java.lang.Object[], java.lang.String)
	 */
	@Override
	public List<RevisionLine> buildTermLexicalValueChangedLine(Object[] revisionData,
			String oldLexicalValue) {
		List<RevisionLine> allLines = new ArrayList<RevisionLine>();
		JournalLine journal = buildLineBase(
				JournalEventsEnum.THESAURUSTERM_LEXICAL_VALUE_UPDATE,
				(GincoRevEntity) revisionData[1]);
		ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
		journal.setTermId(term.getIdentifier());
		if (term.getConcept() != null) {
			journal.setConceptId(term.getConcept().getIdentifier());
		}
		journal.setNewLexicalValue(term.getLexicalValue());
		journal.setOldLexicalValue(oldLexicalValue);

		allLines.add(journal);
		return allLines;
	}
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.csv.RevisionLineBuilder#buildTermAttachmentChangedLine(java.lang.Object[])
	 */
	@Override
	public List<RevisionLine> buildTermAttachmentChangedLine(Object[] revisionData, ThesaurusTerm preferredTerm) {
		List<RevisionLine> allLines = new ArrayList<RevisionLine>();
		ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
		if (term.getConcept() != null) {
			JournalLine journal = buildLineBase(
					JournalEventsEnum.THESAURUSTERM_LINKED_TO_CONCEPT,
					(GincoRevEntity) revisionData[1]);
			journal.setTermId(term.getIdentifier());
			journal.setConceptId(term.getConcept().getIdentifier());
			journal.setNewLexicalValue(term.getLexicalValue());
			journal.setOldLexicalValue(term.getLexicalValue());
			allLines.add(journal);
		}

		return allLines;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.csv.RevisionLineBuilder#buildConceptHierarchyChanged(java.lang.Object[], java.util.Set)
	 */
	@Override
	public List<RevisionLine> buildConceptHierarchyChanged(Object[] revisionData, Set<String> oldGenericConceptIds, String languageId) {
		List<RevisionLine> allLines = new ArrayList<RevisionLine>();
		JournalLine journal = buildLineBase(
				JournalEventsEnum.THESAURUSCONCEPT_HIERARCHY_UPDATE,
				(GincoRevEntity) revisionData[1]);
		ThesaurusConcept concept = (ThesaurusConcept) revisionData[0];
		journal.setConceptId(concept.getIdentifier());
		Set<ThesaurusConcept> parentConcepts = concept
				.getParentConcepts();
		Set<String> parentConceptIds = new HashSet<String>();
		for (ThesaurusConcept parentConcept:parentConcepts) {
			parentConceptIds.add(parentConcept.getIdentifier());
		}
		journal.setNewGenericTerm(parentConceptIds);	
		journal.setOldGenericTerm(oldGenericConceptIds);	

		allLines.add(journal);
		return allLines;
	}
	
}
