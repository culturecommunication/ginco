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

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusNoteView;
import fr.mcc.ginco.services.*;
import fr.mcc.ginco.utils.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Component("thesaurusNoteViewConverter")
public class ThesaurusNoteViewConverter {

	@Inject
	@Named("noteService")
	private INoteService noteService;

	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;

	@Inject
	@Named("noteTypeService")
	private INoteTypeService noteTypeService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;

	/**
	 * This method converts a single {@link Note} into a
	 * {@link ThesaurusNoteView}
	 * 
	 * @param source
	 *            (a {@link Note} object)
	 * @return result (a {@link ThesaurusNoteView} object)
	 */
	public ThesaurusNoteView convert(Note source) {
		ThesaurusNoteView view = new ThesaurusNoteView();
		if (source != null) {
			view.setIdentifier(source.getIdentifier());
			view.setLexicalValue(source.getLexicalValue());
			view.setLanguage(source.getLanguage().getId());
			view.setType(source.getNoteType().getCode());
			if (source.getSource() != null) {
				view.setSource(source.getSource());
			}

			if (source.getCreated() != null) {
				view.setCreated(DateUtil.toString(source.getCreated()));
			}

			if (source.getModified() != null) {
				view.setModified(DateUtil.toString(source.getModified()));
			}

			if (source.getConcept() != null) {
				view.setConceptId(source.getConcept().getIdentifier());
			} else if (source.getTerm() != null) {
				view.setTermId(source.getTerm().getIdentifier());
			}
		}

		return view;
	}

	/**
	 * This method converts a list of {@link Note} objects to a list of
	 * {@link ThesaurusNoteView}
	 * 
	 * @param source
	 *            (a list of {@link Note})
	 * @return result (a list of {@link ThesaurusNoteView} )
	 */
	public List<ThesaurusNoteView> convert(List<Note> source) {
		List<ThesaurusNoteView> views = new ArrayList<ThesaurusNoteView>();
		for (Note thesaurusNote : source) {
			views.add(convert(thesaurusNote));
		}
		return views;
	}

	/**
	 * This method converts a single {@link ThesaurusNoteView} into a
	 * {@link Note}
	 * 
	 * @param source
	 *            (a {@link ThesaurusNoteView} object)
	 * @return result (a {@link Note} object)
	 * @throws BusinessException
	 */
	public Note convert(ThesaurusNoteView source) throws BusinessException {
		Note hibernateRes;
		if (StringUtils.isEmpty(source.getIdentifier())) {
			hibernateRes = new Note();
			hibernateRes.setCreated(DateUtil.nowDate());
			hibernateRes.setIdentifier(generatorService.generate(Note.class));
		} else {
			hibernateRes = noteService.getNoteById(source.getIdentifier());

		}

		hibernateRes.setLexicalValue(source.getLexicalValue());

		if (source.getLanguage() != null) {
			Language lang = languagesService.getLanguageById(source
					.getLanguage());
			if (lang != null) {
				hibernateRes.setLanguage(lang);
			}
		}

		if (source.getSource() != null) {
			hibernateRes.setSource(source.getSource());
		}
		hibernateRes.setModified(DateUtil.nowDate());
		hibernateRes.setNoteType(noteTypeService.getNoteTypeById(source
				.getType()));

		if (source.getConceptId() != null) {
			hibernateRes.setConcept(thesaurusConceptService
					.getThesaurusConceptById(source.getConceptId()));
		} else if (source.getTermId() != null) {
			hibernateRes.setTerm(thesaurusTermService
					.getThesaurusTermById(source.getTermId()));
		}

		return hibernateRes;
	}

	public List<Note> convertToNote(List<ThesaurusNoteView> source)
			throws BusinessException {
		List<Note> views = new ArrayList<Note>();
		for (ThesaurusNoteView thesaurusNoteView : source) {
			views.add(convert(thesaurusNoteView));
		}
		return views;
	}
}