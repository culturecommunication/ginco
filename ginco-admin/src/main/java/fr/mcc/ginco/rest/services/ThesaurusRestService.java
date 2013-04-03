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
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.extjs.view.utils.ThesaurusViewConverter;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    @Named("thesaurusVersionHistoryService")
    private IThesaurusVersionHistoryService thesaurusVersionHistoryService;
	
	@Inject
	@Named("thesaurusViewConverter")
	private ThesaurusViewConverter thesaurusViewConverter;

	@Log
	private Logger logger;

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
		return thesaurusViewConverter.convert(thesaurusService.getThesaurusById(id));
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
    public void destroyVocabulary(ThesaurusView thesaurusViewJAXBElement) throws BusinessException {
        Thesaurus object = thesaurusViewConverter.convert(thesaurusViewJAXBElement);

        if (object != null) {
            thesaurusService.destroyThesaurus(object);
        }
    }


    /**
     * Public method used to publish thesaurus
     * @throws BusinessException
     */
    @GET
    @Path("/publishVocabulary")
    @Consumes({ MediaType.APPLICATION_JSON })
    public ExtJsonFormLoadData publishVocabulary(@QueryParam("thesaurusId") String thesaurusId) throws BusinessException {
        Thesaurus object = thesaurusService.getThesaurusById(thesaurusId);

        if (object != null) {
            thesaurusService.publishThesaurus(object);
            thesaurusVersionHistoryService.publishThesaurus(object);
        }

        return new ExtJsonFormLoadData(object);
    }
}
