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
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.UserRole;
import fr.mcc.ginco.dao.IUserRoleDAO;

/**
 * Implementation of the data access object to the user_role table
 * 
 */
@Repository("userRoleDAO")
public class UserRoleDAO extends GenericHibernateDAO<UserRole, Integer>
		implements IUserRoleDAO {

	private static final String USERNAME = "username";
	private static final String THESAURUS_IDENTIFIER = "thesaurus.identifier";

	public UserRoleDAO() {
		super(UserRole.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.dao.IUserRoleDAO#getUserRoleOnThesaurus(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public UserRole getUserRoleOnThesaurus(String username, String thesaurusId) {
		Criteria criteria = getCurrentSession().createCriteria(UserRole.class);
		criteria.add(Restrictions.eq(USERNAME, username));
		criteria.add(Restrictions.eq(THESAURUS_IDENTIFIER, thesaurusId));
		List<UserRole> userRoles = criteria.list();
		if (userRoles != null && !userRoles.isEmpty()) {
			return userRoles.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.IUserRoleDAO#getUserRolesOnThesaurus(java.lang.String)
	 */
	@Override
	public List<UserRole> getUserRolesOnThesaurus(String thesaurusId) {
		Criteria criteria = getCurrentSession().createCriteria(UserRole.class);
		criteria.add(Restrictions.eq(THESAURUS_IDENTIFIER, thesaurusId));
		return criteria.list();	
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.IUserRoleDAO#getUserRoles(java.lang.String)
	 */
	@Override
	public List<UserRole> getUserRoles(String username) {
		Criteria criteria = getCurrentSession().createCriteria(UserRole.class);
		criteria.add(Restrictions.eq(USERNAME, username));
		return criteria.list();
	}

}