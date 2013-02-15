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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.ThesaurusTermServiceImpl;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.tests.BaseTest;

@TransactionConfiguration
@Transactional
public class ThesaurusTermServiceTest extends BaseTest {
	

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
    public final void testGetPaginatedThesaurusTermsList() {
		ThesaurusTerm thesaurusTerm1 = new ThesaurusTerm();
		ThesaurusTerm thesaurusTerm2 = new ThesaurusTerm();
		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
		terms.add(thesaurusTerm1);
		terms.add(thesaurusTerm2);

		when(thesaurusTermDAO.findPaginatedItems(0, 10, "fake-id")).thenReturn(terms);    	
    	List<ThesaurusTerm> actualResponse = thesaurusTermService.getPaginatedThesaurusList(0, 10, "fake-id");
		Assert.assertEquals(2, actualResponse.size());
    }	
}
