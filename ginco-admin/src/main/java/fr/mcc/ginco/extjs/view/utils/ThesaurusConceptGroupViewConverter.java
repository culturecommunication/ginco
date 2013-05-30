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
import fr.mcc.ginco.beans.*;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptGroupView;
import fr.mcc.ginco.services.*;
import fr.mcc.ginco.utils.DateUtil;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Component("thesaurusConceptGroupViewConverter")
public class ThesaurusConceptGroupViewConverter {
	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@Inject
	@Named("thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;
	
	@Inject
	@Named("thesaurusConceptGroupTypeService")
	private IThesaurusConceptGroupTypeService thesaurusConceptGroupTypeService;
	
	@Inject
	@Named("thesaurusConceptGroupLabelService")
	private IThesaurusConceptGroupLabelService thesaurusConceptGroupLabelService;
	
	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;	

	public ThesaurusConceptGroup convert(ThesaurusConceptGroupView source)
			throws BusinessException {
		ThesaurusConceptGroup hibernateRes;
		
		if (StringUtils.isEmpty(source.getIdentifier())) {
			hibernateRes = new ThesaurusConceptGroup();
			hibernateRes.setIdentifier(generatorService.generate(ThesaurusConceptGroup.class));

		} else {
			hibernateRes = thesaurusConceptGroupService.getConceptGroupById(source.getIdentifier());
		}
		if ("".equals(source.getThesaurusId())) {
			throw new BusinessException(
					"ThesaurusId is mandatory to save a concept group",
					"mandatory-thesaurus");
		} else {
			Thesaurus thesaurus = thesaurusService.getThesaurusById(source
					.getThesaurusId());
			hibernateRes.setThesaurus(thesaurus);
		}
		
		if (hibernateRes.getConcepts() == null) {
			hibernateRes.setConcepts(new HashSet<ThesaurusConcept>());
		}
		hibernateRes.getConcepts().clear();

		for (String conceptId : source.getConcepts()) {
			ThesaurusConcept concept = thesaurusConceptService
					.getThesaurusConceptById(conceptId);
			if (concept == null) {
				throw new BusinessException("Concept doest not exist",
						"concept-does-not-exist");
			}
			hibernateRes.getConcepts().add(concept);
		}
		
		if ("".equals(source.getType())) {
			throw new BusinessException(
					"Type is mandatory to save a concept group",
					"mandatory-type");
		} else {
			ThesaurusConceptGroupType conceptGroupType = thesaurusConceptGroupTypeService.getTypeById(source.getType());
			hibernateRes.setConceptGroupType(conceptGroupType);
		}
		
		if (source.getParentGroupId() != null) {
			hibernateRes.setParent(thesaurusConceptGroupService.getConceptGroupById(source.getParentGroupId()));
		}
		
		return hibernateRes;
	}

	public ThesaurusConceptGroupView convert(final ThesaurusConceptGroup source)
			throws BusinessException {
		ThesaurusConceptGroupView thesaurusConceptGroupView = new ThesaurusConceptGroupView();

		if (source != null) {
			thesaurusConceptGroupView.setIdentifier(source.getIdentifier());
			ThesaurusConceptGroupLabel label = thesaurusConceptGroupLabelService.getByThesaurusConceptGroupAndLanguage(source.getIdentifier());
			thesaurusConceptGroupView.setCreated(DateUtil.toString(label.getCreated()));
			thesaurusConceptGroupView.setModified(DateUtil.toString(label.getModified()));
			thesaurusConceptGroupView.setLabel(label.getLexicalValue());
			
			if (source.getThesaurus() != null) {
				thesaurusConceptGroupView.setThesaurusId(source.getThesaurus().getThesaurusId());
			}
			thesaurusConceptGroupView.setType(source.getConceptGroupType().getCode());
			thesaurusConceptGroupView.setLanguage(label.getLanguage().getId());
			thesaurusConceptGroupView.setGroupConceptLabelId(label.getIdentifier());
			
			List<String> conceptsIds = new ArrayList<String>();
			for (ThesaurusConcept concept: source.getConcepts()) {
				conceptsIds.add(concept.getIdentifier());
			}
			thesaurusConceptGroupView.setConcepts(conceptsIds);
			
			if (source.getParent() != null) {
				//We set id and label of parent concept group
				thesaurusConceptGroupView.setParentGroupId(source.getParent().getIdentifier());
				ThesaurusConceptGroupLabel labelOfParent = thesaurusConceptGroupLabelService.getByThesaurusConceptGroupAndLanguage(source.getParent().getIdentifier());
				thesaurusConceptGroupView.setParentGroupLabel(labelOfParent.getLexicalValue());			
			}
		}
		
		return thesaurusConceptGroupView;
	}
	
	/**
	 * This method converts a list of {@link ThesaurusConceptGroup} into a list of {@link ThesaurusConceptGroupView}
	 * @param {@link ThesaurusConceptGroup} groups
	 * @return {@link ThesaurusConceptGroupView} group views
	 */
	public List<ThesaurusConceptGroupView> convert(List<ThesaurusConceptGroup> groups) {
		List<ThesaurusConceptGroupView> returnedGroupViews = new ArrayList<ThesaurusConceptGroupView>(); 
		for (ThesaurusConceptGroup thesaurusConceptGroup : groups) {
			returnedGroupViews.add(convert(thesaurusConceptGroup));
		}
		return returnedGroupViews;
	}
}
