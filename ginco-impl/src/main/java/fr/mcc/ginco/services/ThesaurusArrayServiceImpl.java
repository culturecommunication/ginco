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
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.services;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.helpers.ThesaurusArrayHelper;

/**
 * Implementation of the thesaurus array service contains methods relatives to
 * the ThesaurusArray object
 */
@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("thesaurusArrayService")
public class ThesaurusArrayServiceImpl implements IThesaurusArrayService {

	@Inject
	@Named("thesaurusArrayDAO")
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@Inject
	@Named("nodeLabelService")
	private INodeLabelService nodeLabelService;

	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Inject
	@Named("thesaurusArrayHelper")
	private ThesaurusArrayHelper thesaurusArrayHelper;

	@Override
	public ThesaurusArray getThesaurusArrayById(String id) {
		return thesaurusArrayDAO.getById(id);
	}

	@Override
	public List<ThesaurusArray> getAllThesaurusArrayByThesaurusId(
			String thesaurusId) {
		return thesaurusArrayDAO
				.getThesaurusArrayListByThesaurusId(thesaurusId);
	}

	@Override
	@Transactional(readOnly = false)
	public ThesaurusArray updateOnlyThesaurusArray(ThesaurusArray thesaurusArray)
			throws BusinessException {
		return thesaurusArrayDAO.update(thesaurusArray);
	}

	@Transactional(readOnly = false)
	@Override
	public ThesaurusArray updateThesaurusArray(ThesaurusArray thesaurusArray,
			NodeLabel nodeLabel, List<ThesaurusArrayConcept> arrayConcepts)
			throws BusinessException {

		if (thesaurusArray.getSuperOrdinateConcept() != null) {
			if (thesaurusArray.getSuperOrdinateConcept().getStatus() != ConceptStatusEnum.VALIDATED
					.getStatus()) {
				throw new BusinessException(
						"Only a validated concept can be a parent for a concept array",
						"only-validated-concept-parent-of-concept-array");
			}
		}

		if (thesaurusArray.getConcepts() != null
				&& thesaurusArray.getSuperOrdinateConcept() != null) {
			// We get all arrays matching our superordinate, excluding our
			// concept from the list
			List<ThesaurusArray> arrayWithSameSuperOrdinate = thesaurusArrayDAO
					.getConceptSuperOrdinateArrays(thesaurusArray
							.getSuperOrdinateConcept().getIdentifier(),
							thesaurusArray.getIdentifier());
			Set<ThesaurusArrayConcept> allChildren = thesaurusArray
					.getConcepts();

			for (ThesaurusArray currentArray : arrayWithSameSuperOrdinate) {
				Set<ThesaurusArrayConcept> conceptOfEachArray = currentArray
						.getConcepts();
				for (ThesaurusArrayConcept thesaurusConcept : conceptOfEachArray) {
					if (allChildren.contains(thesaurusConcept)) {
						// Another array with same superordinate contains a
						// concept we have included in our array
						throw new BusinessException(
								"A concept included in this array is already included in a similar array (same parent concept)",
								"array-concept-included-twice");
					}
				}
			}

			// We test that the select children are child of superordinate
			for (ThesaurusArrayConcept thesaurusArrayConcept : allChildren) {
				Set<ThesaurusConcept> parentsOfChild = thesaurusConceptDAO
						.getById(
								thesaurusArrayConcept.getIdentifier()
										.getConceptId()).getParentConcepts();
				if (!parentsOfChild.contains(thesaurusArray
						.getSuperOrdinateConcept())) {
					throw new BusinessException(
							"A concept is not a child of the selected parent concept",
							"concept-not-child-of-superordinate");
				}
			}
		}

		ThesaurusArray updated = thesaurusArrayDAO.update(thesaurusArray);

		thesaurusArrayHelper.saveArrayConcepts(updated, arrayConcepts);
		nodeLabel.setThesaurusArray(updated);
		nodeLabelService.updateOrCreate(nodeLabel);
		return updated;
	}

	@Transactional(readOnly = false)
	@Override
	public ThesaurusArray destroyThesaurusArray(ThesaurusArray thesaurusArray) {
		return thesaurusArrayDAO.delete(thesaurusArray);
	}

	@Override
	public List<ThesaurusArray> getSubOrdinatedArrays(String thesaurusConceptId) {
		return thesaurusArrayDAO
				.getConceptSuperOrdinateArrays(thesaurusConceptId);
	}

	@Override
	public List<ThesaurusArray> getArraysWithoutParentConcept(String thesaurusId) {
		return thesaurusArrayDAO
				.getArraysWithoutSuperordinatedConcept(thesaurusId);
	}

}