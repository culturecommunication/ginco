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

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusConceptService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator in charge of building concept orphans list
 */
@Component(value = "orphansGenerator")
public class OrphansGenerator {

  	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Log
	private Logger logger;

	/**
	 * Creates a list of orphan concepts for a given thesaurus
	 * 
	 * @param parentId
	 *            id of the thesaurus.
	 * @return created list of leafs.
	 */
	public List<IThesaurusListNode> generateOrphans(String parentId)
			throws BusinessException {
		logger.debug("Generating orphans concepts list for vocabularyId : " + parentId);
		List<ThesaurusConcept> orphans = thesaurusConceptService
				.getOrphanThesaurusConcepts(parentId);
		logger.debug(orphans.size() + " orphans found");

		List<IThesaurusListNode> newOrphans = new ArrayList<IThesaurusListNode>();
		for (ThesaurusConcept orphan : orphans) {
			ThesaurusListBasicNode orphanNode = new ThesaurusListBasicNode();
			orphanNode.setTitle(thesaurusConceptService.getConceptLabel(orphan
					.getIdentifier()));
			orphanNode
                    .setId(ChildrenGenerator.ID_PREFIX
                            + ChildrenGenerator.PARENT_SEPARATOR
                            + orphan.getIdentifier());
			orphanNode.setType(ThesaurusListNodeType.CONCEPT);
			orphanNode.setExpanded(false);
            orphanNode.setThesaurusId(orphan.getThesaurusId());

            if(!thesaurusConceptService.hasChildren(orphan.getIdentifier())) {
                orphanNode.setChildren(new ArrayList<IThesaurusListNode>());
			    orphanNode.setLeaf(true);
            } else {
                orphanNode.setChildren(null);
                orphanNode.setLeaf(false);
            }

			newOrphans.add(orphanNode);
		}
		return newOrphans;
	}

}
