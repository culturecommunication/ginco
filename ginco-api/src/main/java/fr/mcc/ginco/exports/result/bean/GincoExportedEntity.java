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

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.beans.CustomTermAttributeType;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;

public class GincoExportedEntity implements Serializable {
	
	private List<ThesaurusConcept> concepts = new ArrayList<ThesaurusConcept>();
	private List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();

	
	private List<CustomTermAttributeType> termAttributeTypes = new ArrayList<CustomTermAttributeType>();
	private List<CustomConceptAttributeType> conceptAttributeTypes = new ArrayList<CustomConceptAttributeType>();
	
	private Map<String, JaxbList<CustomConceptAttribute>> conceptAttributes = new Hashtable<String, JaxbList<CustomConceptAttribute>>();
	private Map<String, JaxbList<CustomTermAttribute>> termAttributes = new Hashtable<String, JaxbList<CustomTermAttribute>>();
	
	public List<CustomTermAttributeType> getTermAttributeTypes() {
		return termAttributeTypes;
	}
	public void setTermAttributeTypes(
			List<CustomTermAttributeType> termAttributeTypes) {
		this.termAttributeTypes = termAttributeTypes;
	}
	public List<CustomConceptAttributeType> getConceptAttributeTypes() {
		return conceptAttributeTypes;
	}
	public void setConceptAttributeTypes(
			List<CustomConceptAttributeType> conceptAttributeTypes) {
		this.conceptAttributeTypes = conceptAttributeTypes;
	}
	public Map<String, JaxbList<CustomConceptAttribute>> getConceptAttributes() {
		return conceptAttributes;
	}
	public void setConceptAttributes(
			Map<String, JaxbList<CustomConceptAttribute>> conceptAttributes) {
		this.conceptAttributes = conceptAttributes;
	}
	public Map<String, JaxbList<CustomTermAttribute>> getTermAttributes() {
		return termAttributes;
	}
	public void setTermAttributes(
			Map<String, JaxbList<CustomTermAttribute>> termAttributes) {
		this.termAttributes = termAttributes;
	}
	public List<ThesaurusConcept> getConcepts() {
		return concepts;
	}
	public void setConcepts(List<ThesaurusConcept> concepts) {
		this.concepts = concepts;
	}
	public List<ThesaurusTerm> getTerms() {
		return terms;
	}
	public void setTerms(List<ThesaurusTerm> terms) {
		this.terms = terms;
	}
}
