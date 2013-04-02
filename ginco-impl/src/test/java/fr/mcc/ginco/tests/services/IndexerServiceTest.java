package fr.mcc.ginco.tests.services;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.services.IndexerServiceImpl;
import fr.mcc.ginco.solr.SolrField;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class IndexerServiceTest {

	@InjectMocks
	private IndexerServiceImpl indexerService;

	@Mock(name = "noteService")
	private INoteService noteService;

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
		Assert.assertEquals(fakeNote.getLexicalValue(), arg.getValue()
				.getFieldValue(SolrField.NOTES));
		Assert.assertEquals(fakeThesaurusTerm.getIdentifier(), arg.getValue()
				.getFieldValue(SolrField.IDENTIFIER));
		Assert.assertEquals(fakeThesaurusTerm.getLexicalValue(), arg.getValue()
				.getFieldValue(SolrField.LEXICALVALUE));
	}
}
