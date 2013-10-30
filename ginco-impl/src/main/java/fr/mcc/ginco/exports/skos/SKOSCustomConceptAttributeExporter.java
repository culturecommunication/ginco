package fr.mcc.ginco.exports.skos;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.semanticweb.skos.AddAssertion;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataRelationAssertion;
import org.semanticweb.skos.SKOSDataset;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.services.ICustomConceptAttributeService;

@Component("skosCustomConceptAttributeExporter")
public class SKOSCustomConceptAttributeExporter {

	@Inject
	@Named("customConceptAttributeService")
	private ICustomConceptAttributeService customConceptAttributeService;

	private static final String ginco_uri = "http://data.culture.fr/thesaurus/ginco/";

	public List<SKOSChange> exportCustomConceptAttributes(
			ThesaurusConcept concept, SKOSConcept conceptSKOS,
			SKOSDataFactory factory, SKOSDataset vocab) {

		List<SKOSChange> addList = new ArrayList<SKOSChange>();

		List<CustomConceptAttribute> attributes = customConceptAttributeService
				.getAttributesByEntity(concept);

		for (CustomConceptAttribute attribute : attributes) {
			if (attribute.getType().getExportable()) {
				SKOSDataRelationAssertion customAttributAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(URI.create(ginco_uri
										+ attribute.getType().getCode())),
								attribute.getLexicalValue());
				addList.add(new AddAssertion(vocab, customAttributAssertion));
			}
		}
		return addList;
	}
}
