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

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListNodeFactory;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.utils.LabelUtil;

/**
 * Generator in charge of building thesaurus arrays list
 */
@Component(value = "arraysGenerator")
public class ArraysGenerator {

	 public static final String ID_PREFIX = ThesaurusListNodeType.ARRAYS
	            .toString() + "_";
  	@Inject
	@Named("thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;
  	
	@Inject
	@Named("nodeLabelService")
	private INodeLabelService nodeLabelService;
	
	@Inject
	@Named("thesaurusListNodeFactory")
	ThesaurusListNodeFactory thesaurusListNodeFactory;

	private Logger logger  = LoggerFactory.getLogger(ArraysGenerator.class);


	@Value("${ginco.default.language}") private String defaultLanguage;

	/**
	 * Creates a list of orphan concepts for a given thesaurus
	 * 
	 * @param parentId
	 *            id of the thesaurus.
	 * @return created list of leafs.
	 */
	public List<IThesaurusListNode> generateArrays(String thesaurusId)
			throws BusinessException {
		logger.debug("Generating thesaurus arrays list for vocabularyId : " + thesaurusId);
		List<ThesaurusArray> arrays = thesaurusArrayService.getAllThesaurusArrayByThesaurusId(null, thesaurusId);
		logger.debug(arrays.size() + " arrays found");

		List<IThesaurusListNode> newArrays = new ArrayList<IThesaurusListNode>();
		for (ThesaurusArray array : arrays) {
			ThesaurusListBasicNode arrayNode = thesaurusListNodeFactory.getListBasicNode();
			NodeLabel label = nodeLabelService.getByThesaurusArray(array.getIdentifier());
			arrayNode.setTitle(LabelUtil.getLocalizedLabel(label.getLexicalValue(), label.getLanguage(), defaultLanguage));
			
			arrayNode
                    .setId(array.getIdentifier());
			arrayNode.setType(ThesaurusListNodeType.ARRAYS);
			arrayNode.setExpanded(false);
			arrayNode.setThesaurusId(array.getThesaurus().getIdentifier());

			arrayNode.setChildren(new ArrayList<IThesaurusListNode>());
			arrayNode.setLeaf(true);
            arrayNode.setDisplayable(true);

			newArrays.add(arrayNode);
		}
		Collections.sort(newArrays);
		return newArrays;
	}

}
