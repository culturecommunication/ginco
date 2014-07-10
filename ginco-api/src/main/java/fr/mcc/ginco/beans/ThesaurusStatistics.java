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

public class ThesaurusStatistics {

	private String thesaurusId = "";
	private Number nbOfTerms = 0;
	private Number nbOfTermsWoNotes = 0;
	private Number nbOfNonPreferredTerms = 0;
	private Number nbOfConcepts = 0;
	private Number nbOfConceptsWoNotes = 0;
	private Number nbOfComplexConcepts = 0;
	private Number nbOfThesaurusArrays = 0;
	private Number nbOfThesaurusGroups = 0;
	private Number nbOfConceptsAlignedToIntThes = 0;
	private Number nbOfConceptsAlignedToExtThes = 0;
	private Number nbOfConceptsAlignedToMyThes = 0;

	public ThesaurusStatistics(String thesaurusId2) {
		this.thesaurusId = thesaurusId2;
	}

	public Number getNbOfTerms() {
		return nbOfTerms;
	}

	public void setNbOfTerms(Number nbOfTerms) {
		this.nbOfTerms = nbOfTerms;
	}

	public Number getNbOfNonPreferredTerms() {
		return nbOfNonPreferredTerms;
	}

	public void setNbOfNonPreferredTerms(Number nbOfNonPreferredTerms) {
		this.nbOfNonPreferredTerms = nbOfNonPreferredTerms;
	}

	public Number getNbOfConcepts() {
		return nbOfConcepts;
	}

	public void setNbOfConcepts(Number nbOfConcepts) {
		this.nbOfConcepts = nbOfConcepts;
	}

	public Number getNbOfComplexConcepts() {
		return nbOfComplexConcepts;
	}

	public void setNbOfComplexConcepts(Number nbOfComplexConcepts) {
		this.nbOfComplexConcepts = nbOfComplexConcepts;
	}

	public Number getNbOfThesaurusArrays() {
		return nbOfThesaurusArrays;
	}

	public void setNbOfThesaurusArrays(Number nbOfThesaurusArrays) {
		this.nbOfThesaurusArrays = nbOfThesaurusArrays;
	}

	public Number getNbOfThesaurusGroups() {
		return nbOfThesaurusGroups;
	}

	public void setNbOfThesaurusGroups(Number nbOfThesaurusGroups) {
		this.nbOfThesaurusGroups = nbOfThesaurusGroups;
	}

	public Number getNbOfTermsWoNotes() {
		return nbOfTermsWoNotes;
	}

	public void setNbOfTermsWoNotes(Number nbOfTermsWoNotes) {
		this.nbOfTermsWoNotes = nbOfTermsWoNotes;
	}

	public Number getNbOfConceptsWoNotes() {
		return nbOfConceptsWoNotes;
	}

	public void setNbOfConceptsWoNotes(Number nbOfConceptsWoNotes) {
		this.nbOfConceptsWoNotes = nbOfConceptsWoNotes;
	}

	public String getThesaurusId() {
		return thesaurusId;
	}

	public void setThesaurusId(String thesaurusId) {
		this.thesaurusId = thesaurusId;
	}

	public Number getNbOfConceptsAlignedToIntThes() {
		return nbOfConceptsAlignedToIntThes;
	}

	public void setNbOfConceptsAlignedToIntThes(Number nbOfConceptsAlignedToIntThes) {
		this.nbOfConceptsAlignedToIntThes = nbOfConceptsAlignedToIntThes;
	}

	public Number getNbOfConceptsAlignedToExtThes() {
		return nbOfConceptsAlignedToExtThes;
	}

	public void setNbOfConceptsAlignedToExtThes(Number nbOfConceptsAlignedToExtThes) {
		this.nbOfConceptsAlignedToExtThes = nbOfConceptsAlignedToExtThes;
	}

	public Number getNbOfConceptsAlignedToMyThes() {
		return nbOfConceptsAlignedToMyThes;
	}

	public void setNbOfConceptsAlignedToMyThes(Number nbOfConceptsAlignedToMyThes) {
		this.nbOfConceptsAlignedToMyThes = nbOfConceptsAlignedToMyThes;
	}


}
