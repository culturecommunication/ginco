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
package fr.mcc.ginco.audit.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.ThesaurusConcept;

/**
 * Component in charge of building CommandLine relatives to synonyms changes
 * 
 */
@Service("synonymsCommandBuilder")
public class SynonymsCommandBuilder {
	
	@Inject
	@Named("mistralStructuresBuilder")
	private MistralStructuresBuilder mistralStructuresBuilder;


	/**
	 * Builds the list of command lines for synonyms changes between two
	 * revisions
	 * @param previousConcepts
	 * @param currentConcepts
	 * @param oldRevision
	 * @param currentRevision
	 * @param lang
	 * @return
	 */
	public List<CommandLine> buildSynonyms(
			List<ThesaurusConcept> previousConcepts,
			List<ThesaurusConcept> currentConcepts, Number oldRevision,
			Number currentRevision, String lang) {
		List<CommandLine> termsOperations = new ArrayList<CommandLine>();

		Map<String, List<String>> previousSynonyms = mistralStructuresBuilder.buildSynonymsStructure(
				previousConcepts, oldRevision, lang);
		Map<String, List<String>> currentSynonyms = mistralStructuresBuilder.buildSynonymsStructure(
				currentConcepts, currentRevision, lang);

		// Check for existence/non existence of previous synonyms
		for (String previousSynonym : previousSynonyms.keySet()) {
			if (currentSynonyms.containsKey(previousSynonym)) {
				if (!ListUtils.isEqualList(
						previousSynonyms.get(previousSynonym),
						currentSynonyms.get(previousSynonym))) {
					// Change of synonyms list
					for (String termValue : previousSynonyms
							.get(previousSynonym)) {
						if (!currentSynonyms.get(previousSynonym).contains(
								termValue)) {
							// Synonym has been removed from list
							CommandLine synonymRemovedLine = new CommandLine();
							synonymRemovedLine.setValue(CommandLine.SEPARATE
									+ termValue);
							termsOperations.add(synonymRemovedLine);
						}
					}
					for (String termValue : currentSynonyms
							.get(previousSynonym)) {
						if (!previousSynonyms.get(previousSynonym).contains(
								termValue)) {
							// Synonym has been added to list
							CommandLine synonymAddedLine = new CommandLine();
							synonymAddedLine.setValue(previousSynonym
									+ CommandLine.SYNONYM + termValue);
							termsOperations.add(synonymAddedLine);
						}
					}
				}
			} else {
				// Synonyms suppression
				for (String prevSynonymValue : previousSynonyms
						.get(previousSynonym)) {
					CommandLine synonymRemovedLine = new CommandLine();
					synonymRemovedLine.setValue(CommandLine.SEPARATE
							+ prevSynonymValue);
					termsOperations.add(synonymRemovedLine);
				}
			}
		}

		// Check for addition of synonyms relations
		for (String currentSynonym : currentSynonyms.keySet()) {
			if (!previousSynonyms.containsKey(currentSynonym)
					&& currentSynonyms.get(currentSynonym).size() > 0) {
				CommandLine synonymsAddedLine = new CommandLine();
				String lineValue = currentSynonym;
				for (String synonym : currentSynonyms.get(currentSynonym)) {
					lineValue += CommandLine.SYNONYM + synonym;
				}
				synonymsAddedLine.setValue(lineValue);
				termsOperations.add(synonymsAddedLine);
			}
		}

		return termsOperations;
	}

}
