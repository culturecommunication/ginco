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
package fr.mcc.ginco.security;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.extjs.view.pojo.SecuredResourceView;
import fr.mcc.ginco.rest.services.exceptions.ThesaurusArchivedAccessDeniedException;
import fr.mcc.ginco.services.IAdminUserService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IUserRoleService;

public class BasePermissionEvaluator implements PermissionEvaluator {

	@Inject
	@Named("userRoleService")
	private IUserRoleService userRoleService;

	@Inject
	@Named("adminUserService")
	private IAdminUserService adminUserService;
	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	private Logger log = LoggerFactory.getLogger(BasePermissionEvaluator.class);

	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		if (targetDomainObject instanceof List) {
			List targetObjects = (List) targetDomainObject;
			if (targetObjects != null && !targetObjects.isEmpty()) {
				if (!(targetObjects.get(0) instanceof SecuredResourceView)) {
					log.error("Permission exception : trying to apply hasPermission to a non SecuredResourceView or String object");
					return false;
				}
			}
		} else if (!(targetDomainObject instanceof SecuredResourceView || targetDomainObject instanceof String)) {
			log.error("Permission exception : trying to apply hasPermission to a non SecuredResourceView or String object");
			return false;
		}

		String scopeThesaurus = "";
		if (targetDomainObject instanceof SecuredResourceView) {
			SecuredResourceView viewObject = (SecuredResourceView) targetDomainObject;
			scopeThesaurus = viewObject.getThesaurusId();
		} else if (targetDomainObject instanceof List) {
			List targetObjects = (List) targetDomainObject;
			if (targetObjects != null && !targetObjects.isEmpty()) {
				SecuredResourceView viewObject = (SecuredResourceView) targetObjects
						.get(0);
				scopeThesaurus = viewObject.getThesaurusId();
			}
		} else {
			scopeThesaurus = (String) targetDomainObject;
		}
		
		Thesaurus thesaurusObject = thesaurusService.getThesaurusById(scopeThesaurus);
		if (thesaurusObject!= null && thesaurusObject.isArchived() != null && thesaurusObject.isArchived().booleanValue()) {
			if (!"DELETION".equals((String) permission)) {
					log.error("Permission denied : thesaurus is archived");
					throw new ThesaurusArchivedAccessDeniedException("Thesaurus is archived, only deletion is authorized");
			} else {
				return true;
			}
		}
		
		User curUser = (User) authentication.getPrincipal();

		log.debug("Checking permission " + permission + " on thesaurus "
				+ scopeThesaurus + " for user " + curUser.getUsername());

		if (adminUserService.isUserAdmin(curUser.getUsername())) {
			log.debug("User  " + curUser.getUsername()
					+ " is administrator, everything is possible");
			return true;
		}

		Role roleToCheck = null;

		for (Role role : Role.values()) {
			if (role.getIdentifier().toString().equals((String) permission)) {
				log.debug("Found matching role " + role.name()
						+ " for permission " + permission);
				roleToCheck = role;
				break;
			}
		}
		if (roleToCheck == null) {
			log.error("Permission exception : unknow role " + permission);
			return false;
		}

		return userRoleService.hasRole(curUser.getUsername(), scopeThesaurus,
				roleToCheck);

	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		log.error("Permission exception - not implemented : trying to apply the wrong haspermission method");
		return false;
	}

}
