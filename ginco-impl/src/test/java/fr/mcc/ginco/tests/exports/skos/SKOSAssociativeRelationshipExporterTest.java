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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
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

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.AssociativeRelationshipRole;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exports.skos.SKOSAssociativeRelationshipExporter;
import fr.mcc.ginco.services.IAssociativeRelationshipService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.SKOS;

public class SKOSAssociativeRelationshipExporterTest {

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "associativeRelationshipService")
	private IAssociativeRelationshipService associativeRelationshipService;

	@InjectMocks
	private SKOSAssociativeRelationshipExporter skosAssociativeRelationshipExporter;

	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportAssociativeRelationship() {	

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		AssociativeRelationshipRole role1 = new AssociativeRelationshipRole();
		//role1.setCode("TA");
		role1.setSkosLabel("TermeAssocie");

		AssociativeRelationship r1 = new AssociativeRelationship();
		r1.setRelationshipRole(role1);

		List<String> c1AssociatedConceptIds = new ArrayList<String>();
		c1AssociatedConceptIds.add(c2.getIdentifier());

		Set<ThesaurusConcept> c1AssociatedConcepts = new HashSet<ThesaurusConcept>();
		c1AssociatedConcepts.add(c2);

		Mockito.when(thesaurusConceptService
				.getThesaurusConceptList(c1AssociatedConceptIds)).thenReturn(c1AssociatedConcepts);

		Mockito.when(associativeRelationshipService.getAssociatedConceptsId(c1))
				.thenReturn(c1AssociatedConceptIds);

		Mockito.when(associativeRelationshipService
					.getAssociativeRelationshipById(c1.getIdentifier(),
							c2.getIdentifier())).thenReturn(r1);

		Model model = ModelFactory.createDefaultModel();
		skosAssociativeRelationshipExporter.exportAssociativeRelationships(c1, model);
		
		Model expectedModel= ModelFactory.createDefaultModel();
		Resource conceptResource = expectedModel.createResource("http://c1");
		Resource relatedConcept = expectedModel.createResource("http://c2");
		Property customAttributeProperty = 
				expectedModel.createProperty(GINCO.getURI() + "TermeAssocie");

		Assert.assertTrue(model.contains(conceptResource, SKOS.RELATED, relatedConcept));
		Assert.assertTrue(model.contains(conceptResource, customAttributeProperty, relatedConcept));

	}
	
	
}
