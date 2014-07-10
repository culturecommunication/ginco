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

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.extjs.view.pojo.HierarchicalRelationshipView;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Component("hierarchicalRelationshipViewConverter")
public class HierarchicalRelationshipViewConverter {

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("conceptHierarchicalRelationshipService")
	private IConceptHierarchicalRelationshipService conceptHierarchicalRelationshipService;

	/**
	 * This method returns a list of {@link HierarchicalRelationshipView} corresponding to parents of the given concept
	 *
	 * @param concept
	 * @return A list of {@link HierarchicalRelationshipView} parents
	 */
	public List<HierarchicalRelationshipView> getParentViews(ThesaurusConcept concept) {
		List<HierarchicalRelationshipView> result = new ArrayList<HierarchicalRelationshipView>();
		if (concept != null) {
			Set<ThesaurusConcept> parents = concept.getParentConcepts();

			if (parents != null) {
				for (ThesaurusConcept thesaurusConcept : parents) {
					HierarchicalRelationshipView item = new HierarchicalRelationshipView();
					item.setIdentifier(thesaurusConcept.getIdentifier());
					item.setLabel(thesaurusConceptService.getConceptTitle(thesaurusConcept));
					item.setRole(conceptHierarchicalRelationshipService.getByChildAndParentIds(concept.getIdentifier(), thesaurusConcept.getIdentifier()).getRole());
					result.add(item);
				}
			}
		}
		return result;
	}

	/**
	 * This method returns a list of {@link HierarchicalRelationshipView} corresponding to children of the given concept
	 *
	 * @param concept
	 * @return A list of {@link HierarchicalRelationshipView} children
	 */
	public List<HierarchicalRelationshipView> getChildrenViews(ThesaurusConcept concept) {
		List<HierarchicalRelationshipView> result = new ArrayList<HierarchicalRelationshipView>();
		if (concept != null) {
			List<ThesaurusConcept> children = thesaurusConceptService.getChildrenByConceptId(concept.getIdentifier());

			if (children != null) {
				for (ThesaurusConcept thesaurusConcept : children) {
					HierarchicalRelationshipView item = new HierarchicalRelationshipView();
					item.setIdentifier(thesaurusConcept.getIdentifier());
					item.setLabel(thesaurusConceptService.getConceptTitle(thesaurusConcept));
					ConceptHierarchicalRelationship relation = conceptHierarchicalRelationshipService.getByChildAndParentIds(thesaurusConcept.getIdentifier(), concept.getIdentifier());
					item.setRole(relation.getRole());
					result.add(item);
				}
			}
		}
		return result;
	}

	/**
	 * This method convert the relation from child (concept we are saving) to its parent {@link HierarchicalRelationshipView} in {@link ConceptHierarchicalRelationship}
	 *
	 * @param hierarchicalRelationView
	 * @param convertedConcept
	 * @return
	 */
	public ConceptHierarchicalRelationship convertRelationFromChildToParent(HierarchicalRelationshipView hierarchicalRelationView, ThesaurusConcept convertedConcept) {
		ConceptHierarchicalRelationship relation = new ConceptHierarchicalRelationship();

		String childConceptId = convertedConcept.getIdentifier();
		String parentConceptId = hierarchicalRelationView.getIdentifier();

		if (conceptHierarchicalRelationshipService.getByChildAndParentIds(childConceptId, parentConceptId) != null) {
			//If relation already exists, we get it
			relation = conceptHierarchicalRelationshipService.getByChildAndParentIds(childConceptId, parentConceptId);
		} else {
			//We create an id for the new relation
			ConceptHierarchicalRelationship.Id id = new ConceptHierarchicalRelationship.Id();
			id.setChildconceptid(childConceptId);
			id.setParentconceptid(parentConceptId);
			relation.setIdentifier(id);
		}
		relation.setRole(hierarchicalRelationView.getRole());
		return relation;
	}
}
