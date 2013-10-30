package fr.mcc.ginco.imports;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

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
	 * Get URIs for different custom concept attribute types
	 */
	public static String getCustomAttributeTypeURI(String type){
		return uri + type;
	}

	/**
	 * CustomConceptAttribute URI
	 */
	public static final String CUSTOM_CONCEPT_ATTRIBUTE_URI = uri + "CustomConceptAttribute";

}
