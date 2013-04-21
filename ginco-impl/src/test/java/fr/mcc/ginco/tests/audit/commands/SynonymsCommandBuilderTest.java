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

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.audit.commands.CommandLine;
import fr.mcc.ginco.audit.commands.MistralStructuresBuilder;
import fr.mcc.ginco.audit.commands.SynonymsCommandBuilder;
import fr.mcc.ginco.beans.ThesaurusConcept;

public class SynonymsCommandBuilderTest {

	@Mock(name = "mistralStructuresBuilder")
	private MistralStructuresBuilder mistralStructuresBuilder;

	@InjectMocks
	private SynonymsCommandBuilder synonymsCommandBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSynonymRemovedInList() {

		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> previousSynonyms = new HashMap<String, List<String>>();
		List<String> previousSyns = new ArrayList<String>();
		previousSyns.add("énumération");
		previousSyns.add("catalogue");
		previousSynonyms.put("liste", previousSyns);

		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				previousSynonyms);

		Map<String, List<String>> currentSynonyms = new HashMap<String, List<String>>();
		List<String> currentSyns = new ArrayList<String>();
		currentSyns.add("catalogue");
		currentSynonyms.put("liste", currentSyns);
		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				currentSynonyms);

		List<CommandLine> actualLines = synonymsCommandBuilder.buildSynonyms(
				previousConcepts, currentConcepts, oldRevision,
				currentRevision, lang);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "S, énumération");
	}

	@Test
	public void testSynonymAddedInList() {

		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> previousSynonyms = new HashMap<String, List<String>>();
		List<String> previousSyns = new ArrayList<String>();
		previousSyns.add("catalogue");
		previousSynonyms.put("liste", previousSyns);

		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				previousSynonyms);

		Map<String, List<String>> currentSynonyms = new HashMap<String, List<String>>();
		List<String> currentSyns = new ArrayList<String>();
		currentSyns.add("énumération");
		currentSyns.add("catalogue");
		currentSynonyms.put("liste", currentSyns);
		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				currentSynonyms);

		List<CommandLine> actualLines = synonymsCommandBuilder.buildSynonyms(
				previousConcepts, currentConcepts, oldRevision,
				currentRevision, lang);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "liste = énumération");
	}

	@Test
	public void testSynonymAdded() {

		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> previousSynonyms = new HashMap<String, List<String>>();
		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				previousSynonyms);

		Map<String, List<String>> currentSynonyms = new HashMap<String, List<String>>();
		List<String> currentSyns = new ArrayList<String>();
		currentSyns.add("catalogue");
		currentSyns.add("énumération");
		currentSynonyms.put("liste", currentSyns);
		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				currentSynonyms);

		List<CommandLine> actualLines = synonymsCommandBuilder.buildSynonyms(
				previousConcepts, currentConcepts, oldRevision,
				currentRevision, lang);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert
				.assertContains(lineValues, "liste = catalogue = énumération");
	}

	@Test
	public void testSynonymRemoved() {

		List<ThesaurusConcept> previousConcepts = new ArrayList<ThesaurusConcept>();
		// just to make a difference between the two paramaters, otherwise
		// Mockito is lost
		ThesaurusConcept concept1 = new ThesaurusConcept();
		previousConcepts.add(concept1);
		List<ThesaurusConcept> currentConcepts = new ArrayList<ThesaurusConcept>();
		Number oldRevision = 0;
		Number currentRevision = 0;
		String lang = "FR-fr";

		Map<String, List<String>> previousSynonyms = new HashMap<String, List<String>>();
		List<String> prevSyns = new ArrayList<String>();
		prevSyns.add("catalogue");
		prevSyns.add("énumération");
		previousSynonyms.put("liste", prevSyns);
		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						previousConcepts, oldRevision, lang)).thenReturn(
				previousSynonyms);

		Map<String, List<String>> currentSynonyms = new HashMap<String, List<String>>();

		Mockito.when(
				mistralStructuresBuilder.buildSynonymsStructure(
						currentConcepts, currentRevision, lang)).thenReturn(
				currentSynonyms);

		List<CommandLine> actualLines = synonymsCommandBuilder.buildSynonyms(
				previousConcepts, currentConcepts, oldRevision,
				currentRevision, lang);
		Assert.assertEquals(2, actualLines.size());

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : actualLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "S, catalogue");
		ListAssert.assertContains(lineValues, "S, énumération");
	}

}
