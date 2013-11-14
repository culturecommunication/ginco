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
package fr.mcc.ginco.tests.imports.ginco.idgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.result.bean.GincoExportedBranch;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoAlignmentIdGenerator;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoConceptBranchIdGenerator;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoConceptIdGenerator;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoNoteIdGenerator;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoRelationshipIdGenerator;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoTermIdGenerator;

public class GincoConceptBranchIdGeneratorTest {	
	
	@Mock(name = "gincoTermIdGenerator")
	private GincoTermIdGenerator gincoTermIdGenerator;

	@Mock(name = "gincoConceptIdGenerator")
	private GincoConceptIdGenerator gincoConceptIdGenerator;

	@Mock(name = "gincoNoteIdGenerator")
	private GincoNoteIdGenerator gincoNoteIdGenerator;

	@Mock(name = "gincoRelationshipIdGenerator")
	private GincoRelationshipIdGenerator gincoRelationshipIdGenerator;
	
	@Mock(name = "gincoAlignmentIdGenerator")
	private GincoAlignmentIdGenerator gincoAlignmentIdGenerator;
	
	@InjectMocks
	private GincoConceptBranchIdGenerator gincoConceptBranchIdGenerator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testResetIdsForExportedBranch() {
		GincoExportedBranch branchToUpdate  = new GincoExportedBranch();
		ThesaurusConcept rootConcept = new ThesaurusConcept();
		rootConcept.setIdentifier("rootconceptId");
		branchToUpdate.setRootConcept(rootConcept);
		
		Map<String, String> fakeIdMap = new HashMap<String, String>();
		Mockito.when( gincoConceptIdGenerator.getIdForConcept("rootconceptId",fakeIdMap)).thenReturn("newRootConceptId");
		
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("idConcept1");
		ThesaurusConcept concept2 = new ThesaurusConcept();
		concept2.setIdentifier("idConcept2");
		List<ThesaurusConcept> branchToUpdateConcepts = new ArrayList<ThesaurusConcept>();
		branchToUpdateConcepts.add(concept1);
		branchToUpdateConcepts.add(concept2);
		branchToUpdate.setConcepts(branchToUpdateConcepts);
		
		Mockito.when( gincoConceptIdGenerator.getIdForConcept("idConcept1",fakeIdMap)).thenReturn("newIdConcept1");
		Mockito.when( gincoConceptIdGenerator.getIdForConcept("idConcept2",fakeIdMap)).thenReturn("idConcept2");
		
		Map<String, JaxbList<Note>> existingNotes = new HashMap <String, JaxbList<Note>>();
		branchToUpdate.setConceptNotes(existingNotes);		
		
		Map<String, JaxbList<Note>> newNotes = new HashMap <String, JaxbList<Note>>();
		Note newNote1 = new Note();
		newNote1.setIdentifier("newNote1");
		Note newNote2 = new Note();
		newNote2.setIdentifier("newNote2");

		JaxbList<Note> notes2 = new JaxbList<Note>();
		notes2.getList().add(newNote1);
		notes2.getList().add(newNote2);
		newNotes.put("newNotes", notes2);		
		
		Mockito.when(gincoNoteIdGenerator
			.getNotesWithNewIds(branchToUpdate.getConceptNotes(), fakeIdMap)).thenReturn(newNotes);		
		
		
		List<ThesaurusTerm> conceptTerms = new ArrayList<ThesaurusTerm>();
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setIdentifier("term1");
		term1.setConcept(new ThesaurusConcept());
		conceptTerms.add(term1);
		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setIdentifier("term2");
		term2.setConcept(new ThesaurusConcept());
		conceptTerms.add(term2);
		
		Mockito.when(gincoTermIdGenerator.getIdForTerm(
				"term1", fakeIdMap)).thenReturn("newTerm1");
		Mockito.when(gincoTermIdGenerator.getIdForTerm(
				"term2", fakeIdMap)).thenReturn("term2");		
		
		branchToUpdate.setTerms(conceptTerms);
		
		Map<String, JaxbList<Note>> newTermNotes = new HashMap <String, JaxbList<Note>>();
		Note newTermNote1 = new Note();
		newTermNote1.setIdentifier("newTermNote1");
		Note newTermNote2 = new Note();
		newTermNote2.setIdentifier("newTermNote2");

		JaxbList<Note> notesTerm2 = new JaxbList<Note>();
		notesTerm2.getList().add(newTermNote1);
		notesTerm2.getList().add(newTermNote2);
		newTermNotes.put("newNotes", notesTerm2);
		
		Mockito.when(gincoNoteIdGenerator
			.getNotesWithNewIds(branchToUpdate.getTermNotes(), fakeIdMap)).thenReturn(newTermNotes);		
		
		
		gincoConceptBranchIdGenerator.resetIdsForExportedBranch(branchToUpdate);		
				
		
		//Checks
		Assert.assertEquals("newRootConceptId", branchToUpdate.getRootConcept().getIdentifier());	
		List<ThesaurusConcept> updatedConcepts = branchToUpdate.getConcepts();
		
		Assert.assertEquals(2, updatedConcepts.size());
		List<String> newConceptIds = new ArrayList<String>();
		for (ThesaurusConcept thConcept:updatedConcepts) {
			newConceptIds.add(thConcept.getIdentifier());
		}
		ListAssert.assertContains(newConceptIds, "newIdConcept1");
		ListAssert.assertContains(newConceptIds, "idConcept2");
		
		
		JaxbList<Note> actualConceptNotes = branchToUpdate.getConceptNotes().get("newNotes");		
		ListAssert.assertEquals(notesTerm2.getList(), actualConceptNotes.getList());

		
		List<String> newTermIds = new ArrayList<String>();
		for (ThesaurusTerm thTerm:branchToUpdate.getTerms()) {
			newTermIds.add(thTerm.getIdentifier());
		}
		ListAssert.assertContains(newTermIds, "newTerm1");
		ListAssert.assertContains(newTermIds, "term2");		

		Assert.assertEquals(newTermNotes, branchToUpdate.getTermNotes());		
	}
	
}
