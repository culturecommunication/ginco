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
import javax.persistence.NoResultException;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.audit.RevisionLine;
import fr.mcc.ginco.audit.RevisionLineBuilder;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.TechnicalException;

/**
 * Queries the thesaurus term audit tables and build RevisionLines
 *
 */
@Service("thesaurusTermAuditReader")
public class ThesaurusTermAuditReader {

	@Inject
	@Named("auditQueryBuilder")
	private AuditQueryBuilder auditQueryBuilder;

	private RevisionLineBuilder revisionLineBuilder;

	public void setRevisionLineBuilder(RevisionLineBuilder revisionLineBuilder) {
		this.revisionLineBuilder = revisionLineBuilder;
	}

	/**
	 * Builds the revision lines matching the events of term creation
	 * 
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @return
	 */
	public List<RevisionLine> getTermAdded(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		return getTermAdded(reader, thesaurus, startDate, null);		
	}
	
	/**
	 * Builds the revision lines matching the events of term creation in a given language
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @param lang
	 * @return
	 */
	public List<RevisionLine> getTermAdded(AuditReader reader,
			Thesaurus thesaurus, Date startDate, Language lang) {

		List<RevisionLine> allEvents = new ArrayList<RevisionLine>();

		try {
			AuditQuery termQuery = auditQueryBuilder.getEntityAddedQuery(
					reader, thesaurus, startDate, ThesaurusTerm.class);
			if (lang != null) {
				auditQueryBuilder.addFilterOnLanguage(termQuery, lang);
			}
			List<Object[]> allTermRevisions = termQuery.getResultList();
			for (Object[] revisionData : allTermRevisions) {
				RevisionLine line = revisionLineBuilder
						.buildTermAddedLine(revisionData);
				allEvents.add(line);
			}
		} catch (AuditException ae) {
			throw new TechnicalException("Error getting term creationevent ",
					ae);
		}
		return allEvents;
	}	

	/**
	 * Builds the revision lines matching the events of term role change
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @return
	 */
	public List<RevisionLine> getTermRoleChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		return getTermRoleChanged(reader, thesaurus, startDate, null);		
	}
	
	/**
	 * Builds the revision lines matching the events of term role change for terms in the given language
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @param lang the language of the terms we are searching on
	 * @return
	 */
	public List<RevisionLine> getTermRoleChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate, Language lang) {

		List<RevisionLine> allEvents = new ArrayList<RevisionLine>();

		try {
			AuditQuery termRoleChangedQuery = auditQueryBuilder
					.getPropertyChangedQueryOnUpdate(reader, thesaurus, startDate,
							ThesaurusTerm.class, "prefered");
			if (lang != null) {
				auditQueryBuilder.addFilterOnLanguage(termRoleChangedQuery, lang);
			}
			List<Object[]> allRoleChanges = termRoleChangedQuery
					.getResultList();
			for (Object[] revisionData : allRoleChanges) {
				RevisionLine journal = revisionLineBuilder
						.buildTermRoleChangedLine(revisionData);
				allEvents.add(journal);
			}
		} catch (AuditException ae) {
			throw new TechnicalException(
					"Error getting term role changed event ", ae);
		}
		return allEvents;
	}

	/**
	 * Builds the revision lines matching the events of term lexical value change
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @return =
	 */
	public List<RevisionLine> getTermLexicalValueChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		return getTermLexicalValueChanged(reader, thesaurus, startDate, null);	
	}
	
	/**
	 * Builds the revision lines matching the events of term lexical value change for terms in the given language
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @param lang the language of the terms we are searching on
	 * @return
	 */
	public List<RevisionLine> getTermLexicalValueChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate, Language lang) {
		List<RevisionLine> allEvents = new ArrayList<RevisionLine>();

		try {
			AuditQuery lexicalValueChangedQuery = auditQueryBuilder
					.getPropertyChangedQueryOnUpdate(reader, thesaurus, startDate,
							ThesaurusTerm.class, "lexicalValue");
			if (lang != null) {
				auditQueryBuilder.addFilterOnLanguage(lexicalValueChangedQuery, lang);
			}
			List<Object[]> allLexicalValueChanges = lexicalValueChangedQuery
					.getResultList();
			for (Object[] revisionData : allLexicalValueChanges) {
				ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
				String oldLexicalValue = "";
				AuditQuery previousElementQuery = auditQueryBuilder
						.getPreviousVersionQuery(reader, ThesaurusTerm.class,
								term.getIdentifier(),
								((GincoRevEntity) revisionData[1]).getId());
				Number previousRevision = (Number) previousElementQuery
						.getSingleResult();
				if (previousRevision != null) {
					ThesaurusTerm previousTerm = reader.find(
							ThesaurusTerm.class, term.getIdentifier(),
							previousRevision);
					oldLexicalValue = previousTerm.getLexicalValue();
				}

				List<RevisionLine> lines = revisionLineBuilder
						.buildTermLexicalValueChangedLine(revisionData,
								oldLexicalValue);

				allEvents.addAll(lines);
			}
		} catch (AuditException ae) {
			throw new TechnicalException(
					"Error getting term lexical value changed event ", ae);
		}
		return allEvents;
	}


	/**
	 * Builds the revision lines matching the events of term attachment to concept change for terms
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @return
	 */
	public List<RevisionLine> getTermAttachmentChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		return getTermAttachmentChanged(reader, thesaurus, startDate, null);		
	}
	
	/**
	 * Builds the revision lines matching the events of term attachment to concept change for terms in the given language
	 * @param reader
	 * @param thesaurus the thesaurus we are searching in 
	 * @param startDate the start date of events
	 * @param lang the language of the terms we are searching on
	 * @return
	 */
	public List<RevisionLine> getTermAttachmentChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate, Language lang) {
		List<RevisionLine> allEvents = new ArrayList<RevisionLine>();

		try {
			AuditQuery termAttachedQuery = auditQueryBuilder
					.getPropertyChangedQueryOnUpdateAndAdd(reader, thesaurus, startDate,
							ThesaurusTerm.class, "concept");
			if (lang != null) {
				auditQueryBuilder.addFilterOnLanguage(termAttachedQuery, lang);
			}
			List<Object[]> allTermAttached = termAttachedQuery.getResultList();	
			
			for (Object[] revisionData : allTermAttached) {
				ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
				if (term.getConcept() != null) {			
					AuditQuery previousPreferedQuery = auditQueryBuilder
							.getPreviousPreferredTermQuery(reader, ((GincoRevEntity) revisionData[1]).getId(),
									term.getConcept().getIdentifier());
					ThesaurusTerm previousPreferredTerm = null;
						List<Object[]> previousRevision = previousPreferedQuery
							.getResultList();
						if (previousRevision.size()>0) {
							previousPreferredTerm = (ThesaurusTerm) previousRevision.get(previousRevision.size()-1)[0];
						}
					
					List<RevisionLine> journals = revisionLineBuilder
							.buildTermAttachmentChangedLine(revisionData, previousPreferredTerm);
					allEvents.addAll(journals);
				}
			}
		} catch (AuditException ae) {
			throw new TechnicalException(
					"Error getting term attachment changed event ", ae);
		}
		return allEvents;
	}
}
