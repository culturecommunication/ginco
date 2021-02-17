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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.AlignmentView;
import fr.mcc.ginco.extjs.view.pojo.AssociativeRelationshipView;
import fr.mcc.ginco.extjs.view.pojo.HierarchicalRelationshipView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptReducedView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusTermView;
import fr.mcc.ginco.services.IAlignmentService;
import fr.mcc.ginco.services.IAssociativeRelationshipService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.utils.DateUtil;

/**
 * Small class responsible for converting real {@link ThesaurusConcept} object
 * into its view {@link ThesaurusConceptReducedView}.
 */
@Component("thesaurusConceptViewConverter")
public class ThesaurusConceptViewConverter {

	private Logger logger = LoggerFactory.getLogger(ThesaurusConceptViewConverter.class);

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("associativeRelationshipViewConverter")
	private AssociativeRelationshipViewConverter associativeRelationshipViewConverter;

	@Inject
	@Named("hierarchicalRelationshipViewConverter")
	private HierarchicalRelationshipViewConverter hierarchicalRelationshipViewConverter;

	@Inject
	@Named("alignmentViewConverter")
	private AlignmentViewConverter alignmentViewConverter;

	@Inject
	@Named("termViewConverter")
	private TermViewConverter termViewConverter;


	@Inject
	@Named("alignmentService")
	private IAlignmentService alignmentService;

	@Inject
	@Named("associativeRelationshipService")
	private IAssociativeRelationshipService associativeRelationshipService;

	public List<ThesaurusConceptReducedView> convert(
			List<ThesaurusConcept> conceptList) {

		List<ThesaurusConceptReducedView> result = new ArrayList<ThesaurusConceptReducedView>();

		for (ThesaurusConcept concept : conceptList) {
			ThesaurusConceptReducedView view = new ThesaurusConceptReducedView();
			view.setIdentifier(concept.getIdentifier());
			view.setLabel(thesaurusConceptService.getConceptLabel(concept
					.getIdentifier()));
			result.add(view);
		}

		return result;
	}

	public ThesaurusConceptReducedView convert(ThesaurusConcept concept) {
		ThesaurusConceptReducedView view = new ThesaurusConceptReducedView();
		view.setIdentifier(concept.getIdentifier());
		view.setLabel(thesaurusConceptService.getConceptLabel(concept
				.getIdentifier()));
		return view;
	}

	public ThesaurusConceptView convert(ThesaurusConcept concept,
	                                    List<ThesaurusTerm> thesaurusTerms) {
		ThesaurusConceptView view = new ThesaurusConceptView();
		view.setIdentifier(concept.getIdentifier());
		view.setCreated(DateUtil.toString(concept.getCreated()));
		view.setModified(DateUtil.toString(concept.getModified()));
		view.setTopconcept(concept.getTopConcept());
		view.setThesaurusId(concept.getThesaurus().getIdentifier());
		view.setStatus(concept.getStatus());
		view.setNotation(concept.getNotation());

		List<HierarchicalRelationshipView> parentConcepts = hierarchicalRelationshipViewConverter.getParentViews(concept);
		view.setParentConcepts(parentConcepts);
		List<String> parentIdPath = new ArrayList<String>();
		List<String> parentPrefLabelPath = new ArrayList<>();
		List<ThesaurusConcept> parentPath = thesaurusConceptService.getRecursiveParentsByConceptId(concept.getIdentifier());
		for (int i = parentPath.size() - 1; i >= 0; i--) {
			parentIdPath.add(parentPath.get(i).getIdentifier());
			parentPrefLabelPath.add(thesaurusConceptService.getConceptTitle(parentPath.get(i)));
		}
		parentIdPath.add(concept.getIdentifier());
		parentPrefLabelPath.add(thesaurusConceptService.getConceptTitle(concept));
		if (parentPath.size() > 0) {
			view.setTopistopterm(parentPath.get(0).getTopConcept());
		} else {
			view.setTopistopterm(concept.getTopConcept());
		}
		view.setConceptsPath(parentIdPath);
		view.setPrefLabelsPath(parentPrefLabelPath);
		List<HierarchicalRelationshipView> childrenConcepts = hierarchicalRelationshipViewConverter.getChildrenViews(concept);
		view.setChildConcepts(childrenConcepts);

		view.setRootConcepts(getIdsFromConceptList(concept.getRootConcepts()));
		List<ThesaurusTermView> terms = new ArrayList<ThesaurusTermView>();
		for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
			terms.add(termViewConverter.convert(thesaurusTerm));
		}
		view.setTerms(terms);

		List<AssociativeRelationshipView> associatedConcepts = new ArrayList<AssociativeRelationshipView>();

		List<String> ids = associativeRelationshipService.getAssociatedConceptsId(concept);

		for (String conceptAssociated : ids) {
			AssociativeRelationship associativeRelationship =
					associativeRelationshipService.getAssociativeRelationshipById(conceptAssociated, concept.getIdentifier());

			associatedConcepts.add(associativeRelationshipViewConverter.convert(associativeRelationship, concept));
			logger.info("Found associated concept : "
					+ conceptAssociated);
		}

		view.setAssociatedConcepts(associatedConcepts);

		List<AlignmentView> alignmentViews = new ArrayList<AlignmentView>();
		List<Alignment> alignments = alignmentService.getAlignmentsBySourceConceptId(concept.getIdentifier());
		for (Alignment alignment : alignments) {
			alignmentViews.add(alignmentViewConverter.convertAlignment(alignment));
		}
		view.setAlignments(alignmentViews);

		return view;
	}

	private List<String> getIdsFromConceptList(Set<ThesaurusConcept> list) {
		List<String> result = new ArrayList<String>();
		for (ThesaurusConcept concept : list) {
			result.add(concept.getIdentifier());
		}
		return result;
	}

	/**
	 * @param source source to work with
	 * @return ThesaurusConcept
	 * This method extracts a ThesaurusConcept from a
	 * ThesaurusConceptView given in argument
	 */
	public ThesaurusConcept convert(ThesaurusConceptView source) {
		ThesaurusConcept thesaurusConcept;

		// Test if ThesaurusConcept already exists. If yes we get it, if no we
		// create a new one
		if (StringUtils.isEmpty(source.getIdentifier())) {
			thesaurusConcept = new ThesaurusConcept();
			thesaurusConcept.setCreated(DateUtil.nowDate());
			logger.info("Creating a new concept");
		} else {
			thesaurusConcept = thesaurusConceptService
					.getThesaurusConceptById(source.getIdentifier());
			logger.info("Getting an existing concept");
		}

		if (StringUtils.isEmpty(source.getThesaurusId())) {
			throw new BusinessException(
					"ThesaurusId is mandatory to save a concept",
					"mandatory-thesaurus");
		} else {
			Thesaurus thesaurus = new Thesaurus();
			thesaurus = thesaurusService.getThesaurusById(source
					.getThesaurusId());
			thesaurusConcept.setThesaurus(thesaurus);
		}
		thesaurusConcept.setModified(DateUtil.nowDate());
		thesaurusConcept.setStatus(source.getStatus());
		if (source.getStatus() == ConceptStatusEnum.CANDIDATE.getStatus()
				|| source.getStatus() == ConceptStatusEnum.REJECTED.getStatus()) {
			thesaurusConcept.setTopConcept(false);
		} else {
			thesaurusConcept.setTopConcept(source.getTopconcept());
		}

		if (source.getNotation() != null) {
			thesaurusConcept.setNotation(source.getNotation());
		}
		return thesaurusConcept;
	}


	/**
	 * This method generates a list of children concept we have to attach to the concept we are updating
	 *
	 * @param conceptView
	 */
	public List<ThesaurusConcept> convertAddedChildren(ThesaurusConceptView conceptView) {
		List<ThesaurusConcept> addedChildren = new ArrayList<ThesaurusConcept>();
		Set<ThesaurusConcept> oldChildren = new HashSet<ThesaurusConcept>(thesaurusConceptService.getChildrenByConceptId(conceptView.getIdentifier(),null));
		List<String> oldChildrenIds = getIdsFromConceptList(oldChildren);

		List<String> newChildrenIds = new ArrayList<String>();
		for (HierarchicalRelationshipView viewOfChild : conceptView.getChildConcepts()) {
			newChildrenIds.add(viewOfChild.getIdentifier());
		}

		List<String> addedChildrenIds = new ArrayList<String>();
		if (conceptView.getChildConcepts() != null) {
			addedChildrenIds = ListUtils.subtract(newChildrenIds, oldChildrenIds);
		} else {
			addedChildrenIds = oldChildrenIds;
		}

		for (String addedChildId : addedChildrenIds) {
			addedChildren.add(thesaurusConceptService.getThesaurusConceptById(addedChildId));
		}

		return addedChildren;
	}

	/**
	 * This method generates a list of children concept we have to detach from the concept we are updating
	 *
	 * @param conceptView
	 */
	public List<ThesaurusConcept> convertRemovedChildren(ThesaurusConceptView conceptView) {
		List<ThesaurusConcept> removedChildren = new ArrayList<ThesaurusConcept>();
		Set<ThesaurusConcept> oldChildren = new HashSet<ThesaurusConcept>(thesaurusConceptService.getChildrenByConceptId(conceptView.getIdentifier(),null));
		List<String> oldChildrenIds = getIdsFromConceptList(oldChildren);

		List<String> newChildrenIds = new ArrayList<String>();
		for (HierarchicalRelationshipView viewOfChild : conceptView.getChildConcepts()) {
			newChildrenIds.add(viewOfChild.getIdentifier());
		}

		List<String> removedChildrenIds = new ArrayList<String>();
		if (conceptView.getChildConcepts() != null) {
			removedChildrenIds = ListUtils.subtract(oldChildrenIds, newChildrenIds);
		} else {
			removedChildrenIds = oldChildrenIds;
		}

		for (String removedChildId : removedChildrenIds) {
			removedChildren.add(thesaurusConceptService.getThesaurusConceptById(removedChildId));
		}

		return removedChildren;
	}

}

