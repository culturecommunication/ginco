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
package fr.mcc.ginco.dao;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;

import java.util.List;

/**
 * Data Access Object for thesaurus_concept
 */
public interface IThesaurusConceptDAO extends IGenericDAO<ThesaurusConcept, String> {

	/**
	 * Gets the list of ThesaurusConcept which are not top term given
	 * a thesaurusID
	 * @param thesaurus object Thesaurus
	 * @return
     * @throws BusinessException in case of error.
	 */
	List<ThesaurusConcept> getOrphansThesaurusConcept(Thesaurus thesaurus) throws BusinessException ;

    /**
     * Gets the list of ThesaurusConcept which are top term given
     * a thesaurusID
     * @param thesaurus object Thesaurus
     * @return
     * @throws BusinessException in case of error.
     */
	List<ThesaurusConcept> getTopTermThesaurusConcept(Thesaurus thesaurus) throws BusinessException;
	
	
	/**
	 * Gets the number of orphan concepts for a given thesaurus
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	long getOrphansThesaurusConceptCount(Thesaurus thesaurus) throws BusinessException;

	/**
	 * Gets the number of top concept for a given thesaurus
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	long getTopTermThesaurusConceptCount(Thesaurus thesaurus) throws BusinessException;


    /**
     * Get list of all root concepts by thesaurusId.
     * @param thesaurusId should not be null !
     * @param searchOrphans indicates if orphan concepts should be included in result,
     *                      could be null if doesn't matters.
     * @return list
     */
    List<ThesaurusConcept> getRootConcepts(String thesaurusId, Boolean searchOrphans);

    /**
     * Get list of all children concepts by id of parent Concept.
     * @param conceptId
     * @return list of children or all root concepts if conceptId is null.
     */
    List<ThesaurusConcept> getChildrenConcepts(String conceptId);

    /**
     * Get list of all concepts by id of Thesaurus, excluding given conceptId and
     * filtering by TopTerm property.
     * @param excludeConceptId
     * @param thesaurusId
     * @param searchOrphans could be null if doesn't matter.
     * @return list of children or all root concepts if conceptId is null.
     */
    List<ThesaurusConcept> getAllConceptsByThesaurusId(String excludeConceptId, String thesaurusId, Boolean searchOrphans);
}
