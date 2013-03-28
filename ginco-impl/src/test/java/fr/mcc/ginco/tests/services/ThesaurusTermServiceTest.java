package fr.mcc.ginco.tests.services;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.ThesaurusTermServiceImpl;

public class ThesaurusTermServiceTest {


	@Mock(name = "thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;

	@InjectMocks	
	private ThesaurusTermServiceImpl thesaurusTermService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		
	}
	/**
	 * @return Test getting a Thesaurus Term by its Id
	 */
	@Test
    public final void testGetThesaurusTermById() throws BusinessException{
    	ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
    	fakeThesaurusTerm.setLexicalValue("lexicalValue");
		when(thesaurusTermDAO.getById(anyString())).thenReturn(fakeThesaurusTerm);
		
        String actualResponse = thesaurusTermService.getThesaurusTermById("fake-id").getLexicalValue();
        Assert.assertNotNull(actualResponse);
		Assert.assertEquals("Error while getting ThesaurusTerm By Id !", "lexicalValue", actualResponse);
    }
	
	@Test(expected=BusinessException.class)
    public final void testGetThesaurusTermByIdInvalid() throws BusinessException{
		when(thesaurusTermDAO.getById(anyString())).thenReturn(null);
         thesaurusTermService.getThesaurusTermById("fake-id").getLexicalValue();
    }
	
	/**
	 * @return Test getting a paginated list of Thesaurus Terms for a Thesaurus
	 */
	@Test
    public final void testGetPaginatedThesaurusSandoxedTermsList() {
		ThesaurusTerm thesaurusTerm1 = new ThesaurusTerm();
		ThesaurusTerm thesaurusTerm2 = new ThesaurusTerm();
		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
		terms.add(thesaurusTerm1);
		terms.add(thesaurusTerm2);

		when(thesaurusTermDAO.findPaginatedSandboxedItems(0, 10, "fake-id")).thenReturn(terms);    	
    	List<ThesaurusTerm> actualResponse = thesaurusTermService.getPaginatedThesaurusSandoxedTermsList(0, 10, "fake-id");
		Assert.assertEquals(2, actualResponse.size());
    }

    @Test(expected=BusinessException.class)
    public final void testMarkSandboxedTerms() throws BusinessException {
        List<ThesaurusTerm> old_list = thesaurusTermService.getTermsByConceptId("http://www.culturecommunication.gouv.fr/co1");
        ThesaurusTerm termToDetach = thesaurusTermService.getThesaurusTermById("http://www.culturecommunication.gouv.fr/ter2");
        List<ThesaurusTerm> new_list = new ArrayList<ThesaurusTerm>();
        new_list.add(termToDetach);
        thesaurusTermService.markTermsAsSandboxed(old_list, new_list);

        Assert.assertEquals("Term has not been detached !", 1,
                thesaurusTermService.getTermsByConceptId("http://www.culturecommunication.gouv.fr/co1").size());
        Assert.assertEquals("Wrong term detached !", "xylophon",
                thesaurusTermService.getTermsByConceptId("http://www.culturecommunication.gouv.fr/co1")
                        .get(0).getLexicalValue());
    }
    
    @Test
    public final void testGetConceptIdByTermId(){
    	/*ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
    	ThesaurusConcept fakeThesaurusConcept = new ThesaurusConcept();
    	fakeThesaurusConcept.setIdentifier("fakeId");
    	fakeThesaurusTerm.setConcept(fakeThesaurusConcept);
		when(thesaurusTermDAO.getById(anyString())).thenReturn(fakeThesaurusTerm);
		
		String conceptId = thesaurusTermService.getConceptIdByTermId("fakeTerm");
		
		Assert.assertEquals("fakeId", conceptId);*/
    }
    
}
