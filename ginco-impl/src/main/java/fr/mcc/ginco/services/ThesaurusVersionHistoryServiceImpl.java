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


import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.enums.ThesaurusVersionStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.utils.DateUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly=true, rollbackFor = BusinessException.class)
@Service("thesaurusVersionHistoryService")
public class ThesaurusVersionHistoryServiceImpl implements IThesaurusVersionHistoryService {

    @Inject
    @Named("thesaurusVersionHistoryDAO")
    private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;

    @Inject
    @Named("generatorService")
    private IIDGeneratorService generatorService;

    @Log
    private Logger logger;

	@Override
	public List<ThesaurusVersionHistory> getVersionsByThesaurusId(
			String thesaurusId) {
		return thesaurusVersionHistoryDAO.findVersionsByThesaurusId(thesaurusId);
	}

	@Override
	public ThesaurusVersionHistory getThesaurusVersionHistoryById(String id) {
		return thesaurusVersionHistoryDAO.getById(id);
	}

    @Override
    public Boolean hasPublishedVersion(Thesaurus thesaurus) {
        for(ThesaurusVersionHistory version : thesaurusVersionHistoryDAO.findVersionsByThesaurusId(thesaurus.getIdentifier())) {
            if(version.getStatus() == ThesaurusVersionStatusEnum.PUBLISHED.getStatus()) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly=false)
    public ThesaurusVersionHistory publishThesaurus(Thesaurus thesaurus, String userId) {
        ThesaurusVersionHistory newVersion = new ThesaurusVersionHistory();
        newVersion.setThesaurus(thesaurus);
        newVersion.setThisVersion(true);
        newVersion.setDate(DateUtil.nowDate());
        newVersion.setStatus(ThesaurusVersionStatusEnum.PUBLISHED.getStatus());
        newVersion.setUserId(userId);
        newVersion.setIdentifier(generatorService.generate(ThesaurusVersionHistory.class));
        return createOrUpdateVersion(newVersion);
    }

    @Override
	@Transactional(readOnly=false)
	public ThesaurusVersionHistory createOrUpdateVersion(ThesaurusVersionHistory version) {
		if (version.getThisVersion() == true) {
			//We set attribute thisVersion for all versions to false
			List<ThesaurusVersionHistory> allOtherVersions = new ArrayList<ThesaurusVersionHistory>();
			if (version.getThesaurus() != null) {
				allOtherVersions = thesaurusVersionHistoryDAO.findAllOtherThisVersionTrueByThesaurusId(version.getThesaurus().getIdentifier(), version.getIdentifier());				
			}
			for (ThesaurusVersionHistory thesaurusVersionHistory : allOtherVersions) {
				thesaurusVersionHistory.setThisVersion(false);
			}
		} else {
			//We verify there is at least one version to true for the flag thisVersion (thisVersion)
			if ( thesaurusVersionHistoryDAO.findThisVersionByThesaurusId(version.getThesaurus().getIdentifier()) == null) {
				throw new BusinessException("A version must be set as the current version", "no-current-version");
			}
		}
		return thesaurusVersionHistoryDAO.update(version);
	}
}