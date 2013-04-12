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
package fr.mcc.ginco.audit.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.envers.AuditReader;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.audit.RevisionLine;
import fr.mcc.ginco.audit.RevisionLineBuilder;
import fr.mcc.ginco.audit.csv.JournalEventsEnum;
import fr.mcc.ginco.audit.readers.AuditHelper;
import fr.mcc.ginco.beans.GincoRevEntity;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.services.IThesaurusConceptService;

/**
 * Implementation of {@link RevisionLineBuilder} for Lexic/Mistral command file
 * export
 * 
 */
@Service("commandLineBuilder")
public class CommandLineBuilder implements RevisionLineBuilder {	
	
	@Inject
	@Named("auditHelper")
	private AuditHelper auditHelper;


	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.RevisionLineBuilder#buildLineBase(fr.mcc.ginco.audit.csv.JournalEventsEnum, fr.mcc.ginco.beans.GincoRevEntity)
	 */
	@Override
	public CommandLine buildLineBase(JournalEventsEnum event,
			GincoRevEntity gincoRevEntity) {
		CommandLine journal = new CommandLine();
		journal.setEventDate(gincoRevEntity.getRevisionDate());
		return journal;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.RevisionLineBuilder#buildTermAddedLine(java.lang.Object[])
	 */
	@Override
	public List<RevisionLine> buildTermAddedLine(Object[] revisionData) {
		List<RevisionLine> allLines = new ArrayList<RevisionLine>();

		CommandLine commandLine = buildLineBase(
				JournalEventsEnum.THESAURUSTERM_CREATED,
				(GincoRevEntity) revisionData[1]);
		ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
		commandLine.setValue(term.getLexicalValue());
		allLines.add(commandLine);
		if (term.getPrefered() != null && term.getPrefered()) {
			CommandLine commandLinepref = buildLineBase(
					JournalEventsEnum.THESAURUSTERM_ROLE_UPDATE,
					(GincoRevEntity) revisionData[1]);
			commandLinepref.setValue(CommandLine.STARS + term.getLexicalValue());
			allLines.add(commandLinepref);
		}
		return allLines;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.RevisionLineBuilder#buildTermRoleChangedLine(java.lang.Object[])
	 */
	@Override
	public RevisionLine buildTermRoleChangedLine(Object[] revisionData) {
		CommandLine commandLine = buildLineBase(
				JournalEventsEnum.THESAURUSTERM_ROLE_UPDATE,
				(GincoRevEntity) revisionData[1]);
		ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
		if (term.getPrefered()) {
			commandLine.setValue(CommandLine.STARS + term.getLexicalValue());
		} else {
			commandLine.setValue(CommandLine.UNPREFERRERD
					+ term.getLexicalValue());

		}
		return commandLine;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.RevisionLineBuilder#buildTermLexicalValueChangedLine(java.lang.Object[], java.lang.String)
	 */
	@Override
	public List<RevisionLine> buildTermLexicalValueChangedLine(
			Object[] revisionData, String oldLexicalValue) {
		List<RevisionLine> allLines = new ArrayList<RevisionLine>();
		allLines.addAll(buildTermAddedLine(revisionData));

		CommandLine commandLine = buildLineBase(
				JournalEventsEnum.THESAURUSTERM_DELETED,
				(GincoRevEntity) revisionData[1]);
		commandLine.setValue(CommandLine.REMOVED + oldLexicalValue);

		return allLines;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.RevisionLineBuilder#buildTermAttachmentChangedLine(java.lang.Object[])
	 */
	@Override
	public List<RevisionLine> buildTermAttachmentChangedLine(
			Object[] revisionData, ThesaurusTerm preferredTerm) {
		List<RevisionLine> allLines = new ArrayList<RevisionLine>();
		ThesaurusTerm term = (ThesaurusTerm) revisionData[0];
		if (term.getConcept() != null && !term.getPrefered()) {
			if (preferredTerm != null) {
				CommandLine journal = buildLineBase(
						JournalEventsEnum.THESAURUSTERM_LINKED_TO_CONCEPT,
						(GincoRevEntity) revisionData[1]);
				journal.setValue(term.getLexicalValue() + CommandLine.SYNONYM
						+ preferredTerm.getLexicalValue());
				allLines.add(journal);
			}
		} else {
			CommandLine journal = buildLineBase(
					JournalEventsEnum.THESAURUSTERM_LINKED_TO_CONCEPT,
					(GincoRevEntity) revisionData[1]);
			journal.setValue(CommandLine.SEPARATE + term.getLexicalValue());
		}
		return allLines;
	}

	/* (non-Javadoc)
	 * @see fr.mcc.ginco.audit.RevisionLineBuilder#buildConceptHierarchyChanged(java.lang.Object[], java.util.Set, java.lang.String)
	 */
	@Override
	public List<RevisionLine> buildConceptHierarchyChanged(
			Object[] revisionData, Set<String> oldGenericConceptIds,
			String languageId, AuditReader reader) {
		List<RevisionLine> allLines = new ArrayList<RevisionLine>();

		ThesaurusConcept concept = (ThesaurusConcept) revisionData[0];
		Set<ThesaurusConcept> parentConcepts = concept.getParentConcepts();
		Set<String> parentConceptIds = new HashSet<String>();
		for (ThesaurusConcept parentConcept : parentConcepts) {
			parentConceptIds.add(parentConcept.getIdentifier());
		}
		
		/*ThesaurusTerm currentConceptPrefTerm = thesaurusConceptService
				.getConceptPreferredTerm(
						concept.getIdentifier(),
						languageId);*/
		ThesaurusTerm currentConceptPrefTerm = auditHelper.getPreferredTermAtRevision(
				reader, 
				((GincoRevEntity)revisionData[1]).getId(), concept.getIdentifier(), languageId);
		
		for (String oldGenericConceptId : oldGenericConceptIds) {
			if (!parentConceptIds.contains(oldGenericConceptId)) {
				CommandLine commandLineDeletedHierarchy = buildLineBase(
						JournalEventsEnum.THESAURUSCONCEPT_HIERARCHY_UPDATE,
						(GincoRevEntity) revisionData[1]);				
				
				/*ThesaurusTerm oldGenericConceptPrefTerm = thesaurusConceptService
						.getConceptPreferredTerm(
								oldGenericConceptId,
								languageId);*/
				ThesaurusTerm oldGenericConceptPrefTerm =auditHelper.getPreferredTermAtRevision(
						reader, 
						((GincoRevEntity)revisionData[1]).getId(), oldGenericConceptId, languageId);
				
				if(currentConceptPrefTerm != null && oldGenericConceptPrefTerm != null) {
				commandLineDeletedHierarchy
						.setValue(CommandLine.HIERARCHY_REMOVED
								+currentConceptPrefTerm.getLexicalValue()
								+ CommandLine.HIERARCHY
								+ oldGenericConceptPrefTerm
										.getLexicalValue());
				allLines.add(commandLineDeletedHierarchy);
				}

			}
		}
		
		CommandLine commandLineNewRelations = buildLineBase(
				JournalEventsEnum.THESAURUSCONCEPT_HIERARCHY_UPDATE,
				(GincoRevEntity) revisionData[1]);
		List<String> newParentLexicalValues = new ArrayList<String>();
		for (String parentConceptId : parentConceptIds) {
			if (!oldGenericConceptIds.contains(parentConceptId)) {
				/*ThesaurusTerm preferredTerm = thesaurusConceptService
						.getConceptPreferredTerm(parentConceptId, languageId);*/
				ThesaurusTerm preferredTerm =auditHelper.getPreferredTermAtRevision(
						reader, 
						((GincoRevEntity)revisionData[1]).getId(), parentConceptId, languageId);
				
				
				if (preferredTerm != null) {
					newParentLexicalValues.add(preferredTerm.getLexicalValue());
				}
			}
		}
		if (newParentLexicalValues.size() > 0) {
			String val = currentConceptPrefTerm.getLexicalValue()
					+ CommandLine.HIERARCHY;
			Iterator<String> it = newParentLexicalValues.iterator();
			while (it.hasNext()) {
				val += it.next();
				if (it.hasNext()) {
					val += CommandLine.COMMA;
				}
			}
			commandLineNewRelations.setValue(val);
			allLines.add(commandLineNewRelations);
		}
		

		return allLines;
	}
}
