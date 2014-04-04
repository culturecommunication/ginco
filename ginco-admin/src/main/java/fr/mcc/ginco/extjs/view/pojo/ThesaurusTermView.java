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

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * View class corresponding to {@link ThesaurusTerm} bean, but fully serializable;
 * contains all links to other business-objects (full beans
 * {@link Thesaurus} and {@link Language}).
 *
 * @see fr.mcc.ginco.beans
 */
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ThesaurusTermView implements Serializable, SecuredResourceView {
	private String identifier;
	private String lexicalValue;
	private String created;
	private String modified;
	private String source;
	private Boolean prefered;
	private Boolean hidden;
	private Integer status;
	private String conceptId;
	private String thesaurusId;
	private String language;
	private String role;
	private Boolean topistopterm;
	private List<String> conceptsPath;


	public ThesaurusTermView() {
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLexicalValue() {
		return lexicalValue;
	}

	public void setLexicalValue(String lexicalValue) {
		this.lexicalValue = lexicalValue;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Boolean getPrefered() {
		return prefered;
	}

	public void setPrefered(Boolean prefered) {
		this.prefered = prefered;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	public String getThesaurusId() {
		return thesaurusId;
	}

	public void setThesaurusId(String thesaurusId) {
		this.thesaurusId = thesaurusId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getTopistopterm() {
		return topistopterm;
	}

	public void setTopistopterm(Boolean topistopterm) {
		this.topistopterm = topistopterm;
	}

	public List<String> getConceptsPath() {
		return conceptsPath;
	}

	public void setConceptsPath(List<String> conceptsPath) {
		this.conceptsPath = conceptsPath;
	}

}