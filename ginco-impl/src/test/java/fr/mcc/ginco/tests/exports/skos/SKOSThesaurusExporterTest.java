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
package fr.mcc.ginco.tests.exports.skos;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exports.skos.SKOSThesaurusExporter;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * This component is in charge of exporting collections to SKOS
 *
 */
public class SKOSThesaurusExporterTest {		
	
	@Mock(name="thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;
	
	@InjectMocks
	private SKOSThesaurusExporter skosThesaurusExporter;	
	
	
	
	@Before
	public void init() {		
			MockitoAnnotations.initMocks(this);	
	}
	
	@Test
	public void testExportThesaurus() throws IOException {				
		Model model = ModelFactory.createDefaultModel();
		
		ThesaurusVersionHistory tvh = new ThesaurusVersionHistory();
		tvh.setVersionNote("note de version");
		Mockito.when(thesaurusVersionHistoryDAO.findThisVersionByThesaurusId("http://thesaurus1")).thenReturn(tvh);
		
		Language lang1 = new Language();
		lang1.setId("fr-FR");
		Language lang2 = new Language();
		lang2.setId("fr-CA");
		Set<Language> languages = new HashSet<Language>();
		languages.add(lang1);
		languages.add(lang2);
		
		Calendar cal1 = new GregorianCalendar();
		cal1.set(2013,10,06, 21,33,9);
		cal1.set(Calendar.MILLISECOND, 0);
		
		Calendar cal2 = new GregorianCalendar();
		cal2.set(2012,10,06, 21,33,9);
		cal2.set(Calendar.MILLISECOND, 0);
		
		ThesaurusType type = new ThesaurusType();
		type.setLabel("Thésaurus");
		
		ThesaurusOrganization org = new ThesaurusOrganization();
		org.setEmail("mail@culture.gouv.fr");
		org.setHomepage("http://www.culture.gouv.fr");
		org.setName("MCC");
		
		Thesaurus thesaurus = new Thesaurus();
		thesaurus.setIdentifier("http://thesaurus1");
		thesaurus.setTitle("Thesaurus title");
		thesaurus.setCreated(cal1.getTime());
		thesaurus.setDate(cal2.getTime());
		thesaurus.setRights("thesaurus rights");
		thesaurus.setDescription("thesaurus description");
		thesaurus.setRelation("thesaurus relation");
		thesaurus.setSource("thesaurus source");
		thesaurus.setPublisher("CC-BY-SA");
		thesaurus.setContributor("contributor1\ncontributor2");
		thesaurus.setCoverage("coverage1\ncoverage2");
		thesaurus.setSubject("");
		thesaurus.setLang(languages);
		thesaurus.setType(type);
		thesaurus.setCreator(org);
		
		skosThesaurusExporter.exportThesaurusSKOS(thesaurus, model);
		
		Model modelExpected = ModelFactory.createDefaultModel();
		Resource thExpected = modelExpected.createResource("http://thesaurus1");
		
		Assert.assertTrue(model.containsResource(thExpected));	
		Assert.assertTrue(model.getResource("http://thesaurus1").hasProperty(RDF.type, SKOS.CONCEPTSCHEME));

		Resource actualRes = model.getResource("http://thesaurus1");
		Assert.assertTrue(actualRes.hasProperty(DC.title, "Thesaurus title"));
		Assert.assertTrue(actualRes.hasProperty(DCTerms.issued, "note de version"));
		
		Assert.assertTrue(model.contains(actualRes, DCTerms.created));
		Assert.assertTrue(model.getProperty(actualRes, DCTerms.created).getString().startsWith("2013-11-06T21:33:09"));
		Assert.assertTrue(model.contains(actualRes, DCTerms.modified));
		Assert.assertTrue(model.getProperty(actualRes, DCTerms.modified).getString().startsWith("2012-11-06T21:33:09"));
		
		Assert.assertTrue(actualRes.hasProperty(DCTerms.issued, "note de version"));

		Assert.assertTrue(actualRes.hasProperty(DC.rights, "thesaurus rights"));

		Assert.assertTrue(actualRes.hasProperty(DC.description, "thesaurus description"));
		Assert.assertTrue(actualRes.hasProperty(DC.relation, "thesaurus relation"));
		Assert.assertTrue(actualRes.hasProperty(DC.source, "thesaurus source"));
		Assert.assertTrue(actualRes.hasProperty(DC.publisher, "CC-BY-SA"));
		
		Assert.assertTrue(actualRes.hasProperty(DC.contributor, "contributor1"));
		Assert.assertTrue(actualRes.hasProperty(DC.contributor, "contributor2"));

		Assert.assertTrue(actualRes.hasProperty(DC.coverage, "coverage1"));
		Assert.assertTrue(actualRes.hasProperty(DC.coverage, "coverage2"));

		Assert.assertTrue(actualRes.hasProperty(DC.subject));
		
		Assert.assertTrue(actualRes.hasProperty(DC.language, "fr-FR"));
		Assert.assertTrue(actualRes.hasProperty(DC.language, "fr-CA"));	
		Assert.assertTrue(actualRes.hasProperty(DC.type, "Thésaurus"));	
		
		Assert.assertTrue(actualRes.hasProperty(DC.creator));	
		Statement orgStmt = actualRes.getProperty(DC.creator);
		Assert.assertTrue(orgStmt.getObject().asResource().hasProperty(RDF.type, FOAF.Organization));
		Resource foafOrg = orgStmt.getObject().asResource();
		Assert.assertTrue(foafOrg.hasProperty(FOAF.name, "MCC"));
		Assert.assertTrue(foafOrg.hasProperty(FOAF.homepage, "http://www.culture.gouv.fr"));
		Assert.assertTrue(foafOrg.hasProperty(FOAF.mbox, "mail@culture.gouv.fr"));

	}
	


}
