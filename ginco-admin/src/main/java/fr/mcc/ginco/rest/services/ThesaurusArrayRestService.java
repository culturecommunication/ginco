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

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView;
import fr.mcc.ginco.extjs.view.utils.NodeLabelViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusArrayViewConverter;
import fr.mcc.ginco.services.IThesaurusArrayService;

/**
 * Service contains methods, exposed to user to control ThesaurusArray objects.
 */
@Service
@Path("/thesaurusarrayservice")
@Produces({ MediaType.APPLICATION_JSON })
@PreAuthorize("isAuthenticated()")
public class ThesaurusArrayRestService {

	@Inject
	@Named("thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;

	@Inject
	@Named("nodeLabelViewConverter")
	private NodeLabelViewConverter nodeLabelViewConverter;

	@Inject
	@Named("thesaurusArrayViewConverter")
	private ThesaurusArrayViewConverter thesaurusArrayViewConverter;

	/**
	 * Public method used to get
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView} object by
	 * providing its id.
	 * 
	 * @param thesaurusArrayId
	 *            {@link String} identifier to try with
	 * 
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView} object in
	 *         JSON format or {@code null} if not found
	 * @throws BusinessException
	 */
	@GET
	@Path("/getArray")
	@Produces({ MediaType.APPLICATION_JSON })
	public ThesaurusArrayView getThesaurusArrayById(
			@QueryParam("id") String thesaurusArrayId) throws BusinessException {
		return thesaurusArrayViewConverter.convert(thesaurusArrayService
				.getThesaurusArrayById(thesaurusArrayId));
	}

    /**
     * Public method used to create or update a concept.
     * @param thesaurusArrayViewJAXBElement element to create/update.
     * @return newly created object.
     * @throws BusinessException in case of error.
     */
	@POST
	@Path("/updateArray")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ThesaurusArrayView updateThesaurusArray(
			ThesaurusArrayView thesaurusArrayViewJAXBElement)
	
			throws BusinessException {

		NodeLabel nodeLabel = nodeLabelViewConverter
				.convert(thesaurusArrayViewJAXBElement);
		
		ThesaurusArray convertedArray = thesaurusArrayViewConverter
				.convert(thesaurusArrayViewJAXBElement);
		

		ThesaurusArray updated = thesaurusArrayService.updateThesaurusArray(
				convertedArray, nodeLabel);
		return thesaurusArrayViewConverter.convert(updated);
	}
	
	  /**
		 * Public method used to delete
		 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView} -
	     * thesaurus term JSON object send by extjs
	     *
		 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView} deleted object
		 *         in JSON format or {@code null} if not found
		 * @throws BusinessException 
		 */
		@POST
		@Path("/destroyArray")
		@Consumes({ MediaType.APPLICATION_JSON })
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		public void destroyArray(ThesaurusArrayView thesaurusArrayViewJAXBElement) throws BusinessException {
			ThesaurusArray object = thesaurusArrayViewConverter.convert(thesaurusArrayViewJAXBElement);
		
			if (object != null) {
				thesaurusArrayService.destroyThesaurusArray(object);
			}
		}
}
