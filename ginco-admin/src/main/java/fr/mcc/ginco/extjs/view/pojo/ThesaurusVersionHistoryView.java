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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import fr.mcc.ginco.beans.ThesaurusVersionHistory;

/**
 * View class corresponding to {@link ThesaurusVersionHistory} bean, but fully serializable;
 * 
 * @see fr.mcc.ginco.beans
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ThesaurusVersionHistoryView implements Serializable {
	
	private String identifier;
    private String date;
    private String versionNote;
    private String status;
    private Boolean thisVersion;
    private String thesaurusId;

    public ThesaurusVersionHistoryView() {}

	public String getIdentifier() {
		return identifier;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getVersionNote() {
		return versionNote;
	}


	public void setVersionNote(String versionNote) {
		this.versionNote = versionNote;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Boolean getThisVersion() {
		return thisVersion;
	}


	public void setThisVersion(Boolean thisVersion) {
		this.thisVersion = thisVersion;
	}


	public String getThesaurusId() {
		return thesaurusId;
	}


	public void setThesaurusId(String thesaurusId) {
		this.thesaurusId = thesaurusId;
	}

}