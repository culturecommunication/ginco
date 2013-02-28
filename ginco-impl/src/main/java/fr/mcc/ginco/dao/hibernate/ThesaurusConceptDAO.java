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

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<ThesaurusConcept> getRootConcepts(String thesaurusId, boolean searchOrphans) {
        return getChildrenConcepts(null, thesaurusId, searchOrphans);
    }

    @Override
    public List<ThesaurusConcept> getChildrenConcepts(String conceptId, String thesaurusId) {
        return getChildrenConcepts(conceptId, thesaurusId, true);
    }

    @Override
    public List<ThesaurusConcept> getChildrenConcepts(String conceptId, String thesaurusId, boolean searchOrhapns) {
        Criteria criteria = getCurrentSession().createCriteria(
                ThesaurusConcept.class, "tc");

        if(conceptId == null || conceptId.isEmpty()) {
            criteria.add(Restrictions.eq("thesaurus.identifier", thesaurusId));
            criteria.add(Restrictions.or(Restrictions.isNull("tc.parentConcepts"),
                    Restrictions.isEmpty("tc.parentConcepts")));
        } else {
            criteria.createCriteria("tc.parentConcepts", "pc")
                    .add(Restrictions.eq("pc.identifier",
                            conceptId));
        }

        if(!searchOrhapns) {
            criteria.add(Restrictions.eq("topConcept",true));
        }


        return criteria.list();
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
		criteria.add(Restrictions.eq("tc.thesaurus.identifier",
				thesaurus.getId()));
		criteria.add(Restrictions.eq("topConcept", topConcept));
		return criteria;
	}
}