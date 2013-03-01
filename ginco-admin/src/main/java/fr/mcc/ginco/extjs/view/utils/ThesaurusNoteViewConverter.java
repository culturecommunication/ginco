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
package fr.mcc.ginco.extjs.view.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusNoteView;
import fr.mcc.ginco.utils.DateUtil;

@Component("thesaurusNoteViewConverter")
public class ThesaurusNoteViewConverter {
	
	public ThesaurusNoteView convert(Note source) {
		ThesaurusNoteView view = new ThesaurusNoteView();
		if (source != null) {
			view.setIdentifier(source.getIdentifier());
			view.setLexicalValue(source.getLexicalValue());
			view.setLanguage(source.getLanguage().getId());
			view.setType(source.getNoteTypeCode());
			if (source.getSource() != null) {
				view.setSource(source.getSource());
			}
			
			if(source.getCreated() != null) {
            	view.setCreated(DateUtil.toString(source.getCreated()));
            }
			
			if(source.getModified() != null) {
            	view.setModified(DateUtil.toString(source.getModified()));
            }
		}
		
		return view;
	}
	
	public List<ThesaurusNoteView> convert(List<Note> source) {
		List<ThesaurusNoteView> views = new ArrayList<ThesaurusNoteView>();
		for (Note thesaurusNote : source) {
			views.add(convert(thesaurusNote));
		}
		return views;
	}
}