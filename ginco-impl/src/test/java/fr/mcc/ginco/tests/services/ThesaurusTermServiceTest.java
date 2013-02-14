package fr.mcc.ginco.tests.services;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.IThesaurusTermService;
import fr.mcc.ginco.tests.BaseServiceTest;

@TransactionConfiguration
@Transactional
public class ThesaurusTermServiceTest extends BaseServiceTest {
	
	@Inject
	private IThesaurusTermService thesaurusTermService;
	
	/**
	 * @return Test getting a Thesaurus Term by its Id
	 */
	@Test
    public final void testGetThesaurusTermById() {
    	String idThesaurusTerm = "0";
    	String expectedResponse = "lexical value";
        String actualResponse = thesaurusTermService.getThesaurusTermById(idThesaurusTerm).getLexicalValue();
		Assert.assertEquals("Error while getting ThesaurusTerm By Id !", expectedResponse, actualResponse);
    }
	
	/**
	 * @return Test getting a paginated list of Thesaurus Terms for a Thesaurus
	 */
	@Test
    public final void testGetPaginatedThesaurusTermsList() {
    	Integer expectedResponse = 2;
    	Integer startIndex = 0;
    	Integer limit= 2;
    	String idThesaurus = "0";
    	Integer actualResponse = thesaurusTermService.getPaginatedThesaurusList(startIndex, limit, idThesaurus).size();
    	
    	// compare size and first/last elements
        //String actualResponse = thesaurusTermService.getThesaurusTermById(idThesaurusTerm).getLexicalValue();
		//Assert.assertEquals("Error while getting ThesaurusTerm By Id !", expectedResponse, actualResponse);
    }

	@Override
	public String getXmlDataFileInit() {
		return "/thesaurus_init.xml";
	}
}
