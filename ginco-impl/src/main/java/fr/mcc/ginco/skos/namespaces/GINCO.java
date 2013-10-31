package fr.mcc.ginco.skos.namespaces;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * GINCO vocabulary class for namespace http://data.culture.fr/thesaurus/ginco/ns/
 */

public class GINCO {

	private static final String uri = "http://data.culture.fr/thesaurus/ginco/ns/";

	/**
	 * returns the URI for this schema
	 *
	 * @return the URI for this schema
	 */
	public static String getURI() {
		return uri;
	}

	private static Model m = ModelFactory.createDefaultModel();

	/**
	 * Get GINCO resource URIs
	 */
	public static String getResourceURI(String type){
		return uri + type;
	}

	/**
	 * Get GINCO resource
	 */
	public static Resource getResource(String type){
		return m.createResource(uri + type);
	}

	/**
	 * custom concept attribute resource
	 */
	public static final Resource CUSTOM_CONCEPT_ATTRIBUTE = m
			.createResource(uri + "CustomConceptAttribute");

}
