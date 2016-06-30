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
import java.util.Set;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;

/**
 * Service used to work with {@link ThesaurusConcept} objects, contains basic
 * methods exposed to client part. For example, to get some
 * Language objects, use {@link #getThesaurusConceptList()} ()}
 *
 * @see fr.mcc.ginco.beans
 */
public interface IThesaurusConceptService {
	/**
	 * Get list of all ThesaurusConcept.
	 *
	 * @return
	 */
	List<ThesaurusConcept> getThesaurusConceptList();

	/**
	 * Get list of ThesaurusConcept by list of id.
	 *
	 * @param ids : List of ids
	 * @return
	 */
	Set<ThesaurusConcept> getThesaurusConceptList(List<String> ids);

	/**
	 * Get single ThesaurusConcept by its id.
	 *
	 * @param id of object
	 * @return {@code null} if not found; object otherwise.
	 */
	ThesaurusConcept getThesaurusConceptById(String id);

	/**
	 * Get the ThesaurusConcepts which are not top term in a given thesaurus
	 *
	 * @param thesaurusId
	 * @return
	 */
	List<ThesaurusConcept> getOrphanThesaurusConcepts(String thesaurusId, int maxResults);

	/**
	 * Get the ThesaurusConcepts which are not top term in a given thesaurus
	 *
	 * @param thesaurusId
	 * @return
	 */
	List<ThesaurusConcept> getOrphanThesaurusConcepts(String thesaurusId);

	/**
	 * Gets the preferred term of a concept
	 *
	 * @param conceptId
	 * @return
	 */
	ThesaurusTerm getConceptPreferredTerm(String conceptId);

	/**
	 * Gets the list of the preferred terms of a concept
	 *
	 * @param conceptId
	 * @return
	 */
	List<ThesaurusTerm> getConceptPreferredTerms(String conceptId);

	/**
	 * Gets the label of a concept with title@lang notation
	 *
	 * @param conceptId
	 * @return
	 */
	String getConceptLabel(String conceptId);

	/**
	 * Update a single Thesaurus Concept Object
	 *
	 * @param The concept to update
	 * @param The list of the concept's terms
	 * @param The ids of associated concepts
	 * @param The hierarchical relations (from current concept (child) to its parents)
	 * @param The list of children concepts we must attach
	 * @param The list of children concepts we must detach (must not be still children of our concept)
	 * @param The list of alignemnts where this concept is the source
	 * @return The updated concept
	 */
	ThesaurusConcept updateThesaurusConcept(ThesaurusConcept object,
	                                        List<ThesaurusTerm> terms, List<AssociativeRelationship> associatedConceptIds, List<ConceptHierarchicalRelationship> hierarchicalRelationships, List<ThesaurusConcept> childrenConceptToDetach, List<ThesaurusConcept> childrenConceptToAttach, List<Alignment> alignments);


	/**
	 * Get the ThesaurusConcepts which are top term in a given thesaurus
	 *
	 * @param thesaurusId
	 * @return
	 */
	List<ThesaurusConcept> getTopTermThesaurusConcepts(String thesaurusId);
	
	/**
	 * Get the ThesaurusConcepts which are top term in a given thesaurus
	 *
	 * @param thesaurusId
	 * @return
	 */
	List<ThesaurusConcept> getTopTermThesaurusConcepts(String thesaurusId, ConceptStatusEnum status, int maxResults);

	/**
	 * Get the ThesaurusConcepts which are top term in a given thesaurus
	 *
	 * @param thesaurusId
	 * @param maxResults
	 * @return
	 */
	List<ThesaurusConcept> getTopTermThesaurusConcepts(String thesaurusId, int maxResults);

	/**
	 * Get the number of orphan thesaurus concept for a given thesaurusId
	 *
	 * @param thesaurusId
	 * @return
	 */
	long getOrphanThesaurusConceptsCount(String thesaurusId);

	/**
	 * Get the number of top concept for a given thesaurus
	 *
	 * @param thesaurusId
	 * @return
	 */
	long getTopTermThesaurusConceptsCount(String thesaurusId);

	/**
	 * Search children of concept with given id (orphan or not).
	 *
	 * @param conceptId id of concept.
	 * @return list of objects.
	 */
	List<ThesaurusConcept> getChildrenByConceptId(String conceptId,String like);

	/**
	 * Search children of concept with given id (orphan or not).
	 *
	 * @param conceptId id of concept.
	 * @return list of objects.
	 */
	List<ThesaurusConcept> getChildrenByConceptId(String conceptId, int maxResults,String like);
	
	/**
	 * Search children of concept with given id (orphan or not).
	 *
	 * @param conceptId id of concept.
	 * @return list of objects.
	 */
	List<ThesaurusConcept> getChildrenByConceptId(String conceptId, int maxResults,String like, ConceptStatusEnum status);


	/**
	 * Search paginated concepts of thesaurus excluding given conceptId
	 * with given parameter (orphan or not).
	 *
	 * @param startIndex
	 * @param limit
	 * @param excludeConceptId      id of concept to exclude.
	 * @param thesaurusId           id of thesaurus.
	 * @param searchOrphans         indicates if concepts with topConcept==false should be
	 *                              included in result. Could be null if doesn't matters.
	 * @param onlyValidatedConcepts indicates if returned concepts will be filtered on status = VALIDATED.
	 * @return list of objects.
	 */
	List<ThesaurusConcept> getPaginatedConceptsByThesaurusId(
			Integer startIndex, Integer limit, String excludeConceptId,
			String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts, String like);

	/**
	 *
	 * Get the number of thesaurus concepts excluding given conceptId
	 * with given parameter (orphan or not).
	 *
	 * @param excludeConceptId
	 * @param thesaurusId
	 * @param searchOrphans
	 * @param onlyValidatedConcepts
	 * @return
	 */
	Long getConceptsByThesaurusIdCount(String excludeConceptId,
			String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts,String like);

	/**
	 * Search concepts of thesaurus excluding given conceptId
	 * with given parameter (orphan or not).
	 *
	 * @param excludeConceptId      id of concept to exclude.
	 * @param thesaurusId           id of thesaurus.
	 * @param searchOrphans         indicates if concepts with topConcept==false should be
	 *                              included in result. Could be null if doesn't matters.
	 * @param onlyValidatedConcepts indicates if returned concepts will be filtered on status = VALIDATED.
	 * @return list of objects.
	 */
	List<ThesaurusConcept> getConceptsByThesaurusId(String excludeConceptId, String thesaurusId, Boolean searchOrphans, Boolean onlyValidatedConcepts);

	/**
	 * Indicates whether concept has children.
	 *
	 * @param conceptId
	 * @return
	 */
	boolean hasChildren(String conceptId);

	/**
	 * Delete a ThesaurusConcept object
	 *
	 * @param object
	 * @return
	 */
	ThesaurusConcept destroyThesaurusConcept(ThesaurusConcept object);


	/**
	 * Asynchronous method to recalculate root for all the node childrens
	 *
	 * @param parentId
	 */
	void calculateChildrenRoot(String parentId);

	/**
	 * Get all concepts eligible for an array
	 *
	 * @param arrayId
	 * @param thesaurusId
	 * @return List of concepts eligible for an array
	 */
	List<ThesaurusConcept> getAvailableConceptsOfArray(String arrayId, String thesaurusId,String like);

	/**
	 * Get paginated concepts eligible for an array
	 *
	 * @param startIndex
	 * @param limit
	 * @param arrayId
	 * @param thesaurusId
	 * @return List of concepts eligible for an array
	 */
	List<ThesaurusConcept> getAvailableConceptsOfArray(Integer startIndex,
			Integer limit, String arrayId, String thesaurusId,String like);

	/**
	 * Get all concepts eligible for a group
	 *
	 * @param groupId
	 * @param thesaurusId
	 * @return List of concepts eligible for a group
	 */
	List<ThesaurusConcept> getAvailableConceptsOfGroup(String groupId, String thesaurusId,String like);

	/**
	 * Get paginated concepts eligible for a group
	 *
	 * @param startIndex
	 * @param limit
	 * @param groupId
	 * @param thesaurusId
	 * @return List of concepts eligible for a group
	 */
	List<ThesaurusConcept> getAvailableConceptsOfGroup(Integer startIndex, Integer limit, String groupId, String thesaurusId,String like);

	/**
	 * For indexing purposes.
	 *
	 * @return list of all existing concepts.
	 */
	List<ThesaurusConcept> getAllConcepts();

	/**
	 * Return the lexical value of this concept preferred term
	 *
	 * @param concept
	 * @return
	 */
	String getConceptTitle(ThesaurusConcept concept);


	/**
	 * return the language of this concept preferred term
	 *
	 * @param concept
	 * @return
	 */
	String getConceptTitleLanguage(ThesaurusConcept concept);

	/**
	 * Get hierarchical relations between two concepts
	 *
	 * @param identifier of first concept
	 * @param identifier of second concept
	 * @return 2 if first concept is child of second concept
	 */

	int getConceptsHierarchicalRelations(String firstConceptId, String secondConceptId);

	/**
	 * Returns the list of not preferred ThesaurusTerms by a concept
	 *
	 * @param conceptId
	 * @return list of not preferred terms
	 */
	List<ThesaurusTerm> getConceptNotPreferredTerms(String conceptId);

	/**
	 * Returns the status of a concept
	 *
	 * @param conceptId
	 * @return the status of a concept
	 */
	int getStatusByConceptId(String conceptId);


	/**
	 * Returns this concept preferred term in the given language, null if none is found
	 *
	 * @param conceptId
	 * @param languageId
	 * @return
	 */
	ThesaurusTerm getConceptPreferredTerm(String conceptId, String languageId);

	/**
	 * Returns all the concepts that are under the concept which id is given in parameter (children, children of children, etc. recursively)
	 *
	 * @param conceptId
	 */
	List<ThesaurusConcept> getRecursiveChildrenByConceptId(String conceptId);

	/**
	 * Returns all the concepts that are over the concept which id is given in parameter (children, children of children, etc. recursively)
	 *
	 * @param conceptId
	 */
	List<ThesaurusConcept> getRecursiveParentsByConceptId(String conceptId);

	Set<String> getConceptWithChildrenIdentifers(String thesaurusId);
}
