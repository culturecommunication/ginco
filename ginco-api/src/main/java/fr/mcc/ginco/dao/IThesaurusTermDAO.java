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

import fr.mcc.ginco.beans.ThesaurusTerm;

import java.util.List;

/**
 * Data Access Object for thesaurus_term
 */
public interface IThesaurusTermDAO extends IGenericDAO<ThesaurusTerm, String> {

	/**
	 * Returns a list of Thesaurus Terms filtered by thesaurusId
	 * sorted alphabetically on the lexical value with
	 * a starting index and a limit of items to be returned
	 *
	 * @param start Beginning index
	 * @param limit Number of items
	 * @return List<ThesaurusTerm> Paginated list of Thesaurus Terms for a
	 * specified Thesaurus
	 */
	List<ThesaurusTerm> findPaginatedSandboxedItems(Integer start,
	                                                Integer limit, String idThesaurus);

	/**
	 * Get number of Thesaurus Sandboxed Terms
	 *
	 * @param idThesaurus of a Thesaurus
	 * @return number of Thesaurus Sandboxed Terms for a given Thesaurus
	 */
	Long countSandboxedTerms(String idThesaurus);

	/**
	 * Get number of Thesaurus Preferred Terms
	 *
	 * @param idThesaurus of a Thesaurus
	 * @return number of Thesaurus Preferred Terms for a given Thesaurus
	 */
	Long countPreferredTerms(String idThesaurus);

	/**
	 * Get number of Thesaurus Sandboxed Validated Terms
	 *
	 * @param idThesaurus of a Thesaurus
	 * @return number of Thesaurus Sandboxed Validated Terms for a given Thesaurus
	 */
	Long countSandboxedValidatedTerms(String idThesaurus);

	/**
	 * Returns the preferred ThesaurusTerm of a concept in the default application language
	 *
	 * @param conceptId
	 * @return
	 */
	ThesaurusTerm getConceptPreferredTerm(String conceptId);

	/**
	 * Returns the list of preferred ThesaurusTerm of a concept
	 *
	 * @param conceptId
	 * @return
	 */
	List<ThesaurusTerm> getConceptPreferredTerms(String conceptId);

	/**
	 * Gets the preferred term of the given concept in the given language, null if none is found
	 *
	 * @param conceptId
	 * @param languageId
	 * @return
	 */
	ThesaurusTerm getConceptPreferredTerm(String conceptId, String languageId);


	/**
	 * Returns a list of ThesaurusTerm that belong to the same concept (id given
	 * in parameter)
	 *
	 * @param conceptId
	 * @return List of ThesaurusTerm
	 */
	List<ThesaurusTerm> findTermsByConceptId(String conceptId);

	List<ThesaurusTerm> findTermsByThesaurusId(String thesaurusId);

	/**
	 * Returns a number of similar Thesaurus Terms that fit to the parameter's lexical value
	 * and language
	 *
	 * @param term
	 * @return Number of similar terms (based on lexical value + language comparison)
	 */
	Long countSimilarTermsByLexicalValueAndLanguage(ThesaurusTerm term);

	/**
	 * Returns a list of Thesaurus Terms filtered by thesaurusId
	 * sorted alphabetically on the lexical value with
	 * a starting index and a limit of items to be returned, and
	 * with a status validated
	 *
	 * @param startIndex Beginning index
	 * @param limit      Number of items
	 * @return List<ThesaurusTerm> Paginated list of Thesaurus Terms for a
	 * specified Thesaurus
	 */
	List<ThesaurusTerm> findPaginatedSandboxedValidatedItems(
			Integer startIndex, Integer limit, String idThesaurus);

	/**
	 * Returns a list of Thesaurus Terms filtered by thesaurusId
	 * sorted alphabetically on the lexical value with
	 * a starting index and a limit of items to be returned, and
	 * with a status validated
	 *
	 * @param startIndex Beginning index
	 * @param limit      Number of items
	 * @return List<ThesaurusTerm> Paginated list of Thesaurus Terms for a
	 * specified Thesaurus
	 */
	List<ThesaurusTerm> findPaginatedPreferredItems(
			Integer startIndex, Integer limit, String idThesaurus);

	/**
	 * Returns the identifier of a concept by the term
	 *
	 * @param lexicalValue lexical value of the term,
	 * @param thesaurusId  thesaurus identifier of the term,
	 * @param languageId   language identifier of the term
	 * @return identifier of a concept
	 */
	ThesaurusTerm getTermByLexicalValueThesaurusIdLanguageId(String lexicalValue, String thesaurusId, String languageId);

	/**
	 * Returns the list of not preferred ThesaurusTerms by a concept
	 *
	 * @param conceptId
	 * @return list of not preferred terms
	 */
	List<ThesaurusTerm> getConceptNotPreferredTerms(String conceptId);

	/**
	 * Counts the number of terms in the given thesaurus
	 *
	 * @param idThesaurus
	 * @return
	 */
	Long countTerms(String idThesaurus);

	/**
	 * Counts the number of terms which are not preferred term of any concept
	 *
	 * @param idThesaurus
	 * @return
	 */
	Long countNonPreferredTerms(String idThesaurus);

	/**
	 * Counts the number of terms without notes
	 *
	 * @param idThesaurus
	 * @return
	 */
	Long countTermsWoNotes(String idThesaurus);

	/**
	 * Gets the terms with without notes
	 *
	 * @param idThesaurus
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	List<ThesaurusTerm> getTermsWoNotes(String idThesaurus, int startIndex, int limit);

	ThesaurusTerm update(ThesaurusTerm term, boolean checkExisting);

}