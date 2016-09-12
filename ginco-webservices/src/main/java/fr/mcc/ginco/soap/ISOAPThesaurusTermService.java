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

import fr.mcc.ginco.data.ReducedThesaurusTerm;
import fr.mcc.ginco.enums.TermStatusEnum;

@WebService
public interface ISOAPThesaurusTermService {

	/**
	 * This service returns the identifier of a concept by the term
	 * 
	 * @param lexical
	 *            value of the term,
	 * @param thesaurus
	 *            identifier of the term,
	 * @param language
	 *            identifier of the term
	 * 
	 * @return identifier of a concept
	 */
	String getConceptIdByTerm(
			@WebParam(name = "lexicalValue") String lexicalValue,
			@WebParam(name = "thesaurusId") String thesaurusId,
			@WebParam(name = "languageId") String languageId);

	/**
	 * This service returns: lexical value if the term is preferred, lexical
	 * value of preferred term if current term isn't preferred, null if the term
	 * doesn't exist
	 * 
	 * @param lexical
	 *            value of the term,
	 * @param thesaurus
	 *            identifier of the term,
	 * @param language
	 *            identifier of the term
	 * 
	 * @return reduced preferred term
	 */
	ReducedThesaurusTerm getPreferredTermByTerm(
			@WebParam(name = "lexicalValue") String lexicalValue,
			@WebParam(name = "thesaurusId") String thesaurusId,
			@WebParam(name = "languageId") String languageId,
			@WebParam(name = "withNotes") Boolean withNotes);

	/**
	 * This service returns true if the term is preferred, false if the term
	 * isn't preferred
	 * 
	 * @param lexical
	 *            value of the term,
	 * @param thesaurus
	 *            identifier of the term,
	 * @param language
	 *            identifier of the term
	 * 
	 * @return preferred or not preferred
	 */
	Boolean isPreferred(@WebParam(name = "lexicalValue") String lexicalValue,
			@WebParam(name = "thesaurusId") String thesaurusId,
			@WebParam(name = "languageId") String languageId);

	/**
	 * This service returns list of reduced terms that begin with input string
	 * 
	 * @param beginning string,
	 * @param start index of search,
	 * @param limit of result rows,
	 * 
	 * @return list of reduced terms
	 */

	List<ReducedThesaurusTerm> getTermsBeginWithSomeString(
			@WebParam(name = "request") String request,
			@WebParam(name = "preferredTermOnly") Boolean preferredTermOnly,
			@WebParam(name = "startIndex") int startIndex,
			@WebParam(name = "limit") int limit,
			@WebParam(name = "status") TermStatusEnum status,
			@WebParam(name = "withNotes") Boolean withNotes);
			
	/**
	 * This service returns list of reduced terms that begin with input string
	 * 
	 * @param beginning string,
	 * @param return only preferred terms
	 * @param start index of search,
	 * @param limit of result rows,
	 * 
	 * @return list of reduced terms
	 */

	List<ReducedThesaurusTerm> getTermsBeginWithSomeStringByThesaurus(
			@WebParam(name = "request") String request,
			@WebParam(name = "thesaurusId") String thesaurusId,
			@WebParam(name = "preferredTermOnly") Boolean preferredTermOnly,
			@WebParam(name = "startIndex") int startIndex,
			@WebParam(name = "limit") int limit,
			@WebParam(name = "status") TermStatusEnum status,
			@WebParam(name = "withNotes") Boolean withNotes);


}
