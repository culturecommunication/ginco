/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 *
 * contact.gincoculture_at_gouv.fr
 *
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 *
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
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.solr;

import java.util.List;

public class SearchResult {

	private String identifier;
	private String thesaurusId;
	private String thesaurusTitle;
	private String type;
	private String lexicalValue;
    private String typeExt;
    private String modified;
    private String created;
    private Integer status;
    private String conceptId;
	private List<String> languages;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getThesaurusId() {
		return thesaurusId;
	}

	public void setThesaurusId(String thesaurusId) {
		this.thesaurusId = thesaurusId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLexicalValue() {
		return lexicalValue;
	}

	public void setLexicalValue(String lexicalValue) {
		this.lexicalValue = lexicalValue;
	}

	public String getThesaurusTitle() {
		return thesaurusTitle;
	}

	public void setThesaurusTitle(String thesaurusTitle) {
		this.thesaurusTitle = thesaurusTitle;
	}

    public String getTypeExt() {
        return typeExt;
    }

    public void setTypeExt(String typeExt) {
        this.typeExt = typeExt;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}
}