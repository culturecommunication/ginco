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

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * View class corresponding to {@link fr.mcc.ginco.beans.ThesaurusArray} bean,
 * but fully serializable
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ThesaurusArrayView implements Serializable, SecuredResourceView {
	private String identifier;
	private String superOrdinateId;
	private String superOrdinateConceptLabel;
	private String created;
	private String modified;
	private String parentArrayId;
	private String parentArrayLabel;

	private List<ThesaurusArrayConceptView> concepts;

	private String label;
	private String language;
	private Integer nodeLabelId;

	private String thesaurusId;

	private Boolean order;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSuperOrdinateId() {
		return superOrdinateId;
	}

	public void setSuperOrdinateId(String superOrdinateConceptId) {
		this.superOrdinateId = superOrdinateConceptId;
	}

	public String getSuperOrdinateLabel() {
		return superOrdinateConceptLabel;
	}

	public void setSuperOrdinateLabel(String superOrdinateConceptLabel) {
		this.superOrdinateConceptLabel = superOrdinateConceptLabel;
	}

	public String getSuperOrdinateConceptLabel() {
		return superOrdinateConceptLabel;
	}

	public void setSuperOrdinateConceptLabel(String superOrdinateConceptLabel) {
		this.superOrdinateConceptLabel = superOrdinateConceptLabel;
	}

	public List<ThesaurusArrayConceptView> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<ThesaurusArrayConceptView> concepts) {
		this.concepts = concepts;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getThesaurusId() {
		return thesaurusId;
	}

	public void setThesaurusId(String thesaurusId) {
		this.thesaurusId = thesaurusId;
	}

	public Integer getNodeLabelId() {
		return nodeLabelId;
	}

	public void setNodeLabelId(Integer nodeLabelId) {
		this.nodeLabelId = nodeLabelId;
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

	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
	}

	public String getParentArrayId() {
		return parentArrayId;
	}

	public void setParentArrayId(String parentArrayId) {
		this.parentArrayId = parentArrayId;
	}

	public String getParentArrayLabel() {
		return parentArrayLabel;
	}

	public void setParentArrayLabel(String parentArrayLabel) {
		this.parentArrayLabel = parentArrayLabel;
	}

}
