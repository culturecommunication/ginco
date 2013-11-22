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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.services.LanguagesServiceImpl;

public class LanguagesServiceTest {

	@Mock
	private ILanguageDAO languagesDAO;

	@InjectMocks
	private LanguagesServiceImpl languagesService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test getting languages list with 3 cases : - The favorites languages are
	 * listed firstly - The other languages are listed alphabetically - With a
	 * start index + limit of items > items present in DB
	 */
	@Test
	public final void testGetLanguagesListList() {
		Language defaultLang = new Language();
		List<Language> langs = new ArrayList<Language>();
		langs.add(defaultLang);
		Mockito.when(languagesDAO.findPaginatedItems(0, 50)).thenReturn(langs);
		List<Language> actualResponse = languagesService
				.getLanguagesList(0, 50);
		Assert.assertEquals("Error fetching all Languages", 1,
				actualResponse.size());		
	}

	/**
	 * Test getting top languages
	 */
	@Test
	public final void testGetTopLanguagesList() {
		Language defaultLang = new Language();
		List<Language> langs = new ArrayList<Language>();
		langs.add(defaultLang);
		Mockito.when(languagesDAO.findTopLanguages()).thenReturn(langs);
		List<Language> actualResponse = languagesService.getTopLanguagesList();
		Assert.assertEquals("Error fetching all top languages", 1,
				actualResponse.size());
	}

	/**
	 * Test getting the number of languages
	 */
	@Test
	public final void testCountLanguages() {
		Mockito.when(languagesDAO.count()).thenReturn((long) 6);
		Long actualResponse = languagesService.getLanguageCount();
		Assert.assertEquals("Error counting Languages", 6,
				actualResponse.longValue());
	}

}
