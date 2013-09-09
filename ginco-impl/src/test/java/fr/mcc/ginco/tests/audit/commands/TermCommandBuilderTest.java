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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.audit.commands.CommandLine;
import fr.mcc.ginco.audit.commands.MistralStructuresBuilder;
import fr.mcc.ginco.audit.commands.TermCommandBuilder;
import fr.mcc.ginco.beans.ThesaurusTerm;

public class TermCommandBuilderTest {

	@Mock(name = "mistralStructuresBuilder")
	private MistralStructuresBuilder mistralStructuresBuilder;

	@InjectMocks
	private TermCommandBuilder termCommandBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuildTermsLinesDeletion() {
		List<ThesaurusTerm> currentTerms = new ArrayList<ThesaurusTerm>();

		Map<String, ThesaurusTerm> newLexicalValues = new HashMap<String, ThesaurusTerm>();
		Mockito.when(mistralStructuresBuilder.getTermVersionsView(currentTerms))
				.thenReturn(newLexicalValues);

		List<ThesaurusTerm> previousTerms = new ArrayList<ThesaurusTerm>();
		Map<String, ThesaurusTerm> oldLexicalValues = new HashMap<String, ThesaurusTerm>();
		ThesaurusTerm termToDelete = new ThesaurusTerm();
		termToDelete.setIdentifier("termToDelete");
		termToDelete.setLexicalValue("termToDelete");
		oldLexicalValues.put("termToDelete", termToDelete);
		previousTerms.add(termToDelete);

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);

		List<CommandLine> allLines = termCommandBuilder.buildDeletedTermsLines(
				previousTerms, currentTerms);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "R, termToDelete");
	}

	@Test
	public void testBuildTermsLinesPreferred() {
		List<ThesaurusTerm> currentTerms = new ArrayList<ThesaurusTerm>();

		Map<String, ThesaurusTerm> newLexicalValues = new HashMap<String, ThesaurusTerm>();

		ThesaurusTerm prefTerm = new ThesaurusTerm();
		prefTerm.setIdentifier("pref");
		prefTerm.setLexicalValue("pref");
		prefTerm.setPrefered(true);

		ThesaurusTerm notPrefTerm = new ThesaurusTerm();
		notPrefTerm.setIdentifier("notPref");
		notPrefTerm.setLexicalValue("notPref");
		notPrefTerm.setPrefered(false);

		List<ThesaurusTerm> notPreferredTermsList = new ArrayList<ThesaurusTerm>();
		notPreferredTermsList.add(notPrefTerm);
		Map<String, List<ThesaurusTerm>> notPreferredTerms = new HashMap<String, List<ThesaurusTerm>>();
		notPreferredTerms.put("pref", notPreferredTermsList);

		newLexicalValues.put("pref", prefTerm);
		newLexicalValues.put("notPref", notPrefTerm);

		currentTerms.add(prefTerm);
		currentTerms.add(notPrefTerm);

		Mockito.when(mistralStructuresBuilder.getTermVersionsView(currentTerms))
				.thenReturn(newLexicalValues);

		List<ThesaurusTerm> previousTerms = new ArrayList<ThesaurusTerm>();
		Map<String, ThesaurusTerm> oldLexicalValues = new HashMap<String, ThesaurusTerm>();
		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("pref");
		term.setLexicalValue("pref");
		term.setPrefered(false);
		oldLexicalValues.put("pref", term);
		previousTerms.add(term);

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);
		Mockito.when(
				mistralStructuresBuilder.getNotPreferredTermsByTerm(currentTerms))
				.thenReturn(notPreferredTerms);

		List<CommandLine> allLines = termCommandBuilder.buildChangedTermsLines(
				previousTerms, currentTerms);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "**pref");

	}

	@Test
	public void testBuildTermsLinesUnPreferred() {
		List<ThesaurusTerm> currentTerms = new ArrayList<ThesaurusTerm>();

		Map<String, ThesaurusTerm> newLexicalValues = new HashMap<String, ThesaurusTerm>();
		ThesaurusTerm prefTerm = new ThesaurusTerm();
		prefTerm.setIdentifier("termval");
		prefTerm.setLexicalValue("termval");
		prefTerm.setPrefered(false);
		newLexicalValues.put("termval", prefTerm);
		currentTerms.add(prefTerm);

		Mockito.when(mistralStructuresBuilder.getTermVersionsView(currentTerms))
				.thenReturn(newLexicalValues);

		List<ThesaurusTerm> previousTerms = new ArrayList<ThesaurusTerm>();
		Map<String, ThesaurusTerm> oldLexicalValues = new HashMap<String, ThesaurusTerm>();
		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("termval");
		term.setLexicalValue("termval");
		term.setPrefered(true);
		oldLexicalValues.put("termval", term);
		previousTerms.add(term);

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);

		List<CommandLine> allLines = termCommandBuilder.buildChangedTermsLines(
				previousTerms, currentTerms);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "P, termval");

	}

	/*
	 * Test preferred terms without not preferred synonymes
	 */
	@Test
	public void testBuildPrefTermsLinesAdded() {
		List<ThesaurusTerm> currentTerms = new ArrayList<ThesaurusTerm>();

		Map<String, ThesaurusTerm> newLexicalValues = new HashMap<String, ThesaurusTerm>();

		ThesaurusTerm termToAdd = new ThesaurusTerm();
		termToAdd.setIdentifier("termToAdd");
		termToAdd.setLexicalValue("termToAdd");
		termToAdd.setPrefered(true);
		newLexicalValues.put("termToAdd", termToAdd);
		currentTerms.add(termToAdd);

		Mockito.when(mistralStructuresBuilder.getTermVersionsView(currentTerms))
				.thenReturn(newLexicalValues);

		List<ThesaurusTerm> previousTerms = new ArrayList<ThesaurusTerm>();
		Map<String, ThesaurusTerm> oldLexicalValues = new HashMap<String, ThesaurusTerm>();

		List<ThesaurusTerm> notPreferredTermsList = new ArrayList<ThesaurusTerm>();
		Map<String, List<ThesaurusTerm>> notPreferredTerms = new HashMap<String, List<ThesaurusTerm>>();
		notPreferredTerms.put("termToAdd", notPreferredTermsList);

		Mockito.when(
				mistralStructuresBuilder.getNotPreferredTermsByTerm(currentTerms))
				.thenReturn(notPreferredTerms);

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);

		boolean showPreffered = true;
		List<CommandLine> allLines = termCommandBuilder.buildAddedTermsLines(
				previousTerms, currentTerms, showPreffered);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}

		ListAssert.assertContains(lineValues, "termToAdd");
	}

	/*
	 * Test preferred terms with preferred synonymes
	 */
	@Test
	public void testBuildTermsLinesAdded() {
		List<ThesaurusTerm> currentTerms = new ArrayList<ThesaurusTerm>();

		Map<String, ThesaurusTerm> newLexicalValues = new HashMap<String, ThesaurusTerm>();

		ThesaurusTerm prefTermToAdd = new ThesaurusTerm();
		prefTermToAdd.setIdentifier("prefTermToAdd");
		prefTermToAdd.setLexicalValue("prefTermToAdd");
		prefTermToAdd.setPrefered(true);

		ThesaurusTerm notPrefTermToAdd = new ThesaurusTerm();
		notPrefTermToAdd.setIdentifier("notPrefTermToAdd");
		notPrefTermToAdd.setLexicalValue("notPrefTermToAdd");
		notPrefTermToAdd.setPrefered(false);

		newLexicalValues.put("prefTermToAdd", prefTermToAdd);
		newLexicalValues.put("notPrefTermToAdd", notPrefTermToAdd);

		currentTerms.add(prefTermToAdd);
		currentTerms.add(notPrefTermToAdd);

		Mockito.when(mistralStructuresBuilder.getTermVersionsView(currentTerms))
				.thenReturn(newLexicalValues);

		List<ThesaurusTerm> previousTerms = new ArrayList<ThesaurusTerm>();
		Map<String, ThesaurusTerm> oldLexicalValues = new HashMap<String, ThesaurusTerm>();

		List<ThesaurusTerm> notPreferredTermsList = new ArrayList<ThesaurusTerm>();
		notPreferredTermsList.add(notPrefTermToAdd);

		Map<String, List<ThesaurusTerm>> notPreferredTerms = new HashMap<String, List<ThesaurusTerm>>();
		notPreferredTerms.put("prefTermToAdd", notPreferredTermsList);

		Mockito.when(
				mistralStructuresBuilder.getNotPreferredTermsByTerm(currentTerms))
				.thenReturn(notPreferredTerms);

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);

		boolean showPreffered = true;
		List<CommandLine> allLines = termCommandBuilder.buildAddedTermsLines(
				previousTerms, currentTerms, showPreffered);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "**prefTermToAdd");
	}

}
