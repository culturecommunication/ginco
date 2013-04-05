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
package fr.mcc.ginco.audit.tracking;

import java.io.Serializable;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import fr.mcc.ginco.audit.readers.AuditQueryBuilder;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.GincoRevModifiedEntityType;
import fr.mcc.ginco.beans.IAuditableBean;
import fr.mcc.ginco.dao.hibernate.GenericHibernateDAO;

/**
 * Takes control on audit.
 */
@Component
public class GincoRevListener implements EntityTrackingRevisionListener,
		ApplicationContextAware {

	private Logger logger = LoggerFactory.getLogger(GincoRevListener.class);
	private static ApplicationContext applicationContext;

	@Override
	public void entityChanged(Class entityClass, String entityName,
			Serializable entityId, RevisionType revisionType,
			Object revisionEntity) {
		GincoRevModifiedEntityType revEntity = new GincoRevModifiedEntityType();
		revEntity.setEntityClassName(entityClass.getName());
		revEntity.setRevision(((GincoRevEntity) revisionEntity).getId());
		((GincoRevEntity) revisionEntity).addModifiedEntityType(revEntity);
		if (ArrayUtils.contains(entityClass.getGenericInterfaces(),
				IAuditableBean.class)) {
			if (!revisionType.equals(RevisionType.DEL)) {
				GenericHibernateDAO objectDAO = new GenericHibernateDAO(
						entityClass);
				objectDAO.setSessionFactory((SessionFactory) applicationContext
						.getBean("gincoSessionFactory"));
				String thesaurusId = ((IAuditableBean) objectDAO
						.getById(entityId)).getThesaurusId();
				((GincoRevEntity) revisionEntity).setThesaurusId(thesaurusId);
			} else {
				SessionFactory sessionFactory = (SessionFactory) applicationContext
						.getBean("gincoSessionFactory");
				AuditReader reader = AuditReaderFactory.get(sessionFactory
						.getCurrentSession());

				AuditQueryBuilder queryBuilder = new AuditQueryBuilder();
				AuditQuery query;
				try {
					query = queryBuilder.getEntityAddedQuery(reader,
							Class.forName(entityName), entityId);
					try {
					Object[] createdEvent = (Object[]) query.getSingleResult();
					if (createdEvent != null) {
						((GincoRevEntity) revisionEntity)
								.setThesaurusId(((GincoRevEntity) createdEvent[1])
										.getThesaurusId());
					}
					} catch (NoResultException nrse) {
						logger.warn("Unable to get the creation revision of the destriyed object", nrse);
					}
				} catch (ClassNotFoundException e) {
					logger.error("Error storing audit data", e);
				}
			}
		} else {
			logger.warn("Trying to audit a bean not implementing IAuditableBean interface");
		}

	}

	@Override
	public void newRevision(Object revisionEntity) {
		GincoRevEntity gincoRevEntity = (GincoRevEntity) revisionEntity;
		if (RequestContextHolder.getRequestAttributes() == null) {
			logger.error("The RequestContext is empty!!!!!");
		} else {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			String name = auth.getName();
			gincoRevEntity.setUsername(name);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
