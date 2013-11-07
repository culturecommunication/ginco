package fr.mcc.ginco.exports.skos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.services.ICustomConceptAttributeService;
import fr.mcc.ginco.skos.namespaces.GINCO;

@Component("skosCustomConceptAttributeExporter2")
public class SKOSCustomConceptAttributeExporter2 {

	@Inject
	@Named("customConceptAttributeService")
	private ICustomConceptAttributeService customConceptAttributeService;


	public Model exportCustomConceptAttributes(
			ThesaurusConcept concept, Model model, Resource conceptResource) {

		List<CustomConceptAttribute> attributes = customConceptAttributeService
				.getAttributesByEntity(concept);
		
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		DatatypeProperty customAttrOnt = m.createDatatypeProperty(GINCO.getURI()
				+ "CustomConceptAttribute");
		Literal l = m.createLiteral("CustomConceptAttribute");
		customAttrOnt.addLabel(l);
		
		
	
		for (CustomConceptAttribute attribute : attributes) {
			if (attribute.getType().getExportable()) {						
				
			Resource customAttrRes  = model.createResource(GINCO.getURI() + attribute.getType().getCode(), GINCO.CUSTOM_CONCEPT_ATTRIBUTE);
			model.add(customAttrRes, RDFS.label, attribute.getType().getCode());
		
			Property customAttributeProperty = model.createProperty(GINCO.getURI() + attribute.getType().getCode());
				
			model.add(conceptResource, customAttributeProperty, attribute.getLexicalValue());				
				
			}
		}
		model.add(m);
		return model;
	}
}
