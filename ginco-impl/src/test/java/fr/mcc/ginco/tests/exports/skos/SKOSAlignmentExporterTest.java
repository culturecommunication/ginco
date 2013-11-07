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
import java.net.URI;
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
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOSManager;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentConcept;
import fr.mcc.ginco.beans.AlignmentType;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.skos.skosapi.SKOSAlignmentExporter;
import fr.mcc.ginco.services.IAlignmentService;

public class SKOSAlignmentExporterTest{

	@InjectMocks
	SKOSAlignmentExporter skosAlignmentExporter;

	SKOSManager man;
	SKOSDataset vocab;
	SKOSDataFactory factory;

	@Mock(name="alignmentService")
	private IAlignmentService alignmentService;


	@Before
	public void init() {
			MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportAlignments() throws IOException {

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");
		try {
			man = new SKOSManager();
			vocab = man.createSKOSDataset(URI.create(th.getIdentifier()));
		} catch (SKOSCreationException e) {
			throw new BusinessException("Error creating dataset from URI.",
					"error-in-skos-objects", e);
		}
		factory = man.getSKOSDataFactory();
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
		Alignment a_exact = new Alignment();
		AlignmentType t_exact = new AlignmentType();
		AlignmentConcept alignment_concept_exact = new AlignmentConcept();
		alignment_concept_exact.setInternalTargetConcept(c2);
		Set<AlignmentConcept> target_concepts_exact = new HashSet<AlignmentConcept>();
		target_concepts_exact.add(alignment_concept_exact);
		t_exact.setIsoCode("=EQ");
		a_exact.setIdentifier("http://a_exact");
		a_exact.setAlignmentType(t_exact);
		a_exact.setSourceConcept(c1);
		a_exact.setTargetConcepts(target_concepts_exact);
		alignements.add(a_exact);

		// Alignment ~EQ (external)
		Alignment a_close = new Alignment();
		AlignmentType t_close = new AlignmentType();
		AlignmentConcept alignment_concept_close = new AlignmentConcept();
		alignment_concept_close.setExternalTargetConcept("http://external_concept");
		Set<AlignmentConcept> target_concepts_close = new HashSet<AlignmentConcept>();
		target_concepts_close.add(alignment_concept_close);
		t_close.setIsoCode("~EQ");
		a_close.setIdentifier("http://a_close");
		a_close.setAlignmentType(t_close);
		a_close.setSourceConcept(c1);
		a_close.setTargetConcepts(target_concepts_close);
		alignements.add(a_close);

		Mockito.when(alignmentService.getAlignmentsBySourceConceptId("http://c1")).thenReturn(alignements);
		SKOSConcept conceptSKOS = factory.getSKOSConcept(URI.create(c1
				.getIdentifier()));

		List<SKOSChange> skosChanges  = skosAlignmentExporter.exportAlignments(c1
				.getIdentifier(), factory, conceptSKOS, vocab);
		Assert.assertEquals(2, skosChanges.size());
	}
}
