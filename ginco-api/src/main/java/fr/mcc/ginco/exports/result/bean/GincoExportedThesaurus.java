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

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;

/**
 * Small utility class for representing an exported Thesaurus
 * With {@MCCExportServiceImpl}
 */
@XmlRootElement
@XmlSeeAlso({Note.class, NodeLabel.class})
public class GincoExportedThesaurus implements Serializable {
	
	//Thesaurus and terms are read by Jaxb and automatically added to the XML structure
	//But its not the case for nested elements (notes, relations, etc.), so we include them
	//in a map that contains the id of the element (term id) and the nested elements (notes, relations, etc.)
	//The use of a JaxbList object is due to the incapacity of Jaxb to serialize a HashMap<String, Bean> directly
	
    private Thesaurus thesaurus;
    private List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
    private List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();
    private List<ThesaurusArray> conceptArrays  = new ArrayList<ThesaurusArray>();
    private Map<String, JaxbList<NodeLabel>> conceptArrayLabels = new Hashtable();
    private List<ThesaurusConceptGroup> conceptsGroups  = new ArrayList<ThesaurusConceptGroup>();
    private List<ThesaurusVersionHistory> thesaurusVersions;
    private Map<String, JaxbList<String>> hierarchicalRelationship = new Hashtable();
    private Map<String, JaxbList<String>> associativeRelationship = new Hashtable();
    private Map<String, JaxbList<Note>> termNotes = new Hashtable();
    private Map<String, JaxbList<Note>> conceptNotes = new Hashtable();

	public Thesaurus getThesaurus() {
		return thesaurus;
	}

	public void setThesaurus(Thesaurus thesaurus) {
		this.thesaurus = thesaurus;
	}

	public List<ThesaurusTerm> getTerms() {
		return terms;
	}

	public void setTerms(List<ThesaurusTerm> terms) {
		this.terms = terms;
	}

	public List<ThesaurusVersionHistory> getThesaurusVersions() {
		return thesaurusVersions;
	}

	public void setThesaurusVersions(List<ThesaurusVersionHistory> versions) {
		this.thesaurusVersions = versions;
	}

	public Map<String, JaxbList<String>> getHierarchicalRelationship() {
		return hierarchicalRelationship;
	}

	public void setHierarchicalRelationship(
			Map<String, JaxbList<String>> parentConceptRelationship) {
		this.hierarchicalRelationship = parentConceptRelationship;
	}

	public Map<String, JaxbList<String>> getAssociativeRelationship() {
		return associativeRelationship;
	}

	public void setAssociativeRelationship(Map<String, JaxbList<String>> associativeRelationship) {
		this.associativeRelationship = associativeRelationship;
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

	public List<ThesaurusConceptGroup> getConceptsGroups() {
		return conceptsGroups;
	}

	public void setConceptsGroups(List<ThesaurusConceptGroup> conceptsGroups) {
		this.conceptsGroups = conceptsGroups;
	}

	public List<ThesaurusArray> getConceptArrays() {
		return conceptArrays;
	}

	public void setConceptArrays(List<ThesaurusArray> conceptArrays) {
		this.conceptArrays = conceptArrays;
	}

	public List<ThesaurusConcept> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<ThesaurusConcept> concepts) {
		this.concepts = concepts;
	}

	public Map<String, JaxbList<NodeLabel>> getConceptArrayLabels() {
		return conceptArrayLabels;
	}

	public void setConceptArrayLabels(Map<String, JaxbList<NodeLabel>> conceptArrayLabels) {
		this.conceptArrayLabels = conceptArrayLabels;
	}
    
}
