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

import java.net.URI;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOSManager;

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptHierarchicalRelationshipRoleEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.skos.SKOSHierarchicalRelationshipExporter;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipService;

public class SKOSHierarchicalRelationshipExporterTest {

	@Mock(name="conceptHierarchicalRelationshipService")
	private IConceptHierarchicalRelationshipService conceptHierarchicalRelationshipService;

	@InjectMocks
	SKOSHierarchicalRelationshipExporter skosHierarchicalRelationshipExporter;

	SKOSManager man;
	SKOSDataset vocab;
	SKOSDataFactory factory;
	SKOSConceptScheme scheme;
	SKOSConcept conceptSKOS;
	SKOSConcept parent;

	@Before
	public void init() {
			MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportSimpleHierarchicalRelationships(){
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

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		ConceptHierarchicalRelationship r1 = new ConceptHierarchicalRelationship();
		r1.setRole(ConceptHierarchicalRelationshipRoleEnum.TGTS.getStatus());

		Mockito.when(conceptHierarchicalRelationshipService.getByChildAndParentIds("http://c1", "http://c2")).thenReturn(r1);

		conceptSKOS = factory.getSKOSConcept(URI.create("http://c1"));
		parent = factory.getSKOSConcept(URI.create("http://c2"));

		List<SKOSChange> skosChanges = skosHierarchicalRelationshipExporter
				.exportHierarchicalRelationships(parent, factory, conceptSKOS,
						scheme, vocab);

		Assert.assertEquals(2, skosChanges.size());
	}

	@Test
	public void testExportHierarchicalRelationships(){
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

		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");

		ConceptHierarchicalRelationship r1 = new ConceptHierarchicalRelationship();
		r1.setRole(ConceptHierarchicalRelationshipRoleEnum.TGITSI.getStatus());

		Mockito.when(conceptHierarchicalRelationshipService.getByChildAndParentIds("http://c1", "http://c2")).thenReturn(r1);

		conceptSKOS = factory.getSKOSConcept(URI.create("http://c1"));
		parent = factory.getSKOSConcept(URI.create("http://c2"));

		List<SKOSChange> skosChanges = skosHierarchicalRelationshipExporter
				.exportHierarchicalRelationships(parent, factory, conceptSKOS,
						scheme, vocab);

		Assert.assertEquals(4, skosChanges.size());
	}
}