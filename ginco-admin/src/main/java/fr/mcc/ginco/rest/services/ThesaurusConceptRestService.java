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

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptReducedView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.extjs.view.utils.ChildrenGenerator;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import org.apache.cxf.jaxrs.ext.Nullable;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


/**
 * Thesaurus Concept REST service for all operation on a thesaurus' concepts
 * 
 */
@Service
@Path("/thesaurusconceptservice")
@Produces({ MediaType.APPLICATION_JSON })
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
	public ThesaurusConceptView updateConcept(
			ThesaurusConceptView thesaurusConceptViewJAXBElement)
			throws BusinessException {

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

		if (StringUtils.isNotEmpty(convertedConcept.getIdentifier())) {
				List<ThesaurusTerm> origin = thesaurusTermService
						.getTermsByConceptId(convertedConcept.getIdentifier());
				thesaurusTermService.markTermsAsSandboxed(terms, origin);
			
		}

		// We save or update the concept
		logger.info("Saving concept in DB");

		ThesaurusConcept returnConcept = thesaurusConceptService
				.updateThesaurusConcept(convertedConcept, terms);

		// Return ThesaurusConceptView created/updated
		return thesaurusConceptViewConverter.convert(returnConcept, terms);
	}

    @GET
    @Path("/getConcepts")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<ThesaurusConceptReducedView> getConceptsByThesaurusId(
            @QueryParam("id") String conceptId,
            @QueryParam("thesaurusId") String thesaurusId,
            @QueryParam("searchOrphans") @Nullable String searchOrphans)
            throws BusinessException {

        Boolean searchOrphanParam;

        if(searchOrphans == null) {
            searchOrphanParam = null;
        } else if (searchOrphans.isEmpty()) {
            searchOrphanParam = null;
        } else searchOrphanParam = Boolean.parseBoolean(searchOrphans);

        return thesaurusConceptViewConverter
                .convert(thesaurusConceptService.getConceptsByThesaurusId(conceptId, thesaurusId,
                        searchOrphanParam));
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

}
