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
package fr.mcc.ginco.imports.ginco;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IAssociativeRelationshipDAO;
import fr.mcc.ginco.dao.IAssociativeRelationshipRoleDAO;
import fr.mcc.ginco.dao.IConceptHierarchicalRelationshipDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipServiceUtil;

/**
 * This class gives methods to import relationships (both hierarchical or associative)
 *
 */
@Component("gincoRelationshipImporter")
public class GincoRelationshipImporter {
	
	@Inject
	@Named("associativeRelationshipDAO")
	private IAssociativeRelationshipDAO associativeRelationshipDAO;
	
	@Inject
	@Named("associativeRelationshipRoleDAO")
	private IAssociativeRelationshipRoleDAO associativeRelationshipRoleDAO;
	
	@Inject
	@Named("conceptHierarchicalRelationshipDAO")
	private IConceptHierarchicalRelationshipDAO conceptHierarchicalRelationshipDAO;
	
	
	@Inject
	@Named("conceptHierarchicalRelationshipServiceUtil")
	private IConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;
	
	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;
	
	private static Logger logger = LoggerFactory.getLogger(GincoRelationshipImporter.class);

	
	/**
	 * This method stores all the hierarchical relationships
	 * @param relationsToImport
	 * @return The list of the updated parent concepts
	 */
	public List<ThesaurusConcept> storeHierarchicalRelationship(Map<String, JaxbList<ConceptHierarchicalRelationship>> relationsToImport) {
		List<ThesaurusConcept> updatedChildrenConcepts = new ArrayList<ThesaurusConcept>();
		String childId = null;
		if (relationsToImport != null && !relationsToImport.isEmpty()) {
			Iterator<Map.Entry<String, JaxbList<ConceptHierarchicalRelationship>>> entries = relationsToImport.entrySet().iterator();
			List<ConceptHierarchicalRelationship> parents = null;
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<ConceptHierarchicalRelationship>> entry = entries.next();
				childId = entry.getKey();
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					parents = entry.getValue().getList();
				}
				
				for (ConceptHierarchicalRelationship conceptHierarchicalRelationship : parents) {
					conceptHierarchicalRelationshipDAO.update(conceptHierarchicalRelationship);
					updatedChildrenConcepts.add(thesaurusConceptDAO.getById(childId));
				}
			}
		}
		
		//Processing and setting for all children concepts their root concept
		for (ThesaurusConcept thesaurusConcept : updatedChildrenConcepts) {
			List<ThesaurusConcept> roots = conceptHierarchicalRelationshipServiceUtil.getRootConcepts(thesaurusConcept);
			thesaurusConcept.setRootConcepts(new HashSet<ThesaurusConcept>(roots));
			thesaurusConceptDAO.update(thesaurusConcept);
		}
		
		return updatedChildrenConcepts;
	}
	
	/**
	 * This method stores all the associative relationships of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of the concepts which associations were updated
	 */
	public List<ThesaurusConcept> storeAssociativeRelationship(GincoExportedThesaurus exportedThesaurus) {
		Map<String, JaxbList<String>> associativeRelations = exportedThesaurus.getAssociativeRelationship();
		List<ThesaurusConcept> updatedConcepts = new ArrayList<ThesaurusConcept>();
		
		if (associativeRelations != null && !associativeRelations.isEmpty()) {
			Iterator<Map.Entry<String,  JaxbList<String>>> entries = associativeRelations.entrySet().iterator();
			String conceptId = null;
			List<String> associatedConceptIds = null;
			
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<String>> entry = entries.next();
				//Getting the id of the current concept
				conceptId = entry.getKey();
				
				//Getting the ids of its associated concepts
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					associatedConceptIds = entry.getValue().getList();
				}
				ThesaurusConcept updatedConcept = saveAssociativeRelationship(thesaurusConceptDAO.getById(conceptId), associatedConceptIds);
				updatedConcepts.add(thesaurusConceptDAO.update(updatedConcept));
			}
		}
		return updatedConcepts;
	}
	
	/**
	 * Private method to save associative relationships
	 * @param concept
	 * @param associatedConceptIds
	 * @return The updated {@link ThesaurusConcept}
	 * @throws BusinessException
	 */
	private ThesaurusConcept saveAssociativeRelationship(
			ThesaurusConcept concept, List<String> associatedConceptIds)
			throws BusinessException {
		Set<AssociativeRelationship> relations = new HashSet<AssociativeRelationship>();
		concept.setAssociativeRelationshipLeft(new HashSet<AssociativeRelationship>());
		concept.setAssociativeRelationshipRight(new HashSet<AssociativeRelationship>());
		
		for (String associatedConceptsId : associatedConceptIds) {
			logger.debug("Settings associated concept " + associatedConceptsId);
			ThesaurusConcept linkedThesaurusConcept = thesaurusConceptDAO.getById(associatedConceptsId);
			
			AssociativeRelationship alreadyExistingRelation = associativeRelationshipDAO.getAssociativeRelationship(concept.getIdentifier(), linkedThesaurusConcept.getIdentifier());
			
			if (alreadyExistingRelation == null) {
				AssociativeRelationship relationship = new AssociativeRelationship();
				AssociativeRelationship.Id relationshipId = new AssociativeRelationship.Id();
				relationshipId.setConcept1(concept.getIdentifier());
				relationshipId.setConcept2(associatedConceptsId);
				relationship.setIdentifier(relationshipId);
				relationship.setConceptLeft(concept);
				relationship.setConceptRight(thesaurusConceptDAO
						.getById(associatedConceptsId));
				relationship
						.setRelationshipRole(associativeRelationshipRoleDAO.getDefaultAssociativeRelationshipRole()	);
				relations.add(relationship);
				associativeRelationshipDAO.update(relationship);
			}
		}
		concept.getAssociativeRelationshipLeft().addAll(relations);

		return concept;
	}
}
