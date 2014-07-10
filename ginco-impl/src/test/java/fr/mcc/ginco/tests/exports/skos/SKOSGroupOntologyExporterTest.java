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

import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import fr.mcc.ginco.exports.skos.SKOSGroupOntologyExporter;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;

public class SKOSGroupOntologyExporterTest {

	private SKOSGroupOntologyExporter skosGroupOntologyExporter = new SKOSGroupOntologyExporter();

	@Test
	public void testAddGroupTypeToOntModel() {

		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);

		skosGroupOntologyExporter.addGroupTypeToOntModel(ontModel, "facette");

		Assert.assertNotNull(ontModel.getOntClass(GINCO.getURI() + "facette"));
		Assert.assertTrue(ontModel.getOntClass(GINCO.getURI() + "facette")
				.hasSuperClass(ISOTHES.CONCEPT_GROUP));
	}

	@Test
	public void testBuildGroupOntologyModel() {

		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);

		skosGroupOntologyExporter.buildGroupOntologyModel(ontModel);

		Assert.assertNotNull(ontModel.getOntClass(ISOTHES.CONCEPT_GROUP
				.toString()));
		Assert.assertTrue(ontModel
				.getOntClass(ISOTHES.CONCEPT_GROUP.toString()).hasSuperClass(
						SKOS.COLLECTION));

		Assert.assertNotNull(ontModel.getObjectProperty(ISOTHES.SUB_GROUP
				.getURI()));
		Assert.assertTrue(ontModel
				.getObjectProperty(ISOTHES.SUB_GROUP.getURI()).getRange()
				.equals(ontModel.getOntClass(ISOTHES.CONCEPT_GROUP.toString())));
		Assert.assertTrue(ontModel
				.getObjectProperty(ISOTHES.SUB_GROUP.getURI()).getDomain()
				.equals(ontModel.getOntClass(ISOTHES.CONCEPT_GROUP.toString())));

		Assert.assertNotNull(ontModel.getObjectProperty(ISOTHES.SUPER_GROUP
				.getURI()));
		Assert.assertTrue(ontModel
				.getObjectProperty(ISOTHES.SUPER_GROUP.getURI()).getRange()
				.equals(ontModel.getOntClass(ISOTHES.CONCEPT_GROUP.toString())));
		Assert.assertTrue(ontModel
				.getObjectProperty(ISOTHES.SUPER_GROUP.getURI()).getDomain()
				.equals(ontModel.getOntClass(ISOTHES.CONCEPT_GROUP.toString())));

	}

}
