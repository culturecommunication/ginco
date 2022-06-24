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
import java.util.Set;

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
 * Generator in charge of building top term concepts
 */
@Component(value = "topTermGenerator")
public class TopTermGenerator {

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusListNodeFactory")
	private ThesaurusListNodeFactory thesaurusListNodeFactory;

	private Logger logger = LoggerFactory.getLogger(TopTermGenerator.class);


	@Value("${conceptstree.maxresults}")
	private int maxResults;

	/**
	 * Creates the list of top concepts for a given thesaurusId
	 *
	 * @param thesaurusId id of top node.
	 * @return created list of leafs.
	 */
	public List<IThesaurusListNode> generateTopTerm(String thesaurusId) {
		logger.debug("Generating top term concepts list for thesaurusId : "
				+ thesaurusId);
		List<ThesaurusConcept> topTerms = thesaurusConceptService
				.getTopTermThesaurusConcepts(thesaurusId, maxResults + 1);
		logger.debug(topTerms.size() + " top terms found");
		Boolean hasTooMany = false;
		if (topTerms.size() > maxResults) {
			logger.warn("Limit : " + maxResults + " exceeded");
			hasTooMany = true;
			topTerms.remove(topTerms.size() - 1);
		}
		List<IThesaurusListNode> topConcepts = new ArrayList<IThesaurusListNode>();
		Set<String> conceptsWithChildren = thesaurusConceptService.getConceptWithChildrenIdentifers(thesaurusId);
		
		//MPL 0030210 28/08/2018 debut
		ThesaurusConcept previousTopTerm = new ThesaurusConcept();
		//MPL 0030210 28/08/2018 fin
		
		for (ThesaurusConcept topTerm : topTerms) {
			ThesaurusListBasicNode topTermNode = thesaurusListNodeFactory.getListBasicNode();

			//MPL 0030210 28/08/2018 debut
			if(isPreviouslyChecked(previousTopTerm, topTerm)) {
				continue;
			} else {
				previousTopTerm = topTerm;
			}
			//MPL 0030210 28/08/2018 fin
			
			topTermNode.setTitle(thesaurusConceptService
					.getConceptLabel(topTerm.getIdentifier()));
			topTermNode
					.setId(ChildrenGenerator.ID_PREFIX
							+ ChildrenGenerator.PARENT_SEPARATOR
							+ topTerm.getIdentifier());
			topTermNode.setType(ThesaurusListNodeType.CONCEPT);
			topTermNode.setThesaurusId(topTerm.getThesaurusId());
			topTermNode.setDisplayable(true);
			topTermNode.setIconCls("icon-top-concept");
			//if(!thesaurusConceptService.hasChildren(topTerm.getIdentifier())) {
			if (!conceptsWithChildren.contains(topTerm.getIdentifier())) {
				topTermNode.setChildren(new ArrayList<IThesaurusListNode>());
				topTermNode.setLeaf(true);
			} else {
				topTermNode.setChildren(null);
				topTermNode.setLeaf(false);
			}
			topConcepts.add(topTermNode);
		}
		Collections.sort(topConcepts);
		if (hasTooMany) {
			WarningNode tooManyNode = new WarningNode(maxResults);
			topConcepts.add(tooManyNode);
		}
		return topConcepts;
	}

	//MPL 0030210 28/08/2018 debut
	private boolean isPreviouslyChecked(ThesaurusConcept previous, ThesaurusConcept actual) {
		return previous.getIdentifier() != null && !previous.getIdentifier().isEmpty()
				&& previous.getIdentifier().equals(actual.getIdentifier());
	}
	//MPL 0030210 28/08/2018 fin
}
