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
package fr.mcc.ginco.exports;

import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Service("exportService")
public class ExportServiceImpl implements IExportService {
    @Inject
    @Named("thesaurusArrayService")
    private IThesaurusArrayService thesaurusArrayService;

    @Inject
    @Named("thesaurusTermService")
    private IThesaurusTermService thesaurusTermService;

    @Inject
    @Named("nodeLabelService")
    private INodeLabelService nodeLabelService;

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;

    @Override
    public List<FormattedLine> getHierarchicalText(Integer base, ThesaurusConcept concept) throws BusinessException {
        List<FormattedLine> result = new ArrayList<FormattedLine>();

        Set<String> thesaurusArrayConcepts = new HashSet<String>();

        result.add(new FormattedLine(base, thesaurusTermService.getPreferedTerms(
                thesaurusTermService.getTermsByConceptId(concept.getIdentifier()))
                .get(0).getLexicalValue()));

        List<ThesaurusArray> subOrdArrays = thesaurusArrayService.getSubOrdinatedArrays(concept.getIdentifier());

        for(ThesaurusArray subOrdArray : subOrdArrays) {
            for(ThesaurusConcept conceptInArray : subOrdArray.getConcepts()) {
                thesaurusArrayConcepts.add(conceptInArray.getIdentifier());
            }
        }

        List<ThesaurusConcept> children = new ArrayList<ThesaurusConcept>(thesaurusConceptService.getChildrenByConceptId(concept.getIdentifier()));
        Collections.sort(children, new ThesaurusConceptComparator());

        for(ThesaurusConcept child : children) {
            if (!thesaurusArrayConcepts.contains(child.getIdentifier())) {
                result.addAll(getHierarchicalText(base + 1, child));
            }
        }

        for(ThesaurusArray subOrdArray : subOrdArrays) {
            result.add(new FormattedLine(base + 1,
                    "<" + nodeLabelService.getByThesaurusArray(subOrdArray.getIdentifier()).getLexicalValue() + ">"));
            List<ThesaurusConcept> conceptsInArray =
                    new ArrayList<ThesaurusConcept>(subOrdArray.getConcepts());
            Collections.sort(conceptsInArray, new ThesaurusConceptComparator());

            for(ThesaurusConcept conceptInArray : conceptsInArray) {
                result.addAll(getHierarchicalText(base + 1, conceptInArray));
            }
        }

        return result;
    }

    /**
     * Comparator to use with two concepts - compares based on its lexicalValue.
     */
    class ThesaurusConceptComparator implements Comparator<ThesaurusConcept> {
        @Override
        public int compare(ThesaurusConcept o1, ThesaurusConcept o2) {
            try {
                String l1 = thesaurusTermService.getPreferedTerms(
                        thesaurusTermService.getTermsByConceptId(o1.getIdentifier()))
                        .get(0).getLexicalValue();
                String l2 = thesaurusTermService.getPreferedTerms(
                        thesaurusTermService.getTermsByConceptId(o2.getIdentifier()))
                        .get(0).getLexicalValue();
                return l1.compareToIgnoreCase(l2);
            } catch (BusinessException e) {
                return 0;
            }
        }
    }
}
