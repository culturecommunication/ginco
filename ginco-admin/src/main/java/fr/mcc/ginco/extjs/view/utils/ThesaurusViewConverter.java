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

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusView;
import fr.mcc.ginco.services.*;
import fr.mcc.ginco.utils.DateUtil;
import fr.mcc.ginco.utils.LanguageComparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Small class responsible for converting between
 * {@link Thesaurus} object and its view {@link ThesaurusView}.
 */
@Component("thesaurusViewConverter")
public class ThesaurusViewConverter {
	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;    

	@Inject
	@Named("thesaurusTypeService")
	private IThesaurusTypeService thesaurusTypeService;

	@Inject
	@Named("thesaurusFormatService")
	private IThesaurusFormatService thesaurusFormatService;	
	
	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;

    @Inject
    @Named("thesaurusVersionHistoryService")
    private IThesaurusVersionHistoryService thesaurusVersionHistoryService;

	
	@Value("${ginco.default.language}") private String defaultLanguage;

    /**
     * Main method for conversion from view to object.
     * @param source view to get data from.
     * @return real object.
     * @throws BusinessException
     */
	public Thesaurus convert(ThesaurusView source) throws BusinessException {
		Thesaurus hibernateRes;

		if ("".equals(source.getId())) {
			hibernateRes = new Thesaurus();
			hibernateRes.setCreated(DateUtil.nowDate());
			hibernateRes.setIdentifier(generatorService.generate(Thesaurus.class));
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
		hibernateRes.setDefaultTopConcept(source.getDefaultTopConcept());
		hibernateRes.setFormat(thesaurusFormatService
				.getThesaurusFormatById(source.getFormat()));
        hibernateRes.setPolyHierarchical(source.getPolyHierarchical());
		hibernateRes.setType(thesaurusTypeService.getThesaurusTypeById(source
				.getType()));
		ThesaurusOrganization thesaurusOrganization;
		if (StringUtils.isNotEmpty(source.getCreatorName())
				|| StringUtils.isNotEmpty(source.getCreatorHomepage())) {
			if (hibernateRes.getCreator() != null) {
				thesaurusOrganization = hibernateRes.getCreator();
			} else {
				thesaurusOrganization = new ThesaurusOrganization();
			}
			thesaurusOrganization.setName(source.getCreatorName());
			thesaurusOrganization.setHomepage(source.getCreatorHomepage());
			hibernateRes.setCreator(thesaurusOrganization);
		} else {
			if (hibernateRes.getCreator() != null) {
				hibernateRes.setCreator(null);
			}
		}
		List<String> languages = source.getLanguages();
		Set<Language> realLanguages = new HashSet<Language>();

		for (String language : languages) {
			Language lang = languagesService.getLanguageById(language);
			if (lang != null) {
				realLanguages.add(lang);
			}
		}

		hibernateRes.setLang(realLanguages);

		return hibernateRes;
	}

    /**
     * Main method for conversion from object to view.
     * @param source object to get data from.
     * @return view object.
     * @throws BusinessException
     */
	public ThesaurusView convert(Thesaurus source) {
		ThesaurusView view = new ThesaurusView();
		if (source != null) {
			view.setId(source.getIdentifier());
			view.setContributor(source.getContributor());
			view.setCoverage(source.getCoverage());

            if(source.getDate() != null) {
            	view.setDate (DateUtil.toString(source.getDate()));
            }
            view.setDescription (source.getDescription());
            view.setPublisher(source.getPublisher());
            view.setRelation (source.getRelation());
            view.setRights(source.getRights());
            view.setSource(source.getSource());
            view.setSubject(source.getSubject());
            view.setTitle(source.getTitle());
            view.setCreated(DateUtil.toString(source.getCreated()));

			if (source.getCreator() != null) {
				view.setCreatorName(source.getCreator().getName());
				view.setCreatorHomepage(source.getCreator().getHomepage());
			}

			if (source.isDefaultTopConcept() != null) {
				view.setDefaultTopConcept(source.isDefaultTopConcept());
			} else {
				view.setDefaultTopConcept(false);
			}

            if (source.isArchived() != null) {
                view.setArchived(source.isArchived());
            } else {
                view.setArchived(false);
            }
			if (source.getFormat() != null) {
				view.setFormat(source.getFormat().getIdentifier());
			}
			if (source.getType() != null) {
				view.setType(source.getType().getIdentifier());
			}

			ArrayList<Language> langList = new ArrayList<Language>();
			for (Language lang : source.getLang()) {
				langList.add(lang);
			}
			Collections.sort(langList, new LanguageComparator(defaultLanguage));
			Collections.reverse(langList);
			ArrayList<String> langLabels = new ArrayList<String>();
			for (Language lang:langList) {
				langLabels.add(lang.getId());
			}
			view.setLanguages(langLabels);

            if(thesaurusVersionHistoryService.hasPublishedVersion(source) && !source.isArchived()) {
                view.setCanBeDeleted(false);
            } else {
                view.setCanBeDeleted(true);
            }
            view.setPolyHierarchical(source.isPolyHierarchical());
		}
		return view;
	}
}
