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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
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
	

	public void exportGroup(Thesaurus thesaurus, ThesaurusConceptGroup group,
			Model model) {
		ThesaurusConceptGroupLabel label = thesaurusConceptGroupLabelService
				.getByThesaurusConceptGroup(group.getIdentifier());

		Resource groupRes = model.createResource(group.getIdentifier(),
				GINCO.getResource(group.getConceptGroupType().getSkosLabel()));

		Resource inScheme = model.createResource(thesaurus.getIdentifier());

		model.add(groupRes, SKOS.IN_SCHEME, inScheme);

		model.add(groupRes, DCTerms.created,
				DateUtil.toISO8601String(label.getCreated()));

		model.add(groupRes, DCTerms.modified,
				DateUtil.toISO8601String(label.getModified()));

		model.add(groupRes, RDFS.label, label.getLexicalValue(), label
				.getLanguage().getId());

		String notation = group.getNotation();
		if (StringUtils.isNotEmpty(notation)) {
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
					.getIdentifier());
			model.add(groupRes, ISOTHES.SUPER_GROUP, parentRes);
		}

		List<ThesaurusConceptGroup> childGroups = thesaurusConceptGroupService
				.getChildGroups(group.getIdentifier());
		if (!childGroups.isEmpty()) {
			for (ThesaurusConceptGroup childGroup : childGroups) {
				Resource childRes = ResourceFactory.createResource(childGroup
						.getIdentifier());
				model.add(groupRes, ISOTHES.SUB_GROUP, childRes);
			}
		}
	}
}
