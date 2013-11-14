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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.extjs.view.utils.ThesaurusViewConverter;
import fr.mcc.ginco.rest.services.ThesaurusRestService;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusFormatService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTypeService;

public class ThesaurusRestServiceTest {

	@Mock(name = "thesaurusService")
	private IThesaurusService thesaurusService;

	@Mock(name = "languagesService")
	private ILanguagesService languagesService;

	@Mock(name = "thesaurusFormatService")
	private IThesaurusFormatService thesaurusFormatService;

	@Mock(name = "thesaurusTypeService")
	private IThesaurusTypeService thesaurusTypeService;

	@Mock(name = "thesaurusViewConverter")
	private ThesaurusViewConverter thesaurusViewConverter;

	@InjectMocks
	private ThesaurusRestService thesaurusRestService = new ThesaurusRestService();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public final void testGetDefaultThesaurus(){
		String thesaurusId = "";
		when(thesaurusViewConverter.convert(any(Thesaurus.class))).thenReturn( new ThesaurusView());
		ThesaurusView view = thesaurusRestService.getVocabularyById(thesaurusId);
		Assert.assertNotNull(view);
	}

	/**
	 * Test the putVocabularyById method with a mock vocabulary
	 * @throws BusinessException
	 *
	 */
	@Test
	public final void testCreateThesaurus() throws BusinessException {
		when(thesaurusViewConverter.convert(any(ThesaurusView.class))).thenReturn( new Thesaurus());
		when(thesaurusService.updateThesaurus(any(Thesaurus.class))).thenReturn(new Thesaurus());
		when(thesaurusViewConverter.convert(any(Thesaurus.class))).thenReturn( new ThesaurusView());

		ThesaurusView view = thesaurusRestService
				.updateVocabulary(new ThesaurusView());

		Assert.assertNotNull(view);

	}

	@Test
	public final void testUpdateThesaurus() throws BusinessException {
		Thesaurus existingTh = new Thesaurus();
		existingTh.setIdentifier("any-id");
		when(thesaurusViewConverter.convert(any(ThesaurusView.class))).thenReturn(existingTh);
		when(thesaurusService.updateThesaurus(any(Thesaurus.class))).thenReturn(new Thesaurus());
		when(thesaurusViewConverter.convert(any(Thesaurus.class))).thenReturn( new ThesaurusView());

		// Testing putVocabularyById method
		ThesaurusView view = thesaurusRestService
				.updateVocabulary(new ThesaurusView());

		Assert.assertNotNull(view);
	}


	/**
	 * Gets all top languages
	 * @throws BusinessException
	 */
	@Test
	public final void testGetTopLanguagesAll() throws BusinessException {
		List<Language> langs = getFakeLanguages();
		when(languagesService.getTopLanguagesList()).thenReturn(langs);
		ExtJsonFormLoadData<List<Language>> allLanguages = thesaurusRestService
				.getTopLanguages("");
		Assert.assertEquals(2, allLanguages.getData().size());
	}

	/**
	 * Gets the languages relative to a thesaurus
	 */
	@Test
	public final void testGetTopLanguagesByThesaurus() throws BusinessException{
		List<Language> langs = getFakeLanguages();
		when(thesaurusService.getThesaurusLanguages(anyString())).thenReturn(
				langs);
		ExtJsonFormLoadData<List<Language>> allLanguages = thesaurusRestService
				.getTopLanguages("notempty");
		Assert.assertEquals(2, allLanguages.getData().size());
		Assert.assertEquals("fra", allLanguages.getData().get(0).getId());

	}

	private List<Language> getFakeLanguages() {
		List<Language> langs = new LinkedList<Language>();
		Language lang1 = new Language();
		lang1.setId("rus");
		Language lang2 = new Language();
		lang1.setId("fra");
		langs.add(lang1);
		langs.add(lang2);
		return langs;
	}

}