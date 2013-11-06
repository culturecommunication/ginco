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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.semanticweb.skos.AddAssertion;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skos.SKOSObjectProperty;
import org.semanticweb.skos.SKOSObjectRelationAssertion;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.enums.ConceptHierarchicalRelationshipRoleEnum;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipService;
import fr.mcc.ginco.services.IThesaurusConceptService;

/**
 * This component is in charge of exporting concept hierarchical relationships
 * to SKOS
 *
 */
@Component("skosHierarchicalRelationshipExporter")
public class SKOSHierarchicalRelationshipExporter {

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("conceptHierarchicalRelationshipService")
	private IConceptHierarchicalRelationshipService conceptHierarchicalRelationshipService;

	private static final String ginco_uri = "http://data.culture.fr/thesaurus/ginco/";

	public List<SKOSChange> exportHierarchicalRelationships(SKOSConcept parent, SKOSDataFactory factory,
			SKOSConcept conceptSKOS, SKOSConceptScheme scheme, SKOSDataset vocab) {

		List<SKOSChange> addList = new ArrayList<SKOSChange>();
		if (parent != null) {
			ConceptHierarchicalRelationship relationship = conceptHierarchicalRelationshipService
					.getByChildAndParentIds(conceptSKOS.getURI().toString(),
							parent.getURI().toString());

			String parentGincoLabel = ConceptHierarchicalRelationshipRoleEnum
					.getStatusByCode(relationship.getRole())
					.getParentSkosLabel();
			addList.addAll(buildHierarchicalRelationship (factory, vocab, conceptSKOS, parent, factory.getSKOSBroaderProperty(), parentGincoLabel));

			String childGincoLabel = ConceptHierarchicalRelationshipRoleEnum
					.getStatusByCode(relationship.getRole())
					.getChildSkosLabel();
			addList.addAll(buildHierarchicalRelationship (factory, vocab, parent, conceptSKOS, factory.getSKOSNarrowerProperty(), childGincoLabel));

		} else {
			SKOSObjectRelationAssertion topConcept = factory
					.getSKOSObjectRelationAssertion(scheme,
							factory.getSKOSHasTopConceptProperty(), conceptSKOS);
			addList.add(new AddAssertion(vocab, topConcept));
		}

		return addList;
	}

	private List<SKOSChange> buildHierarchicalRelationship(
			SKOSDataFactory factory, SKOSDataset vocab,
			SKOSConcept conceptSKOS, SKOSConcept relatedConcept,
			SKOSObjectProperty skosRelation, String skosLabel) {

		List<SKOSChange> addList = new ArrayList<SKOSChange>();

		SKOSObjectRelationAssertion skosConnection = factory
				.getSKOSObjectRelationAssertion(conceptSKOS, skosRelation,
						relatedConcept);
		addList.add(new AddAssertion(vocab, skosConnection));

		if (!skosLabel.isEmpty()){
			SKOSObjectRelationAssertion gincoConnection = factory
					.getSKOSObjectRelationAssertion(
							conceptSKOS,
							factory.getSKOSObjectProperty(URI.create(ginco_uri
									+ skosLabel)), relatedConcept);
			addList.add(new AddAssertion(vocab, gincoConnection));
		}
		return addList;
	}
}