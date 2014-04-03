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
package fr.mcc.ginco.services;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.UserRole;
import fr.mcc.ginco.dao.IUserRoleDAO;
import fr.mcc.ginco.exceptions.BusinessException;

@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("userRoleService")
public class UserRoleServiceImpl implements IUserRoleService {

	private static Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);

	@Inject
	private IUserRoleDAO userRoleDAO;

	@Override
	public boolean hasRole(String username, String thesaurusId, Role role) {
		UserRole userRole = userRoleDAO.getUserRoleOnThesaurus(username,
				thesaurusId);
		if (userRole == null) {
			return false;
		} else {
			return userRole.getRole().equals(role);
		}
	}

	@Override
	public List<UserRole> getThesaurusUsers(String thesaurusId) {
		return userRoleDAO.getUserRolesOnThesaurus(thesaurusId);
	}

	@Override
	public UserRole getUserRole(Integer identifier) {
		return userRoleDAO.getById(identifier);
	}

	@Override
	@Transactional(readOnly = false)
	public UserRole updateUserRole(UserRole userRole) {
		UserRole existingUserRole = userRoleDAO
				.getUserRoleOnThesaurus(userRole.getUsername(), userRole
						.getThesaurus().getIdentifier());
		if (existingUserRole == null || existingUserRole.equals(userRole)) {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			String userName = auth.getName();
			logger.info("[updateUserRole] " + userName + " - User " + userRole.getUsername()
					+ " has now role " + userRole.getRole() + " on thesaurus "
					+ userRole.getThesaurus().getIdentifier() + " (" + userRole.getThesaurus().getTitle() + ")");
			return userRoleDAO.update(userRole);
		} else {
			throw new BusinessException(
					"Duplicate username on the same thesaurus : "
							+ userRole.getUsername() + " - "
							+ userRole.getThesaurus().getIdentifier(),
					"duplicate-username-on-thesaurus"
			);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteUserRole(UserRole userRole) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String userName = auth.getName();
		logger.info("[deleteUserRole] " + userName + " - User " + userRole.getUsername()
				+ " has no more role " + userRole.getRole() + " on thesaurus "
				+ userRole.getThesaurus().getIdentifier() + " (" + userRole.getThesaurus().getTitle() + ")");
		userRoleDAO.delete(userRole);
	}

	@Override
	public List<UserRole> getUserRoles(String username) {
		return userRoleDAO.getUserRoles(username);
	}

}
