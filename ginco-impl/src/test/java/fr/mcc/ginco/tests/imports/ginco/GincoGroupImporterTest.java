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
package fr.mcc.ginco.tests.imports.ginco;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.dao.IThesaurusConceptGroupDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.imports.ginco.GincoGroupImporter;

public class GincoGroupImporterTest {

	@Mock(name = "thesaurusConceptGroupDAO")
	private IThesaurusConceptGroupDAO thesaurusConceptGroupDAO;

	@InjectMocks
	GincoGroupImporter gincoGroupImporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testStoreGroups() {
		ThesaurusConceptGroup g1 = new ThesaurusConceptGroup();
		g1.setIdentifier("http://g1");

		ThesaurusConceptGroup g2 = new ThesaurusConceptGroup();
		g2.setIdentifier("http://g2");

		List<ThesaurusConceptGroup> resultedGroups = new ArrayList<ThesaurusConceptGroup>();
		List<ThesaurusConceptGroup> groups = new ArrayList<ThesaurusConceptGroup>();
		groups.add(g1);
		groups.add(g2);

		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setConceptGroups(groups);

		Mockito.when(
				thesaurusConceptGroupDAO.update(Mockito
						.any(ThesaurusConceptGroup.class))).thenReturn(g1);
		resultedGroups = gincoGroupImporter.storeGroups(exportedThesaurus);

		Assert.assertEquals(resultedGroups.size(), resultedGroups.size());
		Assert.assertEquals(resultedGroups.get(0).getIdentifier(),
				resultedGroups.get(0).getIdentifier());
	}

}
