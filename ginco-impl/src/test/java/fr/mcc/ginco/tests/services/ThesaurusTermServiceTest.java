package fr.mcc.ginco.tests.services;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.ThesaurusTermServiceImpl;

public class ThesaurusTermServiceTest {


	@Mock(name = "thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;
	
	@Mock(name = "thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;
	
	@Mock(name = "generatorService")
    private IIDGeneratorService customGeneratorService;
	
	@Mock(name = "languageDAO")
    private ILanguageDAO languageDAO;

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
   
    @Test
    public final void testGetConceptIdByTerm(){
    	
    	ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
    	ThesaurusConcept fakeThesaurusConcept = new ThesaurusConcept();
    	fakeThesaurusConcept.setIdentifier("fakeId");
    	fakeThesaurusTerm.setConcept(fakeThesaurusConcept);
		when(thesaurusTermDAO.getTermByLexicalValueThesaurusIdLanguageId(anyString(), anyString(), anyString())).thenReturn(fakeThesaurusTerm);
		
		String conceptId = thesaurusTermService.getConceptIdByTerm("fakeLexicalValue", "fakeThesaurusId", "fakeLanguageId");
		
		Assert.assertEquals("fakeId", conceptId);
    }
    
    @Test(expected=BusinessException.class)
    public final void testGetConceptIdByInvalidTerm(){
    	when(thesaurusTermDAO.getTermByLexicalValueThesaurusIdLanguageId(anyString(), anyString(), anyString())).thenReturn(null);
    	thesaurusTermService.getConceptIdByTerm("fakeLexicalValue", "fakeThesaurusId", "fakeLanguageId");
    }
    
    @Test(expected=BusinessException.class)
    public final void testGetConceptIdByTermWithInvalidConcept(){
    	ThesaurusTerm fakeThesaurusTerm = new ThesaurusTerm();
    	when(fakeThesaurusTerm.getConcept()).thenReturn(null);
    	thesaurusTermService.getConceptIdByTerm("fakeLexicalValue", "fakeThesaurusId", "fakeLanguageId");
    }
    
    @Test
    public final void testimportSandBoxTerms() {
    	
    	String thesID = "fakeId";
    	Thesaurus thes = new Thesaurus();
    	thes.setIdentifier(thesID);
    	when(thesaurusDAO.getById(thesID)).thenReturn(thes);
    	when(customGeneratorService.generate(ThesaurusTerm.class)).thenReturn("fakeId");
    	when(thesaurusTermDAO.update((ThesaurusTerm) Matchers.anyObject())).then(AdditionalAnswers.returnsFirstArg());
    	Language lang1 = new Language();
    	Language lang2 = new Language();

		Map<String, Language> termsToImport = new HashMap<String, Language>();
		termsToImport.put("term1",lang1 );
		termsToImport.put("Terme accentué2",lang1 );
		termsToImport.put("Terme <b>XSS</b>",lang2);

    	List<ThesaurusTerm> terms = thesaurusTermService.importSandBoxTerms(termsToImport, thesID, TermStatusEnum.VALIDATED.getStatus());
    	Assert.assertEquals(terms.size(),3);
    	ThesaurusTerm expectedTerm2 =  new ThesaurusTerm();
    	expectedTerm2.setLexicalValue("Terme accentué2");
    	expectedTerm2.setLanguage(lang1);

    	//ListAssert.assertContains(terms, expectedTerm2);
    	//Assert.assertEquals(lang1, terms.get(1).getLanguage());
    	//Assert.assertEquals("Terme &lt;b&gt;XSS&lt;/b&gt;", terms.get(2).getLexicalValue());
    }
}
