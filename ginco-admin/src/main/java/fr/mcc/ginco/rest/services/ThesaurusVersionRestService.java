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
import java.util.List;
import java.util.MissingResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.GenericStatusView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusVersionHistoryView;
import fr.mcc.ginco.extjs.view.utils.ThesaurusVersionHistoryViewConverter;
import fr.mcc.ginco.services.IThesaurusVersionHistoryService;
import fr.mcc.ginco.utils.LabelUtil;

/**
 * Thesaurus Version REST service for all operation on thesauruses versions
 * 
 */
@Service
@Path("/thesaurusversionservice")
@Produces({ MediaType.APPLICATION_JSON })
@PreAuthorize("isAuthenticated()")
public class ThesaurusVersionRestService {	
	
	@Inject
	@Named("thesaurusVersionHistoryService")
	private IThesaurusVersionHistoryService thesaurusVersionHistoryService;
	
	@Inject
	@Named("thesaurusVersionHistoryViewConverter")
    private ThesaurusVersionHistoryViewConverter thesaurusVersionHistoryViewConverter;	
	
	
	/**
	 * Public method used to get the list of all versions of a thesaurus
	 * 
	 * @return list of ThesaurusVersionHistoryView objects for a thesaurus
	 */
	@GET
	@Path("/getVersions")
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<ThesaurusVersionHistoryView>> getVersions(
			@QueryParam("thesaurusId") String thesaurusId) {
		List<ThesaurusVersionHistory> thesaurusVersions = new ArrayList<ThesaurusVersionHistory>();
		thesaurusVersions = thesaurusVersionHistoryService.getVersionsByThesaurusId(thesaurusId);
		ExtJsonFormLoadData<List<ThesaurusVersionHistoryView>> result = new ExtJsonFormLoadData<List<ThesaurusVersionHistoryView>>(thesaurusVersionHistoryViewConverter.convertList(thesaurusVersions));
		result.setTotal((long)thesaurusVersions.size());
		return result;
	}
	
	/**
	 * Public method to get all status for concept (id + label)
	 * The types are read from a properties file
	 */
	@GET
	@Path("/getAllVersionStatus")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<GenericStatusView>> getAllVersionStatus() {
		List<GenericStatusView> listOfStatus = new ArrayList<GenericStatusView>();
		
		try {
			String availableStatusIds[] = LabelUtil.getResourceLabel("version-status").split(",");
			
			if (StringUtils.isEmpty(availableStatusIds[0])) {
				//Ids of status for concepts are not set correctly
				throw new BusinessException("Error with property file - check values of identifier version status", "check-values-of-version-status");
			}
			
	        for (String id : availableStatusIds) {
	        	GenericStatusView versionStatusView = new GenericStatusView();
	        	versionStatusView.setStatus(Integer.valueOf(id));
	        	
	        	String label = LabelUtil.getResourceLabel("version-status["+ id +"]");
	        	if (label.isEmpty()) {
	        		//Labels of status are not set correctly
	        		throw new BusinessException("Error with property file - check values of identifier version status", "check-values-of-version-status");
				} else {
					versionStatusView.setStatusLabel(label);
				}
	        	listOfStatus.add(versionStatusView);
			}
		} catch (MissingResourceException e) {
			throw new BusinessException("Error with property file - check values of version status", "check-values-of-version-status", e);
		}
		ExtJsonFormLoadData<List<GenericStatusView>> result = new ExtJsonFormLoadData<List<GenericStatusView>>(listOfStatus);
        result.setTotal((long) listOfStatus.size());
		return result;
	}
	
	/**
	 * Public method used to create new thesaurus versions
	 */
	@POST
	@Path("/updateVersions")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#versionViews, '0')")
	public ExtJsonFormLoadData<List<ThesaurusVersionHistoryView>> updateVersions(List<ThesaurusVersionHistoryView> versionViews) {
	
		List<ThesaurusVersionHistory> versions = thesaurusVersionHistoryViewConverter.convertViewList(versionViews);
		List<ThesaurusVersionHistory> resultVersions = new ArrayList<ThesaurusVersionHistory>() ;
		
		for (ThesaurusVersionHistory thesaurusVersionHistory : versions) {
			resultVersions.add(thesaurusVersionHistoryService.createOrUpdateVersion(thesaurusVersionHistory));
		}
		return new ExtJsonFormLoadData<List<ThesaurusVersionHistoryView>>(thesaurusVersionHistoryViewConverter.convertList(resultVersions));		
	}

}