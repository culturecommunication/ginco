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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exceptions.BusinessException;

/**
 * Implementation of the data access object to the thesaurus_term database table
 * 
 */
@Repository("thesaurusConceptDAO")
public class ThesaurusConceptDAO extends
		GenericHibernateDAO<ThesaurusConcept, String> implements
		IThesaurusConceptDAO {

	public ThesaurusConceptDAO(Class<ThesaurusConcept> clazz) {
		super(clazz);
	}

	public ThesaurusConceptDAO() {
		super(ThesaurusConcept.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mcc.ginco.dao.IThesaurusConceptDAO#getOrphansThesaurusConcept
	 */
	@Override
	public List<ThesaurusConcept> getOrphansThesaurusConcept(Thesaurus thesaurus)
			throws BusinessException {
		return getListByThesaurusAndTopConcept(thesaurus, false);
	}
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.IThesaurusConceptDAO#getOrphansThesaurusConceptCount(fr.mcc.ginco.beans.Thesaurus)
	 */
	@Override
	public long getOrphansThesaurusConceptCount(Thesaurus thesaurus) throws BusinessException {
		return getListByThesaurusAndTopConceptCount(thesaurus, false);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mcc.ginco.dao.IThesaurusConceptDAO#getTopTermThesaurusConcept
	 */
	@Override
	public List<ThesaurusConcept> getTopTermThesaurusConcept(Thesaurus thesaurus)
			throws BusinessException {
		return getListByThesaurusAndTopConcept(thesaurus, true);
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.IThesaurusConceptDAO#getTopTermThesaurusConceptCount(fr.mcc.ginco.beans.Thesaurus)
	 */
	@Override
	public long getTopTermThesaurusConceptCount(Thesaurus thesaurus) throws BusinessException {
		return getListByThesaurusAndTopConceptCount(thesaurus, true);
	}

    @Override
    public List<ThesaurusConcept> getRootConcepts(String thesaurusId, Boolean searchOrphans) {
        return getConcepts(null, thesaurusId, searchOrphans);
    }

    @Override
    public List<ThesaurusConcept> getChildrenConcepts(String conceptId) {
        return getConcepts(conceptId, null, null);
    }

    public List<ThesaurusConcept> getConcepts(String conceptId, String thesaurusId, Boolean searchOrphans) {
        Criteria criteria = getCurrentSession().createCriteria(
                ThesaurusConcept.class, "tc");

        if ((conceptId != null && !conceptId.isEmpty()) &&
            (thesaurusId != null && !thesaurusId.isEmpty()))
        {
            selectRoot(criteria, thesaurusId, conceptId);
        }
        else if (conceptId == null || conceptId.isEmpty()) {
            selectRoot(criteria, thesaurusId);
        }
        else {
            criteria.createCriteria("tc.parentConcepts", "pc")
                    .add(Restrictions.eq("pc.identifier",
                            conceptId));
        }

        selectOrphans(criteria, searchOrphans);

        return criteria.list();
    }

    @Override
    public List<ThesaurusConcept> getAllConceptsByThesaurusId(String excludeConceptId, String thesaurusId, Boolean searchOrphans) {
        Criteria criteria = getCurrentSession().createCriteria(
                ThesaurusConcept.class, "tc");

        selectThesaurus(criteria, thesaurusId);
        selectOrphans(criteria, searchOrphans);
        excludeConcept(criteria, excludeConceptId);

        return criteria.list();
    }
    
    @Override
    public List<ThesaurusConcept> getAssociatedConcepts(ThesaurusConcept concept) {
    
        DetachedCriteria d1 = DetachedCriteria.forClass(AssociativeRelationship.class, "ar1");
		d1.setProjection(Projections.projectionList().add(Projections.property("ar1.concept1")));
		d1.add(Restrictions.eq("concept2", concept));
		
		DetachedCriteria d2 = DetachedCriteria.forClass(AssociativeRelationship.class, "ar2");
		d2.setProjection(Projections.projectionList().add(Projections.property("ar2.concept2")));
		d2.add(Restrictions.eq("concept1", concept));
			
		Criteria criteria = getCurrentSession().createCriteria(ThesaurusConcept.class, "tc")
		.add(Restrictions.or(
				Subqueries.propertyIn("tc.identifier", d1),
				Subqueries.propertyIn("tc.identifier", d2)));		

        return criteria.list();
    }

    /**
     * Selects TopTerm concepts by ThesaurusId without excluding.
     * @param criteria
     * @param thesaurusId
     */
    private void selectRoot(Criteria criteria, String thesaurusId) {
        selectRoot(criteria, thesaurusId, null);
    }

    /**
     * Selects TopTerm concepts by ThesaurusId with excluding.
     * @param criteria
     * @param thesaurusId
     * @param excludeId
     */
    private void selectRoot(Criteria criteria, String thesaurusId, String excludeId) {
        excludeConcept(criteria, excludeId);
        selectThesaurus(criteria, thesaurusId);
        selectNoParents(criteria);
    }

    private void selectNoParents(Criteria criteria) {
        criteria.add(Restrictions.or(Restrictions.isNull("tc.parentConcepts"),
                Restrictions.isEmpty("tc.parentConcepts")));
    }

    private void selectThesaurus(Criteria criteria, String thesaurusId) {
        criteria.add(Restrictions.eq("tc.thesaurus.identifier", (String)thesaurusId));
    }

    private void selectOrphans(Criteria criteria, Boolean searchOrphans) {
        if(searchOrphans != null) {
            criteria.add(Restrictions.eq("topConcept", !searchOrphans));
        }
    }

    private void excludeConcept(Criteria criteria, String excludeId) {
        if(excludeId != null && !excludeId.isEmpty()) {
            criteria.add(Restrictions.not
                    (Restrictions.eq("tc.identifier", excludeId)));
        }
    }

    private List<ThesaurusConcept> getListByThesaurusAndTopConcept(
			Thesaurus thesaurus, boolean topConcept) throws BusinessException {

		if (thesaurus == null) {
			throw new BusinessException("Object thesaurus can't be null !", "empty-thesaurus");
		}

		return getCriteriaByThesaurusAndTopConcept(thesaurus, topConcept)
				.list();
	}

	private long getListByThesaurusAndTopConceptCount(Thesaurus thesaurus,
			boolean topConcept) throws BusinessException {

		if (thesaurus == null) {
			throw new BusinessException("Object thesaurus can't be null !", "empty-thesaurus");
		}
		Criteria crit = getCriteriaByThesaurusAndTopConcept(thesaurus,
				topConcept).setProjection(Projections.rowCount());
		return (Long) crit.list().get(0);
	}

	private Criteria getCriteriaByThesaurusAndTopConcept(Thesaurus thesaurus,
			boolean topConcept) {

		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusConcept.class, "tc");
		selectThesaurus(criteria, thesaurus.getIdentifier());
        selectOrphans(criteria, !topConcept);
        //criteria.add(Restrictions.eq("topConcept", topConcept));
        selectNoParents(criteria);
		return criteria;
	}
}