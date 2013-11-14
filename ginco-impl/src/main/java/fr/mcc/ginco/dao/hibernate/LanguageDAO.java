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
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.dao.ILanguageDAO;

/**
 * Implementation of {@link ILanguageDAO}; basic class for DAO-related work.
 */
@Repository("languagesDAO")
public class LanguageDAO extends GenericHibernateDAO<Language, String>
		implements ILanguageDAO {
	
	private Logger logger  = LoggerFactory.getLogger(LanguageDAO.class);

	
	public LanguageDAO() {
		super(Language.class);
	}

	/**
	 * @return List of Language with favorites Language first, and the other
	 *         elements sorted alphabetically with a starting index and a limit
	 *         of items to be returned
	 */
	@Override
	public List<Language> findPaginatedItems(Integer start, Integer limit) {
		return getCurrentSession().createCriteria(Language.class)
				.setMaxResults(limit).setFirstResult(start)
				.addOrder(Order.desc("topLanguage"))
				.addOrder(Order.asc("refname")).list();
	}

	/**
	 * @return List of top Languages
	 */
	@Override
	public List<Language> findTopLanguages() {
		return getCurrentSession().createCriteria(Language.class)
				.add(Restrictions.eq("topLanguage", true)).list();
	}

	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.ILanguageDAO#getByPart1(java.lang.String)
	 */
	@Override
	public Language getByPart1(String part1) {
		List<Language> languages = getCurrentSession()
				.createCriteria(Language.class)
				.add(Restrictions.eq("part1", part1))
				.add(Restrictions.eq("principalLanguage", true)).list();
		if (languages != null && languages.size() > 0) {
			if(languages.size() > 01) {
				logger.warn("Multiple principal languages found for the same part1 " + part1);
			}
			return languages.get(0);
		}
		return null;
	}

}