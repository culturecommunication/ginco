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

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exceptions.BusinessException;

/**
 * Implementation of the data access object to the thesaurus_term database table
 *
 */
@Repository("thesaurusTermDAO")
public class ThesaurusTermDAO extends
		GenericHibernateDAO<ThesaurusTerm, String> implements IThesaurusTermDAO {
	public ThesaurusTermDAO(Class<ThesaurusTerm> clazz) {
		super(clazz);
	}

	public ThesaurusTermDAO() {
		super(ThesaurusTerm.class);
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.IThesaurusTermDAO#findPaginatedItems(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<ThesaurusTerm> findPaginatedSandboxedItems(Integer start, Integer limit,
			String idThesaurus) {
		return getCurrentSession().createCriteria(ThesaurusTerm.class)
				.setMaxResults(limit)
				.add(Restrictions.eq("thesaurusId.identifier", idThesaurus))
				.add(Restrictions.isNull("conceptId"))
				.setFirstResult(start).addOrder(Order.asc("lexicalValue"))
				.list();
	}

	@Override
	public Long countSandboxedTerms(String idThesaurus) throws BusinessException {
		return (Long) getCurrentSession()
				.createCriteria(ThesaurusTerm.class)
				.add(Restrictions.eq("thesaurusId.identifier", idThesaurus))
				.add(Restrictions.isNull("conceptId"))
				.setProjection(Projections.rowCount())
				.list().get(0);
	}
	
	@Override
	public ThesaurusTerm getConceptPreferredTerm(String conceptId) throws BusinessException {
        List<ThesaurusTerm> list = getCurrentSession()
                .createCriteria(ThesaurusTerm.class)
                .add(Restrictions.eq("conceptId.identifier", conceptId))
                .add(Restrictions.eq("prefered", Boolean.TRUE))
                .list();

        if(list.size() == 0) {
            throw new BusinessException("No preferred term found ! " +
                    "Please check your database !");
        }

        if(list.size() != 1) {
            throw new BusinessException("More than one preferred term found ! " +
                    "Please check your database !");
        }

        return list.get(0);
	}
}