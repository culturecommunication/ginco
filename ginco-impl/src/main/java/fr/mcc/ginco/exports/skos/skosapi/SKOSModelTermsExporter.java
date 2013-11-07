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

package fr.mcc.ginco.exports.skos.skosapi;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.skos.namespaces.SKOSXL;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting concept terms to SKOS jena model
 *
 */

@Component("skosModelTermsExporter")
public class SKOSModelTermsExporter {

	@Log
	private Logger logger;

	public Model exportConceptPreferredTerm(
			ThesaurusTerm term) {
		logger.debug("Start generating model for preferred term "
				+ term.getLexicalValue());
		Model model = ModelFactory.createDefaultModel();
		Resource prefTermRes = model.createResource(term.getIdentifier(),
				ISOTHES.PREFERRED_TERM);
		exportTermInformation(term, model, prefTermRes);
		return model;
	}

	public Model exportConceptSimpleNonPreferredTerm(ThesaurusTerm term) {
		logger.debug("Start generating model for simple non preferred term "
				+ term.getLexicalValue());
		Model model = ModelFactory.createDefaultModel();
		Resource simpleNonPrefTermRes = model.createResource(term.getIdentifier(),
				ISOTHES.SIMPLE_NON_PREFERRED_TERM);
		exportTermInformation(term, model, simpleNonPrefTermRes);
		return model;
	}

	public void exportTermInformation(ThesaurusTerm term, Model model, Resource termResource){
		Resource inScheme = model.createResource(term.getThesaurus()
				.getIdentifier());

		model.add(termResource, SKOS.IN_SCHEME, inScheme);

		model.add(termResource, SKOSXL.LITERAL_FORM, term.getLexicalValue(),
				term.getLanguage().getPart1());
		model.add(termResource, DCTerms.created,
				DateUtil.toString(term.getCreated()));
		model.add(termResource, DCTerms.modified,
				DateUtil.toString(term.getModified()));
		model.add(termResource, ISOTHES.STATUS, term.getStatus().toString());

		if (StringUtils.isNotEmpty(term.getSource())) {
			model.add(termResource, DC.source, term.getSource());
		}

		model.setNsPrefix("skos", SKOS.getURI());
		model.setNsPrefix("dct", DCTerms.getURI());
		model.setNsPrefix("dc", DC.getURI());
		model.setNsPrefix("skos-xl", SKOSXL.getURI());
		model.setNsPrefix("iso-thes", ISOTHES.getURI());
	}
}