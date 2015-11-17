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
package fr.mcc.ginco.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Beans represents <b>thesaurus_array</b> table and is a sub-container for
 * {@link ThesaurusConcept}.
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
public class Alignment implements Serializable {

	private String identifier;
	private Date created;
	private Date modified;
	private String author;
	private ThesaurusConcept sourceConcept;
	private Set<AlignmentConcept> targetConcepts = new HashSet<AlignmentConcept>();
	private Set<AlignmentResource> targetResources = new HashSet<AlignmentResource>();
	private AlignmentType alignmentType;
	private ExternalThesaurus externalTargetThesaurus;

	@XmlTransient
	private Thesaurus internalTargetThesaurus;
	private boolean andRelation;

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ThesaurusConcept getSourceConcept() {
		return sourceConcept;
	}

	public void setSourceConcept(ThesaurusConcept sourceConcept) {
		this.sourceConcept = sourceConcept;
	}

	public Set<AlignmentConcept> getTargetConcepts() {
		return targetConcepts;
	}

	public void setTargetConcepts(Set<AlignmentConcept> targetConcepts) {
		this.targetConcepts = targetConcepts;
	}

	public Set<AlignmentResource> getTargetResources() {
		return targetResources;
	}

	public void setTargetResources(Set<AlignmentResource> targetResources) {
		this.targetResources =targetResources ;
	}

	public AlignmentType getAlignmentType() {
		return alignmentType;
	}

	public void setAlignmentType(AlignmentType alignmentType) {
		this.alignmentType = alignmentType;
	}

	public ExternalThesaurus getExternalTargetThesaurus() {
		return externalTargetThesaurus;
	}

	public void setExternalTargetThesaurus(ExternalThesaurus externalTargetThesaurus) {
		this.externalTargetThesaurus = externalTargetThesaurus;
	}

	public Thesaurus getInternalTargetThesaurus() {
		return internalTargetThesaurus;
	}

	public void setInternalTargetThesaurus(Thesaurus internalTargetThesaurus) {
		this.internalTargetThesaurus = internalTargetThesaurus;
	}

	public boolean isAndRelation() {
		return andRelation;
	}

	public void setAndRelation(boolean andRelation) {
		this.andRelation = andRelation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (identifier == null) {
			result = prime * result
					+ 0;
		} else {
			result = prime * result
					+ identifier.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Alignment other = (Alignment) obj;
		if (identifier == null) {
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) {
			return false;
		}
		return true;
	}


}