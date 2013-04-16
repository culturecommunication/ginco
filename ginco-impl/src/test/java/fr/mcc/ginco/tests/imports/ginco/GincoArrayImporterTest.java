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

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.imports.ginco.GincoArrayImporter;

public class GincoArrayImporterTest {

	@Mock(name = "thesaurusArrayDAO")
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@InjectMocks
	GincoArrayImporter gincoArrayImporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testStoreArrays() {
		ThesaurusArray a1 = new ThesaurusArray();
		a1.setIdentifier("http://a1");

		ThesaurusArray a2 = new ThesaurusArray();
		a2.setIdentifier("http://a2");

		List<ThesaurusArray> resultedArrays = new ArrayList<ThesaurusArray>();
		List<ThesaurusArray> arrays = new ArrayList<ThesaurusArray>();
		arrays.add(a1);
		arrays.add(a2);

		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setConceptArrays(arrays);

		Mockito.when(
				thesaurusArrayDAO.update(Mockito.any(ThesaurusArray.class)))
				.thenReturn(a1);
		resultedArrays = gincoArrayImporter.storeArrays(exportedThesaurus);

		Assert.assertEquals(resultedArrays.size(), resultedArrays.size());
		Assert.assertEquals(resultedArrays.get(0).getIdentifier(),
				resultedArrays.get(0).getIdentifier());
	}

}
