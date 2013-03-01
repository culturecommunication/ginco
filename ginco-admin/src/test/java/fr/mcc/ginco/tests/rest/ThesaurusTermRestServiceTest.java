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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.rest.services.ThesaurusTermRestService;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class ThesaurusTermRestServiceTest{
	
	@Mock(name="thesaurusTermService")
	private IThesaurusTermService termService;
	
	@Mock(name="thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Mock(name="languagesService")
	private ILanguagesService languagesService;
	
    @Mock(name="termViewConverter")
    private TermViewConverter termViewConverter;
	
	@InjectMocks
	private ThesaurusTermRestService thesaurusTermRestService = new ThesaurusTermRestService();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(thesaurusTermRestService);
	}
	/**
	 * Test to get all Thesaurus Sandboxed Items method
	 * @throws BusinessException 
	 */
	@Test
	public final void getAllThesaurusSandboxedTerms() throws BusinessException {

		//Generating mocked Thesauruses in a single list "allThesaurus"
		ThesaurusTerm fakeThesaurusTerm1 = getFakeThesaurusTermWithNonMandatoryEmptyFields("mock1");
		ThesaurusTerm fakeThesaurusTerm2 = getFakeThesaurusTermWithNonMandatoryEmptyFields("mock2");
		ThesaurusTermView mockedThesaurusTermView1 = new ThesaurusTermView(fakeThesaurusTerm1);
		
		List<ThesaurusTerm> fakeTerms = new ArrayList<ThesaurusTerm>();
		fakeTerms.add(fakeThesaurusTerm1);
		fakeTerms.add(fakeThesaurusTerm2);
		
		//Mocking the service
		Mockito.when(termService.getPaginatedThesaurusSandoxedTermsList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(fakeTerms);
		Mockito.when(termService.getSandboxedTermsCount(Mockito.anyString())).thenReturn((long)fakeTerms.size());
		
		//Getting thesauruses from rest webservice method and testing
		ExtJsonFormLoadData<List<ThesaurusTermView>> actualResponse = thesaurusTermRestService.getSandboxedThesaurusTerms(1, 2, "1");
		
		//Assert
		Assert.assertEquals(new Long(2), actualResponse.getTotal());
		Assert.assertEquals(mockedThesaurusTermView1.getIdentifier(), actualResponse.getData().get(0).getIdentifier());
	}
	
	/**
	 * Test the updateTerm method
	 * @throws BusinessException 
	 */
	@Test
	public final void testUpdateTerm() throws BusinessException {
		
		//Generating fake objects
		ThesaurusTerm fakeThesaurusTerm1 = getFakeThesaurusTermWithNonMandatoryEmptyFields("fake1");
		Thesaurus fakeThesaurus1 =  new Thesaurus();
		fakeThesaurus1.setIdentifier("fakeTh1");
		
		Language fakeLanguage1 = new Language();
		fakeLanguage1.setId("fra");
		fakeLanguage1.setRefname("fra");
		
		//Creating a Thesaurus View for the test
		ThesaurusTermView fakeThesaurusTermView = new ThesaurusTermView();
		fakeThesaurusTermView.setIdentifier("fake1");
		fakeThesaurusTermView.setLexicalValue("lexicale value");
		
		Mockito.when(termService.getThesaurusTermById(Mockito.anyString())).thenReturn(fakeThesaurusTerm1);
		Mockito.when(thesaurusService.getThesaurusById(Mockito.anyString())).thenReturn(fakeThesaurus1);
		Mockito.when(languagesService.getLanguageById(Mockito.anyString())).thenReturn(fakeLanguage1);
		Mockito.when(termViewConverter.convert(Mockito.any(ThesaurusTermView.class))).thenReturn(fakeThesaurusTerm1);
		
		Mockito.when(termService.updateThesaurusTerm(any(ThesaurusTerm.class))).thenReturn(fakeThesaurusTerm1);

		ThesaurusTermView actualResponse = thesaurusTermRestService.updateTerm(fakeThesaurusTermView);
		
		//Assert
		Assert.assertEquals("fake1", actualResponse.getIdentifier());
	}
	/**
	 * Test the createTerm method
	 * @throws BusinessException 
	 */
	@Test
	public final void testCreateTerm() throws BusinessException {
		
		//Generating mocked objects
		ThesaurusTerm fakeThesaurusTerm1 = new ThesaurusTerm();
		fakeThesaurusTerm1.setIdentifier("");
		ThesaurusTerm fakeThesaurusCreationReturn = new ThesaurusTerm();

		
		Language fakeLanguage1 = new Language();
		fakeLanguage1.setId("fra");
		fakeLanguage1.setRefname("fra");
		
		Thesaurus fakeThesaurus1 =  new Thesaurus();
		fakeThesaurus1.setIdentifier("mockTh1");
		
		fakeThesaurusCreationReturn.setIdentifier("mock1");
		fakeThesaurusCreationReturn.setThesaurus(fakeThesaurus1);
		fakeThesaurusCreationReturn.setLanguage(fakeLanguage1);

		//Creating a Thesaurus View for the test
		ThesaurusTermView fakeThesaurusTermView = new ThesaurusTermView();
		fakeThesaurusTermView.setIdentifier("mock1");
		fakeThesaurusTermView.setLexicalValue("lexicale value");
		
		Mockito.when(termService.getThesaurusTermById(Mockito.anyString())).thenReturn(fakeThesaurusTerm1);
		Mockito.when(thesaurusService.getThesaurusById(Mockito.anyString())).thenReturn(fakeThesaurus1);
		Mockito.when(languagesService.getLanguageById(Mockito.anyString())).thenReturn(fakeLanguage1);
		Mockito.when(termViewConverter.convert(Mockito.any(ThesaurusTermView.class))).thenReturn(fakeThesaurusTerm1);
		
		Mockito.when(termService.updateThesaurusTerm(any(ThesaurusTerm.class))).thenReturn(fakeThesaurusCreationReturn);
		
		ThesaurusTermView actualResponse = thesaurusTermRestService.updateTerm(fakeThesaurusTermView);
		
		//Assert
		Assert.assertEquals("mock1", actualResponse.getIdentifier());
	}
	
	private ThesaurusTerm getFakeThesaurusTermWithNonMandatoryEmptyFields(String id) {
		ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
		fakeThesaurusTerm.setIdentifier(id);
		fakeThesaurusTerm.setThesaurus(new Thesaurus());
		fakeThesaurusTerm.setLanguage(new Language());
		fakeThesaurusTerm.setLexicalValue("lexicale value");
		return fakeThesaurusTerm;
	}
	@Test
	public void testGetThesaurusTerm() throws BusinessException {
		ThesaurusTerm term =getFakeThesaurusTermWithNonMandatoryEmptyFields("fake-id");
		when(termService.getThesaurusTermById(anyString())).thenReturn(term);	
		ThesaurusTermView actualResponse = thesaurusTermRestService.getThesaurusTerm("fake-id");
		Assert.assertEquals("lexicale value", actualResponse.getLexicalValue());
	}
	@Test(expected=BusinessException.class)
	public void testGetThesaurusTermWithWrongId() throws BusinessException {
		
		when(termService.getThesaurusTermById(anyString())).thenThrow(BusinessException.class);
			ThesaurusTermView actualResponse = thesaurusTermRestService.getThesaurusTerm("fake-id");
	}
	
}