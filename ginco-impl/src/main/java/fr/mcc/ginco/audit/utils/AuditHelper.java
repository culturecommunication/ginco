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
package fr.mcc.ginco.audit.utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;

@Service("auditHelper")
public class AuditHelper {
	
	@Inject
	@Named("auditQueryBuilder")
	private AuditQueryBuilder auditQueryBuilder;
	
	@Inject
	@Named("auditReaderService")
	private AuditReaderService reader;
	

	/**
	 * Gets the preferred term of the given concept at the given revision
	 * @param revisionNumber
	 * @param conceptId
	 * @param lang
	 * @return
	 */
	public ThesaurusTerm getPreferredTermAtRevision(Number revisionNumber, String conceptId, String lang) {
		AuditQuery query = reader.getAuditReader().createQuery().forEntitiesAtRevision(ThesaurusTerm.class, revisionNumber)
				.add(AuditEntity.relatedId("concept").eq(conceptId))
				.add(AuditEntity.property("prefered").eq(true));
		if (lang != null) {
			auditQueryBuilder.addFilterOnLanguage(query, lang);
		}
		if (query.getResultList().isEmpty()){
			return null;
		}
		else {
			return (ThesaurusTerm) query.getSingleResult();
		}
	}
	
	/**
	 * 
	 * Gets the list of ThesaurusConcept revisions where parent id is the conceptId
	 * @param revisionNumber
	 * @param conceptId
	 * @return
	 */
	public List<ThesaurusConcept> getConceptChildrenAtRevision(Number revisionNumber, ThesaurusConcept concept) {
		//This type of relation is not supported by Envers yet
		//AuditQuery query = reader.getAuditReader().createQuery().forEntitiesAtRevision(ThesaurusConcept.class, revisionNumber)
		//		.add(AuditEntity.relatedId("parentConcepts").eq(conceptId));
		List<ThesaurusConcept> childrenConceptAtRevision = new ArrayList<ThesaurusConcept>();
		AuditQuery query = reader.getAuditReader().createQuery().forEntitiesAtRevision(ThesaurusConcept.class, revisionNumber)
						.add(AuditEntity.relatedId("thesaurus").eq(concept.getThesaurus().getIdentifier()));
		List<ThesaurusConcept> allThesaurusConcepts = query.getResultList();
		for (ThesaurusConcept curConcept:allThesaurusConcepts) {
			if (curConcept.getParentConcepts().contains(concept)) {
				childrenConceptAtRevision.add(curConcept);
			}
		}
		return childrenConceptAtRevision;
	}
	
	public ThesaurusConcept  getConceptPreviousVersion(GincoRevEntity revision, String conceptId) {		
		AuditQuery previousElementQuery = auditQueryBuilder
			.getPreviousVersionQuery(ThesaurusConcept.class,
					conceptId, revision.getId());
		Number previousRevision = (Number) previousElementQuery
			.getSingleResult();
		if (previousRevision != null) {
			ThesaurusConcept previousConcept = reader.getAuditReader()
				.find(ThesaurusConcept.class,
						conceptId, previousRevision);
			return previousConcept;
		}
		return null;
	}	
	
	
	public List<ThesaurusTerm> getConceptTermsAtRevision(ThesaurusConcept conceptAtRevision, Number revision, String lang) {
		AuditQuery query = reader.getAuditReader().createQuery().forEntitiesAtRevision(
				ThesaurusTerm.class, revision)
				.add(AuditEntity.relatedId("concept").eq(conceptAtRevision.getIdentifier()));
		if (lang != null) {
			auditQueryBuilder.addFilterOnLanguage(query, lang);
		}
		return query.getResultList();
	}
}
