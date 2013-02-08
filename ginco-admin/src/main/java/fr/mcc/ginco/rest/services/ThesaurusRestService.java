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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.ILanguagesService;
import fr.mcc.ginco.IThesaurusFormatService;
import fr.mcc.ginco.IThesaurusService;
import fr.mcc.ginco.IThesaurusTypeService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.log.Log;

/**
 * Thesaurus REST service for all operation on a unique thesaurus
 * 
 */
@Service
@Path("/thesaurusservice")
@Produces({MediaType.APPLICATION_JSON})
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
	 * Public method used to get list of all existing Language objects in
	 * database.
	 * 
	 * @return list of objects, if not found - {@code null}
	 */
	@GET
	@Path("/getLanguages")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<Language> > getAllLanguages(@QueryParam("start") Integer startIndex, @QueryParam("limit") Integer limit) {
		logger.info("Param passed to me : " + startIndex);
		logger.info("Param passed to me : " + limit);
		List<Language> languages = languagesService.getLanguagesList(startIndex, limit);
		Long total = languagesService.getLanguageCount();
		
		ExtJsonFormLoadData<List<Language> > extLanguages = new  ExtJsonFormLoadData<List<Language> > (languages);
		extLanguages.setTotal(total);
		return extLanguages;
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
     * Public method used to get {@link fr.mcc.ginco.beans.Thesaurus} object by providing its id.
     * @param id {@link String} identifier to try with
     *
     * @return {@link fr.mcc.ginco.beans.Thesaurus} object in JSON format or
     * {@code null} if not found
     */
    @GET
    @Path("/getVocabulary/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ThesaurusView getVocabularyById(@PathParam("id") String id) {
        return new ThesaurusView(thesaurusService.getThesaurusById(id));
    }
}
