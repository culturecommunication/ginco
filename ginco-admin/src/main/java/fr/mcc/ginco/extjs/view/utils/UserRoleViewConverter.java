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
package fr.mcc.ginco.extjs.view.utils;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.UserRole;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.UserRoleView;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IUserRoleService;

/**
 * Small class responsible for converting real {@link UserRole} object into its
 * view {@link UserRoleView} and vice-versa.
 */
@Component("userRoleViewConverter")
public class UserRoleViewConverter {

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Inject
	@Named("userRoleService")
	private IUserRoleService userRoleService;

	private Logger logger  = LoggerFactory.getLogger(UserRoleViewConverter.class);


	/**
	 * Main method used to convert from {@link UserRole} to {@link UserRoleView}
	 * .
	 * 
	 * @param source
	 *            source to work with
	 * @return converted item.
	 * @throws BusinessException
	 */
	public UserRoleView convert(UserRole userRole) throws BusinessException {
		logger.debug("Converting UserRole " + userRole.getIdentifier()
				+ " to UserRoleView");
		UserRoleView userRoleView = new UserRoleView();
		userRoleView.setIdentifier(userRole.getIdentifier());
		userRoleView.setUsername(userRole.getUsername());
		userRoleView.setRole(userRole.getRole().getIdentifier());
		userRoleView.setThesaurusId(userRole.getThesaurus().getIdentifier());
		return userRoleView;
	}

	/**
	 * Main method used to convert from {@link UserRoleView} to {@link UserRole}
	 * .
	 * 
	 * @param userRoleView
	 * @return
	 */
	public UserRole convert(UserRoleView userRoleView) {
		logger.debug("Converting UserRoleView " + userRoleView.getUsername()
				+ " to UserRole");

		UserRole userRole;
		if (userRoleView.getIdentifier() == null || userRoleView.getIdentifier() == 0) {
			logger.debug("UserRole does not exist for user "
					+ userRoleView.getUsername() + ", creating one");
			userRole = new UserRole();
		} else {
			logger.debug("Found existing UserRole to update for user "
					+ userRoleView.getUsername());
			userRole = userRoleService
					.getUserRole(userRoleView.getIdentifier());
		}
		userRole.setThesaurus(thesaurusService.getThesaurusById(userRoleView
				.getThesaurusId()));
		userRole.setUsername(userRoleView.getUsername());
		for (Role role : Role.values()) {
			if (role.getIdentifier().equals(userRoleView.getRole())) {
				logger.debug("Found matching role " + role.name()
						+ " for role identifier " + userRoleView.getRole());
				userRole.setRole(role);
				break;
			}
		}

		return userRole;
	}

}
