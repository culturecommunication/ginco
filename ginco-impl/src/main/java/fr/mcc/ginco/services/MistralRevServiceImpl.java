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
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.audit.commands.CommandLine;
import fr.mcc.ginco.audit.commands.HierarchyCommandBuilder;
import fr.mcc.ginco.audit.commands.SynonymsCommandBuilder;
import fr.mcc.ginco.audit.commands.TermCommandBuilder;
import fr.mcc.ginco.audit.utils.AuditQueryBuilder;
import fr.mcc.ginco.audit.utils.AuditReaderService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.utils.DateUtil;

@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("mistralRevService")
public class MistralRevServiceImpl implements IMistralRevService {
	
	private static Logger logger = LoggerFactory.getLogger(MistralRevServiceImpl.class);

	@Inject
	@Named("auditReaderService")
	private AuditReaderService readerService;

	@Inject
	@Named("gincoSessionFactory")
	private SessionFactory sessionFactory;

	@Inject
	@Named("hierarchyCommandBuilder")
	private HierarchyCommandBuilder hierarchyCommandBuilder;

	@Inject
	@Named("termCommandBuilder")
	private TermCommandBuilder termCommandBuilder;

	@Inject
	@Named("synonymsCommandBuilder")
	private SynonymsCommandBuilder synonymsCommandBuilder;

	@Inject
	@Named("auditQueryBuilder")
	private AuditQueryBuilder auditQueryBuilder;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Override
	public File getRevisions(Thesaurus thesaurus, long timestamp,
			Language language) throws IOException {
		File res;
		try {
			res = File.createTempFile("pattern", ".suffix");

			res.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(res));

			for (CommandLine line : getEventsByThesaurus(thesaurus.getIdentifier(), timestamp, language)){
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

	@Override
	public File getAllRevisions(long timestamp,
			Language language) throws IOException {
		File res;

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("SELECT DISTINCT THESAURUSID FROM REVINFO WHERE REVTSTMP > :pdate");
		query.setLong("pdate", timestamp);
		List<String> allThesaurusId = query.list();

		try {
			res = File.createTempFile("pattern", ".suffix");

			res.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(res));

			for (String thesaurusId : allThesaurusId){
				if (thesaurusId != null && thesaurusService.getThesaurusList()
						.contains(thesaurusService.getThesaurusById(thesaurusId))){
					List<CommandLine> eventsByThesaurus = getEventsByThesaurus(thesaurusId, timestamp, language);
					if (!eventsByThesaurus.isEmpty()){
						out.write("-----------------------------------------");
						out.newLine();
						out.write(thesaurusService.getThesaurusById(thesaurusId).getTitle());
						out.newLine();
						out.write(thesaurusId);
						out.newLine();
						out.write("-----------------------------------------");
						out.newLine();
						for (CommandLine line : eventsByThesaurus){
							out.write(line.toString());
							out.newLine();
						}
						out.newLine();
					}
				}
			}
			out.flush();
			out.close();
			return res;
		} catch (IOException e) {
			throw new TechnicalException("Error writing audit log file", e);
		}
	}

	private List<CommandLine> getEventsByThesaurus(String thesaurusId, long timestamp,
			Language language) throws IOException{
		Date startDate;
		if (timestamp != 0) {
			startDate = new Date(timestamp);
		} else {
			List<Number> allThesaurusRevisions = readerService
					.getAuditReader().getRevisions(Thesaurus.class,
							thesaurusId);
			if (allThesaurusRevisions.size() > 0) {
				startDate = readerService.getAuditReader().getRevisionDate(
						allThesaurusRevisions.get(0));
			} else {
				throw new BusinessException("Unable to get start date",
						"revision-export-invalid-start-date");
			}

		}

		logger.debug("Generating command logs from "
				+ DateUtil.toString(startDate) + " for thesaurus "
				+ thesaurusId);

		List<CommandLine> allEvents = new ArrayList<CommandLine>();

		Number startRevision = readerService.getAuditReader()
				.getRevisionNumberForDate(startDate);
		Number endRevision = readerService.getAuditReader()
				.getRevisionNumberForDate(DateUtil.nowDate());

		AuditQuery startTermsQuery = auditQueryBuilder.getEntityAtRevision(
				ThesaurusTerm.class, startRevision,
				thesaurusId);
		auditQueryBuilder.addFilterOnLanguage(startTermsQuery, language.getId());


		List<ThesaurusTerm> oldTerms = startTermsQuery.getResultList();
		List<ThesaurusTerm> oldTermsWithValidatedConcept = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm term : oldTerms){
			if (term.getConcept() != null && term.getConcept().getStatus() == ConceptStatusEnum.VALIDATED.getStatus()){
				oldTermsWithValidatedConcept.add(term);
			}
		}

		AuditQuery endTermsQuery = auditQueryBuilder
				.getEntityAtRevision(ThesaurusTerm.class, endRevision,
						thesaurusId);
		auditQueryBuilder.addFilterOnLanguage(endTermsQuery, language.getId());

		List<ThesaurusTerm> newTerms = endTermsQuery.getResultList();
		List<ThesaurusTerm> newTermsWithValidatedConcept = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm term : newTerms){
			if (term.getConcept() != null && term.getConcept().getStatus() == ConceptStatusEnum.VALIDATED.getStatus()){
				newTermsWithValidatedConcept.add(term);
			}
		}

		allEvents.addAll(termCommandBuilder.buildChangedTermsLines(oldTermsWithValidatedConcept,
				newTermsWithValidatedConcept));

		AuditQuery startConceptQuery = auditQueryBuilder
				.getEntityAtRevision(ThesaurusConcept.class, startRevision,
						thesaurusId);
		auditQueryBuilder.getFilterOnStatus(startConceptQuery, ConceptStatusEnum.VALIDATED.getStatus());

		List<ThesaurusConcept> previousConcepts = startConceptQuery
				.getResultList();

		AuditQuery currentConceptsQuery = auditQueryBuilder
				.getEntityAtRevision(ThesaurusConcept.class, endRevision,
						thesaurusId);
		auditQueryBuilder.getFilterOnStatus(currentConceptsQuery, ConceptStatusEnum.VALIDATED.getStatus());

		List<ThesaurusConcept> currentConcepts = currentConceptsQuery.getResultList();

		allEvents.addAll(synonymsCommandBuilder.buildSynonyms(
				previousConcepts, currentConcepts, startRevision,
				endRevision, language.getId()));
		allEvents.addAll(hierarchyCommandBuilder.buildHierarchyChanges(
				previousConcepts, currentConcepts, startRevision,
				endRevision, language.getId()));

		allEvents.addAll(termCommandBuilder.buildAddedPrefTermsLines(oldTermsWithValidatedConcept,
				newTermsWithValidatedConcept));

		allEvents.addAll(termCommandBuilder.buildDeletedTermsLines(oldTermsWithValidatedConcept,
				newTermsWithValidatedConcept));
		return allEvents;
	}
}
