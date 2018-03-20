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

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;

/**
 * Implementation of the data access object to the thesaurus_term database table
 */
@Repository
public class ThesaurusConceptDAO extends
		GenericHibernateDAO<ThesaurusConcept, String> implements
		IThesaurusConceptDAO {

	private static final String AL_SOURCE_CONCEPT_IDENTIFIER = "al.sourceConcept.identifier";
	private static final String TC_IDENTIFIER = "tc.identifier";
	private static final String THESAURUS_IDENTIFIER = "thesaurus.identifier";

	@Value("${ginco.default.language}")
	private String defaultLang;

	public ThesaurusConceptDAO() {
		super(ThesaurusConcept.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.mcc.ginco.dao.IThesaurusConceptDAO#getOrphansThesaurusConcept
	 */
	@Override
	public List<ThesaurusConcept> getOrphansThesaurusConcept(
			Thesaurus thesaurus, int maxResults) {
		return getListByThesaurusAndTopConcept(thesaurus, false, maxResults,null,null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.mcc.ginco.dao.IThesaurusConceptDAO#getOrphansThesaurusConceptCount
	 * (fr.mcc.ginco.beans.Thesaurus)
	 */
	@Override
	public long getOrphansThesaurusConceptCount(Thesaurus thesaurus) {
		return getListByThesaurusAndTopConceptCount(thesaurus, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.mcc.ginco.dao.IThesaurusConceptDAO#getTopTermThesaurusConcept
	 */
	
	@Override
	public List<ThesaurusConcept> getTopTermThesaurusConcept(
			Thesaurus thesaurus, int maxResults,String like) {
		return getTopTermThesaurusConcept(thesaurus,  maxResults, like, null);
	}
	
	@Override
	public List<ThesaurusConcept> getTopTermThesaurusConcept(Thesaurus thesaurus, int maxResults, String like,
			ConceptStatusEnum status) {
		// TODO Auto-generated method stub
		return getListByThesaurusAndTopConcept(thesaurus, true, maxResults,like, status);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.mcc.ginco.dao.IThesaurusConceptDAO#getTopTermThesaurusConceptCount
	 * (fr.mcc.ginco.beans.Thesaurus)
	 */
	@Override
	public long getTopTermThesaurusConceptCount(Thesaurus thesaurus) {
		return getListByThesaurusAndTopConceptCount(thesaurus, true);
	}

	@Override
	public List<ThesaurusConcept> getRootConcepts(String thesaurusId,
	                                              Boolean searchOrphans) {
		return getConcepts(null, thesaurusId, searchOrphans, 0,null,null);
	}

	@Override
	public List<String> getIdentifiersOfConceptsWithChildren(String thesaurusId) {
		Query query = getCurrentSession().createSQLQuery("select tc.identifier"
				+ " from thesaurus_concept tc, hierarchical_relationship hr where"
				+ " hr.parentconceptid = tc.identifier and"
				+ " tc.thesaurusId = :pthesaurusid"
				+ " group by tc.identifier");
		query.setParameter("pthesaurusid", thesaurusId);
		return (List<String>) query.list();
	}
	
	@Override
	public List<ThesaurusConcept> getChildrenConcepts(String conceptId, int maxResults,String like, ConceptStatusEnum status) {
		return getConcepts(conceptId, null, null, maxResults,like, status);
	}
	
	@Override
	public List<ThesaurusConcept> getChildrenConcepts(String conceptId, int maxResults,String like) {
		return getConcepts(conceptId, null, null, maxResults,like, null);
	}

	private List<ThesaurusConcept> getConcepts(String conceptId, String thesaurusId,
			Boolean searchOrphans, int maxResults,String like, ConceptStatusEnum status) {
		
		Criteria criteria = getCurrentSession().createCriteria(ThesaurusConcept.class, "tc");
		
		if(null != like){
			//override previous criteria
			criteria = getCurrentSession().createCriteria(ThesaurusTerm.class, "tt")
					.add(Restrictions.isNotNull("tt.concept"))
					.createCriteria("concept", "tc", JoinType.RIGHT_OUTER_JOIN);
					
					criteria.setProjection(
							Projections
									.projectionList()
									.add(Projections.property("tt.lexicalValue"))
									.add(Projections.property("tc.identifier").as(
											"identifier"))).setResultTransformer(
							Transformers.aliasToBean(ThesaurusConcept.class));
			conceptNameIsLike(criteria,like);
		}
		
		if ((conceptId != null && !conceptId.isEmpty())
				&& (thesaurusId != null && !thesaurusId.isEmpty())) {
			selectRoot(criteria, thesaurusId, conceptId);
		} else if (conceptId == null || conceptId.isEmpty()) {
			selectRoot(criteria, thesaurusId);
		} else {
			criteria.createCriteria("tc.parentConcepts", "pc").add(
					Restrictions.eq("pc.identifier", conceptId));
		}

		selectOrphans(criteria, searchOrphans);
		selectStatus(criteria, status);
		if (maxResults > 0)
			criteria.setMaxResults(maxResults);
		return criteria.list();
	}

	@Override
	public List<ThesaurusConcept> getConceptsByThesaurusId(String excludeConceptId,
			String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts) {
		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusConcept.class, "tc");
		selectThesaurus(criteria, thesaurusId);
		selectOrphans(criteria, searchOrphans);
		excludeConcept(criteria, excludeConceptId);
		onlyValidatedConcepts(criteria, onlyValidatedConcepts);
		return criteria.list();
	}

	@Override
	public List<ThesaurusConcept> getPaginatedConceptsByThesaurusId(
			Integer startIndex, Integer limit, String excludeConceptId,
			String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts,String like) {

		Criteria criteria = selectPaginatedConceptsByAlphabeticalOrder(startIndex, limit);
		
		selectThesaurus(criteria, thesaurusId);
		matchLanguage(criteria, thesaurusId);
		selectOrphans(criteria, searchOrphans);
		excludeConcept(criteria, excludeConceptId);
		onlyValidatedConcepts(criteria, onlyValidatedConcepts);
		conceptNameIsLike(criteria,like);

		return criteria.list();
	}

	@Override
	public List<ThesaurusConcept> getPaginatedAvailableConceptsOfGroup(
			Integer startIndex, Integer limit, String groupId,
			String thesaurusId, Boolean onlyValidatedConcepts,String like) {

		DetachedCriteria dc = DetachedCriteria.forClass(ThesaurusConceptGroup.class, "gr");
		dc.createCriteria("concepts", "tc", JoinType.RIGHT_OUTER_JOIN);
		dc.setProjection(Projections.projectionList().add(Projections.property("tc.identifier")));
		dc.add(Restrictions.eq("gr.identifier", groupId));

		
		Criteria criteria = selectPaginatedConceptsByAlphabeticalOrder(startIndex, limit);
		criteria.add(Subqueries.propertyNotIn("tc.identifier", dc));
		
		selectThesaurus(criteria, thesaurusId);
		criteria.add(
				Restrictions.not(Restrictions.and(
						Restrictions.eq("topConcept", false),
						Restrictions.or(Restrictions.isNull("tc.parentConcepts"),
								Restrictions.isEmpty("tc.parentConcepts"))
				
				)));

		if(null != like){
			conceptNameIsLike(criteria,like);
		}
		onlyValidatedConcepts(criteria, onlyValidatedConcepts);

		return criteria.list();
	}

	@Override
	public Long getConceptsByThesaurusIdCount(
			String excludeConceptId, String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts,String like) {
		Criteria criteria = getCurrentSession()
				.createCriteria(ThesaurusTerm.class, "tt")
				.add(Restrictions.isNotNull("tt.concept"))
				.createCriteria("concept", "tc", JoinType.RIGHT_OUTER_JOIN);

		criteria.add(Restrictions.eq("tt.prefered", Boolean.TRUE));
		//criteria.add(Restrictions.eq("tt.language.id", defaultLang));
		
		selectThesaurus(criteria, thesaurusId);
		selectOrphans(criteria, searchOrphans);
		excludeConcept(criteria, excludeConceptId);
		onlyValidatedConcepts(criteria, onlyValidatedConcepts);
		conceptNameIsLike(criteria,like);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}

	@Override
	public List<ThesaurusConcept> getAllRootChildren(ThesaurusConcept concept) {
		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusConcept.class, "tc");
		criteria.createCriteria("tc.rootConcepts", "rc").add(
				Restrictions.eq("rc.identifier", concept.getIdentifier()));
		return criteria.list();

	}

	private Criteria selectPaginatedConceptsByAlphabeticalOrder(Integer startIndex,
			Integer limit) {
		Criteria criteria = getCurrentSession()
				.createCriteria(ThesaurusTerm.class, "tt")
				.add(Restrictions.isNotNull("tt.concept"))
				.createCriteria("concept", "tc", JoinType.RIGHT_OUTER_JOIN);

		criteria.add(Restrictions.eq("tt.prefered", Boolean.TRUE));
		
		criteria.setProjection(
				Projections
						.projectionList()
						.add(Projections.property("tt.lexicalValue"))
						.add(Projections.property("tc.identifier").as(
								"identifier"))).setResultTransformer(
				Transformers.aliasToBean(ThesaurusConcept.class));

		if (limit > 0) {
			criteria.setMaxResults(limit);
		}
		criteria.setFirstResult(startIndex);
		criteria.addOrder(Order.asc("tt.lexicalValue"));
		return criteria;
	}

	/**
	 * Selects TopTerm concepts by ThesaurusId without excluding.
	 *
	 * @param criteria
	 * @param thesaurusId
	 */
	private void selectRoot(Criteria criteria, String thesaurusId) {
		selectRoot(criteria, thesaurusId, null);
	}

	/**
	 * Selects TopTerm concepts by ThesaurusId with excluding.
	 *
	 * @param criteria
	 * @param thesaurusId
	 * @param excludeId
	 */
	private void selectRoot(Criteria criteria, String thesaurusId,
	                        String excludeId) {
		excludeConcept(criteria, excludeId);
		selectThesaurus(criteria, thesaurusId);
		selectNoParents(criteria);
	}

	private void selectNoParents(Criteria criteria) {
		criteria.add(Restrictions.or(Restrictions.isNull("tc.parentConcepts"),
				Restrictions.isEmpty("tc.parentConcepts")));
	}

	private void selectThesaurus(Criteria criteria, String thesaurusId) {
		criteria.add(Restrictions.eq("tc.thesaurus.identifier",
				(String) thesaurusId));
	}

	private void selectOrphans(Criteria criteria, Boolean searchOrphans) {
		if (searchOrphans != null) {
			criteria.add(Restrictions.eq("topConcept", !searchOrphans));
		}
	}

	private void excludeConcept(Criteria criteria, String excludeId) {
		if (excludeId != null && !excludeId.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.eq(TC_IDENTIFIER,
					excludeId)));
		}
	}

	private void onlyValidatedConcepts(Criteria criteria,
	                                   Boolean onlyValidatedConcepts) {

		if (onlyValidatedConcepts == null) {
			return;
		}

		if (onlyValidatedConcepts) {
			selectStatus(criteria , ConceptStatusEnum.VALIDATED);
		}
	}
	
	private void selectStatus (Criteria criteria, ConceptStatusEnum status) {
		if (status != null) {
			criteria.add(Restrictions.eq("status",
					status.getStatus()));
		}
	}
	
	private void conceptNameIsLike(Criteria criteria,String like) {
		if (null == like) {
			return;
		}else {
			criteria.add(Restrictions.ilike("tt.lexicalValue","%"+like+"%"));
		}
	}
	

	private void matchLanguage(Criteria criteria,String thesaurusId) {
		Criteria thesaurusCriteria = getCurrentSession().createCriteria(Thesaurus.class, "t")
				.add(Restrictions.eq("t.identifier", thesaurusId));
		
		List<Thesaurus> lThesaurus = thesaurusCriteria.list();
		Thesaurus thesaurus = lThesaurus.get(0);
		
		criteria.add(Restrictions.in("tt.language", thesaurus.getLang()));
	}

	private List<ThesaurusConcept> getListByThesaurusAndTopConcept(
			Thesaurus thesaurus, boolean topConcept, int maxResults,String like, ConceptStatusEnum status) {

		if (thesaurus == null) {
			throw new BusinessException("Object thesaurus can't be null !",
					"empty-thesaurus");
		}
		Criteria crit = getCriteriaByThesaurusAndTopConcept(thesaurus,topConcept,like, status);
		if (maxResults > 0) {
			crit.setMaxResults(maxResults);
		}
		return crit.list();
	}

	private long getListByThesaurusAndTopConceptCount(Thesaurus thesaurus,
	                                                  boolean topConcept) {

		if (thesaurus == null) {
			throw new BusinessException("Object thesaurus can't be null !",
					"empty-thesaurus");
		}
		Criteria crit = getCriteriaByThesaurusAndTopConcept(thesaurus,
				topConcept,null,null).setProjection(Projections.rowCount());
		return (Long) crit.list().get(0);
	}

	private Criteria getCriteriaByThesaurusAndTopConcept(Thesaurus thesaurus,
	                                                     boolean topConcept,String like, ConceptStatusEnum status) {
		Criteria criteria = getCurrentSession().createCriteria(ThesaurusConcept.class, "tc");
		
		if(null != like){
			criteria = getCurrentSession().createCriteria(ThesaurusTerm.class, "tt")
					.add(Restrictions.isNotNull("tt.concept"))
					.createCriteria("concept", "tc", JoinType.RIGHT_OUTER_JOIN);
					
					criteria.setProjection(
							Projections
									.projectionList()
									.add(Projections.property("tt.lexicalValue"))
									.add(Projections.property("tc.identifier").as(
											"identifier"))).setResultTransformer(
							Transformers.aliasToBean(ThesaurusConcept.class));
			conceptNameIsLike(criteria,like);
		}
		
		selectThesaurus(criteria, thesaurus.getIdentifier());
		selectOrphans(criteria, !topConcept);
		selectNoParents(criteria);
		selectStatus(criteria, status);
		return criteria;
	}

	@Override
	public Long countConcepts(String idThesaurus) {
		Criteria criteria = getCurrentSession().createCriteria(
				ThesaurusConcept.class);
		criteria.add(Restrictions.eq(THESAURUS_IDENTIFIER, idThesaurus))
				.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}

	@Override
	public Long countConceptsWoNotes(String idThesaurus) {
		Query query = getCurrentSession().createSQLQuery(
				"select count(*) " + "from thesaurus_concept c  "
						+ "left join note n on c.identifier = n.conceptid  "
						+ "where c.thesaurusid=:pthesaurusid"
						+ " and n.identifier is null"
		);
		query.setParameter("pthesaurusid", idThesaurus);
		return ((BigInteger) query.list().get(0)).longValue();
	}

	@Override
	public List<ThesaurusConcept> getConceptsWoNotes(String idThesaurus, int startIndex, int limit) {
		Query query = getCurrentSession().createSQLQuery("select c.* "
				+ "from thesaurus_concept c  "
				+ "left join note n on c.identifier = n.conceptid  "
				+ "where c.thesaurusid=:pthesaurusid"
				+ " and n.identifier is null").addEntity(ThesaurusConcept.class);
		query.setParameter("pthesaurusid", idThesaurus);
		query.setFirstResult(startIndex);
		query.setFetchSize(limit);
		query.setMaxResults(limit);
		return (List<ThesaurusConcept>) query.list();
	}

	@Override
	public Long countConceptsAlignedToIntThes(String idThesaurus) {
		DetachedCriteria alignmentCriteria = DetachedCriteria
				.forClass(Alignment.class, "al")
				.add(Restrictions.isNotNull("al.internalTargetThesaurus"))
				.setProjection(
						Projections
								.projectionList()
								.add(Projections
										.property(AL_SOURCE_CONCEPT_IDENTIFIER))
				);

		DetachedCriteria conceptCriteria = DetachedCriteria
				.forClass(ThesaurusConcept.class, "stc")
				.add(Restrictions.eq("stc.thesaurus.identifier", idThesaurus))
				.setProjection(
						Projections.projectionList().add(
								Projections.property("stc.identifier"))
				);

		Criteria criteria = getCurrentSession()
				.createCriteria(ThesaurusConcept.class, "tc")
				.add(Restrictions.and(
						Subqueries.propertyIn(TC_IDENTIFIER, alignmentCriteria),
						Subqueries.propertyIn(TC_IDENTIFIER, conceptCriteria)))
				.setProjection(Projections.rowCount());

		return (Long) criteria.list().get(0);
	}

	@Override
	public Long countConceptsAlignedToExtThes(String idThesaurus) {
		DetachedCriteria alignmentCriteria = DetachedCriteria
				.forClass(Alignment.class, "al")
				.add(Restrictions.isNotNull("al.externalTargetThesaurus"))
				.setProjection(
						Projections
								.projectionList()
								.add(Projections
										.property(AL_SOURCE_CONCEPT_IDENTIFIER))
				);

		DetachedCriteria conceptCriteria = DetachedCriteria
				.forClass(ThesaurusConcept.class, "stc")
				.add(Restrictions.eq("stc.thesaurus.identifier", idThesaurus))
				.setProjection(
						Projections.projectionList().add(
								Projections.property("stc.identifier"))
				);

		Criteria criteria = getCurrentSession()
				.createCriteria(ThesaurusConcept.class, "tc")
				.add(Restrictions.and(
						Subqueries.propertyIn(TC_IDENTIFIER, alignmentCriteria),
						Subqueries.propertyIn(TC_IDENTIFIER, conceptCriteria)))
				.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}

	@Override
	public Long countConceptsAlignedToMyThes(String idThesaurus) {
		DetachedCriteria alignmentCriteria = DetachedCriteria
				.forClass(Alignment.class, "al")
				.add(Restrictions.eq("al.internalTargetThesaurus.identifier",
						idThesaurus))
				.setProjection(
						Projections
								.projectionList()
								.add(Projections
										.property(AL_SOURCE_CONCEPT_IDENTIFIER))
				);

		Criteria criteria = getCurrentSession()
				.createCriteria(ThesaurusConcept.class, "tc")
				.add(Subqueries.propertyIn(TC_IDENTIFIER, alignmentCriteria))
				.setProjection(Projections.rowCount());

		return (Long) criteria.list().get(0);
	}

	@Override
	public List<ThesaurusConcept> getConceptsAlignedToMyThes(String idThesaurus, int startIndex, int limit) {
		DetachedCriteria alignmentCriteria = DetachedCriteria
				.forClass(Alignment.class, "al")
				.add(Restrictions.eq("al.internalTargetThesaurus.identifier",
						idThesaurus))
				.setProjection(
						Projections
								.projectionList()
								.add(Projections
										.property(AL_SOURCE_CONCEPT_IDENTIFIER))
				);

		Criteria criteria = getCurrentSession()
				.createCriteria(ThesaurusConcept.class, "tc")
				.add(Subqueries.propertyIn(TC_IDENTIFIER, alignmentCriteria))
				.setFirstResult(startIndex)
				.setMaxResults(limit);

		return (List<ThesaurusConcept>) criteria.list();
	}



}