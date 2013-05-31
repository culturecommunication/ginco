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
package fr.mcc.ginco.exports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.AlphabeticalExportedItem;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.helpers.ThesaurusArrayHelper;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.utils.ThesaurusTermUtils;

@Service("exportService")
public class ExportServiceImpl implements IExportService {
	@Inject
	@Named("alphabeticConceptExporter")
	private AlphabeticConceptExporter alphabeticConceptExporter;
	

	@Inject
	@Named("alphabeticComplexConceptExporter")
	private AlphabeticComplexConceptExporter alphabeticComplexConceptExporter;
	
	@Inject
	@Named("alphabeticalExportedItemComparator")
	private AlphabeticalExportedItemComparator alphabeticalExportedItemComparator;

	@Inject
	@Named("nodeLabelService")
	private INodeLabelService nodeLabelService;

	@Inject
	@Named("splitNonPreferredTermService")
	private ISplitNonPreferredTermService splitNonPreferredTermService;

	@Inject
	@Named("thesaurusArrayHelper")
	private ThesaurusArrayHelper thesaurusArrayHelper;

	@Inject
	@Named("thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;	

	@Inject
	@Named("thesaurusConceptComparator")
	private ThesaurusConceptComparator thesaurusConceptComparator;

	@Inject
	@Named("nodeLabelComparator")
	private NodeLabelComparator nodeLabelComparator;

	@Inject
	@Named("arrayNaturalComparator")
	private ArrayNaturalComparator arrayNaturalComparator;

	@Inject
	@Named("thesaurusTermUtils")
	private ThesaurusTermUtils thesaurusTermUtils;

	@Override
	public List<FormattedLine> getHierarchicalText(Thesaurus thesaurus)
			throws BusinessException {
		List<ThesaurusConcept> listTT = thesaurusConceptService
				.getTopTermThesaurusConcepts(thesaurus.getIdentifier());
		Collections.sort(listTT, thesaurusConceptComparator);
		
		List<ThesaurusArray> orphanArrays = thesaurusArrayService
				.getArraysWithoutParentConcept(thesaurus.getIdentifier());
		Collections.sort(orphanArrays, nodeLabelComparator);
		
		List<ThesaurusArray> arraysWithoutParentArray = thesaurusArrayService.getArraysWithoutParentArray(thesaurus.getIdentifier());
		Collections.sort(arraysWithoutParentArray, nodeLabelComparator);
		
		Set<ThesaurusConcept> exclude = new HashSet<ThesaurusConcept>();

		for (ThesaurusArray array : orphanArrays) {
			exclude.addAll(thesaurusArrayHelper.getArrayConcepts(array
					.getIdentifier()));
		}

		List<FormattedLine> result = new ArrayList<FormattedLine>();

		for (ThesaurusConcept conceptTT : listTT) {
			if (!exclude.contains(conceptTT)) {
				result.addAll(getHierarchicalText(0, conceptTT));
			}
		}

		for (ThesaurusArray array : arraysWithoutParentArray) {
			if (orphanArrays.contains(array)){
				addThesaurusArray(result, array, -1);
			}
		}

		return result;
	}

	@Override
	public List<FormattedLine> getAlphabeticalText(Thesaurus thesaurus)
			throws BusinessException {
		String thesaurusId = thesaurus.getThesaurusId();
		List<FormattedLine> result = new ArrayList<FormattedLine>();

		List<AlphabeticalExportedItem> itemsToExport = new ArrayList<AlphabeticalExportedItem>();
		List<ThesaurusConcept> concepts = thesaurusConceptService
				.getConceptsByThesaurusId(null, thesaurusId, null, Boolean.TRUE);

		for (ThesaurusConcept thesaurusConcept : concepts) {
			AlphabeticalExportedItem item = new AlphabeticalExportedItem();
			item.setLexicalValue(thesaurusConceptService
					.getConceptTitle(thesaurusConcept));
			item.setObjectToExport(thesaurusConcept);
			itemsToExport.add(item);
		}

		List<SplitNonPreferredTerm> complexConcepts = splitNonPreferredTermService
				.getSplitNonPreferredTermList(0, splitNonPreferredTermService
						.getSplitNonPreferredTermCount(thesaurusId).intValue(),
						thesaurusId);
		for (SplitNonPreferredTerm complexConcept : complexConcepts) {
			AlphabeticalExportedItem item = new AlphabeticalExportedItem();
			item.setLexicalValue(complexConcept.getLexicalValue());
			item.setObjectToExport(complexConcept);
			itemsToExport.add(item);
		}

		Collections.sort(itemsToExport, alphabeticalExportedItemComparator);

		for (AlphabeticalExportedItem item : itemsToExport) {
			if (ThesaurusConcept.class.equals(item.getObjectToExport()
					.getClass())) {
				addConceptTitle(0, result,
						(ThesaurusConcept) item.getObjectToExport());
				alphabeticConceptExporter.addConceptInfo(1, result,
						(ThesaurusConcept) item.getObjectToExport());
			} else if (SplitNonPreferredTerm.class.equals(item
					.getObjectToExport().getClass())) {
				alphabeticComplexConceptExporter.addComplexConceptTitle(0,
						result,
						(SplitNonPreferredTerm) item.getObjectToExport());
				alphabeticComplexConceptExporter.addComplexConceptInfo(1,
						result,
						(SplitNonPreferredTerm) item.getObjectToExport());
			} else {
				throw new BusinessException(
						"non-available-type-for-alpha-export",
						"Object type non available for alphabetical export");
			}
		}
		return result;
	}

	private List<FormattedLine> getHierarchicalText(Integer base,
			ThesaurusConcept concept) throws BusinessException {
		List<FormattedLine> result = new ArrayList<FormattedLine>();

		Set<ThesaurusConcept> thesaurusArrayConcepts = new HashSet<ThesaurusConcept>();

		addConceptTitle(base, result, concept);

		List<ThesaurusArray> subOrdArrays = thesaurusArrayService
				.getSubOrdinatedArrays(concept.getIdentifier());
		Collections.sort(subOrdArrays, nodeLabelComparator);

		for (ThesaurusArray subOrdArray : subOrdArrays) {
			thesaurusArrayConcepts.addAll(thesaurusArrayHelper
					.getArrayConcepts(subOrdArray.getIdentifier()));
		}

		List<ThesaurusConcept> children = new ArrayList<ThesaurusConcept>(
				thesaurusConceptService.getChildrenByConceptId(concept
						.getIdentifier()));
		Collections.sort(children, thesaurusConceptComparator);

		for (ThesaurusConcept child : children) {
			if (!thesaurusArrayConcepts.contains(child)) {
				result.addAll(getHierarchicalText(base + 1, child));
			}
		}

		for (ThesaurusArray subOrdArray : subOrdArrays) {
			if (subOrdArray.getParent()==null) {
				addThesaurusArray(result, subOrdArray, base);
			}
		}

		return result;
	}
	
	private void addConceptTitle(Integer base, List<FormattedLine> result,
			ThesaurusConcept concept) {
		result.add(new FormattedLine(base, thesaurusConceptService
				.getConceptTitle(concept)));
	}

	private void addThesaurusArray(List<FormattedLine> result,
			ThesaurusArray subOrdArray, Integer base) throws BusinessException {
		NodeLabel nodeLabel = nodeLabelService.getByThesaurusArray(subOrdArray
				.getIdentifier());
		result.add(new FormattedLine(base + 1, "<"
				+ nodeLabel.getLexicalValue() + ">"));
		List<ThesaurusArray> childrenArray = thesaurusArrayService.getChildrenArrays(subOrdArray.getIdentifier());
		Collections.sort(childrenArray, nodeLabelComparator);
		
		for (ThesaurusArray children : childrenArray) {
				addThesaurusArray(result, children, base + 1);
		}
		if (subOrdArray.getOrdered()) {
			List<ThesaurusConcept> conceptsInArray = thesaurusArrayHelper
					.getArrayConcepts(subOrdArray.getIdentifier());
			Collections.sort(conceptsInArray, thesaurusConceptComparator);
			for (ThesaurusConcept conceptInArray : conceptsInArray) {
				result.addAll(getHierarchicalText(base + 1, conceptInArray));
			}
		} else {
			List<ThesaurusArrayConcept> conceptsInArray = thesaurusArrayHelper
					.getArrayConceptRelations(subOrdArray);
			Collections.sort(conceptsInArray, arrayNaturalComparator);
			for (ThesaurusArrayConcept conceptInArray : conceptsInArray) {
				result.addAll(getHierarchicalText(base + 1,
						thesaurusConceptService
								.getThesaurusConceptById(conceptInArray
										.getIdentifier().getConceptId())));
			}
		}
		
		
	}
}