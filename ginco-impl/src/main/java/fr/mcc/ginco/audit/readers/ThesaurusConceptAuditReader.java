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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import fr.mcc.ginco.beans.ThesaurusConcept;

@Service("thesaurusConceptAuditReader")
public class ThesaurusConceptAuditReader{

	@Inject
	@Named("auditQueryBuilder")
	private AuditQueryBuilder auditQueryBuilder;

	@Inject
	@Named("journalLineBuilder")
	private JournalLineBuilder journalLineBuilder;

	public List<JournalLine> getConceptAdded(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		List<JournalLine> allEvents = new ArrayList<JournalLine>();
		AuditQuery conceptQuery = auditQueryBuilder.getEntityAddedQuery(reader,
				thesaurus, startDate, ThesaurusConcept.class);

		List<Object[]> allConceptRevisions = conceptQuery.getResultList();
		for (Object[] revisionData : allConceptRevisions) {
			JournalLine journal = journalLineBuilder.buildLineBase(
					JournalEventsEnum.THESAURUSCONCEPT_CREATED,
					(GincoRevEntity) revisionData[1]);
			journal.setConceptId(((ThesaurusConcept) revisionData[0])
					.getIdentifier());
			allEvents.add(journal);
		}
		return allEvents;
	}

	public List<JournalLine> getConceptHierarchyChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		List<JournalLine> allEvents = new ArrayList<JournalLine>();
		AuditQuery conceptHierarchyQuery = auditQueryBuilder
				.getPropertyChangedQuery(reader, thesaurus, startDate,
						ThesaurusConcept.class, "parentConcepts");

		List<Object[]> allConceptHierarchyChanges = conceptHierarchyQuery
				.getResultList();
		for (Object[] revisionData : allConceptHierarchyChanges) {
			JournalLine journal = journalLineBuilder.buildLineBase(
					JournalEventsEnum.THESAURUSCONCEPT_HIERARCHY_UPDATE,
					(GincoRevEntity) revisionData[1]);
			ThesaurusConcept concept = (ThesaurusConcept) revisionData[0];
			journal.setConceptId(concept.getIdentifier());
			journal.setNewGenericTerm(getConceptIds(concept.getParentConcepts()));

			AuditQuery previousElementQuery = auditQueryBuilder
					.getPreviousVersionQuery(reader, ThesaurusConcept.class,
							concept.getIdentifier(),
							((GincoRevEntity) revisionData[1]).getId());
			Number previousRevision = (Number) previousElementQuery
					.getSingleResult();
			if (previousRevision != null) {
				ThesaurusConcept previousConcept = reader.find(
						ThesaurusConcept.class, concept.getIdentifier(),
						previousRevision);
				journal.setOldGenericTerm(getConceptIds(previousConcept
						.getParentConcepts()));
			}

			allEvents.add(journal);
		}
		return allEvents;
	}

	public List<JournalLine> getConceptStatusChanged(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {
		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		AuditQuery conceptStatusQuery = auditQueryBuilder
				.getPropertyChangedQuery(reader, thesaurus, startDate,
						ThesaurusConcept.class, "status");

		List<Object[]> allConceptStatusChanges = conceptStatusQuery
				.getResultList();
		for (Object[] revisionData : allConceptStatusChanges) {
			JournalLine journal = journalLineBuilder.buildLineBase(
					JournalEventsEnum.THESAURUSCONCEPT_STATUS_UPDATE,
					(GincoRevEntity) revisionData[1]);
			ThesaurusConcept concept = (ThesaurusConcept) revisionData[0];
			journal.setConceptId(concept.getIdentifier());
			journal.setStatus(concept.getStatus());
			allEvents.add(journal);
		}
		return allEvents;
	}

	private Set<String> getConceptIds(Set<ThesaurusConcept> concepts) {
		Set<String> conceptIds = new HashSet<String>();
		for (ThesaurusConcept concept : concepts) {
			conceptIds.add(concept.getIdentifier());
		}
		return conceptIds;
	}

}
