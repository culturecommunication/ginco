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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringEscapeUtils;
import org.semanticweb.skos.AddAssertion;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataProperty;
import org.semanticweb.skos.SKOSDataRelationAssertion;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skos.SKOSObjectProperty;
import org.semanticweb.skos.SKOSObjectRelationAssertion;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusTermService;
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

	public MixedSKOSModel exportConceptPreferredTerms(
			List<ThesaurusTerm> prefTerms, SKOSConcept conceptSKOS,
			SKOSDataFactory factory, SKOSDataset vocab) {
		MixedSKOSModel result = new MixedSKOSModel();

		List<SKOSChange> addList = new ArrayList<SKOSChange>();
		List<Model> models = new ArrayList<Model>();

		for (ThesaurusTerm prefTerm : prefTerms) {
			SKOSDataRelationAssertion prefLabelInsertion = factory
					.getSKOSDataRelationAssertion(conceptSKOS, factory
							.getSKOSDataProperty(factory
									.getSKOSPrefLabelProperty().getURI()),
							StringEscapeUtils.unescapeXml(prefTerm
									.getLexicalValue()), prefTerm.getLanguage()
									.getPart1());
			addList.add(new AddAssertion(vocab, prefLabelInsertion));


			SKOSConcept fakeConcept = factory.getSKOSConcept(URI.create(prefTerm
					.getIdentifier()));
			SKOSObjectProperty dataProperty = factory.getSKOSObjectProperty(URI.create(SKOSXL
					.getURI() + "prefLabel"));
			SKOSObjectRelationAssertion prefLabelXLInsertion = factory
						.getSKOSObjectRelationAssertion(
								conceptSKOS,
								dataProperty,
								fakeConcept);

			addList.add(new AddAssertion(vocab, prefLabelXLInsertion));

			Model prefTermModel = skosModelTermsExporter.exportConceptPreferredTerm(prefTerm);
			logger.debug("Preferred term Model is =  " + prefTermModel);
			models.add(prefTermModel);
		}

		result.setSkosChanges(addList);
		result.setModels(models);
		return result;
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

	public MixedSKOSModel exportConceptNotPreferredTerms(String conceptId,
			SKOSConcept conceptSKOS, SKOSDataFactory factory, SKOSDataset vocab) {
		MixedSKOSModel result = new MixedSKOSModel();
		List<SKOSChange> addList = new ArrayList<SKOSChange>();
		List<Model> models = new ArrayList<Model>();
		for (ThesaurusTerm altLabel : thesaurusTermService
				.getTermsByConceptId(conceptId)) {
			if (altLabel.getPrefered()) {
				continue;
			}

			if (altLabel.getHidden()) {
				SKOSDataRelationAssertion hiddenLabelInsertion = factory
						.getSKOSDataRelationAssertion(
								conceptSKOS,
								factory.getSKOSDataProperty(factory
										.getSKOSHiddenLabelProperty().getURI()),
								StringEscapeUtils.unescapeXml(altLabel
										.getLexicalValue()), altLabel
										.getLanguage().getPart1());

				addList.add(new AddAssertion(vocab, hiddenLabelInsertion));


				SKOSDataProperty dataProperty = factory.getSKOSDataProperty(URI.create(SKOSXL
						.getURI() + "hiddenLabel"));
				SKOSDataRelationAssertion hiddenLabelXLInsertion = factory
							.getSKOSDataRelationAssertion(
									conceptSKOS,
									dataProperty,
									altLabel.getLexicalValue());
				addList.add(new AddAssertion(vocab, hiddenLabelXLInsertion));

			} else {
				SKOSDataRelationAssertion altLabelInsertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSAltLabelProperty().getURI()),
								StringEscapeUtils.unescapeXml(altLabel
										.getLexicalValue()), altLabel
										.getLanguage().getPart1());

				addList.add(new AddAssertion(vocab, altLabelInsertion));

				SKOSDataProperty dataProperty = factory.getSKOSDataProperty(URI.create(SKOSXL
						.getURI() + "altLabel"));
				SKOSDataRelationAssertion altLabelXLInsertion = factory
							.getSKOSDataRelationAssertion(
									conceptSKOS,
									dataProperty,
									altLabel.getLexicalValue());
				addList.add(new AddAssertion(vocab, altLabelXLInsertion));
			}
			Model simpleNonPrefTermModel = skosModelTermsExporter.exportConceptSimpleNonPreferredTerm(altLabel);
			logger.debug("Simple non preferred term Model is =  " + simpleNonPrefTermModel);
			models.add(simpleNonPrefTermModel);
		}
		result.setSkosChanges(addList);
		result.setModels(models);
		return result;
	}
}