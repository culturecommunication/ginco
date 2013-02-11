package fr.mcc.ginco;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.IThesaurusVersionHistoryService;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IGenericDAO;

@Transactional
@Service("thesaurusVersionHistoryService")
public class ThesaurusVersionHistoryServiceImpl implements
                IThesaurusVersionHistoryService {

        @Inject
        @Named("thesaurusVersionHistoryDAO")
    private IGenericDAO<ThesaurusVersionHistory, String> thesaurusVersionHistoryDAO;


}