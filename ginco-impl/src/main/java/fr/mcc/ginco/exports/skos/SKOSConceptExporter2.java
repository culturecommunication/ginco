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

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting concept to SKOS
 * 
 */
@Component("skosConceptExporter2")
public class SKOSConceptExporter2 {

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("skosTermsExporter2")
	private SKOSTermsExporter2 skosTermsExporter;

	@Inject
	@Named("skosNotesExporter2")
	private SKOSNotesExporter2 skosNotesExporter;

	@Inject
	@Named("skosAssociativeRelationshipExporter2")
	private SKOSAssociativeRelationshipExporter2 skosAssociativeRelationshipExporter;

	@Inject
	@Named("skosAlignmentExporter2")
	private SKOSAlignmentExporter2 skosAlignmentExporter;

	@Inject
	@Named("skosCustomConceptAttributeExporter2")
	private SKOSCustomConceptAttributeExporter2 skosCustomConceptAttributeExporter;
	
	
	@Inject
	@Named("skosHierarchicalRelationshipExporter2")
	private SKOSHierarchicalRelationshipExporter2 skosHierarchicalRelationshipExporter;


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
	public Model exportConceptSKOS(ThesaurusConcept concept,
			ThesaurusConcept parent, Model model, OntModel ontModel) {		

		Resource conceptResource = model.createResource(
				concept.getIdentifier(), SKOS.CONCEPT);		

		exportConceptInformation(concept, conceptResource, model, ontModel);

		List<ThesaurusTerm> prefTerms = thesaurusConceptService
				.getConceptPreferredTerms(concept.getIdentifier());

		skosTermsExporter.exportConceptPreferredTerms(prefTerms, model,
				conceptResource);

		skosTermsExporter.exportConceptNotPreferredTerms(
				concept.getIdentifier(), model, conceptResource);
		
		skosNotesExporter.exportNotes(model, prefTerms, concept);

		skosAssociativeRelationshipExporter.exportAssociativeRelationships(
				concept, model, conceptResource);

		if (thesaurusConceptService.hasChildren(concept.getIdentifier())) {
			for (ThesaurusConcept child : thesaurusConceptService
					.getChildrenByConceptId(concept.getIdentifier())) {			

				exportConceptSKOS(child, concept, model, ontModel);

			}
		}	
		
		skosHierarchicalRelationshipExporter
				.exportHierarchicalRelationships(model, parent, concept);

		skosAlignmentExporter.exportAlignments(concept.getIdentifier(),
				conceptResource, model);

		skosCustomConceptAttributeExporter.exportCustomConceptAttributes(
				concept, model, conceptResource, ontModel);

		return model;
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
	public Model exportConceptInformation(ThesaurusConcept concept,
			Resource conceptResource, Model model, OntModel ontModel) {

		Resource inScheme = model.createResource(concept.getThesaurus()
				.getIdentifier());
		model.add(conceptResource, SKOS.IN_SCHEME, inScheme);

		model.add(conceptResource, DCTerms.created,
				DateUtil.toString(concept.getCreated()));
		model.add(conceptResource, DCTerms.modified,
				DateUtil.toString(concept.getModified()));

		
		if (concept.getNotation() != null && !concept.getNotation().isEmpty()) {

			model.add(conceptResource, SKOS.NOTATION,
					concept.getNotation());

		}

		DatatypeProperty statusOnt = ontModel.createDatatypeProperty(ISOTHES.getURI()
				+ "status");
		Literal l = ontModel.createLiteral("status");
		statusOnt.addLabel(l);

		model.add(conceptResource, ISOTHES.STATUS, concept.getStatus()
				.toString());

		return model;
	}
}
