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
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.extjs.view.pojo.NodeLabelView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.utils.DateUtil;

/**
 * Utility ton convert {@link NodeLabel} to {@link NodeLabelView} and vice versa
 *
 */
@Component("nodeLabelViewConverter")
public class NodeLabelViewConverter {   

    @Inject
    @Named("nodeLabelService")
    private INodeLabelService nodeLabelService;

    @Inject
    @Named("languagesService")
    private ILanguagesService languagesService;    
    

    /**
     * Converts a {@link NodeLabel} into a {@link NodeLabelView}
     * @param source
     * @return
     */
    public NodeLabelView convert(NodeLabel source) {
        NodeLabelView nodeLabelView = new NodeLabelView();
        nodeLabelView.setIdentifier(source.getIdentifier());
        nodeLabelView.setCreated(DateUtil.toString(source.getCreated()));
        nodeLabelView.setModified(DateUtil.toString(source.getModified()));
        nodeLabelView.setLanguage(source.getLanguage().getId());
        nodeLabelView.setThesaurusArrayId(source.getThesaurusArray().getIdentifier());

        return nodeLabelView;
    }

    /**
     * Converts a set of {@link NodeLabel} to a list of {@link NodeLabelView}
     * @param source
     * @return
     */
    public List<NodeLabelView> convert(Set<NodeLabel> source) {
        List<NodeLabelView> result = new ArrayList<NodeLabelView>();
        for(NodeLabel nodeLabel : source) {
            result.add(convert(nodeLabel));
        }

        return result;
    }

    /**
     * Gets the NodeLabel of a {@link ThesaurusArrayView}
     * @param thesaurusConceptViewJAXBElement
     * @return
     */
    public NodeLabel convert(ThesaurusArrayView thesaurusConceptViewJAXBElement) {
        NodeLabel label;
        if(thesaurusConceptViewJAXBElement.getNodeLabelId() == null || thesaurusConceptViewJAXBElement.getNodeLabelId() ==0) {
            label = new NodeLabel();
            label.setCreated(DateUtil.nowDate());
        } else {
            label = nodeLabelService.getById(thesaurusConceptViewJAXBElement.getNodeLabelId());
        }
        label.setLexicalValue(thesaurusConceptViewJAXBElement.getLabel());
        label.setLanguage(languagesService.getLanguageById(thesaurusConceptViewJAXBElement.getLanguage()));
        label.setModified(DateUtil.nowDate());

        return label;
    }
}
