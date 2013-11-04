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
import com.hp.hpl.jena.rdf.model.StmtIterator;

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
	public void testBuildGroup(){

		Language lang = new Language();
		lang.setPart1("fr");

		Model model = ModelFactory.createDefaultModel();

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		ThesaurusConceptGroupType type = new ThesaurusConceptGroupType();
		type.setSkosLabel("Domaine");

		ThesaurusConceptGroup g1 = new ThesaurusConceptGroup();
		g1.setIdentifier("http://g1");
		g1.setIsDynamic(true);
		g1.setParentConcept(c1);
		g1.setConceptGroupType(type);

		ThesaurusConceptGroupLabel label1 = new ThesaurusConceptGroupLabel();
		label1.setCreated(DateUtil.nowDate());
		label1.setModified(DateUtil.nowDate());
		label1.setLexicalValue("group1");
		label1.setLanguage(lang);
		label1.setConceptGroup(g1);

		ThesaurusConceptGroup g2 = new ThesaurusConceptGroup();
		g2.setIdentifier("http://g2");
		g2.setIsDynamic(false);
		g2.setConceptGroupType(type);
		g2.setParent(g1);
		g2.setNotation("notation");

		ThesaurusConceptGroupLabel label2 = new ThesaurusConceptGroupLabel();
		label2.setCreated(DateUtil.nowDate());
		label2.setModified(DateUtil.nowDate());
		label2.setLexicalValue("group2");
		label2.setLanguage(lang);
		label2.setConceptGroup(g2);

		Mockito.when(thesaurusConceptGroupLabelService
		.getByThesaurusConceptGroup(g1.getIdentifier())).thenReturn(label1);

		Mockito.when(thesaurusConceptGroupLabelService
				.getByThesaurusConceptGroup(g2.getIdentifier())).thenReturn(label2);

		skosGroupExporter.buildGroup(th, g1, model);
		skosGroupExporter.buildGroup(th, g2, model);

		StmtIterator properties1 = model.getResource("http://g1").listProperties();
		StmtIterator properties2 = model.getResource("http://g2").listProperties();

		Assert.assertEquals(6, properties1.toList().size());
		Assert.assertEquals(7, properties2.toList().size());
	}

}
