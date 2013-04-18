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

import fr.mcc.ginco.beans.*;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.solr.*;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hufon
 * 
 */

@Service("indexerService")
public class IndexerServiceImpl implements IIndexerService {

	@Log
	private Logger logger;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Inject
	@Named("noteService")
	private INoteService noteService;

	@Value("${solr.url}")
	private String url;

	@Inject
	@Named("solrServer")
	private SolrServer solrServer;

	@Override
	public void removeTerm(ThesaurusTerm thesaurusTerm)
			throws TechnicalException {
		try {
			solrServer.deleteById(thesaurusTerm.getIdentifier());
		} catch (SolrServerException e) {
			throw new TechnicalException(
					"Error executing query for removing Term from index!", e);
		} catch (IOException e) {
			throw new TechnicalException(
					"IO error during executing query for removing Term from index!",
					e);
		}
	}
	
	private String getSolrField(SolrDocument doc, String solrField)
	{
		if (doc.getFieldValue(solrField)!=null)
		{
			return doc.getFieldValue(solrField).toString();
		} else
			return "";
	}

	private SearchResult getSearchResult(SolrDocument doc)
    {
    	SearchResult result = new SearchResult();
    	result.setIdentifier(getSolrField(doc,SolrField.IDENTIFIER));
    	result.setLexicalValue(getSolrField(doc,SolrField.LEXICALVALUE));
    	result.setType(getSolrField(doc,SolrField.TYPE));
    	result.setThesaurusId(getSolrField(doc,SolrField.THESAURUSID));
    	result.setThesaurusTitle(getSolrField(doc,SolrField.THESAURUSTITLE));
        result.setCreated(getSolrField(doc,SolrField.CREATED));
        result.setModified(getSolrField(doc,SolrField.MODIFIED));
        result.setStatus(getSolrField(doc,SolrField.STATUS));
        result.setTypeExt(getSolrField(doc,SolrField.EXT_TYPE));
        List<String> languages = new ArrayList<String>();
        for (Object lang : doc.getFieldValues(SolrField.LANGUAGE))
        {
        	languages.add(lang.toString());
        }
        result.setLanguages(languages);
    	return result;
    }

	private SearchResultList getSearchResultList(SolrDocumentList list) {
		SearchResultList results = new SearchResultList();
		results.setNumFound(list.getNumFound());
		for (int i = 0; i < list.size(); ++i) {
			results.add(getSearchResult(list.get(i)));
		}
		return results;
	}

	@Override
	public SearchResultList search(String request, Integer type,
			String thesaurus, Integer status, String createdFrom,
			String modifiedFrom, String language, int startIndex, int limit)
			throws SolrServerException {

		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set(SolrParam.DEF_TYPE, SolrConstants.EDISMAX);
		params.set(SolrParam.QUERY_FIELDS, SolrField.LEXICALVALUE + "^1.0 "
				+ SolrField.NOTES + "^0.5 " + SolrField.TEXT + "^0.1");
		params.set(SolrParam.FILTER, "*," + SolrConstants.SCORE);
		params.set(SolrParam.SORT, SolrConstants.SCORE + " "
				+ SolrConstants.DESCENDING);
		params.set(SolrParam.QUERY, request);
		params.set(SolrParam.START, startIndex);
		params.set(SolrParam.ROWS, limit);

		params.set(
				SolrParam.FILTER_QUERY,
				addAndQuery(SolrField.CREATED, createdFrom, true, false)
						+ addAndQuery(SolrField.MODIFIED, modifiedFrom, true,
								false)
						+ addAndQuery(SolrField.THESAURUSID, thesaurus, null,
								true)
						+ addAndQuery(SolrField.STATUS, status, null, false)
						+ addAndQuery(SolrField.LANGUAGE, language, null, false)
						+ getExtTypeQuery(type));

		QueryResponse response = solrServer.query(params);
		SolrDocumentList solrresults = response.getResults();
		return getSearchResultList(solrresults);
	}

	/**
	 * {type: 1, typeLabel:me.xConceptLabel}, {type: 2,
	 * typeLabel:me.xTermLabel}, {type: 3, typeLabel:me.xNonPreferredTermLabel},
	 * {type: 4, typeLabel: me.xPreferredTermLabel}
	 * 
	 * @param type
	 * @return
	 */
	private String getExtTypeQuery(Integer type) {
		if (type == null) {
			return "";
		}
		if (type == -1) {
			return "";
		}

		String query = "";
		if (type == 1) {
			query += "+" + SolrField.EXT_TYPE + ":" + EntityType.CONCEPT;
		} else if (type == 2) {
			query += "+" + SolrField.EXT_TYPE + ":" + "("
					+ EntityType.TERM_PREF + " OR " + EntityType.TERM_NON_PREF
					+ ")";
		} else if (type == 3) {
			query += "+" + SolrField.EXT_TYPE + ":" + EntityType.TERM_NON_PREF;
		} else if (type == 4) {
			query += "+" + SolrField.EXT_TYPE + ":" + EntityType.TERM_PREF;
		}

		return query;
	}

	private String addAndQuery(String field, Object value, Boolean from,
			boolean string) {
		if (value == null) {
			return "";
		}

		if (value.toString().isEmpty() || value.toString().equals("-1")) {
			return "";
		}

		String result = "";
		result += "+" + field + ":";

		if (value instanceof String && string) {
			result += '"';
		}

		if (from != null) {
			if (from) {
				result += "[ " + value + "Z TO * ]";
			} else {
				result += "[ * TO " + value + "Z ]";
			}
		} else {
			result += value;
		}

		if (value instanceof String && string) {
			result += '"';
		}

		result += " ";
		return result;
	}

	@Override
	@Async
	public void removeConcept(ThesaurusConcept thesaurusConcept)
			throws TechnicalException {
		try {
			solrServer.deleteById(thesaurusConcept.getIdentifier());
		} catch (SolrServerException e) {
			throw new TechnicalException(
					"Error executing query for removing Concept from index!", e);
		} catch (IOException e) {
			throw new TechnicalException(
					"IO error during executing query for removing Concept from index!",
					e);
		}
	}

	private SolrInputDocument convertConcept(ThesaurusConcept thesaurusConcept) {

		/*
		 * <field name="identifier" type="string" indexed="true" stored="true"
		 * required="true" multiValued="false" /> <field name="lexicalValue"
		 * type="text_fr" indexed="true" stored="true"/> <field name="text"
		 * type="text_general" indexed="true" stored="false"
		 * multiValued="true"/> <field name="thesaurusId" type="string"
		 * indexed="true" stored="true" multiValued="false"/> <field
		 * name="thesaurusTitle" type="string" indexed="false" stored="true"
		 * multiValued="false"/> <field name="created" type="date"
		 * indexed="true" stored="true" multiValued="false" /> <field
		 * name="modified" type="date" indexed="true" stored="true"
		 * multiValued="false" /> <field name="type" type="string"
		 * indexed="true" stored="true" multiValued="false"/> <field
		 * name="language" type="string" indexed="true" stored="true"
		 * multiValued="true"/> <field name="status" type="string"
		 * indexed="true" stored="true" multiValued="false"/> <field
		 * name="ext_type" type="int" indexed="true" stored="true"
		 * multiValued="false"/> <field name="notes" type="text_fr"
		 * indexed="true" stored="true" multiValued="true"/> <field
		 * name="_version_" type="long" indexed="true" stored="true"/>
		 */

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField(SolrField.THESAURUSID, thesaurusConcept.getThesaurusId());
		doc.addField(SolrField.THESAURUSTITLE, thesaurusConcept.getThesaurus()
				.getTitle());
		doc.addField(SolrField.IDENTIFIER, thesaurusConcept.getIdentifier());
		doc.addField(SolrField.TYPE, ThesaurusConcept.class.getSimpleName());
		doc.addField(SolrField.EXT_TYPE, EntityType.CONCEPT);

		Timestamp modifiedDate = new Timestamp(thesaurusConcept.getModified()
				.getTime());
		doc.addField(SolrField.MODIFIED, modifiedDate);

		Timestamp createdDate = new Timestamp(thesaurusConcept.getCreated()
				.getTime());
		doc.addField(SolrField.CREATED, createdDate);

		doc.addField(SolrField.STATUS, thesaurusConcept.getStatus());
		for (ThesaurusTerm term : thesaurusConceptService
				.getConceptPreferredTerms(thesaurusConcept.getIdentifier())) {
			doc.addField(SolrField.LANGUAGE, term.getLanguage().getId());
		}

		String prefLabel;
		try {
			prefLabel = thesaurusConceptService.getConceptPreferredTerm(
					thesaurusConcept.getIdentifier()).getLexicalValue();
		} catch (BusinessException ex) {
			logger.warn(ex.getMessage());
			return null;
		}
		doc.addField(SolrField.LEXICALVALUE, prefLabel);
		List<Note> notes = noteService.getConceptNotePaginatedList(
				thesaurusConcept.getIdentifier(), 0, 0);
		for (Note note : notes) {
			doc.addField(SolrField.NOTES, note.getLexicalValue());
		}
		return doc;
	}

	@Override
	@Async
	public void addConcept(ThesaurusConcept thesaurusConcept)
			throws TechnicalException {
		try {
			addConcepts(Arrays.asList(thesaurusConcept));
		} catch (TechnicalException ex) {
			logger.error(ex.getMessage(), ex.getCause().getMessage());
			throw ex;
		}
	}

	private void addConcepts(List<ThesaurusConcept> thesaurusConcepts)
			throws TechnicalException {
		for (ThesaurusConcept concept : thesaurusConcepts) {
			SolrInputDocument doc = convertConcept(concept);
			if (doc != null) {
				try {
					logger.info("Indexing concept :" + concept.getIdentifier());
					solrServer.add(doc);
				} catch (SolrServerException e) {
					throw new TechnicalException(
							"Error during adding to SOLR!", e);
				} catch (IOException e) {
					throw new TechnicalException("IO error!", e);
				}
			}
		}
		try {
			solrServer.commit();
		} catch (SolrServerException e) {
			throw new TechnicalException("Error during adding to SOLR!", e);
		} catch (IOException e) {
			throw new TechnicalException("IO error!", e);
		}
	}

	@Override
	public void removeThesaurusIndex(String thesaurusId)
			throws TechnicalException {
		try {
			solrServer.deleteByQuery(SolrField.THESAURUSID + ":"
					+ ClientUtils.escapeQueryChars(thesaurusId));
			solrServer.commit();
		} catch (SolrServerException e) {
			throw new TechnicalException(
					"Error executing query for clearing SOLR index!", e);
		} catch (IOException e) {
			throw new TechnicalException(
					"IO error during executing query for clearing SOLR index!",
					e);
		} catch (SolrException e) {
			throw new TechnicalException(
					"Error executing query for clearing SOLR index!", e);
		}
	}

	private void removeAllIndex(SolrServer solrCore) {
		try {
			solrServer.deleteByQuery("*:*");
			solrServer.commit();
		} catch (SolrServerException e) {
			throw new TechnicalException(
					"Error executing query for clearing SOLR index!", e);
		} catch (IOException e) {
			throw new TechnicalException(
					"IO error during executing query for clearing SOLR index!",
					e);
		} catch (SolrException e) {
			throw new TechnicalException(
					"Error executing query for clearing SOLR index!", e);
		}
	}

	@Override
	public void forceIndexing() throws TechnicalException {
		removeAllIndex(solrServer);
		addConcepts(thesaurusConceptService.getAllConcepts());
		addTerms(thesaurusTermService.getAllTerms());
		try {
			solrServer.optimize();
		} catch (SolrServerException e) {
			throw new TechnicalException(
					"Error executing query for clearing SOLR index!", e);
		} catch (IOException e) {
			throw new TechnicalException(
					"IO error during executing query for clearing SOLR index!",
					e);
		}
	}

	@Override
	@Async
	public void addTerm(ThesaurusTerm thesaurusTerm) throws TechnicalException {
		try {
			addTerms(Arrays.asList(thesaurusTerm));
		} catch (TechnicalException ex) {
			logger.error(ex.getMessage(), ex.getCause().getMessage());
			throw ex;
		}
	}

	/**
	 * Convert a Thesaurus Term into a SolrDocument
	 * 
	 * @param thesaurusTerm
	 * @return SolrInputDocument
	 */
	private SolrInputDocument convertTerm(ThesaurusTerm thesaurusTerm) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField(SolrField.THESAURUSID, thesaurusTerm.getThesaurusId());
		doc.addField(SolrField.THESAURUSTITLE, thesaurusTerm.getThesaurus()
				.getTitle());
		doc.addField(SolrField.IDENTIFIER, thesaurusTerm.getIdentifier());
		doc.addField(SolrField.LEXICALVALUE, thesaurusTerm.getLexicalValue());
		doc.addField(SolrField.TYPE, ThesaurusTerm.class.getSimpleName());
		doc.addField(SolrField.LANGUAGE, thesaurusTerm.getLanguage().getId());

		boolean preferred;

		if (thesaurusTerm.getPrefered() == null) {
			preferred = false;
		} else {
			preferred = thesaurusTerm.getPrefered();
		}

		doc.addField(SolrField.EXT_TYPE, (preferred) ? EntityType.TERM_PREF
				: EntityType.TERM_NON_PREF);

		Timestamp modifiedDate = new Timestamp(thesaurusTerm.getModified()
				.getTime());
		doc.addField(SolrField.MODIFIED, modifiedDate);

		Timestamp createdDate = new Timestamp(thesaurusTerm.getCreated()
				.getTime());
		doc.addField(SolrField.CREATED, createdDate);

		doc.addField(SolrField.STATUS, thesaurusTerm.getStatus());

		List<Note> notes = noteService.getTermNotePaginatedList(
				thesaurusTerm.getIdentifier(), 0, 0);
		for (Note note : notes) {
			doc.addField(SolrField.NOTES, note.getLexicalValue());
		}
		return doc;
	}

	private void addTerms(List<ThesaurusTerm> thesaurusTerms) {
		try {
			for (ThesaurusTerm term : thesaurusTerms) {
				logger.info("Indexing term :" + term.getIdentifier());
				SolrInputDocument doc = convertTerm(term);
				solrServer.add(doc);
			}
			solrServer.commit();
		} catch (SolrServerException e) {
			throw new TechnicalException("Error during adding to SOLR!", e);
		} catch (IOException e) {
			throw new TechnicalException("IO error!", e);
		}
	}

	@Async
	@Override
	public void addNote(Note thesaurusNote) throws TechnicalException {
		if (thesaurusNote.getTerm() != null)
			this.addTerm(thesaurusNote.getTerm());
		if (thesaurusNote.getConcept() != null)
			this.addConcept(thesaurusNote.getConcept());
	}

	@Override
	@Async
	public void indexThesaurus(Thesaurus thesaurus) throws TechnicalException {
		removeThesaurusIndex(thesaurus.getIdentifier());
		addTerms(thesaurusTermService.getAllTerms(thesaurus.getIdentifier()));
		addConcepts(thesaurusConceptService.getConceptsByThesaurusId(null,
				thesaurus.getIdentifier(), true, false));
	}
}
