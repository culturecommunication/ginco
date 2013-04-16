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
package fr.mcc.ginco.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Bean represents <b>thesaurus_term</b> table, contains some lexical
 * value that could be used in different cases.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class SplitNonPreferredTerm implements Serializable, IAuditableBean {

    private String identifier;

    private String lexicalValue;
    private Date created;
    private Date modified;
    private String source;
    private Integer status;
    @XmlTransient
    private Thesaurus thesaurus;
    private Language language;
    private Set<ThesaurusTerm> preferredTerms = new HashSet<ThesaurusTerm>();

    public String getIdentifier() {
        return identifier;
    }

    @Field
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLexicalValue() {
        return lexicalValue;
    }

    @Field
    public void setLexicalValue(String lexicalValue) {
        this.lexicalValue = lexicalValue;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

   
    public Thesaurus getThesaurus() {
		return thesaurus;
	}

	public void setThesaurus(Thesaurus thesaurus) {
		this.thesaurus = thesaurus;
	}

	public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

	@Override
	public String getThesaurusId() {		
		return thesaurus.getIdentifier();
	}

	public Set<ThesaurusTerm> getPreferredTerms() {
		return preferredTerms;
	}

	public void setPreferredTerms(Set<ThesaurusTerm> preferredTerms) {
		this.preferredTerms = preferredTerms;
	}

}