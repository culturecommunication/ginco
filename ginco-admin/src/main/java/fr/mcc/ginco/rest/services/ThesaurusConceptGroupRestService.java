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

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.beans.ThesaurusConceptGroupType;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptGroupView;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptGroupLabelViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptGroupViewConverter;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptGroupTypeService;

/**
 * Thesaurus Concept Group REST service for all operation on concept groups
 */
@Service
@Path("/thesaurusconceptgroupservice")
@Produces({ MediaType.APPLICATION_JSON })
@PreAuthorize("isAuthenticated()")
public class ThesaurusConceptGroupRestService {

	@Inject
	@Named("thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;

	@Inject
	@Named("thesaurusConceptGroupTypeService")
	private IThesaurusConceptGroupTypeService thesaurusConceptGroupTypeService;

	@Inject
	@Named("thesaurusConceptGroupViewConverter")
	private ThesaurusConceptGroupViewConverter thesaurusConceptGroupViewConverter;

	@Inject
	@Named("thesaurusConceptGroupLabelViewConverter")
	private ThesaurusConceptGroupLabelViewConverter thesaurusConceptGroupLabelViewConverter;


	/**
	 * Public method used to get the list of all concept groups types in the database.
	 *
	 * @return list of ThesaurusConceptGroupType objects for a concept, if not found - {@code null}
	 */
	@GET
	@Path("/getConceptGroupTypes")
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<ThesaurusConceptGroupType>> getConceptGroupTypes() {
		List<ThesaurusConceptGroupType> conceptGroupTypes;
		conceptGroupTypes = thesaurusConceptGroupTypeService.getConceptGroupTypeList();
		ExtJsonFormLoadData<List<ThesaurusConceptGroupType>> types = new ExtJsonFormLoadData<List<ThesaurusConceptGroupType>>(conceptGroupTypes);
		types.setTotal((long) conceptGroupTypes.size());
		return types;
	}

	/**
	 * Public method used to get a concept group.
	 *
	 * @return a concept group related to the id given in parameter
	 */
	@GET
	@Path("/getConceptGroup")
	@Produces({ MediaType.APPLICATION_JSON })
	public ThesaurusConceptGroupView getConceptGroupById(
			@QueryParam("id") String conceptGroupId) {
		return thesaurusConceptGroupViewConverter.convert(thesaurusConceptGroupService
				.getConceptGroupById(conceptGroupId));
	}

	/**
	 * Public method used to get all concept groups of a thesaurus without the ConceptGroup which Id is given in parameter (can be null).
	 *
	 * @return a list of concept groups related to the ids of the thesaurus and excluded concept group given in parameter
	 */
	@GET
	@Path("/getAllConceptGroups")
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<ThesaurusConceptGroupView>> getAllConceptGroupsByThesaurusId(
			@QueryParam("excludedConceptGroupId") String excludedConceptGroupId,
			@QueryParam("thesaurusId") String thesaurusId) {

		if (StringUtils.isEmpty(excludedConceptGroupId)) {
			excludedConceptGroupId = null;
		}
		List<ThesaurusConceptGroup> allGroups =
				thesaurusConceptGroupService.getAllThesaurusConceptGroupsByThesaurusId(excludedConceptGroupId, thesaurusId);
		ExtJsonFormLoadData<List<ThesaurusConceptGroupView>> groupViews =
				new ExtJsonFormLoadData<List<ThesaurusConceptGroupView>>(thesaurusConceptGroupViewConverter.convert(allGroups));
		groupViews.setTotal((long) allGroups.size());
		return groupViews;
	}


	/**
	 * Public method used to create or update a concept group.
	 *
	 * @param thesaurusConceptGroupViewJAXBElement element to create/update.
	 * @return newly created object.
	 */
	@POST
	@Path("/updateConceptGroup")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#thesaurusConceptGroupViewJAXBElement, '0')")
	public ThesaurusConceptGroupView updateThesaurusConceptGroup(
			ThesaurusConceptGroupView thesaurusConceptGroupViewJAXBElement) {

		ThesaurusConceptGroupLabel conceptGroupLabel =
				thesaurusConceptGroupLabelViewConverter.convert(thesaurusConceptGroupViewJAXBElement);

		ThesaurusConceptGroup convertedConceptGroup = thesaurusConceptGroupViewConverter
				.convert(thesaurusConceptGroupViewJAXBElement);

		ThesaurusConceptGroup updated = thesaurusConceptGroupService.updateThesaurusConceptGroup(
				convertedConceptGroup, conceptGroupLabel);

		return thesaurusConceptGroupViewConverter.convert(updated);
	}

	/**
	 * Public method used to delete a concept group.
	 *
	 * @param thesaurusConceptGroupViewJAXBElement element to delete
	 */
	@POST
	@Path("/destroyConceptGroup")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#thesaurusConceptGroupViewJAXBElement, '0')")
	public void destroyConceptGroup(ThesaurusConceptGroupView thesaurusConceptGroupViewJAXBElement) {
		ThesaurusConceptGroup object = thesaurusConceptGroupViewConverter.convert(thesaurusConceptGroupViewJAXBElement);
		if (object != null) {
			thesaurusConceptGroupService.destroyThesaurusConceptGroup(object);
		}
	}

}