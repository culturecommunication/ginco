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

package fr.mcc.ginco.soap;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.data.ReducedThesaurusConcept;
import fr.mcc.ginco.data.ReducedThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.IAssociativeRelationshipService;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipServiceUtil;
import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import org.codehaus.plexus.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * This class is the implementation of all SOAP services related to concept objects
 */

@WebService(endpointInterface = "fr.mcc.ginco.soap.ISOAPThesaurusConceptService")
public class SOAPThesaurusConceptServiceImpl implements ISOAPThesaurusConceptService {

	public static final String CONCEPT_IDENTIFIER_IS_EMPTY = "Concept identifier is empty";
	public static final String EMPTY_PARAMETER = "empty-parameter";
	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("associativeRelationshipService")
	private IAssociativeRelationshipService associativeRelationshipService;

	@Inject
	@Named("conceptHierarchicalRelationshipServiceUtil")
	private IConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("noteService")
	private INoteService noteService;

	@Override
	public int getConceptsHierarchicalRelations(String firstConceptId, String secondConceptId) {
		if (StringUtils.isNotEmpty(firstConceptId) && StringUtils.isNotEmpty(secondConceptId)) {
			return thesaurusConceptService.getConceptsHierarchicalRelations(firstConceptId, secondConceptId);
		} else {
			throw new BusinessException("One or more parameters are empty", EMPTY_PARAMETER);
		}
	}

	@Override
	public List<ReducedThesaurusTerm> getPreferredTermByConceptId(String conceptId, Boolean withNotes) {
		if (StringUtils.isNotEmpty(conceptId)) {
			List<ReducedThesaurusTerm> results = new ArrayList<ReducedThesaurusTerm>();
			List<ThesaurusTerm> thesaurusTerms = thesaurusConceptService.getConceptPreferredTerms(conceptId);

			for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
				ReducedThesaurusTerm reducedThesaurusTerm = ReducedThesaurusTerm.getReducedThesaurusTerm(thesaurusTerm);
				if (withNotes!=null && withNotes==true) {
					addNotesToTerm(reducedThesaurusTerm);
				}
				results.add(reducedThesaurusTerm);
			}
			Collections.sort(results, new Comparator<ReducedThesaurusTerm>() {
				Collator frCollator = Collator.getInstance(Locale.FRENCH);
	
				public int compare(ReducedThesaurusTerm t1, ReducedThesaurusTerm t2) {
					return frCollator.compare(t1.getLexicalValue(), t2.getLexicalValue());
				}
			});
			return results;
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}
	
	private void addNotesToTerm (ReducedThesaurusTerm rterm) {
		List<Note> notes= noteService.getTermNotePaginatedList(rterm.getIdentifier(), 0, 1000);
		rterm.setNotes(notes);
	}
	
	private void addNotesToConcept (ReducedThesaurusConcept rconcept) {
		List<Note> notes= noteService.getConceptNotePaginatedList(rconcept.getIdentifier(), 0, 1000);
		rconcept.setNotes(notes);
	}
	
	private void addAssociatesToConcept (ReducedThesaurusConcept rconcept) {
		ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(rconcept.getIdentifier());
		if (thesaurusConcept != null) {
			rconcept.setParents(new ArrayList<ReducedThesaurusConcept>());
			rconcept.setAssociates(new ArrayList<ReducedThesaurusConcept>());
			for (ThesaurusConcept parentConcept : thesaurusConcept.getParentConcepts())
			{
				rconcept.getParents().add(ReducedThesaurusConcept.getReducedThesaurusConcept(parentConcept));
			}
			
			for (String associatedId : associativeRelationshipService.getAssociatedConceptsId(thesaurusConcept))
			{
				ReducedThesaurusConcept associated = new ReducedThesaurusConcept();
				associated.setIdentifier(associatedId);
				rconcept.getAssociates().add(associated);
			}
			
		}
	}

	@Override
	public List<ReducedThesaurusTerm> getConceptNotPreferredTerms(String conceptId, Boolean withNotes) {
		if (StringUtils.isNotEmpty(conceptId)) {
			List<ReducedThesaurusTerm> results = new ArrayList<ReducedThesaurusTerm>();
			List<ThesaurusTerm> thesaurusTerms = thesaurusConceptService.getConceptNotPreferredTerms(conceptId);
			for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
				ReducedThesaurusTerm reducedThesaurusTerm = ReducedThesaurusTerm.getReducedThesaurusTerm(thesaurusTerm);
				if (withNotes!=null && withNotes==true) {
					addNotesToTerm(reducedThesaurusTerm);
				}
				results.add(reducedThesaurusTerm);
			}
			Collections.sort(results, new Comparator<ReducedThesaurusTerm>() {
				Collator frCollator = Collator.getInstance(Locale.FRENCH);
	
				public int compare(ReducedThesaurusTerm t1, ReducedThesaurusTerm t2) {
					return frCollator.compare(t1.getLexicalValue(), t2.getLexicalValue());
				}
			});
			
			return results;
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}

	@Override
	public int getStatusByConceptId(String conceptId) {
		if (StringUtils.isNotEmpty(conceptId)) {
			return thesaurusConceptService.getStatusByConceptId(conceptId);
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}

	@Override
	public List<String> getChildrenByConceptId(String conceptId, ConceptStatusEnum status) {
		if (StringUtils.isNotEmpty(conceptId)) {
			List<String> results = new ArrayList<String>();
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null) {
				List<ThesaurusConcept> thesaurusConceptList = thesaurusConceptService.getChildrenByConceptId(conceptId,0 ,null, status);
				for (ThesaurusConcept conceptChild : thesaurusConceptList) {
					results.add(conceptChild.getIdentifier());
				}
				return results;
			} else {
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}
	
	@Override
	public List<ReducedThesaurusConcept> getChildrenByConceptId_v2(String conceptId, ConceptStatusEnum status,
			Boolean withAssociates,
			Boolean withNotes) {
		if (StringUtils.isNotEmpty(conceptId)) {
			List<ReducedThesaurusConcept> results = new ArrayList<ReducedThesaurusConcept>();
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null) {
				List<ThesaurusConcept> thesaurusConceptList = thesaurusConceptService.getChildrenByConceptId(conceptId,0 ,null, status);
				for (ThesaurusConcept conceptChild : thesaurusConceptList) {
					ReducedThesaurusConcept rChildConcept = ReducedThesaurusConcept.getReducedThesaurusConcept(conceptChild);
					if (withNotes!=null && withNotes==true) {
						addNotesToConcept(rChildConcept);
					}
					if (withAssociates != null && withAssociates == true)
					{
						addAssociatesToConcept(rChildConcept);
					}
					results.add(rChildConcept);
				}
				return results;
			} else {
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}

	@Override
	public List<String> getRootConcepts(String conceptId, ConceptStatusEnum status) {
		if (StringUtils.isNotEmpty(conceptId)) {
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null) {
				List<String> results = new ArrayList<String>();
				List<ThesaurusConcept> thesaurusConceptList = conceptHierarchicalRelationshipServiceUtil.getRootConcepts(thesaurusConcept);
				for (ThesaurusConcept conceptChild : thesaurusConceptList) {
					if (status != null) {
						if (conceptChild.getStatus().intValue()==status.getStatus()) {
							results.add(conceptChild.getIdentifier());
						}
					} else 
					results.add(conceptChild.getIdentifier());
				}
				return results;
			} else {
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}
	
	@Override
	public List<ReducedThesaurusConcept> getRootConcepts_v2(String conceptId, ConceptStatusEnum status,
			Boolean withAssociates,
			Boolean withNotes) {
		if (StringUtils.isNotEmpty(conceptId)) {
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null) {
				List<ReducedThesaurusConcept> results = new ArrayList<ReducedThesaurusConcept>();
				List<ThesaurusConcept> thesaurusConceptList = conceptHierarchicalRelationshipServiceUtil.getRootConcepts(thesaurusConcept);
				for (ThesaurusConcept rootConcept : thesaurusConceptList) {
					ReducedThesaurusConcept rRootConcept = ReducedThesaurusConcept.getReducedThesaurusConcept(rootConcept);
					if (withNotes!=null && withNotes==true) {
						addNotesToConcept(rRootConcept);
					}
					if (withAssociates != null && withAssociates == true)
					{
						addAssociatesToConcept(rRootConcept);
					}
					if (status != null) {
						if (rootConcept.getStatus().intValue()==status.getStatus()) {
							results.add(rRootConcept);
						}
					} else 
					results.add(rRootConcept);
				}
				return results;
			} else {
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}

	@Override
	public List<String> getParentConcepts(String conceptId, ConceptStatusEnum status) {
		if (StringUtils.isNotEmpty(conceptId)) {
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null) {
				List<String> results = new ArrayList<String>();
				Set<ThesaurusConcept> parentConceptList = thesaurusConcept.getParentConcepts();
				for (ThesaurusConcept parentConcept : parentConceptList) {
					if (status != null)
					{
						if (parentConcept.getStatus().intValue()==status.getStatus()) {
							results.add(parentConcept.getIdentifier());
						}
					} else
					results.add(parentConcept.getIdentifier());
				}
				return results;
			} else {
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}
	
	@Override
	public List<ReducedThesaurusConcept> getParentConcepts_v2(String conceptId, ConceptStatusEnum status,
			Boolean withAssociates,Boolean withNotes) {
		if (StringUtils.isNotEmpty(conceptId)) {
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null) {
				List<ReducedThesaurusConcept> results = new ArrayList<ReducedThesaurusConcept>();
				Set<ThesaurusConcept> parentConceptList = thesaurusConcept.getParentConcepts();
				for (ThesaurusConcept parentConcept : parentConceptList) {
					ReducedThesaurusConcept rParentConcept = ReducedThesaurusConcept.getReducedThesaurusConcept(parentConcept);
					if (withNotes!=null && withNotes==true) {
						addNotesToConcept(rParentConcept);
					}
					if (withAssociates != null && withAssociates == true)
					{
						addAssociatesToConcept(rParentConcept);
					}
					if (status != null)
					{
						if (parentConcept.getStatus().intValue()==status.getStatus()) {
							results.add(rParentConcept);
						}
					} else
					results.add(rParentConcept);
				}
				return results;
			} else {
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}

	@Override
	public List<String> getAssociativeConcepts(String conceptId, ConceptStatusEnum status) {
		if (StringUtils.isNotEmpty(conceptId)) {
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null) {
				return associativeRelationshipService.getAssociatedConceptsId(thesaurusConcept, status);
			} else {
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		} else {
			throw new BusinessException(CONCEPT_IDENTIFIER_IS_EMPTY, EMPTY_PARAMETER);
		}
	}

	@Override
	public List<String> getTopConceptsByThesaurusId(String thesaurusId, ConceptStatusEnum status) {
		if (StringUtils.isNotEmpty(thesaurusId)) {
			Thesaurus thesaurus = thesaurusService.getThesaurusById(thesaurusId);
			if (thesaurus != null) {
				List<String> results = new ArrayList<String>();
				List<ThesaurusConcept> topConceptList = thesaurusConceptService.getTopTermThesaurusConcepts(thesaurusId, status, 0);
				for (ThesaurusConcept topConcept : topConceptList) {
					results.add(topConcept.getIdentifier());
				}
				return results;
			} else {
				throw new BusinessException("Thesaurus with identifier " + thesaurusId + " does not exist", "thesaurus-does-not-exist");
			}
		} else {
			throw new BusinessException("Thesaurus identifier is empty", EMPTY_PARAMETER);
		}
	}

	@Override
	public List<ReducedThesaurusConcept> getTopConceptsByThesaurusId_v2(String thesaurusId, ConceptStatusEnum status,
			Boolean withAssociates,
			Boolean withNotes) {
		if (StringUtils.isNotEmpty(thesaurusId)) {
			Thesaurus thesaurus = thesaurusService.getThesaurusById(thesaurusId);
			if (thesaurus != null) {
				List<ReducedThesaurusConcept> results = new ArrayList<ReducedThesaurusConcept>();
				List<ThesaurusConcept> topConceptList = thesaurusConceptService.getTopTermThesaurusConcepts(thesaurusId, status, 0);
				for (ThesaurusConcept topConcept : topConceptList) {
					ReducedThesaurusConcept rTopConcept = ReducedThesaurusConcept.getReducedThesaurusConcept(topConcept);
					if (withNotes!=null && withNotes==true) {
						addNotesToConcept(rTopConcept);
					}
					if (withAssociates != null && withAssociates == true)
					{
						addAssociatesToConcept(rTopConcept);
					}
					if (status != null)
					{
						if (topConcept.getStatus().intValue()==status.getStatus()) {
							results.add(rTopConcept);
						}
					} else
						results.add(rTopConcept);
				}
				return results;
			} else {
				throw new BusinessException("Thesaurus with identifier " + thesaurusId + " does not exist", "thesaurus-does-not-exist");
			}
		} else {
			throw new BusinessException("Thesaurus identifier is empty", EMPTY_PARAMETER);
		}
	}



}
