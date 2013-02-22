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

import javax.inject.Inject;
import javax.inject.Named;

import fr.mcc.ginco.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.utils.DateUtil;

@Component("termViewConverter")
public class TermViewConverter {
	
	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;
	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;   
	
	@Log
	private Logger logger;
	
	@Value("${ginco.default.language}") private String language;
	
	public ThesaurusTerm convert(ThesaurusTermView source) throws BusinessException {
		ThesaurusTerm hibernateRes;

		if ("".equals(source.getIdentifier())) {
			hibernateRes = new ThesaurusTerm();
			hibernateRes.setCreated(DateUtil.nowDate());
			logger.info("Creating a new term");
		} else {
			hibernateRes = thesaurusTermService.getThesaurusTermById(source.getIdentifier());
			logger.info("Getting an existing term");
		}
		
		hibernateRes.setLexicalValue(source.getLexicalValue());
		hibernateRes.setModified(DateUtil.nowDate());
		hibernateRes.setSource(source.getSource());
		hibernateRes.setPrefered(source.getPrefered());
		hibernateRes.setStatus(source.getStatus());

//          ********* RESERVED for further usage !!

//        if(source.getRole() != null) {
//            hibernateRes.setRole(thesaurusTermRoleService.getThesaurusTermRoleByCode(source.getRole()));
//        }

        if(source.getConceptId() != null && !source.getConceptId().equals("")) {
            ThesaurusConcept concept = thesaurusConceptService.getThesaurusConceptById(source.getConceptId());
            if(concept != null) {
                hibernateRes.setConceptId(concept);
            }
        }
		hibernateRes.setThesaurusId(thesaurusService.getThesaurusById(source.getThesaurusId()));
		if ("".equals(source.getLanguage())) {
			//If not filled in, the language for the term is "ginco.default.language" property in application.properties
			hibernateRes.setLanguage(languagesService.getLanguageById(language));

		} else
		{
			hibernateRes.setLanguage(languagesService.getLanguageById(source.getLanguage()));			
		}
		
		return hibernateRes;
	}
}
