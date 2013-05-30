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
package fr.mcc.ginco.tests.audit.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.audit.commands.CommandLine;
import fr.mcc.ginco.audit.commands.HierarchyCommandBuilder;
import fr.mcc.ginco.audit.commands.MistralStructuresBuilder;
import fr.mcc.ginco.beans.ThesaurusConcept;

public class HierarchyCommandBuilderTest {

	@Mock(name = "mistralStructuresBuilder")
	private MistralStructuresBuilder mistralStructuresBuilder;

	@InjectMocks
	private HierarchyCommandBuilder hierarchyCommandBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testHierarchyRemoved() {
		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> oldHierarchy = new HashMap<String, List<String>>();
		List<String> oldChildren = new ArrayList<String>();
		oldChildren.add("abbaye");
		oldChildren.add("temple");
		oldChildren.add("eglise");
		oldHierarchy.put("édifices religieux", oldChildren);
		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				oldHierarchy);

		Map<String, List<String>> newHierarchy = new HashMap<String, List<String>>();
		List<String> newChildren = new ArrayList<String>();
		newChildren.add("abbaye");
		newChildren.add("temple");
		newHierarchy.put("édifices religieux", newChildren);
		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				newHierarchy);

		List<CommandLine> actualLines = hierarchyCommandBuilder
				.buildHierarchyChanges(previousConcepts, currentConcepts,
						oldRevision, currentRevision, lang);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "H, eglise < édifices religieux");
	}

	@Test
	public void testHierarchyAdded() {
		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> oldHierarchy = new HashMap<String, List<String>>();
		List<String> oldChildren = new ArrayList<String>();
		oldChildren.add("abbaye");
		oldChildren.add("temple");
		oldHierarchy.put("édifices religieux", oldChildren);
		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				oldHierarchy);

		Map<String, List<String>> newHierarchy = new HashMap<String, List<String>>();
		List<String> newChildren = new ArrayList<String>();
		newChildren.add("abbaye");
		newChildren.add("temple");
		newChildren.add("eglise");
		newHierarchy.put("édifices religieux", newChildren);
		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				newHierarchy);

		List<CommandLine> actualLines = hierarchyCommandBuilder
				.buildHierarchyChanges(previousConcepts, currentConcepts,
						oldRevision, currentRevision, lang);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "eglise < édifices religieux");
	}

	@Test
	public void testHierarchyAndConceptRemoved() {
		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> oldHierarchy = new HashMap<String, List<String>>();
		List<String> oldChildren = new ArrayList<String>();
		oldChildren.add("abbaye");
		oldChildren.add("temple");
		oldChildren.add("eglise");
		oldHierarchy.put("édifices religieux", oldChildren);
		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				oldHierarchy);

		Map<String, List<String>> newHierarchy = new HashMap<String, List<String>>();

		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				newHierarchy);

		List<CommandLine> actualLines = hierarchyCommandBuilder
				.buildHierarchyChanges(previousConcepts, currentConcepts,
						oldRevision, currentRevision, lang);
		Assert.assertEquals(3, actualLines.size());
		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "H, eglise < édifices religieux");
		ListAssert.assertContains(lineValues, "H, abbaye < édifices religieux");
		ListAssert.assertContains(lineValues, "H, temple < édifices religieux");

	}
	@Test
	public void testHierarchyAndConceptAdded() {
		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> oldHierarchy = new HashMap<String, List<String>>();
		
		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				oldHierarchy);

		Map<String, List<String>> newHierarchy = new HashMap<String, List<String>>();
		List<String> newChildren = new ArrayList<String>();

		newChildren.add("abbaye");
		newChildren.add("temple");
		newChildren.add("eglise");
		newHierarchy.put("édifices religieux", newChildren);
		Mockito.when(
				mistralStructuresBuilder.buildHierarchyStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				newHierarchy);

		List<CommandLine> actualLines = hierarchyCommandBuilder
				.buildHierarchyChanges(previousConcepts, currentConcepts,
						oldRevision, currentRevision, lang);
		Assert.assertEquals(3, actualLines.size());
		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "eglise < édifices religieux");
		ListAssert.assertContains(lineValues, "abbaye < édifices religieux");
		ListAssert.assertContains(lineValues, "temple < édifices religieux");

	}
}
