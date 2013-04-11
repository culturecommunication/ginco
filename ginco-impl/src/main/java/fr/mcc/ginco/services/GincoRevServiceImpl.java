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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.audit.RevisionExportTypesEnum;
import fr.mcc.ginco.audit.RevisionLine;
import fr.mcc.ginco.audit.RevisionLineBuilderFactory;
import fr.mcc.ginco.audit.csv.AuditCSVWriter;
import fr.mcc.ginco.audit.readers.ThesaurusAuditReader;
import fr.mcc.ginco.audit.readers.ThesaurusConceptAuditReader;
import fr.mcc.ginco.audit.readers.ThesaurusTermAuditReader;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.utils.DateUtil;

@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("gincoRevService")
public class GincoRevServiceImpl implements IGincoRevService {
	@Log
	private Logger logger;

	@Inject
	@Named("gincoSessionFactory")
	private SessionFactory sessionFactory;

	@Inject
	@Named("thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;

	@Inject
	@Named("auditCSVWriter")
	private AuditCSVWriter auditCSVWriter;

	@Inject
	@Named("thesaurusConceptAuditReader")
	ThesaurusConceptAuditReader conceptAuditReader;

	@Inject
	@Named("thesaurusTermAuditReader")
	ThesaurusTermAuditReader termAuditReader;

	@Inject
	@Named("thesaurusAuditReader")
	ThesaurusAuditReader thesaurusAuditReader;

	@Inject
	@Named("revisionLineBuilderFactory")
	RevisionLineBuilderFactory revisionLineBuilderFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.services.IGincoRevService#getLogJournal(fr.mcc.ginco.beans
	 * .Thesaurus)
	 */
	@Override
	public File getLogJournal(Thesaurus thesaurus) {
		File res;
		try {
			res = File.createTempFile("pattern", ".suffix");

			res.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(res));
			auditCSVWriter.writeHeader(out);
			termAuditReader.setRevisionLineBuilder(revisionLineBuilderFactory
					.getRevisionLineBuilder(RevisionExportTypesEnum.CSV));

			ThesaurusVersionHistory lastPublishedVersion = thesaurusVersionHistoryDAO
					.getLastPublishedVersionByThesaurusId(thesaurus
							.getIdentifier());
			Date startDate = thesaurus.getCreated();
			if (lastPublishedVersion != null) {
				startDate = lastPublishedVersion.getDate();
			}
			logger.debug("Generating audit logs from "
					+ DateUtil.toString(startDate) + " for thesaurus "
					+ thesaurus.getIdentifier());

			List<RevisionLine> allEvents = new ArrayList<RevisionLine>();

			AuditReader reader = AuditReaderFactory.get(sessionFactory
					.getCurrentSession());

			allEvents.addAll(thesaurusAuditReader.getThesaurusAdded(reader,
					thesaurus, startDate));

			allEvents.addAll(termAuditReader.getTermAdded(reader, thesaurus,
					startDate));

			allEvents.addAll(termAuditReader.getTermRoleChanged(reader,
					thesaurus, startDate));

			allEvents.addAll(termAuditReader.getTermLexicalValueChanged(reader,
					thesaurus, startDate));

			allEvents.addAll(termAuditReader.getTermAttachmentChanged(reader,
					thesaurus, startDate));

			allEvents.addAll(conceptAuditReader.getConceptAdded(reader,
					thesaurus, startDate));

			allEvents.addAll(conceptAuditReader.getConceptHierarchyChanged(
					reader, thesaurus, startDate));

			allEvents.addAll(conceptAuditReader.getConceptStatusChanged(reader,
					thesaurus, startDate));

			Collections.sort(allEvents);

			for (RevisionLine line : allEvents) {
				auditCSVWriter.writeJournalLine(line, out);
			}

			out.flush();
			out.close();
			return res;

		} catch (IOException e) {
			throw new TechnicalException("Error writing audit log file", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.mcc.ginco.services.IGincoRevService#getRevisions(fr.mcc.ginco.beans
	 * .Thesaurus, long, fr.mcc.ginco.beans.Language)
	 */
	@Override
	public File getRevisions(Thesaurus thesaurus, long timestamp,
			Language language) throws IOException {
		File res;
		try {
			res = File.createTempFile("pattern", ".suffix");

			res.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(res));
			termAuditReader.setRevisionLineBuilder(revisionLineBuilderFactory
					.getRevisionLineBuilder(RevisionExportTypesEnum.COMMANDS));
			conceptAuditReader.setRevisionLineBuilder(revisionLineBuilderFactory
					.getRevisionLineBuilder(RevisionExportTypesEnum.COMMANDS));

			Date startDate = new Date(timestamp);
			logger.debug("Generating command logs from "
					+ DateUtil.toString(startDate) + " for thesaurus "
					+ thesaurus.getIdentifier());

			AuditReader reader = AuditReaderFactory.get(sessionFactory
					.getCurrentSession());

			List<RevisionLine> allEvents = new ArrayList<RevisionLine>();

			//Insert isolated term
			allEvents.addAll(termAuditReader.getTermAdded(reader, thesaurus,
					startDate, language));
			
			//Declare a synonym as prefered
			allEvents.addAll(termAuditReader.getTermRoleChanged(reader,
					thesaurus, startDate, language));

			//delete a term + Insert isolated term
			allEvents.addAll(termAuditReader.getTermLexicalValueChanged(reader,
					thesaurus, startDate, language));

			//Insert synonym
			allEvents.addAll(termAuditReader.getTermAttachmentChanged(reader,
					thesaurus, startDate, language));	

			//Declare/delete a hierarchical relationship
			allEvents.addAll(conceptAuditReader.getConceptHierarchyChanged(
					reader, thesaurus, startDate, language));

			Collections.sort(allEvents);

			for (RevisionLine line : allEvents) {
				out.write(line.toString());
				out.newLine();
			}

			out.flush();
			out.close();
			return res;
		} catch (IOException e) {
			throw new TechnicalException("Error writing audit log file", e);
		}
	}

}
