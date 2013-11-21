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


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.ThesaurusReportServiceImpl;
import fr.mcc.ginco.solr.SearchResultList;

public class ThesaurusReportServiceTest {	
	
	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	
	@Mock(name = "thesaurusConceptDAO")
	private IThesaurusConceptDAO conceptDAO;
	
	@Mock(name = "thesaurusTermDAO")
	private IThesaurusTermDAO termDAO;
	
	@InjectMocks
	private ThesaurusReportServiceImpl thesaurusReportService;	
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
    public final void testGetConceptsWithoutNotes() {
		String thId = "http://th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thId);
		ThesaurusConcept concept1=new ThesaurusConcept();
		concept1.setIdentifier("http://c1");
		concept1.setThesaurus(th);
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setIdentifier("http://t1");
		term1.setLexicalValue("terme1");
		List<ThesaurusConcept> listConcepts=new ArrayList<ThesaurusConcept>();
		listConcepts.add(concept1);
		Mockito.when(conceptDAO.countConceptsWoNotes(thId)).thenReturn((long) listConcepts.size());
		Mockito.when(conceptDAO.getConceptsWoNotes(thId,0,100)).thenReturn(listConcepts);
		Mockito.when(thesaurusConceptService.getConceptPreferredTerm("http://c1")).thenReturn(term1);
        SearchResultList resultList = thesaurusReportService.getConceptsWithoutNotes(thId, 0, 100);
        Assert.assertEquals(1,resultList.getNumFound());
        Assert.assertEquals("http://c1", resultList.get(0).getIdentifier());
        Assert.assertEquals("terme1", resultList.get(0).getLexicalValue());
        Assert.assertEquals(thId, resultList.get(0).getThesaurusId());
        Assert.assertEquals("ThesaurusConcept", resultList.get(0).getType());
    }
	
	@Test
    public final void testGetTermssWithoutNotes() {
		String thId = "http://th1";
		ThesaurusTerm term1=new ThesaurusTerm();
		term1.setIdentifier("http://t1");
		term1.setLexicalValue("terme1");
		List<ThesaurusTerm> listTerms=new ArrayList<ThesaurusTerm>();
		listTerms.add(term1);
		Mockito.when(termDAO.countTermsWoNotes(thId)).thenReturn((long) listTerms.size());
		Mockito.when(termDAO.getTermsWoNotes(thId,0,100)).thenReturn(listTerms);
        SearchResultList resultList = thesaurusReportService.getTermsWithoutNotes(thId, 0, 100);
        Assert.assertEquals(1,resultList.getNumFound());
        Assert.assertEquals("http://t1", resultList.get(0).getIdentifier());
        Assert.assertEquals("terme1", resultList.get(0).getLexicalValue());
        Assert.assertEquals(thId, resultList.get(0).getThesaurusId());
        Assert.assertEquals("ThesaurusTerm", resultList.get(0).getType());
    }
	
	@Test 
	public final void testGetConceptsAlignedToMyThes()
	{
		String thId = "http://th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thId);
		ThesaurusConcept concept1=new ThesaurusConcept();
		concept1.setIdentifier("http://c1");
		concept1.setThesaurus(th);
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setIdentifier("http://t1");
		term1.setLexicalValue("terme1");
		List<ThesaurusConcept> listConcepts=new ArrayList<ThesaurusConcept>();
		listConcepts.add(concept1);
		Mockito.when(conceptDAO.countConceptsAlignedToMyThes(thId)).thenReturn((long) listConcepts.size());
		Mockito.when(conceptDAO.getConceptsAlignedToMyThes(thId,0,100)).thenReturn(listConcepts);
		Mockito.when(thesaurusConceptService.getConceptPreferredTerm("http://c1")).thenReturn(term1);
        SearchResultList resultList = thesaurusReportService.getConceptsAlignedToMyThes(thId, 0, 100);
        Assert.assertEquals(1,resultList.getNumFound());
        Assert.assertEquals("http://c1", resultList.get(0).getIdentifier());
        Assert.assertEquals("terme1", resultList.get(0).getLexicalValue());
        Assert.assertEquals(thId, resultList.get(0).getThesaurusId());
        Assert.assertEquals("ThesaurusConcept", resultList.get(0).getType());
	}

}
