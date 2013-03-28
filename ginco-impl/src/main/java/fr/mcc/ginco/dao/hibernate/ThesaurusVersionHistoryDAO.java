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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;

/**
 * Implementation of the data access object to the thesaurus_version_history database table
 *
 */
@Repository("thesaurusVersionHistoryDAO")
public class ThesaurusVersionHistoryDAO extends
		GenericHibernateDAO<ThesaurusVersionHistory, String> implements IThesaurusVersionHistoryDAO {	

	public ThesaurusVersionHistoryDAO() {
		super(ThesaurusVersionHistory.class);
	}

	@Override
	public List<ThesaurusVersionHistory> findVersionsByThesaurusId(
			String thesaurusId) {
		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusVersionHistory.class, "tv");
		selectThesaurus(criteria, thesaurusId);
		descOrderBy(criteria, "date");
		return criteria.list();
	}
	
	@Override
	public List<ThesaurusVersionHistory> findAllOtherThisVersionTrueByThesaurusId(
			String thesaurusId, String excludedVersionId) {
		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusVersionHistory.class, "tv");
		selectThesaurus(criteria, thesaurusId);
		excludeAVersion(criteria, excludedVersionId);
		isThisVersion(criteria, true);
		return criteria.list();
	}

	@Override
	public ThesaurusVersionHistory findThisVersionByThesaurusId(String thesaurusId) {
		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusVersionHistory.class, "tv");
		selectThesaurus(criteria, thesaurusId);
		isThisVersion(criteria, true);
		
		List<ThesaurusVersionHistory>foundVersions = criteria.list();
		if (foundVersions.size()>0) {
			return (ThesaurusVersionHistory) criteria.list().get(0);
		}
		return null;
	}

	private void selectThesaurus(Criteria criteria, String thesaurusId) {
        criteria.add(Restrictions.eq("tv.thesaurus.identifier", (String)thesaurusId));
    }
	
	private void excludeAVersion(Criteria criteria, String excludedVersionId) {
        criteria.add(Restrictions.ne("tv.identifier", (String)excludedVersionId));
    }
	
	private void isThisVersion(Criteria criteria, Boolean isThisVersion) {
        criteria.add(Restrictions.eq("tv.thisVersion", isThisVersion));
    }
	
	private void descOrderBy(Criteria criteria, String sortColumn) {
        criteria.addOrder(Order.desc(sortColumn));
    }

}