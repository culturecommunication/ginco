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

import java.util.List;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Suggestion;

/**
 * Data Access Object for suggestion
 */
public interface ISuggestionDAO extends IGenericDAO<Suggestion, Integer> {
    
    /**
	 * Gets the list of suggestions attached to a given concept
	 * @param conceptId
	 * @param startIndex
	 * @param limit
	 * @return  List<Note> List of paginated suggestions for a term
	 */
	List<Suggestion> findConceptPaginatedSuggestions(String conceptId, Integer startIndex, Integer limit);
	
	
	/**
	 * Gets the list of suggestions attached to a given term
	 * @param termId
	 * @param startIndex
	 * @param limit
	 * @return  List<Note> List of paginated suggestions for a term
	 */
	List<Suggestion> findTermPaginatedSuggestions(String termId, Integer startIndex, Integer limit);
    
    
    /**
     * Counts the number of suggestions of the given concept
     * @param conceptId
     * @return
     */
    Long getConceptSuggestionCount(String conceptId);
    
    /**
     * Counts the number of suggestions of the given term
     * @param termId
     * @return
     */
    Long getTermSuggestionCount(String termId);
    
    /**
     * Gets the list of suggestions whose recipient is the given parameter
     * @param recipient
     * @param startIndex
     * @param limit
     * @return
     */
    List<Suggestion> findPaginatedSuggestionsByRecipient(String recipient, Integer startIndex, Integer limit);
    
	/**
	 * Counts the number of suggestions for the given recipient
	 * @param recipient
	 * @return
	 */
	Long getSuggestionsByRecipientCount(String recipient);

}
