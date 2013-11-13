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
package fr.mcc.ginco.rest.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.UserRole;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.RoleView;
import fr.mcc.ginco.extjs.view.pojo.UserRoleView;
import fr.mcc.ginco.extjs.view.utils.UserRoleViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IUserRoleService;
import fr.mcc.ginco.utils.LabelUtil;

/**
 * User and roles REST service
 * 
 */
@Service
@Path("/userroleservice")
@Produces({ MediaType.APPLICATION_JSON })
@PreAuthorize("isAuthenticated()")
public class UserRoleRestService {
	@Inject
	@Named("userRoleService")
	private IUserRoleService userRoleService;

	@Inject
	@Named("userRoleViewConverter")
	private UserRoleViewConverter userRoleViewConverter;	

	/**
	 * Method to get the list of the thesaurus users
	 * s
	 * @return
	 */
	@GET
	@Path("/getThesaurusUsers")
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<UserRoleView>> getThesaurusUsers(
			@QueryParam("idThesaurus") String idThesaurus) {
		List<UserRoleView> userRoleViews = new ArrayList<UserRoleView>();

		List<UserRole> userRoles = userRoleService
				.getThesaurusUsers(idThesaurus);
		for (UserRole userRole : userRoles) {
			UserRoleView userRoleView = userRoleViewConverter.convert(userRole);
			userRoleViews.add(userRoleView);
		}		
		return new ExtJsonFormLoadData<List<UserRoleView>>(userRoleViews, userRoleViews.size());
	}

	/**
	 * Public method to get available roles
	 * 
	 * @throws BusinessException
	 */
	@GET
	@Path("/getAllRoles")
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<RoleView>> getAvailableRoles()
			throws BusinessException {
		List<RoleView> roleViews = new ArrayList<RoleView>();

		for (Role role : Role.values()) {
			RoleView roleView = new RoleView();
			roleView.setRole(role.getIdentifier());
			roleView.setRoleLabel(LabelUtil.getResourceLabel("userrole["
					+ role.getIdentifier() + "]"));
			roleViews.add(roleView);
		}

		ExtJsonFormLoadData<List<RoleView>> result = new ExtJsonFormLoadData<List<RoleView>>(
				roleViews, roleViews.size());
		return result;
	}

	@POST
	@Path("/updateThesaurusUsers")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#userRoleViews, '0')")
	public ExtJsonFormLoadData<List<UserRoleView>> updateThesaurusUsers(
			List<UserRoleView> userRoleViews) throws BusinessException {

		Map<String, UserRole> userRoles = new HashMap<String, UserRole>();
		List<UserRoleView> updatedUserRoleViews = new ArrayList<UserRoleView>();

		for (UserRoleView userRoleView : userRoleViews) {
			if (!userRoles.keySet().contains(userRoleView.getUsername())) {
				UserRole userRole = userRoleViewConverter.convert(userRoleView);
				userRoles.put(userRoleView.getUsername(), userRole);
			} else {
				 throw new BusinessException("Duplicate username on the same thesaurus : " + userRoleView.getUsername() + " - " + userRoleView.getThesaurusId(), "duplicate-username-on-thesaurus");
			}
		}

		List<UserRole> updatedUserRoles = new ArrayList<UserRole>();

		for (UserRole userRole : userRoles.values()) {
			UserRole updatedUserRole = userRoleService.updateUserRole(userRole);
			updatedUserRoles.add(updatedUserRole);
			UserRoleView userRoleView = userRoleViewConverter
					.convert(updatedUserRole);
			updatedUserRoleViews.add(userRoleView);
		}

		return new ExtJsonFormLoadData<List<UserRoleView>>(userRoleViews, userRoleViews.size());

	}

	
	@POST
	@Path("/deleteThesaurusUsers")
	@Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasPermission(#userRoleViews, '0')")
	public void deleteThesaurusUsers(
			List<UserRoleView> userRoleViews)
			throws BusinessException {
		for (UserRoleView userRoleView : userRoleViews) {
			UserRole userRole = userRoleViewConverter.convert(userRoleView);
			userRoleService
					.deleteUserRole(userRole);
		}
	}
}
