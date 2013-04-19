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
package fr.mcc.ginco.extjs.view.utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.beans.CustomTermAttributeType;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.extjs.view.pojo.GenericCustomAttributeView;
import fr.mcc.ginco.services.ICustomConceptAttributeService;
import fr.mcc.ginco.services.ICustomTermAttributeService;
import fr.mcc.ginco.services.ICustomTermAttributeTypeService;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;

/**
 *
 */
@Component(value = "customAttributeConverter")
public class CustomAttributeConverter {

    @Inject
    @Named("customConceptAttributeService")
    private ICustomConceptAttributeService customConceptAttributeService;

    @Inject
    @Named("thesaurusService")
    private IThesaurusService thesaurusService;
    
    @Inject 
    @Named("thesaurusTermService")
    private IThesaurusTermService thesaurusTermService;
    
    @Inject 
    @Named("languagesService")
    private ILanguagesService languagesService;
    
	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;

    @Inject
    @Named("customTermAttributeService")
    private ICustomTermAttributeService customTermAttributeService;
    
    @Inject
    @Named("customTermAttributeTypeService")
    private ICustomTermAttributeTypeService customTermAttributeTypeService;


    public GenericCustomAttributeView convertTermAttribute(CustomTermAttribute source) {
    	GenericCustomAttributeView view = new GenericCustomAttributeView();
    	view.setEntityid(source.getEntity().getIdentifier());
    	view.setLang(source.getLanguage().getId());
    	view.setLexicalValue(source.getLexicalValue());
    	view.setTypeid(source.getType().getIdentifier());
        return view;
    }
    
    public GenericCustomAttributeView convertConceptAttribute(CustomConceptAttribute source) {
    	GenericCustomAttributeView view = new GenericCustomAttributeView();
    	view.setEntityid(source.getEntity().getIdentifier());
    	view.setLang(source.getLanguage().getId());
    	view.setLexicalValue(source.getLexicalValue());
    	view.setTypeid(source.getType().getIdentifier());
        return view;
    }
    
    public CustomTermAttribute convertTermAttribute(GenericCustomAttributeView view) {
    	ThesaurusTerm entity = thesaurusTermService.getThesaurusTermById(view.getEntityid());
    	CustomTermAttributeType type = customTermAttributeTypeService.getAttributeTypeById(view.getTypeid());
    	CustomTermAttribute hibernateRes = customTermAttributeService.getAttributeByType(entity, type);
    	if (hibernateRes == null)
    	{
    		hibernateRes = new CustomTermAttribute();
    		hibernateRes.setIdentifier(generatorService.generate(CustomTermAttribute.class));
    	}
    	hibernateRes.setEntity(entity);
    	hibernateRes.setLanguage(languagesService.getLanguageById(view.getLang()));
    	hibernateRes.setLexicalValue(view.getLexicalValue());
    	hibernateRes.setType(type);
    	return hibernateRes;
    }
    
    

    public List<GenericCustomAttributeView> convertListTerm(List<CustomTermAttribute> sourceList) {
        List<GenericCustomAttributeView> list = new ArrayList<GenericCustomAttributeView>();
        for(CustomTermAttribute attribute : sourceList) {
            list.add(convertTermAttribute(attribute));
        }
        return list;
    }
    /*
    public GenericCustomAttributeType convert(GenericCustomAttributeTypeView view, boolean isConcept) {

        Thesaurus thesaurus = thesaurusService.getThesaurusById(view.getThesaurusId());

        if(isConcept) {
            GenericCustomAttributeType hibernateRes = customConceptAttributeTypeService.getAttributeTypeById(view.getIdentifier());
            if(hibernateRes == null) {
                hibernateRes = new CustomConceptAttributeType();
                hibernateRes.setThesaurus(thesaurus);
            }

            hibernateRes.setCode(view.getCode());
            hibernateRes.setValue(view.getValue());

            return hibernateRes;
        } else {
            GenericCustomAttributeType hibernateRes = customTermAttributeTypeService.getAttributeTypeById(view.getIdentifier());
            if(hibernateRes == null) {
                hibernateRes = new CustomTermAttributeType();
                hibernateRes.setThesaurus(thesaurus);
            }

            hibernateRes.setCode(view.getCode());
            hibernateRes.setValue(view.getValue());

            return hibernateRes;
        }
    }*/
}
