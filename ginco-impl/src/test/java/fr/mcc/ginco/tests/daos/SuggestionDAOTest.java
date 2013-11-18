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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.mcc.ginco.beans.Suggestion;
import fr.mcc.ginco.dao.hibernate.SuggestionDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.tests.BaseDAOTest;

public class SuggestionDAOTest extends BaseDAOTest {

	private SuggestionDAO suggestionDAO = new SuggestionDAO();

	@Before
	public void handleSetUpOperation() throws Exception {
		super.handleSetUpOperation();
		suggestionDAO.setSessionFactory(getSessionFactory());
	}

	@Test
	public void testFindTermPaginatedSuggestions()
			throws BusinessException {		
		List<Suggestion> actualSuggestions = suggestionDAO.findTermPaginatedSuggestions("http://www.culturecommunication.gouv.fr/ter1", 0,10);
		Assert.assertEquals(2, actualSuggestions.size());	
		
		List<Suggestion> actualSuggestions2 = suggestionDAO.findTermPaginatedSuggestions("http://www.culturecommunication.gouv.fr/ter1", 0,1);
		Assert.assertEquals(1, actualSuggestions2.size());	
		Assert.assertEquals(1, actualSuggestions2.get(0).getIdentifier().intValue());

	}
	
	@Test
	public void testFindConceptPaginatedSuggestions()
			throws BusinessException {		
		List<Suggestion> actualSuggestions = suggestionDAO.findConceptPaginatedSuggestions("http://www.culturecommunication.gouv.fr/co1", 0,10);
		Assert.assertEquals(2, actualSuggestions.size());	
		
		List<Suggestion> actualSuggestions2 = suggestionDAO.findConceptPaginatedSuggestions("http://www.culturecommunication.gouv.fr/co1", 0,1);
		Assert.assertEquals(1, actualSuggestions2.size());	
		Assert.assertEquals(3, actualSuggestions2.get(0).getIdentifier().intValue());

	}
	
	@Test
	public void testGetConceptSuggestionCount()
			throws BusinessException {		
		Long actualSuggestionsNb = suggestionDAO.getConceptSuggestionCount("http://www.culturecommunication.gouv.fr/co1");
		Assert.assertEquals(2, actualSuggestionsNb.intValue());	

	}
	
	@Test
	public void testGetTermSuggestionCount()
			throws BusinessException {		
		Long actualSuggestionsNb = suggestionDAO.getTermSuggestionCount("http://www.culturecommunication.gouv.fr/ter1");
		Assert.assertEquals(2, actualSuggestionsNb.intValue());	

	}
	
	
	@Override
	public String getXmlDataFileInit() {
		return "/suggestion_init.xml";
	}
}
