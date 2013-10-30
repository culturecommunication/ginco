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

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.utils.DateUtil;

import org.semanticweb.skos.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * This component is in charge of exporting concept to SKOS
 *
 */
@Component("skosConceptExporter")
public class SKOSConceptExporter {

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("skosTermsExporter")
	private SKOSTermsExporter skosTermsExporter;

	@Inject
	@Named("skosNotesExporter")
	private SKOSNotesExporter skosNotesExporter;

	@Inject
	@Named("skosAssociativeRelationshipExporter")
	private SKOSAssociativeRelationshipExporter skosAssociativeRelationshipExporter;

	@Inject
	@Named("skosAlignmentExporter")
	private SKOSAlignmentExporter skosAlignmentExporter;

	@Inject
	@Named("skosCustomConceptAttributeExporter")
	private SKOSCustomConceptAttributeExporter skosCustomConceptAttributeExporter;

	private static final String dct_uri = "http://purl.org/dct#";
	private static final String isothes_uri = "http://www.niso.org/schemas/iso25964/iso-thes#";

	/**
	 * Export a concept to SKOS using the skos API
	 *
	 * @param concept
	 * @param parent
	 * @param scheme
	 * @param factory
	 * @param vocab
	 * @return
	 */
	public List<SKOSChange> exportConceptSKOS(ThesaurusConcept concept,
			SKOSConcept parent, SKOSConceptScheme scheme,
			SKOSDataFactory factory, SKOSDataset vocab) {

		List<SKOSChange> addList = new ArrayList<SKOSChange>();

		SKOSConcept conceptSKOS = factory.getSKOSConcept(URI.create(concept
				.getIdentifier()));

		addList.addAll(exportConceptInformation(concept,
				conceptSKOS, scheme, factory, vocab));

		List<ThesaurusTerm> prefTerms = thesaurusConceptService
				.getConceptPreferredTerms(concept.getIdentifier());

		addList.addAll(skosTermsExporter.exportConceptPreferredTerms(prefTerms,
				conceptSKOS, factory, vocab));
		addList.addAll(skosTermsExporter.exportConceptNotPreferredTerms(
				concept.getIdentifier(), conceptSKOS, factory, vocab));

		addList.addAll(skosAssociativeRelationshipExporter
				.exportAssociativeRelationships(concept, factory, conceptSKOS,
						vocab));

		if (parent != null) {
			SKOSObjectRelationAssertion childConnection = factory
					.getSKOSObjectRelationAssertion(conceptSKOS,
							factory.getSKOSBroaderProperty(), parent);
			SKOSObjectRelationAssertion parentConnection = factory
					.getSKOSObjectRelationAssertion(parent,
							factory.getSKOSNarrowerProperty(), conceptSKOS);
			addList.add(new AddAssertion(vocab, childConnection));
			addList.add(new AddAssertion(vocab, parentConnection));
		} else {
			SKOSObjectRelationAssertion topConcept = factory
					.getSKOSObjectRelationAssertion(scheme,
							factory.getSKOSHasTopConceptProperty(), conceptSKOS);
			addList.add(new AddAssertion(vocab, topConcept));
		}

		if (thesaurusConceptService.hasChildren(concept.getIdentifier())) {
			for (ThesaurusConcept child : thesaurusConceptService
					.getChildrenByConceptId(concept.getIdentifier())) {
				addList.addAll(exportConceptSKOS(child, conceptSKOS, scheme,
						factory, vocab));
			}
		}

		addList.addAll(skosAlignmentExporter.exportAlignments(
				concept.getIdentifier(), factory, conceptSKOS, vocab));
		addList.addAll(skosCustomConceptAttributeExporter.exportCustomConceptAttributes(concept, conceptSKOS, factory, vocab));

		return addList;
	}

	/**
	 * Export minimal concept information
	 *
	 * @param concept
	 * @param parent
	 * @param scheme
	 * @param factory
	 * @param vocab
	 * @return
	 */
	public List<SKOSChange> exportConceptInformation(ThesaurusConcept concept,
			SKOSConcept conceptSKOS, SKOSConceptScheme scheme,
			SKOSDataFactory factory, SKOSDataset vocab) {

		List<SKOSChange> addList = new ArrayList<SKOSChange>();

		SKOSEntityAssertion conceptAssertion = factory
				.getSKOSEntityAssertion(conceptSKOS);
		addList.add(new AddAssertion(vocab, conceptAssertion));

		SKOSObjectRelationAssertion inScheme = factory
				.getSKOSObjectRelationAssertion(conceptSKOS,
						factory.getSKOSInSchemeProperty(), scheme);
		addList.add(new AddAssertion(vocab, inScheme));

		SKOSDataRelationAssertion createdAssertion = factory
				.getSKOSDataRelationAssertion(conceptSKOS, factory
						.getSKOSDataProperty(URI
								.create(dct_uri + "created")),
						DateUtil.toString(concept.getCreated()));
		addList.add(new AddAssertion(vocab, createdAssertion));

		SKOSDataRelationAssertion modifiedAssertion = factory
				.getSKOSDataRelationAssertion(conceptSKOS, factory
						.getSKOSDataProperty(URI
								.create(dct_uri + "modified")),
						DateUtil.toString(concept.getModified()));
		addList.add(new AddAssertion(vocab, modifiedAssertion));

		if (concept.getNotation() != null && !concept.getNotation().isEmpty()) {
			SKOSDataRelationAssertion notationAssertion = factory
					.getSKOSDataRelationAssertion(conceptSKOS, factory
							.getSKOSDataProperty(factory
									.getSKOSNotationProperty().getURI()),
							concept.getNotation());
			addList.add(new AddAssertion(vocab, notationAssertion));
		}

		SKOSDataRelationAssertion statusAssertion = factory
				.getSKOSDataRelationAssertion(conceptSKOS, factory
						.getSKOSDataProperty(URI
								.create(isothes_uri + "status")),
						concept.getStatus().toString());
		addList.add(new AddAssertion(vocab, statusAssertion));

		return addList;
	}
}
