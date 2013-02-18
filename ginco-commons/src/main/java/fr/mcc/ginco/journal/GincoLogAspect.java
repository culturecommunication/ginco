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
package fr.mcc.ginco.journal;

import javax.inject.Inject;
import javax.inject.Named;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;

import fr.mcc.ginco.ILogJournalService;
import fr.mcc.ginco.beans.IBaseBean;
import fr.mcc.ginco.beans.users.IUser;
import fr.mcc.ginco.log.Log;

/**
 * Aspect to insert a LogJournal entry on events
 */
@Aspect
public class GincoLogAspect {

	private static final String UNKNOWN_AUTHOR = "unknown";
	/**
	 * logger
	 */
	@Log
	private Logger log;

	/**
	 * Service class to manipualte LogJournal
	 */
	@Inject
	@Named("logJournalService")
	private ILogJournalService logJournalService;

	/**
	 * Processes the GincoLog Annotation to add a log journal entry
	 * 
	 * @param pjp
	 * @param gincoLog
	 *            the annotation
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(gincoLog)")
	public Object logAction(ProceedingJoinPoint pjp, GincoLog gincoLog) {
		Object res = null;
		try {
			res = pjp.proceed();

			log.debug("Into GincoLogAspect with action = "
					+ gincoLog.action().toString());
			String entityType = gincoLog.entityType().toString();
			String action = gincoLog.action().toString();
			String entityId = "";
			String author = UNKNOWN_AUTHOR;

			Object[] args = pjp.getArgs();
			for (Object arg : args) {
				if (arg instanceof IBaseBean) {
					entityId = ((IBaseBean) arg).getId();
				} else if (arg instanceof IUser) {
					author = ((IUser) arg).getName();
				}
			}
			log.debug("Saving new LogJournal entry =  {");
			log.debug("		action: " + gincoLog.action().toString());
			log.debug("		entityId: " + entityId);
			log.debug("		entityType: " + entityType);
			log.debug("		author: " + author);
			log.debug("}");
			if (!"".equals(entityId) && !"".equals(author)) {
				logJournalService.addLogJournalEntry(action, entityId,
						entityType, author);
			} else {
				log.warn("Some data are missing in order to process correctly the GincoLog annotation");
				log.warn("Datas are : {");
				log.warn("		action: " + gincoLog.action().toString());
				log.warn("		entityId: " + entityId);
				log.warn("		entityType: " + entityType);
				log.warn("		author: " + author);
				log.warn("}");
				log.warn("All data are mandatory");

			}

		} catch (Throwable t) {
			log.error("Error in GincoLog aspect", t);
		}
		return res;

	}
}
