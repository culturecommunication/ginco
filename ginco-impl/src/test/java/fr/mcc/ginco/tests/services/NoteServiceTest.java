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
package fr.mcc.ginco.tests.services;


import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.dao.INoteDAO;
import fr.mcc.ginco.services.NoteServiceImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class NoteServiceTest {
    @Mock(name = "noteDAO")
    private INoteDAO noteDAO;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void testGetConceptNotePaginatedList() {
        List<Note> paginatedList = new ArrayList<Note>() {{
            add(new Note(){{setIdentifier("test");}});
        }};

        Mockito.when(noteDAO.findConceptPaginatedNotes(anyString(),any(Integer.class),any(Integer.class)))
                .thenReturn(paginatedList);
        Assert.assertEquals("List of paginated notes should not be empty!", 1,
                noteService.getConceptNotePaginatedList("test", 0, 1).size());
    }

    @Test
    public final void testGetTermNotePaginatedList() {
        List<Note> paginatedList = new ArrayList<Note>() {{
            add(new Note(){{setIdentifier("test");}});
        }};

        Mockito.when(noteDAO.findTermPaginatedNotes(anyString(),any(Integer.class),any(Integer.class)))
                .thenReturn(paginatedList);
        Assert.assertEquals("List of paginated notes should not be empty!", 1,
                noteService.getTermNotePaginatedList("test", 0, 1).size());
    }

    @Test
    public final void testCreateOrUpdateNote() {
        Note note = new Note(){{setIdentifier("test");}};

        Mockito.when(noteDAO.update(any(Note.class)))
                .thenReturn(note);
        Assert.assertEquals("Update should return updated object!", "test",
                noteService.createOrUpdateNote(note).getIdentifier());
    }

    @Test
    public final void testDeleteNote() {
        Note note = new Note(){{setIdentifier("test");}};

        Mockito.when(noteDAO.delete(any(Note.class)))
                .thenReturn(note);
        Assert.assertEquals("Delete should return deleted object!", "test",
                noteService.deleteNote(note).getIdentifier());
    }

    @Test
    public final void testGetConceptNoteCount() {
        List<Note> paginatedList = new ArrayList<Note>() {{
            add(new Note() {{
                setIdentifier("test");
            }});
            add(new Note(){{setIdentifier("test2");}});
        }};

        Mockito.when(noteDAO.getConceptNoteCount(anyString()))
                .thenReturn((long) paginatedList.size());
        Assert.assertEquals("List should contain right count of concept notes!", (long)paginatedList.size(), (long)noteService.getConceptNoteCount(""));
    }

    @Test
    public final void testGetTermNoteCount() {
        List<Note> paginatedList = new ArrayList<Note>() {{
            add(new Note() {{
                setIdentifier("test");
            }});
            add(new Note(){{setIdentifier("test2");}});
        }};

        Mockito.when(noteDAO.getTermNoteCount(anyString()))
                .thenReturn((long) paginatedList.size());
        Assert.assertEquals("List should contain right count of term notes!",
                (long)paginatedList.size(),
                (long)noteService.getTermNoteCount(""));
    }

    @Test
    public final void testGetNoteById() {
        Note note = new Note() {{setIdentifier("test");}};
        Note note2 = new Note(){{setIdentifier("test2");}};

        Mockito.when(noteDAO.getById("test"))
                .thenReturn(note);

        Mockito.when(noteDAO.getById("test2"))
                .thenReturn(note2);

        Assert.assertEquals("Expected right note!", note.getIdentifier(),
                noteService.getNoteById("test").getIdentifier());
        Assert.assertEquals("Expected right note!", note2.getIdentifier(),
                noteService.getNoteById("test2").getIdentifier());
    }
}
