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

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly=true, rollbackFor = BusinessException.class)
@Service("thesaurusTermService")
public class ThesaurusTermServiceImpl implements IThesaurusTermService {

    @Inject
    @Named("thesaurusTermDAO")
    private IThesaurusTermDAO thesaurusTermDAO;

    @Log
    private Logger logger;

    @Override
    public ThesaurusTerm getThesaurusTermById(String id) throws BusinessException {
        ThesaurusTerm thesaurusTerm = thesaurusTermDAO.getById(id);
        if (thesaurusTerm != null) {
            return thesaurusTerm;
        } else {
            throw new BusinessException("Invalid termId requested : " + id, "invalid-term-id");
        }
    }

    @Override
    public List<ThesaurusTerm> getPaginatedThesaurusSandoxedTermsList(Integer startIndex,
                                                                      Integer limit, String idThesaurus) {
        return thesaurusTermDAO.findPaginatedSandboxedItems(startIndex, limit, idThesaurus);
    }


	@Override
    public List<ThesaurusTerm> getTermsByConceptId(String idConcept) throws BusinessException {
        return thesaurusTermDAO.findTermsByConceptId(idConcept);
    }

    @Override
    public void markTermsAsSandboxed(List<ThesaurusTerm> sent, List<ThesaurusTerm> origin) throws BusinessException {

        for (ThesaurusTerm old : origin) {
            if (!sent.contains(old)) {
                ThesaurusTerm term = thesaurusTermDAO.getById(old.getIdentifier());
                term.setConcept(null);
                thesaurusTermDAO.update(term);
                logger.info("Marking Term with ID " + old.getIdentifier() + " as SandBoxed.");
            }
        }
    }

	@Override
    public Long getSandboxedTermsCount(String idThesaurus) throws BusinessException {
        return thesaurusTermDAO.countSandboxedTerms(idThesaurus);
    }
    
	@Override
	public Long getSandboxedValidatedTermsCount(String idThesaurus)
			throws BusinessException {
		return thesaurusTermDAO.countSandboxedValidatedTerms(idThesaurus);
	}
    
	@Transactional(readOnly=false)
    @Override
    public ThesaurusTerm updateThesaurusTerm(ThesaurusTerm object) throws BusinessException {
    	if (object.getStatus() != TermStatusEnum.VALIDATED.getStatus() && object.getConcept() != null){
    		throw new BusinessException("The term is associated to a concept. The status must be set to validated", "term-selected-must-have-validated-status");
    	}
    	ThesaurusTerm updatedThesaurus = thesaurusTermDAO.update(object);
    	return updatedThesaurus;
    }

	@Transactional(readOnly=false)
    @Override
    public ThesaurusTerm destroyThesaurusTerm(ThesaurusTerm object) throws BusinessException {
        if (object.getConcept() == null && object.getStatus()==TermStatusEnum.REJECTED.getStatus()) {
            return thesaurusTermDAO.delete(object);
        } else {
            throw new BusinessException("It's not possible to delete a term attached to a concept or with a status different from rejected", "delete-attached-term");
        }
    }

	@Override
    public List<ThesaurusTerm> getPreferedTerms(List<ThesaurusTerm> listOfTerms) {
        List<ThesaurusTerm> preferedTerms = new ArrayList<ThesaurusTerm>();
        for (ThesaurusTerm thesaurusTerm : listOfTerms) {
            if (thesaurusTerm.getPrefered()) {
                preferedTerms.add(thesaurusTerm);
            }
        }
        return preferedTerms;
    }

    @Override
    public List<ThesaurusTerm> getAllTerms() {
        return thesaurusTermDAO.findAll();
    }

    @Override
	public List<ThesaurusTerm> getPaginatedThesaurusSandoxedValidatedTermsList(
			Integer startIndex, Integer limit, String idThesaurus) {
		return thesaurusTermDAO.findPaginatedSandboxedValidatedItems(startIndex, limit, idThesaurus);
	}
    
    @Override
    public String getConceptIdByTerm(String lexicalValue, String thesaurusId, String languageId) throws BusinessException{
    	ThesaurusTerm thesaurusTerm = thesaurusTermDAO
    			.getTermByLexicalValueThesaurusIdLanguageId(lexicalValue, thesaurusId, languageId);
    	if (thesaurusTerm != null){
    		ThesaurusConcept thesaurusConcept = thesaurusTerm.getConcept();
    		if (thesaurusConcept != null){
    			return thesaurusConcept.getIdentifier();
    		}
    		else{
    			throw new BusinessException("The concept does not exist", "concept-does-not-exist");
    		}
    	}
    	else{
    		throw new BusinessException("The term does not exist", "term-does-not-exist");
    	}
    		
    }
    @Override
    public ThesaurusTerm getPreferredTermByTerm(String lexicalValue, String thesaurusId,  String languageId) throws BusinessException{
    	ThesaurusTerm thesaurusTerm = thesaurusTermDAO.
    			getTermByLexicalValueThesaurusIdLanguageId(lexicalValue, thesaurusId, languageId);
    	if (thesaurusTerm != null){
    		if (thesaurusTerm.getPrefered())
    			return thesaurusTerm;
    		else{
    			ThesaurusConcept thesaurusConcept = thesaurusTerm.getConcept();
    			if (thesaurusConcept != null){
    				ThesaurusTerm preferredTerm = thesaurusTermDAO.getConceptPreferredTerm(thesaurusConcept.getIdentifier());
    				if (preferredTerm.getLanguage().getId() == thesaurusTerm.getLanguage().getId())
    					return preferredTerm;
    				else return null;
    			}
    			else{
    				throw new BusinessException("The concept does not exist", "concept-does-not-exist");
    			}
    		}		
    	}
    	else return null;
    }
    
    @Override
	public Boolean isPreferred(String lexicalValue, String thesaurusId,  String languageId) throws BusinessException{
		ThesaurusTerm thesaurusTerm = thesaurusTermDAO.
    			getTermByLexicalValueThesaurusIdLanguageId(lexicalValue, thesaurusId, languageId);
    	if (thesaurusTerm != null){
    		return thesaurusTerm.getPrefered();
    	}
    	else{
    		throw new BusinessException("The term does not exist", "term-does-not-exist");
    	}
    		
    	
	}

	@Override
	public List<ThesaurusTerm> getAllTerms(String thesaurusId) {
		return thesaurusTermDAO.findTermsByThesaurusId(thesaurusId);
	}
}