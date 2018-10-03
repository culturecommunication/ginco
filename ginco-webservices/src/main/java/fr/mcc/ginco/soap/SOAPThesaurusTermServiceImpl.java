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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IConceptHierarchicalRelationshipDAO;
import fr.mcc.ginco.data.ReducedThesaurusTerm;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;

import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.solr.ISearcherService;
import fr.mcc.ginco.solr.SearchEntityType;
import fr.mcc.ginco.solr.SearchResult;
import fr.mcc.ginco.solr.SearchResultList;
import fr.mcc.ginco.solr.SolrConstants;
import fr.mcc.ginco.solr.SolrField;
import fr.mcc.ginco.solr.SortCriteria;

/**
 * This class is the implementation of all SOAP services related to term objects
 */
@WebService(endpointInterface = "fr.mcc.ginco.soap.ISOAPThesaurusTermService")
public class SOAPThesaurusTermServiceImpl implements ISOAPThesaurusTermService {

	
	private Logger logger = LoggerFactory.getLogger(SOAPThesaurusTermServiceImpl.class);
	
	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;
	
	@Inject
	@Named("noteService")
	private INoteService noteService;

	@Inject
	@Named("searcherService")
	private ISearcherService searcherService;
	
	@Inject
	@Named("conceptHierarchicalRelationshipDAO")
	private IConceptHierarchicalRelationshipDAO conceptHierarchicalRelationshipDAO;
	
	
	@Override
	public String getConceptIdByTerm(String lexicalValue, String thesaurusId,
	                                 String languageId) {
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
	                                                   String thesaurusId, String languageId, Boolean withNotes) {
		if (StringUtils.isNotEmpty(lexicalValue)
				&& StringUtils.isNotEmpty(thesaurusId)
				&& StringUtils.isNotEmpty(languageId)) {
			ReducedThesaurusTerm reducedThesaurusTerm = new ReducedThesaurusTerm();
			ThesaurusTerm thesaurusTerm = thesaurusTermService
					.getPreferredTermByTerm(lexicalValue, thesaurusId,
							languageId);

			if (thesaurusTerm != null) {
				ThesaurusConcept termConcept = thesaurusTerm.getConcept();
				if (termConcept != null) {
					reducedThesaurusTerm.setConceptId(termConcept.getIdentifier());
				}
				reducedThesaurusTerm.setIdentifier(thesaurusTerm
						.getIdentifier());
				reducedThesaurusTerm.setLexicalValue(thesaurusTerm
						.getLexicalValue());
				reducedThesaurusTerm.setLanguageId(thesaurusTerm.getLanguage()
						.getId());
				if (withNotes!=null && withNotes==true) {
					addNotesToTerm(reducedThesaurusTerm);
				}
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
	public List<ReducedThesaurusTerm> getTermsBeginWithSomeString(String request, Boolean preferredTermOnly,
	                                                              int startIndex, int limit, TermStatusEnum status, Boolean withNotes, String languageId, Boolean leaves) {
		return getTermsBeginWithSomeStringByThesaurus(request, null,preferredTermOnly, startIndex, limit, status, withNotes, languageId, leaves);
	}
	
	public void addNotesToTerm (ReducedThesaurusTerm rterm) {
		List<Note> notes= noteService.getTermNotePaginatedList(rterm.getIdentifier(), 0, 1000);
		rterm.setNotes(notes);
	}

	@Override
	public List<ReducedThesaurusTerm> getTermsBeginWithSomeStringByThesaurus(String request, String thesaurusId, Boolean preferredTermOnly, 
	                                                                         int startIndex, int limit, TermStatusEnum status, Boolean withNotes, String languageId, Boolean leaves) {
		String copyRequest = StringUtils.stripAccents(request);
		
		if (StringUtils.isNotEmpty(request) && limit != 0) {
			try {
				request = ClientUtils.escapeQueryChars(request);	
				// AAM EVO 2017 début
				String requestFormat = SolrField.LEXICALVALUE+":"+request + "*";
				// old 
				// String requestFormat = SolrField.LEXICALVALUE_STR+":"+request + "*";
				// AAM EVO 2017 fin
				List<ReducedThesaurusTerm> reducedThesaurusTermList = new ArrayList<ReducedThesaurusTerm>();
				SortCriteria crit = new SortCriteria(SolrField.LEXICALVALUE, SolrConstants.ASCENDING);
				Integer searchType = SearchEntityType.TERM;
				if (preferredTermOnly)
				{
					searchType = SearchEntityType.TERM_PREF;
				}
				Integer intStatus = null;
				if (status!=null) {
					intStatus = status.getStatus();
				}
				/*SearchResultList searchResultList = searcherService.search(
						requestFormat, searchType, thesaurusId, intStatus, null, null, null, crit,
						startIndex, limit);*/
				
				//JLSO Evolutions V5 E0 15/01/2018 début
				//JLSO 29055 21/06/2018 début
				SearchResultList searchResultList2 = searcherService.search(
						"*", searchType, thesaurusId, intStatus, null, null, null, crit,
						startIndex, 1000000000);
				//JLSO 29055 21/06/2018 fin
				
				String cleanRequest = StringUtils.stripAccents(request);
				String cleanRequestChecked = cleanRequest;
				String cleanRequestChecked2 = cleanRequest;
				//JLSO Evolutions V5 E0 15/01/2018 fin
				
				if (searchResultList2 != null) {
					for (SearchResult searchResult : searchResultList2) {
						//JLSO Evolutions V5 E0 15/01/2018 début
						ReducedThesaurusTerm reducedThesaurusTerm = new ReducedThesaurusTerm();
						
						String cleanResult = StringUtils.stripAccents(searchResult.getLexicalValue()).toLowerCase();
						
						// Check œ and æ
						if(cleanRequest.contains("œ")){
							cleanRequestChecked = cleanRequestChecked.replace("œ", "o");
						}
						if(cleanRequest.contains("æ")){
							cleanRequestChecked = cleanRequestChecked.replace("æ", "a");
						}
						if(cleanRequest.contains("o")){
							cleanRequestChecked2 = cleanRequestChecked2.replace("o", "œ");
						}
						if(cleanRequest.contains("a")){
							cleanRequestChecked2 = cleanRequestChecked2.replace("a", "æ");
						}
						
						//MPL 30861 (The request includes more than one word) 03/10/2018 debut
						String[] requests = new String[] {cleanRequest, cleanRequestChecked, cleanRequestChecked2, copyRequest};
						
						/*
						String resultNoAccent = cleanResult.replace("'", " ");
						String[] splitString = resultNoAccent.split(" ");
						Boolean encontrado = false;
						
						for(String palabra : splitString) {
							if(palabra.startsWith(cleanRequest.toLowerCase()) || palabra.startsWith(cleanRequestChecked.toLowerCase()) || palabra.startsWith(cleanRequestChecked2.toLowerCase()) || palabra.startsWith(copyRequest.toLowerCase())){
								encontrado = true;
							}
						}
						if(encontrado){
							//JLSO Evolutions V5 E0 15/01/2018 fin
						*/
						if(isMatched(cleanResult, requests)) {
						//MPL 30861 (The request includes more than one word) 03/10/2018 fin
							reducedThesaurusTerm.setIdentifier(searchResult.getIdentifier());
							reducedThesaurusTerm.setConceptId(searchResult.getConceptId());
							reducedThesaurusTerm.setLexicalValue(searchResult.getLexicalValue());
							reducedThesaurusTerm.setLanguageId(searchResult.getLanguages().get(0));
							reducedThesaurusTerm.setStatus(TermStatusEnum.getStatusByCode(searchResult.getStatus()));
							if (withNotes!=null && withNotes==true) {
								addNotesToTerm(reducedThesaurusTerm);
							}
							//JLSO 29055 21/06/2018 début
							if(reducedThesaurusTermList.size() < limit){
								List<ConceptHierarchicalRelationship> parents = conceptHierarchicalRelationshipDAO.findParentsByChildId(reducedThesaurusTerm.getConceptId());
								if(null != languageId && !languageId.isEmpty() && !languageId.equals("?")){
									if(reducedThesaurusTerm.getLanguageId().toLowerCase().equals(languageId.replace("_", "-").toLowerCase())){
										if(null != leaves && leaves == true && (null == parents || parents.size() == 0)){
											reducedThesaurusTermList.add(reducedThesaurusTerm);
										} else {
											if (null == leaves || !leaves) {
												reducedThesaurusTermList.add(reducedThesaurusTerm);
											}
										}
									} 
								} else {
									if(null != leaves && leaves == true && (null == parents || parents.size() == 0)){
										reducedThesaurusTermList.add(reducedThesaurusTerm);
									} else {
										if(null == leaves || !leaves) {
											reducedThesaurusTermList.add(reducedThesaurusTerm);
										}
									}
								}
							}
							//JLSO 29055 21/06/2018 fin
						}
					}
				}
				
				Collections.sort(reducedThesaurusTermList, new Comparator<ReducedThesaurusTerm>() {
								Collator frCollator = Collator.getInstance(Locale.FRENCH);
					
								public int compare(ReducedThesaurusTerm t1, ReducedThesaurusTerm t2) {
									frCollator.setStrength(Collator.SECONDARY);
									
									//JLSO Evolutions V5 E0 15/01/2018 début
									
									String term1 = t1.getLexicalValue();
									String term2 = t2.getLexicalValue();
									
									term1 = term1.replace(" ", "000");
									term1 = term1.replace("œ", "oZZZ");
									term1 = term1.replace("æ", "aZZZ");
									term2 = term2.replace(" ", "000");
									term2 = term2.replace("œ", "oZZZ");
									term2 = term2.replace("æ", "aZZZ");
									return frCollator.compare(term1, term2);
									
									//return frCollator.compare(t1.getLexicalValue().replace(" ", "000"), t2.getLexicalValue().replace(" ", "000"));
									//return frCollator.compare(t1.getLexicalValue().replace(" ", "000").replace("œ", "oZZZ").replace("æ", "aZZZ"), t2.getLexicalValue().replace(" ", "000").replace("œ", "oZZZ").replace("æ", "aZZZ"));
									
									//JLSO Evolutions V5 E0 15/01/2018 fin
								}
				});
				return reducedThesaurusTermList;
			} catch (SolrServerException e) {
				logger.error("Excepción en SOAPThesaurusTermServiceImpl.getTermsBeginWithSomeStringByThesaurus. Exception [" + e.toString()+ "]. ", e);
				
				throw new TechnicalException("Search exception", e);
			} catch (Exception e) {
				logger.error("Excepción en SOAPThesaurusTermServiceImpl.getTermsBeginWithSomeStringByThesaurus. Exception [" + e.toString()+ "]. ", e);
				throw e;
			}
		} else {
			throw new BusinessException("One or more parameters are empty",
					"empty-parameters");
		}
	}

	@Override
	public List<ReducedThesaurusTerm> getStringBeginWithSomeString(String request, Boolean preferredTermOnly,
			int startIndex, int limit, TermStatusEnum status, Boolean withNotes) {
		return getStringBeginWithSomeStringByThesaurus(request, null,preferredTermOnly, startIndex, limit, status, withNotes);
	}

	@Override
	public List<ReducedThesaurusTerm> getStringBeginWithSomeStringByThesaurus(String request, String thesaurusId,
			Boolean preferredTermOnly, int startIndex, int limit, TermStatusEnum status, Boolean withNotes) {

		String copyRequest = StringUtils.stripAccents(request);
		
		if (StringUtils.isNotEmpty(request) && limit != 0) {
			try {
				request = ClientUtils.escapeQueryChars(request);	
				// AAM EVO 2017 début
				String requestFormat = SolrField.LEXICALVALUE+":"+request + "*";
				// old 
				// String requestFormat = SolrField.LEXICALVALUE_STR+":"+request + "*";
				// AAM EVO 2017 fin
				List<ReducedThesaurusTerm> reducedThesaurusTermList = new ArrayList<ReducedThesaurusTerm>();
				SortCriteria crit = new SortCriteria(SolrField.LEXICALVALUE, SolrConstants.ASCENDING);
				Integer searchType = SearchEntityType.TERM;
				if (preferredTermOnly)
				{
					searchType = SearchEntityType.TERM_PREF;
				}
				Integer intStatus = null;
				if (status!=null) {
					intStatus = status.getStatus();
				}
				//JLSO 29055 21/06/2018 début
				/*SearchResultList searchResultList = searcherService.search(
						requestFormat, searchType, thesaurusId, intStatus, null, null, null, crit,
						startIndex, 1000000000);*/
				
				//JLSO Evolutions V5 E0 15/01/2018 début
				SearchResultList searchResultList2 = searcherService.search(
						"*", searchType, thesaurusId, intStatus, null, null, null, crit,
						startIndex, 1000000000);
				//JLSO 29055 21/06/2018 fin
				
				String cleanRequest = StringUtils.stripAccents(request);
				String cleanRequestChecked = cleanRequest;
				String cleanRequestChecked2 = cleanRequest;
				//JLSO Evolutions V5 E0 15/01/2018 fin
				
				if (searchResultList2 != null) {
					for (SearchResult searchResult : searchResultList2) {
						//JLSO Evolutions V5 E0 15/01/2018 début
						ReducedThesaurusTerm reducedThesaurusTerm = new ReducedThesaurusTerm();
						
						String cleanResult = StringUtils.stripAccents(searchResult.getLexicalValue()).toLowerCase();
						
						// Check œ and æ
						if(cleanRequest.contains("œ")){
							cleanRequestChecked = cleanRequestChecked.replace("œ", "o");
						}
						if(cleanRequest.contains("æ")){
							cleanRequestChecked = cleanRequestChecked.replace("æ", "a");
						}
						if(cleanRequest.contains("o")){
							cleanRequestChecked2 = cleanRequestChecked2.replace("o", "œ");
						}
						if(cleanRequest.contains("a")){
							cleanRequestChecked2 = cleanRequestChecked2.replace("a", "æ");
						}

						//MPL 30861 (The request includes more than one word) 03/10/2018 debut
						String[] requests = new String[] {cleanRequest, cleanRequestChecked, cleanRequestChecked2, copyRequest};
						/*
						if(cleanResult.startsWith(cleanRequest.toLowerCase()) || cleanResult.startsWith(cleanRequestChecked.toLowerCase()) || cleanResult.startsWith(cleanRequestChecked2.toLowerCase()) || cleanResult.startsWith(copyRequest.toLowerCase())){
							//JLSO Evolutions V5 E0 15/01/2018 fin 
						*/
						if(isMatched(cleanResult, requests)) {
							//MPL 30861 (The request includes more than one word) 03/10/2018 fin
							reducedThesaurusTerm.setIdentifier(searchResult.getIdentifier());
							reducedThesaurusTerm.setConceptId(searchResult.getConceptId());
							reducedThesaurusTerm.setLexicalValue(searchResult.getLexicalValue());
							reducedThesaurusTerm.setLanguageId(searchResult.getLanguages().get(0));
							reducedThesaurusTerm.setStatus(TermStatusEnum.getStatusByCode(searchResult.getStatus()));
							if (withNotes!=null && withNotes==true) {
								addNotesToTerm(reducedThesaurusTerm);
							}
							//JLSO 29055 21/06/2018 début
							if(reducedThesaurusTermList.size() < limit){
								reducedThesaurusTermList.add(reducedThesaurusTerm);
							}
							//JLSO 29055 21/06/2018 fin
						}
					}
				}
						
				Collections.sort(reducedThesaurusTermList, new Comparator<ReducedThesaurusTerm>() {
								Collator frCollator = Collator.getInstance(Locale.FRENCH);
					
								public int compare(ReducedThesaurusTerm t1, ReducedThesaurusTerm t2) {
									frCollator.setStrength(Collator.SECONDARY);
									
									//JLSO Evolutions V5 E0 15/01/2018 début
									
									String term1 = t1.getLexicalValue();
									String term2 = t2.getLexicalValue();
									
									term1 = term1.replace(" ", "000");
									term1 = term1.replace("œ", "oZZZ");
									term1 = term1.replace("æ", "aZZZ");
									term2 = term2.replace(" ", "000");
									term2 = term2.replace("œ", "oZZZ");
									term2 = term2.replace("æ", "aZZZ");
									return frCollator.compare(term1, term2);
									
									//return frCollator.compare(t1.getLexicalValue().replace(" ", "000"), t2.getLexicalValue().replace(" ", "000"));
									//return frCollator.compare(t1.getLexicalValue().replace(" ", "000").replace("œ", "oZZZ").replace("æ", "aZZZ"), t2.getLexicalValue().replace(" ", "000").replace("œ", "oZZZ").replace("æ", "aZZZ"));
									
									//JLSO Evolutions V5 E0 15/01/2018 fin
								}
				});
				/*List<String> listReturn = new ArrayList<String>();
				for(ReducedThesaurusTerm element : reducedThesaurusTermList){
					listReturn.add(element.getLexicalValue());
				}*/
				return reducedThesaurusTermList;
			} catch (SolrServerException e) {
				logger.error("Excepción en SOAPThesaurusTermServiceImpl.getTermsBeginWithSomeStringByThesaurus. Exception [" + e.toString()+ "]. ", e);
				
				throw new TechnicalException("Search exception", e);
			} catch (Exception e) {
				logger.error("Excepción en SOAPThesaurusTermServiceImpl.getTermsBeginWithSomeStringByThesaurus. Exception [" + e.toString()+ "]. ", e);
				throw e;
			}
		} else {
			throw new BusinessException("One or more parameters are empty",
					"empty-parameters");
		}
		//return getTermsBeginWithSomeString(request, preferredTermOnly, startIndex, limit, status, withNotes);
	}

	//MPL 30861 (The request includes more than one word) 03/10/2018 debut
	private boolean isMatched(String resultDAO, String[] requests) {
		char[] cleanResultChars = resultDAO.toCharArray();
		for(int i = 0; i < requests.length; i++) {
			if(resultDAO.indexOf(requests[i]) != -1 
					&& (resultDAO.indexOf(requests[i]) == 0 
					|| cleanResultChars[resultDAO.indexOf(requests[i]) - 1] == ' ')){
				return true;
			}
		}
		return false;
	}
	//MPL 30861 (The request includes more than one word) 03/10/2018 fin
}