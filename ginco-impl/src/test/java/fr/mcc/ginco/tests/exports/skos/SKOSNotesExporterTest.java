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
package fr.mcc.ginco.tests.exports.skos;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.NoteType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.skos.SKOSNotesExporter;
import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * This component is in charge of exporting collections to SKOS
 *
 */
public class SKOSNotesExporterTest {		
	@Mock(name="noteService")
	private INoteService noteService;
	
	@InjectMocks
	private SKOSNotesExporter skosNotesExporter;	
	
	
	
	@Before
	public void init() {		
			MockitoAnnotations.initMocks(this);	
	}
	
	@Test
	public void testExportNotes() throws IOException {		

		NoteType exType = new NoteType();
		exType.setCode("example");
		
		NoteType defType = new NoteType();
		defType.setCode("definition");
		
		
		NoteType historyType = new NoteType();
		historyType.setCode("historyNote");
		
		NoteType scopeType = new NoteType();
		scopeType.setCode("scopeNote");
		
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");
		
		Language lang = new Language();
		lang.setId("fr-FR");

		
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		
		Note n1 = new Note();
		n1.setNoteType(exType);
		n1.setLanguage(lang);
		n1.setLexicalValue("exemple");		
		
		Note n2 = new Note();
		n2.setNoteType(historyType);
		n2.setLanguage(lang);
		n2.setLexicalValue("history");
		
		ArrayList<Note> conceptNotes = new ArrayList<Note>();
		conceptNotes.add(n1);
		conceptNotes.add(n2);

		ThesaurusTerm t1 = new ThesaurusTerm();
		t1.setIdentifier("http://t1");
		
		Note n3 = new Note();
		n3.setNoteType(defType);
		n3.setLanguage(lang);
		n3.setLexicalValue("definition");
		
		Note n4 = new Note();
		n4.setNoteType(scopeType);
		n4.setLanguage(lang);
		n4.setLexicalValue("scope");
		
		ArrayList<Note> termNotes = new ArrayList<Note>();
		termNotes.add(n3);
		termNotes.add(n4);

		ArrayList<ThesaurusTerm> prefTerms = new ArrayList<ThesaurusTerm>();
		prefTerms.add(t1);
		
		Mockito.when(noteService.getConceptNotePaginatedList("http://c1", 0, 0)).thenReturn(conceptNotes);
		Mockito.when(noteService.getTermNotePaginatedList("http://t1", 0, 0)).thenReturn(termNotes);
	
		Model model = ModelFactory.createDefaultModel();
		skosNotesExporter.exportNotes(model,prefTerms,c1);
		
		Model expectedModel = ModelFactory.createDefaultModel();
		Resource expectedRes = expectedModel.createResource("http://c1");
		
		Assert.assertTrue(model.contains(expectedRes, SKOS.EXAMPLE, "exemple", "fr-FR"));
		Assert.assertTrue(model.contains(expectedRes, SKOS.HISTORY_NOTE, "history", "fr-FR"));
		Assert.assertTrue(model.contains(expectedRes, SKOS.DEFINITION, "definition", "fr-FR"));
		Assert.assertTrue(model.contains(expectedRes, SKOS.SCOPE_NOTE, "scope", "fr-FR"));
	
	}
}
