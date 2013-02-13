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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * @author frsto
 *
 */
@SuppressWarnings("serial")
public class Thesaurus implements Serializable, IBaseBean {
    private String identifier;
    private String contributor;
    private String coverage;
    private Date date;
    private String description;
    private String publisher;
    private String relation;
    private String rights;
    private String source;
    private String subject;
    private String title;
    private Date created;
    private ThesaurusFormat format;
    private ThesaurusType type;
    private ThesaurusOrganization creator;
    private Set<Language> lang = new HashSet<Language>();
    private Set<ThesaurusVersionHistory> versions;
    private Set<ThesaurusTerm> thesaurusesTerms  = new HashSet<ThesaurusTerm>();

    public Thesaurus() {
    }

    public Thesaurus(String identifier, String contributor, String coverage,
                     Date date, String description, String publisher, String relation,
                     String rights, String source, String subject, String title,
                     Date created, ThesaurusFormat format, ThesaurusType type, ThesaurusOrganization creator) {
        this.identifier = identifier;
        this.contributor = contributor;
        this.coverage = coverage;
        this.date = new Timestamp(date.getTime());
        this.description = description;
        this.publisher = publisher;
        this.relation = relation;
        this.rights = rights;
        this.source = source;
        this.subject = subject;
        this.title = title;
        this.created = new Timestamp(created.getTime());
        this.format = format;
        this.type = type;
        this.creator = creator;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public ThesaurusFormat getFormat() {
        return format;
    }

	public void setFormat(ThesaurusFormat format) {
		this.format = format;
	}

	public ThesaurusType getType() {
		return type;
	}

	public void setType(ThesaurusType type) {
		this.type = type;
	}

	public ThesaurusOrganization getCreator() {
		return creator;
	}

	public void setCreator(ThesaurusOrganization creator) {
		this.creator = creator;
	}	

	@Override
	public String getId() {
		return identifier;
	}

	@JsonIgnore
	public Set<ThesaurusVersionHistory> getVersions() {
		return versions;
	}

	public void setVersions(Set<ThesaurusVersionHistory> versions) {
		this.versions = versions;
	}

    /**
     * @return All languages avalaible for this Thesaurus
     */
    @JsonIgnore
    public Set<Language> getLang() {
        return lang;
    }

    /**
     * @param lang
     */
    public void setLang(Set<Language> lang) {
        this.lang = lang;
    }

	/**
	 * @return the thesaurusesTerms
	 */
	public Set<ThesaurusTerm> getThesaurusesTerms() {
		return thesaurusesTerms;
	}

	/**
	 * @param thesaurusesTerms the thesaurusesTerms to set
	 */
	public void setThesaurusesTerms(Set<ThesaurusTerm> thesaurusesTerms) {
		this.thesaurusesTerms = thesaurusesTerms;
	}

}