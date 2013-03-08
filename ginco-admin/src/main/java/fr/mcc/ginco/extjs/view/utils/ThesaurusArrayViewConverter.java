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

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptReducedView;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Component("thesaurusArrayViewConverter")
public class ThesaurusArrayViewConverter {

    @Value("${ginco.default.language}")
    private String language;

    @Inject
    @Named("thesaurusArrayService")
    private IThesaurusArrayService thesaurusArrayService;

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;

    @Inject
    @Named("thesaurusConceptViewConverter")
    private ThesaurusConceptViewConverter thesaurusConceptViewConverter;

    @Inject
    @Named("nodeLabelService")
    private INodeLabelService nodeLabelService;

    @Inject
    @Named("nodeLabelViewConverter")
    private NodeLabelViewConverter nodeLabelViewConverter;


    public ThesaurusArray convert(ThesaurusArrayView source) throws BusinessException {
        ThesaurusArray hibernateRes;
        if(StringUtils.isEmpty(source.getIdentifier())) {
            hibernateRes = new ThesaurusArray();
        } else {
            hibernateRes = thesaurusArrayService.getThesaurusArrayById(source.getIdentifier());
        }

        hibernateRes.setNotation(source.getNotation());
        hibernateRes.setOrdered(source.getOrdered());

        if(source.getSuperOrdinateConcept() != null) {
            hibernateRes.setSuperOrdinateConcept(
                    thesaurusConceptService.getThesaurusConceptById(
                            source.getSuperOrdinateConcept().getIdentifier()));
        }

        hibernateRes.getConcepts().clear();

        for(ThesaurusConceptReducedView conceptReducedView : source.getConcepts()) {
            ThesaurusConcept concept =
                    thesaurusConceptService.getThesaurusConceptById(conceptReducedView.getIdentifier());
            if(concept == null) {
                throw new BusinessException("Concept doest not exist","concept-does-not-exist");
            }
            hibernateRes.getConcepts().add(concept);
        }


        return hibernateRes;
    }

    public ThesaurusArrayView convert(ThesaurusArray source) throws BusinessException {
        ThesaurusArrayView thesaurusArrayView = new ThesaurusArrayView();

        thesaurusArrayView.setIdentifier(source.getIdentifier());
        thesaurusArrayView.setNotation(source.getNotation());
        thesaurusArrayView.setOrdered(source.getOrdered());

        if(source.getSuperOrdinateConcept() != null) {
            thesaurusArrayView.setSuperOrdinateConcept(
                    thesaurusConceptViewConverter.convert(source.getSuperOrdinateConcept()));
        }

        thesaurusArrayView.setConcepts(
                    thesaurusConceptViewConverter.convert(new ArrayList<ThesaurusConcept>(source.getConcepts())));

        NodeLabel label = nodeLabelService.getByThesaurusArrayAndLanguage(source.getIdentifier(), language);

        thesaurusArrayView.setLabel(label.getLexicalValue());
        thesaurusArrayView.setLanguage(label.getLanguage().getId());
        thesaurusArrayView.setNodeLabelId(label.getIdentifier());

        thesaurusArrayView.setThesaurusId(source.getThesaurus().getThesaurusId());

        return thesaurusArrayView;
    }
}
