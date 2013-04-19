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
package fr.mcc.ginco.exports;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.services.IAssociativeRelationshipService;
import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermRoleService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.LabelUtil;
import fr.mcc.ginco.utils.ThesaurusTermUtils;

/**
 * This component gives methods to export concepts alphabetically
 * 
 */
@Component("alphabeticConceptExporter")
public class AlphabeticConceptExporter {

	@Inject
	@Named("associativeRelationshipService")
	private IAssociativeRelationshipService associativeRelationshipService;

	@Inject
	@Named("noteService")
	private INoteService noteService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusTermRoleService")
	private IThesaurusTermRoleService thesaurusTermRoleService;

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Inject
	@Named("thesaurusTermUtils")
	private ThesaurusTermUtils thesaurusTermUtils;

	/**
	 * For alphabetic export.
	 * 
	 * banquette NA: Siège à plusieurs places, peu profond, garni, comportant
	 * éventuellement soit un dossier, soit des accotoirs, soit les deux. EP:
	 * banquette à accotoirs banquette à dossier TG: siège banquette à accotoirs
	 * EM: banquette banquette à dossier EM: banquette
	 */
	public void addConceptInfo(Integer base, List<FormattedLine> result,
			ThesaurusConcept concept) {
		List<Note> notes = noteService.getConceptNotePaginatedList(
				concept.getIdentifier(), 0, 0);

		List<ThesaurusTerm> prefTerms = thesaurusConceptService
				.getConceptPreferredTerms(concept.getIdentifier());

		if (concept.getNotation() != null && !concept.getNotation().isEmpty()) {
			result.add(new FormattedLine(base, LabelUtil.getResourceLabel("CC")
					+ ": " + concept.getNotation()));
		}

		for (Note note : notes) {
			if ("scopeNote".equals(note.getNoteType().getCode())) {
				result.add(new FormattedLine(base, LabelUtil
						.getResourceLabel("NA") + ": " + note.getLexicalValue()));
			}
		}

		for (ThesaurusConcept parent : concept.getParentConcepts()) {

			List<ThesaurusTerm> parentPrefs = thesaurusConceptService
					.getConceptPreferredTerms(parent.getIdentifier());

			result.add(new FormattedLine(base, LabelUtil.getResourceLabel("TG")
					+ ": "
					+ thesaurusTermUtils.generatePrefTermsText(parentPrefs)));
		}

		for (ThesaurusConcept ta : thesaurusConceptService
				.getThesaurusConceptList(associativeRelationshipService
						.getAssociatedConceptsId(concept))) {

			List<ThesaurusTerm> taPrefs = thesaurusConceptService
					.getConceptPreferredTerms(ta.getIdentifier());

			result.add(new FormattedLine(base, LabelUtil.getResourceLabel("TA")
					+ ": " + thesaurusTermUtils.generatePrefTermsText(taPrefs)));
		}

		for (ThesaurusTerm term : thesaurusTermService
				.getTermsByConceptId(concept.getIdentifier())) {
			if (!term.getPrefered()) {
				result.add(new FormattedLine(base, LabelUtil
						.getResourceLabel("EP")
						+ ": "
						+ thesaurusTermUtils.generatePrefTermText(term)));
			}
		}

		for (ThesaurusConcept child : thesaurusConceptService
				.getChildrenByConceptId(concept.getIdentifier())) {

			ThesaurusTerm term = thesaurusConceptService
					.getConceptPreferredTerm(child.getIdentifier());

			result.add(new FormattedLine(base, LabelUtil.getResourceLabel("TS")
					+ ": " + thesaurusTermUtils.generatePrefTermText(term)));
		}

		for (ThesaurusTerm term : thesaurusConceptService
				.getConceptNotPreferredTerms(concept.getIdentifier())) {
			result.add(new FormattedLine(base - 1, thesaurusTermUtils
					.generatePrefTermText(term)));
			if (term.getRole() == null) {
				result.add(new FormattedLine(base, thesaurusTermRoleService
						.getDefaultThesaurusTermRole().getCode()
						+ ": "
						+ thesaurusTermUtils.generatePrefTermsText(prefTerms)));
			} else {
				result.add(new FormattedLine(base, term.getRole().getCode()
						+ ": "
						+ thesaurusTermUtils.generatePrefTermsText(prefTerms)));
			}
		}
	}
}
