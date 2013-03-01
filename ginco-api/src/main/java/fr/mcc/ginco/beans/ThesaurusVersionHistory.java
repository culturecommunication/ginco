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

public class ThesaurusVersionHistory implements Serializable {
	private String identifier;
    private Date date;
    private  String versionNote;
    private Boolean currentVersion;
    private Boolean thisVersion;
    private Thesaurus thesaurusIdentifier;

    public ThesaurusVersionHistory() {
		super();
	}

	public ThesaurusVersionHistory(String identifier, Date date,
			String versionVote, Boolean currentVersion, Boolean thisVersion,
			Thesaurus thesaurusIdentifier) {
		super();
		this.identifier = identifier;
		this.date = date;
		this.versionNote = versionVote;
		this.currentVersion = currentVersion;
		this.thisVersion = thisVersion;
		this.thesaurusIdentifier = thesaurusIdentifier;
	}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVersionNote() {
        return versionNote;
    }

    public void setVersionNote(String versionNote) {
        this.versionNote = versionNote;
    }

    public Boolean getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Boolean currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Boolean getThisVersion() {
        return thisVersion;
    }

    public void setThisVersion(Boolean thisVersion) {
        this.thisVersion = thisVersion;
    }

    public Thesaurus getThesaurusIdentifier() {
        return thesaurusIdentifier;
    }

    public void setThesaurusIdentifier(Thesaurus thesaurusIdentifier) {
        this.thesaurusIdentifier = thesaurusIdentifier;
    }	
}
