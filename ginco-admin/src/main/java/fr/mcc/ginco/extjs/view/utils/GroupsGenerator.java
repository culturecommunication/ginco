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

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusConceptGroupLabelService;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;

/**
 * Generator in charge of building thesaurus groups list
 */
@Component(value = "groupsGenerator")
public class GroupsGenerator {

	 public static final String ID_PREFIX = ThesaurusListNodeType.GROUPS
	            .toString() + "_";
  	@Inject
	@Named("thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;
  	
	@Inject
	@Named("thesaurusConceptGroupLabelService")
	private IThesaurusConceptGroupLabelService thesaurusConceptGroupLabelService;

	@Log
	private Logger logger;

	/**
	 * Creates the list of groups for a given thesaurus
	 * 
	 * @param parentId
	 *            id of the thesaurus.
	 * @return created list of leafs.
	 */
	public List<IThesaurusListNode> generateGroups(String thesaurusId)
			throws BusinessException {
		logger.debug("Generating thesaurus groups list for vocabularyId : " + thesaurusId);
		List<ThesaurusConceptGroup> groups = thesaurusConceptGroupService.getAllThesaurusConceptGroupsByThesaurusId(thesaurusId);
		logger.debug(groups.size() + " groups found");

		List<IThesaurusListNode> newGroups = new ArrayList<IThesaurusListNode>();
		for (ThesaurusConceptGroup group : groups) {
			ThesaurusListBasicNode groupeNode = new ThesaurusListBasicNode();
			ThesaurusConceptGroupLabel label = thesaurusConceptGroupLabelService.getByThesaurusConceptGroup(group.getIdentifier());
			groupeNode.setTitle(label.getLexicalValue());
			
			groupeNode.setId(group.getIdentifier());
			groupeNode.setType(ThesaurusListNodeType.GROUPS);
			groupeNode.setExpanded(false);
			groupeNode.setThesaurusId(group.getThesaurus().getIdentifier());

			groupeNode.setChildren(new ArrayList<IThesaurusListNode>());
			groupeNode.setLeaf(true);
            

			newGroups.add(groupeNode);
		}
		return newGroups;
	}
}
