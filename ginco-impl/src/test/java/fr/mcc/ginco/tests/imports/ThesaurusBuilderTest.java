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

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.dao.IThesaurusTypeDAO;
import fr.mcc.ginco.imports.SKOSImportUtils;
import fr.mcc.ginco.imports.ThesaurusBuilder;
import fr.mcc.ginco.imports.ThesaurusOrganizationBuilder;
import fr.mcc.ginco.tests.LoggerTestUtil;
import fr.mcc.ginco.utils.DateUtil;

public class ThesaurusBuilderTest {


	@Mock(name = "thesaurusFormatDAO")
	private IGenericDAO<ThesaurusFormat, Integer> thesaurusFormatDAO;

	@Mock(name = "thesaurusTypeDAO")
	private IThesaurusTypeDAO thesaurusTypeDAO;

	@Mock(name = "languagesDAO")
	private ILanguageDAO languagesDAO;
	
	@Mock(name = "skosThesaurusOrganizationBuilder")
	private ThesaurusOrganizationBuilder thesaurusOrganizationBuilder;
	
	@Mock(name = "skosImportUtils")
	private SKOSImportUtils skosImportUtils;

	@InjectMocks
	private ThesaurusBuilder thesaurusBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);	
		ReflectionTestUtils.setField(thesaurusBuilder, "defaultThesaurusFormat", 3);
		LoggerTestUtil.initLogger(thesaurusBuilder);
	}

	@Test
	public void testBuildThesaurus() throws ParseException {
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

		Resource skosThesaurus = model
				.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");		
		
		ThesaurusOrganization org = new ThesaurusOrganization();
		Mockito.when(thesaurusOrganizationBuilder.getCreator(skosThesaurus, model)).thenReturn(org);		
		
		Date now = DateUtil.nowDate();
		Mockito.when(skosImportUtils.getSkosDate("2013-11-13 11:09:10")).thenReturn(now);
		Mockito.when(skosImportUtils.getSkosDate("2012-11-13 11:09:10")).thenReturn(now);
		
		skosThesaurus.addProperty(DC.title,"Thésaurus des objets mobiliers");
		skosThesaurus.addProperty(DC.subject, "test\ninstruments de musique");
		skosThesaurus.addProperty(DCTerms.contributor, "Renaud");
		skosThesaurus.addProperty(DC.coverage, "de l'Antiquité à nos jours");
		skosThesaurus.addProperty(DC.description, "Vocabulaire de la désignation des oeuvres mobilières");
		skosThesaurus.addProperty(DC.publisher, "Ministère de la culture et de la communication");
		skosThesaurus.addProperty(DC.rights, "CC-BY-SA");
		skosThesaurus.addProperty(DC.type, "Thésaurus");
		skosThesaurus.addProperty(DC.relation, "relation");
		skosThesaurus.addProperty(DC.source, "source");
		skosThesaurus.addProperty(DCTerms.created, "2013-11-13 11:09:10");
		skosThesaurus.addProperty(DCTerms.modified, "2012-11-13 11:09:10");
		skosThesaurus.addProperty(DC.language, "fr-FR");

		
		Thesaurus actualThesaurus = thesaurusBuilder.buildThesaurus(skosThesaurus, model);
		
		Assert.assertEquals("Thésaurus des objets mobiliers", actualThesaurus.getTitle());
		Assert.assertEquals(true, actualThesaurus.getSubject().contains("instruments de musique"));
		Assert.assertEquals(true, actualThesaurus.getContributor().contains("Renaud"));
		Assert.assertEquals(true, actualThesaurus.getCoverage().contains("de l&apos;Antiquité à nos jours"));
		Assert.assertEquals("Vocabulaire de la désignation des oeuvres mobilières", actualThesaurus.getDescription());
		Assert.assertEquals("Ministère de la culture et de la communication", actualThesaurus.getPublisher());
		Assert.assertEquals("CC-BY-SA", actualThesaurus.getRights());
		Assert.assertEquals(fakeType, actualThesaurus.getType());
		Assert.assertEquals("relation", actualThesaurus.getRelation());
		Assert.assertEquals("source", actualThesaurus.getSource());
		Assert.assertTrue(actualThesaurus.isPolyHierarchical());
		Assert.assertEquals(now, actualThesaurus.getCreated());
		Assert.assertEquals(now, actualThesaurus.getDate());
		Assert.assertTrue(actualThesaurus.getLang().contains(french));
		Assert.assertTrue(actualThesaurus.getFormat().contains(format));
		Assert.assertEquals(org, actualThesaurus.getCreator());
		Assert.assertFalse(actualThesaurus.isArchived());
	}


}
