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

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptHierarchicalRelationshipRoleEnum;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipService;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * This component is in charge of exporting concept hierarchical relationships
 * to SKOS
 * 
 */
@Component("skosHierarchicalRelationshipExporter")
public class SKOSHierarchicalRelationshipExporter {	
	
	private Logger logger = LoggerFactory.getLogger(SKOSHierarchicalRelationshipExporter.class);
	
	@Inject
	@Named("conceptHierarchicalRelationshipService")
	private IConceptHierarchicalRelationshipService conceptHierarchicalRelationshipService;

	public Model exportHierarchicalRelationships(Model model,
			ThesaurusConcept parentConcept, ThesaurusConcept childConcept) {
		Resource childRes = model.createResource(childConcept.getIdentifier());
		if (parentConcept != null) {
			Resource parentRes = model.createResource(parentConcept
					.getIdentifier());
			ConceptHierarchicalRelationship relationship = conceptHierarchicalRelationshipService
					.getByChildAndParentIds(childConcept.getIdentifier(),
							parentConcept.getIdentifier());
			String parentGincoLabel = ConceptHierarchicalRelationshipRoleEnum
					.getStatusByCode(relationship.getRole())
					.getParentSkosLabel();
			buildHierarchicalRelationship(model, childRes, parentRes,
					SKOS.BROADER, parentGincoLabel);

			String childGincoLabel = ConceptHierarchicalRelationshipRoleEnum
					.getStatusByCode(relationship.getRole())
					.getChildSkosLabel();
			buildHierarchicalRelationship(model, parentRes, childRes,
					SKOS.NARROWER, childGincoLabel);

		} else {
			Resource scheme = model.createResource(childConcept
					.getThesaurusId());
			model.add(scheme, SKOS.HAS_TOP_CONCEPT, childRes);
		}

		return model;
	}

	private void buildHierarchicalRelationship(Model defaultModel,
			Resource sourceConcept, Resource relatedConcept,
			Property skosRelation, String skosLabel) {
		logger.info("Relation "+skosRelation.toString()+" "+relatedConcept.getURI()+" "+sourceConcept.getURI());
		defaultModel.add(sourceConcept, skosRelation, relatedConcept);
		if (!skosLabel.isEmpty()) {
			Property gincoRelProperty = defaultModel.createProperty(GINCO
					.getURI() + skosLabel);

			defaultModel.add(sourceConcept, gincoRelProperty, relatedConcept);

		}
	}
}