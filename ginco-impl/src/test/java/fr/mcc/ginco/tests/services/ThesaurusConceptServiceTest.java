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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IAlignmentDAO;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusConceptGroupDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.ConceptHierarchicalRelationshipServiceUtil;
import fr.mcc.ginco.services.ThesaurusConceptServiceImpl;

public class ThesaurusConceptServiceTest {

	@Mock
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Mock
	private IThesaurusDAO thesaurusDAO;

	@Mock
	private IThesaurusTermDAO thesaurusTermDAO;

	@Mock
	private IGenericDAO<AssociativeRelationship, Class<?>> associativeRelationshipDAO;

	@Mock
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@Mock
	private IThesaurusConceptGroupDAO thesaurusConceptGroupDAO;

	@Mock
	private IAlignmentDAO alignmentDAO;


	@InjectMocks
	private ThesaurusConceptServiceImpl thesaurusConceptService;

	@InjectMocks
	private ConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public final void testGetOrphanThesaurusConcepts() throws BusinessException {
		when(thesaurusDAO.getById(anyString())).thenReturn(new Thesaurus());
		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		when(
				thesaurusConceptDAO
						.getOrphansThesaurusConcept(any(Thesaurus.class), eq(0)))
				.thenReturn(concepts);

		List<ThesaurusConcept> thesaurusRes = thesaurusConceptService
				.getOrphanThesaurusConcepts("any-thesaurus-id");
		Assert.assertNotNull("Error while getting Thesaurus By Id",
				thesaurusRes);
	}

	@Test(expected = BusinessException.class)
	public final void testGetOrphanThesaurusConceptsWithWrongThesaurusId()
			throws BusinessException {
		when(thesaurusDAO.getById(anyString())).thenReturn(null);
		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		when(
				thesaurusConceptDAO
						.getOrphansThesaurusConcept(any(Thesaurus.class), eq(0)))
				.thenReturn(concepts);

		thesaurusConceptService.getOrphanThesaurusConcepts("any-thesaurus-id");
	}

	@Test
	public final void testGetTopTermThesaurusConceptsCount()
			throws BusinessException {
		List<ThesaurusConcept> list = new ArrayList<ThesaurusConcept>();
		ThesaurusConcept co1 = new ThesaurusConcept();
		co1.setIdentifier("co1");
		list.add(co1);
		when(
				thesaurusConceptDAO
						.getTopTermThesaurusConceptCount(any(Thesaurus.class)))
				.thenReturn((long) list.size());

		Assert.assertEquals("Not null list expected", 1, thesaurusConceptDAO
				.getTopTermThesaurusConceptCount(any(Thesaurus.class)));
	}

	@Test
	public final void testGetTopTermThesaurusConcepts()
			throws BusinessException {
		List<ThesaurusConcept> list = new ArrayList<ThesaurusConcept>();
		ThesaurusConcept co1 = new ThesaurusConcept();
		co1.setIdentifier("co1");
		list.add(co1);
		when(
				thesaurusConceptDAO
						.getTopTermThesaurusConcept(any(Thesaurus.class), eq(0)))
				.thenReturn(list);

		Assert.assertNotNull("Not null list expected", thesaurusConceptDAO
				.getTopTermThesaurusConcept(any(Thesaurus.class), eq(0)));
	}

	// ------------------------------------------
	// | root1 root3 root2 |
	// | / \ / \ / \ |
	// --leaf2_1=leaf2_2 leaf2_3 leaf2_4 |
	// \ / / \ / \ |
	// !!leaf1_1!!---- leaf1_2 leaf1_3<-|
	@Test
	public final void testGetRootsWithCycling() throws BusinessException {
		ThesaurusConcept leaf1_1 = new ThesaurusConcept();
		leaf1_1.setIdentifier("leaf1_1");
		ThesaurusConcept leaf1_2 = new ThesaurusConcept();
		leaf1_2.setIdentifier("leaf1_2");
		final ThesaurusConcept leaf1_3 = new ThesaurusConcept();
		leaf1_3.setIdentifier("leaf1_3");

		final ThesaurusConcept leaf2_1 = new ThesaurusConcept();
		leaf2_1.setIdentifier("leaf2_1");
		final ThesaurusConcept leaf2_2 = new ThesaurusConcept();
		leaf2_2.setIdentifier("leaf2_2");
		final ThesaurusConcept leaf2_3 = new ThesaurusConcept();
		leaf2_3.setIdentifier("leaf2_3");
		final ThesaurusConcept leaf2_4 = new ThesaurusConcept();
		leaf2_4.setIdentifier("leaf2_4");

		final ThesaurusConcept root1 = new ThesaurusConcept();
		root1.setIdentifier("root1");
		final ThesaurusConcept root2 = new ThesaurusConcept();
		root2.setIdentifier("root2");
		final ThesaurusConcept root3 = new ThesaurusConcept();
		root3.setIdentifier("root3");

		leaf2_1.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(root1);
				add(leaf2_2);
				add(leaf1_3);
			}
		});
		leaf2_2.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(root1);
				add(root3);
				add(leaf2_1);
			}
		});
		leaf2_3.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(root2);
				add(root3);
			}
		});
		leaf2_4.getParentConcepts().add(root2);

		leaf1_1.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(leaf2_1);
				add(leaf2_2);
				add(leaf2_3);
			}
		});
		leaf1_2.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(leaf2_3);
				add(leaf2_4);
			}
		});
		leaf1_3.getParentConcepts().add(leaf2_4);

		List<ThesaurusConcept> roots_leaf1_1 = conceptHierarchicalRelationshipServiceUtil
				.getRootConcepts(leaf1_1);

		Assert.assertEquals(3, roots_leaf1_1.size());

		List<ThesaurusConcept> roots_leaf1_3 = conceptHierarchicalRelationshipServiceUtil
				.getRootConcepts(leaf1_3);
		Assert.assertEquals(1, roots_leaf1_3.size());

		List<ThesaurusConcept> roots_leaf1_2 = conceptHierarchicalRelationshipServiceUtil
				.getRootConcepts(leaf1_2);
		Assert.assertEquals(2, roots_leaf1_2.size());
	}

	// node1
	// /\ |
	// | \/
	// node2<--node3
	//
	@Test
	public final void testGetRootsWithNoEvidentRoot() throws BusinessException {
		final ThesaurusConcept node1 = new ThesaurusConcept();
		node1.setIdentifier("node1");
		final ThesaurusConcept node2 = new ThesaurusConcept();
		node2.setIdentifier("node2");

		node1.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node2);
			}
		});
		node2.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node1);
			}
		});

		List<ThesaurusConcept> roots_leaf1_1 = conceptHierarchicalRelationshipServiceUtil
				.getRootConcepts(node1);
		Assert.assertEquals(1, roots_leaf1_1.size());
		Assert.assertEquals(node2.getIdentifier(), roots_leaf1_1.get(0)
				.getIdentifier());

		final ThesaurusConcept node3 = new ThesaurusConcept();
		node3.setIdentifier("node3");
		final ThesaurusConcept node4 = new ThesaurusConcept();
		node4.setIdentifier("node4");
		final ThesaurusConcept node5 = new ThesaurusConcept();
		node5.setIdentifier("node5");
		final ThesaurusConcept node6 = new ThesaurusConcept();
		node6.setIdentifier("node6");
		final ThesaurusConcept node7 = new ThesaurusConcept();
		node7.setIdentifier("node7");

		node2.getParentConcepts().clear();
		node2.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node3);
				add(node1);
			}
		});
		node3.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node4);
			}
		});
		node4.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node5);
			}
		});
		node5.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node6);
			}
		});
		node6.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node7);
			}
		});
		node7.getParentConcepts().addAll(new ArrayList<ThesaurusConcept>() {
			{
				add(node1);
			}
		});

		List<ThesaurusConcept> roots_node1 = conceptHierarchicalRelationshipServiceUtil
				.getRootConcepts(node1);
		Assert.assertEquals(1, roots_node1.size());
		Assert.assertEquals(node7.getIdentifier(), roots_node1.get(0)
				.getIdentifier());
	}

	@Test
	public final void testGetThesaurusConceptList() {
		final ThesaurusConcept node1 = new ThesaurusConcept();
		final ThesaurusConcept node2 = new ThesaurusConcept();
		List<ThesaurusConcept> allConcepts = new ArrayList<ThesaurusConcept>();
		allConcepts.add(node1);
		allConcepts.add(node2);
		when(thesaurusConceptDAO.findAll()).thenReturn(allConcepts);
		List<ThesaurusConcept> concepts = thesaurusConceptService
				.getThesaurusConceptList();
		Assert.assertEquals(2, concepts.size());

	}

	@Test
	public final void testGetThesaurusConceptById() {
		final ThesaurusConcept node1 = new ThesaurusConcept();
		node1.setIdentifier("concept-1");
		when(thesaurusConceptDAO.getById(anyString())).thenReturn(node1);
		ThesaurusConcept concept = thesaurusConceptService
				.getThesaurusConceptById("concept-1");
		Assert.assertEquals("concept-1", concept.getIdentifier());

	}

	@Test
	public final void testGetOrphanThesaurusConceptsCount() throws BusinessException {
		when(thesaurusDAO.getById(anyString())).thenReturn(new Thesaurus());
		when(thesaurusConceptDAO.getOrphansThesaurusConceptCount(any(Thesaurus.class))).thenReturn((long)2);
		long conceptNb = thesaurusConceptService
				.getOrphanThesaurusConceptsCount("thesaurus-1");
		Assert.assertEquals(2, conceptNb);

	}

	@Test(expected = BusinessException.class)
	public final void testGetOrphanThesaurusConceptsCountWithNotExistingThesaurus() throws BusinessException {
		when(thesaurusDAO.getById(anyString())).thenReturn(null);
		when(thesaurusConceptDAO.getOrphansThesaurusConceptCount(any(Thesaurus.class))).thenReturn((long)2);
		thesaurusConceptService
				.getOrphanThesaurusConceptsCount("thesaurus-1");
	}

	@Test
	public final void testDestroyThesaurusConcept() throws BusinessException {
		final ThesaurusConcept node1 = new ThesaurusConcept();
		node1.setIdentifier("concept1");
		node1.setStatus(ConceptStatusEnum.CANDIDATE.getStatus());
		ThesaurusTerm term1 = new ThesaurusTerm();
		List<ThesaurusTerm> termList = new ArrayList<ThesaurusTerm>();
		termList.add(term1);
		when(thesaurusTermDAO.findTermsByConceptId(anyString())).thenReturn(termList);

		final ThesaurusConcept node2 = new ThesaurusConcept();
		node2.setIdentifier("concept2");
		List<ThesaurusConcept> conceptList = new ArrayList<ThesaurusConcept>();
		when(thesaurusConceptDAO.getChildrenConcepts(anyString(), eq(0))).thenReturn(conceptList);

		when(thesaurusConceptDAO.getAllRootChildren(any(ThesaurusConcept.class))).thenReturn(conceptList);


		ThesaurusArray t1 = new ThesaurusArray();
		List<ThesaurusArray> arrays = new ArrayList<ThesaurusArray>();
		arrays.add(t1);
		when(thesaurusArrayDAO
				.getConceptSuperOrdinateArrays(anyString())).thenReturn(arrays);
		when(thesaurusConceptDAO.delete(any(ThesaurusConcept.class))).thenReturn(node1);

		ThesaurusConcept concept = thesaurusConceptService.destroyThesaurusConcept(node1);
		Assert.assertEquals("concept1", concept.getIdentifier());

	}

	@Test
	public void testGetConceptPreferredTerm()
			throws BusinessException {
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setIdentifier("term1");
		when(thesaurusTermDAO.getConceptPreferredTerm(anyString())).thenReturn(term1);
		ThesaurusTerm actualTerm = thesaurusConceptService.getConceptPreferredTerm("anyString");
		Assert.assertEquals("term1", actualTerm.getIdentifier());
	}

	@Test(expected = BusinessException.class)
	public void testGetConceptPreferredTermWithNoPreferredTerm()
			throws BusinessException {
		when(thesaurusTermDAO.getConceptPreferredTerm(anyString())).thenReturn(null);
		thesaurusConceptService.getConceptPreferredTerm("anyString");
	}

	@Test
	public void testGetAssociatedConcepts()
			throws BusinessException {
		final ThesaurusConcept node1 = new ThesaurusConcept();
		node1.setIdentifier("concept1");

		final ThesaurusConcept node2= new ThesaurusConcept();
		node2.setIdentifier("concept2");
		final ThesaurusConcept node3 = new ThesaurusConcept();
		node3.setIdentifier("concept3");
		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		concepts.add(node2);
		concepts.add(node3);

		when(thesaurusConceptDAO.getById(anyString())).thenReturn(node1);
		/*when(thesaurusConceptDAO.getAssociatedConcepts(node1)).thenReturn(concepts);

		List<ThesaurusConcept> returnedConcepts = thesaurusConceptService.getAssociatedConcepts("anyString");

		Assert.assertEquals(2, returnedConcepts.size());*/
	}

	/**
	 * Succeeds if BusinessException
	 */
	@Test(expected=BusinessException.class)
	public void testHierarchieBrotherAsParent() {
		final ThesaurusConcept parentConcept = new ThesaurusConcept();
		parentConcept.setIdentifier("parentConcept");
		final ThesaurusConcept brotherConcept = new ThesaurusConcept();
		brotherConcept.setIdentifier("brotherConcept");
		final ThesaurusConcept conflictConcept = new ThesaurusConcept();
		conflictConcept.setIdentifier("conflictConcept");

		// Creation of the Hierarchical relationships
		List<ConceptHierarchicalRelationship> conceptHierarchicalRelationshipList = new ArrayList<ConceptHierarchicalRelationship>();

		ConceptHierarchicalRelationship relationshipParentConflictConcepts = new ConceptHierarchicalRelationship();
		ConceptHierarchicalRelationship.Id id1 = new ConceptHierarchicalRelationship.Id();
		id1.setChildconceptid("conflictConcept");
		id1.setParentconceptid("parentConcept");
        relationshipParentConflictConcepts.setIdentifier(id1);
		conceptHierarchicalRelationshipList.add(relationshipParentConflictConcepts);

		ConceptHierarchicalRelationship relationshipBrotherConflictConcepts = new ConceptHierarchicalRelationship();
		ConceptHierarchicalRelationship.Id id2 = new ConceptHierarchicalRelationship.Id();
		id2.setChildconceptid("conflictConcept");
		id2.setParentconceptid("brotherConcept");
		relationshipBrotherConflictConcepts.setIdentifier(id2);
		conceptHierarchicalRelationshipList.add(relationshipBrotherConflictConcepts);

		// Mocks
		List<ThesaurusConcept> childrenOfParentConcept = new ArrayList<ThesaurusConcept>();
		childrenOfParentConcept.add(brotherConcept);
		when(thesaurusConceptDAO.getChildrenConcepts("parentConcept", 0)).thenReturn(childrenOfParentConcept);

		ThesaurusTerm dummyTerm = new ThesaurusTerm();
		dummyTerm.setLexicalValue("dummy Value");
		when(thesaurusTermDAO.getConceptPreferredTerm(anyString())).thenReturn(dummyTerm);

		conceptHierarchicalRelationshipServiceUtil.saveHierarchicalRelationship(
				conflictConcept,
				conceptHierarchicalRelationshipList,
				new ArrayList<ThesaurusConcept>(),
				new ArrayList<ThesaurusConcept>(),
				new ArrayList<ThesaurusConcept>(),
				new ArrayList<ThesaurusConcept>());
	}


	@Test
	public final void testGetAvailableConceptsOfGroup(){

		// Creation of concepts and group
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("concept1");
		ThesaurusConcept concept2 = new ThesaurusConcept();
		concept2.setIdentifier("concept2");

		List<ThesaurusConcept> fakeAvailableConcepts = new ArrayList<ThesaurusConcept>();
		fakeAvailableConcepts.add(concept1);
		fakeAvailableConcepts.add(concept2);

		ThesaurusConceptGroup fakeCurrentGroup = new ThesaurusConceptGroup();
		fakeCurrentGroup.setIdentifier("fakeCurrentGroup");
		Set<ThesaurusConcept> existedConcepts = new HashSet<ThesaurusConcept>();
		existedConcepts.add(concept1);
		fakeCurrentGroup.setConcepts(existedConcepts);

		List<ThesaurusConcept> resultAvailableConcepts = new ArrayList<ThesaurusConcept>();
		resultAvailableConcepts.add(concept2);

		// Mocks
		when(thesaurusConceptDAO
				.getAllConceptsByThesaurusId(anyString(), anyString(), any(Boolean.class), any(Boolean.class))).thenReturn(fakeAvailableConcepts);
		when(thesaurusConceptGroupDAO.getById(anyString())).thenReturn(fakeCurrentGroup);

		fakeAvailableConcepts = thesaurusConceptService.getAvailableConceptsOfGroup ("fakeCurrentGroup", "fakeThesaurus");
		Assert.assertEquals("The list with 'concept2' expected",fakeAvailableConcepts, resultAvailableConcepts);
	}
	
	@Test
	public void testGetRecursiveParentsByConceptId() {
		String conceptId1 = "http://c1";
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier(conceptId1);
		
		String conceptId11 = "http://c11";
		ThesaurusConcept concept11 = new ThesaurusConcept();
		concept11.setIdentifier(conceptId11);
		concept11.setParentConcepts(new HashSet<ThesaurusConcept>(Arrays.asList(concept1)));

		String conceptId12 = "http://c12";
		ThesaurusConcept concept12 = new ThesaurusConcept();
		concept12.setIdentifier(conceptId12);
		concept12.setParentConcepts(new HashSet<ThesaurusConcept>(Arrays.asList(concept1)));

		String conceptId121 = "http://c121";
		ThesaurusConcept concept121 = new ThesaurusConcept();
		concept121.setIdentifier(conceptId121);
		concept121.setParentConcepts(new HashSet<ThesaurusConcept>(Arrays.asList(concept12)));
		
		String conceptId1211 = "http://c1211";
		ThesaurusConcept concept1211 = new ThesaurusConcept();
		concept1211.setIdentifier(conceptId1211);
		concept1211.setParentConcepts(new HashSet<ThesaurusConcept>(Arrays.asList(concept121)));
		
		Mockito.when( thesaurusConceptDAO
				.getById("http://c1211")).thenReturn(concept1211);
		List<ThesaurusConcept> parentConcepts = thesaurusConceptService.getRecursiveParentsByConceptId(conceptId1211);
		Assert.assertEquals(3, parentConcepts.size());
		ListAssert.assertContains(parentConcepts, concept1);
		ListAssert.assertContains(parentConcepts, concept12);
		ListAssert.assertContains(parentConcepts, concept121);
	}
	
	
	
	@Test
	public void testGetRecursiveChildrenByConceptId() {
		String conceptId1 = "http://c1";
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier(conceptId1);

		String conceptId11 = "http://c11";
		ThesaurusConcept concept11 = new ThesaurusConcept();
		concept11.setIdentifier(conceptId11);

		String conceptId12 = "http://c12";
		ThesaurusConcept concept12 = new ThesaurusConcept();
		concept12.setIdentifier(conceptId12);

		String conceptId121 = "http://c121";
		ThesaurusConcept concept121 = new ThesaurusConcept();
		concept121.setIdentifier(conceptId121);

		String conceptId1211 = "http://c1211";
		ThesaurusConcept concept1211 = new ThesaurusConcept();
		concept1211.setIdentifier(conceptId1211);

		List<ThesaurusConcept> level1Concepts = new ArrayList<ThesaurusConcept>();
		level1Concepts.add(concept11);
		level1Concepts.add(concept12);
		List<ThesaurusConcept> level2Concepts = new ArrayList<ThesaurusConcept>();
		level2Concepts.add(concept121);
		List<ThesaurusConcept> level3Concepts = new ArrayList<ThesaurusConcept>();
		level3Concepts.add(concept1211);
		List<ThesaurusConcept> recursiveConcepts = new ArrayList<ThesaurusConcept>();
		level3Concepts.add(concept1211);

		Mockito.when( thesaurusConceptDAO
				.getChildrenConcepts("http://c1", 0)).thenReturn(level1Concepts);
		Mockito.when( thesaurusConceptDAO
				.getChildrenConcepts("http://c11", 0)).thenReturn(level2Concepts);
		Mockito.when( thesaurusConceptDAO
				.getChildrenConcepts("http://c121", 0)).thenReturn(level3Concepts);
		Mockito.when( thesaurusConceptDAO
				.getChildrenConcepts("http://c1211", 0)).thenReturn(recursiveConcepts);

		List<ThesaurusConcept> actualconcepts= thesaurusConceptService.getRecursiveChildrenByConceptId(conceptId1);
		Assert.assertEquals(4, actualconcepts.size());
		ListAssert.assertContains(actualconcepts, concept11);
		ListAssert.assertContains(actualconcepts, concept12);
		ListAssert.assertContains(actualconcepts, concept121);
		ListAssert.assertContains(actualconcepts, concept1211);

	}



}