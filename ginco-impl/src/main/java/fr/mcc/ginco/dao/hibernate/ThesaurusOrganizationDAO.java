package fr.mcc.ginco.dao.hibernate;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.dao.IThesaurusOrganizationDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO for Thesaurus organization
 */
@Repository
public class ThesaurusOrganizationDAO extends GenericHibernateDAO<ThesaurusOrganization, Integer>
		implements IThesaurusOrganizationDAO {

	public ThesaurusOrganizationDAO() {
		super(ThesaurusOrganization.class);
	}

	@Override
	public List<ThesaurusOrganization> getFilteredOrganizations() {
		Criteria criteria = getCurrentSession().createCriteria(Thesaurus.class, "t")
				.add(Restrictions.isNotNull("t.creator"))
				.createCriteria("creator", "c", JoinType.RIGHT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("c.identifier"))
						.add(Projections.property("c.identifier").as("identifier"))
						.add(Projections.property("c.name").as("name"))
						.add(Projections.property("c.homepage").as("homepage"))
						.add(Projections.property("c.email").as("email")))
				.setResultTransformer(Transformers.aliasToBean(ThesaurusOrganization.class))
				.addOrder(Order.asc("name"));

		return criteria.list();
	}
}
