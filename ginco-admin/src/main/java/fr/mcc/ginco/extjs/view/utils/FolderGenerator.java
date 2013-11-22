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

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.extjs.view.enums.ClassificationFolderType;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;

/**
 * Component in charge of generating each categorization folder node of a given thesaurus
 */
@Component
public class FolderGenerator {
	
	private static final String UNDERSCORE = "_";

	public static final String ORPHANS_PREFIX = ClassificationFolderType.ORPHANS
			.toString() + UNDERSCORE;
	public static final String CONCEPTS_PREFIX = ClassificationFolderType.CONCEPTS
			.toString() + UNDERSCORE;
	public static final String ARRAYS_PREFIX = ClassificationFolderType.ARRAYS
			.toString() + UNDERSCORE;
	public static final String GROUPS_PREFIX = ClassificationFolderType.GROUPS
			.toString() + UNDERSCORE;

	public static final String SANDBOX_PREFIX = ClassificationFolderType.SANDBOX
			.toString() + UNDERSCORE;

	public static final String COMPLEXCONCEPTS_PREFIX = ClassificationFolderType.COMPLEXCONCEPTS
			.toString() + UNDERSCORE;
	

	@Inject
	private IThesaurusConceptService thesaurusConceptService;
	
	@Inject
	private IThesaurusTermService thesaurusTermService;

	@Inject
	private IThesaurusArrayService thesaurusArrayService;

	@Inject
	private IThesaurusConceptGroupService thesaurusConceptGroupService;
	
	@Inject
	private ISplitNonPreferredTermService splitNonPreferredTermService;


	/**
	 * Builds the main concept node of the left tree
	 * @param parentId
	 * 			the identifier of the thesaurus
	 * @return
	 */
	public IThesaurusListNode getConcepts(String parentId) {
		IThesaurusListNode concepts = new ThesaurusListBasicNode();
		concepts.setTitle("Arborescence des concepts");
		concepts.setId(CONCEPTS_PREFIX + parentId);
		concepts.setType(ThesaurusListNodeType.FOLDER);
		concepts.setIconCls("icon-tree");
		concepts.setExpanded(false);
		concepts.setDisplayable(false);
		long nbTopConcepts = thesaurusConceptService
				.getTopTermThesaurusConceptsCount(parentId);
		if (nbTopConcepts > 0) {
			concepts.setChildren(null);
		} else {
			concepts.setChildren(new ArrayList<IThesaurusListNode>());
		}
		return concepts;
	}

	/**
	 * Builds the sandbox node of the left tree.
	 * @param parentId
	 * 			the identifier of the thesaurus
	 * @return the node, null if there are no terms in sandbox
	 * 		
	 */
	public IThesaurusListNode getSandbox(String parentId) {
		IThesaurusListNode sandbox = new ThesaurusListBasicNode();
		sandbox.setTitle("Termes orphelins");
		sandbox.setId(SANDBOX_PREFIX + parentId);
		sandbox.setType(ThesaurusListNodeType.FOLDER);
		sandbox.setIconCls("sandbox");
		sandbox.setExpanded(false);
		sandbox.setDisplayable(true);
		
		long nbSandbox = thesaurusTermService.getSandboxedTermsCount(parentId);
		if (nbSandbox>0) {
				sandbox.setChildren(new ArrayList<IThesaurusListNode>());
		} else {
			return null;
		}
		return sandbox;
	}

	/**
	 * Builds the complex concepts node of the tree
	 * @param parentId
	 * 			the identifier of the thesaurus
	 * @return the node, null if there are no complex concepts
	 */
	public IThesaurusListNode getSplitNonPreferredTerms(String parentId) {
		IThesaurusListNode complexConceptNode = new ThesaurusListBasicNode();
		complexConceptNode.setTitle("Concepts complexes");
		complexConceptNode.setId(COMPLEXCONCEPTS_PREFIX + parentId);
		complexConceptNode.setType(ThesaurusListNodeType.FOLDER);
		complexConceptNode.setIconCls("icon-complex-concept");
		complexConceptNode.setExpanded(false);
		complexConceptNode.setDisplayable(true);
		
		long nbComplex = splitNonPreferredTermService.getSplitNonPreferredTermCount(parentId);
		if (nbComplex >0) {
			complexConceptNode.setChildren(new ArrayList<IThesaurusListNode>());
		} else {
			return null;
		}
		return complexConceptNode;
	}

	/**
	 * Builds the array node of the left tree
	 * @param parentId
	 * 			the identifier of the thesaurus
	 * @return the node, null if there are no arrays
	 */
	public IThesaurusListNode getArrays(String parentId) {
		IThesaurusListNode arrays = new ThesaurusListBasicNode();
		arrays.setTitle("Tableaux");
		arrays.setId(ARRAYS_PREFIX + parentId);
		arrays.setType(ThesaurusListNodeType.FOLDER);
		arrays.setExpanded(false);
		arrays.setIconCls("icon-table");
		arrays.setDisplayable(false);
		List<ThesaurusArray> realArrays = thesaurusArrayService
				.getAllThesaurusArrayByThesaurusId(null, parentId);
		if (realArrays != null && realArrays.size() > 0) {
			arrays.setChildren(null);
		} else {
			return null;
		}
		return arrays;
	}

	/**
	 * Builds the orphan concepts node of the left tree
	 * @param parentId
	 * 			the identifier of the thesaurus
	 * @return the node, null if there are no orphan concepts
	 */
	public IThesaurusListNode getOrphans(String parentId){
		IThesaurusListNode orphans = new ThesaurusListBasicNode();
		orphans.setTitle("Concepts orphelins");
		orphans.setId(ORPHANS_PREFIX + parentId);
		orphans.setType(ThesaurusListNodeType.FOLDER);
		orphans.setIconCls("sandbox");
		orphans.setExpanded(false);
		orphans.setDisplayable(false);
		long nbOrphans = thesaurusConceptService
				.getOrphanThesaurusConceptsCount(parentId);
		if (nbOrphans > 0) {
			orphans.setChildren(null);
		} else {
			return null;
		}
		return orphans;
	}

	/**
	 * Builds the group node of the left tree
	 * @param parentId
	 * 			the identifier of the thesaurus
	 * @return the node, null if there are no groups concepts
	 */
	public IThesaurusListNode getGroups(String parentId) {
		IThesaurusListNode groups = new ThesaurusListBasicNode();
		groups.setTitle("Groupes");
		groups.setId(GROUPS_PREFIX + parentId);
		groups.setType(ThesaurusListNodeType.FOLDER);
		groups.setExpanded(false);
		groups.setIconCls("icon-group");
		groups.setChildren(new ArrayList<IThesaurusListNode>());
		groups.setDisplayable(false);

		List<ThesaurusConceptGroup> realArrays = thesaurusConceptGroupService
				.getAllThesaurusConceptGroupsByThesaurusId(null, parentId);
		if (realArrays != null && realArrays.size() > 0) {
			groups.setChildren(null);
		} else {
			return null;
		}

		return groups;
	}

}
