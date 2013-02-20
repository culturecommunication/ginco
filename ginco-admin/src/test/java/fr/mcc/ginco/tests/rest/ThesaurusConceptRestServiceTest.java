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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.IThesaurusConceptService;
import fr.mcc.ginco.IThesaurusTermService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.rest.services.ThesaurusConceptRestService;
import fr.mcc.ginco.tests.LoggerTestUtil;
import fr.mcc.ginco.utils.DateUtil;

public class ThesaurusConceptRestServiceTest {
	
	@Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@InjectMocks
	private ThesaurusConceptRestService thesaurusConceptRestService = new ThesaurusConceptRestService();
	
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(thesaurusConceptRestService);
	}

	
	/**
	 * Test to put a concept
	 * @throws BusinessException 
	 */
	/*@Test
	public final void putNewConceptWithTwoTermsWithOnePrefered() throws BusinessException {
		ThesaurusTerm fakeTerm1 = getFakeThesaurusTermWithNonMandatoryEmptyFields("fakeTerm1");
		ThesaurusTerm fakeTerm2 = getFakeThesaurusTermWithNonMandatoryEmptyFields("fakeTerm2");
		fakeTerm1.setPrefered(true);
		
		ThesaurusTermView fakeTermView1 = new ThesaurusTermView(fakeTerm1);
		ThesaurusTermView fakeTermView2 = new ThesaurusTermView(fakeTerm2);
		
		List<ThesaurusTermView> termViews = new ArrayList<ThesaurusTermView>();
		termViews.add(fakeTermView1);
		termViews.add(fakeTermView2);
		
		//ThesaurusConcept fakeThesaurusConcept = getFakeThesaurusConceptWithNonMandatoryEmptyFields("");
		ThesaurusConceptView fakeConceptView = new ThesaurusConceptView("", "", "", "", termViews);
		
		//Mockito.when(thesaurusConceptService.getThesaurusConceptById("fakeConcept1")).thenReturn(fakeThesaurusConcept);
		
		ThesaurusConceptView actualResponse = thesaurusConceptRestService.updateConcept(fakeConceptView);
		Assert.assertEquals(fakeConceptView.getIdentifier(), actualResponse.getIdentifier());
		
	}*/
	
	private ThesaurusTerm getFakeThesaurusTermWithNonMandatoryEmptyFields(String id) {
		ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
		fakeThesaurusTerm.setIdentifier(id);
		fakeThesaurusTerm.setThesaurusId(new Thesaurus());
		fakeThesaurusTerm.setLanguage(new Language());
		fakeThesaurusTerm.setLexicalValue("lexicale value");
		return fakeThesaurusTerm;
	}
	
	private ThesaurusConcept getFakeThesaurusConceptWithNonMandatoryEmptyFields(String id) {
		ThesaurusConcept fakeThesaurusConcept = new ThesaurusConcept();
		fakeThesaurusConcept.setIdentifier(id);
		fakeThesaurusConcept.setCreated(DateUtil.nowDate());
		fakeThesaurusConcept.setModified(DateUtil.nowDate());
		//fakeThesaurusConcept.setThesaurus(Mockito.any(Thesaurus.class));
		return fakeThesaurusConcept;
	}
}
