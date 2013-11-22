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
package fr.mcc.ginco.audit.csv;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import fr.mcc.ginco.utils.DateUtil;
import fr.mcc.ginco.utils.LabelUtil;

public class JournalLine  implements Comparable<JournalLine>{

	private static final String COMMA = ",";
	private static final String PIPE = "|";

	private String authorId;
	private String termId;
	private String conceptId;
	private String termRole;
	private Integer status;
	private String oldLexicalValue;
	private String newLexicalValue;
	private Set<String> oldGenericTerm;
	private Set<String> newGenericTerm;
	private Date eventDate;

	private Integer revisionNumber;

	private JournalEventsEnum eventType;

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	public String getTermRole() {
		return termRole;
	}

	public void setTermRole(String termRole) {
		this.termRole = termRole;
	}

	public String getOldLexicalValue() {
		return oldLexicalValue;
	}

	public void setOldLexicalValue(String oldLexicalValue) {
		this.oldLexicalValue = oldLexicalValue;
	}

	public String getNewLexicalValue() {
		return newLexicalValue;
	}

	public void setNewLexicalValue(String newLexicalValue) {
		this.newLexicalValue = newLexicalValue;
	}

	public Set<String> getOldGenericTerm() {
		return oldGenericTerm;
	}

	public void setOldGenericTerm(Set<String> oldGenericTerm) {
		this.oldGenericTerm = oldGenericTerm;
	}

	public Set<String> getNewGenericTerm() {
		return newGenericTerm;
	}

	public void setNewGenericTerm(Set<String> newGenericTerm) {
		this.newGenericTerm = newGenericTerm;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String toString() {
		String line = "";
		line += StringEscapeUtils.escapeCsv(getEventType().toString()) + COMMA;
		line += StringEscapeUtils.escapeCsv(DateUtil.toString(getEventDate()))
				+ COMMA;
		line += StringEscapeUtils.escapeCsv(authorId) + COMMA;
		if (conceptId != null) {
			line += StringEscapeUtils.escapeCsv(conceptId);
		}
		line += COMMA;
		if (termId != null) {
			line += StringEscapeUtils.escapeCsv(termId);
		}
		line += COMMA;
		if (termRole != null) {
			line += StringEscapeUtils.escapeCsv(termRole);
		}
		line += COMMA;
		if (status != null) {
			line += StringEscapeUtils.escapeCsv(LabelUtil
					.getResourceLabel("concept-status[" + status + "]"));
		}
		line += COMMA;
		if (oldLexicalValue != null) {
			line += StringEscapeUtils.escapeCsv(oldLexicalValue);
		}
		line += COMMA;
		if (newLexicalValue != null) {
			line += StringEscapeUtils.escapeCsv(newLexicalValue);
		}
		line += COMMA;
		if (oldGenericTerm != null) {
			Iterator<String> olGenericTermItr = oldGenericTerm.iterator();
			while (olGenericTermItr.hasNext()) {
				line += StringEscapeUtils.escapeCsv(olGenericTermItr.next());
				if (olGenericTermItr.hasNext()) {
					line += PIPE;
				}
			}
		}
		line += COMMA;
		if (newGenericTerm != null) {
			Iterator<String> newGenericTermItr = newGenericTerm.iterator();
			while (newGenericTermItr.hasNext()) {
				line += StringEscapeUtils.escapeCsv(newGenericTermItr.next());
				if (newGenericTermItr.hasNext()) {
					line += PIPE;
				}
			}
		}
		return line;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Integer getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(Integer revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public JournalEventsEnum getEventType() {
		return eventType;
	}

	public void setEventType(JournalEventsEnum eventType) {
		this.eventType = eventType;
	}

	@Override
	public int compareTo(JournalLine o) {
		if (!revisionNumber.equals(o.getRevisionNumber())) {
			return revisionNumber.compareTo(o.getRevisionNumber());
		} else {
			if (!eventDate.equals(o.getEventDate())) {
				return eventDate.compareTo(o.getEventDate());
			} else {
				return eventType.getPriority().compareTo(
						o.getEventType().getPriority());
			}

		}
	}

}
