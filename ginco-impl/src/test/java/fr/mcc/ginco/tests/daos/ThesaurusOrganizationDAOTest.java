package fr.mcc.ginco.tests.daos;

import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.dao.hibernate.ThesaurusOrganizationDAO;
import fr.mcc.ginco.tests.BaseDAOTest;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ThesaurusOrganizationDAOTest extends BaseDAOTest {

	private ThesaurusOrganizationDAO thesaurusOrganizationDAO;

	@Before
	public void handleSetUpOperation() throws Exception{
		super.handleSetUpOperation();
		thesaurusOrganizationDAO = new ThesaurusOrganizationDAO();
		thesaurusOrganizationDAO.setSessionFactory(getSessionFactory());
	}

	@Test
	public void testGetFilteredOrganizationNames() {
		List<ThesaurusOrganization> list = thesaurusOrganizationDAO.getFilteredOrganizationNames();
		Assert.assertEquals("Should contain only one organization.", 1, list.size());
		Assert.assertEquals("Should be the first organization", "Smile", list.get(0).getName());
	}

	@Override
	public String getXmlDataFileInit() {
		return "/thesaurus_init.xml";
	}
}
