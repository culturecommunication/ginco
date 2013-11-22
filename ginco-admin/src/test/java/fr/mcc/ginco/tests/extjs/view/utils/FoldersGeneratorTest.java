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

import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.extjs.view.utils.FolderGenerator;
import fr.mcc.ginco.extjs.view.utils.FoldersGenerator;

public class FoldersGeneratorTest {

	@Mock(name = "folderGenerator")
	private FolderGenerator folderGenerator;

	@InjectMocks
	private FoldersGenerator foldersGenerator = new FoldersGenerator();

	@Before
	public final void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public final void testConceptFolderOnly() throws BusinessException {

		IThesaurusListNode concepts = new ThesaurusListBasicNode();
		concepts.setTitle("Arborescence des concepts");
		concepts.setId("CONCEPTS_fake");
		concepts.setType(ThesaurusListNodeType.FOLDER);
		concepts.setIconCls("icon-tree");
		concepts.setExpanded(false);
		concepts.setDisplayable(false);

		Mockito.when(folderGenerator.getConcepts(Matchers.anyString()))
				.thenReturn(concepts);

		List<IThesaurusListNode> nodes = foldersGenerator
				.generateFolders("fake");

		Assert.assertEquals("Invalid number of nodes", 1, nodes.size());

	}

	@Test
	public final void testComplexConceptsFolder() throws BusinessException {
		IThesaurusListNode concepts = new ThesaurusListBasicNode();
		concepts.setTitle("Arborescence des concepts");
		concepts.setId("CONCEPTS_fake");
		concepts.setType(ThesaurusListNodeType.FOLDER);
		concepts.setIconCls("icon-tree");
		concepts.setExpanded(false);
		concepts.setDisplayable(false);

		Mockito.when(folderGenerator.getConcepts(Matchers.anyString()))
				.thenReturn(concepts);

		IThesaurusListNode complexConceptNode = new ThesaurusListBasicNode();
		complexConceptNode.setTitle("Concepts complexes");
		complexConceptNode.setId("COMPLEXCONCEPTS_fake");
		complexConceptNode.setType(ThesaurusListNodeType.FOLDER);
		complexConceptNode.setIconCls("icon-complex-concept");
		complexConceptNode.setExpanded(false);
		complexConceptNode.setDisplayable(true);

		Mockito.when(folderGenerator.getSplitNonPreferredTerms("fake"))
				.thenReturn(complexConceptNode);

		List<IThesaurusListNode> nodes = foldersGenerator
				.generateFolders("fake");

		Assert.assertEquals("Invalid number of nodes", 2, nodes.size());

		// tests of the top concepts node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(0)
				.getType());
		Assert.assertEquals("CONCEPTS_fake", nodes.get(0).getId());
		Assert.assertEquals("Arborescence des concepts", nodes.get(0)
				.getTitle());
		Assert.assertEquals(false, nodes.get(0).isExpanded());
		Assert.assertNull(nodes.get(0).getChildren());

		// tests of the complex concepts node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(1)
				.getType());
		Assert.assertEquals("COMPLEXCONCEPTS_fake", nodes.get(1).getId());
		Assert.assertEquals("Concepts complexes", nodes.get(1).getTitle());
		Assert.assertEquals(false, nodes.get(1).isExpanded());
		Assert.assertNull(nodes.get(1).getChildren());
	}

	@Test
	public final void testGroupsFolder() throws BusinessException {

		IThesaurusListNode concepts = new ThesaurusListBasicNode();

		Mockito.when(folderGenerator.getConcepts(Matchers.anyString()))
				.thenReturn(concepts);

		IThesaurusListNode groups = new ThesaurusListBasicNode();
		groups.setTitle("Groupes");
		groups.setId("GROUPS_fake");
		groups.setType(ThesaurusListNodeType.FOLDER);
		groups.setExpanded(false);
		groups.setIconCls("icon-group");
		groups.setChildren(new ArrayList<IThesaurusListNode>());
		groups.setDisplayable(false);

		Mockito.when(folderGenerator.getGroups("fake")).thenReturn(groups);

		List<IThesaurusListNode> nodes = foldersGenerator
				.generateFolders("fake");

		Assert.assertEquals("Invalid number of nodes", 2, nodes.size());

		// tests of the groups node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(1)
				.getType());
		Assert.assertEquals("GROUPS_fake", nodes.get(1).getId());
		Assert.assertEquals("Groupes", nodes.get(1).getTitle());
		Assert.assertEquals(false, nodes.get(1).isExpanded());
		Assert.assertNotNull(nodes.get(1).getChildren());

	}

	@Test
	public final void testArraysFolder() throws BusinessException {

		IThesaurusListNode concepts = new ThesaurusListBasicNode();

		Mockito.when(folderGenerator.getConcepts(Matchers.anyString()))
				.thenReturn(concepts);

		IThesaurusListNode arrays = new ThesaurusListBasicNode();
		arrays.setTitle("Tableaux");
		arrays.setId("ARRAYS_fake");
		arrays.setType(ThesaurusListNodeType.FOLDER);
		arrays.setExpanded(false);
		arrays.setIconCls("icon-table");
		arrays.setDisplayable(false);

		Mockito.when(folderGenerator.getArrays("fake")).thenReturn(arrays);

		List<IThesaurusListNode> nodes = foldersGenerator
				.generateFolders("fake");

		Assert.assertEquals("Invalid number of nodes", 2, nodes.size());

		// tests of the concept arrays node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(1)
				.getType());
		Assert.assertEquals("ARRAYS_fake", nodes.get(1).getId());
		Assert.assertEquals("Tableaux", nodes.get(1).getTitle());
		Assert.assertEquals(false, nodes.get(1).isExpanded());
		Assert.assertNull(nodes.get(1).getChildren());

	}

	@Test
	public final void testOrphansFolder() throws BusinessException {

		IThesaurusListNode concepts = new ThesaurusListBasicNode();

		Mockito.when(folderGenerator.getConcepts(Matchers.anyString()))
				.thenReturn(concepts);

		IThesaurusListNode orphans = new ThesaurusListBasicNode();
		orphans.setTitle("Concepts orphelins");
		orphans.setId("ORPHANS_fake");
		orphans.setType(ThesaurusListNodeType.FOLDER);
		orphans.setIconCls("sandbox");
		orphans.setExpanded(false);
		orphans.setDisplayable(false);

		Mockito.when(folderGenerator.getOrphans("fake")).thenReturn(orphans);

		List<IThesaurusListNode> nodes = foldersGenerator
				.generateFolders("fake");

		Assert.assertEquals("Invalid number of nodes", 2, nodes.size());

		// tests of the orphan concepts node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(1)
				.getType());
		Assert.assertEquals("ORPHANS_fake", nodes.get(1).getId());
		Assert.assertEquals("Concepts orphelins", nodes.get(1).getTitle());
		Assert.assertEquals(false, nodes.get(1).isExpanded());

	}

	@Test
	public final void testSandboxFolder() throws BusinessException {

		IThesaurusListNode concepts = new ThesaurusListBasicNode();

		Mockito.when(folderGenerator.getConcepts(Matchers.anyString()))
				.thenReturn(concepts);

		IThesaurusListNode sandbox = new ThesaurusListBasicNode();
		sandbox.setTitle("Termes orphelins");
		sandbox.setId("SANDBOX_fake");
		sandbox.setType(ThesaurusListNodeType.FOLDER);
		sandbox.setIconCls("sandbox");
		sandbox.setExpanded(false);
		sandbox.setDisplayable(true);

		Mockito.when(folderGenerator.getSandbox("fake")).thenReturn(sandbox);

		List<IThesaurusListNode> nodes = foldersGenerator
				.generateFolders("fake");

		Assert.assertEquals("Invalid number of nodes", 2, nodes.size());

		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(1)
				.getType());
		Assert.assertEquals("SANDBOX_fake", nodes.get(1).getId());
		Assert.assertEquals("Termes orphelins", nodes.get(1).getTitle());
		Assert.assertEquals(false, nodes.get(1).isExpanded());

	}

}
