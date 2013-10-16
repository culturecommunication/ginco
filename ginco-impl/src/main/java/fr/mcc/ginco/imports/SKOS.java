package fr.mcc.ginco.imports;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * SKOS vocabulary class for namespace http://www.w3.org/2004/02/skos/core#
 */
public class SKOS {

	private static final String uri = "http://www.w3.org/2004/02/skos/core#";

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
	 * Resource for a conceptScheme
	 */
	public static final Resource CONCEPTSCHEME = m.createResource(uri
			+ "ConceptScheme");
	/**
	 * Resource for a Concept
	 */
	public static final Resource CONCEPT = m.createResource(uri + "Concept");

	/**
	 * prefLabel property
	 */
	public static final Property PREF_LABEL = m.createProperty(uri
			+ "prefLabel");

	/**
	 * altLabel property
	 */
	public static final Property ALT_LABEL = m.createProperty(uri + "altLabel");

	/**
	 * hiddenLabel property
	 */
	public static final Property HIDDEN_LABEL = m.createProperty(uri
			+ "hiddenLabel");

	/**
	 * broader property
	 */
	public static final Property BROADER = m.createProperty(uri + "broader");

	/**
	 * narrower property
	 */
	public static final Property NARROWER = m.createProperty(uri + "narrower");

	/**
	 * related property
	 */
	public static final Property RELATED = m.createProperty(uri + "related");

    /**
     * resource for Collection
     */
    public static final Resource COLLECTION = m.createResource(uri
            + "Collection");

    /**
     * member property
     */
    public static final Property MEMBER = m.createProperty(uri + "member");

    /**
     * inScheme property
     */
    public static final Property IN_SCHEME = m.createProperty(uri + "inScheme");

	/**
	 * scopeNote property
	 */
	public static final Property SCOPE_NOTE = m.createProperty(uri + "scopeNote");

	/**
	 * changeNote property
	 */
	public static final Property CHANGE_NOTE = m.createProperty(uri + "changeNote");

	/**
	 * definition property
	 */
	public static final Property DEFINITION = m.createProperty(uri + "definition");

	/**
	 * editorialNote property
	 */
	public static final Property EDITORIAL_NOTE = m.createProperty(uri + "editorialNote");

	/**
	 * example property
	 */
	public static final Property EXAMPLE = m.createProperty(uri + "example");

	/**
	 * historyNote property
	 */
	public static final Property HISTORY_NOTE = m.createProperty(uri + "historyNote");

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
	public static final Property NOTATION = m.createProperty(uri + "notation");
}