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

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.extjs.view.enums.ClassificationFolderType;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;

import java.util.ArrayList;
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
     * ThesaurusListClassificationFolderNode
     * @param title
     * @param parentId
     * @param type
     */
    public ThesaurusListBasicNode(String title, String parentId, ClassificationFolderType type) {
        setChildren(new ArrayList<IThesaurusListNode>());
        setExpanded(false);
        setId(type.toString() + "_" + parentId);
        setType(ThesaurusListNodeType.FOLDER);
        setTitle(title);
    }

    /**
     * ThesaurusListTopNode
     * @param thesaurus
     */
    public ThesaurusListBasicNode(Thesaurus thesaurus) {
        setExpanded(false);
        setTitle(thesaurus.getTitle());
        setId(thesaurus.getIdentifier());
        setType(ThesaurusListNodeType.THESAURUS);
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public List<IThesaurusListNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<IThesaurusListNode> children) {
        this.children = children;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public ThesaurusListNodeType getType() {
        return nodeType;
    }

    @Override
    public void setType(ThesaurusListNodeType type) {
        this.nodeType = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
