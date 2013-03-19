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
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.services;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.dao.IGenericDAO.SortingTypes;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.utils.LanguageComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the thesaurus service Contains methods relatives to the
 * Thesaurus object
 */
@Transactional(readOnly=true, rollbackFor = BusinessException.class)
@Service("thesaurusService")
public class ThesaurusServiceImpl implements IThesaurusService {

	@Value("${ginco.default.language}")
	private String defaultLang;

	@Inject
	@Named("thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

    @Inject
    @Named("thesaurusTermDAO")
    private IThesaurusTermDAO thesaurusTermDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mcc.ginco.IThesaurusService#getThesaurusById(java.lang.String)
	 */
    @Override
	public Thesaurus getThesaurusById(String id) {
		return thesaurusDAO.getById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mcc.ginco.IThesaurusService#getThesaurusList()
	 */
	@Override
	public List<Thesaurus> getThesaurusList() {
		return thesaurusDAO.findAll("title", SortingTypes.asc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.IThesaurusService#updateThesaurus(fr.mcc.ginco.beans.Thesaurus
	 * , fr.mcc.ginco.beans.users.IUser)
	 */
	@Transactional(readOnly=false)
	@Override
	public Thesaurus updateThesaurus(Thesaurus object) throws BusinessException {
		return thesaurusDAO.update(object);
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.IThesaurusService#getThesaurusLanguages(java.lang.String)
	 */
	@Override
	public List<Language> getThesaurusLanguages(String thesaurusId)
			throws BusinessException {
		Thesaurus th = thesaurusDAO.getById(thesaurusId);
		if (th == null) {
			throw new BusinessException("Invalid thesaurusId : " + thesaurusId, "invalid-thesaurus-id");
		}
		Set<Language> languages = th.getLang();
		List<Language> orderedLangs = new ArrayList<Language>();
		orderedLangs.addAll(languages);
		Collections.sort(orderedLangs, new LanguageComparator(defaultLang));
		Collections.reverse(orderedLangs);
		return orderedLangs;
	}

	@Transactional(readOnly=false)
    @Override
    public Thesaurus destroyThesaurus(Thesaurus object) throws BusinessException {
        return thesaurusDAO.delete(object);
    }

}