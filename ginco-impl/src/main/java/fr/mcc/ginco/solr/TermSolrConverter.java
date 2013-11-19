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

package fr.mcc.ginco.solr;

import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.INoteService;

@Service("termSolrConverter")
public class TermSolrConverter {

	@Inject
	@Named("noteService")
	private INoteService noteService;

	/**
	 * Convert a Thesaurus Term into a SolrDocument
	 *
	 * @param thesaurusTerm
	 * @return SolrInputDocument
	 */

	public SolrInputDocument convertSolrTerm(ThesaurusTerm thesaurusTerm) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField(SolrField.THESAURUSID, thesaurusTerm.getThesaurusId());
		doc.addField(SolrField.THESAURUSTITLE, thesaurusTerm.getThesaurus()
				.getTitle());
		doc.addField(SolrField.IDENTIFIER, thesaurusTerm.getIdentifier());
		doc.addField(SolrField.LEXICALVALUE, thesaurusTerm.getLexicalValue());
		doc.addField(SolrField.TYPE, ThesaurusTerm.class.getSimpleName());
		doc.addField(SolrField.LANGUAGE, thesaurusTerm.getLanguage().getId());

		boolean preferred;

		if (thesaurusTerm.getPrefered() == null) {
			preferred = false;
		} else {
			preferred = thesaurusTerm.getPrefered();
		}

		doc.addField(SolrField.EXT_TYPE, (preferred) ? EntityType.TERM_PREF
				: EntityType.TERM_NON_PREF);

		Timestamp modifiedDate = new Timestamp(thesaurusTerm.getModified()
				.getTime());
		doc.addField(SolrField.MODIFIED, modifiedDate);

		Timestamp createdDate = new Timestamp(thesaurusTerm.getCreated()
				.getTime());
		doc.addField(SolrField.CREATED, createdDate);

		doc.addField(SolrField.STATUS, thesaurusTerm.getStatus());

		List<Note> notes = noteService.getTermNotePaginatedList(
				thesaurusTerm.getIdentifier(), 0, 0);
		for (Note note : notes) {
			doc.addField(SolrField.NOTES, note.getLexicalValue());
		}
		return doc;
	}
}
