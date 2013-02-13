package fr.mcc.ginco.dao.hibernate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusTermDAO;

@Repository("thesaurusTermDAO")
@Scope("prototype")
public class ThesaurusTermDAO extends GenericHibernateDAO<ThesaurusTerm, String> implements IThesaurusTermDAO {
	public ThesaurusTermDAO(Class<ThesaurusTerm> clazz) {
		super(clazz);
	}
	public ThesaurusTermDAO() {
		super(ThesaurusTerm.class);
	}
}
