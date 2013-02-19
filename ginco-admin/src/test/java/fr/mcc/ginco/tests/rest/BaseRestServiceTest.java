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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import fr.mcc.ginco.IThesaurusService;
import fr.mcc.ginco.ThesaurusServiceImpl;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.rest.services.BaseRestService;
import fr.mcc.ginco.utils.DateUtil;

public class BaseRestServiceTest {
	private BaseRestService baseRestService = new BaseRestService();
	
	/**
	 * Test the getVocabularies method with empty values for non mandatory
	 * fields in Thesauruses objects
	 */
	@Test
	public final void testGetVocabularies() {
		
		//Generating mocked Thesauruses in a single list "allThesaurus"
		Thesaurus mockedThesaurus1 = getMockedThesaurusWithAllFields("mock1");
		Thesaurus mockedThesaurus2 = getMockedThesaurusWithAllFields("mock2");
		Thesaurus mockedThesaurus3 = getMockedThesaurusWithAllFields("mock3");
		List<Thesaurus> allThesaurus = new ArrayList<Thesaurus>();
		allThesaurus.add(mockedThesaurus1);
		allThesaurus.add(mockedThesaurus2);
		allThesaurus.add(mockedThesaurus3);
		
		//Mocking the service
		baseRestService.setThesaurusService(getMockedIThesaurusService(allThesaurus));
		
		//Getting thesauruses from rest webservice method and testing
		List<IThesaurusListNode> listThesauruses = baseRestService.getVocabularies();
		Assert.assertEquals(3, listThesauruses.size());
	}
	
	private IThesaurusService getMockedIThesaurusService(
			List<Thesaurus> mockedThesauruses) {
		IThesaurusService mockedService = Mockito
				.mock(ThesaurusServiceImpl.class);
		for (int i = 0; i < mockedThesauruses.size(); i++) {
			Mockito.when(
					mockedService.getThesaurusById(mockedThesauruses.get(i)
							.getIdentifier())).thenReturn(
					mockedThesauruses.get(i));
		}

		Mockito.when(mockedService.getThesaurusList()).thenReturn(
				mockedThesauruses);
		return mockedService;
	}
	
	private Thesaurus getMockedThesaurusWithAllFields(String id) {
		Thesaurus mockedThesaurus = getMockedThesaurusWithNonMandatoryEmptyFields(id);

		ThesaurusFormat mockedFormat = Mockito.mock(ThesaurusFormat.class);
		Mockito.when(mockedFormat.getLabel()).thenReturn("Fake Format");

		ThesaurusType mockedType = Mockito.mock(ThesaurusType.class);
		Mockito.when(mockedType.getLabel()).thenReturn("Fake Type");

		ThesaurusOrganization mockedCreator = Mockito
				.mock(ThesaurusOrganization.class);
		Mockito.when(mockedCreator.getName()).thenReturn("Fake Creator Name");
		Mockito.when(mockedCreator.getHomepage()).thenReturn(
				"Fake Creator Homepage");

		// Format, type and creator might be null (not mandatory)
		Mockito.when(mockedThesaurus.getFormat()).thenReturn(mockedFormat);
		Mockito.when(mockedThesaurus.getType()).thenReturn(mockedType);
		Mockito.when(mockedThesaurus.getCreator()).thenReturn(mockedCreator);

		return mockedThesaurus;
	}
	
	private Thesaurus getMockedThesaurusWithNonMandatoryEmptyFields(String id) {
		Thesaurus mockedThesaurus = Mockito.mock(Thesaurus.class);
		// Format, type and creator might be null (not mandatory)
		Mockito.when(mockedThesaurus.getFormat()).thenReturn(null);
		Mockito.when(mockedThesaurus.getType()).thenReturn(null);
		Mockito.when(mockedThesaurus.getCreator()).thenReturn(null);

		Mockito.when(mockedThesaurus.getIdentifier()).thenReturn(id);
		Mockito.when(mockedThesaurus.getTitle()).thenReturn("Thesaurus Title");
		Mockito.when(mockedThesaurus.getContributor()).thenReturn(
				"Fake Contributor");
		Mockito.when(mockedThesaurus.getCoverage()).thenReturn("Fake Coverage");

        Calendar gregCalendar = new GregorianCalendar();
        gregCalendar.setTime(DateUtil.dateFromString("2013-02-15 02:02:02"));
		Mockito.when(mockedThesaurus.getCreated()).thenReturn(gregCalendar.getTime());
		Mockito.when(mockedThesaurus.getDate()).thenReturn(gregCalendar.getTime());
		Mockito.when(mockedThesaurus.getDescription()).thenReturn(
				"Fake Description");
		Set<Language> mockedLanguages = getMockedLanguages();

		Mockito.when(mockedThesaurus.getLang()).thenReturn(mockedLanguages);

		Mockito.when(mockedThesaurus.getPublisher()).thenReturn(
				"Fake Publisher");
		Mockito.when(mockedThesaurus.getRelation()).thenReturn("Fake Relation");
		Mockito.when(mockedThesaurus.getRights()).thenReturn("Fake Rights");
		Mockito.when(mockedThesaurus.getSource()).thenReturn("Fake Source");
		Mockito.when(mockedThesaurus.getSubject()).thenReturn("Fake Subject");

		return mockedThesaurus;
	}
	
	private Set<Language> getMockedLanguages() {
		Language mockedLanguage1 = Mockito.mock(Language.class);
		Mockito.when(mockedLanguage1.getId()).thenReturn("fra");

		Language mockedLanguage2 = Mockito.mock(Language.class);
		Mockito.when(mockedLanguage2.getId()).thenReturn("rus");

		Set<Language> mockedLanguages = new HashSet<Language>();
		mockedLanguages.add(mockedLanguage1);
		mockedLanguages.add(mockedLanguage2);

		return mockedLanguages;
	}
	
}