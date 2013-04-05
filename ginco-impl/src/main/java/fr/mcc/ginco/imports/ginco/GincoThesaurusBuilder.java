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
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IAssociativeRelationshipDAO;
import fr.mcc.ginco.dao.IAssociativeRelationshipRoleDAO;
import fr.mcc.ginco.dao.INodeLabelDAO;
import fr.mcc.ginco.dao.INoteDAO;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusConceptGroupDAO;
import fr.mcc.ginco.dao.IThesaurusConceptGroupLabelDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusConceptService;

/**
 * This class :
 * - extracts data from a {@link GincoExportedThesaurus} object,
 * - stores it in beans
 * - persists beans
 *
 */
@Component("gincoThesaurusBuilder")
public class GincoThesaurusBuilder {
	
	@Inject
	@Named("associativeRelationshipDAO")
	private IAssociativeRelationshipDAO associativeRelationshipDAO;
	
	@Inject
	@Named("associativeRelationshipRoleDAO")
	private IAssociativeRelationshipRoleDAO associativeRelationshipRoleDAO;
	
	@Inject
	@Named("nodeLabelDAO")
	private INodeLabelDAO nodeLabelDAO;
	
	@Inject
	@Named("noteDAO")
	private INoteDAO noteDAO;
	
	@Inject
	@Named("thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

	@Inject
	@Named("thesaurusArrayDAO")
	private IThesaurusArrayDAO thesaurusArrayDAO;
	
	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;
	
	@Inject
	@Named("thesaurusConceptGroupDAO")
	private IThesaurusConceptGroupDAO thesaurusConceptGroupDAO;
	
	@Inject
	@Named("thesaurusConceptGroupLabelDAO")
	private IThesaurusConceptGroupLabelDAO thesaurusConceptGroupLabelDAO;
	
	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@Inject
	@Named("thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;
	
	@Inject
	@Named("thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;
	
	@Log
	private Logger logger;
	
	/**
	 * This method stores a Ginco Thesaurus with all its objects (concepts, terms, arrays, groups etc.).
	 * 
	 * The order of the import is important :
	 * - Thesaurus, concepts, terms, arrays, arrayslabels, groups, grouplabels, thesaurus versions,
	 * hierarchical relationship, associative relationship, term notes and concepts notes
	 * 
	 * @param {@GincoExportedThesaurus} : the previously exported thesaurus we want to import
	 * @return The imported {@link Thesaurus}
	 */
	public Thesaurus storeGincoExportedThesaurus(GincoExportedThesaurus exportedThesaurus) {
		Thesaurus thesaurus = thesaurusDAO.update(exportedThesaurus.getThesaurus());
		storeConcepts(exportedThesaurus);
		storeTerms(exportedThesaurus);
		
		storeArrays(exportedThesaurus);
		storeArrayLabels(exportedThesaurus);
		
		storeGroups(exportedThesaurus);
		storeGroupLabels(exportedThesaurus);
		
		storeVersions(exportedThesaurus);
		
		storeHierarchicalRelationship(exportedThesaurus);
		storeAssociativeRelationship(exportedThesaurus);
		
		storeTermNotes(exportedThesaurus);
		storeConceptNotes(exportedThesaurus);
		return thesaurus;
	}
	
	/**
	 * This method stores all the concepts of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored concepts
	 */
	public List<ThesaurusConcept> storeConcepts(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusConcept> updatedConcepts = new ArrayList<ThesaurusConcept>();
		if (exportedThesaurus.getConcepts() != null && !exportedThesaurus.getConcepts().isEmpty()) {
			for (ThesaurusConcept concept : exportedThesaurus.getConcepts()) {
				concept.setThesaurus(exportedThesaurus.getThesaurus());
				updatedConcepts.add(thesaurusConceptDAO.update(concept));
			}			
		}
		return updatedConcepts;
	}
	
	/**
	 * This method stores all the terms of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored terms
	 */
	public List<ThesaurusTerm> storeTerms(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusTerm> updatedTerms = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm term : exportedThesaurus.getTerms()) {
			term.setThesaurus(exportedThesaurus.getThesaurus());
			updatedTerms.add(thesaurusTermDAO.update(term));
		}
		return updatedTerms;
	}
	
	/**
	 * This method stores all the versions of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored versions
	 */
	public List<ThesaurusVersionHistory> storeVersions(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusVersionHistory> updatedVersion = new ArrayList<ThesaurusVersionHistory>();
		for (ThesaurusVersionHistory version : exportedThesaurus.getThesaurusVersions()) {
			version.setThesaurus(exportedThesaurus.getThesaurus());
			updatedVersion.add(thesaurusVersionHistoryDAO.update(version));
		}
		return updatedVersion;
	}
	
	/**
	 * This method stores all the array labels of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored labels
	 */
	public List<NodeLabel> storeArrayLabels(GincoExportedThesaurus exportedThesaurus) {
		Map<String, JaxbList<NodeLabel>> labels = exportedThesaurus.getConceptArrayLabels();
		List<NodeLabel> updatedLabels = new ArrayList<NodeLabel>();
		
		if (labels != null && !labels.isEmpty()) {
			Iterator<Map.Entry<String,  JaxbList<NodeLabel>>> entries = labels.entrySet().iterator();
			String arrayId = null;
			List<NodeLabel> nodeLabel = null;
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<NodeLabel>> entry = entries.next();
				//Getting the id of the array
				arrayId = entry.getKey();
				
				//Getting the NodeLabels for this array
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					nodeLabel = entry.getValue().getList();
				}
				
				for (NodeLabel label : nodeLabel) {
					//We set id to null to be regenerated by the sgbd
					label.setIdentifier(null);
					label.setThesaurusArray(thesaurusArrayDAO.getById(arrayId));
					updatedLabels.add(nodeLabelDAO.update(label));
				}
			}
		}
		return updatedLabels;
	}
	
	/**
	 * This method stores all the array of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored arrays
	 */
	public List<ThesaurusArray> storeArrays(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusArray> updatedArrays = new ArrayList<ThesaurusArray>();
		for (ThesaurusArray array : exportedThesaurus.getConceptArrays()) {
			array.setThesaurus(exportedThesaurus.getThesaurus());
			updatedArrays.add(thesaurusArrayDAO.update(array));
		}
		return updatedArrays;
	}
	
	/**
	 * This method stores all the groups of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored groups
	 */
	public List<ThesaurusConceptGroup> storeGroups(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusConceptGroup> updatedGroups = new ArrayList<ThesaurusConceptGroup>();
		for (ThesaurusConceptGroup group : exportedThesaurus.getConceptGroups()) {
			group.setThesaurus(exportedThesaurus.getThesaurus());
			updatedGroups.add(thesaurusConceptGroupDAO.update(group));
		}
		return updatedGroups;
	}
	
	/**
	 * This method stores all the group labels of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored group labels
	 */
	public List<ThesaurusConceptGroupLabel> storeGroupLabels(GincoExportedThesaurus exportedThesaurus) {
		Map<String, JaxbList<ThesaurusConceptGroupLabel>> labels = exportedThesaurus.getConceptGroupLabels();
		List<ThesaurusConceptGroupLabel> updatedLabels = new ArrayList<ThesaurusConceptGroupLabel>();
		
		if (labels != null && !labels.isEmpty()) {
			Iterator<Map.Entry<String,  JaxbList<ThesaurusConceptGroupLabel>>> entries = labels.entrySet().iterator();
			String groupId = null;
			List<ThesaurusConceptGroupLabel> groupLabels = null;
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<ThesaurusConceptGroupLabel>> entry = entries.next();
				//Getting the id of the group
				groupId = entry.getKey();
				
				//Getting the label for this group
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					groupLabels = entry.getValue().getList();
				}
				
				for (ThesaurusConceptGroupLabel label : groupLabels) {
					//We set id to null to be regenerated by the sgbd
					label.setIdentifier(null);
					label.setConceptGroup(thesaurusConceptGroupDAO.getById(groupId));
					updatedLabels.add(thesaurusConceptGroupLabelDAO.update(label));
				}
			}
		}
		return updatedLabels;
	}
	
	/**
	 * This method stores all the hierarchical relationships of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of the concepts which parents were updated
	 */
	public List<ThesaurusConcept> storeHierarchicalRelationship(GincoExportedThesaurus exportedThesaurus) {
		Map<String, JaxbList<String>> relations = exportedThesaurus.getHierarchicalRelationship();
		List<ThesaurusConcept> updatedConcepts = new ArrayList<ThesaurusConcept>();
		
		if (relations != null && !relations.isEmpty()) {
			Iterator<Map.Entry<String, JaxbList<String>>> entries = relations.entrySet().iterator();
			String childId = null;
			List<String> parentIds = null;
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<String>> entry = entries.next();
				//Getting the id of the child
				childId = entry.getKey();
				
				//Getting the ids of its parents
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					parentIds = entry.getValue().getList();				
				}
				
				//We get all the parent concepts of the childconcept
				ThesaurusConcept childConcept = thesaurusConceptDAO.getById(childId);
				Set<ThesaurusConcept> parentConcepts = new HashSet<ThesaurusConcept>();
				for (String id : parentIds) {
					parentConcepts.add(thesaurusConceptDAO.getById(id));
				}
				childConcept.setParentConcepts(parentConcepts);
				updatedConcepts.add(thesaurusConceptDAO.update(childConcept));
			}
		}
		
		//Processing and setting for all concepts their root concept
		for (ThesaurusConcept thesaurusConcept : updatedConcepts) {
			List<ThesaurusConcept> roots = thesaurusConceptService.getRootConcepts(thesaurusConcept);
			thesaurusConcept.setRootConcepts(new HashSet<ThesaurusConcept>(roots));
			thesaurusConceptDAO.update(thesaurusConcept);
		}
		
		return updatedConcepts;
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
				updatedConcepts.add(thesaurusConceptDAO.update(saveAssociativeRelationship(thesaurusConceptDAO.getById(conceptId), associatedConceptIds)));
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
			if (linkedThesaurusConcept.getStatus() != ConceptStatusEnum.VALIDATED
					.getStatus()) {
				throw new BusinessException(
						"A concept must associate a validated concept",
						"concept-associate-validated-concept");
			}
			List<String> alreadyAssociatedConcepts = associativeRelationshipDAO
					.getAssociatedConcepts(linkedThesaurusConcept);
			if (!alreadyAssociatedConcepts.contains(concept.getIdentifier())) {
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
	
	/**
	 * This method stores all the term notes of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored notes
	 */
	public List<Note> storeTermNotes(GincoExportedThesaurus exportedThesaurus) {
		Map<String, JaxbList<Note>> termNotes = exportedThesaurus.getTermNotes();
		List<Note> result = new ArrayList<Note>();
		if (termNotes != null && !termNotes.isEmpty()) {
			Iterator<Map.Entry<String,  JaxbList<Note>>> entries = termNotes.entrySet().iterator();
			String termId = null;
			List<Note> notes = null;
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<Note>> entry = entries.next();
				//Getting the id of the term
				termId = entry.getKey();
				
				//Getting the ids of the notes
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					notes = entry.getValue().getList();
				}
				
				for (Note note : notes) {
					note.setTerm(thesaurusTermDAO.getById(termId));
					result.add(noteDAO.update(note));
				}
			}
		}
		return result;
	}
	
	/**
	 * This method stores all the concept notes of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored notes
	 */
	public List<Note> storeConceptNotes(GincoExportedThesaurus exportedThesaurus) {
		Map<String, JaxbList<Note>> conceptNotes = exportedThesaurus.getConceptNotes();
		List<Note> result = new ArrayList<Note>();
		if (conceptNotes != null && !conceptNotes.isEmpty()) {
			Iterator<Map.Entry<String,  JaxbList<Note>>> entries = conceptNotes.entrySet().iterator();
			String conceptId = null;
			List<Note> notes = null;
			while(entries.hasNext()){
				Map.Entry<String,  JaxbList<Note>> entry = entries.next();
				//Getting the id of the concept
				conceptId = entry.getKey();
				
				//Getting the ids of the notes
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					notes = entry.getValue().getList();
				}
				
				for (Note note : notes) {
					note.setConcept(thesaurusConceptDAO.getById(conceptId));
					result.add(noteDAO.update(note));
				}
			}
		}
		return result;
	}
}
