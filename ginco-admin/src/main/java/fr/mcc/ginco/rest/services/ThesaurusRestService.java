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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.ILanguagesService;
import fr.mcc.ginco.IThesaurusFormatService;
import fr.mcc.ginco.IThesaurusOrganizationService;
import fr.mcc.ginco.IThesaurusService;
import fr.mcc.ginco.IThesaurusTypeService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.beans.users.IUser;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.users.SimpleUserImpl;
import fr.mcc.ginco.utils.DateUtil;

/**
 * Thesaurus REST service for all operation on a unique thesaurus
 * 
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
@Path("/thesaurusservice")
@Produces({MediaType.APPLICATION_JSON})
public class ThesaurusRestService {
	@Context 
	private MessageContext context;
	
	@Inject
	@Named("thesaurusTypeService")
	private IThesaurusTypeService thesaurusTypeService;
	
	@Inject
	@Named("thesaurusFormatService")
	private IThesaurusFormatService thesaurusFormatService;

    @Inject
    @Named("thesaurusOrganizationService")
    private IThesaurusOrganizationService thesaurusOrganizationService;
	
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
	@Path("/getAllLanguages")
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
	 * Public method used to get list of existing top Languages in the database.
	 * @return list of objects, if not found - {@code null}
	 */
	@GET
	@Path("/getTopLanguages")
	@Produces({MediaType.APPLICATION_JSON})
	public ExtJsonFormLoadData<List<Language> > getTopLanguages() {
		logger.info("Getting Top Languages");
		List<Language> topLanguages = languagesService.getTopLanguagesList();
		return new  ExtJsonFormLoadData<List<Language> > (topLanguages);
	}
	
	/**
	 * Public method used to get list of all existing ThesaurusFormat objects in database.
	 * @return list of objects, if not found - {@code null}
	 */
	@GET
	@Path("/getThesaurusFormats")
	public List<ThesaurusFormat> getAllThesaurusFormats() {
		return thesaurusFormatService.getThesaurusFormatList();
	}


    /**
     * Public method used to get {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView} object by providing its id.
     * @param id {@link String} identifier to try with
     *
     * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView} object in JSON format or
     * {@code null} if not found
     */
    @GET
    @Path("/getVocabulary")
    @Produces({MediaType.APPLICATION_JSON})
    public ThesaurusView getVocabularyById(@QueryParam("id") String id) {
        return new ThesaurusView(thesaurusService.getThesaurusById(id));
    }

    /**
     * Public method used to create or update {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView} object
     * @param id {@link ThesaurusView} thesaurus JSON object send by extjs
     *
     * @return {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusView} updated object in JSON format or
     * {@code null} if not found
     */
    @POST
    @Path("/updateVocabulary")
    @Consumes({MediaType.APPLICATION_JSON})
    public ThesaurusView updateVocabulary(ThesaurusView thesaurusViewJAXBElement) {
    	Thesaurus object = convert(thesaurusViewJAXBElement);
        String principal = context.getHttpServletRequest().getRemoteAddr();
        IUser user = new SimpleUserImpl();
        user.setName(principal); 
        if(object != null) {
        	Thesaurus result=thesaurusService.updateThesaurus(object);
        	if (result!=null)
        	{
        		return new ThesaurusView(result);
        	} else 
        	{
        		logger.error("Failed to update thesaurus");
        		return null;
        	}
        }
        return null;
    }


    private Thesaurus convert(ThesaurusView source) {
        Thesaurus hibernateRes;

        if(source.getId().equals("")) {
            hibernateRes = new Thesaurus();
            hibernateRes.setCreated(DateUtil.nowDate());
        } else {
            hibernateRes = thesaurusService.getThesaurusById(source.getId());
        }

        hibernateRes.setContributor(source.getContributor());
        hibernateRes.setCoverage(source.getCoverage());
        hibernateRes.setDate(DateUtil.nowDate());
        hibernateRes.setDescription(source.getDescription());
        hibernateRes.setPublisher(source.getPublisher());
        hibernateRes.setRelation(source.getRelation());
        hibernateRes.setRights(source.getRights());
        hibernateRes.setSource(source.getSource());
        hibernateRes.setSubject(source.getSubject());
        hibernateRes.setTitle(source.getTitle());
        hibernateRes.setFormat(thesaurusFormatService.getThesaurusFormatById(source.getFormat()));
        hibernateRes.setType(thesaurusTypeService.getThesaurusTypeById(source.getType()));
        
        if (thesaurusOrganizationService.getThesaurusOrganizationByNameAndURL(source.getCreatorName(), source.getCreatorHomepage()) == null)
        {
        	//ThesaurusOrganization given by the client is empty, so we drop any reference to ThesaurusOrganization
        	if (source.getCreatorName().isEmpty() && source.getCreatorHomepage().isEmpty()){
        		hibernateRes.setCreator(null);
        	}
        	//ThesaurusOrganization given by the client doesn't yet exist - need to create it (only if service or url <> null)
        	if (!source.getCreatorName().isEmpty() || !source.getCreatorHomepage().isEmpty()){
        	ThesaurusOrganization myThesaurusOrganization = new ThesaurusOrganization();
        	myThesaurusOrganization.setName(source.getCreatorName());
        	myThesaurusOrganization.setHomepage(source.getCreatorHomepage());
        	thesaurusOrganizationService.setThesaurusOrganizationPersistent(myThesaurusOrganization);
        	hibernateRes.setCreator(myThesaurusOrganization);
        	logger.info("Creating a new Thesaurus Organization");
        	}
        } else
        {
        	//Getting an existing ThesaurusOrganization
        	hibernateRes.setCreator(thesaurusOrganizationService.getThesaurusOrganizationByNameAndURL(source.getCreatorName(), source.getCreatorHomepage()));
        	logger.info("Getting an existing Thesaurus Organization");
        }

        List<String> languages = source.getLanguages();
        Set<Language> realLanguages = new HashSet<Language>();

        for(String language : languages) {
            Language lang = languagesService.getLanguageById(language);
            if(lang != null) {
                realLanguages.add(lang);
            }
        }

        hibernateRes.setLang(realLanguages);

        return hibernateRes;
    }

	public IThesaurusTypeService getThesaurusTypeService() {
		return thesaurusTypeService;
	}

	public void setThesaurusTypeService(IThesaurusTypeService thesaurusTypeService) {
		this.thesaurusTypeService = thesaurusTypeService;
	}

	public IThesaurusFormatService getThesaurusFormatService() {
		return thesaurusFormatService;
	}

	public void setThesaurusFormatService(
			IThesaurusFormatService thesaurusFormatService) {
		this.thesaurusFormatService = thesaurusFormatService;
	}

	public IThesaurusOrganizationService getThesaurusOrganizationService() {
		return thesaurusOrganizationService;
	}

	public void setThesaurusOrganizationService(
			IThesaurusOrganizationService thesaurusOrganizationService) {
		this.thesaurusOrganizationService = thesaurusOrganizationService;
	}

	public ILanguagesService getLanguagesService() {
		return languagesService;
	}

	public void setLanguagesService(ILanguagesService languagesService) {
		this.languagesService = languagesService;
	}

	public IThesaurusService getThesaurusService() {
		return thesaurusService;
	}

	public void setThesaurusService(IThesaurusService thesaurusService) {
		this.thesaurusService = thesaurusService;
	}
}
