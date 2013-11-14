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
package fr.mcc.ginco.tests.extjs.view.utils;

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
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.UserRoleView;
import fr.mcc.ginco.extjs.view.utils.UserRoleViewConverter;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IUserRoleService;

public class UserRoleViewConverterTest {

	@Mock(name = "thesaurusService")
	private IThesaurusService thesaurusService;

	@Mock(name = "userRoleService")
	private IUserRoleService userRoleService;

	@InjectMocks
	private UserRoleViewConverter converter = new UserRoleViewConverter();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertExistingUserRoleView() throws BusinessException {
		UserRoleView userRoleView = new UserRoleView();
		userRoleView.setIdentifier(1);
		userRoleView.setUsername("john");
		userRoleView.setThesaurusId("http://thesaurus1");
		userRoleView.setRole(1);

		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setTitle("The thesaurus");
		UserRole existingUserRole = new UserRole();
		existingUserRole.setIdentifier(1);

		Mockito.when(userRoleService.getUserRole(1)).thenReturn(
				existingUserRole);

		Mockito.when(thesaurusService.getThesaurusById("http://thesaurus1"))
				.thenReturn(fakeThesaurus);

		UserRole actualUserRole = converter.convert(userRoleView);

		Assert.assertEquals(new Integer(1), actualUserRole.getIdentifier());
		Assert.assertEquals("The thesaurus", actualUserRole.getThesaurus()
				.getTitle());
		Assert.assertEquals(Role.EXPERT, actualUserRole.getRole());
		Assert.assertEquals("john", actualUserRole.getUsername());

	}

	@Test
	public void testConvertNewUserRoleView() throws BusinessException {
		UserRoleView userRoleView = new UserRoleView();
		userRoleView.setIdentifier(0);
		userRoleView.setUsername("john");
		userRoleView.setThesaurusId("http://thesaurus1");
		userRoleView.setRole(1);

		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setTitle("The thesaurus");

		Mockito.when(thesaurusService.getThesaurusById("http://thesaurus1"))
				.thenReturn(fakeThesaurus);

		UserRole actualUserRole = converter.convert(userRoleView);

		Assert.assertEquals("The thesaurus", actualUserRole.getThesaurus()
				.getTitle());
		Assert.assertEquals(Role.EXPERT, actualUserRole.getRole());
		Assert.assertEquals("john", actualUserRole.getUsername());

	}

	@Test
	public void testConvertUserRole() throws BusinessException {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://thesaurus1");

		UserRole userRole = new UserRole();
		userRole.setIdentifier(22);
		userRole.setUsername("john");
		userRole.setThesaurus(fakeThesaurus);
		userRole.setRole(Role.EXPERT);

		UserRoleView actualUserRoleView = converter.convert(userRole);

		Assert.assertEquals("john", actualUserRoleView.getUsername());
		Assert.assertEquals(1, actualUserRoleView.getRole().intValue());
		Assert.assertEquals("http://thesaurus1",
				actualUserRoleView.getThesaurusId());
		Assert.assertEquals(22, actualUserRoleView.getIdentifier().intValue());

	}
}
