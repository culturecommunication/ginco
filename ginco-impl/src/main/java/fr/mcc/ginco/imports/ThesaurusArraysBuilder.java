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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.INodeLabelDAO;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.ConceptHierarchyUtil;

/**
 * builder in charge of building thesaurus arrays
 * 
 */
@Service("skosArraysBuilder")
public class ThesaurusArraysBuilder extends AbstractBuilder {	
	@Inject
	@Named("skosArrayBuilder")
	private ThesaurusArrayBuilder arrayBuilder;
	
	@Inject
	@Named("skosNodeLabelBuilder")
	private NodeLabelBuilder nodeLabelBuilder;
	
	@Inject
	@Named("thesaurusArrayDAO")
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@Inject
	@Named("nodeLabelDAO")
	private INodeLabelDAO nodeLabelDAO;
	
	/**
	 * Builds the thesaurus arrays from the model
	 * 
	 * @param thesaurus
	 * @param model
	 */
	public void buildArrays(Thesaurus thesaurus, Model model) {
		List<Resource> skosCollections = SKOSImportUtils.getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			ThesaurusArray array = arrayBuilder.buildArray(skosCollection,
					model, thesaurus);
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

	public void buildChildrenArrays(Thesaurus thesaurus, Model model) {
		List<Resource> skosCollections = SKOSImportUtils.getSKOSRessources(model,
				SKOS.COLLECTION);
		for (Resource skosCollection : skosCollections) {
			List<ThesaurusArray> childrenArrays = arrayBuilder
					.getChildrenArrays(skosCollection, thesaurus);
			for (ThesaurusArray childrenArray : childrenArrays) {
				thesaurusArrayDAO.update(childrenArray);
			}
		}
	}
}
