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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.beans.CustomTermAttributeType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.GenericCustomAttributeTypeView;
import fr.mcc.ginco.extjs.view.pojo.GenericCustomAttributeView;
import fr.mcc.ginco.extjs.view.utils.CustomAttributeConverter;
import fr.mcc.ginco.extjs.view.utils.CustomAttributesTypesConverter;
import fr.mcc.ginco.services.ICustomConceptAttributeService;
import fr.mcc.ginco.services.ICustomConceptAttributeTypeService;
import fr.mcc.ginco.services.ICustomTermAttributeService;
import fr.mcc.ginco.services.ICustomTermAttributeTypeService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;

/**
 *
 */
@Service
@Path("/customattributesservice")
@PreAuthorize("isAuthenticated()")
public class CustomAttributesRestService {
    @Inject
    @Named("customConceptAttributeTypeService")
    private ICustomConceptAttributeTypeService customConceptAttributeTypeService;

    @Inject
    @Named("thesaurusService")
    private IThesaurusService thesaurusService;
    
    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;
    
    @Inject 
    @Named("thesaurusTermService")
    private IThesaurusTermService thesaurusTermService;

    @Inject
    @Named("customTermAttributeTypeService")
    private ICustomTermAttributeTypeService customTermAttributeTypeService;

    @Inject
    @Named("customTermAttributeService")
    private ICustomTermAttributeService customTermAttributeService;
    
    @Inject
    @Named("customConceptAttributeService")
    private ICustomConceptAttributeService customConceptAttributeService;

    
    @Inject
    @Named("customAttributesTypesConverter")
    private CustomAttributesTypesConverter customAttributesTypeConverter;
    
    @Inject
    @Named("customAttributeConverter")
    private CustomAttributeConverter customAttributeConverter;

    /**
     * Return list of all custom attribute types for concept in given thesaurus
     *
     * @param thesaurusId
     * @return
     * @throws BusinessException
     */
    @GET
    @Path("/getAllConceptAttributeTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public ExtJsonFormLoadData<List<GenericCustomAttributeTypeView>> getAllConceptAttributeTypes(
            @QueryParam("thesaurusId") String thesaurusId)
            throws BusinessException {
        Thesaurus thesaurus = thesaurusService.getThesaurusById(thesaurusId);

        return new ExtJsonFormLoadData<List<GenericCustomAttributeTypeView>>(
                customAttributesTypeConverter.convertList(customConceptAttributeTypeService.getAttributeTypesByThesaurus(thesaurus)));
    }

    /**
     * Updates a list of concept's type attributes.
     * @param list
     * @throws BusinessException
     * @throws TechnicalException
     */
    @POST
    @Path("/updateConceptAttributeTypes")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateConceptAttributeTypes(List<GenericCustomAttributeTypeView> list) throws BusinessException, TechnicalException {
        for(GenericCustomAttributeTypeView customConceptAttributeType : list) {
            CustomConceptAttributeType conceptAttributeType =
                    (CustomConceptAttributeType) customAttributesTypeConverter.convert(customConceptAttributeType, true);

            if(customConceptAttributeTypeService.isUnique(conceptAttributeType.getThesaurus(), conceptAttributeType.getCode())) {
                customConceptAttributeTypeService.saveOrUpdate(conceptAttributeType);
            }
        }
    }

    /**
     * Removes a list of concept's type attributes.
     * @param list
     * @throws BusinessException
     * @throws TechnicalException
     */
    @POST
    @Path("/deleteConceptAttributeTypes")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteConceptAttributeTypes(List<GenericCustomAttributeTypeView> list) throws BusinessException, TechnicalException {
        for(GenericCustomAttributeTypeView customConceptAttributeType : list) {
            CustomConceptAttributeType conceptAttributeType =
                    (CustomConceptAttributeType) customAttributesTypeConverter.convert(customConceptAttributeType, true);

            customConceptAttributeTypeService.deleteAttribute(conceptAttributeType);
        }
    }

    /**
     * Return list of all custom attribute types for term in given thesaurus
     *
     * @param thesaurusId
     * @return
     * @throws BusinessException
     */
    @GET
    @Path("/getAllTermAttributeTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public ExtJsonFormLoadData<List<GenericCustomAttributeTypeView>> getAllTermAttributeTypes(
            @QueryParam("thesaurusId") String thesaurusId)
            throws BusinessException {
        Thesaurus thesaurus = thesaurusService.getThesaurusById(thesaurusId);

        return new ExtJsonFormLoadData<List<GenericCustomAttributeTypeView>>(
                customAttributesTypeConverter.convertList(customTermAttributeTypeService.getAttributeTypesByThesaurus(thesaurus)));
    }

    /**
     * Updates a list of term type attributes.
     * @param list
     * @throws BusinessException
     * @throws TechnicalException
     */
    @POST
    @Path("/updateTermAttributeTypes")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateTermAttributeTypes(List<GenericCustomAttributeTypeView> list) throws BusinessException, TechnicalException {
        for(GenericCustomAttributeTypeView customConceptAttributeType : list) {
            CustomTermAttributeType conceptAttributeType =
                    (CustomTermAttributeType) customAttributesTypeConverter.convert(customConceptAttributeType, false);

            customTermAttributeTypeService.saveOrUpdate(conceptAttributeType);
        }
    }

    /**
     * Removes a list of term's type attributes.
     * @param list
     * @throws BusinessException
     * @throws TechnicalException
     */
    @POST
    @Path("/deleteTermAttributeTypes")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteTermAttributeTypes(List<GenericCustomAttributeTypeView> list) throws BusinessException, TechnicalException {
        for(GenericCustomAttributeTypeView customConceptAttributeType : list) {
            CustomTermAttributeType conceptAttributeType =
                    (CustomTermAttributeType) customAttributesTypeConverter.convert(customConceptAttributeType, false);

            customTermAttributeTypeService.deleteAttribute(conceptAttributeType);
        }
    }
    
    /**
     * Updates a list of term type attributes.
     * @param list
     * @throws BusinessException
     * @throws TechnicalException
     */
    @POST
    @Path("/updateTermAttribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ExtJsonFormLoadData<List<GenericCustomAttributeView>> updateTermAttribute(List<GenericCustomAttributeView> list) throws BusinessException, TechnicalException {
        for(GenericCustomAttributeView customAttributeView : list) {
            CustomTermAttribute termAttribute =
                     customAttributeConverter.convertTermAttribute(customAttributeView);
            customTermAttributeService.saveOrUpdate(termAttribute);
        }
        return new ExtJsonFormLoadData<List<GenericCustomAttributeView>>(list);
    }
    
    /**
     * Return list of all custom attribute for term 
     *
     * @param thesaurusId
     * @return
     * @throws BusinessException
     */
    @GET
    @Path("/getTermAttribute")
    @Produces(MediaType.APPLICATION_JSON)
    public ExtJsonFormLoadData<List<GenericCustomAttributeView>> getTermAttribute(
            @QueryParam("entityId") String entityId)
            throws BusinessException {
    	
    	ThesaurusTerm entity = thesaurusTermService.getThesaurusTermById(entityId);
    	List<CustomTermAttribute> list= customTermAttributeService.getAttributesByEntity(entity);
        return new ExtJsonFormLoadData<List<GenericCustomAttributeView>>(
        		customAttributeConverter.convertListTerm(list));
    }
    
    /**
     * Return list of all custom attribute for concept 
     *
     * @param thesaurusId
     * @return
     * @throws BusinessException
     */
    @GET
    @Path("/getConceptAttribute")
    @Produces(MediaType.APPLICATION_JSON)
    public ExtJsonFormLoadData<List<GenericCustomAttributeView>> getConceptAttribute(
            @QueryParam("entityId") String entityId)
            throws BusinessException {
    	
    	ThesaurusConcept entity = thesaurusConceptService.getThesaurusConceptById(entityId);
    	List<CustomConceptAttribute> list= customConceptAttributeService.getAttributesByEntity(entity);
        return new ExtJsonFormLoadData<List<GenericCustomAttributeView>>(
        		customAttributeConverter.convertListConcept(list));
    }
    
    /**
     * Updates a list of term type attributes.
     * @param list
     * @throws BusinessException
     * @throws TechnicalException
     */
    @POST
    @Path("/updateConceptAttribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ExtJsonFormLoadData<List<GenericCustomAttributeView>> updateConceptAttribute(List<GenericCustomAttributeView> list) throws BusinessException, TechnicalException {
        for(GenericCustomAttributeView customAttributeView : list) {
            CustomConceptAttribute conceptAttribute =
                     customAttributeConverter.convertConceptAttribute(customAttributeView);
            customConceptAttributeService.saveOrUpdate(conceptAttribute);
        }
        return new ExtJsonFormLoadData<List<GenericCustomAttributeView>>(list);
    }
}
