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

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.rest.services.ThesaurusConceptRestService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.tests.LoggerTestUtil;
import fr.mcc.ginco.utils.DateUtil;

public class ThesaurusConceptRestServiceTest {
	
	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;
	
	@Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@InjectMocks
	private ThesaurusConceptRestService thesaurusConceptRestService = new ThesaurusConceptRestService();
	
    @Inject
    @Named("termViewConverter")
    private TermViewConverter termViewConverter;
	
	
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
	public final void putNewTopConceptWithOnlyOneTermWhichIsPrefered() throws BusinessException {
		ThesaurusTerm fakeTerm1 = getFakeThesaurusTermWithNonMandatoryEmptyFields("fakeTerm1");
		fakeTerm1.setPrefered(true);
		
		ThesaurusTermView fakeTermView1 = new ThesaurusTermView(fakeTerm1);
		
		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
		terms.add(fakeTerm1);
		List<ThesaurusTermView> termViews = new ArrayList<ThesaurusTermView>();
		termViews.add(fakeTermView1);
		
		ThesaurusConcept fakeThesaurusConcept = getFakeThesaurusConceptWithNonMandatoryEmptyFields("fakeConcept1");
		ThesaurusConceptView fakeConceptView = new ThesaurusConceptView("", "", "", "1",termViews);
		
		String principal = "unknown";
		IUser user = new SimpleUserImpl();
		user.setName(principal);
		
		//Mockito.when(thesaurusConceptService.getThesaurusConceptById("")).thenReturn(fakeThesaurusConcept);
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("1");
		Mockito.when(thesaurusService.getThesaurusById("1")).thenReturn(fakeThesaurus);
		Mockito.when(termViewConverter.convert(Mockito.any(ThesaurusTermView.class))).thenReturn(fakeTerm1);
		Mockito.when(thesaurusTermService.getPreferedTerms(terms)).thenReturn(terms);
		Mockito.when(thesaurusConceptService.createThesaurusConcept(Mockito.any(ThesaurusConcept.class), Mockito.any(IUser.class))).thenReturn(fakeThesaurusConcept);
		Mockito.when(thesaurusTermService.updateThesaurusTerm(Mockito.any(ThesaurusTerm.class), Mockito.any(IUser.class))).thenReturn(fakeTerm1);
		
		ThesaurusConceptView actualResponse = thesaurusConceptRestService.updateConcept(fakeConceptView);
		Assert.assertEquals(fakeConceptView.getTerms().get(0).getIdentifier(), actualResponse.getTerms().get(0).getIdentifier());
		
	}*/
	
	private ThesaurusTerm getFakeThesaurusTermWithNonMandatoryEmptyFields(String id) {
		ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
		fakeThesaurusTerm.setIdentifier(id);
		fakeThesaurusTerm.setThesaurus(new Thesaurus());
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
