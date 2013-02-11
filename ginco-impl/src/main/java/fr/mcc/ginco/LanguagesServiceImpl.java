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

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.log.Log;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("languagesService")
public class LanguagesServiceImpl implements ILanguagesService {
	
	@Inject
	@Named("languagesDAO")
    private ILanguageDAO languagesDAO;
	
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.ILanguagesService#getLanguagesList()
	 */
	@Override
	public List<Language> getLanguagesList(Integer startIndex, Integer limit) {
		return languagesDAO.findPaginatedItems(startIndex, limit);
	}
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.ILanguagesService#getTopLanguagesList()
	 */
	@Override
	public List<Language> getTopLanguagesList() {
		return languagesDAO.findTopLanguages();
	}
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.ILanguagesService#getLanguageCount()
	 */
	public Long getLanguageCount(){
		return languagesDAO.count();
	}

    /* (non-Javadoc)
	 * @see fr.mcc.ginco.ILanguagesService#getLanguageById()
	 */
    @Override
    public Language getLanguageById(String id) {
        return languagesDAO.getById(id);
    }
}