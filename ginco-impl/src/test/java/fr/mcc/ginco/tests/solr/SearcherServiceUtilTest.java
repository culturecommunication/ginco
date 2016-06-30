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
package fr.mcc.ginco.tests.solr;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.solr.ExtEntityType;
import fr.mcc.ginco.solr.SearchEntityType;
import fr.mcc.ginco.solr.SearchResult;
import fr.mcc.ginco.solr.SearchResultList;
import fr.mcc.ginco.solr.SearcherServiceUtil;
import fr.mcc.ginco.solr.SolrField;
import fr.mcc.ginco.utils.DateUtil;

public class SearcherServiceUtilTest {

	@InjectMocks
	private SearcherServiceUtil searcherServiceUtil;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetConceptExtTypeQuery(){
		String expectedQuery = "+ext_type:1";
		String actualQuery = searcherServiceUtil.getExtTypeQuery(SearchEntityType.CONCEPT);
		Assert.assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testGetTermExtTypeQuery(){
		String expectedQuery = "+ext_type:(2 OR 3)";
		String actualQuery = searcherServiceUtil.getExtTypeQuery(SearchEntityType.TERM);
		Assert.assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testGetPrefTermExtTypeQuery(){
		String expectedQuery = "+ext_type:2";
		String actualQuery = searcherServiceUtil.getExtTypeQuery(SearchEntityType.TERM_PREF);
		Assert.assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testGetNonPrefTermExtTypeQuery(){
		String expectedQuery = "+ext_type:3";
		String actualQuery = searcherServiceUtil.getExtTypeQuery(SearchEntityType.TERM_NON_PREF);
		Assert.assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testGetNoteExtTypeQuery(){
		String expectedQuery = "+ext_type:(4 OR 5 OR 6 OR 7 OR 8 OR 9)";
		String actualQuery = searcherServiceUtil.getExtTypeQuery(SearchEntityType.NOTE);
		Assert.assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testGetComplexConceptExtTypeQuery(){
		String expectedQuery = "+ext_type:10";
		String actualQuery = searcherServiceUtil.getExtTypeQuery(SearchEntityType.COMPLEX_CONCEPT);
		Assert.assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testGetSearchResultList(){
		SolrDocumentList fakeDocList = new SolrDocumentList();
		SolrDocument fakeDoc = new SolrDocument();
		fakeDoc.addField(SolrField.IDENTIFIER, "id1");
		fakeDoc.addField(SolrField.LEXICALVALUE, "lex1");
		fakeDoc.addField(SolrField.THESAURUSID, "th1");
		fakeDoc.addField(SolrField.THESAURUSTITLE, "title1");
		fakeDoc.addField(SolrField.TYPE, ThesaurusTerm.class.getSimpleName());
        fakeDoc.addField(SolrField.EXT_TYPE, ExtEntityType.TERM_NON_PREF);
        fakeDoc.addField(SolrField.MODIFIED, DateUtil.dateFromString("2013-11-21 18:19:47"));
        fakeDoc.addField(SolrField.CREATED, DateUtil.dateFromString("2013-11-21 15:51:00"));
        fakeDoc.addField(SolrField.STATUS, 0);
        fakeDoc.addField(SolrField.LANGUAGE, "lang1");
		fakeDocList.add(fakeDoc);
		fakeDocList.setNumFound(1);

		SearchResultList searchResultList = searcherServiceUtil.getSearchResultList(fakeDocList);
		Assert.assertEquals(1, searchResultList.getNumFound());

		SearchResult searchResult = searchResultList.get(0);
		Assert.assertEquals(searchResult.getIdentifier(), "id1");
		Assert.assertEquals(searchResult.getLexicalValue(), "lex1");
		Assert.assertEquals(searchResult.getThesaurusId(), "th1");
		Assert.assertEquals(searchResult.getThesaurusTitle(), "title1");
		Assert.assertEquals(searchResult.getType(), ThesaurusTerm.class.getSimpleName());
		Assert.assertEquals(searchResult.getTypeExt(), String.valueOf(ExtEntityType.TERM_NON_PREF));
		Assert.assertEquals(searchResult.getModified(), "2013-11-21 18:19:47");
		Assert.assertEquals(searchResult.getCreated(), "2013-11-21 15:51:00");
		Assert.assertEquals(searchResult.getStatus(), Integer.valueOf(0));
		Assert.assertEquals(searchResult.getLanguages().get(0), "lang1");

	}
}
