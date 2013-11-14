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

import java.io.IOException;
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.extjs.view.utils.ThesaurusViewConverter;
import fr.mcc.ginco.services.IIndexerService;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusFormatService;
import fr.mcc.ginco.services.IThesaurusOrganizationService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusStatisticsService;
import fr.mcc.ginco.services.IThesaurusTypeService;
import fr.mcc.ginco.services.IThesaurusVersionHistoryService;

/**
 * Thesaurus REST service for all operation on a unique thesaurus
 *
 */
@Service
@Path("/thesaurusservice")
@Produces({ MediaType.APPLICATION_JSON })
@PreAuthorize("isAuthenticated()")
public class ThesaurusRestService {
	@Inject
	@Named("thesaurusTypeService")
	private IThesaurusTypeService thesaurusTypeService;

	@Inject
	@Named("thesaurusFormatService")
	private IThesaurusFormatService thesaurusFormatService;

	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("thesaurusStatisticsService")
	private IThesaurusStatisticsService thesaurusStatisticsService;

    @Inject
    @Named("thesaurusOrganizationService")
    private IThesaurusOrganizationService thesaurusOrganizationService;

    @Inject
    @Named("thesaurusVersionHistoryService")
    private IThesaurusVersionHistoryService thesaurusVersionHistoryService;

	@Inject
	@Named("thesaurusViewConverter")
	private ThesaurusViewConverter thesaurusViewConverter;

    @Inject
    @Named("indexerService")
    private IIndexerService indexerService;

	private Logger logger  = LoggerFactory.getLogger(ThesaurusRestService.class);


	/**
	 * Public method used to get list of all existing ThesaurusType objects in
	 * database.
	 *
	 * @return list of objects, if not found - {@code null}
	 */
	@GET
	@Path("/getThesaurusTypes")
	public List<ThesaurusType> getAllThesaurusTypes() {
		return thesaurusTypeService.getThesaurusTypeList();
	}

	/**
	 * Public method used to get list of existing top Languages in the database.
	 *
	 * @return list of objects, if not found - {@code null}
	 * @throws BusinessException
	 */
	@GET
	@Path("/getTopLanguages")
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<Language>> getTopLanguages(
			@QueryParam("thesaurusId") String thesaurusId) throws BusinessException {
		logger.info("Getting Top Languages");
		logger.info("thesaurusId = " + thesaurusId);

		List<Language> topLanguages;
		if (StringUtils.isNotEmpty(thesaurusId)) {
			topLanguages = thesaurusService.getThesaurusLanguages(thesaurusId);
		} else {
			topLanguages = languagesService.getTopLanguagesList();
		}
		return new ExtJsonFormLoadData<List<Language>>(topLanguages);

	}

	/**
	 * Public method used to get list of all existing ThesaurusFormat objects in
	 * database.
	 *
	 * @return list of objects, if not found - {@code null}
	 */
	@GET
	@Path("/getThesaurusFormats")
	public List<ThesaurusFormat> getAllThesaurusFormats() {
		return thesaurusFormatService.getThesaurusFormatList();
	}

	/**
	 * Public method used to get
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView} object by providing
	 * its id.
	 *
	 * @param id
	 *            {@link String} identifier to try with
	 *
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView} object in JSON
	 *         format or {@code null} if not found
	 */
	@GET
	@Path("/getVocabulary")
	@Produces({ MediaType.APPLICATION_JSON })
	public ThesaurusView getVocabularyById(@QueryParam("id") String id) {
			if (!id.isEmpty()){
				return thesaurusViewConverter.convert(thesaurusService.getThesaurusById(id));
			}
			else{
				return thesaurusViewConverter.convert(thesaurusService.getDefaultThesaurus());
			}
	}

	@GET
	@Path("/getVocabularies")
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<ThesaurusView>> getVocabularies() {
		List<ThesaurusView> listOfThesaurusView = new ArrayList<ThesaurusView>();
		List<Thesaurus> thesList = thesaurusService.getThesaurusList();
		for (Thesaurus thes : thesList) {
			listOfThesaurusView.add(thesaurusViewConverter.convert(thes));
		}
		ExtJsonFormLoadData<List<ThesaurusView>> result = new ExtJsonFormLoadData<List<ThesaurusView>>(listOfThesaurusView);
        result.setTotal((long) listOfThesaurusView.size());
		return result;
	}

	/**
	 * Public method used to create or update
	 * {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView}
     * thesaurus JSON object send by extjs.
     *
	 * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView} updated object
	 *         in JSON format or {@code null} if not found
	 */
	@POST
	@Path("/updateVocabulary")
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("hasPermission(#thesaurusViewJAXBElement, '0')")
	public ThesaurusView updateVocabulary(ThesaurusView thesaurusViewJAXBElement) throws BusinessException {
		Thesaurus object = thesaurusViewConverter.convert(thesaurusViewJAXBElement);

		ThesaurusView view = null;
        if (object != null) {
			Thesaurus result = thesaurusService.updateThesaurus(object);

            if (result != null) {
                view = thesaurusViewConverter.convert(result);
			} else {
				logger.error("Failed to update thesaurus");
			}
		}
		return view;
	}

    /**
     * Public method used to delete thesaurus
     * @throws BusinessException
     */
    @POST
    @Path("/destroyVocabulary")
    @Consumes({ MediaType.APPLICATION_JSON })
    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#thesaurusViewJAXBElement, 'DELETION')")
    public void destroyVocabulary(ThesaurusView thesaurusViewJAXBElement) throws BusinessException {
        Thesaurus object = thesaurusViewConverter.convert(thesaurusViewJAXBElement);

        if (object != null) {
            thesaurusService.destroyThesaurus(object);
            try {
            indexerService.removeThesaurusIndex(object.getIdentifier());
            } catch (TechnicalException tex)
            {
            	logger.error("Problem when removing thesaurus index...",tex);
            }
        }
    }


    /**
     * Public method used to publish thesaurus
     * @throws BusinessException
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @GET
    @Path("/publishVocabulary")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.TEXT_HTML)
	@PreAuthorize("hasPermission(#thesaurusId, '0')")
    public String publishVocabulary(@QueryParam("thesaurusId") String thesaurusId,
                                                 @QueryParam("userId") String userId)
            throws BusinessException, JsonGenerationException, JsonMappingException, IOException {
        Thesaurus object = thesaurusService.getThesaurusById(thesaurusId);

        if (object != null) {
            thesaurusService.publishThesaurus(object);
            thesaurusVersionHistoryService.publishThesaurus(object, userId);
        }

        ObjectMapper mapper = new ObjectMapper();
		String serialized = mapper.writeValueAsString(new ExtJsonFormLoadData(
				object));
		return StringEscapeUtils.unescapeHtml4(serialized);       
    }

    /**
     * Public method used to archive thesaurus
     * @throws BusinessException
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @GET
    @Path("/archiveVocabulary")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.TEXT_HTML)
	@PreAuthorize("hasPermission(#thesaurusId, '0')")
    public String archiveVocabulary(@QueryParam("thesaurusId") String thesaurusId)
            throws BusinessException, JsonGenerationException, JsonMappingException, IOException {
        Thesaurus object = thesaurusService.getThesaurusById(thesaurusId);

        ThesaurusView view = null;

        if (object != null) {         

            Thesaurus result =  thesaurusService.archiveThesaurus(object);

            if (result != null) {
                view = thesaurusViewConverter.convert(result);
            } else {
                logger.error("Failed to archive thesaurus");
            }
        }

        ObjectMapper mapper = new ObjectMapper();
		String serialized = mapper.writeValueAsString(new ExtJsonFormLoadData(
				view));
		return StringEscapeUtils.unescapeHtml4(serialized);       
    }

    @GET
    @Path("/getAllAuthors")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<ThesaurusOrganization> getAllAuthors() throws BusinessException {
    	List<ThesaurusOrganization> allOrgs = thesaurusOrganizationService.getOrganizations();
    	List<String> returnedAuthorNames = new ArrayList<String>();
    	List<ThesaurusOrganization> returnedOrgs = new ArrayList<ThesaurusOrganization>();
    	for (ThesaurusOrganization aOrg : allOrgs)
    	{
    		if (!StringUtils.isEmpty(aOrg.getName()) && !returnedAuthorNames.contains(aOrg.getName()))
    		{
    			returnedOrgs.add(aOrg);
    			returnedAuthorNames.add(aOrg.getName());
    		}
    	}
    	return returnedOrgs;
    }
    
    @GET
    @Path("/getStatistics")
    @Produces({ MediaType.APPLICATION_JSON })
    public ExtJsonFormLoadData getStatistics(@QueryParam("id") String thesaurusId) throws BusinessException {
    	return new ExtJsonFormLoadData(thesaurusStatisticsService.getStatistics(thesaurusId));
    }
}
