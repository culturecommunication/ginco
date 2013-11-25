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
package fr.mcc.ginco.tests.exports.ginco;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IConceptHierarchicalRelationshipDAO;
import fr.mcc.ginco.exports.ginco.GincoConceptExporter;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.services.IAssociativeRelationshipService;
import fr.mcc.ginco.services.INoteService;

/**
 * This class tests the methods of MCCConceptExporter
 * (export of thesaurus in MCC XML format)
 *
 */
public class GincoConceptExporterTest {
	
	@Mock(name="noteService")
	private INoteService noteService;
	
    @Mock
    private IConceptHierarchicalRelationshipDAO conceptHierarchicalRelationshipDAO;
	
	@Mock(name="associativeRelationshipService")
	private IAssociativeRelationshipService associativeRelationshipService;
	
	@InjectMocks
	GincoConceptExporter gincoConceptExporter;
	
	@Before
	public void init() {		
			MockitoAnnotations.initMocks(this);	
	}
	
	@Test
	public void testGetExportHierarchicalConcepts() {
		
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");
		
		Set<ThesaurusConcept> parents = new HashSet<ThesaurusConcept>();
		parents.add(c2);
		c1.setParentConcepts(parents);

		ConceptHierarchicalRelationship relation = new ConceptHierarchicalRelationship();
		ConceptHierarchicalRelationship.Id id = new ConceptHierarchicalRelationship.Id();
        id.setChildconceptid(c1.getIdentifier());
        id.setParentconceptid(c2.getIdentifier());
        relation.setIdentifier(id);
		
		Mockito.when(conceptHierarchicalRelationshipDAO.getById(Mockito.any(ConceptHierarchicalRelationship.Id.class))).thenReturn(relation);		
		
		JaxbList<ConceptHierarchicalRelationship> result = gincoConceptExporter.getExportHierarchicalConcepts(c1);
		Assert.assertEquals(result.getList().size(), c1.getParentConcepts().size());
		Assert.assertEquals(result.getList().get(0).getIdentifier().getParentconceptid(), c2.getIdentifier());
	}
	
	@Test
	public void testGetExportConceptNotes() {
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		
		Note fakeNote1 = new Note();
		fakeNote1.setIdentifier("http://n1");
		
		Note fakeNote2 = new Note();
		fakeNote2.setIdentifier("http://n2");
		
		List<Note> notes = new ArrayList<Note>();
		notes.add(fakeNote1);
		notes.add(fakeNote2);
		Long count = (long) 2;
		
		Mockito.when(noteService.getConceptNoteCount(Mockito.anyString())).thenReturn((long) count);
		Mockito.when(noteService.getConceptNotePaginatedList(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(notes);
		
		JaxbList<Note> resultNotes = gincoConceptExporter.getExportConceptNotes(c1);
		Assert.assertEquals(resultNotes.getList().size(), notes.size());
	}
	
	@Test
	public void testGetExportAssociativeRelationShip() {
		
			ThesaurusConcept thesaurusConcept = new ThesaurusConcept();
			List<String> relationships  = new ArrayList<String>();
			relationships.add("http://id1");
			relationships.add("http://id2");

			Mockito.when(associativeRelationshipService.getAssociatedConceptsId(thesaurusConcept)).thenReturn(relationships);
			
			JaxbList<AssociativeRelationship> actualRes = gincoConceptExporter.getExportAssociativeRelationShip(thesaurusConcept);
			
			//Assert.assertEquals(2, actualRes.getList().size());
		
	}
}
