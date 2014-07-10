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
package fr.mcc.ginco.audit.csv.readers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.audit.csv.JournalLine;
import fr.mcc.ginco.audit.csv.JournalLineBuilder;
import fr.mcc.ginco.audit.utils.AuditQueryBuilder;
import fr.mcc.ginco.audit.utils.AuditReaderService;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.TechnicalException;

/**
 * Queries the thesaurus term audit tables and build RevisionLines
 */
@Service("thesaurusTermAuditReader")
public class ThesaurusTermAuditReader {

	@Inject
	@Named("auditQueryBuilder")
	private AuditQueryBuilder auditQueryBuilder;

	@Inject
	@Named("auditReaderService")
	private AuditReaderService readerService;

	@Inject
	@Named("journalLineBuilder")
	private JournalLineBuilder journalLineBuilder;


	/**
	 * Builds the revision lines matching the events of term creation in a given language
	 *
	 * @param thesaurus the thesaurus we are searching in
	 * @param startDate the start date of events
	 * @return
	 */
	public List<JournalLine> getTermAdded(Thesaurus thesaurus, Date startDate) {

		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		try {
			AuditQuery termQuery = auditQueryBuilder.getEntityAddedQuery(
					thesaurus, startDate, ThesaurusTerm.class);

			List<Object[]> allTermRevisions = termQuery.getResultList();
			for (Object[] revisionData : allTermRevisions) {
				JournalLine line = journalLineBuilder
						.buildTermAddedLine((ThesaurusTerm) revisionData[0], (GincoRevEntity) revisionData[1]);
				allEvents.add(line);
			}
		} catch (AuditException ae) {
			throw new TechnicalException("Error getting term creationevent ",
					ae);
		}
		return allEvents;
	}


	/**
	 * Builds the revision lines matching the events of term role change for terms in the given language
	 *
	 * @param thesaurus the thesaurus we are searching in
	 * @param startDate the start date of events
	 * @return
	 */
	public List<JournalLine> getTermRoleChanged(Thesaurus thesaurus, Date startDate) {

		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		try {
			AuditQuery termRoleChangedQuery = auditQueryBuilder
					.getPropertyChangedQueryOnUpdate(thesaurus, startDate,
							ThesaurusTerm.class, "prefered");

			List<Object[]> allRoleChanges = termRoleChangedQuery
					.getResultList();
			for (Object[] revisionData : allRoleChanges) {
				ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
				GincoRevEntity revision = (GincoRevEntity) revisionData[1];
				JournalLine journal = journalLineBuilder
						.buildTermRoleChangedLine(term, revision);
				if (journal != null) {
					allEvents.add(journal);
				}
			}
		} catch (AuditException ae) {
			throw new TechnicalException(
					"Error getting term role changed event ", ae);
		}
		return allEvents;
	}


	/**
	 * Builds the revision lines matching the events of term lexical value change for terms in the given language
	 *
	 * @param thesaurus the thesaurus we are searching in
	 * @param startDate the start date of events
	 * @return
	 */
	public List<JournalLine> getTermLexicalValueChanged(Thesaurus thesaurus, Date startDate) {
		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		try {
			AuditQuery lexicalValueChangedQuery = auditQueryBuilder
					.getPropertyChangedQueryOnUpdate(thesaurus, startDate,
							ThesaurusTerm.class, "lexicalValue");
			List<Object[]> allLexicalValueChanges = lexicalValueChangedQuery
					.getResultList();
			for (Object[] revisionData : allLexicalValueChanges) {
				ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
				GincoRevEntity revision = (GincoRevEntity) revisionData[1];
				String oldLexicalValue = "";
				AuditQuery previousElementQuery = auditQueryBuilder
						.getPreviousVersionQuery(ThesaurusTerm.class,
								term.getIdentifier(),
								revision.getId());
				Number previousRevision = (Number) previousElementQuery
						.getSingleResult();
				if (previousRevision != null) {
					ThesaurusTerm previousTerm = readerService.getAuditReader().find(
							ThesaurusTerm.class, term.getIdentifier(),
							previousRevision);
					oldLexicalValue = previousTerm.getLexicalValue();
				}

				JournalLine line = journalLineBuilder
						.buildTermLexicalValueChangedLine(term, revision,
								oldLexicalValue);

				allEvents.add(line);
			}
		} catch (AuditException ae) {
			throw new TechnicalException(
					"Error getting term lexical value changed event ", ae);
		}
		return allEvents;
	}

	/**
	 * Builds the revision lines matching the events of term attachment to concept change for terms in the given language
	 *
	 * @param thesaurus the thesaurus we are searching in
	 * @param startDate the start date of events
	 * @return
	 */
	public List<JournalLine> getTermAttachmentChanged(Thesaurus thesaurus, Date startDate) {
		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		try {
			AuditQuery termAttachedQuery = auditQueryBuilder
					.getPropertyChangedQueryOnUpdate(thesaurus, startDate,
							ThesaurusTerm.class, "concept");

			List<Object[]> allTermAttached = termAttachedQuery.getResultList();

			for (Object[] revisionData : allTermAttached) {
				ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
				GincoRevEntity revision = (GincoRevEntity) revisionData[1];
				if (term.getConcept() != null) {
					AuditQuery previousPreferedQuery = auditQueryBuilder
							.getPreviousPreferredTermQuery(revision.getId(),
									term.getConcept().getIdentifier());
					ThesaurusTerm previousPreferredTerm = null;
					List<Object[]> previousRevision = previousPreferedQuery.getResultList();
					if (previousRevision.size() > 0) {
						previousPreferredTerm = (ThesaurusTerm) previousRevision.get(previousRevision.size() - 1)[0];
					}

					JournalLine journal = journalLineBuilder
							.buildTermAttachmentChangedLine(term, revision, previousPreferredTerm);
					allEvents.add(journal);
				}
			}
		} catch (AuditException ae) {
			throw new TechnicalException(
					"Error getting term attachment changed event ", ae);
		}
		return allEvents;
	}
}
