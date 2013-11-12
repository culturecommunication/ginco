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
package fr.mcc.ginco.imports;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.mcc.ginco.skos.namespaces.SKOS;

public class SKOSImportUtils {
	/**
	 * Gets the list of resources from the given model
	 * 
	 * @param model
	 * @param resource
	 * @return
	 */
	public static List<Resource> getSKOSRessources(Model model,
			final Resource resource) {
		SimpleSelector schemeSelector = new SimpleSelector(null, null,
				(RDFNode) null) {
			public boolean selects(Statement s) {
				if (s.getObject().isResource()
						&& !s.getSubject().hasProperty(RDF.type, OWL.Class)
						&& !s.getSubject().hasProperty(RDF.type,
								OWL.ObjectProperty)
						&& !s.getSubject().hasProperty(RDF.type,
								OWL.DatatypeProperty)) {
					return s.getObject().asResource().equals(resource);
				} else {
					return false;
				}
			}
		};
		StmtIterator iter = model.listStatements(schemeSelector);

		List<Resource> skosRessources = new ArrayList<Resource>();
		while (iter.hasNext()) {
			Statement s = iter.next();
			skosRessources.add(s.getSubject().asResource());
		}
		return skosRessources;
	}
	
	public static List<ObjectProperty> getBroaderTypeProperty(OntModel model) {
		List<ObjectProperty> skosRessources = new ArrayList<ObjectProperty>();
		ExtendedIterator<ObjectProperty> properties = model.listObjectProperties();
		while (properties.hasNext()) {
			ObjectProperty property = properties.next();
			if (property.hasSuperProperty(SKOS.BROADER, true)) {
				skosRessources.add(property);
			}
		}
		return skosRessources;
		
	}
	
}
