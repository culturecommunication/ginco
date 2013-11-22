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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;

/**
 * Component in charge of generating categorization folder nodes of a given thesaurus (by its
 * ID).
 */
@Component(value = "foldersGenerator")
public class FoldersGenerator {	
	
	@Inject
	private FolderGenerator folderGenerator;

	private Logger logger  = LoggerFactory.getLogger(FoldersGenerator.class);


	/**
	 * Creates categorization folders.
	 *
	 * @param parentId
	 *            id of top node.
	 * @return created list of folders.
	 */
	public List<IThesaurusListNode> generateFolders(String parentId) {
		logger.debug("Calling FolderGenerator.generateFolders with parameters : {");
		logger.debug("	parentId : " + parentId);
		logger.debug("	} ");

		List<IThesaurusListNode> list = new ArrayList<IThesaurusListNode>();

		list.add(folderGenerator.getConcepts(parentId));

		IThesaurusListNode complexConcepts = folderGenerator.getSplitNonPreferredTerms(parentId);
		if (complexConcepts != null) {
			list.add(complexConcepts);
		}
		
		IThesaurusListNode groups = folderGenerator.getGroups(parentId);
		if (groups != null) {
			list.add(groups);
		}
		
		IThesaurusListNode arrays = folderGenerator.getArrays(parentId);
		if (arrays != null) {
			list.add(arrays);
		}

		IThesaurusListNode orphans = folderGenerator.getOrphans(parentId);
		if (orphans != null) {
			list.add(orphans);
		}

		IThesaurusListNode sandbox = folderGenerator.getSandbox(parentId);
		if (sandbox != null) {
			list.add(sandbox);
		}

		return list;
	}

}
