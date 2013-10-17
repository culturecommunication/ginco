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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.AlignmentType;
import fr.mcc.ginco.beans.ExternalThesaurus;
import fr.mcc.ginco.beans.ExternalThesaurusType;
import fr.mcc.ginco.extjs.view.pojo.ExternalThesaurusView;
import fr.mcc.ginco.extjs.view.utils.ExternalThesaurusViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IAlignmentTypeService;
import fr.mcc.ginco.services.IExternalThesaurusService;
import fr.mcc.ginco.services.IExternalThesaurusTypeService;


/**
 * Thesaurus Concept REST service for all operation on a thesaurus' concepts
 *
 */
@Service
@Path("/thesaurusalignmentservice")
@Produces({ MediaType.APPLICATION_JSON })
@PreAuthorize("isAuthenticated()")
public class AlignmentsRestService {	

	@Inject
	@Named("alignmentTypeService")
	private IAlignmentTypeService alignmentTypeService;
	
	@Inject
	@Named("externalThesaurusTypeService")
	private IExternalThesaurusTypeService externalThesaurusTypeService;
	
	@Inject
	@Named("externalThesaurusService")
	private IExternalThesaurusService externalThesaurusService;

	@Inject
	@Named("externalThesaurusViewConverter")
	private ExternalThesaurusViewConverter externalThesaurusViewConverter;

	
	
   @Log
	private Logger logger;

	
	/**
	 * Method to get the list of alignment types
	 * @return
	 */
	@GET
	@Path("/getAlignmentTypes")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<AlignmentType> getAlignmentTypes() {       
		return alignmentTypeService.getAlignmentTypeList();
	}
	
	/**
	 * Method to get the list of external thesaurus types
	 * @return
	 */
	@GET
	@Path("/getExternalThesaurusTypes")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ExternalThesaurusType> getExternalThesaurusTypes() {       
		return externalThesaurusTypeService.getExternalThesaurusTypeList();
	}

	
	@GET
	@Path("/getExternalThesauruses")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ExternalThesaurusView> getExternalThesauruses() {       
		 List<ExternalThesaurusView> views = new ArrayList<ExternalThesaurusView>();
		 for (ExternalThesaurus externalThesaurus: externalThesaurusService.getExternalThesaurusList()) {
			 views.add(externalThesaurusViewConverter.convertExternalThesaurus(externalThesaurus));
		 }
		return views;
	}
}
