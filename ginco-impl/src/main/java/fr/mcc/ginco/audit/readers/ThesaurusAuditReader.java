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
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.audit.csv.JournalEventsEnum;
import fr.mcc.ginco.audit.csv.JournalLine;
import fr.mcc.ginco.audit.csv.JournalLineBuilder;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.TechnicalException;

@Service("thesaurusAuditReader")
public class ThesaurusAuditReader {

	@Inject
	@Named("auditQueryBuilder")
	private AuditQueryBuilder auditQueryBuilder;

	@Inject
	@Named("journalLineBuilder")
	private JournalLineBuilder journalLineBuilder;

	public List<JournalLine> getThesaurusAdded(AuditReader reader,
			Thesaurus thesaurus, Date startDate) {

		List<JournalLine> allEvents = new ArrayList<JournalLine>();

		try {
			AuditQuery thesaurusQuery = auditQueryBuilder.getEntityAddedQuery(
					reader, thesaurus, startDate, Thesaurus.class);
			List<Object[]> allAddedThesaurusRevisions = thesaurusQuery
					.getResultList();

			for (Object[] revisionData : allAddedThesaurusRevisions) {
				JournalLine journal = journalLineBuilder.buildLineBase(
						JournalEventsEnum.THESAURUS_CREATED,
						(GincoRevEntity) revisionData[1]);
				allEvents.add(journal);
			}
		} catch (AuditException ae) {
			throw new TechnicalException(
					"Error getting thesaurus creation event", ae);
		}
		return allEvents;
	}

}
