package fr.mcc.ginco.exports.skos;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.services.ICustomConceptAttributeTypeService;
import fr.mcc.ginco.skos.namespaces.GINCO;

@Component("skosCustomConceptAttributeTypesExporter")
public class SKOSCustomConceptAttributeTypesExporter {

	@Inject
	@Named("customConceptAttributeTypeService")
	private ICustomConceptAttributeTypeService customConceptAttributeTypeService;

	public Map<String, String> exportCustomConceptAttributeTypes(Thesaurus thesaurus){

		Map<String, String> typeToOWLString = new HashMap<String, String>();
		List<String> codes = new ArrayList<String>();
		List<String> OWLStrings = new ArrayList<String>();

		List<CustomConceptAttributeType> types = customConceptAttributeTypeService
				.getAttributeTypesByThesaurus(thesaurus);

		if (!types.isEmpty()){
			OntModel ontmodel = ModelFactory.createOntologyModel();
			DatatypeProperty typeProperty = ontmodel.createDatatypeProperty(GINCO.CUSTOM_CONCEPT_ATTRIBUTE.getURI());
			typeProperty.addLabel(ontmodel.createLiteral(GINCO.CUSTOM_CONCEPT_ATTRIBUTE.getLocalName()));
			for (CustomConceptAttributeType type : types){
				if (type.getExportable()){
					DatatypeProperty typeAttributeProperty = ontmodel.createDatatypeProperty(GINCO.getResourceURI(type.getCode()));
					typeAttributeProperty.addRDFType(typeProperty);
					typeAttributeProperty.addLabel(ontmodel.createLiteral(type.getCode()));
					codes.add(type.getCode());
				}
			}

			ontmodel.setNsPrefix("ginco", GINCO.getURI());

			StringWriter sw = new StringWriter();
			ontmodel.write(sw, "RDF/XML-ABBREV");
			String result = sw.toString();

			int start = result.lastIndexOf("ginco/ns/\">") + "ginco/ns/\">".length()
					+ 2;
			int end = result.lastIndexOf("</rdf:RDF>");
			result = result.substring(start, end);

			OWLStrings = Arrays.asList(result.split("</ginco:CustomConceptAttribute>"));
			for (String code : codes){
				for (String type : OWLStrings){
					if(type.contains(GINCO.getURI() + code)){
						typeToOWLString.put(code, type);
					}
				}
			}
		}
		return typeToOWLString;
	}
}
