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

import fr.mcc.ginco.beans.*;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.AssociativeRelationshipView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.extjs.view.utils.AssociativeRelationshipViewConverter;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptViewConverter;
import fr.mcc.ginco.rest.services.ThesaurusConceptRestService;
import fr.mcc.ginco.services.IIndexerService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.tests.LoggerTestUtil;
import fr.mcc.ginco.utils.DateUtil;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class ThesaurusConceptRestServiceTest {
	
	
	@Mock(name="thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Mock(name="thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;
	
	@Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@Mock(name="termViewConverter")
    private TermViewConverter termViewConverter;

    @Mock(name="indexerService")
    private IIndexerService indexerService;

	@Mock(name="thesaurusConceptViewConverter")
    private ThesaurusConceptViewConverter thesaurusConceptViewConverter;

    @Mock(name="associativeRelationshipViewConverter")
    private AssociativeRelationshipViewConverter associativeRelationshipViewConverter;
	
	@InjectMocks
	private ThesaurusConceptRestService thesaurusConceptRestService = new ThesaurusConceptRestService();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(thesaurusConceptRestService);
	}

	
	/**
	 * Test to put a concept with two terms (which one is prefered)
	 * @throws BusinessException 
	 */
	@Test
	public final void putNewTopConceptWithTwoTermsAndOnlyOneWhichIsPrefered() throws BusinessException {
		ThesaurusTerm fakeTerm1 = getFakeThesaurusTermWithNonMandatoryEmptyFields("fakeTerm1");
		ThesaurusTerm fakeTerm2 = getFakeThesaurusTermWithNonMandatoryEmptyFields("fakeTerm2");
		fakeTerm1.setPrefered(true);
		fakeTerm2.setPrefered(false);
		fakeTerm1.setStatus(TermStatusEnum.VALIDATED.getStatus());
		fakeTerm2.setStatus(TermStatusEnum.VALIDATED.getStatus());
		ThesaurusTermView fakeTermView1 = new ThesaurusTermView(fakeTerm1);
		ThesaurusTermView fakeTermView2 = new ThesaurusTermView(fakeTerm2);
		
		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
		terms.add(fakeTerm1);
		terms.add(fakeTerm2);
		
		List<ThesaurusTerm> preferedTerms = new ArrayList<ThesaurusTerm>();
		preferedTerms.add(fakeTerm1);
		
		List<ThesaurusTermView> termViews = new ArrayList<ThesaurusTermView>();
		termViews.add(fakeTermView1);
		termViews.add(fakeTermView2);

        List<AssociativeRelationshipView> associatedConceptsView = new ArrayList<AssociativeRelationshipView>();

		ThesaurusConcept fakeThesaurusConcept = getFakeThesaurusConceptWithNonMandatoryEmptyFields("fakeConcept1");
		ThesaurusConceptView fakeConceptView = new ThesaurusConceptView();
		fakeConceptView.setIdentifier("");
		fakeConceptView.setCreated("");
		fakeConceptView.setModified("");
		fakeConceptView.setTopconcept(false);
		fakeConceptView.setThesaurusId("1");
		fakeConceptView.setTerms(termViews);
        fakeConceptView.setAssociatedConcepts(associatedConceptsView);


        List<AssociativeRelationship> associatedConcepts = new ArrayList<AssociativeRelationship>();
        List<ConceptHierarchicalRelationship> hierarchicalRelationships = new ArrayList<ConceptHierarchicalRelationship>();
        List<ThesaurusConcept> childToRemove = new ArrayList<ThesaurusConcept>();
        List<Alignment> alignments = new ArrayList<Alignment>();

		Mockito.when(thesaurusConceptViewConverter.convert(fakeConceptView)).thenReturn(fakeThesaurusConcept);
		Mockito.when(termViewConverter.convertTermViewsInTerms(termViews, true)).thenReturn(terms);

		Mockito.when(thesaurusConceptService.updateThesaurusConcept(fakeThesaurusConcept, terms, associatedConcepts, hierarchicalRelationships, childToRemove, alignments)).thenReturn(fakeThesaurusConcept);
		Mockito.when(thesaurusConceptViewConverter.convert(Mockito.any(ThesaurusConcept.class), Mockito.anyListOf(ThesaurusTerm.class))).thenReturn(fakeConceptView);
		
		ThesaurusConceptView actualResponse = thesaurusConceptRestService.updateConcept(fakeConceptView);
		Assert.assertEquals(fakeConceptView.getTerms().get(0).getIdentifier(), actualResponse.getTerms().get(0).getIdentifier());
		Assert.assertEquals(fakeConceptView.getTerms().get(1).getIdentifier(), actualResponse.getTerms().get(1).getIdentifier());
	}
	
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
		return fakeThesaurusConcept;
	}
}
