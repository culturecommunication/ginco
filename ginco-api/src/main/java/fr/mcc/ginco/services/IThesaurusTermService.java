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

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;

import java.util.List;

/**
 * Service used to work with {@link ThesaurusTerm} objects, contains basic
 * methods exposed to client part. For example, to get a single
 * ThesaurusTerm object, use {@link #getThesaurusTermById(String)}
 *
 * @see fr.mcc.ginco.beans
 */
public interface IThesaurusTermService {
	
	/**
     * Get a single Thesaurus Term by its id
     *
     * @param id to search
     * @return {@code null} if not found
     */
	ThesaurusTerm getThesaurusTermById(String id) throws BusinessException;
	
    /**
     * Get list of paginated Thesaurus Terms.
     * @return List of Thesaurus Terms (the number given in argument), from the start index
     */
    List<ThesaurusTerm> getPaginatedThesaurusSandoxedTermsList(Integer startIndex, Integer limit, String idThesaurus);

    /**
     * Get number of Thesaurus Sandboxed Terms
     * @param idThesaurus of a Thesaurus
     * @return number of Thesaurus Sandboxed Terms for a given Thesaurus
     */
    Long getSandboxedTermsCount(String idThesaurus) throws BusinessException;
    
    /**
     * Get number of Thesaurus Sandboxed Validated Terms
     * @param idThesaurus of a Thesaurus
     * @return number of Thesaurus Validated Sandboxed Terms for a given Thesaurus
     */
    Long getSandboxedValidatedTermsCount(String idThesaurus) throws BusinessException;
   
    
    /**
     * Update a single Thesaurus Term Object
     * @throws BusinessException 
     */
    ThesaurusTerm updateThesaurusTerm(ThesaurusTerm object) throws BusinessException;
    
    /**
     * Delete a single Thesaurus Term Object
     * @throws BusinessException 
     */
    ThesaurusTerm destroyThesaurusTerm(ThesaurusTerm object) throws BusinessException;

    
    /**
     * @param listOfTerms
     * @return
     * This method returns all the prefered terms
     */
    List<ThesaurusTerm> getPreferedTerms(List<ThesaurusTerm> listOfTerms);
    
    /**
     * @param idConcept
     * @return
     * This method returns all the terms that belong to a concept
     * @throws BusinessException 
     */
    List<ThesaurusTerm> getTermsByConceptId(String idConcept) throws BusinessException;
    
    /**
     * This method compares lists of terms - if previosly presented term has been deleted
     * (so it is not anymore in Concept) it will be marked as SandBoxed.
     * @param sent new list of Terms
     * @param origin old list of Terms
     */
    void markTermsAsSandboxed(List<ThesaurusTerm> sent, List<ThesaurusTerm> origin) throws BusinessException;

    /**
     * Get list of paginated Thesaurus Validated Terms.
     * @return List of Thesaurus Terms with status validated (the number given in argument), from the start index
     */
    List<ThesaurusTerm> getPaginatedThesaurusSandoxedValidatedTermsList(
			Integer startIndex, Integer limit, String idThesaurus);

    /**
     * For indexing purposes.
      * @return list of all existing terms.
     */
    List<ThesaurusTerm> getAllTerms();
    
    /**
	 * This service returns the identifier of a concept by the term
	 * 
	 * @param  lexical value of the term,@param
	 * @param  thesaurus identifier of the term,
	 * @param  language of the term
	 * 
	 * @return identifier of a concept
	 */
    
    String getConceptIdByTerm(String lexicalValue, String thesaurusId, String languageId);
    
}