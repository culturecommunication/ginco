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
package fr.mcc.ginco.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IConceptHierarchicalRelationshipDAO;
import fr.mcc.ginco.dao.IThesaurusArrayConceptDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.utils.ThesaurusConceptUtils;

@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("conceptHierarchicalRelationshipServiceUtil")
public class ConceptHierarchicalRelationshipServiceUtil implements
		IConceptHierarchicalRelationshipServiceUtil {

	@Log
	private Logger logger;	
	
	@Inject
	@Named("thesaurusArrayConceptDAO")
	private IThesaurusArrayConceptDAO thesaurusArrayConceptDAO;

	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;
	
	@Inject
	@Named("thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;

	@Inject
	@Named("conceptHierarchicalRelationshipDAO")
	private IConceptHierarchicalRelationshipDAO conceptHierarchicalRelationshipDAO;

	@Override
	public ThesaurusConcept saveHierarchicalRelationship(
			ThesaurusConcept conceptToUpdate,
			List<ConceptHierarchicalRelationship> hierarchicalRelationships,
			List<ThesaurusConcept> childrenConceptToDetach) {
		
		
		List<ThesaurusConcept> childrenConcepts = thesaurusConceptDAO
				.getChildrenConcepts(conceptToUpdate.getIdentifier());
		

		// We update the modified relations, and we delete the relations that
		// have been removed
		List<String> oldParentConceptIds = new ArrayList<String>();
		if (!conceptToUpdate.getParentConcepts().isEmpty()) {
			oldParentConceptIds = ThesaurusConceptUtils
					.getIdsFromConceptList(new ArrayList<ThesaurusConcept>(
							conceptToUpdate.getParentConcepts()));
		}
		
		List<String> newParentConceptIds = new ArrayList<String>();
		for (ConceptHierarchicalRelationship relation : hierarchicalRelationships) {
			newParentConceptIds.add(relation.getIdentifier().getParentconceptid());
		}
		
		for (ThesaurusConcept childConcept : childrenConcepts)
		{
			if (newParentConceptIds.contains(childConcept.getIdentifier()))
			{
				throw new BusinessException(
						"A parent concept cannot be the child of the same concept",
						"hierarchical-loop-violation");
			}
		}

		
		// Verify if the concept doesn't have one of its brothers as parent
		List<String> commonIds;
		for (String currentParentId : newParentConceptIds){
			List<String> childrenOfCurrentParentIds = ThesaurusConceptUtils
					.getIdsFromConceptList(thesaurusConceptDAO.getChildrenConcepts(currentParentId));
			commonIds = new ArrayList<String>(newParentConceptIds);
			// Compare both lists and see which elements are in common. 
			// Those elements are both parents and brothers to the considered concept.
			commonIds.retainAll(childrenOfCurrentParentIds);
			
			if( !commonIds.isEmpty() ){
				String commonPreferedTerms = "";
				for(String conceptId : commonIds){
					if( commonIds.indexOf(conceptId) != 0){
						commonPreferedTerms += ", ";
					}
					commonPreferedTerms += thesaurusTermDAO.getConceptPreferredTerm(conceptId).getLexicalValue();
				}
				throw new BusinessException(
						"A concept cannot have one of its brother ("+ commonPreferedTerms +") as a parent",
						"hierarchical-brotherIsParent-violation", new Object[] {commonPreferedTerms});
			}
		}
		
		List<String> addedParentConceptIds = ListUtils.subtract(
				newParentConceptIds, oldParentConceptIds);
		List<String> removedParentConceptIds = ListUtils.subtract(
				oldParentConceptIds, newParentConceptIds);

		List<ThesaurusConcept> addedParentConcepts = new ArrayList<ThesaurusConcept>();
		for (String id : addedParentConceptIds) {
			addedParentConcepts.add(thesaurusConceptDAO.getById(id));
		}

		List<ThesaurusConcept> removedParentConcepts = new ArrayList<ThesaurusConcept>();
		for (String id : removedParentConceptIds) {
			removedParentConcepts.add(thesaurusConceptDAO.getById(id));
		}

		if (!addedParentConcepts.isEmpty() || !removedParentConcepts.isEmpty()) {
			// Treatment in case of modified hierarchy (both add or remove)
			
			// We remove this concept in all array it belongs
			List<ThesaurusArrayConcept> arrays = thesaurusArrayConceptDAO.getArraysOfConcept(conceptToUpdate);
			for (ThesaurusArrayConcept thesaurusArrayConcept : arrays) {
				thesaurusArrayConceptDAO.delete(thesaurusArrayConcept);
			}
			
			// We remove all removed parents
			if (!removedParentConcepts.isEmpty()) {
				removeParents(conceptToUpdate, removedParentConcepts);
			}

			// We set all added parents
			Set<ThesaurusConcept> addedParentsSet = new HashSet<ThesaurusConcept>();
			for (ThesaurusConcept addedParentId : addedParentConcepts) {
				addedParentsSet.add(addedParentId);
			}

			if (!addedParentConcepts.isEmpty()) {
					conceptToUpdate.getParentConcepts().addAll(addedParentsSet);
					conceptToUpdate.setTopConcept(false);
			}
			
			if (!conceptToUpdate.getThesaurus().isPolyHierarchical()
					&& conceptToUpdate.getParentConcepts().size() > 1) {
				throw new BusinessException(
						"Thesaurus is monohierarchical, but some concepts have multiple parents!",
						"monohierarchical-violation");
			}
			

			// We calculate the rootconcepts for the concept to update
			conceptToUpdate.setRootConcepts(new HashSet<ThesaurusConcept>(
					getRootConcepts(conceptToUpdate)));

			// We launch an async method to calculate new root concept for the
			// children of the concept we update
			calculateChildrenRoots(conceptToUpdate.getIdentifier(),
					conceptToUpdate.getIdentifier());
		}

		// We process children delete
		removeChildren(conceptToUpdate, childrenConceptToDetach);

		thesaurusConceptDAO.update(conceptToUpdate);
		thesaurusConceptDAO.flush();
		saveRoleOfHierarchicalRelationship(hierarchicalRelationships);

		return conceptToUpdate;
	}

	public void calculateChildrenRoots(String parentId, String originalParentId) {
		List<ThesaurusConcept> childrenConcepts = thesaurusConceptDAO
				.getChildrenConcepts(parentId);
		for (ThesaurusConcept concept : childrenConcepts) {
			if (concept.getIdentifier() != originalParentId) {
				logger.info("calculating root concept for chiled with concept Id : "
						+ concept.getIdentifier());
				List<ThesaurusConcept> thisChildRoots = getRootConcepts(concept);
				concept.setRootConcepts(new HashSet<ThesaurusConcept>(
						thisChildRoots));
				calculateChildrenRoots(concept.getIdentifier(),
						originalParentId);
			}
		}
	}

	@Override
	public List<ThesaurusConcept> getRootConcepts(ThesaurusConcept concept) {
		ThesaurusConcept start;
		HashMap<String, Integer> path = new HashMap<String, Integer>();
		Set<ThesaurusConcept> roots = new HashSet<ThesaurusConcept>();
		path.clear();
		roots.clear();
		path.put(concept.getIdentifier(), 0);
		start = concept;
		getRoot(concept, 0, start, path, roots);
		return new ArrayList<ThesaurusConcept>(roots);
	}

	private void getRoot(ThesaurusConcept concept, Integer iteration,
			ThesaurusConcept start, Map<String, Integer> path,
			Set<ThesaurusConcept> roots) {
		iteration++;
		Set<ThesaurusConcept> directParents = concept.getParentConcepts();
		if (directParents.isEmpty()) {
			if (iteration != 1) {
				roots.add(concept);
			}
			return;
		}
		boolean flag = false;
		Set<ThesaurusConcept> stack = new HashSet<ThesaurusConcept>();
		for (ThesaurusConcept directParent : directParents) {
			if (directParent == null
					|| path.containsKey(directParent.getIdentifier())) {
				continue;
			} else {
				path.put(directParent.getIdentifier(), iteration);
				stack.add(directParent);
				flag = true;
			}
		}

		// HACK to deal with cyclic dependencies. Should be reThink in some
		// time...
		if (!flag && directParents.size() == 1 && directParents.contains(start)) {
			roots.add(concept);
		}

		if (!stack.isEmpty()) {
			for (ThesaurusConcept toVisit : stack) {
				getRoot(toVisit, iteration, start, path, roots);
			}
			stack.clear();
		}
	}

	/**
	 * This method saves the role of a hierarchical relationship
	 * 
	 * @param hierarchicalRelationships
	 * @return
	 */
	private List<ConceptHierarchicalRelationship> saveRoleOfHierarchicalRelationship(
			List<ConceptHierarchicalRelationship> hierarchicalRelationships) {
		List<ConceptHierarchicalRelationship> updatedHierarchicalRelationships = new ArrayList<ConceptHierarchicalRelationship>();

		for (ConceptHierarchicalRelationship conceptHierarchicalRelationship : hierarchicalRelationships) {
			ConceptHierarchicalRelationship gettedRelation = conceptHierarchicalRelationshipDAO
					.getById(conceptHierarchicalRelationship.getIdentifier());
			gettedRelation.setRole(conceptHierarchicalRelationship.getRole());
			updatedHierarchicalRelationships
					.add(conceptHierarchicalRelationshipDAO
							.update(gettedRelation));
		}
		return updatedHierarchicalRelationships;
	}

	/**
	 * This method detach children concept from concept given in parameter
	 * 
	 * @param conceptToUpdate
	 * @param childrenConceptToDetach
	 */
	private void removeChildren(ThesaurusConcept conceptToUpdate,
			List<ThesaurusConcept> childrenConceptToDetach) {
		Set<ThesaurusConcept> parentRootConcepts = conceptToUpdate
				.getRootConcepts();
		List<ThesaurusConcept> parentToRemove = new ArrayList<ThesaurusConcept>();
		parentToRemove.add(conceptToUpdate);

		for (ThesaurusConcept childConcept : childrenConceptToDetach) {
			removeParents(childConcept, parentToRemove);

			if (conceptToUpdate.getRootConcepts().isEmpty()) {
				// If the parent concept is the root concept, we remove it from
				// root concept of child
				childConcept.getRootConcepts().remove(conceptToUpdate);
			} else {
				// The parent concept is not the root concept, so we remove for
				// each child its root concept
				for (ThesaurusConcept rootConcept : parentRootConcepts) {
					if (childConcept.getRootConcepts().contains(rootConcept)) {
						// We remove for children's root concepts all references
						// to concepts that belongs to
						childConcept.getRootConcepts().remove(rootConcept);
					}
				}
			}
			// We recalculate the rootconcepts for children of the child
			// TODO : VERIFIY IF OK IN NON ASYNC LAUNCH
			calculateChildrenRoots(childConcept.getIdentifier(),
					childConcept.getIdentifier());
		}
	}

	private void removeParents(ThesaurusConcept concept,
			List<ThesaurusConcept> parents) throws BusinessException {
		boolean isDefaultTopConcept = concept.getThesaurus()
				.isDefaultTopConcept();
		if (concept.getParentConcepts().size() == 1) {
			concept.getParentConcepts().clear();
			concept.setTopConcept(isDefaultTopConcept);
		} else {
			for (ThesaurusConcept parent : parents) {
				concept.getParentConcepts().remove(parent);
			}
		}
	}

}
