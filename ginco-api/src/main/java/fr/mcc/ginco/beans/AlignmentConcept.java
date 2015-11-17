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

@XmlAccessorType(XmlAccessType.FIELD)
public class AlignmentConcept {
	@XmlTransient
	private Integer identifier;
	private String externalTargetConcept;
	private ThesaurusConcept internalTargetConcept;

	@XmlTransient
	private Alignment alignment;

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public String getExternalTargetConcept() {
		return externalTargetConcept;
	}

	public void setExternalTargetConcept(String externalTargetConcept) {
		this.externalTargetConcept = externalTargetConcept;
	}

	public ThesaurusConcept getInternalTargetConcept() {
		return internalTargetConcept;
	}

	public void setInternalTargetConcept(ThesaurusConcept internalTargetConcept) {
		this.internalTargetConcept = internalTargetConcept;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (externalTargetConcept == null) {
			result = prime
					* result
					+ 0;
		} else {
			result = prime
					* result
					+ externalTargetConcept
					.hashCode();
		}
		if (internalTargetConcept == null) {
			result = prime
					* result
					+ 0;
		} else {
			result = prime
					* result
					+ internalTargetConcept
					.hashCode();
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
		AlignmentConcept other = (AlignmentConcept) obj;
		if (externalTargetConcept == null) {
			if (other.externalTargetConcept != null) {
				return false;
			}
		} else if (!externalTargetConcept.equals(other.externalTargetConcept)) {
			return false;
		}
		if (internalTargetConcept == null) {
			if (other.internalTargetConcept != null) {
				return false;
			}
		} else if (!internalTargetConcept.equals(other.internalTargetConcept)) {
			return false;
		}
		return true;
	}


}
