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

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.skos.namespaces.SKOSXL;

/**
 * This component is in charge of exporting concept terms to SKOS
 * 
 */

@Component("skosTermsExporter")
public class SKOSTermsExporter {

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Inject
	@Named("skosModelTermsExporter")
	private SKOSModelTermsExporter skosModelTermsExporter;

	@Log
	private Logger logger;

	/**
	 * Export concept preferred terms to SKOS using the skos API
	 * 
	 * @param prefTerms
	 * @param conceptSKOS
	 * @param factory
	 * @param vocab
	 * 
	 * @return list of concept preferred terms for skos
	 * @throws URISyntaxException
	 */

	public Model exportConceptPreferredTerms(List<ThesaurusTerm> prefTerms,
			Model model, Resource conceptResource) {

		for (ThesaurusTerm prefTerm : prefTerms) {
			model.add(conceptResource, SKOS.PREF_LABEL,
					StringEscapeUtils.unescapeXml(prefTerm.getLexicalValue()),
					prefTerm.getLanguage().getPart1());			

			model.add(conceptResource, SKOSXL.PREF_LABEL,
					model.createResource(prefTerm.getIdentifier()));		
			skosModelTermsExporter.exportConceptPreferredTerm(prefTerm, model);

		}

		return model;
	}

	/**
	 * Export concept not preferred terms to SKOS using the skos API
	 * 
	 * @param conceptId
	 * @param conceptSKOS
	 * @param factory
	 * @param vocab
	 * 
	 * @return list of concept not preferred terms for skos
	 */

	public Model exportConceptNotPreferredTerms(String conceptId, Model model,
			Resource conceptResource) {

		for (ThesaurusTerm altLabel : thesaurusTermService
				.getTermsByConceptId(conceptId)) {
			if (altLabel.getPrefered()) {
				continue;
			}

			if (altLabel.getHidden()) {
				model.add(conceptResource, SKOS.HIDDEN_LABEL, StringEscapeUtils
						.unescapeXml(altLabel.getLexicalValue()), altLabel
						.getLanguage().getPart1());

			} else {
				model.add(conceptResource, SKOS.ALT_LABEL, StringEscapeUtils
						.unescapeXml(altLabel.getLexicalValue()), altLabel
						.getLanguage().getPart1());

			}
			skosModelTermsExporter.exportConceptNotPreferredTerm(altLabel, model,
					conceptResource, altLabel.getHidden());
		}
		
		return model;
	}
}
