package fr.mcc.ginco.soap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.IThesaurusConceptService;


/**
 * Unit tests class for the ThesaurusConceptService
 * 
 */
public class SOAPThesaurusConceptServiceTest {

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@InjectMocks
	private SOAPThesaurusConceptServiceImpl soapThesaurusConceptService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);	
	}
	
	/**
	 * Test the getConceptsHierarchicalRelations method with empty parameters
	 */
	
	@Test(expected=BusinessException.class)
	public final void testGetConceptsHierarchicalRelationsWithEmptyParameters(){
		soapThesaurusConceptService.getConceptsHierarchicalRelations("", "");
	}
}
