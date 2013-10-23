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
package fr.mcc.ginco.extjs.view.pojo;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusType;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * View class corresponding to {@link Thesaurus} bean, but fully serializable;
 * contains all links to other business-objects (full beans
 * {@link ThesaurusType} and {@link ThesaurusFormat}).
 *
 * @see fr.mcc.ginco.beans
 */
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ThesaurusView implements Serializable {

	private String id;
	private String contributor;
	private String coverage;
	private String date;
	private String description;
	private String publisher;
	private String relation;
	private String rights;
	private String source;
	private String subject;
	private String title;
	private String created;
	private Boolean defaultTopConcept;
    private Boolean archived;
    private Boolean canBeDeleted;
	private Integer type;
	private String creatorName;
	private String creatorHomepage;
	private String creatorEmail;
    private Boolean polyHierarchical;

	private List<String> languages = new ArrayList<String>();
	private List<Integer> formats = new ArrayList<Integer>();

    public ThesaurusView() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer typeId) {
		this.type = typeId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorHomepage() {
		return creatorHomepage;
	}

	public void setCreatorHomepage(String creatorHomepage) {
		this.creatorHomepage = creatorHomepage;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> lang) {
		this.languages = lang;
	}

	public Boolean getDefaultTopConcept() {
		return defaultTopConcept;
	}

	public void setDefaultTopConcept(Boolean defaulttopconcept) {
		this.defaultTopConcept = defaulttopconcept;
	}

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(Boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    public Boolean getPolyHierarchical() {
        return polyHierarchical;
    }

    public void setPolyHierarchical(Boolean polyHierarchical) {
        this.polyHierarchical = polyHierarchical;
    }

	public List<Integer> getFormats() {
		return formats;
	}

	public void setFormats(List<Integer> formats) {
		this.formats = formats;
	}
}
