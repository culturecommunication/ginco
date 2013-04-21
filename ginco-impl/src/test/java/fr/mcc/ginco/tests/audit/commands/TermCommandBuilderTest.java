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

		List<CommandLine> allLines = termCommandBuilder.buildTermsLines(
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
		prefTerm.setIdentifier("termval");
		prefTerm.setLexicalValue("termval");
		prefTerm.setPrefered(true);
		newLexicalValues.put("termval", prefTerm);
		currentTerms.add(prefTerm);

		Mockito.when(mistralStructuresBuilder.getTermVersionsView(currentTerms))
				.thenReturn(newLexicalValues);

		List<ThesaurusTerm> previousTerms = new ArrayList<ThesaurusTerm>();
		Map<String, ThesaurusTerm> oldLexicalValues = new HashMap<String, ThesaurusTerm>();
		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("termval");
		term.setLexicalValue("termval");
		term.setPrefered(false);
		oldLexicalValues.put("termval", term);
		previousTerms.add(term);

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);

		List<CommandLine> allLines = termCommandBuilder.buildTermsLines(
				previousTerms, currentTerms);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "**termval");

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

		List<CommandLine> allLines = termCommandBuilder.buildTermsLines(
				previousTerms, currentTerms);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "P, termval");

	}

	@Test
	public void testBuildTermsLinesAdded() {
		List<ThesaurusTerm> currentTerms = new ArrayList<ThesaurusTerm>();

		Map<String, ThesaurusTerm> newLexicalValues = new HashMap<String, ThesaurusTerm>();

		ThesaurusTerm termToAdd = new ThesaurusTerm();
		termToAdd.setIdentifier("termToAdd");
		termToAdd.setLexicalValue("termToAdd");
		newLexicalValues.put("termToAdd", termToAdd);
		currentTerms.add(termToAdd);

		Mockito.when(mistralStructuresBuilder.getTermVersionsView(currentTerms))
				.thenReturn(newLexicalValues);

		List<ThesaurusTerm> previousTerms = new ArrayList<ThesaurusTerm>();
		Map<String, ThesaurusTerm> oldLexicalValues = new HashMap<String, ThesaurusTerm>();

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);

		List<CommandLine> allLines = termCommandBuilder.buildTermsLines(
				previousTerms, currentTerms);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "termToAdd");
	}

	@Test
	public void testBuildTermsLinesAddedPref() {
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

		Mockito.when(
				mistralStructuresBuilder.getTermVersionsView(previousTerms))
				.thenReturn(oldLexicalValues);

		List<CommandLine> allLines = termCommandBuilder.buildTermsLines(
				previousTerms, currentTerms);

		List<String> lineValues = new ArrayList<String>();
		for (CommandLine line : allLines) {
			lineValues.add(line.getValue());
		}
		ListAssert.assertContains(lineValues, "**termToAdd");
	}

}
