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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptHierarchicalRelationshipRoleEnum;
import fr.mcc.ginco.exports.skos.SKOSHierarchicalRelationshipExporter;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipService;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.SKOS;

public class SKOSHierarchicalRelationshipExporterTest {

	@Mock(name = "conceptHierarchicalRelationshipService")
	private IConceptHierarchicalRelationshipService conceptHierarchicalRelationshipService;

	@InjectMocks
	private SKOSHierarchicalRelationshipExporter skosHierarchicalRelationshipExporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportSimpleHierarchicalRelationshipsTGTS() {
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		ConceptHierarchicalRelationship r1 = new ConceptHierarchicalRelationship();
		r1.setRole(ConceptHierarchicalRelationshipRoleEnum.TGTS.getStatus());

		Mockito.when(
				conceptHierarchicalRelationshipService.getByChildAndParentIds(
						"http://c2", "http://c1")).thenReturn(r1);

		Model model = ModelFactory.createDefaultModel();
		skosHierarchicalRelationshipExporter.exportHierarchicalRelationships(
				model, c1, c2);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource c1Res = expectedModel.createResource("http://c1");
		Resource c2Res = expectedModel.createResource("http://c2");

		Assert.assertTrue(model.contains(c1Res, SKOS.NARROWER, c2Res));
		Assert.assertTrue(model.contains(c2Res, SKOS.BROADER, c1Res));

	}

	@Test
	public void testExportSimpleHierarchicalRelationshipsTGGTSG() {
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		ConceptHierarchicalRelationship r1 = new ConceptHierarchicalRelationship();
		r1.setRole(ConceptHierarchicalRelationshipRoleEnum.TGGTSG.getStatus());

		Mockito.when(
				conceptHierarchicalRelationshipService.getByChildAndParentIds(
						"http://c2", "http://c1")).thenReturn(r1);

		Model model = ModelFactory.createDefaultModel();
		skosHierarchicalRelationshipExporter.exportHierarchicalRelationships(
				model, c1, c2);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource c1Res = expectedModel.createResource("http://c1");
		Resource c2Res = expectedModel.createResource("http://c2");

		Property gincoRelBProperty = expectedModel.createProperty(GINCO
				.getURI() + "broaderGeneric");
		Property gincoRelNProperty = expectedModel.createProperty(GINCO
				.getURI() + "narrowerGeneric");

		Assert.assertTrue(model.contains(c1Res, SKOS.NARROWER, c2Res));
		Assert.assertTrue(model.contains(c2Res, SKOS.BROADER, c1Res));

		Assert.assertTrue(model.contains(c1Res, gincoRelNProperty, c2Res));
		Assert.assertTrue(model.contains(c2Res, gincoRelBProperty, c1Res));
	}

	@Test
	public void testExportSimpleHierarchicalRelationshipsTGITSI() {
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		ConceptHierarchicalRelationship r1 = new ConceptHierarchicalRelationship();
		r1.setRole(ConceptHierarchicalRelationshipRoleEnum.TGITSI.getStatus());

		Mockito.when(
				conceptHierarchicalRelationshipService.getByChildAndParentIds(
						"http://c2", "http://c1")).thenReturn(r1);

		Model model = ModelFactory.createDefaultModel();
		skosHierarchicalRelationshipExporter.exportHierarchicalRelationships(
				model, c1, c2);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource c1Res = expectedModel.createResource("http://c1");
		Resource c2Res = expectedModel.createResource("http://c2");

		Property gincoRelBProperty = expectedModel.createProperty(GINCO
				.getURI() + "broaderInstantive");
		Property gincoRelNProperty = expectedModel.createProperty(GINCO
				.getURI() + "narrowerInstantive");

		Assert.assertTrue(model.contains(c1Res, SKOS.NARROWER, c2Res));
		Assert.assertTrue(model.contains(c2Res, SKOS.BROADER, c1Res));

		Assert.assertTrue(model.contains(c1Res, gincoRelNProperty, c2Res));
		Assert.assertTrue(model.contains(c2Res, gincoRelBProperty, c1Res));
	}

	@Test
	public void testExportSimpleHierarchicalRelationshipsTGPTSP() {
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		ConceptHierarchicalRelationship r1 = new ConceptHierarchicalRelationship();
		r1.setRole(ConceptHierarchicalRelationshipRoleEnum.TGPTSP.getStatus());

		Mockito.when(
				conceptHierarchicalRelationshipService.getByChildAndParentIds(
						"http://c2", "http://c1")).thenReturn(r1);

		Model model = ModelFactory.createDefaultModel();
		skosHierarchicalRelationshipExporter.exportHierarchicalRelationships(
				model, c1, c2);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource c1Res = expectedModel.createResource("http://c1");
		Resource c2Res = expectedModel.createResource("http://c2");

		Property gincoRelBProperty = expectedModel.createProperty(GINCO
				.getURI() + "broaderPartitive");
		Property gincoRelNProperty = expectedModel.createProperty(GINCO
				.getURI() + "narrowerPartitive");

		Assert.assertTrue(model.contains(c1Res, SKOS.NARROWER, c2Res));
		Assert.assertTrue(model.contains(c2Res, SKOS.BROADER, c1Res));

		Assert.assertTrue(model.contains(c1Res, gincoRelNProperty, c2Res));
		Assert.assertTrue(model.contains(c2Res, gincoRelBProperty, c1Res));
	}

	@Test
	public void testExportSimpleHierarchicalRelationshipsTopconcept() {
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");
		c2.setThesaurus(th);

		Model model = ModelFactory.createDefaultModel();
		skosHierarchicalRelationshipExporter.exportHierarchicalRelationships(
				model, null, c2);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource scheme = expectedModel.createResource("http://th1");
		Resource childRes = expectedModel.createResource("http://c2");

		Assert.assertTrue(model
				.contains(scheme, SKOS.HAS_TOP_CONCEPT, childRes));

	}

}