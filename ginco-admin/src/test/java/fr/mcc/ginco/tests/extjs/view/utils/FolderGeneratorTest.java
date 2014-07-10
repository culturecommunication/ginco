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

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListNodeFactory;
import fr.mcc.ginco.extjs.view.utils.FolderGenerator;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;

public class FolderGeneratorTest {
	@Mock
	private IThesaurusConceptService thesaurusConceptService;
	
	@Mock
	private IThesaurusTermService thesaurusTermService;

	@Mock
	private IThesaurusArrayService thesaurusArrayService;

	@Mock
	private ThesaurusListNodeFactory thesaurusListNodeFactory;
	
	@Mock
	private IThesaurusConceptGroupService thesaurusConceptGroupService;

	@Mock
	private ISplitNonPreferredTermService splitNonPreferredTermService;
	
	@InjectMocks
	private FolderGenerator folderGenerator = new FolderGenerator();

	

	@Before
	public final void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public final void testGetConceptsWithChildren() {
		Mockito.when(
				thesaurusConceptService
						.getTopTermThesaurusConceptsCount("fake1")).thenReturn(
				(long) 3);

		IThesaurusListNode actualNode = folderGenerator.getConcepts("fake1");
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, actualNode.getType());
		Assert.assertEquals("CONCEPTS_fake1", actualNode.getId());
		Assert.assertEquals("Arborescence des concepts", actualNode.getTitle());
		Assert.assertEquals(false, actualNode.isExpanded());
		Assert.assertEquals(false, actualNode.isDisplayable());
		Assert.assertEquals("icon-tree", actualNode.getIconCls());
		Assert.assertNull(actualNode.getChildren());
	}

	@Test
	public final void testGetConceptsWithNoChildren() {
		Mockito.when(
				thesaurusConceptService
						.getTopTermThesaurusConceptsCount("fake1")).thenReturn(
				(long) 0);
		IThesaurusListNode actualNode = folderGenerator.getConcepts("fake1");
		Assert.assertNotNull(actualNode.getChildren());
	}

	@Test
	public final void testGetArraysWithChildren() {
		List<ThesaurusArray> realArrays = new ArrayList<ThesaurusArray>();
		ThesaurusArray array1 = new ThesaurusArray();
		realArrays.add(array1);

		Mockito.when(
				thesaurusArrayService.getAllThesaurusArrayByThesaurusId(null,
						"fake1")).thenReturn(realArrays);

		IThesaurusListNode actualNode = folderGenerator.getArrays("fake1");
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, actualNode.getType());
		Assert.assertEquals("ARRAYS_fake1", actualNode.getId());
		Assert.assertEquals("Tableaux", actualNode.getTitle());
		Assert.assertEquals(false, actualNode.isExpanded());
		Assert.assertEquals(false, actualNode.isDisplayable());
		Assert.assertEquals("icon-table", actualNode.getIconCls());
		Assert.assertNull(actualNode.getChildren());
	}

	@Test
	public final void testGetArraysWithNoChildren() {
		List<ThesaurusArray> realArrays = new ArrayList<ThesaurusArray>();

		Mockito.when(
				thesaurusArrayService.getAllThesaurusArrayByThesaurusId(null,
						"fake1")).thenReturn(realArrays);

		IThesaurusListNode actualNode = folderGenerator.getArrays("fake1");
		Assert.assertNull(actualNode);
	}

	@Test
	public final void testGetGroupsWithChildren() {
		List<ThesaurusConceptGroup> realGroups = new ArrayList<ThesaurusConceptGroup>();
		ThesaurusConceptGroup group = new ThesaurusConceptGroup();
		realGroups.add(group);

		Mockito.when(
				thesaurusConceptGroupService
						.getAllThesaurusConceptGroupsByThesaurusId(null,
								"fake1")).thenReturn(realGroups);

		IThesaurusListNode actualNode = folderGenerator.getGroups("fake1");
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, actualNode.getType());
		Assert.assertEquals("GROUPS_fake1", actualNode.getId());
		Assert.assertEquals("Groupes", actualNode.getTitle());
		Assert.assertEquals(false, actualNode.isExpanded());
		Assert.assertEquals(false, actualNode.isDisplayable());
		Assert.assertEquals("icon-group", actualNode.getIconCls());

		Assert.assertNull(actualNode.getChildren());
	}

	@Test
	public final void testGetGroupsWithNoChildren() {
		Mockito.when(
				thesaurusConceptGroupService
						.getAllThesaurusConceptGroupsByThesaurusId(null,
								"fake1")).thenReturn(null);
		IThesaurusListNode actualNode = folderGenerator.getGroups("fake1");
		Assert.assertNull(actualNode);
	}

	@Test
	public final void testGetOrphansWithChildren() {

		Mockito.when(
				thesaurusConceptService
						.getOrphanThesaurusConceptsCount("fake1")).thenReturn(
				(long) 2);

		IThesaurusListNode actualNode = folderGenerator.getOrphans("fake1");

		Assert.assertEquals(ThesaurusListNodeType.FOLDER, actualNode.getType());
		Assert.assertEquals("ORPHANS_fake1", actualNode.getId());
		Assert.assertEquals("Concepts orphelins", actualNode.getTitle());
		Assert.assertEquals(false, actualNode.isExpanded());
		Assert.assertEquals(false, actualNode.isDisplayable());
		Assert.assertEquals("sandbox", actualNode.getIconCls());
		Assert.assertNull(actualNode.getChildren());
	}

	@Test
	public final void testGetOrphansWithNoChildren() {
		Mockito.when(
				thesaurusConceptService
						.getOrphanThesaurusConceptsCount("fake1")).thenReturn(
				(long) 0);

		IThesaurusListNode actualNode = folderGenerator.getOrphans("fake1");

		Assert.assertNull(actualNode);
	}

	@Test
	public final void testGetSandboxWithChildren() {
		Mockito.when(thesaurusTermService.getSandboxedTermsCount("fake1")).thenReturn((long)1);

		IThesaurusListNode actualNode = folderGenerator.getSandbox("fake1");

		Assert.assertEquals(ThesaurusListNodeType.FOLDER, actualNode.getType());
		Assert.assertEquals("SANDBOX_fake1", actualNode.getId());
		Assert.assertEquals("Termes orphelins", actualNode.getTitle());
		Assert.assertEquals(false, actualNode.isExpanded());
		Assert.assertEquals(true, actualNode.isDisplayable());
		Assert.assertEquals("sandbox", actualNode.getIconCls());

		Assert.assertNotNull(actualNode.getChildren());
	}
	
	@Test
	public final void testGetSandboxWithNoChildren() {
		
		Mockito.when(thesaurusTermService.getSandboxedTermsCount("fake1")).thenReturn((long)0);

		IThesaurusListNode actualNode = folderGenerator.getSandbox("fake1");
		Assert.assertNull(actualNode);
	}

	@Test
	public final void testGetSplitNonPreferredTermsWithChildren() {
		
		Mockito.when(splitNonPreferredTermService.getSplitNonPreferredTermCount("fake1")).thenReturn((long)2);
		IThesaurusListNode actualNode = folderGenerator
				.getSplitNonPreferredTerms("fake1");

		Assert.assertEquals(ThesaurusListNodeType.FOLDER, actualNode.getType());
		Assert.assertEquals("COMPLEXCONCEPTS_fake1", actualNode.getId());
		Assert.assertEquals("Concepts complexes", actualNode.getTitle());
		Assert.assertEquals(false, actualNode.isExpanded());
		Assert.assertEquals(true, actualNode.isDisplayable());
		Assert.assertEquals("icon-complex-concept", actualNode.getIconCls());
		Assert.assertNotNull(actualNode.getChildren());
	}
	
	@Test
	public final void testGetSplitNonPreferredTermsWithNoChildren() {
		
		Mockito.when(splitNonPreferredTermService.getSplitNonPreferredTermCount("fake1")).thenReturn((long)0);

		IThesaurusListNode actualNode = folderGenerator
				.getSplitNonPreferredTerms("fake1");
		
		Assert.assertNull(actualNode);
	}
}
