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

import java.io.Serializable;
import java.util.Date;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Thesaurus;

/**
 * This service is in charge of building AuditQuery
 * 
 */
@Service("auditQueryBuilder")
public class AuditQueryBuilder {

	/**
	 * Builds a query returning objects creations revisions
	 * 
	 * @param reader
	 * @param thesaurus
	 *            the thesaurus on which searched should be performed
	 * @param startDate
	 *            the start date for the search
	 * @param clazz
	 *            the object class
	 * @return
	 */
	public AuditQuery getEntityAddedQuery(AuditReader reader,
			Thesaurus thesaurus, Date startDate, Class clazz) {
		AuditQuery auditQuery = reader.createQuery().forRevisionsOfEntity(
				clazz, false, true);
		filterOnDateAndThesaurusId(auditQuery, thesaurus, startDate);
		auditQuery.add(AuditEntity.revisionType().eq(RevisionType.ADD));
		return auditQuery;
	}

	/**
	 * Builds a query returning objects property updates revisions
	 * 
	 * @param reader
	 * @param thesaurus
	 *            the thesaurus on which searched should be performed
	 * @param startDate
	 *            the start date for the search
	 * @param clazz
	 *            the object class
	 * @param propertyName
	 *            the audited property
	 * @return
	 */
	public AuditQuery getPropertyChangedQuery(AuditReader reader,
			Thesaurus thesaurus, Date startDate, Class clazz,
			String propertyName) {
		AuditQuery auditQuery = reader.createQuery().forRevisionsOfEntity(
				clazz, false, true);
		filterOnDateAndThesaurusId(auditQuery, thesaurus, startDate);
		auditQuery.add(AuditEntity.property(propertyName).hasChanged());
		auditQuery.add(AuditEntity.revisionType().eq(RevisionType.MOD));
		return auditQuery;
	}

	/**
	 * Builds the query to get the previous version 
	 * @param reader
	 * @param clazz
	 * @param identifier
	 * @param currentRevision
	 * @return
	 */
	public AuditQuery getPreviousVersionQuery(AuditReader reader, Class clazz,
			Serializable identifier, int currentRevision) {
		return reader.createQuery().forRevisionsOfEntity(clazz, false, true)
				.addProjection(AuditEntity.revisionNumber().max())
				.add(AuditEntity.id().eq(identifier))
				.add(AuditEntity.revisionNumber().lt(currentRevision));
	}

	/**
	 * Builds a query returning an object creation revision Assumes the id field
	 * of the object is named "identifier"
	 * 
	 * @param reader
	 * @param clazz
	 *            the object class
	 * @param identifier
	 *            the object identifier
	 * @return
	 */
	public AuditQuery getEntityAddedQuery(AuditReader reader, Class clazz,
			Serializable identifier) {
		return reader.createQuery().forRevisionsOfEntity(clazz, false, true)
				.add(AuditEntity.id().eq(identifier))
				.add(AuditEntity.revisionType().eq(RevisionType.ADD));

	}

	private void filterOnDateAndThesaurusId(AuditQuery query,
			Thesaurus thesaurus, Date startDate) {
		query.add(
				AuditEntity.revisionProperty("thesaurusId").eq(
						thesaurus.getIdentifier())).add(
				AuditEntity.revisionProperty("timestamp").ge(
						startDate.getTime()));
	}
}
