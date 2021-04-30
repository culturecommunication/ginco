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

import java.util.ArrayList;
import java.util.List;

import junitx.framework.ListAssert;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.dao.hibernate.ThesaurusConceptDAO;
import fr.mcc.ginco.tests.BaseDAOTest;

public class ThesaurusConceptDAOTest extends BaseDAOTest {

	private ThesaurusConceptDAO thesaurusConceptDAO = new ThesaurusConceptDAO();

	@Before
	public void handleSetUpOperation() throws Exception {
		super.handleSetUpOperation();
		thesaurusConceptDAO.setSessionFactory(getSessionFactory());
		ReflectionTestUtils.setField(thesaurusConceptDAO, "defaultLang",
				"fr-FR");
	}

	@Test
	public void testGetOrphansThesaurusConcept() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thesaurusId);
		List<ThesaurusConcept> actualConcepts = thesaurusConceptDAO
				.getOrphansThesaurusConcept(th, 0);
		//MPL 0030087 09/08/2018 debut
		Assert.assertEquals(2, actualConcepts.size());
		//Assert.assertEquals(3, actualConcepts.size());
		//Assert.assertEquals(4, actualConcepts.size());
		//MPL 0030087 09/08/2018 fin
	}

	@Test
	public void testGetOrphansThesaurusConceptCount() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thesaurusId);
		long actualCount = thesaurusConceptDAO
				.getOrphansThesaurusConceptCount(th);
		Assert.assertEquals(2, actualCount);
	}

	@Test
	public void testGetTopTermThesaurusConcept() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thesaurusId);
		List<ThesaurusConcept> actualConcepts = thesaurusConceptDAO
				.getTopTermThesaurusConcept(th, 0,null);
		Assert.assertEquals(1, actualConcepts.size());
	}

	@Test
	public void testGetTopTermThesaurusConceptCount() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thesaurusId);
		long actualCount = thesaurusConceptDAO
				.getTopTermThesaurusConceptCount(th);
		Assert.assertEquals(1, actualCount);
	}

	@Test
	public void testGetThesaurusFromConcept() {
		String thesaurusConceptId = "http://www.culturecommunication.gouv.fr/co1";
		ThesaurusConcept thesaurusConcept = thesaurusConceptDAO
				.getById(thesaurusConceptId);
		String expectedThesaurusTitle = "test";
		Assert.assertEquals("Parent thesaurus is not as expected !",
				thesaurusConcept.getThesaurus().getTitle(),
				expectedThesaurusTitle);
	}

	//TODO move to associative relationships test
	/*@Test
	public void testGetAssociatedConcepts() {
		ThesaurusConcept concept1 = thesaurusConceptDAO
				.getById("http://www.culturecommunication.gouv.fr/co1");
		List<ThesaurusConcept> thesaurusConcepts = thesaurusConceptDAO
				.getAssociatedConcepts(concept1);
		Assert.assertEquals(2, thesaurusConcepts.size());
	}*/

	@Test
	public void testGetTopTermByThesaurus() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thesaurusId);

		List<ThesaurusConcept> list = thesaurusConceptDAO
				.getTopTermThesaurusConcept(th, 0,null);
		Assert.assertEquals(list.size(), 1);
	}

	@Test
	public void testGetChildrenByConcept() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thesaurusId);

		List<ThesaurusConcept> list = thesaurusConceptDAO
				.getChildrenConcepts("http://www.culturecommunication.gouv.fr/co2", 0,null);
		Assert.assertEquals(list.size(), 2);

		List<String> expectedIds = new ArrayList<String>();
		expectedIds.add("http://www.culturecommunication.gouv.fr/co2_1");
		expectedIds.add("http://www.culturecommunication.gouv.fr/co2_2");

		List<String> actualIds = new ArrayList<String>();
		for (ThesaurusConcept concept : list) {
			actualIds.add(concept.getIdentifier());
		}
		ListAssert.assertEquals(expectedIds, actualIds);
	}

	@Test
	public void testGetRootByThesaurusId() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Thesaurus th = new Thesaurus();
		th.setIdentifier(thesaurusId);

		List<ThesaurusConcept> list = thesaurusConceptDAO.getRootConcepts(
				th.getIdentifier(), false);
		Assert.assertEquals(1, list.size());

		List<ThesaurusConcept> listWithOrphans = thesaurusConceptDAO
				.getRootConcepts(th.getIdentifier(), null);
		//MPL 0030087 09/08/2018 debut
		Assert.assertEquals(5, listWithOrphans.size());
		//Assert.assertEquals(3, listWithOrphans.size());
		//MPL 0030087 09/08/2018 fin
		List<String> expectedOrphans = new ArrayList<String>();
		//MPL 0030087 09/08/2018 debut
		expectedOrphans.add("http://www.culturecommunication.gouv.fr/co1");
		expectedOrphans.add("http://www.culturecommunication.gouv.fr/co1");
		//MPL 0030087 09/08/2018 fin
		expectedOrphans.add("http://www.culturecommunication.gouv.fr/co1");
		expectedOrphans.add("http://www.culturecommunication.gouv.fr/co2");
		expectedOrphans.add("http://www.culturecommunication.gouv.fr/co3");

		List<String> actualOrphansIds = new ArrayList<String>();
		for (ThesaurusConcept concept : listWithOrphans) {
			actualOrphansIds.add(concept.getIdentifier());
		}
		ListAssert.assertEquals(expectedOrphans, actualOrphansIds);
		List<ThesaurusConcept> listOnlyOrphans = thesaurusConceptDAO
				.getRootConcepts(th.getIdentifier(), true);
		//MPL 0030087 09/08/2018 debut
		Assert.assertEquals(4, listOnlyOrphans.size());
		//Assert.assertEquals(2, listOnlyOrphans.size());
		//MPL 0030087 09/08/2018 fin
		List<String> expectedOnlyOrphans = new ArrayList<String>();
		//MPL 0030087 09/08/2018 debut
		expectedOnlyOrphans.add("http://www.culturecommunication.gouv.fr/co1");
		expectedOnlyOrphans.add("http://www.culturecommunication.gouv.fr/co1");
		//MPL 0030087 09/08/2018 fin
		expectedOnlyOrphans.add("http://www.culturecommunication.gouv.fr/co1");
		expectedOnlyOrphans.add("http://www.culturecommunication.gouv.fr/co3");

		List<String> actualOnlyOrphansIds = new ArrayList<String>();
		for (ThesaurusConcept concept : listOnlyOrphans) {
			actualOnlyOrphansIds.add(concept.getIdentifier());
		}
		ListAssert.assertEquals(expectedOnlyOrphans, actualOnlyOrphansIds);
	}

	@Test
	public void testGetAllConceptsByThesaurusId() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";

		List<ThesaurusConcept> list = thesaurusConceptDAO
				.getPaginatedConceptsByThesaurusId(0, 0, null, thesaurusId, null, false,null);
		Assert.assertEquals(5, list.size());

		List<ThesaurusConcept> listExclude = thesaurusConceptDAO
				.getPaginatedConceptsByThesaurusId(0, 0,
						"http://www.culturecommunication.gouv.fr/co1",
						thesaurusId, null, false,null);
		Assert.assertEquals(4, listExclude.size());

		List<ThesaurusConcept> listExcludeTopTerm = thesaurusConceptDAO
				.getPaginatedConceptsByThesaurusId(0, 0,
						"http://www.culturecommunication.gouv.fr/co1",
						thesaurusId, false, false,null);
		Assert.assertEquals(1, listExcludeTopTerm.size());

		/*List<ThesaurusConcept> listOnlyValidated = thesaurusConceptDAO
				.getAllConceptsByThesaurusId(
						"http://www.culturecommunication.gouv.fr/co3",
						thesaurusId, false, true);
		Assert.assertEquals(1, listOnlyValidated.size());*/
	}

	@Test
	public void testPaginatedConceptsByThesaurusId() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";

		List<ThesaurusConcept> listMaxResults = thesaurusConceptDAO
				.getPaginatedConceptsByThesaurusId(0, 3, null, thesaurusId, null, false,null);
		Assert.assertEquals(3, listMaxResults.size());

		List<ThesaurusConcept> listStartIndex = thesaurusConceptDAO
				.getPaginatedConceptsByThesaurusId(1, 0, null, thesaurusId, null, false,null);
		Assert.assertEquals(4, listStartIndex.size());
	}

	@Test
	public void testAlphabeticalOrderConceptsByThesaurusId() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";

		List<ThesaurusConcept> list = thesaurusConceptDAO
				.getPaginatedConceptsByThesaurusId(0, 0, null, thesaurusId, null, false,null);
		Assert.assertEquals("http://www.culturecommunication.gouv.fr/co2", list.get(0).getIdentifier());
	}



	@Test
	public void testCountConceptsAlignedToIntThes() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Long nbConceptsAlignedToIntThes = thesaurusConceptDAO
				.countConceptsAlignedToIntThes(thesaurusId);
		Assert.assertEquals(2, nbConceptsAlignedToIntThes.longValue());
	}
	@Test
	public void testCountConceptsAlignedToExtThes(){
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Long nbConceptsAlignedToExtThes = thesaurusConceptDAO
				.countConceptsAlignedToExtThes(thesaurusId);
		Assert.assertEquals(2, nbConceptsAlignedToExtThes.longValue());
	}
	@Test
	public void testCountConceptsAlignedToMyThes() {
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Long nbConceptsAlignedToMyThes = thesaurusConceptDAO
				.countConceptsAlignedToMyThes(thesaurusId);
		Assert.assertEquals(1, nbConceptsAlignedToMyThes.longValue());
	}

	@Test
	public void testCountConceptsWoNotes(){
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		Long nbConceptsWoNotes = thesaurusConceptDAO
				.countConceptsWoNotes(thesaurusId);
		Assert.assertEquals(4, nbConceptsWoNotes.longValue());
	}

	@Test
	public void testGetConceptsWoNotes(){
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		List<ThesaurusConcept> conceptsWoNotes = thesaurusConceptDAO
				.getConceptsWoNotes(thesaurusId, 0, 2);
		Assert.assertEquals(2, conceptsWoNotes.size());
		conceptsWoNotes = thesaurusConceptDAO
				.getConceptsWoNotes(thesaurusId, 0, 100);
		Assert.assertEquals(4, conceptsWoNotes.size());
	}

	@Test
	public void testGetConceptsAlignedToMyThes()
	{
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th1";
		List<ThesaurusConcept> conceptsAligned = thesaurusConceptDAO
				.getConceptsAlignedToMyThes(thesaurusId, 0, 100);
		Assert.assertEquals(1, conceptsAligned.size());
	}
	
	@Test
	public void testGetPaginatedAvailableConceptsOfGroup()
	{
		String thesaurusId = "http://www.culturecommunication.gouv.fr/th3";
		String groupId = "http://www.culturecommunication.gouv.fr/grp1";
		List<ThesaurusConcept> conceptList = thesaurusConceptDAO.getPaginatedAvailableConceptsOfGroup(0, 10, groupId, thesaurusId, true,null);
		Assert.assertEquals(1, conceptList.size());
		
	}

	@Override
	public String getXmlDataFileInit() {
		return "/thesaurusconcept_init.xml";
	}
}
