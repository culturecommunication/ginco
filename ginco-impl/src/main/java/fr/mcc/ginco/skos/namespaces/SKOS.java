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
package fr.mcc.ginco.skos.namespaces;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * SKOS vocabulary class for namespace http://www.w3.org/2004/02/skos/core#
 */

public final class SKOS {
	
	private SKOS() {};

	private static final String URI = "http://www.w3.org/2004/02/skos/core#";

	/**
	 * returns the URI for this schema
	 *
	 * @return the URI for this schema
	 */
	public static String getURI() {
		return URI;
	}

	private static Model m = ModelFactory.createDefaultModel();

	/**
	 * Resource for a conceptScheme
	 */
	public static final Resource CONCEPTSCHEME = m.createResource(URI
			+ "ConceptScheme");
	/**
	 * Resource for a Concept
	 */
	public static final Resource CONCEPT = m.createResource(URI + "Concept");

	/**
	 * prefLabel property
	 */
	public static final Property PREF_LABEL = m.createProperty(URI
			+ "prefLabel");

	/**
	 * altLabel property
	 */
	public static final Property ALT_LABEL = m.createProperty(URI + "altLabel");

	/**
	 * hiddenLabel property
	 */
	public static final Property HIDDEN_LABEL = m.createProperty(URI
			+ "hiddenLabel");

	/**
	 * broader property
	 */
	public static final Property BROADER = m.createProperty(URI + "broader");

	/**
	 * narrower property
	 */
	public static final Property NARROWER = m.createProperty(URI + "narrower");

	/**
	 * related property
	 */
	public static final Property RELATED = m.createProperty(URI + "related");

    /**
     * resource for Collection
     */
    public static final Resource COLLECTION = m.createResource(URI
            + "Collection");

    /**
     * member property
     */
    public static final Property MEMBER = m.createProperty(URI + "member");

    /**
     * inScheme property
     */
    public static final Property IN_SCHEME = m.createProperty(URI + "inScheme");

	/**
	 * scopeNote property
	 */
	public static final Property SCOPE_NOTE = m.createProperty(URI + "scopeNote");

	/**
	 * changeNote property
	 */
	public static final Property CHANGE_NOTE = m.createProperty(URI + "changeNote");

	/**
	 * definition property
	 */
	public static final Property DEFINITION = m.createProperty(URI + "definition");

	/**
	 * editorialNote property
	 */
	public static final Property EDITORIAL_NOTE = m.createProperty(URI + "editorialNote");

	/**
	 * example property
	 */
	public static final Property EXAMPLE = m.createProperty(URI + "example");

	/**
	 * historyNote property
	 */
	public static final Property HISTORY_NOTE = m.createProperty(URI + "historyNote");

	/**
	 * MAP of all notes types
	 */
	public static final Map<String, Property> SKOS_NOTES = new HashMap<String, Property>() {{
		put(SCOPE_NOTE.getLocalName(), SCOPE_NOTE);
		put(CHANGE_NOTE.getLocalName(),CHANGE_NOTE);
		put(DEFINITION.getLocalName(),DEFINITION);
		put(EDITORIAL_NOTE.getLocalName(),EDITORIAL_NOTE);
		put(EXAMPLE.getLocalName(),EXAMPLE);
		put(HISTORY_NOTE.getLocalName(),HISTORY_NOTE);
	}};

	/**
	 * notation property
	 */
	public static final Property NOTATION = m.createProperty(URI + "notation");

	/**
	 * exactMatch property
	 */
	public static final Property EXACT_MATCH = m.createProperty(URI + "exactMatch");

	/**
	 * closeMatch property
	 */
	public static final Property CLOSE_MATCH = m.createProperty(URI + "closeMatch");

	/**
	 * narrowMatch property
	 */
	public static final Property NARROW_MATCH = m.createProperty(URI + "narrowMatch");

	/**
	 * broadMatch property
	 */
	public static final Property BROAD_MATCH = m.createProperty(URI + "broadMatch");

	/**
	 * relatedMatch property
	 */
	public static final Property RELATED_MATCH = m.createProperty(URI + "relatedMatch");
	
	
	/**
	 * hasTopConcept
	 */
	public static final Property HAS_TOP_CONCEPT = m.createProperty(URI + "hasTopConcept");

	/**
	 * MAP of all alignment types
	 */
	public static final Map<String, Property> SKOS_ALIGNMENTS = new HashMap<String, Property>() {{
		put("=EQ", EXACT_MATCH);
		put("~EQ",CLOSE_MATCH);
		put("BM",BROAD_MATCH);
		put("NM",NARROW_MATCH);
		put("RM",RELATED_MATCH);
	}};
}