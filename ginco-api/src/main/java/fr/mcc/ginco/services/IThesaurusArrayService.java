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
package fr.mcc.ginco.services;

import java.util.List;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.exceptions.BusinessException;

/**
 * Service used to work with {@link ThesaurusArray} objects, contains basic
 * methods exposed to client part.
 *
 * @see fr.mcc.ginco.beans
 */
public interface IThesaurusArrayService {
    /**
     * Get single ThesaurusArray object by its id.
     * @param id
     * @return
     */
    ThesaurusArray getThesaurusArrayById(String id);

    /**
     * Get list of ThesaurusArray by corresponding Thesaurus, excluding the concept array which id is given in parameter
     * @param excludedConceptArrayId : the concept to exclude (can be null)
     * @param thesaurusId
     * @return
     */
    List<ThesaurusArray> getAllThesaurusArrayByThesaurusId(String excludedConceptArrayId, String thesaurusId);

    /**
     * Update a single Thesaurus Concept Object    
     * @param thesaurusArray
     * @param label
     * @param arrayConcepts
     * @return
     * @throws BusinessException
     */
    ThesaurusArray updateThesaurusArray(ThesaurusArray thesaurusArray, NodeLabel label,  List<ThesaurusArrayConcept> arrayConcepts) throws BusinessException;

    /**
     * Update a single Thesaurus Concept Object without NodeLabel
     */
    ThesaurusArray updateOnlyThesaurusArray(ThesaurusArray thesaurusArray) throws BusinessException;
    /**
     * Delete a ThesaurusArray
     * @param thesaurusArray
     * @return
     */
    ThesaurusArray destroyThesaurusArray(ThesaurusArray thesaurusArray);

    /**
     * Return list of all arrays who have given concept as SuperOrdinatedConcept.
     * @return
     */
    List<ThesaurusArray> getSubOrdinatedArrays(String thesaurusConceptId);

    /**
     * Return list of all arrays who have not SuperOrdinatedConcept.
     * @param thesaurusId
     * @return
     */
    List<ThesaurusArray> getArraysWithoutParentConcept(String thesaurusId);
    
    List<ThesaurusArray> getArraysWithoutParentArray(String thesaurusId);
	List<ThesaurusArray> getChildrenArrays(String thesaurusArrayId);
}
