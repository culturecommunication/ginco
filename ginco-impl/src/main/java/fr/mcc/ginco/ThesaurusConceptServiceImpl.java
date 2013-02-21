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
package fr.mcc.ginco;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import fr.mcc.ginco.dao.IThesaurusTermDAO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.users.IUser;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.journal.GincoLog;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.utils.LabelUtil;

/**
 * Implementation of the thesaurus concept service.
 * Contains methods relatives to the ThesaurusConcept object
 */
@Transactional
@Service("thesaurusConceptService")
public class ThesaurusConceptServiceImpl implements IThesaurusConceptService  {

	@Log
	private Logger logger;
	
    @Inject
    @Named("thesaurusConceptDAO")
    private IThesaurusConceptDAO thesaurusConceptDAO;
    
    @Inject
    @Named("thesaurusDAO")
    private IThesaurusDAO thesaurusDAO;

    @Inject
    @Named("thesaurusTermDAO")
    private IThesaurusTermDAO thesaurusTermDAO;

    @Value("${ginco.default.language}")
    private String defaultLang;


    /*
	 * (non-Javadoc)
	 *
	 * @see fr.mcc.ginco.IThesaurusConceptService#getThesaurusConceptList()
	 */
    @Override
    public List<ThesaurusConcept> getThesaurusConceptList() {
        return thesaurusConceptDAO.findAll();
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see fr.mcc.ginco.IThesaurusConceptService#getThesaurusConceptById(java.lang.String)
	 */
    @Override
    public ThesaurusConcept getThesaurusConceptById(String id) {
        return thesaurusConceptDAO.getById(id);
    }
    
    @Override
    public List<ThesaurusConcept> getOrphanThesaurusConcepts(String thesaurusId) throws BusinessException {
    	Thesaurus thesaurus = thesaurusDAO.getById(thesaurusId);
		if (thesaurus == null) {
			throw new BusinessException("Invalid thesaurusId : "
					+ thesaurusId);
		} else {
			logger.info("thesaurus found");

		}
    	return thesaurusConceptDAO.getOrphansThesaurusConcept(thesaurus);
    }
    
    @Override
    public long getOrphanThesaurusConceptsCount(String thesaurusId) throws BusinessException {
    	Thesaurus thesaurus = thesaurusDAO.getById(thesaurusId);
		if (thesaurus == null) {
			throw new BusinessException("Invalid thesaurusId : "
					+ thesaurusId);
		} else {
			logger.info("thesaurus found");

		}
    	return thesaurusConceptDAO.getOrphansThesaurusConceptCount(thesaurus);
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see fr.mcc.ginco.IThesaurusConceptService#getTopTermThesaurusConcept(java.lang.String)
	 */
    @Override
    public List<ThesaurusConcept> getTopTermThesaurusConcepts(String thesaurusId) throws BusinessException {
        Thesaurus thesaurus = thesaurusDAO.getById(thesaurusId);
        if (thesaurus == null) {
            throw new BusinessException("Invalid thesaurusId : "
                    + thesaurusId);
        } else {
            logger.info("thesaurus found");

        }
        return thesaurusConceptDAO.getTopTermThesaurusConcept(thesaurus);
    }   
    
    @Override
    public long getTopTermThesaurusConceptsCount(String thesaurusId) throws BusinessException {
        Thesaurus thesaurus = thesaurusDAO.getById(thesaurusId);
        if (thesaurus == null) {
            throw new BusinessException("Invalid thesaurusId : "
                    + thesaurusId);
        } else {
            logger.info("thesaurus found");

        }
        return thesaurusConceptDAO.getTopTermThesaurusConceptCount(thesaurus);
    }   
   

	@Override
	public ThesaurusTerm getConceptPreferredTerm(String conceptId)
			throws BusinessException {

        logger.debug("ConceptId : " + conceptId);

        ThesaurusTerm preferredTerm = thesaurusTermDAO
				.getConceptPreferredTerm(conceptId);
		if (preferredTerm == null) {
			throw new BusinessException("The concept " + conceptId
					+ "has no preferred term");
		}
		return preferredTerm;
	}


	@Override
	public String getConceptLabel(String conceptId) throws BusinessException {
		ThesaurusTerm term = getConceptPreferredTerm(conceptId);
		return LabelUtil.getConceptLabel(term, defaultLang);
	}

	
	@GincoLog(action = GincoLog.Action.CREATE, entityType=GincoLog.EntityType.THESAURUSCONCEPT)
	public ThesaurusConcept createThesaurusConcept(ThesaurusConcept object, IUser user) {
    	return thesaurusConceptDAO.update(object);
    }

}
