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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupType;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptGroupTypeService;

/**
 * This component is in charge of exporting groups to SKOS
 *
 */
@Component("skosGroupsExporter")
public class SKOSGroupsExporter {

	@Inject
	@Named("thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;

	
	@Inject
	@Named("thesaurusConceptGroupTypeService")
	private IThesaurusConceptGroupTypeService thesaurusConceptGroupTypeService;
		
	@Inject
	@Named("skosGroupOntologyExporter")
	private  SKOSGroupOntologyExporter skosGroupOntologyExporter;
	
	@Inject
	@Named("skosGroupExporter")
	private SKOSGroupExporter skosGroupExporter;

	public Model exportGroups(Thesaurus thesaurus, Model model, OntModel ontModel) {
		List<ThesaurusConceptGroup> groups = thesaurusConceptGroupService
				.getAllThesaurusConceptGroupsByThesaurusId(null,
						thesaurus.getIdentifier());
		if (!groups.isEmpty()) {
			skosGroupOntologyExporter.buildGroupOntologyModel(ontModel);
			
			for (ThesaurusConceptGroupType groupType:thesaurusConceptGroupTypeService.getConceptGroupTypeList()) {
				skosGroupOntologyExporter.addGroupTypeToOntModel(ontModel, groupType
						.getSkosLabel());		
			}
			
			for (ThesaurusConceptGroup group : groups) {						
				skosGroupExporter.exportGroup(thesaurus, group, model);
			}
		
		}
		return model;
	}
	
}
