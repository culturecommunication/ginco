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
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentConcept;
import fr.mcc.ginco.beans.AlignmentType;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exports.skos.SKOSAlignmentExporter;
import fr.mcc.ginco.services.IAlignmentService;
import fr.mcc.ginco.skos.namespaces.SKOS;

public class SKOSAlignmentExporterTest {
	@Mock(name = "alignmentService")
	private IAlignmentService alignmentService;

	@InjectMocks
	private SKOSAlignmentExporter skosAlignmentExporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportAlignmentsEQ() throws IOException {

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		// Source concept
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		// Target internal concept
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		List<Alignment> alignements = new ArrayList<Alignment>();

		// Alignment =EQ (internal)
		alignements.add(buildInternalAlignement("=EQ", c2));

		// Alignment ~EQ (external)
		Alignment aClose = new Alignment();
		AlignmentType tClose = new AlignmentType();
		AlignmentConcept alignmentConceptClose = new AlignmentConcept();
		alignmentConceptClose
				.setExternalTargetConcept("http://external_concept");
		Set<AlignmentConcept> targetConceptsClose = new HashSet<AlignmentConcept>();
		targetConceptsClose.add(alignmentConceptClose);
		tClose.setIsoCode("~EQ");
		aClose.setAlignmentType(tClose);
		aClose.setTargetConcepts(targetConceptsClose);
		alignements.add(aClose);

		Mockito.when(
				alignmentService.getAlignmentsBySourceConceptId("http://c1"))
				.thenReturn(alignements);

		Model model = ModelFactory.createDefaultModel();
		skosAlignmentExporter.exportAlignments(c1.getIdentifier(), model);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource conceptResource = expectedModel.createResource("http://c1");
		Resource alignment1 = expectedModel.createResource("http://c2");
		Resource alignment2 = expectedModel
				.createResource("http://external_concept");

		Assert.assertTrue(model.contains(conceptResource, SKOS.EXACT_MATCH,
				alignment1));
		Assert.assertTrue(model.contains(conceptResource, SKOS.CLOSE_MATCH,
				alignment2));
	}

	@Test
	public void testExportAlignmentsBM() throws IOException {

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		// Source concept
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		// Target internal concept
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		// Target internal concept
		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("http://c3");

		// Target internal concept
		ThesaurusConcept c4 = new ThesaurusConcept();
		c4.setIdentifier("http://c4");

		// Target internal concept
		ThesaurusConcept c5 = new ThesaurusConcept();
		c5.setIdentifier("http://c5");

		List<Alignment> alignements = new ArrayList<Alignment>();

		// Alignment BM (internal)
		alignements.add(buildInternalAlignement("BM", c2));

		// Alignment BMG (external)
		alignements.add(buildInternalAlignement("BMG", c3));

		// Alignment BMP (internal)
		alignements.add(buildInternalAlignement("BMP", c4));

		// Alignment BMI (internal)
		alignements.add(buildInternalAlignement("BMI", c5));

		Mockito.when(
				alignmentService.getAlignmentsBySourceConceptId("http://c1"))
				.thenReturn(alignements);

		Model model = ModelFactory.createDefaultModel();
		skosAlignmentExporter.exportAlignments(c1.getIdentifier(), model);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource conceptResource = expectedModel.createResource("http://c1");
		Resource alignment1 = expectedModel.createResource("http://c2");
		Resource alignment2 = expectedModel.createResource("http://c3");
		Resource alignment3 = expectedModel.createResource("http://c4");
		Resource alignment4 = expectedModel.createResource("http://c5");

		Assert.assertTrue(model.contains(conceptResource, SKOS.BROAD_MATCH,
				alignment1));
		Assert.assertTrue(model.contains(conceptResource, SKOS.BROAD_MATCH,
				alignment2));
		Assert.assertTrue(model.contains(conceptResource, SKOS.BROAD_MATCH,
				alignment3));
		Assert.assertTrue(model.contains(conceptResource, SKOS.BROAD_MATCH,
				alignment4));
	}

	private Alignment buildInternalAlignement(String isocode,
			ThesaurusConcept concept) {
		Alignment alignment = new Alignment();
		AlignmentType alignementType = new AlignmentType();
		AlignmentConcept alignmentConcept = new AlignmentConcept();
		alignmentConcept.setInternalTargetConcept(concept);
		Set<AlignmentConcept> targetConcepts = new HashSet<AlignmentConcept>();
		targetConcepts.add(alignmentConcept);
		alignementType.setIsoCode(isocode);
		alignment.setAlignmentType(alignementType);
		alignment.setTargetConcepts(targetConcepts);
		return alignment;
	}

	@Test
	public void testExportAlignmentsNM() throws IOException {

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		Language lang = new Language();
		lang.setPart1("fr");

		// Source concept
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		// Target internal concept
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		// Target internal concept
		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("http://c3");

		// Target internal concept
		ThesaurusConcept c4 = new ThesaurusConcept();
		c4.setIdentifier("http://c4");

		// Target internal concept
		ThesaurusConcept c5 = new ThesaurusConcept();
		c5.setIdentifier("http://c5");

		List<Alignment> alignements = new ArrayList<Alignment>();

		// Alignment BM (internal)
		alignements.add(buildInternalAlignement("NM", c2));

		// Alignment BMG (external)
		alignements.add(buildInternalAlignement("NMG", c3));

		// Alignment BMP (internal)
		alignements.add(buildInternalAlignement("NMP", c4));

		// Alignment BMI (internal)
		alignements.add(buildInternalAlignement("NMI", c5));

		Mockito.when(
				alignmentService.getAlignmentsBySourceConceptId("http://c1"))
				.thenReturn(alignements);

		Model model = ModelFactory.createDefaultModel();
		skosAlignmentExporter.exportAlignments(c1.getIdentifier(), model);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource conceptResource = expectedModel.createResource("http://c1");
		Resource alignment1 = expectedModel.createResource("http://c2");
		Resource alignment2 = expectedModel.createResource("http://c3");
		Resource alignment3 = expectedModel.createResource("http://c4");
		Resource alignment4 = expectedModel.createResource("http://c5");

		Assert.assertTrue(model.contains(conceptResource, SKOS.NARROW_MATCH,
				alignment1));
		Assert.assertTrue(model.contains(conceptResource, SKOS.NARROW_MATCH,
				alignment2));
		Assert.assertTrue(model.contains(conceptResource, SKOS.NARROW_MATCH,
				alignment3));
		Assert.assertTrue(model.contains(conceptResource, SKOS.NARROW_MATCH,
				alignment4));
	}

	@Test
	public void testExportAlignmentsRM() throws IOException {

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");		

		// Source concept
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");

		// Target internal concept
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		List<Alignment> alignements = new ArrayList<Alignment>();

		// Alignment BM (internal)
		alignements.add(buildInternalAlignement("RM", c2));

		Mockito.when(
				alignmentService.getAlignmentsBySourceConceptId("http://c1"))
				.thenReturn(alignements);

		Model model = ModelFactory.createDefaultModel();
		skosAlignmentExporter.exportAlignments(c1.getIdentifier(), model);

		Model expectedModel = ModelFactory.createDefaultModel();
		Resource conceptResource = expectedModel.createResource("http://c1");
		Resource alignment1 = expectedModel.createResource("http://c2");

		Assert.assertTrue(model.contains(conceptResource, SKOS.RELATED_MATCH,
				alignment1));
	}

}
