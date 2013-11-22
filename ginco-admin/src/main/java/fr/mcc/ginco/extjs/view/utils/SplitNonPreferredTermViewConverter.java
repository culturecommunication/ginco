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
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.SplitNonPreferredTermView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.utils.DateUtil;

/**
 * Small class responsible for converting real {@link ThesaurusTerm} object into
 * its view {@link ThesaurusViewConverter}.
 */
@Component("splitNonPreferredTermViewConverter")
public class SplitNonPreferredTermViewConverter {

	@Inject
	@Named("splitNonPreferredTermService")
	private ISplitNonPreferredTermService splitNonPreferredTermService;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;
	
	@Inject
	@Named("termViewConverter")
	private TermViewConverter termViewConverter;

	private Logger logger  = LoggerFactory.getLogger(SplitNonPreferredTermViewConverter.class);


	@Value("${ginco.default.language}")
	private String language;

	
	public SplitNonPreferredTermView convert(SplitNonPreferredTerm source)
			throws BusinessException {
		SplitNonPreferredTermView view = new SplitNonPreferredTermView();
		if (source != null) {
			view.setIdentifier(source.getIdentifier());
			view.setLexicalValue(source.getLexicalValue());
			if(source != null) {
				view.setCreated(DateUtil.toString(source.getCreated()));
				view.setModified(DateUtil.toString(source.getModified()));
			}
			view.setSource(source.getSource());
    
			view.setStatus(source.getStatus());
			List<ThesaurusTermView> preferredList = new ArrayList<ThesaurusTermView>();
			view.setPreferredTerms(new ArrayList<ThesaurusTermView>());
            for (ThesaurusTerm preferredTerm : source.getPreferredTerms())
            {
            	preferredList.add(termViewConverter.convert(preferredTerm));
            }
            view.setPreferredTerms(preferredList);

            view.setThesaurusId(source.getThesaurus().getIdentifier());
    		if(source.getLanguage() != null) {
    			view.setLanguage(source.getLanguage().getId());
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
	 * @throws BusinessException
	 */
	public SplitNonPreferredTerm convert(SplitNonPreferredTermView source)
			throws BusinessException {
		SplitNonPreferredTerm hibernateRes;

		if (StringUtils.isEmpty(source.getIdentifier())) {
			hibernateRes = getNewSplitNonPreferredTerm();
		} else {
			hibernateRes = getExistingSplitNonPreferredTerm(source.getIdentifier());
		}
		Set<ThesaurusTerm> existingTerms = hibernateRes.getPreferredTerms();
		List<ThesaurusTermView> prefTermsView = source.getPreferredTerms();
		// Handling adding new terms
		if (prefTermsView!=null)
		{
			for (ThesaurusTermView prefTermView : prefTermsView)
			{
				ThesaurusTerm convertedTerm = termViewConverter.convert(prefTermView,false);
				if (!existingTerms.contains(convertedTerm))
				{
					existingTerms.add(convertedTerm);
				}
			}
		}
		// Handling removing terms
		List<ThesaurusTerm> termsToRemove = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm existingTerm : existingTerms)
		{
			Boolean found = false;
			for (ThesaurusTermView prefTermView : prefTermsView)
			{
				if (prefTermView.getIdentifier().equals(existingTerm.getIdentifier())){
					found = true;
				}
			}
			if (!found) {
				termsToRemove.add(existingTerm);
			}
		}
		for (ThesaurusTerm termToRemove : termsToRemove) 
		{
			existingTerms.remove(termToRemove);
		}
		hibernateRes.setLexicalValue(source.getLexicalValue().trim());
		hibernateRes.setModified(DateUtil.nowDate());
		hibernateRes.setSource(source.getSource());
		hibernateRes.setStatus(source.getStatus());

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

	private SplitNonPreferredTerm getNewSplitNonPreferredTerm() {
		SplitNonPreferredTerm hibernateRes = new SplitNonPreferredTerm();
		hibernateRes.setCreated(DateUtil.nowDate());
		hibernateRes.setIdentifier(generatorService.generate(ThesaurusTerm.class));
		logger.info("Creating a new SplitNonPreferredTerm");

		return hibernateRes;
	}
	
	private SplitNonPreferredTerm getExistingSplitNonPreferredTerm(String identifier)
			throws BusinessException {
		SplitNonPreferredTerm hibernateRes = splitNonPreferredTermService.getSplitNonPreferredTermById(identifier);
		logger.info("Getting an existing SplitNonPreferredTerm with identifier " + identifier);
		return hibernateRes;
	}
}
