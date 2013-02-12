package fr.mcc.ginco.tests.rest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import fr.mcc.ginco.ILanguagesService;
import fr.mcc.ginco.IThesaurusFormatService;
import fr.mcc.ginco.IThesaurusOrganizationService;
import fr.mcc.ginco.IThesaurusService;
import fr.mcc.ginco.IThesaurusTypeService;
import fr.mcc.ginco.ThesaurusOrganizationServiceImpl;
import fr.mcc.ginco.ThesaurusServiceImpl;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.rest.services.ThesaurusRestService;


public class ThesaurusRestServiceTest {
	
	private ThesaurusRestService thesaurusRestService = new ThesaurusRestService();
	
	/**
	 * Test the putVocabularyById method with a mock vocabulary
	 */
	//TODO PUT ANNOTATION @Test when ready to test
	public final void testPutVocabularyByIdWithNonExistantOrganization() {
		//Generating a mocked ThesaurusView 
		ThesaurusView mockedThesaurusView1 = getMockedThesaurusViewWithAllFieldsFilledIn("mock1");
		
		//Mocking all services needed by ThesaurusRestService
		thesaurusRestService.setThesaurusService(getMockedIThesaurusService());
		thesaurusRestService.setThesaurusOrganizationService(getMockedIOrganizationThesaurusService());
		thesaurusRestService.setLanguagesService(getMockedILanguagesService());
		thesaurusRestService.setThesaurusFormatService(getMockedIThesaurusFormatService());
		thesaurusRestService.setThesaurusTypeService(getMockedIThesaurusTypeService());
		
		//Testing putVocabularyById method
		thesaurusRestService.putVocabularyById("mock1", mockedThesaurusView1);
		
		//DBUnit verification
		//TODO IMPLEMENTATION
	}

	private ThesaurusView getMockedThesaurusViewWithNonMandatoryEmptyFields(String id) {
		ThesaurusView mockedThesaurusView = Mockito.mock(ThesaurusView.class);
		
		//Mandatory fields
		Mockito.when(mockedThesaurusView.getId()).thenReturn(id);
		Mockito.when(mockedThesaurusView.getTitle()).thenReturn("Title");
		Mockito.when(mockedThesaurusView.getRights()).thenReturn("Fake Rights");
		
		//Optional fields (return null)
		Mockito.when(mockedThesaurusView.getContributor()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getCoverage()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getDate()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getDescription()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getPublisher()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getRelation()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getSource()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getSubject()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getCreated()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getFormat()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getType()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getCreatorName()).thenReturn(null);
		Mockito.when(mockedThesaurusView.getCreatorHomepage()).thenReturn(null);
		
		List<String> mockedLanguages = new ArrayList<String>();
		Mockito.when(mockedThesaurusView.getLanguages()).thenReturn(mockedLanguages);
		
		return mockedThesaurusView;
	}
	
	private ThesaurusView getMockedThesaurusViewWithAllFieldsFilledIn(String id) {
		
		ThesaurusView mockedThesaurusView = Mockito.mock(ThesaurusView.class);
		
		//Mandatory fields
		mockedThesaurusView = getMockedThesaurusViewWithNonMandatoryEmptyFields(id);
		
		//Optional fields filled in
		Mockito.when(mockedThesaurusView.getContributor()).thenReturn("Fake Contributor");
		Mockito.when(mockedThesaurusView.getCoverage()).thenReturn("Fake Coverage");
		Mockito.when(mockedThesaurusView.getDate()).thenReturn("2013-02-15 02:02:02");
		Mockito.when(mockedThesaurusView.getDescription()).thenReturn("Fake Description");
		Mockito.when(mockedThesaurusView.getPublisher()).thenReturn("Fake Publisher");
		Mockito.when(mockedThesaurusView.getRelation()).thenReturn("Fake Relation");
		Mockito.when(mockedThesaurusView.getSource()).thenReturn("Fake Source");
		Mockito.when(mockedThesaurusView.getSubject()).thenReturn("Fake Subject");
		Mockito.when(mockedThesaurusView.getCreated()).thenReturn("2013-02-15 02:02:02");
		Mockito.when(mockedThesaurusView.getFormat()).thenReturn(1);
		Mockito.when(mockedThesaurusView.getType()).thenReturn(1);
		Mockito.when(mockedThesaurusView.getCreatorName()).thenReturn("Creator");
		Mockito.when(mockedThesaurusView.getCreatorHomepage()).thenReturn("http://fakeurl.com");
		
		List<String> mockedLanguages = new ArrayList<String>();
		String mockedLanguage1 = "fra";
		String mockedLanguage2 = "rus";
		mockedLanguages.add(mockedLanguage1);
		mockedLanguages.add(mockedLanguage2);
		Mockito.when(mockedThesaurusView.getLanguages()).thenReturn(mockedLanguages);
		
		return mockedThesaurusView;
	}
	
	private IThesaurusService getMockedIThesaurusService() {
		IThesaurusService mockedService = Mockito
				.mock(ThesaurusServiceImpl.class);
		return mockedService;
	}
	
	private IThesaurusOrganizationService getMockedIOrganizationThesaurusService() {
		ThesaurusOrganizationServiceImpl mockedService = Mockito
				.mock(ThesaurusOrganizationServiceImpl.class);
		return mockedService;
	}
	
	private ILanguagesService getMockedILanguagesService() {
		ILanguagesService mockedService = Mockito
				.mock(ILanguagesService.class);
		return mockedService;
	}
	
	private IThesaurusFormatService getMockedIThesaurusFormatService() {
		IThesaurusFormatService mockedService = Mockito
				.mock(IThesaurusFormatService.class);
		return mockedService;
	}
	
	private IThesaurusTypeService getMockedIThesaurusTypeService() {
		IThesaurusTypeService mockedService = Mockito
				.mock(IThesaurusTypeService.class);
		return mockedService;
	}
	
}