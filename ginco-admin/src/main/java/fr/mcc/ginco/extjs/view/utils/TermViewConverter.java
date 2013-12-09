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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.ThesaurusTermRole;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermRoleService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.DateUtil;

/**
 * Small class responsible for converting real {@link ThesaurusTerm} object into
 * its view {@link ThesaurusViewConverter}.
 */
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

	@Inject
	@Named("thesaurusTermRoleService")
	private IThesaurusTermRoleService thesaurusTermRoleService;

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;

	private Logger logger  = LoggerFactory.getLogger(TermViewConverter.class);


	@Value("${ginco.default.language}")
	private String language;

	private ThesaurusTerm getNewThesaurusTerm() {
		ThesaurusTerm hibernateRes = new ThesaurusTerm();
		hibernateRes.setCreated(DateUtil.nowDate());
		hibernateRes.setIdentifier(generatorService.generate(ThesaurusTerm.class));
		logger.info("Creating a new term");

		return hibernateRes;
	}

	private ThesaurusTerm getExistingThesaurusTerm(String identifier) {
		ThesaurusTerm hibernateRes = thesaurusTermService
				.getThesaurusTermById(identifier);
		logger.info("Getting an existing term with identifier " + identifier);
		return hibernateRes;
	}
	
	public ThesaurusTermView convert(ThesaurusTerm source){
		return convert(source, false);
	}
	
	public ThesaurusTermView convert(ThesaurusTerm source, boolean includeConceptInfo){
		ThesaurusTermView view = new ThesaurusTermView();
		if (source != null) {
			view.setIdentifier(source.getIdentifier());
			view.setLexicalValue(source.getLexicalValue());
			if(source != null) {
				view.setCreated(DateUtil.toString(source.getCreated()));
				view.setModified(DateUtil.toString(source.getModified()));
			}
			view.setSource(source.getSource());
			view.setPrefered(source.getPrefered());
			view.setHidden(source.getHidden());
			view.setStatus(source.getStatus());
            
            if(includeConceptInfo && source.getConcept() != null) {
            	view.setConceptId(source.getConcept().getIdentifier());
            	ArrayList<String> parentIdPath = new ArrayList<String>();
        		List<ThesaurusConcept> parentPath = thesaurusConceptService.getRecursiveParentsByConceptId(source.getConcept().getIdentifier());
        		for (int i = parentPath.size() - 1; i >= 0; i--)
        		{
        			parentIdPath.add(parentPath.get(i).getIdentifier());
        		}
        		parentIdPath.add(source.getConcept().getIdentifier());
        		if (parentPath.size()>0) {
        			view.setTopistopterm(parentPath.get(0).getTopConcept());
        		}
        		else  {
        			view.setTopistopterm(source.getConcept().getTopConcept());
        		}
        		view.setConceptsPath(parentIdPath);
            }

            view.setThesaurusId(source.getThesaurus().getIdentifier());
    		if(source.getLanguage() != null) {
    			view.setLanguage(source.getLanguage().getId());
    		if (source.getRole() != null) {
    			view.setRole(source.getRole().getCode());
    		}
    		}
		}
		
		return view;
	}

	/**
	 * Main method used to do conversion.
	 * 
	 * @param source
	 *            source to work with
	 * @param fromConcept
	 * @return converted item.
	 */
	public ThesaurusTerm convert(ThesaurusTermView source, boolean fromConcept) {
		ThesaurusTerm hibernateRes;

		if (StringUtils.isEmpty(source.getIdentifier())) {
			hibernateRes = getNewThesaurusTerm();
		} else {
			hibernateRes = getExistingThesaurusTerm(source.getIdentifier());
		}

		hibernateRes.setLexicalValue(source.getLexicalValue().trim());
		hibernateRes.setModified(DateUtil.nowDate());
		hibernateRes.setSource(source.getSource());
		hibernateRes.setStatus(source.getStatus());

		if (fromConcept) {
			hibernateRes.setPrefered(source.getPrefered());
			hibernateRes.setHidden(source.getHidden());

			if (StringUtils.isNotEmpty(source.getConceptId())) {

				ThesaurusConcept concept = thesaurusConceptService
						.getThesaurusConceptById(source.getConceptId());
				if (concept != null) {
					hibernateRes.setConcept(concept);
					
				}
			}
			if (!source.getPrefered()) {
				if (StringUtils.isNotBlank(source.getRole())) {
					ThesaurusTermRole role = thesaurusTermRoleService
							.getTermRole(source.getRole());
					hibernateRes.setRole(role);
				} else {
					hibernateRes.setRole(thesaurusTermRoleService
							.getDefaultThesaurusTermRole());
				}
			} else {
				hibernateRes.setRole(null);
			}
		}
		

		hibernateRes.setThesaurus(thesaurusService.getThesaurusById(source
				.getThesaurusId()));
		if (StringUtils.isEmpty(source.getLanguage())) {
			// If not filled in, the language for the term is
			// "ginco.default.language" property in application.properties
			hibernateRes
					.setLanguage(languagesService.getLanguageById(language));
		} else {
			hibernateRes.setLanguage(languagesService.getLanguageById(source
					.getLanguage()));
		}

		return hibernateRes;
	}

	/**
	 * This method extracts a list of ThesaurusTerm from a ThesaurusConceptView
	 * given in argument
	 * 
	 * @param termViews
	 *            array of view.
	 * @param fromConcept
	 * 
	 * @return {@code List<ThesaurusTerm>}
	 */
	public List<ThesaurusTerm> convertTermViewsInTerms(
			List<ThesaurusTermView> termViews, boolean fromConcept) {
		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();

		for (ThesaurusTermView thesaurusTermView : termViews) {
			terms.add(convert(thesaurusTermView, fromConcept));
		}
		return terms;
	}
}
