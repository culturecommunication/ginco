package fr.mcc.ginco.tests.services;

import fr.mcc.ginco.beans.*;
import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IndexerServiceImpl;
import fr.mcc.ginco.solr.EntityType;
import fr.mcc.ginco.solr.SearchResultList;
import fr.mcc.ginco.solr.SolrConstants;
import fr.mcc.ginco.solr.SolrField;
import fr.mcc.ginco.solr.SortCriteria;
import fr.mcc.ginco.tests.LoggerTestUtil;
import fr.mcc.ginco.utils.DateUtil;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndexerServiceTest {

	@InjectMocks
	private IndexerServiceImpl indexerService;

	@Mock(name = "noteService")
	private INoteService noteService;
	
	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "solrServer")
	private SolrServer solrServer;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(indexerService);
	}

	@Test
	public void testAddTerm() throws SolrServerException, IOException {
		ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
		Thesaurus mockThesaurus = new Thesaurus();
		mockThesaurus.setIdentifier("th1");
		fakeThesaurusTerm.setIdentifier("term1");
		fakeThesaurusTerm.setThesaurus(mockThesaurus);
		fakeThesaurusTerm.setLexicalValue("lexicalValue");
        fakeThesaurusTerm.setCreated(DateUtil.nowDate());
        fakeThesaurusTerm.setModified(DateUtil.nowDate());
        fakeThesaurusTerm.setLanguage(new Language() {{setId("test");}});
        fakeThesaurusTerm.setStatus(1);
        fakeThesaurusTerm.setPrefered(true);
		List<Note> mockListNote = new ArrayList<Note>();
		Note fakeNote = new Note();
		fakeNote.setLexicalValue("test");
		mockListNote.add(fakeNote);
		ArgumentCaptor<SolrInputDocument> arg = ArgumentCaptor
				.forClass(SolrInputDocument.class);
		when(
				noteService.getTermNotePaginatedList(anyString(), anyInt(),
						anyInt())).thenReturn(mockListNote);
		fakeThesaurusTerm.setLexicalValue("lexicalValue");
		indexerService.addTerm(fakeThesaurusTerm);
		verify(solrServer).add(arg.capture());
		verify(solrServer).commit();
		Assert.assertEquals(fakeNote.getLexicalValue(), arg.getValue()
				.getFieldValue(SolrField.NOTES));
		Assert.assertEquals(mockThesaurus.getIdentifier(), arg.getValue()
				.getFieldValue(SolrField.THESAURUSID));
		Assert.assertEquals(fakeThesaurusTerm.getIdentifier(), arg.getValue()
				.getFieldValue(SolrField.IDENTIFIER));
		Assert.assertEquals(fakeThesaurusTerm.getLexicalValue(), arg.getValue()
				.getFieldValue(SolrField.LEXICALVALUE));
	}
	
	@Test
	public void testAddConcept() throws SolrServerException, IOException {
		ThesaurusConcept fakeThesaurusConcept = new ThesaurusConcept();
		Thesaurus mockThesaurus = new Thesaurus();
		mockThesaurus.setIdentifier("th1");
		fakeThesaurusConcept.setIdentifier("concept1");
        fakeThesaurusConcept.setThesaurus(mockThesaurus);
        fakeThesaurusConcept.setCreated(DateUtil.nowDate());
        fakeThesaurusConcept.setModified(DateUtil.nowDate());
        fakeThesaurusConcept.setStatus(1);
		ThesaurusTerm fakePreferredTerm = new ThesaurusTerm();
		fakePreferredTerm.setLexicalValue("lexicalValue");
		fakePreferredTerm.setThesaurus(mockThesaurus);
		List<Note> mockListNote = new ArrayList<Note>();
		Note fakeNote = new Note();
		fakeNote.setLexicalValue("test");
		mockListNote.add(fakeNote);
		
		when(
				noteService.getConceptNotePaginatedList(anyString(), anyInt(),
						anyInt())).thenReturn(mockListNote);
		when(
				thesaurusConceptService.getConceptPreferredTerm(fakeThesaurusConcept.getIdentifier())).thenReturn(fakePreferredTerm);
		indexerService.addConcept(fakeThesaurusConcept);
		ArgumentCaptor<SolrInputDocument> arg = ArgumentCaptor
				.forClass(SolrInputDocument.class);
		verify(solrServer).add(arg.capture());
		verify(solrServer).commit();
		Assert.assertEquals(fakeNote.getLexicalValue(), arg.getValue()
				.getFieldValue(SolrField.NOTES));
		Assert.assertEquals(mockThesaurus.getIdentifier(), arg.getValue()
				.getFieldValue(SolrField.THESAURUSID));
		Assert.assertEquals(fakeThesaurusConcept.getIdentifier(), arg.getValue()
				.getFieldValue(SolrField.IDENTIFIER));
		Assert.assertEquals(fakePreferredTerm.getLexicalValue(), arg.getValue()
				.getFieldValue(SolrField.LEXICALVALUE));
	}
	
	@Test
	public void testSearch() throws SolrServerException, IOException {
		QueryResponse fakeResp = new QueryResponse();
		NamedList<Object> respContent = new NamedList<Object>();
		SolrDocumentList fakeDocList = new SolrDocumentList();
		SolrDocument fakeDoc = new SolrDocument();
		fakeDoc.addField(SolrField.IDENTIFIER, "id1");
		fakeDoc.addField(SolrField.LEXICALVALUE, "lex1");
		fakeDoc.addField(SolrField.THESAURUSID, "th1");
		fakeDoc.addField(SolrField.THESAURUSTITLE, "title1");
		fakeDoc.addField(SolrField.TYPE, ThesaurusTerm.class.getSimpleName());
        fakeDoc.addField(SolrField.EXT_TYPE, EntityType.TERM_NON_PREF);
        fakeDoc.addField(SolrField.MODIFIED, DateUtil.nowDate());
        fakeDoc.addField(SolrField.CREATED, DateUtil.nowDate());
        fakeDoc.addField(SolrField.STATUS, 0);
        fakeDoc.addField(SolrField.LANGUAGE, "lang1");
		fakeDocList.add(fakeDoc);
		fakeDocList.setNumFound(1);
		respContent.add("response", fakeDocList);
		fakeResp.setResponse(respContent);
		when(
				solrServer.query(any(SolrParams.class))).thenReturn(fakeResp);
		SortCriteria crit = new SortCriteria(SolrField.LEXICALVALUE,SolrConstants.DESCENDING);
		SearchResultList results = indexerService.search("test", null, null, null, null, null, null,crit, 0, 10);
		Assert.assertEquals(results.getNumFound() , 1);
		Assert.assertEquals(results.get(0).getLexicalValue(), fakeDoc.getFieldValue(SolrField.LEXICALVALUE));
	}
}
