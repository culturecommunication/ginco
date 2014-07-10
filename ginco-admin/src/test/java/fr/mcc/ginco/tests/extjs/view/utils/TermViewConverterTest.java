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
package fr.mcc.ginco.tests.extjs.view.utils;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.ThesaurusTermRole;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermRoleService;
import fr.mcc.ginco.services.IThesaurusTermService;

public class TermViewConverterTest {	
	
	@Mock(name = "thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Mock(name = "thesaurusService")
	private IThesaurusService thesaurusService;

	@Mock(name = "languagesService")
	private ILanguagesService languagesService;

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "thesaurusTermRoleService")
	private IThesaurusTermRoleService thesaurusTermRoleService;

	@Mock(name = "generatorService")
	private IIDGeneratorService generatorService;
	
	
	@InjectMocks
	private TermViewConverter converter = new TermViewConverter();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertExistingThesaurusTermViewNoLanguage() {

		ThesaurusConcept fakeConcept = new ThesaurusConcept();
		fakeConcept.setIdentifier("fakeConcept");
		when(thesaurusConceptService
				.getThesaurusConceptById("fakeConceptId")).thenReturn(fakeConcept);
		
		ThesaurusTermView view = buildThesaurusTermView("view1");		
		view.setRole("TA");
		ThesaurusTermRole fakeRole = new ThesaurusTermRole();
		fakeRole.setCode("TA");
		when(thesaurusTermRoleService
		.getTermRole("TA")).thenReturn(fakeRole);
		
		ThesaurusTerm termFromDB = new ThesaurusTerm();		
		when(thesaurusTermService
				.getThesaurusTermById(Mockito.anyString())).thenReturn(termFromDB);
		
		ThesaurusTerm actualterm = converter.convert(view, true);

		Assert.assertEquals("lexical value", actualterm.getLexicalValue());
		Assert.assertEquals(new Integer(0), actualterm.getStatus());
		Assert.assertEquals("source", actualterm.getSource());
		Assert.assertEquals(false, actualterm.getPrefered());
		Assert.assertEquals(true, actualterm.getHidden());
		Assert.assertEquals("fakeConcept", actualterm.getConcept().getIdentifier());
		Assert.assertEquals("TA", actualterm.getRole().getCode());
	}
	
	@Test
	public void testConvertExistingThesaurusTermNoConcept() {		
		ThesaurusTermView view = buildThesaurusTermView("view1");		
		
		ThesaurusTerm termFromDB = new ThesaurusTerm();		
		when(thesaurusTermService
				.getThesaurusTermById(Mockito.anyString())).thenReturn(termFromDB);
		
		ThesaurusTerm actualterm = converter.convert(view, false);


		Assert.assertNull(actualterm.getPrefered());
		Assert.assertNull(actualterm.getHidden());
		Assert.assertNull(actualterm.getConcept());
		Assert.assertNull(actualterm.getRole());
	}

	private ThesaurusTermView buildThesaurusTermView(String id) {
		ThesaurusTermView fakeTermView = new ThesaurusTermView();

		fakeTermView.setIdentifier(id);
		
		
		fakeTermView.setLexicalValue(" lexical value  ");
		fakeTermView.setSource("source");
		fakeTermView.setStatus(0);	
		
		fakeTermView.setPrefered(false);
		fakeTermView.setHidden(true);
		fakeTermView.setConceptId("fakeConceptId");

		return fakeTermView;
	}

}
