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
package fr.mcc.ginco.tests.daos;

import java.util.List;

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.hibernate.AssociativeRelationshipDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.tests.BaseDAOTest;

public class AssociativeRelationshipDAOTest extends BaseDAOTest {

	private AssociativeRelationshipDAO associativeRelationshipDAO = new AssociativeRelationshipDAO();

	@Before
	public void handleSetUpOperation() throws Exception {
		super.handleSetUpOperation();
		associativeRelationshipDAO.setSessionFactory(getSessionFactory());
	}

	@Test
	public void testGetDefaultAssociativeRelationshipRole() {
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("http://www.culturecommunication.gouv.fr/co1");
		List<String> associatedConcepts =  associativeRelationshipDAO.getAssociatedConcepts(concept1);
		Assert.assertEquals(3, associatedConcepts.size());
		ListAssert.assertContains(associatedConcepts, "http://www.culturecommunication.gouv.fr/co2");
		ListAssert.assertContains(associatedConcepts, "http://www.culturecommunication.gouv.fr/co4");
		ListAssert.assertContains(associatedConcepts, "http://www.culturecommunication.gouv.fr/co6");

	}
	
	@Test
	public void testGetDefaultAssociativeRelationshipRoleStatus() {
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("http://www.culturecommunication.gouv.fr/co1");
		List<String> associatedConcepts =  associativeRelationshipDAO.getAssociatedConcepts(concept1, ConceptStatusEnum.DEPRECATED);
		Assert.assertEquals(1, associatedConcepts.size());
		ListAssert.assertContains(associatedConcepts, "http://www.culturecommunication.gouv.fr/co2");
	}

    @Test
    public void testGetRelationshipRole() {
        AssociativeRelationship associativeRelationship =
                associativeRelationshipDAO.getAssociativeRelationship(
                        "http://www.culturecommunication.gouv.fr/co1",
                        "http://www.culturecommunication.gouv.fr/co2");
        Assert.assertEquals("TA", associativeRelationship.getRelationshipRole().getCode());

        AssociativeRelationship associativeRelationship2 =
                associativeRelationshipDAO.getAssociativeRelationship(
                        "http://www.culturecommunication.gouv.fr/co2",
                        "http://www.culturecommunication.gouv.fr/co1");
        Assert.assertEquals("TA", associativeRelationship2.getRelationshipRole().getCode());
    }
	
	
	@Override
	public String getXmlDataFileInit() {
		return "/associativerelationship_init.xml";
	}
}
