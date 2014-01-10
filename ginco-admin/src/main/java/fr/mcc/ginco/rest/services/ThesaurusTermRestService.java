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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.GenericStatusView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.services.IUserRoleService;
import fr.mcc.ginco.solr.IConceptIndexerService;
import fr.mcc.ginco.solr.ITermIndexerService;
import fr.mcc.ginco.utils.DateUtil;
import fr.mcc.ginco.utils.LabelUtil;

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
	@Named("languagesService")
	private ILanguagesService languagesService;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

    @Inject
    @Named("termViewConverter")
    private TermViewConverter termViewConverter;

    @Inject
	@Named("termIndexerService")
	private ITermIndexerService termIndexerService;
    
    @Inject
	@Named("conceptIndexerService")
	private IConceptIndexerService conceptIndexerService;

    @Inject
	@Named("userRoleService")
	private IUserRoleService userRoleService;

	private Logger logger  = LoggerFactory.getLogger(ThesaurusTermRestService.class);


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
     @QueryParam("onlyValidatedTerms") Boolean onlyValidatedTerms) {
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
			results.add(termViewConverter.convert(thesaurusTerm));
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
     @QueryParam("idThesaurus") String idThesaurus) {
		logger.info("Getting Thesaurus Preferred Terms with following parameters : " + "index start " +startIndex + " with a limit of " + limit );
		List<ThesaurusTerm> thesaurusTerms = new ArrayList<ThesaurusTerm>();
		thesaurusTerms = thesaurusTermService.getPaginatedThesaurusPreferredTermsList(startIndex, limit, idThesaurus);
		Long total = thesaurusTermService.getPreferredTermsCount(idThesaurus);
		List<ThesaurusTermView>results = new ArrayList<ThesaurusTermView>();
		for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
			results.add(termViewConverter.convert(thesaurusTerm));
		}
		ExtJsonFormLoadData<List<ThesaurusTermView> > extTerms = new  ExtJsonFormLoadData<List<ThesaurusTermView> > (results);
		extTerms.setTotal(total);
		return extTerms;
	}

	/**
	 * Public method used to get the details of a single ThesaurusTerm
	 *
	 * @return list of ThesaurusTermView, if not found - {@code null}
	 */
	@GET
	@Path("/getThesaurusTerm")
	@Produces({MediaType.APPLICATION_JSON})
	public ThesaurusTermView getThesaurusTerm(@QueryParam("id") String idTerm) {
		ThesaurusTerm thesaurusTerm = thesaurusTermService.getThesaurusTermById(idTerm);
        return termViewConverter.convert(thesaurusTerm, true);
	}

	/**
	 * Public method used to create or update
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView} -
     * thesaurus term JSON object send by extjs
	 *
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView} updated object
	 *         in JSON format or {@code null} if not found
	 */
	@POST
	@Path("/updateTerm")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#thesaurusViewJAXBElement, '0') or hasPermission(#thesaurusViewJAXBElement, '1')")
	public ThesaurusTermView updateTerm(ThesaurusTermView thesaurusViewJAXBElement) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String username = auth.getName();

		if (userRoleService.hasRole(username, thesaurusViewJAXBElement.getThesaurusId(), Role.EXPERT)) {

			try {
				ThesaurusTerm existingTerm = thesaurusTermService.getThesaurusTermById(thesaurusViewJAXBElement.getIdentifier());
				if (existingTerm !=null) {
					ThesaurusConcept attachedConcept = existingTerm.getConcept(); 
					if (attachedConcept == null) {
						if (thesaurusViewJAXBElement.getStatus() != TermStatusEnum.CANDIDATE.getStatus()) {
							throw new AccessDeniedException("You can save only candidate terms");
						}
						if ( existingTerm.getStatus() != TermStatusEnum.CANDIDATE.getStatus()) {
							throw new AccessDeniedException("You can save only candidate terms");
						}
					} else 
					{
						if (attachedConcept.getStatus() != ConceptStatusEnum.CANDIDATE.getStatus()) {
							throw new AccessDeniedException("You can save only terms attached to candidate concepts");
						}
					}
				} else 
				{
					if (thesaurusViewJAXBElement.getStatus() != TermStatusEnum.CANDIDATE.getStatus()) {
						throw new AccessDeniedException("You can save only candidate terms");
					}
				}
			} catch (BusinessException be) {
				//Do nothing, term just doen't exist
				logger.debug("Case of term creation detected");
			}
		}
		ThesaurusTerm object = termViewConverter.convert(thesaurusViewJAXBElement, false);

		if (object != null) {
			ThesaurusTerm result = thesaurusTermService.updateThesaurusTerm(object);
			if (result != null) {
				termIndexerService.addTerm(object);
				if (result.getConcept()!=null && result.getPrefered()==true)
				{
					conceptIndexerService.addConcept(result.getConcept());
				}
				return termViewConverter.convert(result);
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
	 */
	@POST
	@Path("/destroyTerm")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#thesaurusViewJAXBElement, '0')")
	public ThesaurusTermView destroyTerm(ThesaurusTermView thesaurusViewJAXBElement){
		ThesaurusTerm object =thesaurusTermService
				.getThesaurusTermById(thesaurusViewJAXBElement.getIdentifier());
		if (object != null) {
			object.setModified(DateUtil.nowDate());
			ThesaurusTerm result = thesaurusTermService.destroyThesaurusTerm(object);
			termIndexerService.removeTerm(object);
			return termViewConverter.convert(result);
		}
		return null;
	}

	/**
	 * Public method to get all term status for terms (id + label)
	 * The types are read from a properties file
	 */
	@GET
	@Path("/getAllTermStatus")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<GenericStatusView>> getAllTermStatus() {

		List<GenericStatusView> listOfStatus = new ArrayList<GenericStatusView>();
		try {
			String availableStatusIds[] = LabelUtil.getResourceLabel("term-status").split(",");

			if (StringUtils.isEmpty(availableStatusIds[0])) {
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
	
	@GET
	@Path("/checkTermUnicity")
	@Produces({MediaType.APPLICATION_JSON})
	public Boolean checkTermUnicity(@QueryParam("idThesaurus") String idThesaurus,
			@QueryParam("lexicalValue") String lexicalValue,
			@QueryParam("lang") String lang)
	{
		Thesaurus thesaurus = thesaurusService.getThesaurusById(idThesaurus);
		if (thesaurus != null) {
			Language language = languagesService.getLanguageById(lang);
			if (language != null) {
				ThesaurusTerm term = new ThesaurusTerm();
				term.setThesaurus(thesaurus);
				term.setLanguage(language);
				term.setLexicalValue(lexicalValue);
				return !thesaurusTermService.isTermExist(term);
			} else 
			{
				throw new BusinessException("Unable to find Language", "unknown-language");
			}
		} else 
		{
			throw new BusinessException("Unable to find thesaurus", "unknown-thesaurus");
		}
	}
}