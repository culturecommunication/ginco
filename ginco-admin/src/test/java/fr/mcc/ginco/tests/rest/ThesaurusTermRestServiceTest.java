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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import fr.mcc.ginco.IThesaurusTermService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.rest.services.ThesaurusTermRestService;

public class ThesaurusTermRestServiceTest{
	
	@Mock(name="thesaurusTermService")
	private IThesaurusTermService termService;
	
	@InjectMocks
	private ThesaurusTermRestService thesaurusTermRestService = new ThesaurusTermRestService();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		 ReflectionUtils.doWithFields(thesaurusTermRestService.getClass(), new ReflectionUtils.FieldCallback() {

	            public void doWith(Field field) throws IllegalArgumentException,
	                    IllegalAccessException {
	                ReflectionUtils.makeAccessible(field);

	                if (field.getAnnotation(Log.class) != null) {
	                    Logger logger = LoggerFactory.getLogger(thesaurusTermRestService.getClass());
	                    field.set(thesaurusTermRestService, logger);
	                }
	            }
	        });
		 
	}
	/**
	 * Test the getAllThesaurusTerms method
	 */
	@Test
	public final void getAllThesaurusTerms() {
		
		//Generating mocked Thesauruses in a single list "allThesaurus"
		ThesaurusTerm fakeThesaurusTerm1 = getFakeThesaurusTermWithNonMandatoryEmptyFields("mock1");
		ThesaurusTerm fakeThesaurusTerm2 = getFakeThesaurusTermWithNonMandatoryEmptyFields("mock2");
		ThesaurusTermView mockedThesaurusTermView1 = new ThesaurusTermView(fakeThesaurusTerm1);
		
		List<ThesaurusTerm> fakeTerms = new ArrayList<ThesaurusTerm>();
		fakeTerms.add(fakeThesaurusTerm1);
		fakeTerms.add(fakeThesaurusTerm2);
		
		//Mocking the service
		Mockito.when(termService.getPaginatedThesaurusList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(fakeTerms);
		Mockito.when(termService.getCount()).thenReturn((long)fakeTerms.size());
		
		//Getting thesauruses from rest webservice method and testing
		ExtJsonFormLoadData<List<ThesaurusTermView>> actualResponse = thesaurusTermRestService.getAllThesaurusTerms(1, 2, "1");
		
		//Assert
		Assert.assertEquals(new Long(2), actualResponse.getTotal());
		Assert.assertEquals(mockedThesaurusTermView1.getIdentifier(), actualResponse.getData().get(0).getIdentifier());
	}
	
	private ThesaurusTerm getFakeThesaurusTermWithNonMandatoryEmptyFields(String id) {
		ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
		fakeThesaurusTerm.setThesaurusId(new Thesaurus());
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