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
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import fr.mcc.ginco.dao.IGenericDAO;

/**
 * Implementation of {@link IGenericDAO}; basic class for DAO-related work.
 * @param <T> type of object.
 * @param <ID> primary key.
 */
public class GenericHibernateDAO<T, ID extends Serializable> implements IGenericDAO<T, ID> {

	private final Class<T> persistentClass;
	
	@Inject
	@Named("gincoSessionFactory")
	private SessionFactory sessionFactory;
		
	public GenericHibernateDAO(Class<T> clazz) {
		this.persistentClass = clazz;
	}
	
	@Override
	final public T loadById(ID id) {
		return (T) getCurrentSession().load(persistentClass, id);
	}	

	@Override
	final public T getById(ID id) {
		return (T) getCurrentSession().get(persistentClass, id);
	}	

	@Override
	final public List<T> findAll() {
		return getCurrentSession().createCriteria(persistentClass).list();
	}	
	
	@Override
	final public List<T> findAll(String sortColumn, SortingTypes order) {
		if (order.asc.equals(order)){
			return getCurrentSession().createCriteria(persistentClass).addOrder(Order.asc(sortColumn)).list();
		}else {
			return getCurrentSession().createCriteria(persistentClass).addOrder(Order.desc(sortColumn)).list();
		}
	}
	
	@Override
	final public Long count(){
		return (Long) getCurrentSession().createCriteria(persistentClass).setProjection(Projections.rowCount()).list().get(0);
	}

	@Override
	final public T makePersistent(T entity) {
		getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public T update(T entity) {
		return makePersistent(entity);
	}
	
	@Override
	final public T delete(T entity) {
		this.getCurrentSession().delete(entity);
		return entity;
	}
	
	final public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();	
	}

	final public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	final public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}