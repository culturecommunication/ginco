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
package fr.mcc.ginco.exports.ginco;

import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.result.bean.GincoExportedEntity;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.services.INoteService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * This component gives methods to export terms and its related objects (notes
 * for example) to Ginco Custom Export Format
 */
@Component
public class GincoTermExporter {

	@Inject
	private INoteService noteService;

	@Inject
	private GincoAttributesExporter gincoAttributesExporter;

	/**
	 * This method gets all the notes of a term in a JaxbList object
	 *
	 * @param thesaurusTerm
	 * @return JaxbList<Note> termNotes : A JaxbList of notes
	 */
	public JaxbList<Note> getExportTermNotes(ThesaurusTerm thesaurusTerm) {
		List<Note> notes = noteService.getTermNotePaginatedList(thesaurusTerm
						.getIdentifier(), 0,
				noteService.getTermNoteCount(thesaurusTerm.getIdentifier())
						.intValue()
		);
		return new JaxbList<Note>(notes);
	}

	/**
	 * Adds the term and the related custom attributes to the thesaurus to export
	 *
	 * @param thesaurusToExport
	 * @param term
	 * @return
	 */
	public GincoExportedEntity addExportedTerms(
			GincoExportedEntity thesaurusToExport, ThesaurusTerm term) {

		thesaurusToExport.getTerms().add(term);
		JaxbList<CustomTermAttribute> termAttributes = gincoAttributesExporter
				.getExportedTermAttributes(term);
		if (termAttributes != null && !termAttributes.isEmpty()) {
			thesaurusToExport.getTermAttributes().put(term.getIdentifier(),
					termAttributes);
		}
		return thesaurusToExport;
	}

}
