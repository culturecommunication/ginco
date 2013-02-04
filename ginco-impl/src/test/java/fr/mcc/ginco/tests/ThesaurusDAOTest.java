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

import javax.inject.Inject;
import javax.sql.DataSource;

import fr.mcc.ginco.IThesaurusService;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml",
        "classpath:applicationContext-daos.xml"
})
@TransactionConfiguration
@Transactional
public class ThesaurusDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    IThesaurusService testVocabulary;
    
    @Inject
    private DataSource dataSourceTest;
    
	// needed to initialize DBUnit
	@Before
	public void handleSetUpOperation() throws Exception {
		// init db
		final IDatabaseConnection conn = new DatabaseDataSourceConnection(dataSourceTest);
		final IDataSet data = getDataset("/thesaurus_init.xml");
		try {
			DatabaseOperation.CLEAN_INSERT.execute(conn, data);
		} finally {
			conn.close();
		}
	}

	private IDataSet getDataset(String datasetPath) throws DataSetException {
		InputStream is = ThesaurusDAOTest.class.getResourceAsStream(datasetPath);
		return new XmlDataSet(is);
	}

    @Test
    public final void testGetThesaurusById() {
    	String idThesaurus = "0";
    	String expectedResponse = "test";
        String actualResponse = testVocabulary.getThesaurusById(idThesaurus).getTitle();
		Assert.assertEquals("Error while getting Thesaurus By Id !", expectedResponse, actualResponse);
    }
}
