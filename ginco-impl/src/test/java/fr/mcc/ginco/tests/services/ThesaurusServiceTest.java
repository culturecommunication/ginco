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
package fr.mcc.ginco.tests.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.ark.CustomGeneratorService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.dao.IGenericDAO.SortingTypes;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.helpers.ThesaurusHelper;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.ThesaurusServiceImpl;

public class ThesaurusServiceTest {

	@Mock(name = "thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

	@Mock(name = "thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;

	@Mock(name = "generatorService")
	private CustomGeneratorService generatorService;

    @Mock(name = "thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;

    @Mock(name = "thesaurushelper")
	private ThesaurusHelper thesaurusHelper;

	@InjectMocks
	private ThesaurusServiceImpl thesaurusService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public final void testGetThesaurusById() {
		Thesaurus mockThesaurus = mock(Thesaurus.class);
		when(thesaurusDAO.getById(anyString())).thenReturn(mockThesaurus);
		Thesaurus thesaurusRes = thesaurusService.getThesaurusById("not-empty");
		Assert.assertNotNull("Error while getting Thesaurus By Id",
				thesaurusRes);
	}

	@Test
	public final void testGetDefaultThesaurus() {
		Thesaurus thesaurusRes = thesaurusService.getDefaultThesaurus();
		Assert.assertNotNull("Error while getting default thesaurus",
				thesaurusRes);
	}

	@Test
	public final void testCreateThesaurus() throws BusinessException {
		Thesaurus mockThesaurus = mock(Thesaurus.class);
		when(thesaurusDAO.update(any(Thesaurus.class))).thenReturn(
				mockThesaurus);
		//when(generatorService.generate()).thenReturn(mockThesaurus.getIdentifier());
		Thesaurus thesaurusRes = thesaurusService.updateThesaurus(
				mockThesaurus);
		Assert.assertNotNull("Error while creating Thesaurus", thesaurusRes);
	}

	@Test
	public final void testUpdateThesaurus() throws BusinessException {
		Thesaurus mockThesaurus = mock(Thesaurus.class);
		when(thesaurusDAO.update(any(Thesaurus.class))).thenReturn(
				mockThesaurus);
		Thesaurus thesaurusRes = thesaurusService.updateThesaurus(
				mockThesaurus);
		Assert.assertNotNull("Error while updating Thesaurus", thesaurusRes);
	}

	@Test
	public final void testGetThesaurusList() {
		Thesaurus mockThesaurus1 = mock(Thesaurus.class);
		Thesaurus mockThesaurus2 = mock(Thesaurus.class);
		List<Thesaurus> mockedThesaurus = new ArrayList<Thesaurus>();
		mockedThesaurus.add(mockThesaurus1);
		mockedThesaurus.add(mockThesaurus2);
		when(thesaurusDAO.findAll(anyString(), any(SortingTypes.class)))
				.thenReturn(mockedThesaurus);
		List<Thesaurus> thesaurusRes = thesaurusService.getThesaurusList();
		Assert.assertEquals("Error while getting Thesaurus list !", 2,
				thesaurusRes.size());
	}

	@Test
	public final void testGetThesaurusLanguages() throws Exception {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setLang(getFakeLanguages());
		when(thesaurusDAO.getById(anyString())).thenReturn(fakeThesaurus);

		List<Language> actualLanguages = thesaurusService
				.getThesaurusLanguages("mockedThesaurus");
		Assert.assertEquals("error while getting thesaurus language list", 3,
				actualLanguages.size());

		Assert.assertEquals("fra", actualLanguages.get(0).getId());
		Assert.assertEquals("jpn", actualLanguages.get(1).getId());
		Assert.assertEquals("rus", actualLanguages.get(2).getId());
	}

	private Set<Language> getFakeLanguages() {
		Set<Language> langs = new HashSet<Language>();
		Language lang1 = new Language();
		lang1.setId("rus");
		lang1.setRefname("Russian");
		Language lang2 = new Language();
		lang2.setId("fra");
		lang2.setRefname("French");
		Language lang3 = new Language();
		lang3.setId("jpn");
		lang3.setRefname("Japanese");
		langs.add(lang1);
		langs.add(lang2);
		langs.add(lang3);
		return langs;
	}

}
