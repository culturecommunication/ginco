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
package fr.mcc.ginco.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.AssociativeRelationshipRole;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IAssociativeRelationshipDAO;
import fr.mcc.ginco.dao.IAssociativeRelationshipRoleDAO;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.enums.ConceptHierarchicalRelationsEnum;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.utils.LabelUtil;
import fr.mcc.ginco.utils.ThesaurusTermUtils;

/**
 * Implementation of the thesaurus concept service. Contains methods relatives
 * to the ThesaurusConcept object
 */
@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("thesaurusConceptService")
public class ThesaurusConceptServiceImpl implements IThesaurusConceptService {

	@Log
	private Logger logger;

	@Inject
	@Named("thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

	@Inject
	@Named("thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;
	
	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Inject
	@Named("thesaurusArrayDAO")
	private IThesaurusArrayDAO thesaurusArrayDAO;

	@Value("${ginco.default.language}")
	private String defaultLang;

	@Inject
	@Named("associativeRelationshipDAO")
	private IAssociativeRelationshipDAO associativeRelationshipDAO;
	
	@Inject
	@Named("conceptHierarchicalRelationshipServiceUtil")
	private IConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;

	@Inject
	@Named("associativeRelationshipRoleDAO")
	private IAssociativeRelationshipRoleDAO associativeRelationshipRoleDAO;

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mcc.ginco.IThesaurusConceptService#getThesaurusConceptList()
	 */
	@Override
	public List<ThesaurusConcept> getThesaurusConceptList() {
		return thesaurusConceptDAO.findAll();
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.services.IThesaurusConceptService#getThesaurusConceptList(java.util.List)
	 */
	@Override
	public Set<ThesaurusConcept> getThesaurusConceptList(List<String> ids)
			throws BusinessException {
		Set<ThesaurusConcept> result = new HashSet<ThesaurusConcept>();
		for (String id : ids) {
			ThesaurusConcept concept = thesaurusConceptDAO.getById(id);
			if (concept == null) {
				throw new BusinessException("The concept " + id
						+ " does not exist!", "concept-does-not-exist");
			} else {
				if (!result.contains(concept)) {
					result.add(concept);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.IThesaurusConceptService#getThesaurusConceptById(java.lang
	 * .String)
	 */
	@Override
	public ThesaurusConcept getThesaurusConceptById(String id) {
		return thesaurusConceptDAO.getById(id);
	}

	@Override
	public List<ThesaurusConcept> getOrphanThesaurusConcepts(String thesaurusId)
			throws BusinessException {
		Thesaurus thesaurus = checkThesaurusId(thesaurusId);
		return thesaurusConceptDAO.getOrphansThesaurusConcept(thesaurus);
	}

	@Override
	public long getOrphanThesaurusConceptsCount(String thesaurusId)
			throws BusinessException {
		Thesaurus thesaurus = checkThesaurusId(thesaurusId);
		return thesaurusConceptDAO.getOrphansThesaurusConceptCount(thesaurus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.IThesaurusConceptService#getTopTermThesaurusConcept(java
	 * .lang.String)
	 */
	@Override
	public List<ThesaurusConcept> getTopTermThesaurusConcepts(String thesaurusId)
			throws BusinessException {
		Thesaurus thesaurus = checkThesaurusId(thesaurusId);
		return thesaurusConceptDAO.getTopTermThesaurusConcept(thesaurus);
	}

	@Override
	public long getTopTermThesaurusConceptsCount(String thesaurusId)
			throws BusinessException {
		Thesaurus thesaurus = checkThesaurusId(thesaurusId);
		return thesaurusConceptDAO.getTopTermThesaurusConceptCount(thesaurus);
	}

	@Override
	public List<ThesaurusConcept> getChildrenByConceptId(String conceptId) {
		return thesaurusConceptDAO.getChildrenConcepts(conceptId);
	}

	@Override
	public List<ThesaurusConcept> getConceptsByThesaurusId(
			String excludeConceptId, String thesaurusId, Boolean searchOrphans,
			Boolean onlyValidatedConcepts) {
		return thesaurusConceptDAO.getAllConceptsByThesaurusId(
				excludeConceptId, thesaurusId, searchOrphans,
				onlyValidatedConcepts);
	}

	@Override
	public boolean hasChildren(String conceptId) {
		return (thesaurusConceptDAO.getChildrenConcepts(conceptId).size() > 0);
	}

	@Override
	public ThesaurusTerm getConceptPreferredTerm(String conceptId)
			throws BusinessException {

		logger.debug("ConceptId : " + conceptId);

		ThesaurusTerm preferredTerm = thesaurusTermDAO
				.getConceptPreferredTerm(conceptId);
		if (preferredTerm == null) {
			throw new BusinessException("The concept " + conceptId
					+ "has no preferred term",
					"concept-does-not-have-a-preferred-term");
		}
		return preferredTerm;
	}
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.services.IThesaurusConceptService#getConceptPreferredTerm(java.lang.String, java.lang.String)
	 */
	@Override
	public ThesaurusTerm getConceptPreferredTerm(String conceptId, String languageId){
		logger.debug("ConceptId : " + conceptId);
		ThesaurusTerm preferredTerm = thesaurusTermDAO
				.getConceptPreferredTerm(conceptId, languageId);		
		return preferredTerm;
	}


	@Override
	public String getConceptLabel(String conceptId) throws BusinessException {
		ThesaurusTerm term = getConceptPreferredTerm(conceptId);
		return LabelUtil.getLocalizedLabel(term.getLexicalValue(),
				term.getLanguage(), defaultLang);
	}	
	
	@Transactional(readOnly = false)
	@Override
	public ThesaurusConcept updateThesaurusConcept(ThesaurusConcept object,
			List<ThesaurusTerm> terms, List<AssociativeRelationship> associatedConcepts, List<ConceptHierarchicalRelationship> hierarchicalRelationships, List<ThesaurusConcept>childrenConceptToDetach)
			throws BusinessException {
		
		ThesaurusTermUtils.checkTerms(terms);
		
		if (StringUtils.isNotEmpty(object.getIdentifier())) {
			List<ThesaurusTerm> existingTerms = thesaurusTermDAO.findTermsByConceptId(object.getIdentifier());
			for (ThesaurusTerm existingTerm : existingTerms) {
	            if (!terms.contains(existingTerm)) {
	                ThesaurusTerm term = thesaurusTermDAO.getById(existingTerm.getIdentifier());
	                term.setConcept(null);
	                thesaurusTermDAO.update(term);
	                logger.info("Marking Term with ID " + existingTerm.getIdentifier() + " as SandBoxed.");
	            }
	        }					
		} else {
			object.setIdentifier(generatorService.generate(ThesaurusConcept.class));
		}

		if (object.getStatus() == ConceptStatusEnum.CANDIDATE.getStatus()) {
			// We can set status = candidate only if concept has not relation
			// (both hierarchical or associative)
			if (!associatedConcepts.isEmpty()
					// || !object.getAssociativeRelationshipRight().isEmpty()
					|| !object.getParentConcepts().isEmpty()
					|| hasChildren(object.getIdentifier())) {
				throw new BusinessException(
						"A concept must not have a status candidate when it still have relations",
						"concept-status-candidate-relation");
			}
		}

		if (object.getStatus() == ConceptStatusEnum.VALIDATED.getStatus()) {
			// Test if parent concepts are validated
			Set<ThesaurusConcept> parents = object.getParentConcepts();
			for (ThesaurusConcept parent : parents) {
				if (parent.getStatus() != ConceptStatusEnum.VALIDATED
						.getStatus()) {
					throw new BusinessException(
							"A concept cannot have a parent which status is not validated",
							"concept-parent-not-validated");
				}
			}

		}
		object = saveAssociativeRelationship(object, associatedConcepts);
		object = conceptHierarchicalRelationshipServiceUtil.saveHierarchicalRelationship(object, hierarchicalRelationships, childrenConceptToDetach);

		ThesaurusConcept concept = thesaurusConceptDAO.update(object);
		updateConceptTerms(concept, terms);
		// indexerService.addConcept(concept);
		return concept;
	}
		

	private ThesaurusConcept saveAssociativeRelationship(
			ThesaurusConcept concept, List<AssociativeRelationship> associatedConcepts)
			throws BusinessException {
		Set<AssociativeRelationship> relations = new HashSet<AssociativeRelationship>();
		if (concept.getAssociativeRelationshipLeft() == null) {
			concept.setAssociativeRelationshipLeft(new HashSet<AssociativeRelationship>());
		} else {
			for (AssociativeRelationship associativeRelationship : concept
					.getAssociativeRelationshipLeft()) {
				associativeRelationshipDAO.delete(associativeRelationship);
			}
		}
		concept.getAssociativeRelationshipLeft().clear();

		if (concept.getAssociativeRelationshipRight() == null) {
			concept.setAssociativeRelationshipRight(new HashSet<AssociativeRelationship>());
		} else {
			for (AssociativeRelationship associativeRelationship : concept
					.getAssociativeRelationshipRight()) {
				associativeRelationshipDAO.delete(associativeRelationship);
			}
		}
		concept.getAssociativeRelationshipRight().clear();

		for (AssociativeRelationship association : associatedConcepts) {
			logger.debug("Settings associated concept " + association);

            if(association.getIdentifier().getConcept1() == association.getIdentifier().getConcept2()) {
                throw new BusinessException("It's not possible to make association to itself!",
                        "association-to-itself-error");
            }

            ThesaurusConcept linkedThesaurusConcept = association.getConceptRight();
			if (linkedThesaurusConcept.getStatus() != ConceptStatusEnum.VALIDATED
					.getStatus()) {
				throw new BusinessException(
						"A concept must associate a validated concept",
						"concept-associate-validated-concept");
			}
			List<String> alreadyAssociatedConcepts = associativeRelationshipDAO
					.getAssociatedConcepts(linkedThesaurusConcept);
			if (!alreadyAssociatedConcepts.contains(concept.getIdentifier())) {
                relations.add(association);
                if(association.getRelationshipRole() == null) {
                    association.setRelationshipRole(new AssociativeRelationshipRole());
                }
                if(association.getRelationshipRole().getCode() == null
                        || association.getRelationshipRole().getCode() == "") {
                   association.getRelationshipRole()
                           .setCode(associativeRelationshipRoleDAO.getDefaultAssociativeRelationshipRole().getCode());
                }
                associativeRelationshipDAO.update(association);
			}
		}
		concept.getAssociativeRelationshipLeft().addAll(relations);

		return concept;
	}

	@Transactional(readOnly = false)
	@Override
	public ThesaurusConcept destroyThesaurusConcept(ThesaurusConcept object)
			throws BusinessException {
		List<ThesaurusTerm> terms = thesaurusTermDAO
				.findTermsByConceptId(object.getIdentifier());
		for (ThesaurusTerm term : terms) {
			term.setConcept(null);
			thesaurusTermDAO.update(term);
		}

		List<ThesaurusConcept> childrenConcepts = getChildrenByConceptId(object
				.getIdentifier());
		for (ThesaurusConcept childConcept : childrenConcepts) {
			childConcept.getParentConcepts().remove(object);
			thesaurusConceptDAO.update(childConcept);
		}

		List<ThesaurusConcept> rootChildrenConcepts = thesaurusConceptDAO
				.getAllRootChildren(object);
		for (ThesaurusConcept rootChild : rootChildrenConcepts) {
			rootChild.getRootConcepts().remove(object);
			thesaurusConceptDAO.update(rootChild);
		}

		List<ThesaurusArray> arrays = thesaurusArrayDAO
				.getConceptSuperOrdinateArrays(object.getIdentifier());
		for (ThesaurusArray array : arrays) {
			thesaurusArrayDAO.delete(array);
		}

		return thesaurusConceptDAO.delete(object);
	}

	private void updateConceptTerms(ThesaurusConcept concept,
			List<ThesaurusTerm> terms) throws BusinessException {
		List<ThesaurusTerm> returnTerms = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm thesaurusTerm : terms) {

			// We always set validated status to the terms that are joined to a
			// concept
			thesaurusTerm.setStatus(TermStatusEnum.VALIDATED.getStatus());
			thesaurusTerm.setConcept(concept);
			returnTerms.add(thesaurusTermDAO.update(thesaurusTerm));

		}
	}

	private Thesaurus checkThesaurusId(String thesaurusId)
			throws BusinessException {
		Thesaurus thesaurus = thesaurusDAO.getById(thesaurusId);
		if (thesaurus == null) {
			throw new BusinessException("Invalid thesaurusId : " + thesaurusId,
					"invalid-thesaurus-id");
		} else {
			logger.debug("thesaurus with id =  " + thesaurusId + " found");

		}
		return thesaurus;
	}

	@Async
	public void calculateChildrenRoot(String parentId) {
		logger.info("root concept calculation launched for parent concept id = "
				+ parentId);
		conceptHierarchicalRelationshipServiceUtil.calculateChildrenRoots(parentId, parentId);
		logger.info("root concept calculation ended for parent concept id = "
				+ parentId);
	}


	@Override
	public List<ThesaurusConcept> getAllConcepts() {
		return thesaurusConceptDAO.findAll();
	}

	@Override
	public List<ThesaurusConcept> getAvailableConceptsOfArray(String arrayId,
			String thesaurusId) {
		ThesaurusArray currentArray = new ThesaurusArray();
		List<ThesaurusConcept> returnAvailableConcepts = new ArrayList<ThesaurusConcept>();

		if (arrayId != null && !"".equals(arrayId)) {
			currentArray = thesaurusArrayDAO.getById(arrayId);
		}

		if (arrayId != null && !"".equals(arrayId)
				&& currentArray.getSuperOrdinateConcept() != null) {
			// We get all arrays matching our superordinate, excluding our
			// concept from the list
			List<ThesaurusArray> arrayWithSameSuperOrdinate = thesaurusArrayDAO
					.getConceptSuperOrdinateArrays(currentArray
							.getSuperOrdinateConcept().getIdentifier(),
							currentArray.getIdentifier());
			returnAvailableConcepts = thesaurusConceptDAO
					.getChildrenConcepts(currentArray.getSuperOrdinateConcept()
							.getIdentifier());

			for (ThesaurusArray thesaurusArray : arrayWithSameSuperOrdinate) {
				Set<ThesaurusConcept> conceptOfEachArray = thesaurusArray
						.getConcepts();
				for (ThesaurusConcept thesaurusConcept : conceptOfEachArray) {
					returnAvailableConcepts.remove(thesaurusConcept);
				}
			}
		} else {
			// The array is null or has no superordinate : we get all topterm
			// concepts which are not already in other arrays
			List<ThesaurusArray> arrayWithNoSuperOrdinate = thesaurusArrayDAO
					.getConceptSuperOrdinateArrays(null);
			returnAvailableConcepts = thesaurusConceptDAO
					.getTopTermThesaurusConcept(thesaurusDAO
							.getById(thesaurusId));
			for (ThesaurusArray thesaurusArray : arrayWithNoSuperOrdinate) {
				Set<ThesaurusConcept> conceptOfEachArrayWithoutSuperordinate = thesaurusArray
						.getConcepts();
				for (ThesaurusConcept thesaurusConcept : conceptOfEachArrayWithoutSuperordinate) {
					returnAvailableConcepts.remove(thesaurusConcept);
				}
			}

		}
		return returnAvailableConcepts;
	}

	@Override
	public String getConceptTitle(ThesaurusConcept concept) {
		return getConceptPreferredTerm(concept.getIdentifier())
				.getLexicalValue();
	}

	@Override
	public String getConceptTitleLanguage(ThesaurusConcept concept) {
		return getConceptPreferredTerm(concept.getIdentifier()).getLanguage()
				.getPart1();
	}

	@Override
	public int getConceptsHierarchicalRelations(String firstConceptId,
			String secondConceptId) throws BusinessException {
		ThesaurusConcept firstConcept = thesaurusConceptDAO
				.getById(firstConceptId);
		ThesaurusConcept secondConcept = thesaurusConceptDAO
				.getById(secondConceptId);

		if (firstConcept != null && secondConcept != null) {
			if (thesaurusConceptDAO.getChildrenConcepts(firstConceptId)
					.contains(secondConcept)) {
				return ConceptHierarchicalRelationsEnum.PARENT.getStatus();
			}

			if (thesaurusConceptDAO.getChildrenConcepts(secondConceptId)
					.contains(firstConcept)) {
				return ConceptHierarchicalRelationsEnum.CHILD.getStatus();
			}

			else
				return ConceptHierarchicalRelationsEnum.NORELATIONS.getStatus();
		} else {
			throw new BusinessException("One or both concepts don't exist",
					"concepts-do-not-exist");
		}

	}

	@Override
	public List<ThesaurusTerm> getConceptNotPreferredTerms(String conceptId)
			throws BusinessException {
		List<ThesaurusTerm> notPreferredTerms = thesaurusTermDAO
				.getConceptNotPreferredTerms(conceptId);
		return notPreferredTerms;
    }

	@Override
	public int getStatusByConceptId(String conceptId) throws BusinessException {
		ThesaurusConcept thesaurusConcept = thesaurusConceptDAO
				.getById(conceptId);
		if (thesaurusConcept != null)
			return thesaurusConcept.getStatus();
		else
			throw new BusinessException("Concept with identifier " + conceptId
					+ " does not exist", "concepts-does-not-exist");
	}

	@Override
	public List<ThesaurusTerm> getConceptPreferredTerms(String conceptId)
			throws BusinessException {
		logger.debug("ConceptId : " + conceptId);

		List<ThesaurusTerm> preferredTerms = thesaurusTermDAO
				.getConceptPreferredTerms(conceptId);
		if (preferredTerms == null || preferredTerms.size()==0) {
			throw new BusinessException("The concept " + conceptId
					+ "has no preferred term",
					"concept-does-not-have-a-preferred-term");
		}
		return preferredTerms;
	}
}
