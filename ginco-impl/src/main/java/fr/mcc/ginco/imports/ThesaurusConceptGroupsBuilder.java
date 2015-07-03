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
package fr.mcc.ginco.imports;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.dao.IThesaurusConceptGroupDAO;
import fr.mcc.ginco.dao.IThesaurusConceptGroupLabelDAO;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * builder in charge of building thesaurus concept groups
 */
@Service("skosConceptGroupsBuilder")
public class ThesaurusConceptGroupsBuilder extends AbstractBuilder {
	@Inject
	@Named("skosConceptGroupBuilder")
	private ThesaurusConceptGroupBuilder conceptGroupBuilder;

	@Inject
	@Named("skosConceptGroupLabelBuilder")
	private ConceptGroupLabelBuilder conceptGroupLabelBuilder;

	@Inject
	private IThesaurusConceptGroupDAO thesaurusGroupDAO;

	@Inject
	private IThesaurusConceptGroupLabelDAO conceptGroupLabelDAO;

	@Inject
	@Named("skosImportUtils")
	private SKOSImportUtils skosImportUtils;

	/**
	 * Builds the thesaurus concept groups from the model
	 *
	 * @param thesaurus
	 * @param model
	 */
	public void buildConceptGroups(Thesaurus thesaurus, Model model, Map<String, ThesaurusConceptGroup> builtConceptGroups, Set<String> builtThesaurusArrays) {
		List<Resource> skosCollections = skosImportUtils.getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			// don't process collections already imported as arrays
			if(!builtThesaurusArrays.contains(skosCollection.getURI())) {
				ThesaurusConceptGroup group = conceptGroupBuilder.buildConceptGroup(skosCollection,
						model, thesaurus, builtConceptGroups);
				thesaurusGroupDAO.update(group);
				ThesaurusConceptGroupLabel nodeLabel = conceptGroupLabelBuilder.buildNodeLabel(
						skosCollection, thesaurus, group);
				conceptGroupLabelDAO.update(nodeLabel);
			}
		}
	}

	/**
	 * Builds thesaurus concept groups relations from the model
	 *
	 * @param thesaurus
	 * @param model
	 */

	public void buildChildrenGroups(Thesaurus thesaurus, Model model, Map<String, ThesaurusConceptGroup> builtConceptGroups) {
		List<Resource> skosCollections = skosImportUtils.getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			List<ThesaurusConceptGroup> childrenGroups = conceptGroupBuilder
					.getChildrenGroups(skosCollection, thesaurus, builtConceptGroups);
			
			ThesaurusConceptGroup aGroup = thesaurusGroupDAO.getById(skosCollection.getURI());
//			if (aGroup.getConcepts().size() == 0 && childrenGroups.size() > 0) {
//				for (ThesaurusConceptGroup childrenGroup : childrenGroups) {
//					if (childrenGroup.getSuperOrdinateConcept() != null) {
//						aGroup.setSuperOrdinateConcept(childrenGroup.getSuperOrdinateConcept());
//					}
//				}
//			}
			for (ThesaurusConceptGroup childrenGroup : childrenGroups) {
				thesaurusGroupDAO.update(childrenGroup);
			}
		}
	}
}
