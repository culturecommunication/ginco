package fr.mcc.ginco.cli;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * Ginco session initializer.
 */
@Service
public class SessionInitier {

	@Inject
	@Named("gincoSessionFactory")
	private SessionFactory sessionFactory;

	/**
	 * Init session.
	 */
	@PostConstruct
	public void init() {
		Session session = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}
}
