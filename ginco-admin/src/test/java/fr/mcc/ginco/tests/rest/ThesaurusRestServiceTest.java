package fr.mcc.ginco.tests.rest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import fr.mcc.ginco.ILanguagesService;
import fr.mcc.ginco.IThesaurusFormatService;
import fr.mcc.ginco.IThesaurusService;
import fr.mcc.ginco.IThesaurusTypeService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.users.IUser;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.rest.services.ThesaurusRestService;
import fr.mcc.ginco.utils.DateUtil;

public class ThesaurusRestServiceTest {

	@Mock(name = "thesaurusService")
	private IThesaurusService thesaurusService;

	@Mock(name = "languagesService")
	private ILanguagesService languagesService;

	@Mock(name = "thesaurusFormatService")
	private IThesaurusFormatService thesaurusFormatService;

	@Mock(name = "thesaurusTypeService")
	private IThesaurusTypeService thesaurusTypeService;

	@InjectMocks
	private ThesaurusRestService thesaurusRestService = new ThesaurusRestService();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ReflectionUtils.doWithFields(thesaurusRestService.getClass(),
				new ReflectionUtils.FieldCallback() {

					public void doWith(Field field)
							throws IllegalArgumentException,
							IllegalAccessException {
						ReflectionUtils.makeAccessible(field);

						if (field.getAnnotation(Log.class) != null) {
							Logger logger = LoggerFactory
									.getLogger(thesaurusRestService.getClass());
							field.set(thesaurusRestService, logger);
						}
					}
				});

	}

	/**
	 * Test the putVocabularyById method with a mock vocabulary
	 * 
	 */
	@Test
	public final void testPutVocabularyByIdWithNonExistantOrganization() {
		// Generating a mocked ThesaurusView
		ThesaurusView mockedThesaurusView1 = getMockedThesaurusViewWithAllFieldsFilledIn("mock1");
		initThesaurusService();
		
		// Testing putVocabularyById method
		ThesaurusView view = thesaurusRestService
				.updateVocabulary(mockedThesaurusView1);

		Assert.assertEquals(mockedThesaurusView1.getContributor(),
				view.getContributor());
		Assert.assertEquals(mockedThesaurusView1.getCoverage(),
				view.getCoverage());
		Assert.assertEquals(mockedThesaurusView1.getCreated(),
				view.getCreated());
		/*
		 * Assert.assertEquals(mockedThesaurusView1.getCreatorHomepage(),
		 * view.getCreatorHomepage());
		 * Assert.assertEquals(mockedThesaurusView1.getCreatorName(),
		 * view.getCreatorName());
		 */
		Assert.assertEquals(mockedThesaurusView1.getDate(), view.getDate());
		Assert.assertEquals(mockedThesaurusView1.getDescription(),
				view.getDescription());
		Assert.assertEquals(mockedThesaurusView1.getId(), view.getId());
		Assert.assertEquals(mockedThesaurusView1.getPublisher(),
				view.getPublisher());
		Assert.assertEquals(mockedThesaurusView1.getRelation(),
				view.getRelation());
		Assert.assertEquals(mockedThesaurusView1.getRights(), view.getRights());
		Assert.assertEquals(mockedThesaurusView1.getSource(), view.getSource());
		Assert.assertEquals(mockedThesaurusView1.getSubject(),
				view.getSubject());
		Assert.assertEquals(mockedThesaurusView1.getTitle(), view.getTitle());
		/*
		 * Assert.assertEquals(mockedThesaurusView1.getFormat(),
		 * view.getFormat());
		 */
		ListAssert.assertEquals(mockedThesaurusView1.getLanguages(),
				view.getLanguages());
		/* Assert.assertEquals(mockedThesaurusView1.getType(), view.getType()); */
	}

	private ThesaurusView getMockedThesaurusViewWithNonMandatoryEmptyFields(
			String id) {
		ThesaurusView mockedThesaurusView = mock(ThesaurusView.class);

		// Mandatory fields
		when(mockedThesaurusView.getId()).thenReturn(id);
		when(mockedThesaurusView.getTitle()).thenReturn("Title");
		when(mockedThesaurusView.getRights()).thenReturn("Fake Rights");

		// Optional fields (return null)
		when(mockedThesaurusView.getContributor()).thenReturn(null);
		when(mockedThesaurusView.getCoverage()).thenReturn(null);
		when(mockedThesaurusView.getDate()).thenReturn(null);
		when(mockedThesaurusView.getDescription()).thenReturn(null);
		when(mockedThesaurusView.getPublisher()).thenReturn(null);
		when(mockedThesaurusView.getRelation()).thenReturn(null);
		when(mockedThesaurusView.getSource()).thenReturn(null);
		when(mockedThesaurusView.getSubject()).thenReturn(null);
		when(mockedThesaurusView.getCreated()).thenReturn(null);
		when(mockedThesaurusView.getFormat()).thenReturn(null);
		when(mockedThesaurusView.getType()).thenReturn(null);
		when(mockedThesaurusView.getCreatorName()).thenReturn(null);
		when(mockedThesaurusView.getCreatorHomepage()).thenReturn(null);

		List<String> mockedLanguages = new ArrayList<String>();
		when(mockedThesaurusView.getLanguages()).thenReturn(mockedLanguages);

		return mockedThesaurusView;
	}

	private ThesaurusView getMockedThesaurusViewWithAllFieldsFilledIn(String id) {

		ThesaurusView mockedThesaurusView = mock(ThesaurusView.class);

		// Mandatory fields
		mockedThesaurusView = getMockedThesaurusViewWithNonMandatoryEmptyFields(id);

		// Optional fields filled in
		when(mockedThesaurusView.getContributor()).thenReturn(
				"Fake Contributor");
		when(mockedThesaurusView.getCoverage()).thenReturn("Fake Coverage");
		when(mockedThesaurusView.getDate()).thenReturn("2013-02-15 02:02:02");
		when(mockedThesaurusView.getDescription()).thenReturn(
				"Fake Description");
		when(mockedThesaurusView.getPublisher()).thenReturn("Fake Publisher");
		when(mockedThesaurusView.getRelation()).thenReturn("Fake Relation");
		when(mockedThesaurusView.getSource()).thenReturn("Fake Source");
		when(mockedThesaurusView.getSubject()).thenReturn("Fake Subject");
		when(mockedThesaurusView.getCreated())
				.thenReturn("2013-02-15 02:02:02");
		when(mockedThesaurusView.getFormat()).thenReturn(1);
		when(mockedThesaurusView.getType()).thenReturn(1);
		when(mockedThesaurusView.getCreatorName()).thenReturn("Creator");
		when(mockedThesaurusView.getCreatorHomepage()).thenReturn(
				"http://fakeurl.com");

		List<String> mockedLanguages = new ArrayList<String>();
		String mockedLanguage1 = "fra";
		String mockedLanguage2 = "rus";
		mockedLanguages.add(mockedLanguage1);
		mockedLanguages.add(mockedLanguage2);
		when(mockedThesaurusView.getLanguages()).thenReturn(mockedLanguages);

		return mockedThesaurusView;
	}

	private void initThesaurusService() {
		Thesaurus mockedThesaurus = getMockedThesaurus();

		when(thesaurusService.getThesaurusById("mock1")).thenReturn(
				mockedThesaurus);

		when(thesaurusService.updateThesaurus(any(Thesaurus.class),
						any(IUser.class))).thenReturn(mockedThesaurus);
	}

	private Thesaurus getMockedThesaurus() {

		Thesaurus mockedThesaurus = mock(Thesaurus.class);
		// Format, type and creator might be null (not mandatory)
		when(mockedThesaurus.getFormat()).thenReturn(null);
		when(mockedThesaurus.getType()).thenReturn(null);
		when(mockedThesaurus.getCreator()).thenReturn(null);

		when(mockedThesaurus.getIdentifier()).thenReturn("mock1");
		when(mockedThesaurus.getTitle()).thenReturn("Title");
		when(mockedThesaurus.getContributor()).thenReturn("Fake Contributor");
		when(mockedThesaurus.getCoverage()).thenReturn("Fake Coverage");

		Calendar gregCalendar = new GregorianCalendar();
		gregCalendar.setTime(DateUtil.dateFromString("2013-02-15 02:02:02"));
		when(mockedThesaurus.getCreated()).thenReturn(gregCalendar.getTime());
		when(mockedThesaurus.getDate()).thenReturn(gregCalendar.getTime());
		when(mockedThesaurus.getDescription()).thenReturn("Fake Description");

		Set<Language> mockedLanguages = getMockedLanguages();
		when(mockedThesaurus.getLang()).thenReturn(mockedLanguages);

		when(mockedThesaurus.getPublisher()).thenReturn("Fake Publisher");
		when(mockedThesaurus.getRelation()).thenReturn("Fake Relation");
		when(mockedThesaurus.getRights()).thenReturn("Fake Rights");
		when(mockedThesaurus.getSource()).thenReturn("Fake Source");
		when(mockedThesaurus.getSubject()).thenReturn("Fake Subject");

		return mockedThesaurus;
	}

	private Set<Language> getMockedLanguages() {
		Language mockedLanguage1 = mock(Language.class);
		when(mockedLanguage1.getId()).thenReturn("fra");

		Language mockedLanguage2 = mock(Language.class);
		when(mockedLanguage2.getId()).thenReturn("rus");

		Set<Language> mockedLanguages = new HashSet<Language>();
		mockedLanguages.add(mockedLanguage1);
		mockedLanguages.add(mockedLanguage2);

		return mockedLanguages;
	}

	/**
	 * Gets all top languages
	 * @throws BusinessException 
	 */
	@Test
	public final void testGetTopLanguagesAll() throws BusinessException {
		List<Language> langs = getMockLanguages();
		when(languagesService.getTopLanguagesList()).thenReturn(langs);
		ExtJsonFormLoadData<List<Language>> allLanguages = thesaurusRestService
				.getTopLanguages("");
		Assert.assertEquals(2, allLanguages.getData().size());
	}

	/**
	 * Gets the languages relative to a thesaurus
	 */
	@Test
	public final void testGetTopLanguagesByThesaurus() throws BusinessException{
		List<Language> langs = getMockLanguages();
		when(thesaurusService.getThesaurusLanguages(anyString())).thenReturn(
				langs);
		ExtJsonFormLoadData<List<Language>> allLanguages = thesaurusRestService
				.getTopLanguages("notempty");
		Assert.assertEquals(2, allLanguages.getData().size());
	}
	
	private List<Language> getMockLanguages() {
		List<Language> langs = new LinkedList<Language>();
		Language lang1 = mock(Language.class);
		lang1.setId("rus");
		Language lang2 = mock(Language.class);
		lang1.setId("fra");
		langs.add(lang1);
		langs.add(lang2);
		return langs;
	}

}