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

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ClassificationFolderType;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;

/**
 * Class used to generate categorization folder nodes of given thesaurus (by its
 * ID).
 */
@Component(value = "folderGenerator")
public class FolderGenerator {

	public static final String ORPHANS_PREFIX = ClassificationFolderType.ORPHANS
			.toString() + "_";
	public static final String CONCEPTS_PREFIX = ClassificationFolderType.CONCEPTS
			.toString() + "_";
	public static final String ARRAYS_PREFIX = ClassificationFolderType.ARRAYS
			.toString() + "_";

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;

	@Log
	private Logger logger;

	/**
	 * Creates categorization folders.
	 * 
	 * @param parentId
	 *            id of top node.
	 * @return created list of folders.
	 */
	public List<IThesaurusListNode> generateFolders(String parentId)
			throws BusinessException {
		logger.debug("Calling FolderGenerator.generateFolders with parameters : {");
		logger.debug("	parentId : " + parentId);
		logger.debug("	} ");

		List<IThesaurusListNode> list = new ArrayList<IThesaurusListNode>();

		list.add(getConcepts(parentId));

		list.add(getSandbox(parentId));

		list.add(getOrphans(parentId));

		list.add(getGroups(parentId));

		list.add(getArrays(parentId));

		return list;
	}

	private IThesaurusListNode getConcepts(String parentId)
			throws BusinessException {
		IThesaurusListNode concepts = new ThesaurusListBasicNode();
		concepts.setTitle("Arborescence des concepts");
		concepts.setId(CONCEPTS_PREFIX + parentId);
		concepts.setType(ThesaurusListNodeType.FOLDER);
		concepts.setExpanded(false);
		long nbTopConcepts = thesaurusConceptService
				.getTopTermThesaurusConceptsCount(parentId);
		if (nbTopConcepts > 0) {
			concepts.setChildren(null);
		} else {
			concepts.setChildren(new ArrayList<IThesaurusListNode>());
		}
		return concepts;
	}

	private IThesaurusListNode getSandbox(String parentId) {
		IThesaurusListNode sandbox = new ThesaurusListBasicNode();
		sandbox.setTitle("Bac à sable");
		sandbox.setId(ClassificationFolderType.SANDBOX.toString() + "_"
				+ parentId);
		sandbox.setType(ThesaurusListNodeType.FOLDER);
		sandbox.setIconCls("sandbox");
		sandbox.setExpanded(false);
		sandbox.setChildren(new ArrayList<IThesaurusListNode>());
		return sandbox;
	}

	private IThesaurusListNode getArrays(String parentId) {
		IThesaurusListNode arrays = new ThesaurusListBasicNode();
		arrays.setTitle("Tableaux");
		arrays.setId(ClassificationFolderType.ARRAYS.toString() + "_"
				+ parentId);
		arrays.setType(ThesaurusListNodeType.FOLDER);
		arrays.setExpanded(false);
		List<ThesaurusArray> realArrays = thesaurusArrayService
				.getAllThesaurusArrayByThesaurusId(parentId);
		if (realArrays != null && realArrays.size() > 0) {
			arrays.setChildren(null);
		} else {
			arrays.setChildren(new ArrayList<IThesaurusListNode>());
		}
		return arrays;
	}

	private IThesaurusListNode getOrphans(String parentId)
			throws BusinessException {
		IThesaurusListNode orphans = new ThesaurusListBasicNode();
		orphans.setTitle("Concepts orphelins");
		orphans.setId(ORPHANS_PREFIX + parentId);
		orphans.setType(ThesaurusListNodeType.FOLDER);
		orphans.setExpanded(false);
		long nbOrphans = thesaurusConceptService
				.getOrphanThesaurusConceptsCount(parentId);
		if (nbOrphans > 0) {
			orphans.setChildren(null);
		} else {
			orphans.setChildren(new ArrayList<IThesaurusListNode>());
		}
		return orphans;
	}

	private IThesaurusListNode getGroups(String parentId) {
		IThesaurusListNode groups = new ThesaurusListBasicNode();
		groups.setTitle("Groupes");
		groups.setId(ClassificationFolderType.GROUPS.toString() + "_"
				+ parentId);
		groups.setType(ThesaurusListNodeType.FOLDER);
		groups.setExpanded(false);
		groups.setChildren(new ArrayList<IThesaurusListNode>());
		return groups;
	}

}
