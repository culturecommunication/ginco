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

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.GenericStatusView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IIndexerService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.EncodedControl;
import fr.mcc.ginco.utils.LabelUtil;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Thesaurus Term REST service for all operations on Thesauruses Terms
 * 
 */
@Service
@Path("/thesaurustermservice")
@Produces({MediaType.APPLICATION_JSON})
@PreAuthorize("isAuthenticated()")
public class ThesaurusTermRestService {	
	
	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;
	
    @Inject
    @Named("termViewConverter")
    private TermViewConverter termViewConverter;
    
    @Inject
    @Named("indexerService")
    private IIndexerService indexerService;
    
	@Log
	private Logger logger;

	/**
	 * Public method used to get list of all existing Thesaurus terms objects in
	 * database.
	 * 
	 * @return list of ThesaurusTermView, if not found - {@code null}
	 */
	@GET
	@Path("/getSandboxedThesaurusTerms")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<ThesaurusTermView> > getSandboxedThesaurusTerms
    (@QueryParam("start") Integer startIndex,
     @QueryParam("limit") Integer limit,
     @QueryParam("idThesaurus") String idThesaurus,
     @QueryParam("onlyValidatedTerms") Boolean onlyValidatedTerms) throws BusinessException{
		logger.info("Getting Thesaurus Sandboxed Terms with following parameters : " + "index start " +startIndex + " with a limit of " + limit + "and a onlyValidatedTerms parameter set to " + onlyValidatedTerms);
		List<ThesaurusTerm> thesaurusTerms = new ArrayList<ThesaurusTerm>();
		if (onlyValidatedTerms){
			thesaurusTerms = thesaurusTermService.getPaginatedThesaurusSandoxedValidatedTermsList(startIndex, limit, idThesaurus);	
		} else {
			thesaurusTerms = thesaurusTermService.getPaginatedThesaurusSandoxedTermsList(startIndex, limit, idThesaurus);
		}
		
		Long total = null;
		if (onlyValidatedTerms){
			total = thesaurusTermService.getSandboxedValidatedTermsCount(idThesaurus);
		} else {
			total = thesaurusTermService.getSandboxedTermsCount(idThesaurus);
		}
		
		List<ThesaurusTermView>results = new ArrayList<ThesaurusTermView>();
		for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
			results.add(new ThesaurusTermView(thesaurusTerm));
		}
		ExtJsonFormLoadData<List<ThesaurusTermView> > extTerms = new  ExtJsonFormLoadData<List<ThesaurusTermView> > (results);
		extTerms.setTotal(total);
		return extTerms;
	}
	
	/**
	 * Public method used to get list of all existing Thesaurus terms objects in
	 * database.
	 * 
	 * @return list of ThesaurusTermView, if not found - {@code null}
	 */
	@GET
	@Path("/getPreferredThesaurusTerms")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<ThesaurusTermView> > getPreferredThesaurusTerms
    (@QueryParam("start") Integer startIndex,
     @QueryParam("limit") Integer limit,
     @QueryParam("idThesaurus") String idThesaurus) throws BusinessException{
		logger.info("Getting Thesaurus Preferred Terms with following parameters : " + "index start " +startIndex + " with a limit of " + limit );
		List<ThesaurusTerm> thesaurusTerms = new ArrayList<ThesaurusTerm>();
		thesaurusTerms = thesaurusTermService.getPaginatedThesaurusPreferredTermsList(startIndex, limit, idThesaurus);	
		Long total = thesaurusTermService.getPreferredTermsCount(idThesaurus);
		
		List<ThesaurusTermView>results = new ArrayList<ThesaurusTermView>();
		for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
			results.add(new ThesaurusTermView(thesaurusTerm));
		}
		ExtJsonFormLoadData<List<ThesaurusTermView> > extTerms = new  ExtJsonFormLoadData<List<ThesaurusTermView> > (results);
		extTerms.setTotal(total);
		return extTerms;
	}
	
	/**
	 * Public method used to get the details of a single ThesaurusTerm
	 * 
	 * @return list of ThesaurusTermView, if not found - {@code null}
	 * @throws BusinessException 
	 */
	@GET
	@Path("/getThesaurusTerm")
	@Produces({MediaType.APPLICATION_JSON})
	public ThesaurusTermView getThesaurusTerm(@QueryParam("id") String idTerm) throws BusinessException {
		ThesaurusTerm thesaurusTerm = thesaurusTermService.getThesaurusTermById(idTerm);
        return new ThesaurusTermView(thesaurusTerm);
	}

	/**
	 * Public method used to create or update
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView} -
     * thesaurus term JSON object send by extjs
	 * 
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView} updated object
	 *         in JSON format or {@code null} if not found
	 * @throws BusinessException 
	 */
	@POST
	@Path("/updateTerm")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ThesaurusTermView updateTerm(ThesaurusTermView thesaurusViewJAXBElement)
            throws BusinessException, TechnicalException {

		ThesaurusTerm object = termViewConverter.convert(thesaurusViewJAXBElement, false);
		
		if (object != null) {
			ThesaurusTerm result = thesaurusTermService.updateThesaurusTerm(object);
			if (result != null) {
				indexerService.addTerm(object);
				return new ThesaurusTermView(result);
			} else {
				logger.error("Failed to update thesaurus term");
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Public method used to delete
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView} -
     * thesaurus term JSON object send by extjs
     *
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView} deleted object
	 *         in JSON format or {@code null} if not found
	 * @throws BusinessException 
	 */
	@POST
	@Path("/destroyTerm")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ThesaurusTermView destroyTerm(ThesaurusTermView thesaurusViewJAXBElement) throws BusinessException {
		ThesaurusTerm object = termViewConverter.convert(thesaurusViewJAXBElement, false);
	
		if (object != null) {
			ThesaurusTerm result = thesaurusTermService.destroyThesaurusTerm(object);
			return new ThesaurusTermView(result);
		}
		return null;
	}
	
	/**
	 * Public method to get all term status for terms (id + label)
	 * The types are read from a properties file
	 * @throws BusinessException 
	 */
	@GET
	@Path("/getAllTermStatus")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<GenericStatusView>> getAllTermStatus() throws BusinessException {

		List<GenericStatusView> listOfStatus = new ArrayList<GenericStatusView>();
		try {
			String availableStatusIds[] = LabelUtil.getResourceLabel("term-status").split(",");
			
			if ("".equals(availableStatusIds[0])) {
				//Ids of status for terms are not set correctly
				throw new BusinessException("Error with property file - check values of identifier term status", "check-values-of-term-status");
			}
			
	        for (String id : availableStatusIds) {
	        	GenericStatusView termStatusView = new GenericStatusView();
	        	termStatusView.setStatus(Integer.valueOf(id));
	        	
	        	String label = LabelUtil.getResourceLabel("term-status["+ id +"]");
	        	if (label.isEmpty()) {
	        		//Labels of status are not set correctly
	        		throw new BusinessException("Error with property file - check values of identifier term status", "check-values-of-term-status");
				} else {
					termStatusView.setStatusLabel(label);
				}
	        	listOfStatus.add(termStatusView);
			}
		} catch (MissingResourceException e) {
			throw new BusinessException("Error with property file - check values of term status", "check-values-of-term-status", e);
		}
		ExtJsonFormLoadData<List<GenericStatusView>> result = new ExtJsonFormLoadData<List<GenericStatusView>>(listOfStatus);
        result.setTotal((long) listOfStatus.size());
		return result;
	}
}