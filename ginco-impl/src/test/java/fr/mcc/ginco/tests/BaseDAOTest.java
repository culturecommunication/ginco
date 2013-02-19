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
package fr.mcc.ginco.tests;

import java.io.InputStream;
import java.sql.Connection;

import javax.inject.Inject;
import javax.inject.Named;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

public abstract class BaseDAOTest extends BaseTest {

	@Inject
	@Named("gincoSessionFactory")
	private SessionFactory gincoSessionFactory;

	@Before
	public void handleSetUpOperation() throws Exception {
		// init db

		Connection jdbcConnection = SessionFactoryUtils.getDataSource(
				gincoSessionFactory).getConnection();
		final IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		final IDataSet dataset = getDataset(getXmlDataFileInit());
		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
		} finally {
			connection.close();
		}
	}

	@After
	public void handleCleanOperation() throws Exception {
		// init db
		Connection jdbcConnection = SessionFactoryUtils.getDataSource(
				gincoSessionFactory).getConnection();
		final IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		
		final IDataSet data = getDataset(getXmlDataFileInit());
		try {
			DatabaseOperation.DELETE_ALL.execute(connection, data);
		} finally {
			connection.close();
		}
	}

	public IDataSet getDataset(String datasetPath) throws DataSetException {
		InputStream is = BaseDAOTest.class.getResourceAsStream(datasetPath);
		return new XmlDataSet(is);
	}

	public SessionFactory getSessionFactory() {
		return this.gincoSessionFactory;
	}

	public abstract String getXmlDataFileInit();

}
