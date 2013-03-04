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
package fr.mcc.ginco.tests.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.AssociativeRelationshipRole;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.ThesaurusConceptServiceImpl;
import fr.mcc.ginco.tests.BaseTest;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class ThesaurusConceptServiceTest extends BaseTest {

	@Mock(name = "thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Mock(name = "thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;
	
	@Mock(name = "associativeRelationshipDAO")
	private IGenericDAO<AssociativeRelationship,Class<?>> associativeRelationshipDAO;

	@InjectMocks
	private ThesaurusConceptServiceImpl thesaurusConceptService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(thesaurusConceptService);
	}

	@Test
	public final void testGetOrphanThesaurusConcepts() throws BusinessException {
		when(thesaurusDAO.getById(anyString())).thenReturn(new Thesaurus());
		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		when(
				thesaurusConceptDAO
						.getOrphansThesaurusConcept(any(Thesaurus.class)))
				.thenReturn(concepts);

		List<ThesaurusConcept> thesaurusRes = thesaurusConceptService
				.getOrphanThesaurusConcepts("any-thesaurus-id");
		Assert.assertNotNull("Error while getting Thesaurus By Id",
				thesaurusRes);
	}

	@Test(expected = BusinessException.class)
	public final void testGetOrphanThesaurusConceptsWithWrongThesaurusId()
			throws BusinessException {
		when(thesaurusDAO.getById(anyString())).thenReturn(null);
		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		when(
				thesaurusConceptDAO
						.getOrphansThesaurusConcept(any(Thesaurus.class)))
				.thenReturn(concepts);

		thesaurusConceptService
				.getOrphanThesaurusConcepts("any-thesaurus-id");
	}
	
	@Test
	public final void testAddAssociativeRelationship() {
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("id-concept-1");
		ThesaurusConcept concept2 = new ThesaurusConcept();
		concept2.setIdentifier("id-concept-2");
		AssociativeRelationshipRole role = new AssociativeRelationshipRole();
		
		AssociativeRelationship association1 = new AssociativeRelationship();
		when(associativeRelationshipDAO.makePersistent(any(AssociativeRelationship.class))).thenReturn(association1);
		AssociativeRelationship sh1 = thesaurusConceptService.addAssociativeRelationship(concept1, concept2, role);
		Assert.assertNotNull(sh1);
	}

}
