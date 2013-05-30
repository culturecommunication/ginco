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
import java.util.Set;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.imports.ConceptBuilder;
import fr.mcc.ginco.imports.SKOS;
import fr.mcc.ginco.services.IAssociativeRelationshipRoleService;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipServiceUtil;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.tests.LoggerTestUtil;
import fr.mcc.ginco.utils.DateUtil;

public class ConceptBuilderTest {

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@Mock(name = "conceptHierarchicalRelationshipServiceUtil")
	private IConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;

	@Mock(name = "associativeRelationshipRoleService")
	private IAssociativeRelationshipRoleService associativeRelationshipRoleService;

	@InjectMocks
	private ConceptBuilder conceptBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(conceptBuilder);
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

		ThesaurusConcept actualConcept = conceptBuilder.buildConcept(
				skosConcept, fakeThesaurus);
		Assert.assertEquals("new-uri", actualConcept.getIdentifier());
		Assert.assertEquals("thesaurus-uri", actualConcept.getThesaurus()
				.getIdentifier());
		Assert.assertEquals(false, actualConcept.getTopConcept());
		Assert.assertEquals(DateUtil.dateFromString("2013-02-01 01:05:03"),
				actualConcept.getCreated());
		Assert.assertEquals(DateUtil.dateFromString("2013-05-02 02:10:13"),
				actualConcept.getModified());

		Assert.assertEquals(ConceptStatusEnum.VALIDATED.getStatus(),
				actualConcept.getStatus().intValue());
		ListAssert.assertContains(new ArrayList<String>(
				conceptBuilder.builtConcepts.keySet()), "new-uri");

	}

	@Test
	public void testBuildConceptHierarchicaleRelationships() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Model model = ModelFactory.createDefaultModel();
		InputStream is = ConceptBuilderTest.class
				.getResourceAsStream("/imports/concept_associations.rdf");
		model.read(is, null);

		Resource skosConcept = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");

		ThesaurusConcept currentConcept = new ThesaurusConcept();
		currentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");		
		
		ThesaurusConcept parentConcept = new ThesaurusConcept();
		parentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423");

		conceptBuilder.builtConcepts
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428",
						currentConcept);		
		conceptBuilder.builtConcepts
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423",
						parentConcept);

		ThesaurusConcept actualConcept = conceptBuilder.buildConceptHierarchicalRelationships(skosConcept, fakeThesaurus);
		
		Set<ThesaurusConcept> actualParents = actualConcept.getParentConcepts();
		List<String> parentIds = new ArrayList<String>();
		for (ThesaurusConcept actualParent : actualParents) {
			parentIds.add(actualParent.getIdentifier());
		}
		Assert.assertEquals(1, actualParents.size());
		ListAssert
				.assertContains(parentIds,
						"http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2423");
	}
	
	@Test
	public void testBuildConceptAssociativeRelationships() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Model model = ModelFactory.createDefaultModel();
		InputStream is = ConceptBuilderTest.class
				.getResourceAsStream("/imports/concept_associations.rdf");
		model.read(is, null);

		Resource skosConcept = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");

		ThesaurusConcept currentConcept = new ThesaurusConcept();
		currentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");
		ThesaurusConcept relatedConcept = new ThesaurusConcept();
		relatedConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933");		

		conceptBuilder.builtConcepts
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428",
						currentConcept);
		conceptBuilder.builtConcepts
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933",
						relatedConcept);
		

		Set<AssociativeRelationship> actualRelations = conceptBuilder
				.buildConceptAssociativerelationship(skosConcept, fakeThesaurus);
		List<String> relatedIds = new ArrayList<String>();
		for (AssociativeRelationship actualRelation : actualRelations) {
			if (actualRelation.getIdentifier().getConcept1().equals("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428")) {
				relatedIds.add(actualRelation.getIdentifier().getConcept2());
			} else {
				relatedIds.add(actualRelation.getIdentifier().getConcept1());

			}
		}
		Assert.assertEquals(1, relatedIds.size());
		ListAssert
				.assertContains(relatedIds,
						"http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1933");
	}

	@Test
	public void testBuildConceptRoot() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Model model = ModelFactory.createDefaultModel();
		InputStream is = ConceptBuilderTest.class
				.getResourceAsStream("/imports/concept_associations.rdf");
		model.read(is, null);

		Resource skosConcept = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");
		
		ThesaurusConcept currentConcept = new ThesaurusConcept();
		currentConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428");		
		
		ThesaurusConcept rootConcept = new ThesaurusConcept();
		rootConcept
				.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1930");
		conceptBuilder.builtConcepts
				.put("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-2428",
						currentConcept);
		List<ThesaurusConcept> realRoots = new ArrayList<ThesaurusConcept>();
		realRoots.add(rootConcept);
		
		Mockito.when(conceptHierarchicalRelationshipServiceUtil.getRootConcepts(currentConcept)).thenReturn(realRoots);
		ThesaurusConcept actualConcept = conceptBuilder
				.buildConceptRoot(skosConcept, fakeThesaurus);
		
		Assert.assertEquals(1, actualConcept.getRootConcepts().size());
		Assert.assertEquals("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1930", actualConcept.getRootConcepts().iterator().next().getIdentifier());

	}
	
}
