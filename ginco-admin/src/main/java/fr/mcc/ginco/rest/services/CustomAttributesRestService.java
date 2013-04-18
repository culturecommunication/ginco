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

import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.CustomTermAttributeType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.GenericCustomAttributeTypeView;
import fr.mcc.ginco.extjs.view.utils.CustomAtrributeTypesConverter;
import fr.mcc.ginco.services.ICustomConceptAttributeTypeService;
import fr.mcc.ginco.services.ICustomTermAttributeTypeService;
import fr.mcc.ginco.services.IThesaurusService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    @Named("customTermAttributeTypeService")
    private ICustomTermAttributeTypeService customTermAttributeTypeService;

    @Inject
    @Named("customAttributesConverter")
    private CustomAtrributeTypesConverter customAttributesConverter;

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
                customAttributesConverter.convertList(customConceptAttributeTypeService.getAttributeTypesByThesaurus(thesaurus)));
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
                    (CustomConceptAttributeType) customAttributesConverter.convert(customConceptAttributeType, true);

            customConceptAttributeTypeService.saveOrUpdate(conceptAttributeType);
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
                    (CustomConceptAttributeType) customAttributesConverter.convert(customConceptAttributeType, true);

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
                customAttributesConverter.convertList(customTermAttributeTypeService.getAttributeTypesByThesaurus(thesaurus)));
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
                    (CustomTermAttributeType) customAttributesConverter.convert(customConceptAttributeType, false);

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
                    (CustomTermAttributeType) customAttributesConverter.convert(customConceptAttributeType, false);

            customTermAttributeTypeService.deleteAttribute(conceptAttributeType);
        }
    }
}
