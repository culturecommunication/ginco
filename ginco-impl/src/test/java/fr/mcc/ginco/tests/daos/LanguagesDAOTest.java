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

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.dao.hibernate.LanguageDAO;
import fr.mcc.ginco.tests.BaseDAOTest;

public class LanguagesDAOTest extends BaseDAOTest {
	
    private LanguageDAO languageDAO = new LanguageDAO() ; 
    
    @Before
	public void handleSetUpOperation() throws Exception {
		super.handleSetUpOperation();
		languageDAO.setSessionFactory(getSessionFactory());
	}   
    
    @Test
	public void testFindPaginatedItems() {
    	List<Language> languages = languageDAO.findPaginatedItems(0, 1);
    	Assert.assertNotNull(languages);
    	Assert.assertEquals( "en-US", languages.get(0).getId());
	}


	@Test
	public void testFindTopLanguages() {
    	List<Language> languages= languageDAO.findTopLanguages();
    	Assert.assertEquals(3, languages.size());
	}
	

	@Test
	public void  testGetByPart1() {
		Language lang = languageDAO.getByPart1("fr");
    	Assert.assertEquals("fr-FR", lang.getId());
	}
    
    
	@Override
	public String  getXmlDataFileInit() {
		return "/languages_init.xml";		
	}
}
