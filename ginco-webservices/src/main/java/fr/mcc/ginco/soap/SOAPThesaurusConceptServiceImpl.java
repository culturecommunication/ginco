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

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.data.ReducedThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.IAssociativeRelationshipService;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipServiceUtil;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import org.codehaus.plexus.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is the implementation of all SOAP services related to concept objects
 * 
 */

@WebService(endpointInterface="fr.mcc.ginco.soap.ISOAPThesaurusConceptService")
public class SOAPThesaurusConceptServiceImpl implements ISOAPThesaurusConceptService{
	
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
	
	@Override
	public int getConceptsHierarchicalRelations(String firstConceptId, String secondConceptId) 
			throws BusinessException{
		if (StringUtils.isNotEmpty(firstConceptId) && StringUtils.isNotEmpty(secondConceptId)){
			return thesaurusConceptService.getConceptsHierarchicalRelations(firstConceptId, secondConceptId);
		}
		else 
		{
			throw new BusinessException("One or more parameters are empty","empty-parameters");
		}
	}
	
	@Override
	public List<ReducedThesaurusTerm> getPreferredTermByConceptId(String conceptId)
			throws BusinessException{
		if (StringUtils.isNotEmpty(conceptId)){			
			List<ReducedThesaurusTerm> results = new ArrayList<ReducedThesaurusTerm>();
			List<ThesaurusTerm> thesaurusTerms = thesaurusConceptService.getConceptPreferredTerms(conceptId);
			
			for (ThesaurusTerm thesaurusTerm : thesaurusTerms)
				results.add(this.conversionThesaurusTermInReduced(thesaurusTerm));	
			return results;
		}
		else 
		{
			throw new BusinessException("Concept identifier is empty","empty-parameter");
		}
	}
	
	@Override
	public List<ReducedThesaurusTerm> getConceptNotPreferredTerms (String conceptId)
			throws BusinessException {
		if (StringUtils.isNotEmpty(conceptId)) {
			List<ReducedThesaurusTerm> results = new ArrayList<ReducedThesaurusTerm>();
			List<ThesaurusTerm> thesaurusTerms = thesaurusConceptService.getConceptNotPreferredTerms(conceptId);
			for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
				results.add(this.conversionThesaurusTermInReduced(thesaurusTerm));
            }
			return results;
		} else {
			throw new BusinessException("Concept identifier is empty","empty-parameter");
		}
	}
	
	private ReducedThesaurusTerm conversionThesaurusTermInReduced(ThesaurusTerm thesaurusTerm){
		ReducedThesaurusTerm reducedThesaurusTerm = new ReducedThesaurusTerm();
		reducedThesaurusTerm.setIdentifier(thesaurusTerm.getIdentifier());
		reducedThesaurusTerm.setLexicalValue(thesaurusTerm.getLexicalValue());
		reducedThesaurusTerm.setLanguageId(thesaurusTerm.getLanguage().getId());
		return reducedThesaurusTerm;
	}
	
	@Override
	public int getStatusByConceptId(String conceptId) throws BusinessException {
		if (StringUtils.isNotEmpty(conceptId)) {
			return thesaurusConceptService.getStatusByConceptId(conceptId);
		} else {
			throw new BusinessException("Concept identifier is empty","empty-parameter");
		}
	}
	
	@Override
	public List<String> getChildrenByConceptId(String conceptId) throws BusinessException{
		if (StringUtils.isNotEmpty(conceptId)){
			List<String> results = new ArrayList<String>();
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null){
				List<ThesaurusConcept> thesaurusConceptList = thesaurusConceptService.getChildrenByConceptId(conceptId);
				for (ThesaurusConcept conceptChild : thesaurusConceptList){
					results.add(conceptChild.getIdentifier());
				}
				return results;
			}
			else{
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		}
		else 
		{
			throw new BusinessException("Concept identifier is empty","empty-parameter");
		}
	}
	
	@Override
	public List<String> getRootConcepts(String conceptId) throws BusinessException{
		if (StringUtils.isNotEmpty(conceptId)){
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null){
				List<String> results = new ArrayList<String>();
				List<ThesaurusConcept> thesaurusConceptList = conceptHierarchicalRelationshipServiceUtil.getRootConcepts(thesaurusConcept);
				for (ThesaurusConcept conceptChild : thesaurusConceptList){
					results.add(conceptChild.getIdentifier());
				}
				return results;
			}
			else{
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		}
		else{
			throw new BusinessException("Concept identifier is empty","empty-parameter");
		}
	}
	
	@Override
	public List<String> getParentConcepts(String conceptId) throws BusinessException{
		if (StringUtils.isNotEmpty(conceptId)){
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null){
				List<String> results = new ArrayList<String>();
				Set<ThesaurusConcept> parentConceptList = thesaurusConcept.getParentConcepts();
				for (ThesaurusConcept parentConcept : parentConceptList){
					results.add(parentConcept.getIdentifier());
				}
				return results;
			}
			else{
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		}
		else{
			throw new BusinessException("Concept identifier is empty","empty-parameter");
		}
	}
	
	@Override
	public List<String> getAssociativeConcepts(String conceptId) throws BusinessException{
		if (StringUtils.isNotEmpty(conceptId)){
			ThesaurusConcept thesaurusConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
			if (thesaurusConcept != null){
				 return associativeRelationshipService.getAssociatedConceptsId(thesaurusConcept);
			}
			else{
				throw new BusinessException("Concept with identifier " + conceptId + " does not exist", "concept-does-not-exist");
			}
		}
		else{
			throw new BusinessException("Concept identifier is empty","empty-parameter");
		}
	}
	
	public List<String> getTopConceptsByThesaurusId(String thesaurusId) throws BusinessException{
		if (StringUtils.isNotEmpty(thesaurusId)){
			Thesaurus thesaurus = thesaurusService.getThesaurusById(thesaurusId);
			if (thesaurus != null){
				List<String> results = new ArrayList<String>();
				List<ThesaurusConcept> topConceptList = thesaurusConceptService.getTopTermThesaurusConcepts(thesaurusId);
				for (ThesaurusConcept topConcept : topConceptList){
					results.add(topConcept.getIdentifier());
				}
				return results;
			}
			else{
				throw new BusinessException("Thesaurus with identifier " + thesaurusId + " does not exist", "thesaurus-does-not-exist");
			}
		}
		else{
			throw new BusinessException("Thesaurus identifier is empty","empty-parameter");
		}
	}
}
