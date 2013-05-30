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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.dao.hibernate.ThesaurusArrayDAO;
import fr.mcc.ginco.tests.BaseDAOTest;

public class ThesaurusArrayDAOTest extends BaseDAOTest {

	private ThesaurusArrayDAO thesaurusArrayDAO = new ThesaurusArrayDAO();

	@Before
	public void handleSetUpOperation() throws Exception {
		super.handleSetUpOperation();
		thesaurusArrayDAO.setSessionFactory(getSessionFactory());
	}

	@Test
	public void testGetThesaurusArrayListByThesaurusId() {
		List<ThesaurusArray> arrays = thesaurusArrayDAO
				.getThesaurusArrayListByThesaurusId(null, "http://www.culturecommunication.gouv.fr/th1");
		Assert.assertEquals(5, arrays.size());
	}
	
	@Test
	public void testGetThesaurusArrayListByThesaurusIdWithExcludedArray() {
		List<ThesaurusArray> arrays = thesaurusArrayDAO
				.getThesaurusArrayListByThesaurusId("1", "http://www.culturecommunication.gouv.fr/th1");
		Assert.assertEquals(4, arrays.size());
	}

	/*
	 * //TODO : Hibernate issue - fix test
	 * 
	 * @Test public void testDelete() throws Exception{ // compare data set
	 * IDataSet expectedDataSet = getDataset("/thesaurusarray_afterdelete.xml");
	 * 
	 * ThesaurusArray array = thesaurusArrayDAO.getById("1");
	 * 
	 * thesaurusArrayDAO.delete(array); DataSource dataSource =
	 * SessionFactoryUtils.getDataSource(thesaurusArrayDAO.getSessionFactory());
	 * Connection con = DataSourceUtils.getConnection(dataSource);
	 * //getSessionFactory().getCurrentSession().flush();
	 * 
	 * IDatabaseConnection dbUnitCon = new DatabaseConnection(con); IDataSet
	 * databaseDataSet = dbUnitCon.createDataSet();
	 * 
	 * // compare data table ITable expectedTable =
	 * expectedDataSet.getTable("thesaurus_array"); ITable databaseTable =
	 * databaseDataSet.getTable("thesaurus_array");
	 * 
	 * Assertion.assertEquals(expectedTable, databaseTable);
	 * 
	 * }
	 */

	@Test
	public void testGetConceptSuperOrdinateArrays() {
		List<ThesaurusArray> arrays = thesaurusArrayDAO
				.getConceptSuperOrdinateArrays("http://www.culturecommunication.gouv.fr/co1");
		Assert.assertEquals(2, arrays.size());

	}

	@Test
	public void testGet() {
		ThesaurusArray array = thesaurusArrayDAO.getById("1");

		Assert.assertNotNull(array);

	}

	@Test
	public void testGetArraysWithoutSuperordinatedConcept() {
		List<ThesaurusArray> orphanArrays = thesaurusArrayDAO
				.getArraysWithoutSuperordinatedConcept("http://www.culturecommunication.gouv.fr/th1");
		Assert.assertEquals(2, orphanArrays.size());
		List<String> arrayIds = new ArrayList<String>();
		for (ThesaurusArray orphanArray : orphanArrays) {
			arrayIds.add(orphanArray.getIdentifier());
		}
		ListAssert.assertContains(arrayIds, "3");
		ListAssert.assertContains(arrayIds, "4");

	}

	@Test
	public void testGetConceptSuperOrdinateArraysWithExclude() {
		List<ThesaurusArray> arrays = thesaurusArrayDAO
				.getConceptSuperOrdinateArrays("http://www.culturecommunication.gouv.fr/co1", "5");
		Assert.assertEquals(1, arrays.size());

	}
	
	/*
	 * @Override
	 * 
	 * 
	 * public List<ThesaurusArray> getConceptSuperOrdinateArrays(String
	 * conceptId, String excludeConcept) {
	 * 
	 * Criteria criteria = getCriteriaByThesaurusAndTopConcept(conceptId);
	 * 
	 * criteria.add(Restrictions.ne("ta.identifier", excludeConcept)); 79 0
	 * 
	 * return criteria.list(); 80
	 * 
	 * 
	 * } 81
	 * 
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see fr.mcc.ginco.tests.BaseDAOTest#getXmlDataFileInit()
	 */

	@Override
	public String getXmlDataFileInit() {
		return "/thesaurusarray_init.xml";
	}

}
