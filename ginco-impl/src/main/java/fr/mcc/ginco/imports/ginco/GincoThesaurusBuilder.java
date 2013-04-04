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
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IAssociativeRelationshipDAO;
import fr.mcc.ginco.dao.IAssociativeRelationshipRoleDAO;
import fr.mcc.ginco.dao.INodeLabelDAO;
import fr.mcc.ginco.dao.INoteDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.log.Log;

/**
 * This class extracts data from a {@link GincoExportedThesaurus} object
 * and stores it in beans.
 * It returns a {@link Thesaurus} object corresponding to the imported thesaurus
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
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;
	
	@Inject
	@Named("thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;
	
	@Inject
	@Named("thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;
	
	@Log
	private Logger logger;
	
	public Thesaurus storeGincoExportedThesaurus(GincoExportedThesaurus exportedThesaurus) {
		Thesaurus thesaurus = thesaurusDAO.update(exportedThesaurus.getThesaurus());
		storeConcepts(exportedThesaurus);
		storeTerms(exportedThesaurus);
		//storeArraysAndLabels(exportedThesaurus);
		storeVersions(exportedThesaurus);
		storeHierarchicalRelationship(exportedThesaurus);
		storeAssociativeRelationship(exportedThesaurus);
		storeTermNotes(exportedThesaurus);
		storeConceptNotes(exportedThesaurus);
		return thesaurus;
	}
	
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
	
	public List<ThesaurusTerm> storeTerms(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusTerm> updatedTerms = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm term : exportedThesaurus.getTerms()) {
			term.setThesaurus(exportedThesaurus.getThesaurus());
			updatedTerms.add(thesaurusTermDAO.update(term));
		}
		return updatedTerms;
	}
	
	public List<NodeLabel> storeArraysAndLabels(GincoExportedThesaurus exportedThesaurus) {
		List<NodeLabel> updatedLabelsAndArrays = new ArrayList<NodeLabel>();
		for (NodeLabel node : exportedThesaurus.getConceptsArrayLabels()) {
			node.getThesaurusArray().setThesaurus(exportedThesaurus.getThesaurus());
			updatedLabelsAndArrays.add(nodeLabelDAO.update(node));
		}
		return updatedLabelsAndArrays;
	}
	
	public List<ThesaurusVersionHistory> storeVersions(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusVersionHistory> updatedVersion = new ArrayList<ThesaurusVersionHistory>();
		for (ThesaurusVersionHistory version : exportedThesaurus.getThesaurusVersions()) {
			version.setThesaurus(exportedThesaurus.getThesaurus());
			updatedVersion.add(thesaurusVersionHistoryDAO.update(version));
		}
		return updatedVersion;
	}
	
	public List<ThesaurusConcept> storeHierarchicalRelationship(GincoExportedThesaurus exportedThesaurus) {
		Map<String, JaxbList<String>> relations = exportedThesaurus.getHierarchicalRelationship();
		List<ThesaurusConcept> updatedConcepts = new ArrayList<ThesaurusConcept>();
		
		if (relations != null && !relations.isEmpty()) {
			Iterator<Map.Entry<String,  JaxbList<String>>> entries = relations.entrySet().iterator();
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
		return updatedConcepts;
	}
	
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
