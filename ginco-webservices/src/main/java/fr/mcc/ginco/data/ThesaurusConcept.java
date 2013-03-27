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

package fr.mcc.ginco.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;

public class ThesaurusConcept {
	
	private String identifier;
	private Date created;
	private Date modified;
	private Integer status;
	private String notation;
	private Boolean topConcept;
	private Thesaurus thesaurus;
	private Set<ThesaurusConcept> parentConcepts = new HashSet<ThesaurusConcept>();
	private Set<ThesaurusConcept> rootConcepts = new HashSet<ThesaurusConcept>();
	private Set<AssociativeRelationship> associativeRelationshipLeft;
	private Set<AssociativeRelationship> associativeRelationshipRight;
	private Set<ThesaurusArray> conceptArrays;
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getNotation() {
		return notation;
	}
	public void setNotation(String notation) {
		this.notation = notation;
	}
	public Boolean getTopConcept() {
		return topConcept;
	}
	public void setTopConcept(Boolean topConcept) {
		this.topConcept = topConcept;
	}
	public Thesaurus getThesaurus() {
		return thesaurus;
	}
	public void setThesaurus(Thesaurus thesaurus) {
		this.thesaurus = thesaurus;
	}
	public Set<ThesaurusConcept> getParentConcepts() {
		return parentConcepts;
	}
	public void setParentConcepts(Set<ThesaurusConcept> parentConcepts) {
		this.parentConcepts = parentConcepts;
	}
	public Set<ThesaurusConcept> getRootConcepts() {
		return rootConcepts;
	}
	public void setRootConcepts(Set<ThesaurusConcept> rootConcepts) {
		this.rootConcepts = rootConcepts;
	}
	public Set<AssociativeRelationship> getAssociativeRelationshipLeft() {
		return associativeRelationshipLeft;
	}
	public void setAssociativeRelationshipLeft(
			Set<AssociativeRelationship> associativeRelationshipLeft) {
		this.associativeRelationshipLeft = associativeRelationshipLeft;
	}
	public Set<AssociativeRelationship> getAssociativeRelationshipRight() {
		return associativeRelationshipRight;
	}
	public void setAssociativeRelationshipRight(
			Set<AssociativeRelationship> associativeRelationshipRight) {
		this.associativeRelationshipRight = associativeRelationshipRight;
	}
	public Set<ThesaurusArray> getConceptArrays() {
		return conceptArrays;
	}
	public void setConceptArrays(Set<ThesaurusArray> conceptArrays) {
		this.conceptArrays = conceptArrays;
	}
	
	
}
