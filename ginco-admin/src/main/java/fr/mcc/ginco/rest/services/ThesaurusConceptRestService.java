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

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.GenericStatusView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptReducedView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView;
import fr.mcc.ginco.extjs.view.utils.ChildrenGenerator;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IIndexerService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.EncodedControl;
import org.apache.cxf.jaxrs.ext.Nullable;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Thesaurus Concept REST service for all operation on a thesaurus' concepts
 * 
 */
@Service
@Path("/thesaurusconceptservice")
@Produces({ MediaType.APPLICATION_JSON })
@PreAuthorize("isAuthenticated()")
public class ThesaurusConceptRestService {

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("termViewConverter")
	private TermViewConverter termViewConverter;

	@Inject
	@Named("thesaurusConceptViewConverter")
	private ThesaurusConceptViewConverter thesaurusConceptViewConverter;
	
	@Inject
    @Named("indexerService")
    private IIndexerService indexerService;

	@Log
	private Logger logger;

	/**
	 * Public method used to get
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView} object by
	 * providing its id.
	 * 
	 * @param conceptId
	 *            {@link String} identifier to try with
	 * 
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView} object
	 *         in JSON format or {@code null} if not found
	 * @throws BusinessException
	 */
	@GET
	@Path("/getConcept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ThesaurusConceptView getConceptById(
			@QueryParam("id") String conceptId) throws BusinessException {

        String resultId = "";

        if(conceptId.startsWith(ChildrenGenerator.ID_PREFIX)) {
            resultId = conceptId.substring(
                    conceptId.indexOf(ChildrenGenerator.PARENT_SEPARATOR)
                            + ChildrenGenerator.PARENT_SEPARATOR.length());
        } else {
        	resultId = conceptId;
        }

        List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
        logger.debug("Trying to load concept with id = " + resultId);
        
		terms = thesaurusTermService.getTermsByConceptId(resultId);
		return thesaurusConceptViewConverter.convert(
				thesaurusConceptService.getThesaurusConceptById(resultId),
				terms);
	}

	/**
	 * Public method used to create or update a concept
	 * 
	 * @throws BusinessException
	 */
	@POST
	@Path("/updateConcept")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ThesaurusConceptView updateConcept(
			ThesaurusConceptView thesaurusConceptViewJAXBElement)
			throws BusinessException, TechnicalException {

		ThesaurusConcept convertedConcept = thesaurusConceptViewConverter
				.convert(thesaurusConceptViewJAXBElement);

		List<ThesaurusTerm> terms = termViewConverter
				.convertTermViewsInTerms(thesaurusConceptViewJAXBElement
                        .getTerms(), true);
		logger.info("Number of converted terms : " + terms.size());

		List<ThesaurusTerm> preferedTerm = thesaurusTermService
				.getPreferedTerms(terms);

		// Business rule : a concept must have at least 1 term
		if (preferedTerm.size() == 0) {
			throw new BusinessException("A concept must have a prefered term",
					"missing-preferred-term-for-concept");
		}

		// Business rule : a concept mustn't have more than one prefered term
		if (preferedTerm.size() > 1) {
			throw new BusinessException(
					"A concept must have at only one prefered term",
					"to-many-preferred-terms-for-concept");
		}

		if (StringUtils.isNotEmpty(thesaurusConceptViewJAXBElement.getIdentifier())) {
				List<ThesaurusTerm> origin = thesaurusTermService
						.getTermsByConceptId(convertedConcept.getIdentifier());
				thesaurusTermService.markTermsAsSandboxed(terms, origin);
			
		}

		// We save or update the concept
		logger.info("Saving concept in DB");

		ThesaurusConcept returnConcept = thesaurusConceptService
				.updateThesaurusConcept(convertedConcept, terms, thesaurusConceptViewJAXBElement.getAssociatedConcepts());
		
		for (ThesaurusTerm term : terms) {
			indexerService.addTerm(term);
		}
		indexerService.addConcept(convertedConcept);
		// Return ThesaurusConceptView created/updated
		return thesaurusConceptViewConverter.convert(returnConcept, terms);
	}

    @GET
    @Path("/getConcepts")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<ThesaurusConceptReducedView> getConceptsByThesaurusId(
            @QueryParam("id") String conceptId,
            @QueryParam("thesaurusId") String thesaurusId,
            @QueryParam("searchOrphans") @Nullable String searchOrphans,
            @QueryParam("onlyValidatedConcepts") @Nullable Boolean onlyValidatedConcepts)
            throws BusinessException {

        Boolean searchOrphanParam;

        if(searchOrphans == null) {
            searchOrphanParam = null;
        } else if (searchOrphans.isEmpty()) {
            searchOrphanParam = null;
        } else {
            searchOrphanParam = Boolean.parseBoolean(searchOrphans);
        }
        
        boolean onlyValidated = onlyValidatedConcepts;
        if (onlyValidatedConcepts == null) {
        	//By default, if the value is not set, we return all concepts
        	onlyValidated = false;
        }

        return thesaurusConceptViewConverter
                .convert(thesaurusConceptService.getConceptsByThesaurusId(conceptId, thesaurusId,
                        searchOrphanParam, onlyValidated));
    }
    
    @GET
    @Path("/getSimpleConcepts")
    @Produces({ MediaType.APPLICATION_JSON })
    public  ExtJsonFormLoadData<List<ThesaurusConceptReducedView> > getSimpleConcepts(@QueryParam("conceptIds") List<String> associatedConcepts)
            throws BusinessException {     
    	List<ThesaurusConceptReducedView> reducedConcepts = new ArrayList<ThesaurusConceptReducedView>();
    	for (String conceptId: associatedConcepts) {
    		ThesaurusConcept concept = thesaurusConceptService.getThesaurusConceptById(conceptId);
    		reducedConcepts.add(thesaurusConceptViewConverter.convert(concept));
    	}
    	 return new ExtJsonFormLoadData<List<ThesaurusConceptReducedView>>(reducedConcepts); 
    }

    @GET
    @Path("/getSimpleChildrenConcepts")
    @Produces({ MediaType.APPLICATION_JSON })
    public  ExtJsonFormLoadData<List<ThesaurusConceptReducedView> > getSimpleChildrenConcepts(@QueryParam("conceptId") String conceptId)
            throws BusinessException {
        List<ThesaurusConceptReducedView> reducedConcepts = new ArrayList<ThesaurusConceptReducedView>();
        List<ThesaurusConcept> children = thesaurusConceptService.getChildrenByConceptId(conceptId);
        for (ThesaurusConcept child : children) {
            reducedConcepts.add(thesaurusConceptViewConverter.convert(child));
        }
        return new ExtJsonFormLoadData<List<ThesaurusConceptReducedView>>(reducedConcepts);
    }
    
    @GET
    @Path("/getAvailableConceptsOfArray")
    @Produces({ MediaType.APPLICATION_JSON })
    public  ExtJsonFormLoadData<List<ThesaurusConceptReducedView> > getAvailableConceptsOfArray(
    		@QueryParam("arrayId") String arrayId)
            throws BusinessException {
        List<ThesaurusConceptReducedView> reducedConcepts = new ArrayList<ThesaurusConceptReducedView>();
        List<ThesaurusConcept> children = thesaurusConceptService.getAvailableConceptsOfArray(arrayId);
        for (ThesaurusConcept child : children) {
            reducedConcepts.add(thesaurusConceptViewConverter.convert(child));
        }
        return new ExtJsonFormLoadData<List<ThesaurusConceptReducedView>>(reducedConcepts);
    }
    
    /**
	 * Public method used to delete
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView} -
     * thesaurus term JSON object send by extjs
     *
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView} deleted object
	 *         in JSON format or {@code null} if not found
	 * @throws BusinessException 
	 */
	@POST
	@Path("/destroyConcept")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void destroyConcept(ThesaurusConceptView thesaurusViewJAXBElement) throws BusinessException {
		ThesaurusConcept object = thesaurusConceptViewConverter.convert(thesaurusViewJAXBElement);
	
		if (object != null) {
			thesaurusConceptService.destroyThesaurusConcept(object);
		}
	}
	
	/**
	 * Public method to get all status for concept (id + label)
	 * The types are read from a properties file
	 * @throws BusinessException 
	 */
	@GET
	@Path("/getAllConceptStatus")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<GenericStatusView>> getAllConceptStatus() throws BusinessException {
		List<GenericStatusView> listOfStatus = new ArrayList<GenericStatusView>();
		
		try {
			ResourceBundle res = ResourceBundle.getBundle("labels", new EncodedControl("UTF-8"));
			String availableStatusIds[] = res.getString("concept-status").split(",");
			
			if ("".equals(availableStatusIds[0])) {
				//Ids of status for concepts are not set correctly
				throw new BusinessException("Error with property file - check values of identifier concept status", "check-values-of-concept-status");
			}
			
	        for (String id : availableStatusIds) {
	        	GenericStatusView conceptStatusView = new GenericStatusView();
	        	conceptStatusView.setStatus(Integer.valueOf(id));
	        	
	        	String label = res.getString("concept-status["+ id +"]");
	        	if (label.isEmpty()) {
	        		//Labels of status are not set correctly
	        		throw new BusinessException("Error with property file - check values of identifier concept status", "check-values-of-concept-status");
				} else {
					conceptStatusView.setStatusLabel(label);
				}
	        	listOfStatus.add(conceptStatusView);
			}
		} catch (MissingResourceException e) {
			throw new BusinessException("Error with property file - check values of concept status", "check-values-of-concept-status", e);
		}
		ExtJsonFormLoadData<List<GenericStatusView>> result = new ExtJsonFormLoadData<List<GenericStatusView>>(listOfStatus);
        result.setTotal((long) listOfStatus.size());
		return result;
	}
}
