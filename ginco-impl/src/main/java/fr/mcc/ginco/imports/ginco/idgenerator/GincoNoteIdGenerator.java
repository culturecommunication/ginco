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
package fr.mcc.ginco.imports.ginco.idgenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.exports.result.bean.JaxbList;

/**
 * This class generate new ids for notes (both concept or term notes)
 * for importing branch in existing thesaurus
 * 
 */
@Component("gincoNoteIdGenerator")
public class GincoNoteIdGenerator {	
	
	@Inject
	@Named("gincoIdMapParser")
	private GincoIdMapParser gincoIdMapParser;

	/**
	 * This method updates ids of the concept or term notes
	 *  
	 * @param notes : map of notes
	 * @param idMapping : the map where we store the mapping between old and new ids
	 * @return Map<String, JaxbList<Note>> notes : updated concept notes with new ids
	 */
	public Map<String, JaxbList<Note>> checkIdsForNotes(
			Map<String, JaxbList<Note>> notes, Map<String, String> idMapping) {
		Map<String, JaxbList<Note>> updatedNotes = new HashMap<String, JaxbList<Note>>();
		Iterator<Map.Entry<String, JaxbList<Note>>> iterator = notes.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<String, JaxbList<Note>> entry = iterator.next();
			//New id for the key
			String newId = gincoIdMapParser.getNewId(entry.getKey(), idMapping);
			JaxbList<Note> note = notes.get(entry.getKey());
			
			updatedNotes.put(newId, note);
		}
		notes.clear();
		notes.putAll(updatedNotes);
		return notes;
	}
}