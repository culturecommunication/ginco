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

import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;

import java.util.List;

/**
 * Class with general behaviour to be shared with every implementation.
 */
public class ThesaurusListBasicNode implements IThesaurusListNode {
    /**
     * Indicates if node should be expanded by default.
     */
    private boolean expanded;
    /**
     * List of all children, now only String lines.
     */
    private List<IThesaurusListNode> children;
    /**
     * Title to display for user.
     */
    private String title;
    /**
     * Service tag - not visible in UI.
     */
    private String id;
    /**
     * Type of visual node.
     */
    private ThesaurusListNodeType nodeType;
    
    /**
     * if the node is a leaf.
     */
    private boolean leaf;
    
    /**
     * Css class of the node
     */
    private String iconcls;

    /**
     * Parent Thesaurus of the node
     */
    private String thesaurusId;
    
    
    

    public ThesaurusListBasicNode() {

    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#isExpanded()
     */
    @Override
    public boolean isExpanded() {
        return expanded;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setExpanded(boolean)
     */
    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#getChildren()
     */
    @Override
    public List<IThesaurusListNode> getChildren() {
        return children;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setChildren(java.util.List)
     */
    @Override
    public void setChildren(List<IThesaurusListNode> children) {
        this.children = children;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setTitle(java.lang.String)
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#getType()
     */
    @Override
    public ThesaurusListNodeType getType() {
        return nodeType;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setType(fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType)
     */
    @Override
    public void setType(ThesaurusListNodeType type) {
        this.nodeType = type;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }


	/* (non-Javadoc)
	 * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#isLeaf()
	 */
    @Override
	public boolean isLeaf() {
		return leaf;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setLeaf(boolean)
	 */
    @Override
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}


	/* (non-Javadoc)
	 * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#getCls()
	 */
    @Override
	public String getIconCls() {
		return iconcls;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setCls(string)
	 */
    @Override
	public void setIconCls(String iconcls) {
		this.iconcls = iconcls;
	}

    /* (non-Javadoc)
	 * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#getThesaurusId
	 */
    @Override
    public String getThesaurusId() {
        return thesaurusId;
    }

    /* (non-Javadoc)
	 * @see fr.mcc.ginco.extjs.view.node.IThesaurusListNode#setThesaurusId(string)
	 */
    @Override
    public void setThesaurusId(String thesaurusId) {
        this.thesaurusId = thesaurusId;
    }

}
