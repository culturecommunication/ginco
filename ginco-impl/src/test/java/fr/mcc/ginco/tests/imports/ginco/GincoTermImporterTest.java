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

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.imports.ginco.GincoTermImporter;

public class GincoTermImporterTest {

	@Mock
	private IThesaurusDAO thesaurusDAO;

	@Mock
	private IThesaurusTermDAO thesaurusTermDAO;

	@InjectMocks
	GincoTermImporter gincoTermImporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testStoreTerms() {
		
		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");
		
		ThesaurusTerm t1 = new ThesaurusTerm();
		t1.setIdentifier("http://t1");
		t1.setThesaurus(th1);

		List<ThesaurusTerm> resultedTerms = new ArrayList<ThesaurusTerm>();
		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
		terms.add(t1);
		
		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setThesaurus(th1);
		exportedThesaurus.setTerms(terms);
		
		Mockito.when(thesaurusTermDAO.update(Mockito.any(ThesaurusTerm.class))).thenReturn(t1);
		Mockito.when(thesaurusDAO.getById(Mockito.anyString())).thenReturn(th1);
		resultedTerms = gincoTermImporter.storeTerms(exportedThesaurus.getTerms(), exportedThesaurus.getThesaurus());
		
		Assert.assertEquals(resultedTerms.size(), terms.size());
		Assert.assertEquals(resultedTerms.get(0).getIdentifier(), terms.get(0).getIdentifier());
	}
}
