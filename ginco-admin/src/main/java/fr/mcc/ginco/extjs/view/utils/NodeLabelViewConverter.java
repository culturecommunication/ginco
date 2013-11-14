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

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.NodeLabelView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.utils.DateUtil;

@Component("nodeLabelViewConverter")
public class NodeLabelViewConverter {

    @Value("${ginco.default.language}")
    private String language;

	private Logger logger  = LoggerFactory.getLogger(NodeLabelViewConverter.class);


    @Inject
    @Named("nodeLabelService")
    private INodeLabelService nodeLabelService;

    @Inject
    @Named("languagesService")
    private ILanguagesService languagesService;

    @Inject
    @Named("thesaurusArrayService")
    private IThesaurusArrayService thesaurusArrayService;

    private NodeLabel getNewNodeLabel() {
        NodeLabel hibernateRes = new NodeLabel();
        hibernateRes.setCreated(DateUtil.nowDate());
        logger.info("Creating a new term");

        return hibernateRes;
    }

    private NodeLabel getExistingNodeLabel(Integer id)
            throws BusinessException {

        NodeLabel hibernateRes = nodeLabelService
                .getById(id);
        logger.info("Getting an existing term with id " + id);
        return hibernateRes;
    }

    /**
     * Main method used to do conversion.
     * @param source source to work with
     * @return converted item.
     * @throws BusinessException
     */
    public NodeLabel convert(NodeLabelView source, boolean fromArray) throws BusinessException{
        NodeLabel hibernateRes;

        if ("".equals(source.getIdentifier())) {
            hibernateRes = getNewNodeLabel();
        } else {
            hibernateRes = getExistingNodeLabel(source.getIdentifier());
        }

        hibernateRes.setModified(DateUtil.nowDate());
        if (StringUtils.isEmpty(source.getLanguage())) {
            // If not filled in, the language for the term is
            // "ginco.default.language" property in application.properties
            hibernateRes
                    .setLanguage(languagesService.getLanguageById(language));
        } else {
            hibernateRes.setLanguage(languagesService.getLanguageById(source
                    .getLanguage()));
        }

        hibernateRes.setLexicalValue(source.getLexicalValue());
        if (fromArray) {
            if (StringUtils.isNotEmpty(source.getThesaurusArrayId())) {
                ThesaurusArray array = thesaurusArrayService
                        .getThesaurusArrayById(source.getThesaurusArrayId());
                if (array != null) {
                    hibernateRes.setThesaurusArray(array);
                }
            }
        }

        return hibernateRes;
    }

    public NodeLabelView convert(NodeLabel source) {
        NodeLabelView nodeLabelView = new NodeLabelView();
        nodeLabelView.setIdentifier(source.getIdentifier());
        nodeLabelView.setCreated(DateUtil.toString(source.getCreated()));
        nodeLabelView.setModified(DateUtil.toString(source.getModified()));
        nodeLabelView.setLanguage(source.getLanguage().getId());
        nodeLabelView.setThesaurusArrayId(source.getThesaurusArray().getIdentifier());

        return nodeLabelView;
    }

    public List<NodeLabelView> convert(Set<NodeLabel> source) {
        List<NodeLabelView> result = new ArrayList<NodeLabelView>();
        for(NodeLabel nodeLabel : source) {
            result.add(convert(nodeLabel));
        }

        return result;
    }

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
