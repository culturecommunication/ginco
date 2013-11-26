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
package fr.mcc.ginco.tests.extjs.view.utils;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.extjs.view.utils.ThesaurusViewConverter;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusFormatService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTypeService;

public class ThesaurusViewConverterTest {
	@Mock(name = "thesaurusService")
	private IThesaurusService thesaurusService;

	@Mock(name = "languagesService")
	private ILanguagesService languagesService;

	@Mock(name = "thesaurusTypeService")
	private IThesaurusTypeService thesaurusTypeService;

	@Mock(name = "thesaurusFormatService")
	private IThesaurusFormatService thesaurusFormatService;

	@InjectMocks
	private ThesaurusViewConverter converter = new ThesaurusViewConverter();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertExistingThesaurusView() {

		ThesaurusView view = buildThesaurusView("view1");
		Thesaurus emptyThes = new Thesaurus();
		emptyThes.setIdentifier("view1");
		when(thesaurusService.getThesaurusById("view1")).thenReturn(emptyThes);

		ThesaurusFormat format1 = new ThesaurusFormat();
		format1.setLabel("CSV");
		when(thesaurusFormatService.getThesaurusFormatById(1)).thenReturn(format1);

		ThesaurusFormat format2 = new ThesaurusFormat();
		format2.setLabel("PDF 1.7");
		when(thesaurusFormatService.getThesaurusFormatById(2)).thenReturn(format2);

		ThesaurusType type = new ThesaurusType();
		type.setLabel("TYPE");
		when(thesaurusTypeService.getThesaurusTypeById(1)).thenReturn(type);

		Thesaurus th = converter.convert(view);

		Assert.assertEquals("Fake Contributor", th.getContributor());
		Assert.assertEquals("Fake Coverage", th.getCoverage());

		Assert.assertEquals("contact@fakeurl.com", th.getCreator().getEmail());
		Assert.assertEquals("http://fakeurl.com", th.getCreator().getHomepage());
		Assert.assertEquals("Creator", th.getCreator().getName());

		Assert.assertEquals("Fake Description", th.getDescription());
		Assert.assertEquals(false, th.isDefaultTopConcept());
		Assert.assertEquals("view1", th.getIdentifier());
		Assert.assertEquals("Fake Publisher", th.getPublisher());
		Assert.assertEquals("Fake Relation", th.getRelation());
		Assert.assertEquals("Fake Source", th.getSource());
		Assert.assertEquals("Fake Subject", th.getSubject());
		Assert.assertEquals("Fake Title", th.getTitle());

		List<String> formatList = new ArrayList<String>();
		for (ThesaurusFormat format : th.getFormat()){
			formatList.add(format.getLabel());
		}

		Assert.assertTrue(formatList.contains("CSV"));
		Assert.assertTrue(formatList.contains("PDF 1.7"));

		Assert.assertEquals("TYPE", th.getType().getLabel());

	}

	private ThesaurusView buildThesaurusView(String id) {

		ThesaurusView expectedThesaurusView = new ThesaurusView();

		expectedThesaurusView.setId(id);
		// Optional fields filled in
		expectedThesaurusView.setTitle("Fake Title");

		expectedThesaurusView.setContributor("Fake Contributor");
		expectedThesaurusView.setCoverage("Fake Coverage");
		expectedThesaurusView.setDate("2013-02-15 03:02:02");
		expectedThesaurusView.setDescription("Fake Description");
		expectedThesaurusView.setPublisher("Fake Publisher");
		expectedThesaurusView.setRelation("Fake Relation");
		expectedThesaurusView.setSource("Fake Source");
		expectedThesaurusView.setSubject("Fake Subject");
		expectedThesaurusView.setCreated("2013-02-15 02:02:02");
		expectedThesaurusView.setDefaultTopConcept(false);

		List<Integer> fakeFormats = new ArrayList<Integer>();
		fakeFormats.add(1);
		fakeFormats.add(2);
		expectedThesaurusView.setFormats(fakeFormats);

		expectedThesaurusView.setType(1);
		expectedThesaurusView.setCreatorName("Creator");
		expectedThesaurusView.setCreatorHomepage("http://fakeurl.com");
		expectedThesaurusView.setCreatorEmail("contact@fakeurl.com");

		List<String> expectedLanguages = new ArrayList<String>();
		String expectedLanguage1 = "fra";
		String expectedLanguage2 = "rus";
		expectedLanguages.add(expectedLanguage1);
		expectedLanguages.add(expectedLanguage2);
		expectedThesaurusView.setLanguages(expectedLanguages);

		return expectedThesaurusView;
	}

}
