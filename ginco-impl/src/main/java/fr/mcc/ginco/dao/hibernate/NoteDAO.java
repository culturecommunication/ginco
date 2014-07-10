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

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.INoteDAO;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link INoteDAO}; basic class for DAO-related work.
 */
@Repository
public class NoteDAO extends GenericHibernateDAO<Note, String>
		implements INoteDAO {

	public NoteDAO() {
		super(Note.class);
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.INoteDAO#findConceptNotes(java.lang.String)
	 */
	@Override
	public List<Note> findConceptPaginatedNotes(String conceptId, Integer startIndex, Integer limit) {
		return getCurrentSession().createCriteria(Note.class)
				.setMaxResults(limit)
				.add(Restrictions.eq("concept.identifier", conceptId))
				.setFirstResult(startIndex).addOrder(Order.asc("lexicalValue"))
				.list();
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.INoteDAO#findTermNotes(java.lang.String)
	 */
	@Override
	public List<Note> findTermPaginatedNotes(String termId, Integer startIndex, Integer limit) {
		return getCurrentSession().createCriteria(Note.class)
				.setMaxResults(limit)
				.add(Restrictions.eq("term.identifier", termId))
				.setFirstResult(startIndex).addOrder(Order.asc("lexicalValue"))
				.list();
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.INoteDAO#getConceptNoteCount(java.lang.String)
	 */
	@Override
	public Long getConceptNoteCount(String conceptId) {
		return (Long) getCurrentSession()
				.createCriteria(Note.class)
				.add(Restrictions.eq("concept.identifier", conceptId))
				.setProjection(Projections.rowCount())
				.list().get(0);
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.INoteDAO#getTermNoteCount(java.lang.String)
	 */
	@Override
	public Long getTermNoteCount(String termId) {
		return (Long) getCurrentSession()
				.createCriteria(Note.class)
				.add(Restrictions.eq("term.identifier", termId))
				.setProjection(Projections.rowCount())
				.list().get(0);
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.dao.INoteDAO#findNotesByThesaurusId(java.lang.String)
	 */
	@Override
	public List<Note> findNotesByThesaurusId(String thesaurusId) {

		DetachedCriteria conceptCriteria = DetachedCriteria.forClass(ThesaurusConcept.class, "tc")
				.add(Restrictions.eq("tc.thesaurus.identifier", thesaurusId))
				.setProjection(Projections.projectionList().add(Projections.property("tc.identifier")));

		DetachedCriteria termCriteria = DetachedCriteria.forClass(ThesaurusTerm.class, "tt")
				.add(Restrictions.eq("tt.thesaurus.identifier", thesaurusId))
				.setProjection(Projections.projectionList().add(Projections.property("tt.identifier")));

		Criteria criteria = getCurrentSession().createCriteria(Note.class, "tn")
				.add(Restrictions.or(
						Subqueries.propertyIn("tn.concept.identifier", conceptCriteria),
						Subqueries.propertyIn("tn.term.identifier", termCriteria)));

		return criteria.list();
	}
}
