/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * 
 * contact.gincoculture_at_gouv.fr
 * 
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * 
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * 
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
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

/**
 * This generic interface brings to the DAO classes the methods which must be implemented
 * 
 */
package fr.mcc.ginco.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Data Access Object - generic type.
 * @param <T> type of object.
 * @param <ID> primary key.
 */
public interface IGenericDAO<T, ID extends Serializable> {
	
	public enum SortingTypes {
		asc,
		desc
	}
	
	/**
	 * Get an object of type T by id
	 * 
	 * @param id
	 * @return An object of type T matching with the given id
	 */
	T getById(ID id);


    /**
     * Load an object of type T by id. The same as {@link IGenericDAO#getById(java.io.Serializable)}
     * but uses {@link org.hibernate.Session#load(Class, java.io.Serializable)} method.
     *
     * @param id
     * @return An object of type T matching with the given id
     */
    T loadById(ID id);


	/**
	 * Returns all the objects of type T
	 * 
	 * @return A list of all the objects of type T
	 */
	List<T> findAll();	
	
	/**
	 * Save an object of type T
	 * 
	 * @param entity
	 * @return The persisted object given in argument
	 */
	T makePersistent(T entity);

	/**
	 * Update an object of type T
	 * 
	 * @param entity
	 * @return Object
	 */
	T update(T entity);
	
	/**
	 * Delete an object of type T
	 * 
	 * @param entity
	 * @return Object
	 */
	T delete(T entity);
	
	/**
	 * @return The number of DB rows matching to the DAO
	 */
	Long count();

	/**
	 * Returns all the objects of type T with a sort asc/desc on a column
	 * 
	 * @return A list of all the objects of type T
	 */
	List<T> findAll(String sortColumn, SortingTypes order);

}
