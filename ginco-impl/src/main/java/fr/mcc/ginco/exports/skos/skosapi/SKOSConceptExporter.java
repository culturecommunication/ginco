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

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.utils.DateUtil;

import org.semanticweb.skos.*;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

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

	@Inject
	@Named("skosHierarchicalRelationshipExporter")
	private SKOSHierarchicalRelationshipExporter skosHierarchicalRelationshipExporter;

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
	public MixedSKOSModel exportConceptSKOS(ThesaurusConcept concept,
			SKOSConcept parent, SKOSConceptScheme scheme,
			SKOSDataFactory factory, SKOSDataset vocab) {

		MixedSKOSModel result  = new MixedSKOSModel();
		List<SKOSChange> addList = new ArrayList<SKOSChange>();
		List<Model> models = new ArrayList<Model>();

		SKOSConcept conceptSKOS = factory.getSKOSConcept(URI.create(concept
				.getIdentifier()));

		addList.addAll(exportConceptInformation(concept,
				conceptSKOS, scheme, factory, vocab));

		List<ThesaurusTerm> prefTerms = thesaurusConceptService
				.getConceptPreferredTerms(concept.getIdentifier());

		MixedSKOSModel prefRes = skosTermsExporter.exportConceptPreferredTerms(prefTerms,
				conceptSKOS, factory, vocab);
		addList.addAll(prefRes.getSkosChanges());
		models.addAll(prefRes.getModels());

		MixedSKOSModel simpleNonPrefRes = skosTermsExporter.exportConceptNotPreferredTerms(
				concept.getIdentifier(), conceptSKOS, factory, vocab);
		addList.addAll(simpleNonPrefRes.getSkosChanges());
		models.addAll(simpleNonPrefRes.getModels());

		addList.addAll(skosAssociativeRelationshipExporter
				.exportAssociativeRelationships(concept, factory, conceptSKOS,
						vocab));

		addList.addAll(skosHierarchicalRelationshipExporter
				.exportHierarchicalRelationships(parent, factory,
						conceptSKOS, scheme, vocab));

		addList.addAll(skosAlignmentExporter.exportAlignments(
				concept.getIdentifier(), factory, conceptSKOS, vocab));

		addList.addAll(skosCustomConceptAttributeExporter.exportCustomConceptAttributes(concept, conceptSKOS, factory, vocab));

		if (thesaurusConceptService.hasChildren(concept.getIdentifier())) {
			for (ThesaurusConcept child : thesaurusConceptService
					.getChildrenByConceptId(concept.getIdentifier())) {
				MixedSKOSModel childRes = exportConceptSKOS(child, conceptSKOS, scheme,
						factory, vocab);
				addList.addAll(childRes.getSkosChanges());
				models.addAll(childRes.getModels());
			}
		}

		result.setSkosChanges(addList);
		result.setModels(models);
		return result;
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
