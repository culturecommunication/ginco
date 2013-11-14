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
package fr.mcc.ginco.tests.extjs.view.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListNodeFactory;
import fr.mcc.ginco.extjs.view.utils.OrphansGenerator;
import fr.mcc.ginco.services.IThesaurusConceptService;

public class OrphansGeneratorTest {

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@InjectMocks
	private OrphansGenerator orphanGenerator;
	
    @Mock(name = "thesaurusListNodeFactory")
    private ThesaurusListNodeFactory thesaurusListNodeFactory;

	@Before
	public final void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(orphanGenerator, "maxResults",
				5000);
	}
	
	@Test
	public void testGenerateOrphans() throws BusinessException {
		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();

        Mockito.when(
        		thesaurusListNodeFactory.getListBasicNode())
                .thenAnswer(new Answer<ThesaurusListBasicNode>() {
                		public ThesaurusListBasicNode answer(
							InvocationOnMock invocation) throws Throwable {
						return new ThesaurusListBasicNode();
					}
                   });
		
        Thesaurus fakeThesaurus = new Thesaurus();
        fakeThesaurus.setIdentifier("th1");

        ThesaurusConcept co1 = new ThesaurusConcept();
        co1.setIdentifier("co1");
        co1.setThesaurus(fakeThesaurus);
        ThesaurusConcept co2 = new ThesaurusConcept();
        co2.setIdentifier("co2");
        co2.setThesaurus(fakeThesaurus);
		concepts.add(co2);
		concepts.add(co1);

		 Mockito.when(thesaurusConceptService
			.getOrphanThesaurusConcepts(Mockito.anyString(), Mockito.eq(5001))).thenReturn(concepts);
		 Mockito.when(thesaurusConceptService.getConceptLabel("co1")).thenReturn("zzzzz");
		 Mockito.when(thesaurusConceptService.getConceptLabel("co2")).thenReturn("aaaa");
		 List<IThesaurusListNode> nodes = orphanGenerator.generateOrphans("anystring");
		 
		 Assert.assertEquals(2, nodes.size());
		 //Test alphabetical order
		 Assert.assertEquals("CONCEPT_*co2", nodes.get(0).getId());

	}
	
}
