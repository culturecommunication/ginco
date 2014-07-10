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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.AssociativeRelationshipRole;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IConceptHierarchicalRelationshipDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.imports.ConceptBuilder;
import fr.mcc.ginco.imports.SKOSImportUtils;
import fr.mcc.ginco.services.IAssociativeRelationshipRoleService;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipServiceUtil;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.DateUtil;

public class ConceptBuilderTest {

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "conceptHierarchicalRelationshipServiceUtil")
	private IConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;

	@Mock(name = "associativeRelationshipRoleService")
	private IAssociativeRelationshipRoleService associativeRelationshipRoleService;

	@Mock
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Mock
	private IConceptHierarchicalRelationshipDAO conceptHierarchicalRelationshipDAO;

	@Mock(name = "skosImportUtils")
	private SKOSImportUtils skosImportUtils;

	@InjectMocks
	private ConceptBuilder conceptBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuildConcept() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");
		fakeThesaurus.setDefaultTopConcept(false);
		fakeThesaurus
				.setCreated(DateUtil.dateFromString("2013-02-01 01:05:03"));
		fakeThesaurus.setDate(DateUtil.dateFromString("2013-05-02 02:10:13"));


		fakeThesaurus.setDefaultTopConcept(false);

		Model model = ModelFactory.createDefaultModel();
		Resource skosConcept = model.createResource("new-uri", SKOS.CONCEPT);
		skosConcept.addProperty(DCTerms.created, "2013-11-06T21:33:09.00+0100");
		skosConcept.addProperty(SKOS.NOTATION, "concept-notation");

		Calendar cal = new GregorianCalendar();
		cal.set(2013,  10,6,21,33,9);
		cal.set(Calendar.MILLISECOND, 0);
		Mockito.when(skosImportUtils.getSkosDate("2013-11-06T21:33:09.00+0100")).thenReturn(cal.getTime());

		ThesaurusConcept actualConcept = conceptBuilder.buildConcept(
				skosConcept, fakeThesaurus);

		Assert.assertEquals("new-uri", actualConcept.getIdentifier());
		Assert.assertEquals("thesaurus-uri", actualConcept.getThesaurus()
				.getIdentifier());
		Assert.assertEquals(false, actualConcept.getTopConcept());

		Assert.assertEquals(cal.getTime(),
				actualConcept.getCreated());
		Assert.assertEquals(DateUtil.dateFromString("2013-11-06 21:33:09"),
				actualConcept.getModified());

		Assert.assertEquals("concept-notation", actualConcept.getNotation());

		Assert.assertEquals(ConceptStatusEnum.VALIDATED.getStatus(),
				actualConcept.getStatus().intValue());
		ListAssert.assertContains(new ArrayList<String>(conceptBuilder
				.getBuiltConcepts().keySet()), "new-uri");

	}

	@Test
	public void testBuildConceptHierarchicaleRelationships() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Model model = ModelFactory.createDefaultModel();

		Resource skosConcept = model
				.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");
		Property gincoRelProperty = model.createProperty(GINCO.getURI()
				+ "broaderGeneric");
		model.add(
				skosConcept,
				gincoRelProperty,
				model.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423"));

		skosConcept.addProperty(SKOS.BROADER, model
				.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423"));

		ThesaurusConcept currentConcept = new ThesaurusConcept();
		currentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");

		ThesaurusConcept parentConcept = new ThesaurusConcept();
		parentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423");

		conceptBuilder
				.getBuiltConcepts()
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428",
						currentConcept);
		conceptBuilder
				.getBuiltConcepts()
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423",
						parentConcept);
		Mockito.when(this.thesaurusConceptDAO.update(currentConcept))
				.thenReturn(currentConcept);

		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
		List<ObjectProperty> broaderTypes = new ArrayList<ObjectProperty>();
		ObjectProperty generic = ontModel.createObjectProperty(GINCO.getURI()
				+ "broaderGeneric");
		generic.setLabel("broaderGeneric", null);
		broaderTypes.add(ontModel.createObjectProperty(GINCO.getURI()
				+ "broaderGeneric"));

		Map<ThesaurusConcept, List<ConceptHierarchicalRelationship>> actualRes = conceptBuilder
				.buildConceptHierarchicalRelationships(skosConcept,
						fakeThesaurus, broaderTypes);

		ThesaurusConcept actualConcept = actualRes.keySet().iterator().next();
		List<ConceptHierarchicalRelationship> actualHierarchicalRelations = actualRes
				.get(actualConcept);

		Assert.assertEquals(1, actualConcept.getParentConcepts().size());
		ThesaurusConcept actualParentConcept = actualConcept
				.getParentConcepts().iterator().next();
		Assert.assertEquals(
				"http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423",
				actualParentConcept.getIdentifier());

		Assert.assertEquals(1, actualHierarchicalRelations.size());

		ConceptHierarchicalRelationship actualRelationship = actualHierarchicalRelations
				.iterator().next();
		Assert.assertEquals(
				"http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423",
				actualRelationship.getIdentifier().getParentconceptid());
		Assert.assertEquals(1, actualRelationship.getRole().intValue());

	}

	@Test
	public void testBuildConceptAssociativeRelationships() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Model model = ModelFactory.createDefaultModel();

		Resource skosConcept = model
				.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");
		Property gincoRelProperty = model.createProperty(GINCO.getURI()
				+ "termeAssocie");
		model.add(
				skosConcept,
				gincoRelProperty,
				model.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933"));
		skosConcept
				.addProperty(
						SKOS.RELATED,
						model.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933"));

		ThesaurusConcept currentConcept = new ThesaurusConcept();
		currentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");
		ThesaurusConcept relatedConcept = new ThesaurusConcept();
		relatedConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933");

		conceptBuilder
				.getBuiltConcepts()
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428",
						currentConcept);
		conceptBuilder
				.getBuiltConcepts()
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933",
						relatedConcept);

		OntModel ontModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
		List<ObjectProperty> relatedTypes = new ArrayList<ObjectProperty>();
		ObjectProperty generic = ontModel.createObjectProperty(GINCO.getURI()
				+ "termeAssocie");
		generic.setLabel("termeAssocie", null);
		relatedTypes.add(ontModel.createObjectProperty(GINCO.getURI()
				+ "termeAssocie"));

		AssociativeRelationshipRole role = new AssociativeRelationshipRole();
		role.setCode("valeur_de_test");

		Mockito.when(
				associativeRelationshipRoleService
						.getRoleBySkosLabel("termeAssocie")).thenReturn(role);

		Set<AssociativeRelationship> actualRelations = conceptBuilder
				.buildConceptAssociativerelationship(skosConcept,
						fakeThesaurus, relatedTypes);

		Assert.assertEquals(1, actualRelations.size());
		AssociativeRelationship relationship = actualRelations.iterator()
				.next();
		Assert.assertEquals("valeur_de_test", relationship
				.getRelationshipRole().getCode());

		Assert.assertTrue("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933"
				.equals(relationship.getIdentifier().getConcept1())
				|| "http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933"
						.equals(relationship.getIdentifier().getConcept2()));
		Assert.assertTrue("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428"
				.equals(relationship.getIdentifier().getConcept1())
				|| "http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428"
						.equals(relationship.getIdentifier().getConcept2()));

	}

	@Test
	public void testBuildConceptRoot() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Model model = ModelFactory.createDefaultModel();

		Resource skosConcept = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");

		ThesaurusConcept currentConcept = new ThesaurusConcept();
		currentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");

		ThesaurusConcept rootConcept = new ThesaurusConcept();
		rootConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1930");
		conceptBuilder
				.getBuiltConcepts()
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428",
						currentConcept);
		List<ThesaurusConcept> realRoots = new ArrayList<ThesaurusConcept>();
		realRoots.add(rootConcept);

		Mockito.when(
				conceptHierarchicalRelationshipServiceUtil
						.getRootConcepts(currentConcept)).thenReturn(realRoots);
		ThesaurusConcept actualConcept = conceptBuilder.buildConceptRoot(
				skosConcept, fakeThesaurus);

		Assert.assertEquals(1, actualConcept.getRootConcepts().size());
		Assert.assertEquals(
				"http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1930",
				actualConcept.getRootConcepts().iterator().next()
						.getIdentifier());

	}

}
