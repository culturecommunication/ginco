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
package fr.mcc.ginco.audit.readers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.audit.csv.JournalEventsEnum;
import fr.mcc.ginco.audit.csv.JournalLine;
import fr.mcc.ginco.audit.csv.JournalLineBuilder;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;

@Service("thesaurusTermAuditReader")
public class ThesaurusTermAuditReader {

	@Inject
	@Named("auditQueryBuilder")
	private AuditQueryBuilder auditQueryBuilder;

	@Inject
	@Named("journalLineBuilder")
	private JournalLineBuilder journalLineBuilder;

	public List<JournalLine> getTermAdded(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {

		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		AuditQuery termQuery = auditQueryBuilder.getEntityAddedQuery(reader,
				thesaurus, startDate, ThesaurusTerm.class);
		List<Object[]> allTermRevisions = termQuery.getResultList();
		for (Object[] revisionData : allTermRevisions) {
			JournalLine journal = journalLineBuilder.buildLineBase(
					JournalEventsEnum.THESAURUSTERM_CREATED,
					(GincoRevEntity) revisionData[1]);
			ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
			journal.setTermId(term.getIdentifier());
			journal.setNewLexicalValue(term.getLexicalValue());
			if (term.getConcept() != null) {
				journal.setConceptId(term.getConcept().getIdentifier());
			}
			allEvents.add(journal);
		}
		return allEvents;
	}

	public List<JournalLine> getTermRoleChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {

		List<JournalLine> allEvents = new ArrayList<JournalLine>();
		AuditQuery termRoleChangedQuery = auditQueryBuilder
				.getPropertyChangedQuery(reader, thesaurus, startDate,
						ThesaurusTerm.class, "prefered");

		List<Object[]> allRoleChanges = termRoleChangedQuery.getResultList();
		for (Object[] revisionData : allRoleChanges) {
			JournalLine journal = journalLineBuilder.buildLineBase(
					JournalEventsEnum.THESAURUSTERM_ROLE_UPDATE,
					(GincoRevEntity) revisionData[1]);
			ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
			journal.setTermId(term.getIdentifier());
			journal.setTermRole(term.getPrefered() ? "TP" : "TNP");
			journal.setConceptId(term.getConcept().getIdentifier());

			allEvents.add(journal);
		}
		return allEvents;
	}

	public List<JournalLine> getTermLexicalValueChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		AuditQuery lexicalValueChangedQuery = auditQueryBuilder
				.getPropertyChangedQuery(reader, thesaurus, startDate,
						ThesaurusTerm.class, "lexicalValue");

		List<Object[]> allLexicalValueChanges = lexicalValueChangedQuery
				.getResultList();
		for (Object[] revisionData : allLexicalValueChanges) {
			JournalLine journal = journalLineBuilder.buildLineBase(
					JournalEventsEnum.THESAURUSTERM_LEXICAL_VALUE_UPDATE,
					(GincoRevEntity) revisionData[1]);
			ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
			journal.setTermId(term.getIdentifier());
			if (term.getConcept() != null) {
				journal.setConceptId(term.getConcept().getIdentifier());
			}
			journal.setNewLexicalValue(term.getLexicalValue());
			AuditQuery previousElementQuery = auditQueryBuilder
					.getPreviousVersionQuery(reader, ThesaurusTerm.class,
							term.getIdentifier(),
							((GincoRevEntity) revisionData[1]).getId());
			Number previousRevision = (Number) previousElementQuery
					.getSingleResult();
			if (previousRevision != null) {
				ThesaurusTerm previousTerm = reader.find(ThesaurusTerm.class,
						term.getIdentifier(), previousRevision);
				journal.setOldLexicalValue(previousTerm.getLexicalValue());
			}
			allEvents.add(journal);
		}
		return allEvents;
	}

	public List<JournalLine> getTermAttachmentChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		List<JournalLine> allEvents = new ArrayList<JournalLine>();
		AuditQuery termAttachedQuery = auditQueryBuilder
				.getPropertyChangedQuery(reader, thesaurus, startDate,
						ThesaurusTerm.class, "concept");

		List<Object[]> allTermAttached = termAttachedQuery.getResultList();
		for (Object[] revisionData : allTermAttached) {
			JournalLine journal = journalLineBuilder.buildLineBase(
					JournalEventsEnum.THESAURUSTERM_LINKED_TO_CONCEPT,
					(GincoRevEntity) revisionData[1]);
			ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
			journal.setTermId(term.getIdentifier());
			journal.setConceptId(term.getConcept().getIdentifier());
			journal.setNewLexicalValue(term.getLexicalValue());
			journal.setOldLexicalValue(term.getLexicalValue());
			allEvents.add(journal);
		}
		return allEvents;

	}
}
