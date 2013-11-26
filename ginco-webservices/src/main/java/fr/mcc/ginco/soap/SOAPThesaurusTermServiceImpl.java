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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;

import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.data.ReducedThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.solr.ISearcherService;
import fr.mcc.ginco.solr.SearchResult;
import fr.mcc.ginco.solr.SearchResultList;
import fr.mcc.ginco.solr.SortCriteria;

/**
 * This class is the implementation of all SOAP services related to term objects
 *
 */
@WebService(endpointInterface = "fr.mcc.ginco.soap.ISOAPThesaurusTermService")
public class SOAPThesaurusTermServiceImpl implements ISOAPThesaurusTermService {

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Inject
	@Named("searcherService")
	private ISearcherService searcherService;

	@Override
	public String getConceptIdByTerm(String lexicalValue, String thesaurusId,
			String languageId){
		if (StringUtils.isNotEmpty(lexicalValue)
				&& StringUtils.isNotEmpty(thesaurusId)
				&& StringUtils.isNotEmpty(languageId)) {
			return thesaurusTermService.getConceptIdByTerm(lexicalValue,
					thesaurusId, languageId);
		} else {
			throw new BusinessException("One or more parameters are empty",
					"empty-parameters");
		}
	}

	@Override
	public ReducedThesaurusTerm getPreferredTermByTerm(String lexicalValue,
			String thesaurusId, String languageId){
		if (StringUtils.isNotEmpty(lexicalValue)
				&& StringUtils.isNotEmpty(thesaurusId)
				&& StringUtils.isNotEmpty(languageId)) {
			ReducedThesaurusTerm reducedThesaurusTerm = new ReducedThesaurusTerm();
			ThesaurusTerm thesaurusTerm = thesaurusTermService
					.getPreferredTermByTerm(lexicalValue, thesaurusId,
							languageId);

			if (thesaurusTerm != null) {
				reducedThesaurusTerm.setIdentifier(thesaurusTerm
						.getIdentifier());
				reducedThesaurusTerm.setLexicalValue(thesaurusTerm
						.getLexicalValue());
				reducedThesaurusTerm.setLanguageId(thesaurusTerm.getLanguage()
						.getId());
				return reducedThesaurusTerm;
			} else {
				return null;
			}
		} else {
			throw new BusinessException("One or more parameters are empty",
					"empty-parameters");
		}
	}

	@Override
	public Boolean isPreferred(String lexicalValue, String thesaurusId,
			String languageId) {
		if (StringUtils.isNotEmpty(lexicalValue)
				&& StringUtils.isNotEmpty(thesaurusId)
				&& StringUtils.isNotEmpty(languageId)) {
			return thesaurusTermService.isPreferred(lexicalValue, thesaurusId,
					languageId);
		} else {
			throw new BusinessException("One or more parameters are empty",
					"empty-parameters");
		}
	}

	@Override
	public List<ReducedThesaurusTerm> getTermsBeginWithSomeString(String request,
			int startIndex, int limit){
		if (StringUtils.isNotEmpty(request) && limit != 0) {
			try {
				String requestFormat = request + "*";
				List<ReducedThesaurusTerm> reducedThesaurusTermList = new ArrayList<ReducedThesaurusTerm>();
				SortCriteria crit = new SortCriteria(null, null);
				SearchResultList searchResultList = searcherService.search(
						requestFormat, 2, null, null, null, null, null, crit,
						startIndex, limit);
				if (searchResultList != null) {
					for (SearchResult searchResult : searchResultList) {
						ReducedThesaurusTerm reducedThesaurusTerm = new ReducedThesaurusTerm();
						reducedThesaurusTerm.setIdentifier(searchResult.getIdentifier());
						reducedThesaurusTerm.setLexicalValue(searchResult.getLexicalValue());
						reducedThesaurusTerm.setLanguageId(searchResult.getLanguages().get(0));
						reducedThesaurusTermList.add(reducedThesaurusTerm);
					}
				}
				return reducedThesaurusTermList;
			} catch (SolrServerException e) {
				throw new TechnicalException("Search exception", e);
			}
		} else {
			throw new BusinessException("One or more parameters are empty",
					"empty-parameters");
		}
	}
}
