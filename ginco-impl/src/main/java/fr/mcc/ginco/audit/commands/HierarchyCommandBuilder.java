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
 * Component in charge of building CommandLine relatives to hierarchy changes
 * 
 */
@Service("hierarchyCommandBuilder")
public class HierarchyCommandBuilder {

	@Inject
	@Named("mistralStructuresBuilder")
	private MistralStructuresBuilder mistralStructuresBuilder;

	/**
	 * Builds the list of command lines for hierarchy changes between two
	 * revisions
	 * 
	 * @param previousConcepts
	 * @param currentConcepts
	 * @param oldRevision
	 * @param currentRevision
	 * @param lang
	 * @return
	 */
	public List<CommandLine> buildHierarchyChanges(
			List<ThesaurusConcept> previousConcepts,
			List<ThesaurusConcept> currentConcepts, Number oldRevision,
			Number currentRevision, String lang) {
		List<CommandLine> termsOperations = new ArrayList<CommandLine>();

		Map<String, List<String>> previousHierarchies = mistralStructuresBuilder
				.buildHierarchyStructure(previousConcepts, oldRevision, lang);
		Map<String, List<String>> currentHierarchies = mistralStructuresBuilder
				.buildHierarchyStructure(currentConcepts, currentRevision, lang);
		for (String previousParentConceptId : previousHierarchies.keySet()) {
			if (currentHierarchies.containsKey(previousParentConceptId)) {
				// Concept still exists
				if (!ListUtils.isEqualList(
						previousHierarchies.get(previousParentConceptId),
						currentHierarchies.get(previousParentConceptId))) {
					// but hierarchy has changed
					for (String oldChild : previousHierarchies
							.get(previousParentConceptId)) {
						if (!currentHierarchies.get(previousParentConceptId)
								.contains(oldChild)) {
							// child removed
							CommandLine childRemovedLine = new CommandLine();
							childRemovedLine
									.setValue(CommandLine.HIERARCHY_REMOVED
											+ oldChild + CommandLine.HIERARCHY
											+ previousParentConceptId);
							termsOperations.add(childRemovedLine);
						}
					}
					for (String newChild : currentHierarchies
							.get(previousParentConceptId)) {
						if (!previousHierarchies.get(previousParentConceptId)
								.contains(newChild)) {
							// child added
							CommandLine childAddedLine = new CommandLine();
							childAddedLine.setValue(newChild
									+ CommandLine.HIERARCHY
									+ previousParentConceptId);
							termsOperations.add(childAddedLine);
						}
					}
				}
			} else {
				// Concept does not exist anymore, remove all old hierarchies
				for (String oldChild : previousHierarchies
						.get(previousParentConceptId)) {
					CommandLine childRemovedLine = new CommandLine();
					childRemovedLine.setValue(CommandLine.HIERARCHY_REMOVED
							+ oldChild + CommandLine.HIERARCHY
							+ previousParentConceptId);
					termsOperations.add(childRemovedLine);
				}
			}
		}

		for (String currentParentConceptId : currentHierarchies.keySet()) {
			if (!previousHierarchies.containsKey(currentParentConceptId)) {
				// Add all new hierarchies
				for (String child : currentHierarchies
						.get(currentParentConceptId)) {
					CommandLine hierarchyAddedLine = new CommandLine();
					hierarchyAddedLine.setValue(child + CommandLine.HIERARCHY
							+ currentParentConceptId);
					termsOperations.add(hierarchyAddedLine);
				}
			}

		}
		return termsOperations;

	}
}
