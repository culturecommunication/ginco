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
package fr.mcc.ginco.tests.extjs.view.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Suggestion;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.extjs.view.pojo.SuggestionView;
import fr.mcc.ginco.extjs.view.utils.SuggestionViewConverter;
import fr.mcc.ginco.services.ISuggestionService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.DateUtil;

public class SuggestionViewConverterTest {

	@Mock(name = "suggestionService")
	private ISuggestionService suggestionService;

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@InjectMocks
	private SuggestionViewConverter converter = new SuggestionViewConverter();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertFromSuggestion() {
		Suggestion suggestion = new Suggestion();
		suggestion.setIdentifier(1);
		suggestion.setContent("suggestion content");
		suggestion.setCreator("creator@culture.gouv.fr");
		suggestion.setRecipient("recipient@culture.gouv.fr");
		suggestion.setCreated((new GregorianCalendar(2013, 10, 15, 14, 0, 0))
				.getTime());

		ThesaurusConcept concept = new ThesaurusConcept();
		concept.setIdentifier("http://data/culture.gouv.fr/123/c1");
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://data/culture.gouv.fr/123/th1");
		concept.setThesaurus(th);

		suggestion.setConcept(concept);

		SuggestionView actualView = converter.convert(suggestion);
		Assert.assertEquals(1, actualView.getIdentifier().intValue());
		Assert.assertEquals("suggestion content", actualView.getContent());
		Assert.assertEquals("creator@culture.gouv.fr", actualView.getCreator());
		Assert.assertEquals("recipient@culture.gouv.fr",
				actualView.getRecipient());
		Assert.assertEquals("2013-11-15 14:00:00", actualView.getCreated());
		Assert.assertEquals("http://data/culture.gouv.fr/123/c1",
				actualView.getConceptId());
		Assert.assertEquals("http://data/culture.gouv.fr/123/th1",
				actualView.getThesaurusId());
		Assert.assertNull(actualView.getTermId());

		suggestion.setConcept(null);

		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("http://data/culture.gouv.fr/123/ter1");
		Thesaurus th2 = new Thesaurus();
		th2.setIdentifier("http://data/culture.gouv.fr/123/th2");
		term.setThesaurus(th2);
		suggestion.setTerm(term);

		SuggestionView actualView2 = converter.convert(suggestion);

		Assert.assertEquals("http://data/culture.gouv.fr/123/ter1",
				actualView2.getTermId());
		Assert.assertEquals("http://data/culture.gouv.fr/123/th2",
				actualView2.getThesaurusId());
		Assert.assertNull(actualView2.getConceptId());

	}

	@Test
	public void testConvertFromSuggestionViewNotExistingWithConceptId() {

		SuggestionView suggestionView = new SuggestionView();
		suggestionView.setContent("Content");
		suggestionView.setCreator("creator@test.com");
		suggestionView.setRecipient("recipient@test.com");
		suggestionView.setConceptId("http://data/culture.gouv.fr:/ark/123/co1");

		ThesaurusConcept concept = new ThesaurusConcept();
		concept.setIdentifier("http://data/culture.gouv.fr:/ark/123/co1");
		Mockito.when(
				thesaurusConceptService
						.getThesaurusConceptById("http://data/culture.gouv.fr:/ark/123/co1"))
				.thenReturn(concept);

		Suggestion suggestion = converter.convert(suggestionView);

		Assert.assertEquals("Content", suggestion.getContent());
		Assert.assertEquals("creator@test.com", suggestion.getCreator());
		Assert.assertEquals("recipient@test.com", suggestion.getRecipient());
		Assert.assertEquals(concept, suggestion.getConcept());
	}

	@Test
	public void testConvertFromSuggestionViewNotExistingWithTermId() {
		SuggestionView suggestionView = new SuggestionView();
		suggestionView.setContent("Content");
		suggestionView.setCreator("creator@test.com");
		suggestionView.setRecipient("recipient@test.com");
		suggestionView.setTermId("http://data/culture.gouv.fr:/ark/123/te1");

		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("http://data/culture.gouv.fr:/ark/123/te1");			
		
		Mockito.when(
				thesaurusTermService
						.getThesaurusTermById("http://data/culture.gouv.fr:/ark/123/te1"))
				.thenReturn(term);

		Suggestion suggestion = converter.convert(suggestionView);

		Assert.assertEquals("Content", suggestion.getContent());
		Assert.assertEquals("creator@test.com", suggestion.getCreator());
		Assert.assertEquals("recipient@test.com", suggestion.getRecipient());
		Assert.assertEquals(term, suggestion.getTerm());

	}

	@Test
	public void testConvertFromSuggestionViewExistingWithTermId() {
		SuggestionView suggestionView = new SuggestionView();
		suggestionView.setIdentifier(2);
		suggestionView.setContent("Content");
		suggestionView.setCreator("creator@test.com");
		suggestionView.setRecipient("recipient@test.com");
		suggestionView.setTermId("http://data/culture.gouv.fr:/ark/123/te1");

		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("http://data/culture.gouv.fr:/ark/123/te1");
		Mockito.when(
				thesaurusTermService
						.getThesaurusTermById("http://data/culture.gouv.fr:/ark/123/te1"))
				.thenReturn(term);

		Date now = DateUtil.nowDate();
		Suggestion existingSuggestion = new Suggestion();
		existingSuggestion.setCreated(now);

		Mockito.when(suggestionService.getSuggestionById(2)).thenReturn(
				existingSuggestion);
		Suggestion suggestion = converter.convert(suggestionView);

		Assert.assertEquals("Content", suggestion.getContent());
		Assert.assertEquals("creator@test.com", suggestion.getCreator());
		Assert.assertEquals("recipient@test.com", suggestion.getRecipient());
		Assert.assertEquals(term, suggestion.getTerm());
		Assert.assertEquals(existingSuggestion, suggestion);
		Assert.assertEquals(now, suggestion.getCreated());
	}

}
