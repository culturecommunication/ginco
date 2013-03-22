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
package fr.mcc.ginco.tests.imports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.dao.IThesaurusTypeDAO;
import fr.mcc.ginco.imports.ThesaurusBuilder;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class ThesaurusBuilderTest {	

	
	@Mock(name = "thesaurusFormatDAO")
	private IGenericDAO<ThesaurusFormat, Integer> thesaurusFormatDAO;

	@Mock(name = "thesaurusTypeDAO")
	private IThesaurusTypeDAO thesaurusTypeDAO;
	
	@Mock(name = "languagesDAO")
	private ILanguageDAO languagesDAO;
	
	@InjectMocks
	private ThesaurusBuilder thesaurusBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		List<String> langFormats = new ArrayList<String>();
		langFormats.add("yyyy");		
		ReflectionTestUtils.setField(thesaurusBuilder, "skosDefaultDateFormats", langFormats);
		ReflectionTestUtils.setField(thesaurusBuilder, "defaultThesaurusFormat", 3);	
		LoggerTestUtil.initLogger(thesaurusBuilder);
	}

	@Test
	public void testBuildTerms() {
		ThesaurusType fakeType = new ThesaurusType();
		
		Mockito.when(thesaurusTypeDAO
		.getByLabel("Thésaurus")).thenReturn(fakeType);
		
		Language french = new Language();
		french.setId("fr-FR");
		Mockito.when(languagesDAO.getById("fr-FR")).thenReturn(french);
		
		ThesaurusFormat format = new ThesaurusFormat();
		format.setLabel("SKOS");
		Mockito.when(thesaurusFormatDAO	.getById(3)).thenReturn(format);
		
		Model model = ModelFactory.createDefaultModel();
		InputStream is = ThesaurusBuilderTest.class
				.getResourceAsStream("/concept_associations.rdf");
		model.read(is, null);

		Resource skosThesaurus = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");
		
		Thesaurus actualThesaurus = thesaurusBuilder.buildThesaurus(skosThesaurus, model);	
		Assert.assertEquals("Thésaurus des objets mobiliers", actualThesaurus.getTitle());
		Assert.assertEquals(true, actualThesaurus.getSubject().contains("instruments de musique"));
		Assert.assertEquals(true, actualThesaurus.getContributor().contains("Renaud"));
		Assert.assertEquals(true, actualThesaurus.getCoverage().contains("de l'Antiquité à nos jours"));
		Assert.assertEquals("Vocabulaire de la désignation des oeuvres mobilières", actualThesaurus.getDescription());
		Assert.assertEquals("Ministère de la culture et de la communication", actualThesaurus.getPublisher());
		Assert.assertEquals("CC-BY-SA", actualThesaurus.getRights());
		Assert.assertEquals(fakeType, actualThesaurus.getType());	
		
	}

	
}
