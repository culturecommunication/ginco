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
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.beans;

public class Thesaurus {
	String identifier;
	String contributor;
	String coverage;
	String date;
	String description;
	String publisher;
	String relation;
	String rights;
	String source;
	String subject;
	String title;
	String created;
	String format;
	String type;
	String creator;

	public Thesaurus() {
		super();
	}

	public Thesaurus(String identifier, String contributor, String coverage,
			String date, String description, String publisher, String relation,
			String rights, String source, String subject, String title,
			String created, String format, String type, String creator) {
		super();
		this.identifier = identifier;
		this.contributor = contributor;
		this.coverage = coverage;
		this.date = date;
		this.description = description;
		this.publisher = publisher;
		this.relation = relation;
		this.rights = rights;
		this.source = source;
		this.subject = subject;
		this.title = title;
		this.created = created;
		this.format = format;
		this.type = type;
		this.creator = creator;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getContributor() {
		return contributor;
	}

	public String getCoverage() {
		return coverage;
	}

	public String getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getRelation() {
		return relation;
	}

	public String getRights() {
		return rights;
	}

	public String getSource() {
		return source;
	}

	public String getSubject() {
		return subject;
	}

	public String getTitle() {
		return title;
	}

	public String getCreated() {
		return created;
	}

	public String getFormat() {
		return format;
	}

	public String getType() {
		return type;
	}

	public String getCreator() {
		return creator;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

}