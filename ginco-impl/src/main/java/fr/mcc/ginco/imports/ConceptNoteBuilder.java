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
package fr.mcc.ginco.imports;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.NoteType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.INoteTypeService;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * 	Builder in charge of buidlding concept notes
 *  
 */
@Service("skosConceptNoteBuilder")
public class ConceptNoteBuilder extends AbstractBuilder {

	private static Logger logger = LoggerFactory.getLogger(ConceptNoteBuilder.class);

	@Inject
	@Named("noteTypeService")
	private INoteTypeService noteTypeService;

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;

	@Inject
	private ILanguageDAO languagesDAO;

	@Value("${ginco.default.language}")
	private String defaultLang;

	public ConceptNoteBuilder() {
		super();
	}
	
	private Language getNoteLanguage (String skosLang)
	{
		Language lang = null;
		if (StringUtils.isEmpty(skosLang)) {
			lang = languagesDAO
					.getById(defaultLang);
			return lang;
		} else {
			lang = languagesDAO.getByPart1(skosLang);
			if (lang == null){
				lang = languagesDAO.getById(skosLang);
			}
			
			if (lang != null) {
				return lang;
			} else {
				throw new BusinessException("Note "
						+ skosLang
						+ " is missing it's language",
						"import-note-with-no-lang");
			}
		}
	}

	/**
	 * Returns the list of notes for the given concept
	 * @param skosConcept
	 * @param concept
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	public List<Note> buildConceptNotes(Resource skosConcept,
			ThesaurusConcept concept,ThesaurusTerm preferredTerm, Thesaurus thesaurus)
			throws BusinessException {
		logger.debug("Building notes for concept " +skosConcept.getURI());
		List<Note> allConceptNotes = new ArrayList<Note>();
		List<NoteType> conceptNoteTypes = noteTypeService
				.getConceptNoteTypeList();
		for (NoteType noteType : conceptNoteTypes) {
			String skosNoteType = noteType.getCode();
			if (SKOS.SKOS_NOTES.keySet().contains(skosNoteType)) {
				StmtIterator stmtNotesItr = skosConcept
						.listProperties(SKOS.SKOS_NOTES.get(skosNoteType));
				while (stmtNotesItr.hasNext()) {
					Statement stmt = stmtNotesItr.next();

					Note newNote = new Note();
					newNote.setCreated(thesaurus.getCreated());
					newNote.setIdentifier(generatorService.generate(Note.class));
					newNote.setLexicalValue(stmt.getString());

					RDFNode prefLabel = stmt.getObject();
					newNote.setLanguage(getNoteLanguage(prefLabel.asLiteral().getLanguage()));

					newNote.setModified(thesaurus.getDate());
					newNote.setNoteType(noteType);
					newNote.setConcept(concept);
					allConceptNotes.add(newNote);
				}
			}
		} 
		// Add definition notes to the prefered term...
		StmtIterator stmtNotesItr = skosConcept
				.listProperties(SKOS.SKOS_NOTES.get("definition"));
		while (stmtNotesItr.hasNext()) {
			Statement stmt = stmtNotesItr.next();
			Note newNote = new Note();
			newNote.setCreated(thesaurus.getCreated());
			newNote.setIdentifier(generatorService.generate(Note.class));
			newNote.setLexicalValue(stmt.getString());
			newNote.setModified(thesaurus.getDate());
			RDFNode prefLabel = stmt.getObject();
			newNote.setLanguage(getNoteLanguage(prefLabel.asLiteral().getLanguage()));
			newNote.setNoteType(noteTypeService.getNoteTypeById("definition"));
			newNote.setTerm(preferredTerm);
			allConceptNotes.add(newNote);
		}

		return allConceptNotes;

	}

}
