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
package fr.mcc.ginco.tests.daos;

import org.dbunit.dataset.IDataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.dao.IGenericDAO.SortingTypes;
import fr.mcc.ginco.dao.hibernate.ThesaurusDAO;
import fr.mcc.ginco.tests.BaseDAOTest;


public class ThesaurusDAOTest extends BaseDAOTest {
	
    private ThesaurusDAO thesaurusDAO; ;     
    @Before
	public void handleSetUpOperation() throws Exception {
		super.handleSetUpOperation();
		thesaurusDAO = new ThesaurusDAO();		
		thesaurusDAO.setSessionFactory(getSessionFactory());
	}
    @Test
    public final void testGetThesaurusById() {
    	String idThesaurus = "0";
    	String expectedResponse = "test";
    	thesaurusDAO.getById(idThesaurus);
        String actualResponse = thesaurusDAO.getById(idThesaurus).getTitle();
		Assert.assertEquals("Error while getting Thesaurus By Id !", expectedResponse, actualResponse);
    }

    @Test
    public final void testGetThesaurusType() {
        String idThesaurus = "0";
        String expectedThesaurusTypeTitle = "thesaurus type 2";
        String actualThesaurusTypeTitle =thesaurusDAO.getById(idThesaurus).getType().getLabel();
        Assert.assertEquals("Error while getting ThesaurusType!", expectedThesaurusTypeTitle, actualThesaurusTypeTitle);
    }

    @Test
    public final void testGetThesaurusFormat() {
        String idThesaurus = "0";
        String expectedThesaurusFormatTitle = "PDF 1.7";
        String actualThesaurusFormatTitle = thesaurusDAO.getById(idThesaurus).getFormat().getLabel();
        Assert.assertEquals("Error while getting ThesaurusFormat!", expectedThesaurusFormatTitle, actualThesaurusFormatTitle);
    }
    
    @Test
    public final void testGetThesaurusList() {
        Integer expectedThesaurusListSize = 3;
        Integer actualThesaurusListSize = thesaurusDAO.findAll().size();
        Assert.assertEquals("Wrong number of elements when getting Thesaurus List!", expectedThesaurusListSize, actualThesaurusListSize);
    }
    
    @Test
    public final void testGetAscThesaurusList() {
        String expectedThesaurusName = "essai";
        String actualThesaurusName = thesaurusDAO.findAll("title", SortingTypes.asc).get(0).getTitle();
        Assert.assertEquals("Wrong ascendant sorting when getting Thesaurus List!", expectedThesaurusName, actualThesaurusName);
    }
    
    @Test
    public final void testUpdateThesaurus() throws Exception{
        Thesaurus th = new Thesaurus();
        th.setIdentifier("http://www.culturecommunication.gouv.fr/thesaurus2");
        th.setTitle("title");     
        Thesaurus updatedThesaurus = thesaurusDAO.makePersistent(th);
        Assert.assertTrue("Error while getting updated thesaurus", updatedThesaurus!=null);
        
        //Test the log_journal addition
     	IDataSet databaseDataSet = getDataset(getXmlDataFileInit());
     	int databaseTableSize = databaseDataSet.getTable("log_journal").getRowCount();
     	//Assert.assertEquals(1, databaseTableSize);
        
    }
    
    @Test
    public final void testCreateNewThesaurusWithEmptyCreator() {
        Thesaurus newThesaurus = new Thesaurus();
        newThesaurus.setTitle("test");
        newThesaurus.setCreator(null);
        Thesaurus updatedThesaurus = thesaurusDAO.update(newThesaurus);
        Assert.assertTrue("Error while getting updated thesaurus", updatedThesaurus != null);
    }   
    
    @Test
    public final void testCreateNewThesaurusWithCreator() {
        Thesaurus newThesaurus = new Thesaurus();
        ThesaurusOrganization thOrg = new ThesaurusOrganization();
        thOrg.setName("Un auteur");
        newThesaurus.setTitle("test");
        newThesaurus.setCreator(thOrg);       
        Thesaurus updatedThesaurus = thesaurusDAO.update(newThesaurus);
        Assert.assertEquals("Un auteur", updatedThesaurus.getCreator().getName());
    }   
    
    @Test
    public final void testUpdateThesaurusWithEmptyCreator() throws Exception{
        Integer expectedThesaurusListSize = 3;
        Thesaurus th = new Thesaurus();
        th.setIdentifier("http://www.culturecommunication.gouv.fr/thesaurus2");
        th.setTitle("title");
        th.setCreator(null);
        Thesaurus updatedThesaurus = thesaurusDAO.update(th);
       // Assert.assertEquals("Error while getting Thesaurus List!", expectedThesaurusListSize, actualThesaurusListSize);
        Assert.assertTrue("Error while getting updated thesaurus", updatedThesaurus != null);
        //Test the log_journal addition
     	IDataSet databaseDataSet = getDataset(getXmlDataFileInit());
     	int databaseTableSize = databaseDataSet.getTable("log_journal").getRowCount();
     	//Assert.assertEquals(1, databaseTableSize);
        
    }
    @Test
    public final void testUpdateThesaurusWithCreator() throws Exception{
        Thesaurus th = new Thesaurus();
        th.setIdentifier("http://www.culturecommunication.gouv.fr/thesaurus2");
        th.setTitle("title");
        ThesaurusOrganization thOrg = new ThesaurusOrganization();
        thOrg.setName("Un auteur");
        th.setCreator(thOrg);
        Thesaurus updatedThesaurus = thesaurusDAO.update(th);

        Assert.assertTrue("Error while getting updated thesaurus", updatedThesaurus!=null);
        Assert.assertEquals("Un auteur", updatedThesaurus.getCreator().getName());
        //Test the log_journal addition
     	IDataSet databaseDataSet = getDataset(getXmlDataFileInit());
     	int databaseTableSize = databaseDataSet.getTable("log_journal").getRowCount();
     	//Assert.assertEquals(1, databaseTableSize);
        
    }   
	@Override
	public String  getXmlDataFileInit() {
		return "/thesaurus_init.xml";		
	}
}
