package fr.mcc.ginco.audit.utils;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversIntegrator;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Envers integration for Ginco
 */
public class GincoEnversIntegrator extends EnversIntegrator {

	private static Logger logger = LoggerFactory.getLogger(GincoEnversIntegrator.class);

	@Override
	public void integrate(Configuration configuration,
	                      SessionFactoryImplementor sessionFactory,
	                      SessionFactoryServiceRegistry serviceRegistry) {
		super.integrate(configuration, sessionFactory, serviceRegistry);
		AuditContext.enableAudit();
		final AuditConfiguration enversConfiguration = AuditConfiguration.getFor(configuration,
				serviceRegistry.getService(ClassLoaderService.class));

		EventListenerRegistry listenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);

		logger.info("Registering event listeners");
		if (enversConfiguration.getEntCfg().hasAuditedEntities()) {
			listenerRegistry.appendListeners(EventType.POST_DELETE, new DeleteEnversListener(enversConfiguration));
			listenerRegistry.appendListeners(EventType.POST_INSERT, new InsertEnversListener(enversConfiguration));
			listenerRegistry.appendListeners(EventType.POST_UPDATE, new UpdateEnversListerner(enversConfiguration));
			listenerRegistry.appendListeners(EventType.POST_COLLECTION_RECREATE, new CollectionRecreateEnversListener(enversConfiguration));
			listenerRegistry.appendListeners(EventType.PRE_COLLECTION_REMOVE, new PreCollectionRemoveEnversListener(enversConfiguration));
			listenerRegistry.appendListeners(EventType.PRE_COLLECTION_UPDATE, new PreCollectionUpdateEnversListener(enversConfiguration));
		}
	}
}
