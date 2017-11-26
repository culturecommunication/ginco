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
package fr.mcc.ginco.ark;

import fr.mcc.ginco.beans.ThesaurusArk;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.services.IThesaurusArkService;
import fr.mcc.ginco.utils.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

/**
 * Custom generator of primary keys for Hibernate,
 * should be changed in later revisions to generate real
 * ARK-based ids.
 */
@Transactional(readOnly = false, rollbackFor = TechnicalException.class)
@Service("generatorService")
public class CustomGeneratorService implements IIDGeneratorService {

	@Value("${application.ark.nma}")
	private String nma;
	@Value("${application.ark.naan}")
	private String naan;
	@Value("${application.ark.enabled}")
	private boolean useArk = true;

	@Inject
	@Named("thesaurusArkService")
	private IThesaurusArkService thesaurusArkService;

	@Override
	public String generate(Class entity) {
		String arkId;
		UUID nq = UUID.randomUUID();
		arkId = nma + ((useArk)?"/ark:/" + naan + "/":"/") + nq.toString();

		ThesaurusArk arkHistory = new ThesaurusArk();
		arkHistory.setCreated(DateUtil.nowDate());
		arkHistory.setEntity(entity.getSimpleName());
		arkHistory.setIdentifier(arkId);
		thesaurusArkService.createThesaurusArk(arkHistory);

		return arkId;
	}
}
