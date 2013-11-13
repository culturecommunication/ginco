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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.mcc.ginco.imports.SKOSImportUtils;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;

public class SKOSImportUtilsTest {

	@InjectMocks
	private SKOSImportUtils skosImportUtils;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		List<String> dateFormats = new ArrayList<String>();
		dateFormats.add("yyyy-MM-dd HH:mm:ss");
		ReflectionTestUtils.setField(skosImportUtils, "skosDefaultDateFormats", dateFormats);		
	}

	@Test
	public void testGetSKOSRessources() {

		Model model = ModelFactory.createDefaultModel();
		model.createResource("http://fakeconcept", SKOS.CONCEPT);
		model.createResource("http://fakecollection1", SKOS.COLLECTION);
		model.createResource("http://fakecollection2", SKOS.COLLECTION);

		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
		ontModel.createObjectProperty(ISOTHES.SUPER_GROUP.getURI());

		model.add(ontModel);

		List<Resource> actualRes = skosImportUtils.getSKOSRessources(model,
				SKOS.COLLECTION);

		Assert.assertEquals(2, actualRes.size());
		
		for (Resource res:actualRes) {
			Assert.assertTrue(res.getURI().equals("http://fakecollection1") ||res.getURI().equals("http://fakecollection2"));
		}

	}

	@Test
	public void testGetBroaderTypeProperty() {
		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
		ontModel.createObjectProperty(ISOTHES.SUPER_GROUP.getURI());

		ontModel.createObjectProperty("http://fakebroaderProperty1")
				.addProperty(RDFS.subPropertyOf, SKOS.BROADER);

		ontModel.createObjectProperty("http://fakebroaderProperty2")
				.addProperty(RDFS.subPropertyOf, SKOS.BROADER);

		List<ObjectProperty> actualRes = SKOSImportUtils
				.getBroaderTypeProperty(ontModel);

		Assert.assertEquals(2, actualRes.size());
		for (ObjectProperty prop:actualRes) {
			Assert.assertTrue(prop.getURI().equals("http://fakebroaderProperty1") ||prop.getURI().equals("http://fakebroaderProperty2"));
		}

	}

	@Test
	public void testGetRelatedTypeProperty() {
		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
		ontModel.createObjectProperty(ISOTHES.SUPER_GROUP.getURI());

		ontModel.createObjectProperty("http://relatedProperty1").addProperty(
				RDFS.subPropertyOf, SKOS.RELATED);

		ontModel.createObjectProperty("http://relatedProperty2").addProperty(
				RDFS.subPropertyOf, SKOS.RELATED);

		List<ObjectProperty> actualRes = skosImportUtils
				.getRelatedTypeProperty(ontModel);

		Assert.assertEquals(2, actualRes.size());		
		for (ObjectProperty prop:actualRes) {
			Assert.assertTrue(prop.getURI().equals("http://relatedProperty1") ||prop.getURI().equals("http://relatedProperty2"));
		}	

	}
	
	@Test
	public void testGetSKOSDate() {
		Date skosDate = skosImportUtils.getSkosDate("2013-01-20 20:35:05");		
		Calendar actualCal = new GregorianCalendar();
		actualCal.setTime(skosDate);
	
		Assert.assertEquals(2013, actualCal.get(Calendar.YEAR));
		Assert.assertEquals(0, actualCal.get(Calendar.MONTH));
		Assert.assertEquals(20, actualCal.get(Calendar.DAY_OF_MONTH));
		Assert.assertEquals(20, actualCal.get(Calendar.HOUR_OF_DAY));
		Assert.assertEquals(35, actualCal.get(Calendar.MINUTE));
		Assert.assertEquals(5, actualCal.get(Calendar.SECOND));

	}

}
