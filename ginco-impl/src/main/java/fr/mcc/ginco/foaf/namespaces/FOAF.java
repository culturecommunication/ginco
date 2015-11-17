package fr.mcc.ginco.foaf.namespaces;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by synov on 09/11/15.
 */
public class FOAF extends com.hp.hpl.jena.sparql.vocabulary.FOAF {

    private static Model m_model = ModelFactory.createDefaultModel();

    public static final Property focus = m_model.createProperty("http://xmlns.com/foaf/0.1/focus");

    /**
     * MAP of all alignment types
     */
    public static final Map<String, Property> FOAF_ALIGNMENTS = new HashMap<String, Property>() { {
        put("IMG", focus );
        put("RES", depiction);
    } };

}
