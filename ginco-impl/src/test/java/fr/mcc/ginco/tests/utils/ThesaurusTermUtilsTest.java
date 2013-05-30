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
package fr.mcc.ginco.tests.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.utils.ThesaurusTermUtils;

public class ThesaurusTermUtilsTest {
	
	@InjectMocks
	ThesaurusTermUtils thesaurusTermUtils;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetPreferedTerms() {
		
		List<ThesaurusTerm> listOfTerms = new ArrayList<ThesaurusTerm>();
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setPrefered(false);
		listOfTerms.add(term1);
		
		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setPrefered(true);
		listOfTerms.add(term2);

		ThesaurusTerm term3 = new ThesaurusTerm();
		term3.setPrefered(false);
		listOfTerms.add(term3);

		ThesaurusTerm term4 = new ThesaurusTerm();
		term4.setPrefered(true);
		listOfTerms.add(term4);
		
		List<ThesaurusTerm> prefered  = thesaurusTermUtils.getPreferedTerms(listOfTerms);
		
		ListAssert.assertContains(prefered, term2);
		ListAssert.assertContains(prefered, term4);

	}
	
	@Test(expected=BusinessException.class)
	public void testCheckTermsNoPreferred() {
		List<ThesaurusTerm> listOfTerms = new ArrayList<ThesaurusTerm>();
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setPrefered(false);	
		listOfTerms.add(term1);
		
		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setPrefered(false);
		listOfTerms.add(term2);
		
		thesaurusTermUtils.checkTerms(listOfTerms);
		
	}
	
	@Test(expected=BusinessException.class)
	public void testCheckTermsSameLexicalValueSameLanguage() {
		List<ThesaurusTerm> listOfTerms = new ArrayList<ThesaurusTerm>();
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setPrefered(true);
		term1.setLexicalValue("lexicalvalue");		
		Language lang = new Language();
		lang.setId("lang1");
		term1.setLanguage(lang);
		
		listOfTerms.add(term1);
		
		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setPrefered(true);
		listOfTerms.add(term2);

		ThesaurusTerm term3 = new ThesaurusTerm();
		term3.setPrefered(false);
		term3.setLexicalValue("lexicalvalue");		
		term3.setLanguage(lang);
		listOfTerms.add(term3);

		ThesaurusTerm term4 = new ThesaurusTerm();
		term4.setPrefered(true);
		listOfTerms.add(term4);
		
		thesaurusTermUtils.checkTerms(listOfTerms);

	}
	
	@Test
	public void testCheckTerms() {
		List<ThesaurusTerm> listOfTerms = new ArrayList<ThesaurusTerm>();
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setPrefered(true);
		term1.setLexicalValue("lexicalvalue");		
		Language lang = new Language();
		lang.setId("lang1");
		term1.setLanguage(lang);
		
		listOfTerms.add(term1);
		
		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setPrefered(false);
		listOfTerms.add(term2);

		ThesaurusTerm term3 = new ThesaurusTerm();
		term3.setPrefered(false);
		term3.setLexicalValue("lexicalvalue2");		
		term3.setLanguage(lang);
		listOfTerms.add(term3);

		ThesaurusTerm term4 = new ThesaurusTerm();
		term4.setPrefered(false);
		listOfTerms.add(term4);
		
		boolean check = thesaurusTermUtils.checkTerms(listOfTerms);
		Assert.assertTrue(check);
	}   
}
