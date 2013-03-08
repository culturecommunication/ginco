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

import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.utils.FolderGenerator;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.tests.LoggerTestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.List;

import javax.inject.Named;

public class FolderGeneratorTest {
	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;	

	@InjectMocks
	private FolderGenerator folderGenerator = new FolderGenerator();

	@Before
	public final void setUp() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(folderGenerator);
	}

	@Test
	public final void testRootFolderCreation() throws BusinessException {
		Mockito.when(
				thesaurusConceptService
						.getOrphanThesaurusConceptsCount(Matchers.anyString()))
				.thenReturn((long) 0);
		Mockito.when(
				thesaurusConceptService
						.getTopTermThesaurusConceptsCount(Matchers.anyString()))
				.thenReturn((long) 0);
		List<IThesaurusListNode> nodes = folderGenerator
				.generateFolders("fake");

		Assert.assertEquals("Invalid number of nodes", 5, nodes.size());

		// tests of the top concept node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(0)
				.getType());
		Assert.assertEquals("CONCEPTS_fake", nodes.get(0).getId());
		Assert.assertEquals("Arborescence des concepts", nodes.get(0)
				.getTitle());
		Assert.assertEquals(false, nodes.get(0).isExpanded());
		Assert.assertNotNull(nodes.get(0).getChildren());

		// tests of the sandbox node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(1)
				.getType());
		Assert.assertEquals("SANDBOX_fake", nodes.get(1).getId());
		Assert.assertEquals("Bac à sable", nodes.get(1).getTitle());
		Assert.assertEquals(false, nodes.get(1).isExpanded());
		Assert.assertNotNull(nodes.get(1).getChildren());

		// tests of the orphan concepts node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(2)
				.getType());
		Assert.assertEquals("ORPHANS_fake", nodes.get(2).getId());
		Assert.assertEquals("Concepts orphelins", nodes.get(2).getTitle());
		Assert.assertEquals(false, nodes.get(2).isExpanded());
		Assert.assertNotNull(nodes.get(2).getChildren());

		// tests of the group node
		Assert.assertEquals(ThesaurusListNodeType.FOLDER, nodes.get(3)
				.getType());
		Assert.assertEquals("GROUPS_fake", nodes.get(3).getId());
		Assert.assertEquals("Groupes", nodes.get(3).getTitle());
		Assert.assertEquals(false, nodes.get(3).isExpanded());
		Assert.assertNotNull(nodes.get(3).getChildren());

	}
	@Test
	public final void testRootFolderCreationWithOrphansAndTopConcepts() throws BusinessException {
		Mockito.when(
				thesaurusConceptService
						.getOrphanThesaurusConceptsCount(Matchers.anyString()))
				.thenReturn((long) 3);
		Mockito.when(
				thesaurusConceptService
						.getTopTermThesaurusConceptsCount(Matchers.anyString()))
				.thenReturn((long) 3);
		List<IThesaurusListNode> nodes = folderGenerator
				.generateFolders("fake");
		
		Assert.assertNull(nodes.get(0).getChildren());
		Assert.assertNull(nodes.get(2).getChildren());
	}

}
