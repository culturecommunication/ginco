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

import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.SplitNonPreferredTermView;
import fr.mcc.ginco.extjs.view.utils.SplitNonPreferredTermViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;

/**
 * Thesaurus Term REST service for all operations on complex concepts
 * 
 */
@Service
@Path("/splitnonpreferredtermservice")
@Produces({MediaType.APPLICATION_JSON})
@PreAuthorize("isAuthenticated()")
public class SplitNonPreferredTermRestService {	
	
	@Inject
	@Named("splitNonPreferredTermService")
	private ISplitNonPreferredTermService splitNonPreferredTermService;
	
    @Inject
    @Named("splitNonPreferredTermViewConverter")
    private SplitNonPreferredTermViewConverter splitNonPreferredTermViewConverter;
   
    
	@Log
	private Logger logger;

	/**
	 * Public method used to get the details of a single {@link SplitNonPreferredTerm}
	 * 
	 * @return list of ThesaurusTermView, if not found - {@code null}
	 * @throws BusinessException 
	 */
	@GET
	@Path("/getTerm")
	@Produces({MediaType.APPLICATION_JSON})
	public SplitNonPreferredTermView getTerm(@QueryParam("id") String idTerm) throws BusinessException {
		SplitNonPreferredTerm thesaurusTerm = splitNonPreferredTermService.getSplitNonPreferredTermById(idTerm);
        return new SplitNonPreferredTermView(thesaurusTerm);
	}
	
	/**
	 * Public method used to create or update
	 * {@link fr.mcc.ginco.extjs.view.pojo.SplitNonPreferredTermView} -
     * thesaurus term JSON object send by extjs
	 * 
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.SplitNonPreferredTermView} updated object
	 *         in JSON format or {@code null} if not found
	 * @throws BusinessException 
	 */
	@POST
	@Path("/updateTerm")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#splitTermView, '0')")
	public SplitNonPreferredTermView updateTerm(SplitNonPreferredTermView splitTermView)
            throws BusinessException, TechnicalException {

		SplitNonPreferredTerm object = splitNonPreferredTermViewConverter.convert(splitTermView);
		
		if (object != null) {
			SplitNonPreferredTerm result = splitNonPreferredTermService.updateSplitNonPreferredTerm(object);
			if (result != null) {
				return new SplitNonPreferredTermView(result);
			} else {
				logger.error("Failed to update thesaurus term");
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Public method used to get list of all existing Thesaurus terms objects in
	 * database.
	 * 
	 * @return list of ThesaurusTermView, if not found - {@code null}
	 */
	@GET
	@Path("/getList")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<SplitNonPreferredTermView> > getList
    (@QueryParam("start") Integer startIndex,
     @QueryParam("limit") Integer limit,
     @QueryParam("idThesaurus") String idThesaurus) throws BusinessException{
		logger.info("Getting Thesaurus SplitNonPreferred Terms with following parameters : " + "index start " +startIndex + " with a limit of " + limit );
		List<SplitNonPreferredTerm> thesaurusTerms = splitNonPreferredTermService.getSplitNonPreferredTermList(startIndex, limit, idThesaurus);	
		Long total = splitNonPreferredTermService.getSplitNonPreferredTermCount(idThesaurus);

		List<SplitNonPreferredTermView>results = new ArrayList<SplitNonPreferredTermView>();
		for (SplitNonPreferredTerm thesaurusTerm : thesaurusTerms) {
			results.add(new SplitNonPreferredTermView(thesaurusTerm));
		}
		ExtJsonFormLoadData<List<SplitNonPreferredTermView> > extTerms = new  ExtJsonFormLoadData<List<SplitNonPreferredTermView> > (results);
		extTerms.setTotal(total);
		return extTerms;
	}
	
	/**
	 * Public method used to delete
	 * {@link fr.mcc.ginco.extjs.view.pojo.SplitNonPreferredTermView} -
     * thesaurus term JSON object send by extjs
     *
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.SplitNonPreferredTermView} deleted object
	 *         in JSON format or {@code null} if not found
	 * @throws BusinessException 
	 */
	@POST
	@Path("/destroyTerm")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#element, '0')")
	public void destroyTerm(SplitNonPreferredTermView element) throws BusinessException {
		SplitNonPreferredTerm object = splitNonPreferredTermViewConverter.convert(element);
	
		if (object != null) {
			splitNonPreferredTermService.destroySplitNonPreferredTerm(object);
		}
	}
	
	
}