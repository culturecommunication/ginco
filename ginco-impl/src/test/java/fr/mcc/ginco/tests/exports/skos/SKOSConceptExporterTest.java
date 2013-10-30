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
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOSManager;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.skos.SKOSAlignmentExporter;
import fr.mcc.ginco.exports.skos.SKOSAssociativeRelationshipExporter;
import fr.mcc.ginco.exports.skos.SKOSConceptExporter;
import fr.mcc.ginco.exports.skos.SKOSCustomConceptAttributeExporter;
import fr.mcc.ginco.exports.skos.SKOSNotesExporter;
import fr.mcc.ginco.exports.skos.SKOSTermsExporter;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.utils.DateUtil;

public class SKOSConceptExporterTest {

	@InjectMocks
	SKOSConceptExporter skosConceptExporter;

	@Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name="skosTermsExporter")
	private SKOSTermsExporter skosTermsExporter;

	@Mock(name="skosNotesExporter")
	private SKOSNotesExporter skosNotesExporter;

	@Mock(name="skosAssociativeRelationshipExporter")
	private SKOSAssociativeRelationshipExporter skosAssociativeRelationshipExporter;

	@Mock(name="skosAlignmentExporter")
	private SKOSAlignmentExporter skosAlignmentExporter;

	@Mock(name="skosCustomConceptAttributeExporter")
	private SKOSCustomConceptAttributeExporter skosCustomConceptAttributeExporter;

	SKOSManager man;
	SKOSDataset vocab;
	SKOSDataFactory factory;
	SKOSConceptScheme scheme;
	SKOSConcept conceptSKOS;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportConceptSKOS() throws IOException{

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
		scheme = factory.getSKOSConceptScheme(URI.create(th.getIdentifier()));
		Language lang = new Language();
		lang.setPart1("fr");

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		c1.setCreated(DateUtil.nowDate());
		c1.setModified(DateUtil.nowDate());
		c1.setNotation("c1_notation");
		c1.setStatus(ConceptStatusEnum.DEPRECATED.getStatus());

		conceptSKOS = factory.getSKOSConcept(URI.create("http://c1"));

		List<SKOSChange> skosChanges  = skosConceptExporter.exportConceptSKOS(c1, null, scheme, factory, vocab);
		Assert.assertEquals(7, skosChanges.size());
	}

}