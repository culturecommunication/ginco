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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.beans.ThesaurusConceptGroupType;
import fr.mcc.ginco.exports.skos.skosapi.SKOSGroupExporter;
import fr.mcc.ginco.services.IThesaurusConceptGroupLabelService;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.utils.DateUtil;

public class SKOSGroupExporterTest {

	@Mock(name="thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;

	@Mock(name="thesaurusConceptGroupLabelService")
	private IThesaurusConceptGroupLabelService thesaurusConceptGroupLabelService;

	@Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@InjectMocks
	SKOSGroupExporter skosGroupExporter;

	@Before
	public void init() {
			MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testBuildSimpleGroup(){

		Model model = ModelFactory.createDefaultModel();

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

		Set<ThesaurusConcept> concepts = new HashSet<ThesaurusConcept>();
		concepts.add(c1);
		concepts.add(c2);
		concepts.add(c3);

		ThesaurusConceptGroupType type = new ThesaurusConceptGroupType();
		type.setSkosLabel("Domaine");

		ThesaurusConceptGroup g2 = new ThesaurusConceptGroup();
		g2.setIdentifier("http://g2");

		ThesaurusConceptGroup g1 = new ThesaurusConceptGroup();
		g1.setIdentifier("http://g1");
		g1.setIsDynamic(false);
		g1.setConceptGroupType(type);
		g1.setConcepts(concepts);
		g1.setParent(g2);
		g1.setNotation("notation");

		ThesaurusConceptGroupLabel label1 = new ThesaurusConceptGroupLabel();
		label1.setCreated(DateUtil.nowDate());
		label1.setModified(DateUtil.nowDate());
		label1.setLexicalValue("group1");
		label1.setLanguage(lang);
		label1.setConceptGroup(g1);

		Mockito.when(thesaurusConceptGroupLabelService
		.getByThesaurusConceptGroup(g1.getIdentifier())).thenReturn(label1);

		skosGroupExporter.buildGroup(th, g1, model);

		StmtIterator properties1 = model.getResource("http://g1").listProperties();
		Assert.assertEquals(10, properties1.toList().size());
	}

	@Test
	public void testBuildDynamicGroup(){

		Model model = ModelFactory.createDefaultModel();

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

		skosGroupExporter.buildGroup(th, g1, model);
		StmtIterator properties1 = model.getResource("http://g1").listProperties();
		Assert.assertEquals(8, properties1.toList().size());
	}
}
