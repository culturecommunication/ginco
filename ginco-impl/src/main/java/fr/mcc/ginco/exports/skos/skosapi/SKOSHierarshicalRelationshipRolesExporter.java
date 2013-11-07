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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.mcc.ginco.enums.ConceptHierarchicalRelationshipRoleEnum;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.SKOS;

@Component("skosHierarshicalRelationshipRolesExporter")
public class SKOSHierarshicalRelationshipRolesExporter {

	public Map<String, String> exportHierarshicalRelationshipRoles(){
		Map<String, String> roleToOWLString = new HashMap<String, String>();
		ConceptHierarchicalRelationshipRoleEnum[] roles = ConceptHierarchicalRelationshipRoleEnum.values();
			OntModel ontmodel = ModelFactory.createOntologyModel();
			for (ConceptHierarchicalRelationshipRoleEnum role : roles){
				String parentSkosLabel = role.getParentSkosLabel();
				String childSkosLabel = role.getChildSkosLabel();
				if(!parentSkosLabel.isEmpty() && !childSkosLabel.isEmpty()){

					ObjectProperty broaderRoleProperty = ontmodel.createObjectProperty(GINCO.getResourceURI(parentSkosLabel));
					broaderRoleProperty.addLabel(ontmodel.createLiteral(GINCO.getResource(parentSkosLabel).getLocalName()));
					broaderRoleProperty.addProperty(RDFS.subPropertyOf, SKOS.BROADER);

					ObjectProperty narrowerRoleProperty = ontmodel.createObjectProperty(GINCO.getResourceURI(childSkosLabel));
					narrowerRoleProperty.addLabel(ontmodel.createLiteral(GINCO.getResource(childSkosLabel).getLocalName()));
					narrowerRoleProperty.addProperty(RDFS.subPropertyOf, SKOS.NARROWER);

					broaderRoleProperty.addProperty(OWL.inverseOf, narrowerRoleProperty);
					narrowerRoleProperty.addProperty(OWL.inverseOf, broaderRoleProperty);

					ontmodel.setNsPrefix("ginco", GINCO.getURI());

					StringWriter sw = new StringWriter();
					ontmodel.write(sw, "RDF/XML-ABBREV");
					String result = sw.toString();

					int start = result.lastIndexOf("ginco/ns/\">") + "ginco/ns/\">".length()
							+ 2;
					int end = result.lastIndexOf("</rdf:RDF>");
					String[] owlStrings = result.substring(start, end).split("</owl:ObjectProperty>");
					for (String owlString : owlStrings){
						if (owlString.contains("<rdfs:label>" + parentSkosLabel)){
							roleToOWLString.put(parentSkosLabel, owlString);
						} else if (owlString.contains("<rdfs:label>" + childSkosLabel)){
							roleToOWLString.put(childSkosLabel, owlString);
						}
					}
				}
		}
		return roleToOWLString;
	}
}
