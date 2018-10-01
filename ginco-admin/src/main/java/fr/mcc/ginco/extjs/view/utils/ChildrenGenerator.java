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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListNodeFactory;
import fr.mcc.ginco.extjs.view.node.WarningNode;
import fr.mcc.ginco.services.IThesaurusConceptService;

/**
 * Class used to generate nodes containing children of given concept (by its
 * ID).
 */
@Component(value = "childrenGenerator")
public class ChildrenGenerator {

	public static final String ID_PREFIX = ThesaurusListNodeType.CONCEPT
			.toString() + "_";

	/**
	 * Separator between parent ID and ID of child. For example, Root node has
	 * id "CONCEPT_co1". So, ID of all children will be
	 * "CONCEPT_co1<b>*</b>CHILDREN_ID"
	 */
	public static final String PARENT_SEPARATOR = "*";

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusListNodeFactory")
	private ThesaurusListNodeFactory thesaurusListNodeFactory;

	private Logger logger  = LoggerFactory.getLogger(ChildrenGenerator.class);


	@Value("${conceptstree.maxresults}")
	private int maxResults;

	public List<IThesaurusListNode> getChildrenByConceptId(
			String conceptTopTermId) {
		logger.debug("Generating children concepts list for conceptTopTermId : "
				+ conceptTopTermId);

		String resultId = conceptTopTermId.substring(
				conceptTopTermId.indexOf(ChildrenGenerator.PARENT_SEPARATOR)
						+ ChildrenGenerator.PARENT_SEPARATOR.length(),
				conceptTopTermId.length());

		List<ThesaurusConcept> children = thesaurusConceptService
				.getChildrenByConceptId(resultId, maxResults + 1,null);
		logger.debug(children.size() + " children found");
		Boolean hasTooMany = false;
		if (children.size() > maxResults) {
			logger.warn("Limit : " + maxResults + " exceeded");
			hasTooMany = true;
			children.remove(children.size() - 1);
		}

		//MPL 0030210 24/09/2018 debut
		ThesaurusConcept previousTopTerm = new ThesaurusConcept();
		//MPL 0030210 24/09/2018 fin
		
		List<IThesaurusListNode> childrenNodes = new ArrayList<IThesaurusListNode>();
		for (ThesaurusConcept topTerm : children) {
			ThesaurusListBasicNode childNode = thesaurusListNodeFactory
					.getListBasicNode();

			//MPL 0030210 24/09/2018 debut
			if(isPreviouslyChecked(previousTopTerm, topTerm)) {
				continue;
			} else {
				previousTopTerm = topTerm;
			}
			//MPL 0030210 24/09/2018 fin

			//MPL 0030210 27/09/2018 debut (Problem shown since TMA-2.0.9.1)
			childNode.setId(ID_PREFIX + resultId + PARENT_SEPARATOR
					+ topTerm.getIdentifier());
			/*
			String uniqueSalt = "";
			// This random id is for handling unicity on multiparent concept.
			uniqueSalt=UUID.randomUUID().toString();
			childNode.setId(ID_PREFIX +uniqueSalt+"_"+ resultId + PARENT_SEPARATOR
					+ topTerm.getIdentifier());
			*/
			//MPL 0030210 27/09/2018 fin

			childNode.setTitle(thesaurusConceptService.getConceptLabel(topTerm
					.getIdentifier()));
			childNode.setType(ThesaurusListNodeType.CONCEPT);
			childNode.setThesaurusId(topTerm.getThesaurusId());
			childNode.setDisplayable(true);
			childNode.setIconCls("icon-concept");

			if (!thesaurusConceptService.hasChildren(topTerm.getIdentifier())) {
				childNode.setChildren(new ArrayList<IThesaurusListNode>());
				childNode.setLeaf(true);
			} else {
				childNode.setChildren(null);
				childNode.setLeaf(false);
			}
			childrenNodes.add(childNode);
		}
		Collections.sort(childrenNodes);
		if (hasTooMany) {
			WarningNode tooManyNode = new WarningNode(maxResults);
			childrenNodes.add(tooManyNode);
		}
		return childrenNodes;
	}

	//MPL 0030210 24/09/2018 debut
	private boolean isPreviouslyChecked(ThesaurusConcept previous, ThesaurusConcept actual) {
		return previous.getIdentifier() != null && !previous.getIdentifier().isEmpty()
				&& previous.getIdentifier().equals(actual.getIdentifier());
	}
	//MPL 0030210 24/09/2018 fin
}
