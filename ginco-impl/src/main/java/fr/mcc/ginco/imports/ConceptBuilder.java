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
package fr.mcc.ginco.imports;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusConceptService;

@Service("skosConceptBuilder")
public class ConceptBuilder extends AbstractBuilder{

	@Log
	private Logger logger;	
	
	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	public ConceptBuilder() {
		super();
	}

	public ThesaurusConcept buildConcept(Resource skosConcept, Model model, Thesaurus thesaurus) throws BusinessException {
		ThesaurusConcept concept = new ThesaurusConcept();
		concept.setIdentifier(skosConcept.getURI());
		concept.setThesaurus(thesaurus);
		concept.setTopConcept(thesaurus.isDefaultTopConcept());
		concept.setCreated(thesaurus.getCreated());
		concept.setModified(thesaurus.getDate());	
	

		//Statement stmtRelated = skosConcept.getProperty(SKOS.RELATED);
		//concept.setAssociativeRelationshipLeft(associativeRelationshipLeft);
		// concept.setAssociativeRelationshipRight(associativeRelationshipRight);
		// concept.setConceptArrays(conceptArrays);

		/*StmtIterator stmtParentItr = skosConcept.listProperties(SKOS.BROADER);
		Set<ThesaurusConcept> parentConcepts = new HashSet<ThesaurusConcept>();
		while(stmtParentItr.hasNext()) {
			Statement stmt = stmtParentItr.next();
			Resource parentConceptRes = stmt.getObject().asResource();
			ThesaurusConcept parentConcept  = buildConcept(parentConceptRes, model, thesaurus);
			parentConcepts.add(parentConcept);
		}
		concept.setParentConcepts(parentConcepts);
		concept.setRootConcepts(new HashSet<ThesaurusConcept>(thesaurusConceptService.getRootConcepts(concept)));
		*/
		//TODO
		//concept.setNotation("");
		// concept.setStatus(status);
		
		
	

		return concept;
	}

	
}
