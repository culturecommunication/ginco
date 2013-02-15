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
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

@SuppressWarnings("serial")
public class Language implements Serializable, IBaseBean {	

	private String id;
	private String part2b;
	private String part2t;
	private String part1;
	private String scope;
	private String type;
	private String refname;
	private boolean toplanguage;
	private String comment;

    private Set<Thesaurus> thesauruses = new HashSet<Thesaurus>();
    private Set<ThesaurusTerm> thesaurusesTerms  = new HashSet<ThesaurusTerm>();

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPart2b() {
		return part2b;
	}
	public void setPart2b(String part2b) {
		this.part2b = part2b;
	}
	public String getPart2t() {
		return part2t;
	}
	public void setPart2t(String part2t) {
		this.part2t = part2t;
	}
	public String getPart1() {
		return part1;
	}
	public void setPart1(String part1) {
		this.part1 = part1;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getRefname() {
		return refname;
	}
	public void setRefname(String refname) {
		this.refname = refname;
	}

    @JsonIgnore
    public Set<Thesaurus> getThesauruses() {
        return thesauruses;
    }

    public void setThesauruses(Set<Thesaurus> thesauruses) {
        this.thesauruses = thesauruses;
    }
    
    /**
	 * @return True if the tested language belongs to most-used languages in the application, and false if it's a language used rarely
	 */
	public boolean isToplanguage() {
		return toplanguage;
	}
	/**
	 * @param toplanguage
	 * If set to true, the language is declared as frequently used in the application.
	 */
	public void setToplanguage(boolean toplanguage) {
		this.toplanguage = toplanguage;
	}
	
	public Set<ThesaurusTerm> getThesaurusesTerms() {
		return thesaurusesTerms;
	}
	public void setThesaurusesTerms(Set<ThesaurusTerm> thesaurusesTerms) {
		this.thesaurusesTerms = thesaurusesTerms;
	}	
}