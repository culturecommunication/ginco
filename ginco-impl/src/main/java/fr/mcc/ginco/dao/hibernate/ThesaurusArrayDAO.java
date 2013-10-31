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
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;

/**
 * Implementation of {@link IThesaurusArrayDAO}
 */
@Repository("thesaurusArrayDAO")
public class ThesaurusArrayDAO extends GenericHibernateDAO<ThesaurusArray, String> implements IThesaurusArrayDAO  {

	public ThesaurusArrayDAO() {
		super(ThesaurusArray.class);
	}

    @Override
    public List<ThesaurusArray> getThesaurusArrayListByThesaurusId(String excludedConceptArrayId, String thesaurusId) {
        Criteria criteria = getCurrentSession().createCriteria(
                ThesaurusArray.class, "ta");

        selectThesaurus(criteria, thesaurusId);
        excludeAnArrayById(criteria, excludedConceptArrayId);
        return criteria.list();
    }

    private void selectThesaurus(Criteria criteria, String thesaurusId) {
        criteria.add(Restrictions.eq("ta.thesaurus.identifier", (String) thesaurusId));
    }

	@Override
	public List<ThesaurusArray> getConceptSuperOrdinateArrays(String conceptId) {
		Criteria criteria = getCriteriaBySuperOrdinate(conceptId);
	    return criteria.list();
	}

	@Override
	public List<ThesaurusArray> getConceptSuperOrdinateArrays(String conceptId, String excludeArrayId) {
		Criteria criteria = getCriteriaBySuperOrdinate(conceptId);
	    criteria.add(Restrictions.ne("ta.identifier", excludeArrayId));
	    return criteria.list();
	}

	private Criteria getCriteriaBySuperOrdinate(String conceptId) {
		Criteria criteria = getCurrentSession().createCriteria(
		         ThesaurusArray.class, "ta");
		criteria.add(Restrictions.eq("ta.superOrdinateConcept.identifier", conceptId));
		return criteria;
	}

    @Override
    public List<ThesaurusArray> getArraysWithoutSuperordinatedConcept(String thesaurusId) {
        Criteria criteria = getCurrentSession().createCriteria(ThesaurusArray.class, "ta");
        criteria.add(Restrictions.isNull("ta.superOrdinateConcept"));
        selectThesaurus(criteria, thesaurusId);
        return criteria.list();
    }

    private void excludeAnArrayById(Criteria criteria, String excludedConceptArrayId) {
        if (excludedConceptArrayId != null) {
        	criteria.add(Restrictions.ne("ta.identifier", (String) excludedConceptArrayId));
        }
    }

    @Override
    public List<ThesaurusArray> getArraysWithoutParentArray(String thesaurusId){
    	Criteria criteria = getCurrentSession().createCriteria(ThesaurusArray.class, "ta");
    	criteria.add(Restrictions.isNull("ta.parent.identifier"));
    	selectThesaurus(criteria, thesaurusId);
        return criteria.list();
    }
    @Override
	public List<ThesaurusArray> getChildrenArrays(String thesaurusArrayId){
    	Criteria criteria = getCurrentSession().createCriteria(ThesaurusArray.class, "ta");
    	criteria.add(Restrictions.eq("ta.parent.identifier", thesaurusArrayId));
    	return criteria.list();
    }

    @Override
	public Long countItems(String idThesaurus) {
		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusArray.class);
		criteria.add(Restrictions.eq("thesaurus.identifier", idThesaurus))
				.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}
}