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
package fr.mcc.ginco.exports.skos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.skos.namespaces.SKOSXL;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting complex concept to SKOS
 *
 */
@Component("skosComplexConceptExporter")
public class SKOSComplexConceptExporter {

	@Inject
	@Named("splitNonPreferredTermService")
	private ISplitNonPreferredTermService splitNonPreferredTermService;

	public Model exportComplexConcept(Thesaurus thesaurus, Model model) {
		List<SplitNonPreferredTerm> complexConcepts = splitNonPreferredTermService
				.getSplitNonPreferredTermList(0, -1,
						thesaurus.getThesaurusId());
		if (!complexConcepts.isEmpty()) {

			for (SplitNonPreferredTerm complexConcept : complexConcepts) {

				Resource inScheme = model.createResource(complexConcept
						.getThesaurus().getIdentifier());

				// Add splitNonPreferredTerm resource
				Resource complexConceptRes = model.createResource(
						complexConcept.getIdentifier(),
						ISOTHES.SPLIT_NON_PREFERRED_TERM);

				model.add(complexConceptRes, SKOS.IN_SCHEME, inScheme);

				model.add(complexConceptRes, SKOSXL.LITERAL_FORM,
						complexConcept.getLexicalValue(), complexConcept
								.getLanguage().getId());
				model.add(complexConceptRes, DCTerms.created,
						DateUtil.toISO8601String(complexConcept.getCreated()));
				model.add(complexConceptRes, DCTerms.modified,
						DateUtil.toISO8601String(complexConcept.getModified()));
				model.add(complexConceptRes, ISOTHES.STATUS, complexConcept
						.getStatus().toString());

				if (StringUtils.isNotEmpty(complexConcept.getSource())) {
					model.add(complexConceptRes, DC.source,
							complexConcept.getSource());
				}

				// Add compoundEquivalence resource

				Resource compoundEquivalenceRes = model
						.createResource(ISOTHES.COMPOUND_EQUIVALENCE);
				model.add(compoundEquivalenceRes, SKOS.IN_SCHEME, inScheme);
				model.add(compoundEquivalenceRes, ISOTHES.PLUS_UF, complexConcept.getIdentifier());
				for (ThesaurusTerm term : complexConcept.getPreferredTerms()){
					model.add(compoundEquivalenceRes, ISOTHES.PLUS_USE, term.getIdentifier());
				}
			}
			
		}
		return model;
	}
}
