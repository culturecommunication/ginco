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
package fr.mcc.ginco.solr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.utils.DateUtil;

@Service("searcherServiceUtil")
public class SearcherServiceUtil {

	private static final String CLOSE_PARENTHESIS = ")";
	private static final String OPEN_PARENTHESIS = "(";
	private static final String OR = " OR ";
	private static final String SPACE = " ";
	private static final char DOUBLE_QUOTE = '"';
	private static final String SEMI_COL = ":";
	private static final String PLUS = "+";
	private static final String BLANK = "";


	public String addAndQuery(String field, Object value, Boolean from,
			boolean string) {
		if (value == null) {
			return BLANK;
		}

		if (value.toString().isEmpty() || "-1".equals(value.toString())) {
			return BLANK;
		}

		String result = BLANK;
		result += PLUS + field + SEMI_COL;

		if (value instanceof String && string) {
			result += DOUBLE_QUOTE;
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
			result += DOUBLE_QUOTE;
		}

		result += SPACE;
		return result;
	}

	/**
	 * {type: 1, typeLabel:me.xConceptLabel}, {type: 2,
	 * typeLabel:me.xTermLabel}, {type: 3, typeLabel:me.xNonPreferredTermLabel},
	 * {type: 4, typeLabel: me.xPreferredTermLabel},
	 * {type: 5, typeLabel: me.xNoteLabel}
	 *
	 * @param type
	 * @return
	 */
	public String getExtTypeQuery(Integer type) {
		if (type == null) {
			return BLANK;
		}
		if (type == -1) {
			return BLANK;
		}

		String query = BLANK;
		if (type == 1) {
			query += PLUS + SolrField.EXT_TYPE + SEMI_COL + EntityType.CONCEPT;
		} else if (type == 2) {
			query += PLUS + SolrField.EXT_TYPE + SEMI_COL + OPEN_PARENTHESIS
					+ EntityType.TERM_PREF + OR + EntityType.TERM_NON_PREF
					+ CLOSE_PARENTHESIS;
		} else if (type == 3) {
			query += PLUS + SolrField.EXT_TYPE + SEMI_COL + EntityType.TERM_NON_PREF;
		} else if (type == 4) {
			query += PLUS + SolrField.EXT_TYPE + SEMI_COL + EntityType.TERM_PREF;
		} else if (type == 5) {
			query += PLUS + SolrField.EXT_TYPE + SEMI_COL + OPEN_PARENTHESIS
					+ EntityType.NOTE + OR
					+ EntityType.NOTE_DEFINITION + OR
					+ EntityType.NOTE_EDITORIAL + OR
					+ EntityType.NOTE_EXAMPLE + OR
					+ EntityType.NOTE_HISTORY + OR
					+ EntityType.NOTE_SCOPE
					+ CLOSE_PARENTHESIS;
		}

		return query;
	}

	public SearchResultList getSearchResultList(SolrDocumentList list) {
		SearchResultList results = new SearchResultList();
		results.setNumFound(list.getNumFound());
		for (int i = 0; i < list.size(); ++i) {
			results.add(getSearchResult(list.get(i)));
		}
		return results;
	}

	private SearchResult getSearchResult(SolrDocument doc)
    {
    	SearchResult result = new SearchResult();
    	result.setIdentifier(getSolrField(doc,SolrField.IDENTIFIER));
    	result.setLexicalValue(getSolrField(doc,SolrField.LEXICALVALUE));
    	result.setType(getSolrField(doc,SolrField.TYPE));
    	result.setThesaurusId(getSolrField(doc,SolrField.THESAURUSID));
    	result.setThesaurusTitle(getSolrField(doc,SolrField.THESAURUSTITLE));
        result.setCreated(getSolrDateField(doc,SolrField.CREATED));
        result.setModified(getSolrDateField(doc,SolrField.MODIFIED));
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

	private String getSolrField(SolrDocument doc, String solrField)
	{
		if (doc.getFieldValue(solrField)!=null)
		{
			return doc.getFieldValue(solrField).toString();
		} else {
			return BLANK;
		}
	}

	private String getSolrDateField(SolrDocument doc, String solrField)
	{
		if (doc.getFieldValue(solrField)!=null)
		{
			return DateUtil.toString((Date)doc.getFieldValue(solrField));
		} else {
			return BLANK;
		}
	}
}
