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

import fr.mcc.ginco.audit.utils.AuditHelper;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Component in charge of building utility structures for CommandLine builders
 */
@Service("mistralStructuresBuilder")
public class MistralStructuresBuilder {

	@Inject
	@Named("auditHelper")
	private AuditHelper auditHelper;

	/**
	 * Builds a Map of hierarchy where the key is the lexical value of the
	 * preferred term of the parent concept and the value is the list of lexical
	 * values of the children concept preferred terms
	 *
	 * @param conceptsAtRevision
	 * @param revision
	 * @param lang
	 * @return
	 */
	public Map<String, List<String>> buildHierarchyStructure(
			List<ThesaurusConcept> conceptsAtRevision, Number revision,
			String lang) {
		Map<String, List<String>> hierarchies = new HashMap<String, List<String>>();
		List<ThesaurusConcept> allConcepts = new ArrayList<ThesaurusConcept>();
		for (ThesaurusConcept previousConcept : conceptsAtRevision) {
			ThesaurusTerm previousPrefTerm = auditHelper
					.getPreferredTermAtRevision(revision,
							previousConcept.getIdentifier(), lang);

			if (previousPrefTerm != null) {
				List<ThesaurusConcept> children = auditHelper
						.getConceptChildrenAtRevision(revision, previousConcept, allConcepts);

				List<String> childrenLexicalValues = new ArrayList<String>();

				for (ThesaurusConcept child : children) {
					ThesaurusTerm childPrefTerm = auditHelper
							.getPreferredTermAtRevision(revision,
									child.getIdentifier(), lang);
					if (childPrefTerm != null) {
						childrenLexicalValues.add(childPrefTerm.getLexicalValue());
					}
				}
				Collections.sort(childrenLexicalValues);

				hierarchies.put(previousPrefTerm.getLexicalValue(),
						childrenLexicalValues);
			}
		}
		return hierarchies;
	}

	/**
	 * Builds a Map of synonyms where the key is the lexical value of the
	 * preferred term of the concept and the value is the list of lexical values
	 * of the alternatives terms
	 *
	 * @param conceptsAtRevision
	 * @param revision
	 * @param lang
	 * @return
	 */
	public Map<String, List<String>> buildSynonymsStructure(
			List<ThesaurusConcept> conceptsAtRevision, Number revision,
			String lang) {
		Map<String, List<String>> previousSynonyms = new HashMap<String, List<String>>();
		for (ThesaurusConcept previousConcept : conceptsAtRevision) {
			ThesaurusTerm previousPrefTerm = auditHelper
					.getPreferredTermAtRevision(revision,
							previousConcept.getIdentifier(), lang);
			if (previousPrefTerm != null) {
				List<String> synonymsLexicalvalues = new ArrayList<String>();
				List<ThesaurusTerm> previousConceptTerms = auditHelper
						.getConceptTermsAtRevision(previousConcept, revision, lang);
				for (ThesaurusTerm previousConceptTerm : previousConceptTerms) {
					if (!previousConceptTerm.getLexicalValue().equals(
							previousPrefTerm.getLexicalValue())) {
						synonymsLexicalvalues.add(previousConceptTerm
								.getLexicalValue());
					}
					Collections.sort(synonymsLexicalvalues);
				}

				previousSynonyms.put(previousPrefTerm.getLexicalValue(),
						synonymsLexicalvalues);
			}
		}
		return previousSynonyms;
	}

	/**
	 * Builds a Map of terms where the key is the lexical value of the term and
	 * the value is the ThesaurusTerm object values of the alternatives terms
	 *
	 * @param currentTerms
	 * @return
	 */
	public Map<String, ThesaurusTerm> getTermVersionsView(
			List<ThesaurusTerm> currentTerms) {
		Map<String, ThesaurusTerm> newLexicalvalues = new HashMap<String, ThesaurusTerm>();
		for (ThesaurusTerm currentTerm : currentTerms) {
			newLexicalvalues.put(currentTerm.getLexicalValue(), currentTerm);
		}
		return newLexicalvalues;
	}

	public Map<String, List<ThesaurusTerm>> getNotPreferredTermsByTerm(
			List<ThesaurusTerm> currentTerms) {
		Map<String, List<ThesaurusTerm>> nonPreferredTerms = new HashMap<String, List<ThesaurusTerm>>();
		for (ThesaurusTerm currentTerm : currentTerms) {
			if (currentTerm.getPrefered()) {

				List<ThesaurusTerm> notPreferredTerms = new ArrayList<ThesaurusTerm>();
				for (ThesaurusTerm term : currentTerms) {
					if (term.getConcept().getIdentifier().equals(currentTerm.getConcept().getIdentifier())
							&& !term.getPrefered()) {
						notPreferredTerms.add(term);
					}
				}
				nonPreferredTerms.put(currentTerm.getLexicalValue(), notPreferredTerms);
			}
		}
		return nonPreferredTerms;
	}
}
