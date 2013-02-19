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
package fr.mcc.ginco.extjs.view.factory;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.extjs.view.enums.ClassificationFolderType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory is used to produce specific view type (top) of node
 */
public class ThesaurusTopNodeFactory implements ThesaurusNodeFactory {

    /**
     * Creates {@link fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode}
     * based on real object.
     * @param source should be {@link Thesaurus} object, is used to
     *               get properties from it
     *               ({@link fr.mcc.ginco.beans.Thesaurus#getIdentifier()}
     *               and {@link fr.mcc.ginco.beans.Thesaurus#getTitle()})
     * @param generateFolders flag marking necessity of generating
     *                        categorizing folders under this node.
     * @return {@code if(source instanceof Thesaurus)} created node
     *                  or {@code null} otherwise
     */
    @Override
    public IThesaurusListNode createNode(Object source, boolean generateFolders) {
        IThesaurusListNode node;

        if(source instanceof Thesaurus) {
            node = new ThesaurusListBasicNode((Thesaurus) source);
            return node;
        } else {
            node = null;
        }

        if(node != null && generateFolders) {
            node.setChildren(getFolders(node.getId()));
        }

        return node;
    }

    @Override
    public IThesaurusListNode createNode(Object source) {
        return createNode(source, false);
    }

    /**
     * Creates categorization folders.
     * @param parentId id of top node.
     * @return created list of folders.
     */
    private List<IThesaurusListNode> getFolders(String parentId) {
        List<IThesaurusListNode> list = new ArrayList<IThesaurusListNode>();

        ThesaurusListBasicNode concepts =
                new ThesaurusListBasicNode("Arborescence des concepts",
                        parentId, ClassificationFolderType.CONCEPTS);
        list.add(concepts);
        ThesaurusListBasicNode sandbox =
                new ThesaurusListBasicNode("Bac à sable",
                        parentId, ClassificationFolderType.SANDBOX);
        list.add(sandbox);
        ThesaurusListBasicNode orphans =
                new ThesaurusListBasicNode("Concepts orphelins",
                        parentId, ClassificationFolderType.ORPHANS);
        list.add(orphans);
        ThesaurusListBasicNode groups =
                new ThesaurusListBasicNode("Groupes",
                        parentId, ClassificationFolderType.GROUPS);
        list.add(groups);

        return list;
    }
}
