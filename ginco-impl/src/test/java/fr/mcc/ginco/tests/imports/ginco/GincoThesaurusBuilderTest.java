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
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusConceptGroupDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.imports.ginco.GincoThesaurusBuilder;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;

public class GincoThesaurusBuilderTest {
	
	@Mock(name="thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;
	
	@Mock(name="thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;
	
	
	@Mock(name="thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;
	
	@Mock(name="thesaurusConceptGroupDAO")
	private IThesaurusConceptGroupDAO thesaurusConceptGroupDAO;
	
	@Mock(name="thesaurusArrayDAO")
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@InjectMocks
	GincoThesaurusBuilder gincoThesaurusBuilder;
	
	@Before
	public void init() {		
			MockitoAnnotations.initMocks(this);	
	}
	
	/*@Test
	public void testStoreMccExportedThesaurus() {
		Thesaurus t1 = new Thesaurus();
		t1.setIdentifier("http://t1");
		
		MCCExportedThesaurus exportedThesaurus = new MCCExportedThesaurus();
		exportedThesaurus.setThesaurus(t1);

		Mockito.when(thesaurusDAO.update(Mockito.any(Thesaurus.class))).thenReturn(t1);
		Thesaurus result = mccExportedThesaurusExtractor.storeMccExportedThesaurus(exportedThesaurus);
		
		Assert.assertEquals(result.getIdentifier(), t1.getIdentifier());
	}*/
	
	
	
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
		resultedTerms = gincoThesaurusBuilder.storeTerms(exportedThesaurus);
		
		Assert.assertEquals(resultedTerms.size(), terms.size());
		Assert.assertEquals(resultedTerms.get(0).getIdentifier(), terms.get(0).getIdentifier());
	}
	
	@Test
	public void testStoreConcepts() {
		
		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");
		
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		c1.setThesaurus(th1);

		List<ThesaurusConcept> resultedConcepts = new ArrayList<ThesaurusConcept>();
		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		concepts.add(c1);
		
		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setThesaurus(th1);
		exportedThesaurus.setConcepts(concepts);
		
		Mockito.when(thesaurusConceptDAO.update(Mockito.any(ThesaurusConcept.class))).thenReturn(c1);
		Mockito.when(thesaurusDAO.getById(Mockito.anyString())).thenReturn(th1);
		resultedConcepts = gincoThesaurusBuilder.storeConcepts(exportedThesaurus);
		
		Assert.assertEquals(resultedConcepts.size(), concepts.size());
		Assert.assertEquals(resultedConcepts.get(0).getIdentifier(), concepts.get(0).getIdentifier());
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
		
		Mockito.when(thesaurusArrayDAO.update(Mockito.any(ThesaurusArray.class))).thenReturn(a1);
		resultedArrays = gincoThesaurusBuilder.storeArrays(exportedThesaurus);
		
		Assert.assertEquals(resultedArrays.size(), resultedArrays.size());
		Assert.assertEquals(resultedArrays.get(0).getIdentifier(), resultedArrays.get(0).getIdentifier());
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
		
		Mockito.when(thesaurusConceptGroupDAO.update(Mockito.any(ThesaurusConceptGroup.class))).thenReturn(g1);
		resultedGroups = gincoThesaurusBuilder.storeGroups(exportedThesaurus);
		
		Assert.assertEquals(resultedGroups.size(), resultedGroups.size());
		Assert.assertEquals(resultedGroups.get(0).getIdentifier(), resultedGroups.get(0).getIdentifier());
	}
}
