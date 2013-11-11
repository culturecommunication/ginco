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

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * This component is in charge of exporting groups to SKOS
 *
 */
@Component("skosGroupOntologyExporter")
public class SKOSGroupOntologyExporter {
	
	public OntModel buildGroupOntologyModel(OntModel ontModel) {


		OntClass groupClass = ontModel.createClass(ISOTHES.CONCEPT_GROUP
				.getURI());
		groupClass.addLabel(ontModel.createLiteral(ISOTHES.CONCEPT_GROUP
				.getLocalName()));
		groupClass.addSuperClass(SKOS.COLLECTION);

		ObjectProperty subGroup = ontModel
				.createObjectProperty(ISOTHES.SUB_GROUP.getURI());
		subGroup.addLabel(ontModel.createLiteral(ISOTHES.SUB_GROUP
				.getLocalName()));
		subGroup.addRange(groupClass);
		subGroup.addDomain(groupClass);

		ObjectProperty superGroup = ontModel
				.createObjectProperty(ISOTHES.SUPER_GROUP.getURI());
		superGroup.addLabel(ontModel.createLiteral(ISOTHES.SUPER_GROUP
				.getLocalName()));
		superGroup.addRange(groupClass.asResource());
		superGroup.addDomain(groupClass.asResource());

		return ontModel;
	}

	public OntClass addGroupTypeToOntModel(OntModel ontmodel, String groupType) {		
		OntClass groupTypeRes = ontmodel.createClass(GINCO.getResource(
				groupType).getURI());
		groupTypeRes.addLabel(ontmodel.createLiteral(GINCO.getResource(
				groupType).getLocalName()));
		groupTypeRes.addSuperClass(ontmodel.getResource(ISOTHES.CONCEPT_GROUP
				.getURI()));
		return groupTypeRes;
				
	}
	
}
