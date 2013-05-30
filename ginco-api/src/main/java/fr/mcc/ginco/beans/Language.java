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

/**
 * Beans represents languages_iso639 table, which contains different languages
 * to be used in other beans.
 */
@SuppressWarnings("serial")
public class Language implements Serializable {	

	private String id;
	private String part1;
	private String refname;
	private boolean topLanguage;
	private boolean principalLanguage;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPart1() {
		return part1;
	}
	public void setPart1(String part1) {
		this.part1 = part1;
	}
	
	public String getRefname() {
		return refname;
	}
	public void setRefname(String refname) {
		this.refname = refname;
	}
    
	/**
	 * @return True if the tested language belongs to most-used languages in the application, and false if it's a language used rarely
	 */   
    public boolean isTopLanguage() {
		return topLanguage;
	}
    /**
	 * @param toplanguage
	 * If set to true, the language is declared as frequently used in the application.
	 */
	public void setTopLanguage(boolean topLanguage) {
		this.topLanguage = topLanguage;
	}
	public boolean isPrincipalLanguage() {
		return principalLanguage;
	}
	public void setPrincipalLanguage(boolean principalLanguage) {
		this.principalLanguage = principalLanguage;
	}	
}