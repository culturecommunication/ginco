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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoAlignmentIdGenerator;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoIdMapParser;

public class GincoAlignmentIdGeneratorTest {	
	
	@Mock
	private GincoIdMapParser gincoIdMapParser;	
	
	@InjectMocks
	private GincoAlignmentIdGenerator gincoAlignmentIdGenerator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetIdsForAlignments() {		
		
		Map<String, JaxbList<Alignment>> alignments = new HashMap<String, JaxbList<Alignment>>();
		
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("oldId1");
		Alignment alignment1 = new Alignment();
		alignment1.setSourceConcept(concept1);
		Alignment alignment2 = new Alignment();
		alignment2.setSourceConcept(concept1);
		List<Alignment> alignments1 = new ArrayList<Alignment>();
		alignments1.add(alignment1);
		alignments1.add(alignment2);
		
		alignments.put("oldId1", new JaxbList<Alignment>(alignments1));
		
		ThesaurusConcept concept2 = new ThesaurusConcept();
		concept2.setIdentifier("oldId2");
		Alignment alignment3 = new Alignment();
		alignment3.setSourceConcept(concept2);	
		List<Alignment> alignments2 = new ArrayList<Alignment>();
		alignments2.add(alignment3);
		
		alignments.put("oldId2", new JaxbList<Alignment>(alignments2));
		
		Map<String, String> idMapping = new HashMap<String, String>();

		
		Mockito.when(gincoIdMapParser.getNewId("oldId1", idMapping)).thenReturn("id1");
		Mockito.when(gincoIdMapParser.getNewId("oldId2", idMapping)).thenReturn("id2");
		
		
		
		Map<String, JaxbList<Alignment>> result = gincoAlignmentIdGenerator.getIdsForAlignments(alignments, idMapping);
		

		Assert.assertEquals(2, result.size());
		
		Assert.assertNotNull(result.get("id1"));
		Assert.assertEquals(2, result.get("id1").getList().size());
		Assert.assertEquals("id1", result.get("id1").getList().get(0).getSourceConcept().getIdentifier());
		Assert.assertEquals("id1", result.get("id1").getList().get(1).getSourceConcept().getIdentifier());

		
		Assert.assertNotNull(result.get("id2"));
		Assert.assertEquals(1, result.get("id2").getList().size());	
		Assert.assertEquals("id2", result.get("id2").getList().get(0).getSourceConcept().getIdentifier());	
	}
	
}
