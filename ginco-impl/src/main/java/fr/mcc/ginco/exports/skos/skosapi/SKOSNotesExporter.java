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
package fr.mcc.ginco.exports.skos.skosapi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.semanticweb.skos.AddAssertion;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataRelationAssertion;
import org.semanticweb.skos.SKOSDataset;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.INoteService;

/**
 * This component is in charge of exporting concept notes to SKOS
 *
 */
@Component("skosNotesExporter")
public class SKOSNotesExporter {
	
	@Inject
	@Named("noteService")
	private INoteService noteService;
	
	
	/**
	 * Export concept notes to SKOS using the skos API
	 * @param thesaurus
	 * @return
	 */
	public List<SKOSChange> exportNotes(String conceptId,List<ThesaurusTerm> prefTerms, SKOSDataFactory factory, SKOSConcept conceptSKOS, SKOSDataset vocab) {
		List<SKOSChange> addList = new ArrayList<SKOSChange>();
		List<Note> notes = noteService.getConceptNotePaginatedList(
				conceptId, 0, 0);
		for (ThesaurusTerm prefTerm : prefTerms) {
			List<Note> termNotes = noteService.getTermNotePaginatedList(prefTerm.getIdentifier(), 0, 0);
			notes.addAll(termNotes);
		}
		
		for (Note note : notes) {
			if ("historyNote".equals(note.getNoteType().getCode())) {
				SKOSDataRelationAssertion noteAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSHistoryNoteDataProperty()
										.getURI()), note.getLexicalValue(),
								note.getLanguage().getPart1());
				addList.add(new AddAssertion(vocab, noteAssertion));
			} else if ("scopeNote".equals(note.getNoteType().getCode())) {
				SKOSDataRelationAssertion noteAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSScopeNoteDataProperty()
										.getURI()), note.getLexicalValue(),
								note.getLanguage().getPart1());
				addList.add(new AddAssertion(vocab, noteAssertion));
			} else if ("example".equals(note.getNoteType().getCode())) {
				SKOSDataRelationAssertion noteAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSExampleDataProperty()
										.getURI()), note.getLexicalValue(),
								note.getLanguage().getPart1());
				addList.add(new AddAssertion(vocab, noteAssertion));
			} else if ("definition".equals(note.getNoteType().getCode())) {
				SKOSDataRelationAssertion noteAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSDefinitionDataProperty()
										.getURI()), note.getLexicalValue(),
								note.getLanguage().getPart1());
				addList.add(new AddAssertion(vocab, noteAssertion));
			}
		}
		return addList;
	}	
}
