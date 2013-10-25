package fr.mcc.ginco.imports;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * ISOTHES vocabulary class for namespace http://www.niso.org/schemas/iso25964/skos-thes#
 */

public class ISOTHES {
	private static final String uri = "http://www.niso.org/schemas/iso25964/skos-thes#";

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
	 * prefLabel property
	 */
	public static final Property STATUS = m.createProperty(uri
			+ "status");

}
