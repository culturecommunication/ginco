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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.dao.INodeLabelDAO;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.skos.namespaces.SKOS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;
import java.util.Map;

/**
 * builder in charge of building thesaurus arrays
 */
@Service("skosArraysBuilder")
public class ThesaurusArraysBuilder extends AbstractBuilder {
	
	private static Logger logger = LoggerFactory.getLogger(ThesaurusArraysBuilder.class);
	
	@Inject
	@Named("skosArrayBuilder")
	private ThesaurusArrayBuilder arrayBuilder;

	@Inject
	@Named("skosNodeLabelBuilder")
	private NodeLabelBuilder nodeLabelBuilder;

	@Inject
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@Inject
	private INodeLabelDAO nodeLabelDAO;

	@Inject
	@Named("skosImportUtils")
	private SKOSImportUtils skosImportUtils;

	/**
	 * Builds the thesaurus arrays from the model
	 *
	 * @param thesaurus
	 * @param model
	 */
	public void buildArrays(Thesaurus thesaurus, Model model, Map<String, ThesaurusArray> builtArrays) {
		List<Resource> skosCollections = skosImportUtils.getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			ThesaurusArray array = null;
			try {
				array = arrayBuilder.buildArray(skosCollection,
						model, thesaurus, builtArrays);
			} catch (BusinessException be) {
				logger.debug("SKOS Collection "+skosCollection.getURI()+" not considered like a thesaurus array - will be attempted like a concept group");
				continue;
			}
			thesaurusArrayDAO.update(array);
			NodeLabel nodeLabel = nodeLabelBuilder.buildNodeLabel(
					skosCollection, thesaurus, array);
			nodeLabelDAO.update(nodeLabel);
		}
	}

	/**
	 * Builds thesaurus array relations from the model
	 *
	 * @param thesaurus
	 * @param model
	 */

	public void buildChildrenArrays(Thesaurus thesaurus, Model model, Map<String, ThesaurusArray> builtArrays) {
		List<Resource> skosCollections = skosImportUtils.getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			List<ThesaurusArray> childrenArrays = arrayBuilder
					.getChildrenArrays(skosCollection, thesaurus, builtArrays);
			ThesaurusArray array = thesaurusArrayDAO.getById(skosCollection.getURI());
			
			// if we had this skos:Collection as an array...
			if(array != null) {				
				if (array.getConcepts().size() == 0 && childrenArrays.size() > 0) {
					for (ThesaurusArray childrenArray : childrenArrays) {
						if (childrenArray.getSuperOrdinateConcept() != null) {
							array.setSuperOrdinateConcept(childrenArray.getSuperOrdinateConcept());
						}
					}
				}
				for (ThesaurusArray childrenArray : childrenArrays) {
					thesaurusArrayDAO.update(childrenArray);
				}
			}
		}
	}
}
