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
package fr.mcc.ginco.tests.exports;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.exports.NodeLabelComparator;
import fr.mcc.ginco.services.INodeLabelService;

public class NodeLabelComparatorTest {

	@Mock(name = "nodeLabelService")
	private INodeLabelService nodeLabelService;

	@InjectMocks
	private NodeLabelComparator nodeLabelComparator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(nodeLabelComparator, "defaultLang",
				"fr-FR");
	}

	@Test
	public void testCompareStandard() {
		NodeLabel nodeLabel1 = new NodeLabel();
		nodeLabel1.setLexicalValue("zeste");

		NodeLabel nodeLabel3 = new NodeLabel();
		nodeLabel3.setLexicalValue("apiculture");

		ThesaurusArray array1 = new ThesaurusArray();
		array1.setIdentifier("array1");

		ThesaurusArray array3 = new ThesaurusArray();
		array3.setIdentifier("array3");

		Mockito.when(nodeLabelService.getByThesaurusArray("array1"))
				.thenReturn(nodeLabel1);
		Mockito.when(nodeLabelService.getByThesaurusArray("array3"))
				.thenReturn(nodeLabel3);

		int compareNoAccent = nodeLabelComparator.compare(array1, array3);
		Assert.assertEquals(1, compareNoAccent);

	}

	@Test
	public void testCompareWithAccents() {
		NodeLabel nodeLabel1 = new NodeLabel();
		nodeLabel1.setLexicalValue("zeste");

		NodeLabel nodeLabel2 = new NodeLabel();
		nodeLabel2.setLexicalValue("étrange");

		ThesaurusArray array1 = new ThesaurusArray();
		array1.setIdentifier("array1");

		ThesaurusArray array2 = new ThesaurusArray();
		array2.setIdentifier("array2");

		Mockito.when(nodeLabelService.getByThesaurusArray("array1"))
				.thenReturn(nodeLabel1);
		Mockito.when(nodeLabelService.getByThesaurusArray("array2"))
				.thenReturn(nodeLabel2);

		int compareWithAccents = nodeLabelComparator.compare(array1, array2);
		Assert.assertEquals(1, compareWithAccents);

	}
}
