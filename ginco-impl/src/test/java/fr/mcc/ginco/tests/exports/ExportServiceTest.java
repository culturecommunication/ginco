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

import fr.mcc.ginco.beans.*;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.ExportServiceImpl;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.helpers.ThesaurusArrayHelper;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.tests.LoggerTestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class ExportServiceTest {

    @Mock(name = "thesaurusArrayService")
    private IThesaurusArrayService thesaurusArrayService;

    @Mock(name = "thesaurusTermService")
    private IThesaurusTermService thesaurusTermService;

    @Mock(name = "nodeLabelService")
    private INodeLabelService nodeLabelService;

    @Mock(name = "thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;
    
    @Mock(name = "thesaurusArrayHelper")
	private ThesaurusArrayHelper thesaurusArrayHelper;    

    @InjectMocks
    private ExportServiceImpl exportService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        LoggerTestUtil.initLogger(exportService);
    }

    @Test
    public void testGetHierarchicalText() throws BusinessException {
        final Thesaurus th1 = new Thesaurus();
        th1.setTitle("Test thesaurus");
        th1.setIdentifier("th1");

        Language language = new Language();
        language.setId("fra");

        final ThesaurusConcept co1 = new ThesaurusConcept();
        co1.setIdentifier("co1");
        co1.setThesaurus(th1);
        co1.setTopConcept(true);
        final ThesaurusConcept co1_1 = new ThesaurusConcept();
        co1_1.setThesaurus(th1);
        co1_1.setIdentifier("co1_1");
        final ThesaurusConcept co2 = new ThesaurusConcept();
        co2.setThesaurus(th1);
        co2.setIdentifier("co2");
        co2.setTopConcept(true);
        final ThesaurusConcept co2_1 = new ThesaurusConcept();
        co2_1.setThesaurus(th1);
        co2_1.setIdentifier("co2_1");
        final ThesaurusConcept co3 = new ThesaurusConcept();
        co3.setThesaurus(th1);
        co3.setIdentifier("co3");
        co3.setTopConcept(true);
        final ThesaurusConcept co3_1 = new ThesaurusConcept();
        co3_1.setThesaurus(th1);
        co3_1.setIdentifier("co3_1");

        final ThesaurusTerm tr1 = new ThesaurusTerm();
        tr1.setLexicalValue("co1");
        tr1.setConcept(co1);
        tr1.setIdentifier("tr1");
        tr1.setPrefered(true);
        tr1.setLanguage(language);
        Mockito.when(thesaurusConceptService.getConceptTitle(co1)).thenReturn("co1");
        Mockito.when(thesaurusConceptService.getConceptTitleLanguage(co1)).thenReturn("fra");

        final ThesaurusTerm tr1_1 = new ThesaurusTerm();
        tr1_1.setLexicalValue("co1_1");
        tr1_1.setConcept(co1_1);
        tr1_1.setIdentifier("tr1_1");
        tr1_1.setPrefered(true);
        tr1_1.setLanguage(language);
        Mockito.when(thesaurusConceptService.getConceptTitle(co1_1)).thenReturn("co1_1");
        Mockito.when(thesaurusConceptService.getConceptTitleLanguage(co1_1)).thenReturn("fra");
        
        final ThesaurusTerm tr2 = new ThesaurusTerm();
        tr2.setLexicalValue("co2");
        tr2.setConcept(co2);
        tr2.setIdentifier("tr2");
        tr2.setPrefered(true);
        tr2.setLanguage(language);
        Mockito.when(thesaurusConceptService.getConceptTitle(co2)).thenReturn("co2");
        Mockito.when(thesaurusConceptService.getConceptTitleLanguage(co2)).thenReturn("fra");
        
        final ThesaurusTerm tr2_1 = new ThesaurusTerm();
        tr2_1.setLexicalValue("co2_1");
        tr2_1.setConcept(co2_1);
        tr2_1.setIdentifier("tr2_1");
        tr2_1.setPrefered(true);
        tr2_1.setLanguage(language);
        Mockito.when(thesaurusConceptService.getConceptTitle(co2_1)).thenReturn("co2_1");
        Mockito.when(thesaurusConceptService.getConceptTitleLanguage(co2_1)).thenReturn("fra");
        
        final ThesaurusTerm tr3 = new ThesaurusTerm();
        tr3.setLexicalValue("co3");
        tr3.setConcept(co3);
        tr3.setIdentifier("tr3");
        tr3.setPrefered(true);
        tr3.setLanguage(language);
        Mockito.when(thesaurusConceptService.getConceptTitle(co3)).thenReturn("co3");
        Mockito.when(thesaurusConceptService.getConceptTitleLanguage(co3)).thenReturn("fra");
        
        final ThesaurusTerm tr3_1 = new ThesaurusTerm();
        tr3_1.setLexicalValue("co3_1");
        tr3_1.setConcept(co3_1);
        tr3_1.setIdentifier("tr3_1");
        tr3_1.setPrefered(true);
        tr3_1.setLanguage(language);
        Mockito.when(thesaurusConceptService.getConceptTitle(co3_1)).thenReturn("co3_1");
        Mockito.when(thesaurusConceptService.getConceptTitleLanguage(co3_1)).thenReturn("fra");
        
        final ThesaurusArray ar1 = new ThesaurusArray();
        ar1.setIdentifier("ar");
        ar1.setThesaurus(th1);
        ar1.setSuperOrdinateConcept(co1);
        ar1.setIdentifier("ar1");
        NodeLabel nl1 = new NodeLabel();
        nl1.setLexicalValue("ar1");
        nl1.setThesaurusArray(ar1);

        final ThesaurusArray ar2 = new ThesaurusArray();
        ar2.setThesaurus(th1);
        ar2.setIdentifier("ar2");     
        NodeLabel nl2 = new NodeLabel();
        nl2.setLexicalValue("ar2");
        nl2.setThesaurusArray(ar2);

        Mockito.when(thesaurusConceptService.getTopTermThesaurusConcepts(th1.getIdentifier())).thenReturn(new ArrayList<ThesaurusConcept>() {{
            add(co1);
            add(co2);
            add(co3);
        }});

        Mockito.when(thesaurusArrayService.getArraysWithoutParentConcept(th1.getIdentifier())).thenReturn(new ArrayList<ThesaurusArray>(){{
            add(ar2);
        }});

        Mockito.when(thesaurusArrayService.getSubOrdinatedArrays(co1.getIdentifier())).thenReturn(new ArrayList<ThesaurusArray>(){{
            add(ar1);
        }});
        
        Mockito.when(thesaurusArrayHelper.getArrayConcepts(ar1)).thenReturn(new ArrayList<ThesaurusConcept>(){{
            add(co1_1);
        }});
        
        Mockito.when(thesaurusArrayHelper.getArrayConcepts(ar2)).thenReturn(new ArrayList<ThesaurusConcept>(){{
            add(co2);
        }});

        List<ThesaurusTerm> list_tr1 = new ArrayList<ThesaurusTerm>(){{
            add(tr1);
        }};
        Mockito.when(thesaurusTermService.getTermsByConceptId(co1.getIdentifier())).thenReturn(list_tr1);
        Mockito.when(thesaurusConceptService.getConceptPreferredTerm(co1.getIdentifier())).thenReturn(tr1);

        List<ThesaurusTerm> list_tr1_1 = new ArrayList<ThesaurusTerm>(){{
            add(tr1_1);
        }};
        Mockito.when(thesaurusTermService.getTermsByConceptId(co1_1.getIdentifier())).thenReturn(list_tr1_1);
        Mockito.when(thesaurusConceptService.getConceptPreferredTerm(co1_1.getIdentifier())).thenReturn(tr1_1);

        List<ThesaurusTerm> list_tr2 = new ArrayList<ThesaurusTerm>(){{
            add(tr2);
        }};
        Mockito.when(thesaurusTermService.getTermsByConceptId(co2.getIdentifier())).thenReturn(list_tr2);
        Mockito.when(thesaurusConceptService.getConceptPreferredTerm(co2.getIdentifier())).thenReturn(tr2);

        List<ThesaurusTerm> list_tr2_1 = new ArrayList<ThesaurusTerm>(){{
            add(tr2_1);
        }};
        Mockito.when(thesaurusTermService.getTermsByConceptId(co2_1.getIdentifier())).thenReturn(list_tr2_1);
        Mockito.when(thesaurusConceptService.getConceptPreferredTerm(co2_1.getIdentifier())).thenReturn(tr2_1);

        List<ThesaurusTerm> list_tr3 = new ArrayList<ThesaurusTerm>(){{
            add(tr3);
        }};
        Mockito.when(thesaurusTermService.getTermsByConceptId(co3.getIdentifier())).thenReturn(list_tr3);
        Mockito.when(thesaurusConceptService.getConceptPreferredTerm(co3.getIdentifier())).thenReturn(tr3);

        List<ThesaurusTerm> list_tr3_1 = new ArrayList<ThesaurusTerm>(){{
            add(tr3_1);
        }};
        Mockito.when(thesaurusTermService.getTermsByConceptId(co3_1.getIdentifier())).thenReturn(list_tr3_1);
        Mockito.when(thesaurusConceptService.getConceptPreferredTerm(co3_1.getIdentifier())).thenReturn(tr3_1);

        Mockito.when(thesaurusConceptService.getChildrenByConceptId(co1.getIdentifier())).thenReturn(new ArrayList<ThesaurusConcept>(){{
            add(co1_1);
        }});

        Mockito.when(thesaurusConceptService.getChildrenByConceptId(co2.getIdentifier())).thenReturn(new ArrayList<ThesaurusConcept>(){{
            add(co2_1);
        }});

        Mockito.when(thesaurusConceptService.getChildrenByConceptId(co3.getIdentifier())).thenReturn(new ArrayList<ThesaurusConcept>(){{
            add(co3_1);
        }});       

        Mockito.when(nodeLabelService.getByThesaurusArray(ar1.getIdentifier())).thenReturn(nl1);
        Mockito.when(nodeLabelService.getByThesaurusArray(ar2.getIdentifier())).thenReturn(nl2);

        List<FormattedLine> results = exportService.getHierarchicalText(th1);
       
        Assert.assertEquals("Result should contain 8 records !", 8, results.size());

        Assert.assertEquals("First line should start by co1 with no tabulation", (long)0, (long)results.get(0).getTabs());
        Assert.assertEquals("First line should start by co1 with no tabulation", "co1", results.get(0).getText());

        Assert.assertEquals("Number of tabulations is wrong!", 1, (long)results.get(1).getTabs());
        Assert.assertEquals("Line does not correspond to needed format", "<ar1>", results.get(1).getText());

        Assert.assertEquals("Number of tabulations is wrong!", 1, (long)results.get(2).getTabs());
        Assert.assertEquals("Line does not correspond to needed format", "co1_1", results.get(2).getText());

        Assert.assertEquals("Number of tabulations is wrong!", 0, (long)results.get(3).getTabs());
        Assert.assertEquals("Line does not correspond to needed format", "co3", results.get(3).getText());

        Assert.assertEquals("Number of tabulations is wrong!", 1, (long)results.get(4).getTabs());
        Assert.assertEquals("Line does not correspond to needed format", "co3_1", results.get(4).getText());

        Assert.assertEquals("Number of tabulations is wrong!", 0, (long)results.get(5).getTabs());
        Assert.assertEquals("Line does not correspond to needed format", "<ar2>", results.get(5).getText());

        Assert.assertEquals("Number of tabulations is wrong!", 0, (long)results.get(6).getTabs());
        Assert.assertEquals("Line does not correspond to needed format", "co2", results.get(6).getText());

        Assert.assertEquals("Number of tabulations is wrong!", 1, (long)results.get(7).getTabs());
        Assert.assertEquals("Line does not correspond to needed format", "co2_1", results.get(7).getText());
    }
}
