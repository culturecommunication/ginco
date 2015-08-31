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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exports.skos.SKOSAlignmentExporter;
import fr.mcc.ginco.exports.skos.SKOSAssociativeRelationshipExporter;
import fr.mcc.ginco.exports.skos.SKOSConceptExporter;
import fr.mcc.ginco.exports.skos.SKOSCustomConceptAttributeExporter;
import fr.mcc.ginco.exports.skos.SKOSHierarchicalRelationshipExporter;
import fr.mcc.ginco.exports.skos.SKOSNotesExporter;
import fr.mcc.ginco.exports.skos.SKOSTermsExporter;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;

public class SKOSConceptExporterTest {

	@InjectMocks
	private SKOSConceptExporter skosConceptExporter;

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "skosTermsExporter")
	private SKOSTermsExporter skosTermsExporter;

	@Mock(name = "skosNotesExporter")
	private SKOSNotesExporter skosNotesExporter;

	@Mock(name = "skosAssociativeRelationshipExporter")
	private SKOSAssociativeRelationshipExporter skosAssociativeRelationshipExporter;

	@Mock(name = "skosAlignmentExporter")
	private SKOSAlignmentExporter skosAlignmentExporter;

	@Mock(name = "skosCustomConceptAttributeExporter")
	private SKOSCustomConceptAttributeExporter skosCustomConceptAttributeExporter;

	@Mock(name = "skosHierarchicalRelationshipExporter")
	private SKOSHierarchicalRelationshipExporter skosHierarchicalRelationshipExporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportConceptSKOS() throws IOException {

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");
		
		Calendar cal1 = new GregorianCalendar();
		cal1.set(2013,10,06, 21,33,9);
		cal1.set(Calendar.MILLISECOND, 0);
		
		Calendar cal2 = new GregorianCalendar();
		cal2.set(2012,10,06, 21,33,9);
		cal2.set(Calendar.MILLISECOND, 0);
		

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		c1.setCreated(cal1.getTime());
		c1.setModified(cal2.getTime());
		c1.setNotation("c1_notation");
		c1.setStatus(ConceptStatusEnum.DEPRECATED.getStatus());
		c1.setThesaurus(th);

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");
		c2.setCreated(cal1.getTime());
		c2.setModified(cal2.getTime());
		c2.setNotation("c2_notation");
		c2.setStatus(ConceptStatusEnum.VALIDATED.getStatus());
		c2.setThesaurus(th);

		List<ThesaurusConcept> children = new ArrayList<ThesaurusConcept>();
		children.add(c2);

		Mockito.when(thesaurusConceptService.hasChildren("http://c1"))
				.thenReturn(true);
		Mockito.when(
				thesaurusConceptService.getChildrenByConceptId("http://c1",null))
				.thenReturn(children);

		Model model = ModelFactory.createDefaultModel();
		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);

		skosConceptExporter.exportConceptSKOS(c1, null, model, ontModel);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource expectedConceptRes = expectedModel.createResource("http://c1");
		Resource expectedThesRes = expectedModel.createResource("http://th1");
		Resource expectedConceptRes2 = expectedModel
				.createResource("http://c2");

		Assert.assertTrue(model.containsResource(expectedConceptRes));
		Assert.assertTrue(model.getResource("http://c1").hasProperty(RDF.type,
				SKOS.CONCEPT));

		Assert.assertTrue(model.getResource("http://c1").hasProperty(
				SKOS.IN_SCHEME, expectedThesRes));
		
		Resource conceptRes = model.getResource("http://c1");
		Assert.assertTrue(model.contains(conceptRes, DCTerms.created));
		Assert.assertTrue(model.getProperty(conceptRes, DCTerms.created).getString().startsWith("2013-11-06T21:33:09"));
		Assert.assertTrue(model.contains(conceptRes, DCTerms.modified));
		Assert.assertTrue(model.getProperty(conceptRes, DCTerms.modified).getString().startsWith("2012-11-06T21:33:09"));
		
		
		
		Assert.assertTrue(model.getResource("http://c1").hasProperty(
				SKOS.NOTATION, "c1_notation"));
		Assert.assertTrue(model.getResource("http://c1").hasProperty(
				ISOTHES.STATUS, "3"));

		Assert.assertTrue(model.containsResource(expectedConceptRes2));
		Assert.assertTrue(model.getResource("http://c2").hasProperty(RDF.type,
				SKOS.CONCEPT));	
		
		
		Resource conceptRes2 = model.getResource("http://c2");
		Assert.assertTrue(model.contains(conceptRes2, DCTerms.created));
		Assert.assertTrue(model.getProperty(conceptRes2, DCTerms.created).getString().startsWith("2013-11-06T21:33:09"));
		Assert.assertTrue(model.contains(conceptRes2, DCTerms.modified));
		Assert.assertTrue(model.getProperty(conceptRes2, DCTerms.modified).getString().startsWith("2012-11-06T21:33:09"));
		
		
		
		Assert.assertTrue(model.getResource("http://c2").hasProperty(
				SKOS.NOTATION, "c2_notation"));
		Assert.assertTrue(model.getResource("http://c2").hasProperty(
				ISOTHES.STATUS, "1"));

		Assert.assertNotNull(ontModel.getDatatypeProperty(ISOTHES.getURI()
				+ "status"));
	}

}