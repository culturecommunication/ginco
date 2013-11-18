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

package fr.mcc.ginco.tests.rest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Suggestion;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.SuggestionView;
import fr.mcc.ginco.extjs.view.utils.SuggestionViewConverter;
import fr.mcc.ginco.rest.services.SuggestionRestService;
import fr.mcc.ginco.services.ISuggestionService;

public class SuggestionRestServiceTest {

	@Mock(name = "suggestionService")
	private ISuggestionService suggestionService;

	@Mock(name = "suggestionViewConverter")
	private SuggestionViewConverter suggestionViewConverter;

	@InjectMocks
	private SuggestionRestService suggestionRestService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetSuggestionsWithConceptId() {
		Suggestion suggestion1 = new Suggestion();
		Suggestion suggestion2 = new Suggestion();
		List<Suggestion> suggestionsData = new ArrayList<Suggestion>();
		suggestionsData.add(suggestion1);
		suggestionsData.add(suggestion2);

		SuggestionView view1 = new SuggestionView();
		SuggestionView view2 = new SuggestionView();

		String conceptId = "http://data.culture.gouv.fr:/ark/123/co1";
		String termId = "";
		Integer startIndex = 0;
		Integer limit = 10;

		Mockito.when(
				suggestionService.getConceptSuggestionPaginatedList(conceptId,
						startIndex, limit)).thenReturn(suggestionsData);

		Mockito.when(suggestionService.getConceptSuggestionCount(conceptId))
				.thenReturn(new Long(2));

		Mockito.when(suggestionViewConverter.convert(suggestion1)).thenReturn(
				view1);
		Mockito.when(suggestionViewConverter.convert(suggestion2)).thenReturn(
				view2);

		ExtJsonFormLoadData<List<SuggestionView>> actualSuggestions = suggestionRestService
				.getSuggestions(conceptId, termId, startIndex, limit);

		Assert.assertEquals(new Long(2), actualSuggestions.getTotal());
		ListAssert.assertContains(actualSuggestions.getData(), view1);
		ListAssert.assertContains(actualSuggestions.getData(), view2);
	}

	@Test
	public void testGetSuggestionsWithTermId() {
		Suggestion suggestion1 = new Suggestion();
		Suggestion suggestion2 = new Suggestion();
		List<Suggestion> suggestionsData = new ArrayList<Suggestion>();
		suggestionsData.add(suggestion1);
		suggestionsData.add(suggestion2);

		SuggestionView view1 = new SuggestionView();
		SuggestionView view2 = new SuggestionView();

		String conceptId = "";
		String termId = "http://data.culture.gouv.fr:/ark/123/te1";
		Integer startIndex = 0;
		Integer limit = 10;

		Mockito.when(
				suggestionService.getTermSuggestionPaginatedList(termId,
						startIndex, limit)).thenReturn(suggestionsData);

		Mockito.when(suggestionService.getTermSuggestionCount(termId))
				.thenReturn(new Long(2));

		Mockito.when(suggestionViewConverter.convert(suggestion1)).thenReturn(
				view1);
		Mockito.when(suggestionViewConverter.convert(suggestion2)).thenReturn(
				view2);

		ExtJsonFormLoadData<List<SuggestionView>> actualSuggestions = suggestionRestService
				.getSuggestions(conceptId, termId, startIndex, limit);

		Assert.assertEquals(new Long(2), actualSuggestions.getTotal());
		ListAssert.assertContains(actualSuggestions.getData(), view1);
		ListAssert.assertContains(actualSuggestions.getData(), view2);
	}

	@Test(expected = BusinessException.class)
	public void testGetSuggestionsWithNoId() {
		Suggestion suggestion1 = new Suggestion();
		Suggestion suggestion2 = new Suggestion();
		List<Suggestion> suggestionsData = new ArrayList<Suggestion>();
		suggestionsData.add(suggestion1);
		suggestionsData.add(suggestion2);

		String conceptId = "";
		String termId = "";
		Integer startIndex = 0;
		Integer limit = 10;

		Mockito.when(
				suggestionService.getTermSuggestionPaginatedList(termId,
						startIndex, limit)).thenReturn(suggestionsData);

		Mockito.when(suggestionService.getTermSuggestionCount(termId))
				.thenReturn(new Long(2));

		suggestionRestService.getSuggestions(conceptId, termId, startIndex,
				limit);
	}

	@Test
	public void testUpdateSuggestion() {

		List<SuggestionView> suggestionViews = new ArrayList<SuggestionView>();

		SuggestionView view1 = new SuggestionView();
		SuggestionView view2 = new SuggestionView();

		suggestionViews.add(view1);
		suggestionViews.add(view2);

		Suggestion suggestion1 = new Suggestion();
		Suggestion suggestion2 = new Suggestion();

		Mockito.when(suggestionViewConverter.convert(view1)).thenReturn(
				suggestion1);
		Mockito.when(suggestionViewConverter.convert(view2)).thenReturn(
				suggestion2);

		Mockito.when(suggestionService.createOrUpdateSuggestion(suggestion1))
				.thenReturn(suggestion1);
		Mockito.when(suggestionService.createOrUpdateSuggestion(suggestion2))
				.thenReturn(suggestion2);

		Mockito.when(suggestionViewConverter.convert(suggestion1)).thenReturn(
				view1);
		Mockito.when(suggestionViewConverter.convert(suggestion2)).thenReturn(
				view2);

		ExtJsonFormLoadData<List<SuggestionView>> actualSuggestions = suggestionRestService
				.updateSuggestions(suggestionViews);

		Assert.assertEquals(new Long(2), actualSuggestions.getTotal());
		ListAssert.assertContains(actualSuggestions.getData(), view1);
		ListAssert.assertContains(actualSuggestions.getData(), view2);

	}

	@Test
	public void testCreateSuggestionWithConceptId() {

		String conceptId = "http://data.culture.gouv.fr:/ark/123/co1";
		String termId = "";

		List<SuggestionView> suggestionViews = new ArrayList<SuggestionView>();

		SuggestionView view1 = new SuggestionView();
		SuggestionView view2 = new SuggestionView();
		suggestionViews.add(view1);
		suggestionViews.add(view2);

		Suggestion suggestion1 = new Suggestion();
		Suggestion suggestion2 = new Suggestion();

		Mockito.when(suggestionViewConverter.convert(view1)).thenReturn(
				suggestion1);
		Mockito.when(suggestionViewConverter.convert(view2)).thenReturn(
				suggestion2);

		Mockito.when(suggestionService.createOrUpdateSuggestion(suggestion1))
				.thenReturn(suggestion1);
		Mockito.when(suggestionService.createOrUpdateSuggestion(suggestion2))
				.thenReturn(suggestion2);

		Mockito.when(suggestionViewConverter.convert(suggestion1)).thenReturn(
				view1);
		Mockito.when(suggestionViewConverter.convert(suggestion2)).thenReturn(
				view2);

		ExtJsonFormLoadData<List<SuggestionView>> actualSuggestions = suggestionRestService
				.createSuggestions(suggestionViews, conceptId, termId);

		Assert.assertEquals(new Long(2), actualSuggestions.getTotal());
		ListAssert.assertContains(actualSuggestions.getData(), view1);
		ListAssert.assertContains(actualSuggestions.getData(), view2);
		Assert.assertEquals("http://data.culture.gouv.fr:/ark/123/co1",
				actualSuggestions.getData().get(0).getConceptId());
		Assert.assertEquals("http://data.culture.gouv.fr:/ark/123/co1",
				actualSuggestions.getData().get(1).getConceptId());

	}

	@Test
	public void testCreateSuggestionWithTermId() {

		String conceptId = "";
		String termId = "http://data.culture.gouv.fr:/ark/123/te1";

		List<SuggestionView> suggestionViews = new ArrayList<SuggestionView>();

		SuggestionView view1 = new SuggestionView();
		SuggestionView view2 = new SuggestionView();
		suggestionViews.add(view1);
		suggestionViews.add(view2);

		Suggestion suggestion1 = new Suggestion();
		Suggestion suggestion2 = new Suggestion();

		Mockito.when(suggestionViewConverter.convert(view1)).thenReturn(
				suggestion1);
		Mockito.when(suggestionViewConverter.convert(view2)).thenReturn(
				suggestion2);

		Mockito.when(suggestionService.createOrUpdateSuggestion(suggestion1))
				.thenReturn(suggestion1);
		Mockito.when(suggestionService.createOrUpdateSuggestion(suggestion2))
				.thenReturn(suggestion2);

		Mockito.when(suggestionViewConverter.convert(suggestion1)).thenReturn(
				view1);
		Mockito.when(suggestionViewConverter.convert(suggestion2)).thenReturn(
				view2);

		ExtJsonFormLoadData<List<SuggestionView>> actualSuggestions = suggestionRestService
				.createSuggestions(suggestionViews, conceptId, termId);

		Assert.assertEquals(new Long(2), actualSuggestions.getTotal());
		ListAssert.assertContains(actualSuggestions.getData(), view1);
		ListAssert.assertContains(actualSuggestions.getData(), view2);
		Assert.assertEquals("http://data.culture.gouv.fr:/ark/123/te1",
				actualSuggestions.getData().get(0).getTermId());
		Assert.assertEquals("http://data.culture.gouv.fr:/ark/123/te1",
				actualSuggestions.getData().get(1).getTermId());

	}

	@Test(expected = BusinessException.class)
	public void testCreateSuggestionWithNoId() {

		String conceptId = "";
		String termId = "";

		List<SuggestionView> suggestionViews = new ArrayList<SuggestionView>();

		SuggestionView view1 = new SuggestionView();
		SuggestionView view2 = new SuggestionView();
		suggestionViews.add(view1);
		suggestionViews.add(view2);

		suggestionRestService.createSuggestions(suggestionViews, conceptId,
				termId);

	}

}