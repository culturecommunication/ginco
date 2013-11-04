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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.services.IThesaurusConceptGroupLabelService;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting groups to SKOS
 *
 */
@Component("skosGroupExporter")
public class SKOSGroupExporter {

	@Inject
	@Named("thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;

	@Inject
	@Named("thesaurusConceptGroupLabelService")
	private IThesaurusConceptGroupLabelService thesaurusConceptGroupLabelService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	public String exportGroups(Thesaurus thesaurus) {
		List<ThesaurusConceptGroup> groups = thesaurusConceptGroupService
				.getAllThesaurusConceptGroupsByThesaurusId(null,
						thesaurus.getIdentifier());
		if (!groups.isEmpty()) {

			Model model = ModelFactory.createDefaultModel();
			OntModel ontmodel = buildGroupOntologyModel();

			for (ThesaurusConceptGroup group : groups) {
				addGroupToOntModel(ontmodel, group.getConceptGroupType()
						.getSkosLabel());
				buildGroup(thesaurus, group, model);
			}

			model.setNsPrefix("ginco", GINCO.getURI());
			model.setNsPrefix("iso-thes", ISOTHES.getURI());
			model.setNsPrefix("skos", SKOS.getURI());
			model.setNsPrefix("dct", DCTerms.getURI());

			StringWriter ontsw = new StringWriter();
			ontmodel.write(ontsw, "RDF/XML-ABBREV");
			String ontresult = ontsw.toString();

			StringWriter sw = new StringWriter();
			model.write(sw, "RDF/XML-ABBREV");
			String result = sw.toString();
			result = result.replaceAll("_REMOVEME_", "");

			int ontstart = ontresult.lastIndexOf("rdf-schema#\">")
					+ "rdf-schema#\">".length() + 2;
			int ontend = ontresult.lastIndexOf("</rdf:RDF>");

			int start = result.lastIndexOf("rdf-schema#\">")
					+ "rdf-schema#\">".length() + 2;
			int end = result.lastIndexOf("</rdf:RDF>");

			return ontresult.substring(ontstart, ontend)
					+ result.substring(start, end);
		}
		return "";
	}

	public OntModel buildGroupOntologyModel() {

		OntModel ontmodel = ModelFactory.createOntologyModel();

		OntClass groupClass = ontmodel.createClass(ISOTHES.CONCEPT_GROUP
				.getURI());
		groupClass.addLabel(ontmodel.createLiteral(ISOTHES.CONCEPT_GROUP
				.getLocalName()));
		groupClass.addSuperClass(SKOS.COLLECTION);

		ObjectProperty subGroup = ontmodel
				.createObjectProperty(ISOTHES.SUB_GROUP.getURI());
		subGroup.addLabel(ontmodel.createLiteral(ISOTHES.SUB_GROUP
				.getLocalName()));
		subGroup.addRange(groupClass);
		subGroup.addDomain(groupClass);

		ObjectProperty superGroup = ontmodel
				.createObjectProperty(ISOTHES.SUPER_GROUP.getURI());
		superGroup.addLabel(ontmodel.createLiteral(ISOTHES.SUPER_GROUP
				.getLocalName()));
		superGroup.addRange(groupClass);
		superGroup.addDomain(groupClass);

		return ontmodel;
	}

	public void addGroupToOntModel(OntModel ontmodel, String groupType) {
		OntClass groupTypeRes = ontmodel.createClass(GINCO.getResource(
				groupType).getURI());
		groupTypeRes.addLabel(ontmodel.createLiteral(GINCO.getResource(
				groupType).getLocalName()));
		groupTypeRes.addSuperClass(ontmodel.getResource(ISOTHES.CONCEPT_GROUP
				.getURI()));
	}

	public void buildGroup(Thesaurus thesaurus, ThesaurusConceptGroup group,
			Model model) {
		ThesaurusConceptGroupLabel label = thesaurusConceptGroupLabelService
				.getByThesaurusConceptGroup(group.getIdentifier());

		Resource groupRes = model.createResource(group.getIdentifier(),
				GINCO.getResource(group.getConceptGroupType().getSkosLabel()));

		Resource inScheme = model.createResource(thesaurus.getIdentifier());

		model.add(groupRes, SKOS.IN_SCHEME, inScheme);

		model.add(groupRes, DCTerms.created,
				DateUtil.toString(label.getCreated()));

		model.add(groupRes, DCTerms.modified,
				DateUtil.toString(label.getModified()));

		model.add(groupRes, RDFS.label, label.getLexicalValue(), label
				.getLanguage().getPart1());

		String notation = group.getNotation();
		if (!"".equals(notation) && notation != null) {
			model.add(groupRes, SKOS.NOTATION, group.getNotation());
		}

		List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
		if (group.getIsDynamic()) {
			if (group.getParentConcept() != null) {
				concepts = thesaurusConceptService
						.getRecursiveChildrenByConceptId(group
								.getParentConcept().getIdentifier());
				concepts.add(group.getParentConcept());
			}
		} else {
			if (group.getConcepts() != null) {
				concepts.addAll(group.getConcepts());
			}
		}

		for (ThesaurusConcept concept : concepts) {
			Resource conceptRes = model.createResource(concept.getIdentifier());
			model.add(groupRes, SKOS.MEMBER, conceptRes);
		}

		ThesaurusConceptGroup parentGroup = group.getParent();
		if (parentGroup != null) {
			Resource parentRes = ResourceFactory.createResource(parentGroup
					.getIdentifier() + "_REMOVEME_");
			model.add(groupRes, ISOTHES.SUPER_GROUP, parentRes);
		}

		List<ThesaurusConceptGroup> childGroups = thesaurusConceptGroupService
				.getChildGroups(group.getIdentifier());
		if (!childGroups.isEmpty()) {
			for (ThesaurusConceptGroup childGroup : childGroups) {
				Resource childRes = ResourceFactory.createResource(childGroup
						.getIdentifier() + "_REMOVEME_");
				model.add(groupRes, ISOTHES.SUB_GROUP, childRes);
			}
		}
	}
}
