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
package fr.mcc.ginco.imports.ginco;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.INoteDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.exports.result.bean.JaxbList;

/**
 * This class gives methods to import terms and terms notes
 *
 */
@Component("gincoTermImporter")
public class GincoTermImporter {
	
	@Inject
	private INoteDAO noteDAO;
	
	@Inject
	private IThesaurusTermDAO thesaurusTermDAO;	
	
	
	/**
	 * This method stores all the terms of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored terms
	 */
	public List<ThesaurusTerm> storeTerms(List<ThesaurusTerm> termsToImport, Thesaurus targetedThesaurus) {
		List<ThesaurusTerm> updatedTerms = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm term : termsToImport) {
			term.setThesaurus(targetedThesaurus);
			updatedTerms.add(thesaurusTermDAO.update(term));
		}
		return updatedTerms;
	}

	/**
	 * This method stores all the term notes of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored notes
	 */
	public List<Note> storeTermNotes(Map<String, JaxbList<Note>> termNotesToImport) {
		List<Note> result = new ArrayList<Note>();
		if (termNotesToImport != null && !termNotesToImport.isEmpty()) {
			Iterator<Map.Entry<String,  JaxbList<Note>>> entries = termNotesToImport.entrySet().iterator();
			String termId = null;
			List<Note> notes = null;
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<Note>> entry = entries.next();
				//Getting the id of the term
				termId = entry.getKey();
				
				//Getting the ids of the notes
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					notes = entry.getValue().getList();
				}
				
				for (Note note : notes) {
					note.setTerm(thesaurusTermDAO.getById(termId));
					result.add(noteDAO.update(note));
				}
			}
		}
		return result;
	}
}
