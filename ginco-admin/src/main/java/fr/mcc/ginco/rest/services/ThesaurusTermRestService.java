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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import fr.mcc.ginco.IThesaurusConceptService;
import fr.mcc.ginco.beans.ThesaurusConcept;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import fr.mcc.ginco.ILanguagesService;
import fr.mcc.ginco.IThesaurusService;
import fr.mcc.ginco.IThesaurusTermService;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.beans.users.IUser;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.users.SimpleUserImpl;
import fr.mcc.ginco.utils.DateUtil;

/**
 * Thesaurus Term REST service for all operations on Thesauruses Terms
 * 
 */
@Service
@Path("/thesaurustermservice")
@Produces({MediaType.APPLICATION_JSON})
public class ThesaurusTermRestService {
	
	@Context 
	private MessageContext context;
	
	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;
	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;
	
    @Inject
    @Named("termViewConverter")
    private TermViewConverter termViewConverter;
    
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
	public ExtJsonFormLoadData<List<ThesaurusTermView> > getSandboxedThesaurusTerms(@QueryParam("start") Integer startIndex, @QueryParam("limit") Integer limit, @QueryParam("idThesaurus") String idThesaurus) {
		logger.info("Getting Thesaurus Sandboxed Terms with following parameters : " + "index start " +startIndex + " and limit of " + limit);
		List<ThesaurusTerm> thesaurusTerms = thesaurusTermService.getPaginatedThesaurusSandoxedTermsList(startIndex, limit, idThesaurus);
		Long total = thesaurusTermService.getSandboxedTermsCount(idThesaurus);
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
	public ThesaurusTermView updateTerm(ThesaurusTermView thesaurusViewJAXBElement) throws BusinessException {
		ThesaurusTerm object = termViewConverter.convert(thesaurusViewJAXBElement);
		String principal = "unknown";
		if (context != null) {
			principal = context.getHttpServletRequest().getRemoteAddr();
		}
		IUser user = new SimpleUserImpl();
		user.setName(principal);
		if (object != null) {
			ThesaurusTerm result;
			if (StringUtils.isEmpty(object.getIdentifier())) {
				result = thesaurusTermService.createThesaurusTerm(object, user);

			} else {
				result = thesaurusTermService.updateThesaurusTerm(object, user);
			}
			if (result != null) {
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
	public ThesaurusTermView destroyTerm(ThesaurusTermView thesaurusViewJAXBElement) throws BusinessException {
		ThesaurusTerm object = termViewConverter.convert(thesaurusViewJAXBElement);
		String principal = "unknown";
		if (context != null) {
			principal = context.getHttpServletRequest().getRemoteAddr();
		}
		IUser user = new SimpleUserImpl();
		user.setName(principal);
		if (object != null) {
			ThesaurusTerm result = thesaurusTermService.destroyThesaurusTerm(object, user);
			return new ThesaurusTermView(result);
		}
		return null;
	}
}