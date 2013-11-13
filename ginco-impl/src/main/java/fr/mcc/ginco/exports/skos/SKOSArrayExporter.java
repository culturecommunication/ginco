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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting collections to SKOS
 *
 */
@Component("skosArrayExporter")
public class SKOSArrayExporter {

	@Inject
	@Named("thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;

	@Inject
	@Named("nodeLabelService")
	private INodeLabelService nodeLabelService;

	/**
	 * Export thesaurus arrays from the given thesaurus in SKOS format
	 * @param thesaurus
	 * @return
	 */
	public Model exportCollections(Thesaurus thesaurus, Model model) {
		List<ThesaurusArray> arrays = thesaurusArrayService
				.getAllThesaurusArrayByThesaurusId(null, thesaurus.getIdentifier());

		if (arrays.size() != 0) {

			for (ThesaurusArray array : arrays) {
				NodeLabel label = nodeLabelService.getByThesaurusArray(array
						.getIdentifier());

				Resource collectionRes = model.createResource(
						array.getIdentifier(), SKOS.COLLECTION);

				Resource inScheme = model.createResource(thesaurus
						.getIdentifier());
				model.add(collectionRes, SKOS.IN_SCHEME, inScheme);

				collectionRes
						.addProperty(SKOS.PREF_LABEL, label.getLexicalValue(),
								label.getLanguage().getId());

				for (ThesaurusArrayConcept arrayConcept : array.getConcepts()) {
						Resource y = model.createResource(arrayConcept.getIdentifier().getConceptId());
						model.add(collectionRes, SKOS.MEMBER, y);
				}
				for (ThesaurusArray childrenArray : thesaurusArrayService.getChildrenArrays(array.getIdentifier())){
					Resource arrayMember = model.createResource(childrenArray.getIdentifier());
					model.add(collectionRes, SKOS.MEMBER, arrayMember);
				}

				model.add(collectionRes, DCTerms.created, DateUtil.toISO8601String(label.getCreated()));
				model.add(collectionRes, DCTerms.modified, DateUtil.toISO8601String(label.getModified()));
			}
			
		}

		return model;
	}
}
