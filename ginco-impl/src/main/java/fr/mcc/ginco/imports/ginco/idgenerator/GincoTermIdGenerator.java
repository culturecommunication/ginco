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
package fr.mcc.ginco.imports.ginco.idgenerator;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IThesaurusTermDAO;

/**
 * This class generate new ids for terms for importing branch in existing
 * thesaurus
 * 
 */
@Component("gincoTermIdGenerator")
public class GincoTermIdGenerator {

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;	

	@Inject
	private IThesaurusTermDAO thesaurusTermDAO;	

	private Logger logger  = LoggerFactory.getLogger(GincoTermIdGenerator.class);


	/**
	 * This method checks if the id of the term is not already present in the
	 * application, and replace it if it's the case. It also update the
	 * conceptid if it has changed
	 * 
	 * @param term
	 * @param idMapping
	 *            : the map where we store the mapping between old and new ids
	 * @return ThesaurusTerm updatedTerm
	 */
	public String getIdForTerm(String oldId, Map<String, String> idMapping) {
		if (thesaurusTermDAO.getById(oldId) != null) {
			String newId = generatorService.generate(ThesaurusTerm.class);
			idMapping.put(oldId, newId);
			logger.debug("Setting a new id for the ID" + oldId
					+ "with new id " + newId);
			return newId;
		}
		return oldId;
	}

}
