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
package fr.mcc.ginco.exports.result.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;

/**
 * Small utility class for representing an exported concept branch With
 * {@GincoBranchExportServiceImpl}
 */
@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({ Note.class, ConceptHierarchicalRelationship.class, Alignment.class })
public class GincoExportedBranch implements Serializable {

	// Concept and terms are read by Jaxb and automatically added to the XML
	// structure
	// But its not the case for nested elements (notes, versions, for example),
	// so we include them
	// in a map that contains the id of the element (for example term id) and
	// its nested elements (notes)
	// The use of a JaxbList object is due to the inability of Jaxb to serialize
	// a HashMap<String, Bean> directly
	private ThesaurusConcept rootConcept = new ThesaurusConcept();
	private List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
	private List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
	private Map<String, JaxbList<ConceptHierarchicalRelationship>> hierarchicalRelationship = new Hashtable<String, JaxbList<ConceptHierarchicalRelationship>>();
	private Map<String, JaxbList<Note>> termNotes = new Hashtable<String, JaxbList<Note>>();
	private Map<String, JaxbList<Note>> conceptNotes = new Hashtable<String, JaxbList<Note>>();
    private Map<String, JaxbList<Alignment>> alignments = new Hashtable<String, JaxbList<Alignment>>();

	public List<ThesaurusTerm> getTerms() {
		return terms;
	}

	public void setTerms(List<ThesaurusTerm> terms) {
		this.terms = terms;
	}

	public Map<String, JaxbList<ConceptHierarchicalRelationship>> getHierarchicalRelationship() {
		return hierarchicalRelationship;
	}

	public void setHierarchicalRelationship(
			Map<String, JaxbList<ConceptHierarchicalRelationship>> parentConceptRelationship) {
		this.hierarchicalRelationship = parentConceptRelationship;
	}

	public Map<String, JaxbList<Note>> getTermNotes() {
		return termNotes;
	}

	public void setTermNotes(Map<String, JaxbList<Note>> termNotes) {
		this.termNotes = termNotes;
	}

	public Map<String, JaxbList<Note>> getConceptNotes() {
		return conceptNotes;
	}

	public void setConceptNotes(Map<String, JaxbList<Note>> conceptNotes) {
		this.conceptNotes = conceptNotes;
	}

	public List<ThesaurusConcept> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<ThesaurusConcept> concepts) {
		this.concepts = concepts;
	}

	public ThesaurusConcept getRootConcept() {
		return rootConcept;
	}

	public void setRootConcept(ThesaurusConcept rootConcept) {
		this.rootConcept = rootConcept;
	}

	public Map<String, JaxbList<Alignment>> getAlignments() {
		return alignments;
	}

	public void setAlignments(Map<String, JaxbList<Alignment>> alignments) {
		this.alignments = alignments;
	}
	
}
