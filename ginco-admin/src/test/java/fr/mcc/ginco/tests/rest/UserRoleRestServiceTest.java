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

package fr.mcc.ginco.tests.rest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.UserRole;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.RoleView;
import fr.mcc.ginco.extjs.view.pojo.UserRoleView;
import fr.mcc.ginco.extjs.view.utils.UserRoleViewConverter;
import fr.mcc.ginco.rest.services.UserRoleRestService;
import fr.mcc.ginco.services.IUserRoleService;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class UserRoleRestServiceTest {

	@Mock(name = "userRoleService")
	private IUserRoleService userRoleService;

	@Mock(name = "userRoleViewConverter")
	private UserRoleViewConverter userRoleViewConverter;

	@InjectMocks
	private UserRoleRestService userRoleRestService = new UserRoleRestService();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(userRoleRestService);
	}

	@Test
	public final void testGetThesaurusUsers() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://thesaurus1");

		List<UserRole> userRoles = new ArrayList<UserRole>();
		UserRole userRole1 = new UserRole();
		userRole1.setIdentifier(22);
		userRole1.setUsername("john");
		userRole1.setThesaurus(fakeThesaurus);
		userRole1.setRole(Role.EXPERT);
		userRoles.add(userRole1);
		UserRole userRole2 = new UserRole();
		userRole2.setIdentifier(23);
		userRole2.setUsername("paul");
		userRole2.setThesaurus(fakeThesaurus);
		userRole2.setRole(Role.MANAGER);
		userRoles.add(userRole2);

		UserRoleView userRoleView1 = new UserRoleView();
		userRoleView1.setIdentifier(22);
		userRoleView1.setUsername("john");
		userRoleView1.setThesaurusId("http://thesaurus1");
		userRoleView1.setRole(1);

		UserRoleView userRoleView2 = new UserRoleView();
		userRoleView2.setIdentifier(23);
		userRoleView2.setUsername("paul");
		userRoleView2.setThesaurusId("http://thesaurus1");
		userRoleView2.setRole(0);

		Mockito.when(userRoleService.getThesaurusUsers("http://thesaurus1"))
				.thenReturn(userRoles);
		Mockito.when(userRoleViewConverter.convert(userRole1)).thenReturn(
				userRoleView1);
		Mockito.when(userRoleViewConverter.convert(userRole2)).thenReturn(
				userRoleView2);

		ExtJsonFormLoadData<List<UserRoleView>> actualUserRoles = userRoleRestService
				.getThesaurusUsers("http://thesaurus1");
		Assert.assertEquals(2, actualUserRoles.getData().size());
		Assert.assertEquals(new Long(2), actualUserRoles.getTotal());
		ListAssert.assertContains(actualUserRoles.getData(), userRoleView1);
		ListAssert.assertContains(actualUserRoles.getData(), userRoleView2);

	}

	@Test
	public final void testGetAvailableRoles() {
		RoleView manager = new RoleView();
		manager.setRole(0);
		manager.setRoleLabel("Responsable opérationnel");

		RoleView expert = new RoleView();
		expert.setRole(1);
		expert.setRoleLabel("Expert");

		ExtJsonFormLoadData<List<RoleView>> availableRoles = userRoleRestService
				.getAvailableRoles();
		Assert.assertEquals(2, availableRoles.getData().size());
		Assert.assertEquals(new Long(2), availableRoles.getTotal());
		ListAssert.assertContains(availableRoles.getData(), manager);
		ListAssert.assertContains(availableRoles.getData(), expert);
	}

	@Test
	public final void testUpdateThesaurusUsers() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://thesaurus1");

		List<UserRoleView> userRoleViews = new ArrayList<UserRoleView>();
		UserRoleView userRoleView1 = new UserRoleView();
		userRoleView1.setIdentifier(22);
		userRoleView1.setUsername("john");
		userRoleView1.setThesaurusId("http://thesaurus1");
		userRoleView1.setRole(1);
		userRoleViews.add(userRoleView1);

		UserRoleView userRoleView2 = new UserRoleView();
		userRoleView2.setIdentifier(23);
		userRoleView2.setUsername("paul");
		userRoleView2.setThesaurusId("http://thesaurus1");
		userRoleView2.setRole(0);
		userRoleViews.add(userRoleView2);

		UserRole userRole1 = new UserRole();
		userRole1.setIdentifier(22);
		userRole1.setUsername("john");
		userRole1.setThesaurus(fakeThesaurus);
		userRole1.setRole(Role.EXPERT);

		UserRole userRole2 = new UserRole();
		userRole2.setIdentifier(23);
		userRole2.setUsername("paul");
		userRole2.setThesaurus(fakeThesaurus);
		userRole2.setRole(Role.MANAGER);

		Mockito.when(userRoleViewConverter.convert(userRoleView1)).thenReturn(
				userRole1);
		Mockito.when(userRoleViewConverter.convert(userRoleView2)).thenReturn(
				userRole2);

		Mockito.when(userRoleViewConverter.convert(userRole1)).thenReturn(
				userRoleView1);
		Mockito.when(userRoleViewConverter.convert(userRole2)).thenReturn(
				userRoleView2);

		ExtJsonFormLoadData<List<UserRoleView>> actualUserRoleView = userRoleRestService
				.updateThesaurusUsers(userRoleViews);

		Assert.assertEquals(2, actualUserRoleView.getData().size());
		Assert.assertEquals(new Long(2), actualUserRoleView.getTotal());
		ListAssert.assertContains(actualUserRoleView.getData(), userRoleView1);
		ListAssert.assertContains(actualUserRoleView.getData(), userRoleView2);

	}

}