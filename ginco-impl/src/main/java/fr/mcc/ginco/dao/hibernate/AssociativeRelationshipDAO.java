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
package fr.mcc.ginco.dao.hibernate;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IAssociativeRelationshipDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of the data access object to the thesaurus_term database table
 */
@Repository
public class AssociativeRelationshipDAO extends
		GenericHibernateDAO<AssociativeRelationship, String> implements
		IAssociativeRelationshipDAO {


	public AssociativeRelationshipDAO() {
		super(AssociativeRelationship.class);
	}

	@Override
	public List<String> getAssociatedConcepts(ThesaurusConcept concept, ConceptStatusEnum status) {
		DetachedCriteria d1 = DetachedCriteria.forClass(AssociativeRelationship.class, "ar1");
		d1.setProjection(Projections.projectionList().add(Projections.property("ar1.identifier.concept2")));
		d1.add(Restrictions.eq("identifier.concept1", concept.getIdentifier()));

		DetachedCriteria d2 = DetachedCriteria.forClass(AssociativeRelationship.class, "ar2");
		d2.setProjection(Projections.projectionList().add(Projections.property("ar2.identifier.concept1")));
		d2.add(Restrictions.eq("identifier.concept2", concept.getIdentifier()));

		Criteria criteria = getCurrentSession().createCriteria(ThesaurusConcept.class, "tc")
				.add(Restrictions.or(
						Subqueries.propertyIn("tc.identifier", d1),
						Subqueries.propertyIn("tc.identifier", d2)))
				.setProjection(Projections.property("tc.identifier"));
		if (status!=null) {
			criteria.add(Restrictions.eq("tc.status",
					status.getStatus()));
		}

		return criteria.list();
	}
	
	@Override
	public List<String> getAssociatedConcepts(ThesaurusConcept concept) {
		return getAssociatedConcepts(concept, null);
	}


	@Override
	public List<AssociativeRelationship> getAssociationsForConcept(ThesaurusConcept concept) {

		Criteria criteria = getCurrentSession().createCriteria(AssociativeRelationship.class)
				.add(Restrictions.or(
						Restrictions.eq("conceptLeft.identifier", concept.getIdentifier()),
						Restrictions.eq("conceptRight.identifier", concept.getIdentifier())));
		return criteria.list();
	}

	@Override
	public AssociativeRelationship getAssociativeRelationship(String id1, String id2) {
		Criteria criteria = getCurrentSession().createCriteria(AssociativeRelationship.class, "ar")
				.add(Restrictions.or(
						Restrictions.and(Restrictions.eq("conceptLeft.identifier", id1), Restrictions.eq("conceptRight.identifier", id2)),
						Restrictions.and(Restrictions.eq("conceptRight.identifier", id1), Restrictions.eq("conceptLeft.identifier", id2))));
		List<AssociativeRelationship> res = criteria.list();
		if (res != null && !res.isEmpty()) {
			return res.get(0);
		}
		return null;
	}
}