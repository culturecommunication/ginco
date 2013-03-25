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
package fr.mcc.ginco.extjs.view.node;

import java.util.List;

import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;

/**
 * Interface for generic node in list of nodes.
 */
public interface IThesaurusListNode extends Comparable<IThesaurusListNode>{	
	
    /**
     * @return true if the node should be expanded by default.
     */
    boolean isExpanded();
   
    /**
     * sets whether the node should be expanded by default
     * @param expanded
     */
    void setExpanded(boolean expanded);

    /**
     * @return the list of all node children
     */
    List<IThesaurusListNode> getChildren();

    /**
     * sets the list of all node children
     * @param children
     */
    void setChildren(List<IThesaurusListNode> children);

    /**
     * @return the node title displayed to the user
     */
    String getTitle();

    /**
     * sets the node title displayed to the user
     * @param title
     */
    void setTitle(String title);

    /**
     * @return  the node type
     */
    ThesaurusListNodeType getType();

    /**
     * Sets the node type
     * @param type
     */
    void setType(ThesaurusListNodeType type);

    /**
     * @return the service tag (not visible in UI)
     */
    String getId();

    /**
     * Sets the service tag (not visible in UI.)
     * @param id
     */
    void setId(String id);
    
    /**
     * @return true if the current node is a leaf
     */
    boolean isLeaf();

	/**
	 * Sets whether the currentnode is a leaf
	 * @param leaf
	 */
	void setLeaf(boolean leaf);
	
	 /**
     * @return the css class of the icon node
     */
    String getIconCls();

	/**
	 * Set the css class of the icon node
	 * @param cls
	 */
	void setIconCls(String cls);

    /**
     * Get the parent ThesaurusId
     * @return
     */
    String getThesaurusId();

    /**
     * Set the parent ThesaurusId
     * @param thesaurusId
     */
    void setThesaurusId(String thesaurusId);
    
    /**
     * @return true if the current node is displayable
     */
    boolean isDisplayable();

	/**
	 * Sets whether the current node is displayable
	 * @param displayable
	 */
	void setDisplayable(boolean displayable);
}
