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
package fr.mcc.ginco.tests.rest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.NoteType;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusNoteView;
import fr.mcc.ginco.extjs.view.utils.ThesaurusNoteViewConverter;
import fr.mcc.ginco.rest.services.ThesaurusNoteRestService;
import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.services.INoteTypeService;

public class ThesaurusNoteRestServiceTest {
	
	@Mock(name="noteTypeService")
	private INoteTypeService noteTypeService;

	@Mock(name="noteService")
	private INoteService noteService;
	
	@Mock(name="thesaurusNoteViewConverter")
    private ThesaurusNoteViewConverter thesaurusNoteViewConverter;
	
	
	@InjectMocks
	private ThesaurusNoteRestService thesaurusNoteRestService = new ThesaurusNoteRestService();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Test to get concept note types
	 * 
	 */
	@Test
	public final void getAllConceptNoteTypes() throws BusinessException {
		NoteType fakeNoteType1 = getFakeNoteTypeWithCodeAndEmptyValues("fakeType1");
		NoteType fakeNoteType2 = getFakeNoteTypeWithCodeAndEmptyValues("fakeType2");
		
		List<NoteType> noteTypes = new ArrayList<NoteType>();
		noteTypes.add(fakeNoteType1);
		noteTypes.add(fakeNoteType2);
		
		Mockito.when(noteTypeService.getConceptNoteTypeList()).thenReturn(noteTypes);
		ExtJsonFormLoadData<List<NoteType>> actualResponse = thesaurusNoteRestService.getConceptNoteTypes();
		Assert.assertEquals(noteTypes.get(0).getCode(), actualResponse.getData().get(0).getCode());
	}
	
	/**
	 * Test to get term note types
	 * 
	 */
	@Test
	public final void getAllTermNoteTypes() throws BusinessException {
		NoteType fakeNoteType1 = getFakeNoteTypeWithCodeAndEmptyValues("fakeType1");
		NoteType fakeNoteType2 = getFakeNoteTypeWithCodeAndEmptyValues("fakeType2");
		
		List<NoteType> noteTypes = new ArrayList<NoteType>();
		noteTypes.add(fakeNoteType1);
		noteTypes.add(fakeNoteType2);
		
		Mockito.when(noteTypeService.getTermNoteTypeList()).thenReturn(noteTypes);
		ExtJsonFormLoadData<List<NoteType>> actualResponse = thesaurusNoteRestService.getTermNoteTypes();
		Assert.assertEquals(noteTypes.get(0).getCode(), actualResponse.getData().get(0).getCode());
	}
	
	/**
	 * Test to get notes of a concept
	 * 
	 */
	@Test
	public final void getAllNotesOfAConcept() throws BusinessException {
		
		
		Note fakeNote1 = getFakeNoteWithIdAndEmptyValues("fakeNote1");
		Note fakeNote2 = getFakeNoteWithIdAndEmptyValues("fakeNote2");
		List<Note> notes = new ArrayList<Note>();
		notes.add(fakeNote1);
		notes.add(fakeNote2);
		
		ThesaurusNoteView fakeThesaurusNoteView1 = getFakeThesaurusNoteViewWithIdAndEmptyValues("fakeNote1");
		ThesaurusNoteView fakeThesaurusNoteView2 = getFakeThesaurusNoteViewWithIdAndEmptyValues("fakeNote2");
		List<ThesaurusNoteView> noteViews = new ArrayList<ThesaurusNoteView>();
		noteViews.add(fakeThesaurusNoteView1);
		noteViews.add(fakeThesaurusNoteView2);
		
		Mockito.when(noteService.getConceptNotePaginatedList(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(notes);
		Mockito.when(thesaurusNoteViewConverter.convert(notes)).thenReturn(noteViews);
		ExtJsonFormLoadData<List<ThesaurusNoteView>> actualResponse = thesaurusNoteRestService.getNotes("", "", 0, 10);
		Assert.assertEquals(noteViews.size(), actualResponse.getData().size());
		Assert.assertEquals(noteViews.get(0).getIdentifier(), actualResponse.getData().get(0).getIdentifier());
	}
	
	/**
	 * This method returns a fake NoteType with the code given in parameter
	 * @param code of the type
	 * @return an object NoteType with code given in parameter
	 */
	private NoteType getFakeNoteTypeWithCodeAndEmptyValues(String code) {
		NoteType fakeNoteType = new NoteType();
		fakeNoteType.setCode(code);
		return fakeNoteType; 
	}
	
	/**
	 * This method returns a fake Note with the id given in parameter
	 * @param id of the note
	 * @return an object Note with id given in parameter
	 */
	private Note getFakeNoteWithIdAndEmptyValues(String id) {
		Note fakeNote = new Note();
		fakeNote.setIdentifier(id);
		return fakeNote;
	}
	
	/**
	 * This method returns a fake ThesaurusNoteView with the id given in parameter
	 * @param id of the note
	 * @return an object Note with id given in parameter
	 */
	private ThesaurusNoteView getFakeThesaurusNoteViewWithIdAndEmptyValues(String id) {
		ThesaurusNoteView fakeThesaurusNoteView = new ThesaurusNoteView();
		fakeThesaurusNoteView.setIdentifier(id);
		return fakeThesaurusNoteView; 
	}
}