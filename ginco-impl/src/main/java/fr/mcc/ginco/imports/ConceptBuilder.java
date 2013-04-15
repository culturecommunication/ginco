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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IAssociativeRelationshipRoleService;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipServiceUtil;
import fr.mcc.ginco.services.IThesaurusConceptService;

/**
 * Builder in charge of building ThesaurusConcept
 *
 */
@Service("skosConceptBuilder")
public class ConceptBuilder extends AbstractBuilder {

	@Log
	private Logger logger;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	@Inject
	@Named("conceptHierarchicalRelationshipServiceUtil")
	private IConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;

	@Inject
	@Named("associativeRelationshipRoleService")
	private IAssociativeRelationshipRoleService associativeRelationshipRoleService;

	public static Map<String, ThesaurusConcept> builtConcepts = new HashMap<String, ThesaurusConcept>();

	public ConceptBuilder() {
		super();
	}

	/**
	 * Sets the basic attributes of a concept
	 * @param skosConcept
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	public ThesaurusConcept buildConcept(Resource skosConcept,
			Thesaurus thesaurus) throws BusinessException {
		logger.debug("Building concept with uri : " + skosConcept.getURI());
		ThesaurusConcept concept = new ThesaurusConcept();
		concept.setIdentifier(skosConcept.getURI());
		concept.setThesaurus(thesaurus);
		concept.setTopConcept(thesaurus.isDefaultTopConcept());
		concept.setCreated(thesaurus.getCreated());
		concept.setModified(thesaurus.getDate());
		concept.setStatus(ConceptStatusEnum.VALIDATED.getStatus());
		builtConcepts.put(skosConcept.getURI(), concept);

		return concept;
	}

	/**
	 * Build associative relationships between concepts
	 * @param skosConcept
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	public Set<AssociativeRelationship> buildConceptAssociativerelationship(Resource skosConcept,
			Thesaurus thesaurus) throws BusinessException {
		ThesaurusConcept concept = builtConcepts.get(skosConcept.getURI());
		StmtIterator stmtRelatedtItr = skosConcept.listProperties(SKOS.RELATED);
		Set<AssociativeRelationship> relationshipsLeft = new HashSet<AssociativeRelationship>();

		while (stmtRelatedtItr.hasNext()) {
			Statement stmt = stmtRelatedtItr.next();
			Resource relatedConceptRes = stmt.getObject().asResource();
			ThesaurusConcept relatedConcept = builtConcepts
					.get(relatedConceptRes.getURI());
		
			boolean alreadyAssociatedLeft = false ;
			if (relatedConcept.getAssociativeRelationshipLeft() != null) {
				Set<AssociativeRelationship> alreadyExistingAssociations = relatedConcept.getAssociativeRelationshipLeft();
				for(AssociativeRelationship relation:alreadyExistingAssociations) {
					if (relation.getConceptLeft().equals(concept) || relation.getConceptRight().equals(concept)) {
						alreadyAssociatedLeft = true;
					}
				}
			}

			if (!alreadyAssociatedLeft) {
				AssociativeRelationship relationshipLeft = new AssociativeRelationship();
				AssociativeRelationship.Id relationshipId = new AssociativeRelationship.Id();
				relationshipId.setConcept1(concept.getIdentifier());
				relationshipId.setConcept2(relatedConcept.getIdentifier());
				relationshipLeft.setIdentifier(relationshipId);
				relationshipLeft.setConceptLeft(concept);
				relationshipLeft.setConceptRight(relatedConcept);
				relationshipLeft
						.setRelationshipRole(associativeRelationshipRoleService
								.getDefaultAssociativeRelationshipRoleRole());
				relationshipsLeft.add(relationshipLeft);

			}

		}
		return relationshipsLeft;
	}
	/**
	 * Build direct hierarchical and associative relationships between concepts
	 * @param skosConcept
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	public ThesaurusConcept buildConceptHierarchicalRelationships(Resource skosConcept,
			Thesaurus thesaurus) throws BusinessException {
		logger.debug("Building relationships for concept : " + skosConcept.getURI());
		ThesaurusConcept concept = builtConcepts.get(skosConcept.getURI());
		StmtIterator stmtParentItr = skosConcept.listProperties(SKOS.BROADER);
		Set<ThesaurusConcept> parentConcepts = new HashSet<ThesaurusConcept>();
		while (stmtParentItr.hasNext()) {
			Statement stmt = stmtParentItr.next();
			Resource parentConceptRes = stmt.getObject().asResource();
			String relatedURI = parentConceptRes.getURI();
			ThesaurusConcept parentConcept = builtConcepts.get(relatedURI);
			if (parentConcept!=null)
				parentConcepts.add(parentConcept);
		}
		concept.setParentConcepts(parentConcepts);
	
		return concept;
	}

	/**
	 * Launch the calculation and set the root concepts of the given concept
	 * @param skosConcept
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	public ThesaurusConcept buildConceptRoot(Resource skosConcept,
			Thesaurus thesaurus) throws BusinessException {
		logger.debug("Building root concepts for concept : " + skosConcept.getURI());
		ThesaurusConcept concept = builtConcepts.get(skosConcept.getURI());
		concept.setRootConcepts(new HashSet<ThesaurusConcept>(
				conceptHierarchicalRelationshipServiceUtil.getRootConcepts(concept)));
		return concept;

	}
}
