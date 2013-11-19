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

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.solr.EntityType;
import fr.mcc.ginco.solr.NoteSolrConverter;
import fr.mcc.ginco.solr.SolrField;
import fr.mcc.ginco.utils.DateUtil;

public class NoteSolrConverterTest {

	@InjectMocks
	private NoteSolrConverter noteSolrConverter;


	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertSolrConceptNote() throws SolrServerException, IOException {

		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://th1");
		fakeThesaurus.setTitle("Fake thesaurus");

		ThesaurusConcept fakeConcept = new ThesaurusConcept();
		fakeConcept.setIdentifier("http://c1");
		fakeConcept.setThesaurus(fakeThesaurus);

		Language lang = new Language();
		lang.setId("fr-FR");

		Note fakeConceptNote = new Note();
		fakeConceptNote.setIdentifier("http://note1");
		fakeConceptNote.setCreated(DateUtil.nowDate());
		fakeConceptNote.setModified(DateUtil.nowDate());
		fakeConceptNote.setLexicalValue("Fake concept note");
		fakeConceptNote.setLanguage(lang);
		fakeConceptNote.setConcept(fakeConcept);

		SolrInputDocument doc = noteSolrConverter.convertSolrNote(fakeConceptNote);

		Assert.assertEquals(fakeThesaurus.getIdentifier(), doc
				.getFieldValue(SolrField.THESAURUSID));
		Assert.assertEquals(fakeThesaurus.getTitle(), doc
				.getFieldValue(SolrField.THESAURUSTITLE));
		Assert.assertEquals(fakeConceptNote.getIdentifier(), doc
				.getFieldValue(SolrField.IDENTIFIER));
		Assert.assertEquals(fakeConceptNote.getLexicalValue(), doc
				.getFieldValue(SolrField.LEXICALVALUE));
		Assert.assertEquals(fakeConceptNote.getLanguage().getId(), doc
				.getFieldValue(SolrField.LANGUAGE));
		Assert.assertEquals(fakeConceptNote.getCreated(), doc
				.getFieldValue(SolrField.CREATED));
		Assert.assertEquals(fakeConceptNote.getModified(), doc
				.getFieldValue(SolrField.MODIFIED));
		Assert.assertEquals(Note.class.getSimpleName(), doc
				.getFieldValue(SolrField.TYPE));
		Assert.assertEquals(EntityType.NOTE, doc
				.getFieldValue(SolrField.EXT_TYPE));
	}

	@Test
	public void testConvertSolrTermNote() throws SolrServerException, IOException {

		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://th1");
		fakeThesaurus.setTitle("Fake thesaurus");

		ThesaurusTerm fakeTerm = new ThesaurusTerm();
		fakeTerm.setIdentifier("http://t1");
		fakeTerm.setThesaurus(fakeThesaurus);

		Language lang = new Language();
		lang.setId("fr-FR");

		Note fakeTermNote = new Note();
		fakeTermNote.setIdentifier("http://note1");
		fakeTermNote.setCreated(DateUtil.nowDate());
		fakeTermNote.setModified(DateUtil.nowDate());
		fakeTermNote.setLexicalValue("Fake concept note");
		fakeTermNote.setLanguage(lang);
		fakeTermNote.setTerm(fakeTerm);

		SolrInputDocument doc = noteSolrConverter.convertSolrNote(fakeTermNote);

		Assert.assertEquals(fakeThesaurus.getIdentifier(), doc
				.getFieldValue(SolrField.THESAURUSID));
		Assert.assertEquals(fakeThesaurus.getTitle(), doc
				.getFieldValue(SolrField.THESAURUSTITLE));
		Assert.assertEquals(fakeTermNote.getIdentifier(), doc
				.getFieldValue(SolrField.IDENTIFIER));
		Assert.assertEquals(fakeTermNote.getLexicalValue(), doc
				.getFieldValue(SolrField.LEXICALVALUE));
		Assert.assertEquals(fakeTermNote.getLanguage().getId(), doc
				.getFieldValue(SolrField.LANGUAGE));
		Assert.assertEquals(fakeTermNote.getCreated(), doc
				.getFieldValue(SolrField.CREATED));
		Assert.assertEquals(fakeTermNote.getModified(), doc
				.getFieldValue(SolrField.MODIFIED));
		Assert.assertEquals(Note.class.getSimpleName(), doc
				.getFieldValue(SolrField.TYPE));
		Assert.assertEquals(EntityType.NOTE, doc
				.getFieldValue(SolrField.EXT_TYPE));
	}
}
