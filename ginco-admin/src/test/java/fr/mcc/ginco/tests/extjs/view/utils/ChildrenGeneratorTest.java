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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListNodeFactory;
import fr.mcc.ginco.extjs.view.utils.ChildrenGenerator;
import fr.mcc.ginco.services.IThesaurusConceptService;

public class ChildrenGeneratorTest {

    @Mock(name = "thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;
    
    @Mock(name = "thesaurusListNodeFactory")
    private ThesaurusListNodeFactory thesaurusListNodeFactory;

    @InjectMocks
    private ChildrenGenerator childrenGenerator = new ChildrenGenerator();

    @Before
    public final void setUp() {
        MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(childrenGenerator, "maxResults",
				5000);
    }

    @Test
    public final void testChildrenLeafsCreation(){

        Thesaurus thesaurus = new Thesaurus();
        thesaurus.setIdentifier("th1");

        ThesaurusConcept co1 = new ThesaurusConcept();
        co1.setIdentifier("co1");
        co1.setThesaurus(thesaurus);

        ThesaurusConcept co2 = new ThesaurusConcept();
        co2.setIdentifier("co2");
        co2.setThesaurus(thesaurus);

        List<ThesaurusConcept> children = new ArrayList<ThesaurusConcept>();
        children.add(co1);
        children.add(co2);

        Mockito.when(
                thesaurusConceptService
                        .getChildrenByConceptId(Matchers.anyString(), Matchers.eq(5001), Matchers.anyString()))
                .thenReturn(children);
        
        Mockito.when(
                thesaurusConceptService
                        .getConceptLabel(Matchers.anyString()))
                .thenReturn("a","zzzz");
        
        
        Mockito.when(
        		thesaurusListNodeFactory.getListBasicNode())
                .thenReturn(new ThesaurusListBasicNode(),new ThesaurusListBasicNode());

        Mockito.when(
                thesaurusConceptService
                        .hasChildren(Matchers.anyString()))
                .thenReturn(false); 

        List<IThesaurusListNode> nodes =
                childrenGenerator.getChildrenByConceptId(ChildrenGenerator.ID_PREFIX
                + ChildrenGenerator.PARENT_SEPARATOR
                + "5");

        Assert.assertEquals("Number of children does not correspond!", 2, nodes.size());
        Assert.assertEquals("Title does not correspond!", "a", nodes.get(0).getTitle());
        Assert.assertTrue("ID does not correspond!",nodes.get(0).getId().endsWith(
                 "5"
                        + ChildrenGenerator.PARENT_SEPARATOR
                        + "co1" ));
        Assert.assertEquals("Type does not correspond!", ThesaurusListNodeType.CONCEPT, nodes.get(0).getType());
        Assert.assertEquals("Thesaurus was badly recuperated!", thesaurus.getIdentifier(), nodes.get(0).getThesaurusId());
        Assert.assertTrue("Node should be leaf!", nodes.get(0).isLeaf());
    }

    @Test
    public final void testChildrenBranchesCreation() {

        Thesaurus thesaurus = new Thesaurus();
        thesaurus.setIdentifier("th1");

        ThesaurusConcept co1 = new ThesaurusConcept();
        co1.setIdentifier("co1");
        co1.setThesaurus(thesaurus);

        ThesaurusConcept co2 = new ThesaurusConcept();
        co2.setIdentifier("co2");
        co2.setThesaurus(thesaurus);

        List<ThesaurusConcept> children = new ArrayList<ThesaurusConcept>();
        children.add(co1);
        children.add(co2);

        Mockito.when(
                thesaurusConceptService
                        .getChildrenByConceptId(Matchers.anyString(),Matchers.eq(5001),Matchers.anyString()))
                .thenReturn(children);
        
        Mockito.when(
        		thesaurusListNodeFactory.getListBasicNode())
                .thenAnswer(new Answer<ThesaurusListBasicNode>() {
                		public ThesaurusListBasicNode answer(
							InvocationOnMock invocation) throws Throwable {
						return new ThesaurusListBasicNode();
					}
                   });

        
        Mockito.when(
                thesaurusConceptService
                        .getConceptLabel("co1"))
                .thenReturn("zzzz");
        Mockito.when(
                thesaurusConceptService
                        .getConceptLabel("co2"))
                .thenReturn("aaaa");

        Mockito.when(
                thesaurusConceptService
                        .hasChildren(Matchers.anyString()))
                .thenReturn(true);

        List<IThesaurusListNode> nodes =
                childrenGenerator.getChildrenByConceptId(ChildrenGenerator.ID_PREFIX
                        + ChildrenGenerator.PARENT_SEPARATOR
                        + "5");

        Assert.assertEquals("Number of children does not correspond!", 2, nodes.size());
        //Test alphabetical order
        Assert.assertTrue(nodes.get(0).getId().endsWith("5*co2"));

        
        Assert.assertEquals("Title does not correspond!", "aaaa", nodes.get(0).getTitle());
        Assert.assertTrue("ID does not correspond!", nodes.get(0).getId().endsWith("5*co2"));
        Assert.assertEquals("Type does not correspond!", ThesaurusListNodeType.CONCEPT, nodes.get(0).getType());
        Assert.assertEquals("Thesaurus was badly recuperated!", thesaurus.getIdentifier(), nodes.get(0).getThesaurusId());
        Assert.assertFalse("Node should not be leaf!", nodes.get(0).isLeaf());
        Assert.assertNull("Children should be null", nodes.get(0).getChildren());
    }
}
