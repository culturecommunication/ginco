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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.beans.ThesaurusConceptGroupType;
import fr.mcc.ginco.exports.skos.SKOSGroupExporter;
import fr.mcc.ginco.services.IThesaurusConceptGroupLabelService;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.DateUtil;

public class SKOSGroupExporterTest {

	@Mock(name="thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;

	@Mock(name="thesaurusConceptGroupLabelService")
	private IThesaurusConceptGroupLabelService thesaurusConceptGroupLabelService;

	@Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@InjectMocks
	private SKOSGroupExporter skosGroupExporter;

	@Before
	public void init() {
			MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testExportSimpleGroup(){
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setId("fr-FR");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");
		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("http://c3");

		Set<ThesaurusConcept> concepts = new HashSet<ThesaurusConcept>();
		concepts.add(c1);
		concepts.add(c2);
		concepts.add(c3);

		ThesaurusConceptGroupType type = new ThesaurusConceptGroupType();
		type.setSkosLabel("Domaine");

		ThesaurusConceptGroup g2 = new ThesaurusConceptGroup();
		g2.setIdentifier("http://g2");
		
		ThesaurusConceptGroup g3 = new ThesaurusConceptGroup();
		g3.setIdentifier("http://g3");
		ArrayList<ThesaurusConceptGroup> childrenGroups = new ArrayList<ThesaurusConceptGroup>();
		childrenGroups.add(g3);

		ThesaurusConceptGroup g1 = new ThesaurusConceptGroup();
		g1.setIdentifier("http://g1");
		g1.setIsDynamic(false);
		g1.setConceptGroupType(type);
		g1.setConcepts(concepts);
		g1.setParent(g2);
		g1.setNotation("notation");

		Date now = DateUtil.nowDate();
		ThesaurusConceptGroupLabel label1 = new ThesaurusConceptGroupLabel();
		label1.setCreated(now);
		label1.setModified(now);
		label1.setLexicalValue("group1");
		label1.setLanguage(lang);
		label1.setConceptGroup(g1);

		Mockito.when(thesaurusConceptGroupLabelService
		.getByThesaurusConceptGroup(g1.getIdentifier())).thenReturn(label1);
		
		Mockito.when(thesaurusConceptGroupService.getChildGroups("http://g1")).thenReturn(childrenGroups);

		Model model  = ModelFactory.createDefaultModel();
		skosGroupExporter.exportGroup(th, g1, model);
		
		Model expectedModel  = ModelFactory.createDefaultModel();
		Resource groupeRes = expectedModel.createResource("http://g1");
		Resource schemeRes = expectedModel.createResource("http://th1");
		Resource c1Res = expectedModel.createResource("http://c1");
		Resource c2Res = expectedModel.createResource("http://c2");
		Resource c3Res = expectedModel.createResource("http://c3");
		Resource g2Res = expectedModel.createResource("http://g2");
		Resource g3Res = expectedModel.createResource("http://g3");


		Assert.assertTrue(model.containsResource(groupeRes));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(RDF.type, GINCO.getResource("Domaine")));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(SKOS.IN_SCHEME, schemeRes));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(DCTerms.created, DateUtil.toString(now)));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(DCTerms.modified, DateUtil.toString(now)));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(RDFS.label, "group1", "fr-FR"));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(SKOS.NOTATION, "notation"));
		
		Assert.assertTrue(model.getResource("http://g1").hasProperty(SKOS.MEMBER, c1Res));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(SKOS.MEMBER, c2Res));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(SKOS.MEMBER, c3Res));
		
		Assert.assertTrue(model.getResource("http://g1").hasProperty(ISOTHES.SUPER_GROUP, g2Res));

		Assert.assertTrue(model.getResource("http://g1").hasProperty(ISOTHES.SUB_GROUP, g3Res));
	}
	
	@Test
	public void testBuildDynamicGroup(){

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");
		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("http://c3");

		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		concepts.add(c2);
		concepts.add(c3);

		ThesaurusConceptGroupType type = new ThesaurusConceptGroupType();
		type.setSkosLabel("Domaine");

		ThesaurusConceptGroup g1 = new ThesaurusConceptGroup();
		g1.setIdentifier("http://g1");
		g1.setIsDynamic(true);
		g1.setConceptGroupType(type);
		g1.setParentConcept(c1);

		ThesaurusConceptGroupLabel label1 = new ThesaurusConceptGroupLabel();
		label1.setCreated(DateUtil.nowDate());
		label1.setModified(DateUtil.nowDate());
		label1.setLexicalValue("group1");
		label1.setLanguage(lang);
		label1.setConceptGroup(g1);

		Mockito.when(thesaurusConceptGroupLabelService
				.getByThesaurusConceptGroup(g1.getIdentifier())).thenReturn(label1);

		Mockito.when(thesaurusConceptService
		.getRecursiveChildrenByConceptId(c1.getIdentifier())).thenReturn(concepts);

		Model model  = ModelFactory.createDefaultModel();
		skosGroupExporter.exportGroup(th, g1, model);
		
		Model expectedModel  = ModelFactory.createDefaultModel();
		Resource c2Res = expectedModel.createResource("http://c2");
		Resource c3Res = expectedModel.createResource("http://c3");
		
		Assert.assertTrue(model.getResource("http://g1").hasProperty(SKOS.MEMBER, c2Res));
		Assert.assertTrue(model.getResource("http://g1").hasProperty(SKOS.MEMBER, c3Res));	
		
	}
}
