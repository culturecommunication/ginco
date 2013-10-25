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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.UserRole;
import fr.mcc.ginco.dao.IUserRoleDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.UserRoleServiceImpl;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class UserRoleServiceTest {

	@Mock(name = "userRoleDAO")
	private IUserRoleDAO userRoleDAO;

	@InjectMocks
	private UserRoleServiceImpl userRoleService = new UserRoleServiceImpl();

	@Before
	public void init() {
		LoggerTestUtil.initLogger(userRoleService);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public final void testHasRole() throws BusinessException {
		UserRole userRole = new UserRole();
		userRole.setIdentifier(1);
		userRole.setRole(Role.EXPERT);
		Mockito.when(
				userRoleDAO.getUserRoleOnThesaurus("username1", "thesaurus1"))
				.thenReturn(userRole);
		boolean hasRole = userRoleService.hasRole("username1", "thesaurus1",
				Role.EXPERT);
		Assert.assertTrue(hasRole);

		Mockito.when(
				userRoleDAO.getUserRoleOnThesaurus("username2", "thesaurus2"))
				.thenReturn(null);
		boolean hasRole2 = userRoleService.hasRole("username2", "thesaurus2",
				Role.EXPERT);
		Assert.assertFalse(hasRole2);
	}

	@Test
	public final void testUpdateNewUserRole() throws BusinessException {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus2");
		UserRole userRole = new UserRole();
		userRole.setIdentifier(1);
		userRole.setRole(Role.EXPERT);
		userRole.setUsername("username2");
		userRole.setThesaurus(fakeThesaurus);
		Mockito.when(
				userRoleDAO.getUserRoleOnThesaurus("username2", "thesaurus2"))
				.thenReturn(null);
		Mockito.when(userRoleDAO.update(userRole)).thenReturn(userRole);

		UserRole actual = userRoleService.updateUserRole(userRole);
		Assert.assertEquals(userRole, actual);

	}

	@Test(expected = BusinessException.class)
	public final void testUpdateExistingUserRole() throws BusinessException {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus2");
		UserRole userRole = new UserRole();
		userRole.setIdentifier(1);
		userRole.setRole(Role.EXPERT);
		userRole.setUsername("username2");
		userRole.setThesaurus(fakeThesaurus);

		UserRole userRole2 = new UserRole();
		userRole2.setIdentifier(2);

		Mockito.when(
				userRoleDAO.getUserRoleOnThesaurus("username2", "thesaurus2"))
				.thenReturn(userRole2);

		userRoleService.updateUserRole(userRole);

	}

}