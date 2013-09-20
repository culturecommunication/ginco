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
package fr.mcc.ginco.tests.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IThesaurusArrayConceptDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.helpers.ThesaurusArrayHelper;

public class ThesaurusArrayHelperTest {
	@Mock(name = "thesaurusArrayConceptDAO")
	private IThesaurusArrayConceptDAO thesaurusArrayConceptDAO;

	@Mock(name = "thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@InjectMocks
	private ThesaurusArrayHelper thesaurusArrayHelper;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSaveArrayConcepts() {
		ThesaurusArrayConcept concept1 = new ThesaurusArrayConcept();
		ThesaurusArrayConcept.Id id1 = new ThesaurusArrayConcept.Id();
		id1.setConceptId("conceptId1");
		concept1.setIdentifier(id1);

		ThesaurusArrayConcept concept2 = new ThesaurusArrayConcept();
		ThesaurusArrayConcept.Id id2 = new ThesaurusArrayConcept.Id();
		id2.setConceptId("conceptId2");
		concept2.setIdentifier(id2);

		ThesaurusArrayConcept concept3 = new ThesaurusArrayConcept();
		ThesaurusArrayConcept.Id id3 = new ThesaurusArrayConcept.Id();
		id3.setConceptId("conceptId3");
		concept3.setIdentifier(id3);

		ThesaurusArrayConcept concept4 = new ThesaurusArrayConcept();
		ThesaurusArrayConcept.Id id4 = new ThesaurusArrayConcept.Id();
		id4.setConceptId("conceptId4");
		concept4.setIdentifier(id4);

		Set<ThesaurusArrayConcept> existingConcepts = new HashSet<ThesaurusArrayConcept>();
		existingConcepts.add(concept1);
		existingConcepts.add(concept2);

		ThesaurusArray array = new ThesaurusArray();
		array.setConcepts(existingConcepts);

		List<ThesaurusArrayConcept> newConcepts = new ArrayList<ThesaurusArrayConcept>();
		newConcepts.add(concept2);
		newConcepts.add(concept3);
		newConcepts.add(concept4);

		List<String> emptyAssociatedConcepts = new ArrayList<String>();
		Mockito.when(
				thesaurusArrayConceptDAO.getAssociatedConcepts(Mockito
						.anyString())).thenReturn(emptyAssociatedConcepts);

		ThesaurusArray arrayResult = thesaurusArrayHelper.saveArrayConcepts(
				array, newConcepts);
		List<ThesaurusArrayConcept> resultingConcepts = new ArrayList<ThesaurusArrayConcept>();
		resultingConcepts.addAll(arrayResult.getConcepts());

		Assert.assertEquals(3, resultingConcepts.size());
		ListAssert.assertContains(resultingConcepts, concept2);
		ListAssert.assertContains(resultingConcepts, concept3);
		ListAssert.assertContains(resultingConcepts, concept4);

	}

	@Test
	public void testGetArrayConcepts() {

		String concept1S = "concept1";
		String concept2S = "concept2";
		List<String> associatedConcepts = new ArrayList<String>();
		associatedConcepts.add(concept1S);
		associatedConcepts.add(concept2S);

		ThesaurusConcept concept1 = new ThesaurusConcept();
		ThesaurusConcept concept2 = new ThesaurusConcept();

		Mockito.when(
				thesaurusArrayConceptDAO.getAssociatedConcepts(Mockito
						.anyString())).thenReturn(associatedConcepts);
		Mockito.when(thesaurusConceptDAO.getById("concept1")).thenReturn(
				concept1);
		Mockito.when(thesaurusConceptDAO.getById("concept2")).thenReturn(
				concept2);

		List<ThesaurusConcept> concepts = thesaurusArrayHelper
				.getArrayConcepts("arrayId");
		Assert.assertEquals(2, concepts.size());
		ListAssert.assertContains(concepts, concept1);
		ListAssert.assertContains(concepts, concept2);

	}
}
