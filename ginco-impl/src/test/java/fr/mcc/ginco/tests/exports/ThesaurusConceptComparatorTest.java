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
package fr.mcc.ginco.tests.exports;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.ThesaurusConceptComparator;
import fr.mcc.ginco.services.IThesaurusTermService;

public class ThesaurusConceptComparatorTest {

	@Mock(name = "thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@InjectMocks
	private ThesaurusConceptComparator thesaurusConceptComparator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(thesaurusConceptComparator, "defaultLang",
				"fr-FR");
	}

	@Test
	public void testCompareStandard() {
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("concept1");

		ThesaurusConcept concept2 = new ThesaurusConcept();
		concept2.setIdentifier("concept2");

		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setLexicalValue("zeste");
		term1.setPrefered(true);
		List<ThesaurusTerm> co1terms = new ArrayList<ThesaurusTerm>();
		co1terms.add(term1);

		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setLexicalValue("apiculture");
		term2.setPrefered(true);
		List<ThesaurusTerm> co2terms = new ArrayList<ThesaurusTerm>();
		co2terms.add(term2);

		Mockito.when(thesaurusTermService.getTermsByConceptId("concept1"))
				.thenReturn(co1terms);
		Mockito.when(thesaurusTermService.getTermsByConceptId("concept2"))
				.thenReturn(co2terms);

		int compareNoAccent = thesaurusConceptComparator.compare(concept1,
				concept2);
		Assert.assertEquals(1, compareNoAccent);

	}

	@Test
	public void testCompareWithAccents() {
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("concept1");

		ThesaurusConcept concept2 = new ThesaurusConcept();
		concept2.setIdentifier("concept2");

		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setLexicalValue("zeste");
		term1.setPrefered(true);
		List<ThesaurusTerm> co1terms = new ArrayList<ThesaurusTerm>();
		co1terms.add(term1);

		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setLexicalValue("éléphant");
		term2.setPrefered(true);
		List<ThesaurusTerm> co2terms = new ArrayList<ThesaurusTerm>();
		co2terms.add(term2);

		Mockito.when(thesaurusTermService.getTermsByConceptId("concept1"))
				.thenReturn(co1terms);
		Mockito.when(thesaurusTermService.getTermsByConceptId("concept2"))
				.thenReturn(co2terms);

		int compareWithAccents = thesaurusConceptComparator.compare(concept1,
				concept2);
		Assert.assertEquals(1, compareWithAccents);
	}

}
