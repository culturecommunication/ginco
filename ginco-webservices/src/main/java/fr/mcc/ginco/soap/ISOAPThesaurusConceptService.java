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
package fr.mcc.ginco.soap;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import fr.mcc.ginco.data.ReducedThesaurusConcept;
import fr.mcc.ginco.data.ReducedThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;

/**
 * This class exposes all SOAP services related to concept objects
 */

@WebService
public interface ISOAPThesaurusConceptService {

	/**
	 * Get hierarchical relations between two concepts
	 *
	 * @param firstConceptId identifier of first concept
	 * @param secondConceptId identifier of second concept
	 * @return 2 if first concept is child of second concept
	 */

	int getConceptsHierarchicalRelations(@WebParam(name = "firstConceptId") String firstConceptId,
	                                     @WebParam(name = "secondConceptId") String secondConceptId);


	/**
	 * Get preferredTerm by the identifier of a concept
	 *
	 * @param conceptId identifier of a concept
	 * @return reduced preferred term
	 */


	List<ReducedThesaurusTerm> getPreferredTermByConceptId(@WebParam(name = "conceptId") String conceptId, @WebParam(name = "withNotes") Boolean withNotes);

	/**
	 * Returns the list of not preferred ThesaurusTerms by a concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of not preferred terms
	 */
	List<ReducedThesaurusTerm> getConceptNotPreferredTerms(@WebParam(name = "conceptId") String conceptId, @WebParam(name = "withNotes") Boolean withNotes);

	/**
	 * Returns the status of a concept
	 *
	 * @param conceptId
	 * @return the status of a concept
	 */
	int getStatusByConceptId(@WebParam(name = "conceptId") String conceptId);

	/**
	 * Returns children of a concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of objects
	 */
	List<String> getChildrenByConceptId(@WebParam(name = "conceptId") String conceptId, @WebParam(name="status") ConceptStatusEnum status);
	
	/**
	 * Returns children of a concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of objects
	 */
	List<ReducedThesaurusConcept> getChildrenByConceptId_v2(@WebParam(name = "conceptId") String conceptId, @WebParam(name="status") ConceptStatusEnum status,
			@WebParam(name="withAssociates") Boolean withAssociates,
			@WebParam(name="withNotes") Boolean withNotes);
	
	
	/**
	 * Returns root concepts for given concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of root concepts
	 */
	List<String> getRootConcepts(@WebParam(name = "conceptId") String conceptId, @WebParam(name="status") ConceptStatusEnum status);

	/**
	 * Returns root concepts for given concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of root concepts
	 */
	 List<ReducedThesaurusConcept> getRootConcepts_v2(@WebParam(name = "conceptId") String conceptId, @WebParam(name="status") ConceptStatusEnum status,
			 @WebParam(name="withAssociates") Boolean withAssociates,
			 @WebParam(name="withNotes") Boolean withNotes);

	
	/**
	 * Returns parent concepts for given concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of parent concepts
	 */
	List<String> getParentConcepts(@WebParam(name = "conceptId") String conceptId, @WebParam(name="status") ConceptStatusEnum status);

	/**
	 * Returns parent concepts for given concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of parent concepts
	 */
	List<ReducedThesaurusConcept> getParentConcepts_v2(@WebParam(name = "conceptId") String conceptId, @WebParam(name="status") ConceptStatusEnum status, 
			@WebParam(name="withAssociates") Boolean withAssociates,
			@WebParam(name="withNotes") Boolean withNotes);

	
	/**
	 * Returns associative concepts for given concept
	 *
	 * @param conceptId identifier of a concept
	 * @return list of associative concepts
	 */
	List<String> getAssociativeConcepts(@WebParam(name = "conceptId") String conceptId, @WebParam(name="status") ConceptStatusEnum status);


	/**
	 * Returns top concepts for a thesaurus
	 *
	 * @param thesaurusId identifier of a thesaurus
	 * @param status of concepts
	 * @return list of top concepts
	 */
	List<String> getTopConceptsByThesaurusId(@WebParam(name = "thesaurusId") String thesaurusId, @WebParam(name="status") ConceptStatusEnum status);
	
	/**
	 * Returns top concepts for a thesaurus
	 *
	 * @param thesaurusId identifier of a thesaurus
	 * @param status of concepts
	 * @return list of top concepts
	 */
	List<ReducedThesaurusConcept> getTopConceptsByThesaurusId_v2(@WebParam(name = "thesaurusId") String thesaurusId, @WebParam(name="status") ConceptStatusEnum status, 
			@WebParam(name="withAssociates") Boolean withAssociates,
			@WebParam(name="withNotes") Boolean withNotes);
}

