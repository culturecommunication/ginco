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

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptStatusEnum;

/**
 * Data Access Object for thesaurus_concept
 */
/**
 * @author hufon
 *
 */
public interface IThesaurusConceptDAO extends IGenericDAO<ThesaurusConcept, String> {

	/**
	 * This method flushes the Hibernate Session.
	 * Flush must not be used manually.
	 * Implemented here for a particular case (see comment in implementation).
	 */
	void flush();

	/**
	 * Gets the list of ThesaurusConcept which are not top term given
	 * a thesaurusID
	 * @param thesaurus object Thesaurus
	 * @return
	 */
	List<ThesaurusConcept> getOrphansThesaurusConcept(Thesaurus thesaurus, int maxResults);

    /**
     * Gets the list of ThesaurusConcept which are top term given
     * a thesaurusID
     * @param thesaurus object Thesaurus
     * @return
     */
	List<ThesaurusConcept> getTopTermThesaurusConcept(Thesaurus thesaurus, int maxResults,String like);

	  /**
     * Gets the list of ThesaurusConcept which are top term given
     * a thesaurusID
     * @param thesaurus object Thesaurus
     * @return
     */
	List<ThesaurusConcept> getTopTermThesaurusConcept(Thesaurus thesaurus, int maxResults,String like, ConceptStatusEnum status);
	
	
	/**
	 * Gets the number of orphan concepts for a given thesaurus
	 * @param thesaurus
	 * @return
	 */
	long getOrphansThesaurusConceptCount(Thesaurus thesaurus);

	/**
	 * Gets the number of top concept for a given thesaurus
	 * @param thesaurus
	 * @return
	 */
	long getTopTermThesaurusConceptCount(Thesaurus thesaurus);


    /**
     * Get list of all root concepts by thesaurusId.
     * @param thesaurusId should not be null !
     * @param searchOrphans indicates if orphan concepts should be included in result,
     *                      could be null if doesn't matters.
     * @return list
     */
    List<ThesaurusConcept> getRootConcepts(String thesaurusId, Boolean searchOrphans);
    
    
	/**
     * Get limited list of paginated children concepts by id of parent Concept.
     * @param conceptId
     * @return list of children or all root concepts if conceptId is null.
     */
	List<ThesaurusConcept> getChildrenConcepts(String conceptId, int maxResults,String like, ConceptStatusEnum status);

	/**
     * Get limited list of paginated children concepts by id of parent Concept.
     * @param conceptId
     * @return list of children or all root concepts if conceptId is null.
     */
	List<ThesaurusConcept> getChildrenConcepts(String conceptId, int maxResults,String like);

	/**
     * Get paginated list of all concepts by id of Thesaurus, excluding given conceptId and
     * filtering by TopTerm property.
     *
     * @param startIndex
     * @param limit
     * @param excludeConceptId
     * @param thesaurusId
     * @param searchOrphans could be null if doesn't matter.
     * @return list of children or all root concepts if conceptId is null.
     */
	List<ThesaurusConcept> getPaginatedConceptsByThesaurusId(
			Integer startIndex, Integer limit, String excludeConceptId,
			String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts,String like);


	List<ThesaurusConcept> getPaginatedAvailableConceptsOfGroup(
			Integer startIndex, Integer limit, String groupId,
			String thesaurusId, Boolean onlyValidatedConcepts,String like);

    /**
     * Get list of all concepts by id of Thesaurus, excluding given conceptId and
     * filtering by TopTerm property.
     *
     * @param excludeConceptId
     * @param thesaurusId
     * @param searchOrphans could be null if doesn't matter.
     * @return list of children or all root concepts if conceptId is null.
     */
	List<ThesaurusConcept> getConceptsByThesaurusId(String excludeConceptId,
			String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts);

	/**
	 *
	 * Get the number of all concepts by id of Thesaurus, excluding given conceptId and
     * filtering by TopTerm property.
	 *
	 * @param excludeConceptId
	 * @param thesaurusId
	 * @param searchOrphans
	 * @param onlyValidatedConcepts
	 * @return
	 */
	Long getConceptsByThesaurusIdCount(
			String excludeConceptId, String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts,String like);

    /**
     * Returns all children (recursive) from the given root
     * @param concept
     * @return
     */
    List<ThesaurusConcept> getAllRootChildren(ThesaurusConcept concept);

	/**
	 * @param idThesaurus
	 * @return Number of concept in a thesaurus
	 */
	Long countConcepts(String idThesaurus);

	/**
	 * Counts the number of concepts without notes
	 * @param idThesaurus
	 * @return
	 */
	Long countConceptsWoNotes(String idThesaurus);

	/**
	 * Gets a list a of the concepts whithout notes
	 * @param idThesaurus
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	List<ThesaurusConcept> getConceptsWoNotes(String idThesaurus, int startIndex, int limit);

	/**
	 * Returns the number of concepts aligned to internal thesauruses
	 * @param idThesaurus
	 * @return
	 */
	Long countConceptsAlignedToIntThes(String idThesaurus);

	/**
	 * Returns the number of concepts aligned to external thesauruses
	 * @param idThesaurus
	 * @return
	 */
	Long countConceptsAlignedToExtThes(String idThesaurus);

	/**
	 * Returns the number of concepts aligned to my thesaurus
	 * @param idThesaurus
	 * @return
	 */
	Long countConceptsAlignedToMyThes(String idThesaurus);

	/**
	 * Returns list of aligned concepts for thesaurus given
	 *
	 * @param idThesaurus id of thesaurus to work with
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	List<ThesaurusConcept> getConceptsAlignedToMyThes(String idThesaurus,
			int startIndex, int limit);

	/**
	 * Return list of concept's ids with children
	 *
	 * @param thesaurusId
	 * @return
	 */
	List<String> getIdentifiersOfConceptsWithChildren(String thesaurusId);



}
