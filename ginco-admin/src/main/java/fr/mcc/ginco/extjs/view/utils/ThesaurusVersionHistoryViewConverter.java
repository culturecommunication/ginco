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
package fr.mcc.ginco.extjs.view.utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusVersionHistoryView;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusVersionHistoryService;

/**
 * Small class responsible for converting real {@link ThesaurusVersionHistory} object into
 * its view {@link ThesaurusVersionHistoryView} and vice-versa.
 */
@Component("thesaurusVersionHistoryViewConverter")
public class ThesaurusVersionHistoryViewConverter {

	@Inject
	@Named("thesaurusVersionHistoryService")
	private IThesaurusVersionHistoryService thesaurusVersionHistoryService;

	@Log
	private Logger logger;

	/**
	 * Main method used to do conversion from {@link ThesaurusVersionHistory} to {@link ThesaurusVersionHistoryView}.
	 * 
	 * @param source
	 *            source to work with
	 * @return converted item.
	 * @throws BusinessException
	 */
	public ThesaurusVersionHistoryView convert(ThesaurusVersionHistory source)
			throws BusinessException {
		ThesaurusVersionHistoryView convertedItem = new ThesaurusVersionHistoryView();
		convertedItem.setIdentifier(source.getIdentifier());
		if (source.getDate() != null) {
			convertedItem.setDate(source.getDate().toString());
		}
		convertedItem.setVersionNote(source.getVersionNote());
		convertedItem.setStatus(source.getStatus());
		convertedItem.setThisVersion(source.getThisVersion());
		if (source.getThesauruses() != null) {
			convertedItem.setThesaurusId(source.getThesauruses().getIdentifier());			
		}
		return convertedItem;
	}

	/**
	 * This method converts a list of {@link ThesaurusVersionHistory} in a list of {@link ThesaurusVersionHistoryView}.
	 * 
	 * @param A {@link ThesaurusVersionHistory} list.
	 * @return A {@link ThesaurusVersionHistoryView} list
	 * @throws BusinessException
	 */
	public List<ThesaurusVersionHistoryView> convertList(List<ThesaurusVersionHistory> thesaurusHistoryList) {
		List<ThesaurusVersionHistoryView> result = new ArrayList<ThesaurusVersionHistoryView>();
		for (ThesaurusVersionHistory thesaurusVersionHistory : thesaurusHistoryList) {
			result.add(convert(thesaurusVersionHistory));
		}
		
		return result;
	}
	
}
