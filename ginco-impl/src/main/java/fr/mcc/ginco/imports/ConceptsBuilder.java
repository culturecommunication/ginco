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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentConcept;
import fr.mcc.ginco.beans.AssociativeRelationship;
import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.IAlignmentDAO;
import fr.mcc.ginco.dao.IAssociativeRelationshipDAO;
import fr.mcc.ginco.dao.IConceptHierarchicalRelationshipDAO;
import fr.mcc.ginco.dao.IExternalThesaurusDAO;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.INoteDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.services.IConceptHierarchicalRelationshipServiceUtil;

/**
 * Builder in charge of building ThesaurusConcept
 * 
 */
@Service("skosConceptsBuilder")
public class ConceptsBuilder extends AbstractBuilder {
	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Inject
	@Named("associativeRelationshipDAO")
	private IAssociativeRelationshipDAO associativeRelationshipDAO;

	@Inject
	@Named("thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;

	@Inject
	@Named("noteDAO")
	private INoteDAO noteDAO;

	@Inject
	@Named("skosConceptBuilder")
	private ConceptBuilder conceptBuilder;

	@Inject
	@Named("skosConceptNoteBuilder")
	private ConceptNoteBuilder conceptNoteBuilder;

	@Inject
	@Named("alignmentDAO")
	private IAlignmentDAO alignmentDAO;

	@Inject
	@Named("alignmentConceptDAO")
	private IGenericDAO<AlignmentConcept, Integer> alignmentConceptDAO;

	@Inject
	@Named("externalThesaurusDAO")
	private IExternalThesaurusDAO externalThesaurusDAO;

	@Inject
	@Named("conceptHierarchicalRelationshipDAO")
	private IConceptHierarchicalRelationshipDAO conceptHierarchicalRelationshipDAO;

	@Inject
	@Named("skosAlignmentsBuilder")
	private AlignmentsBuilder alignmentsBuilder;

	@Inject
	@Named("skosTermBuilder")
	private TermBuilder termBuilder;

	@Inject
	@Named("conceptHierarchicalRelationshipServiceUtil")
	private IConceptHierarchicalRelationshipServiceUtil conceptHierarchicalRelationshipServiceUtil;

	/**
	 * Launch the calculation of the root concepts and set it
	 * 
	 * @param thesaurus
	 * @param skosConcepts
	 */
	public void buildConceptsRoot(Thesaurus thesaurus,
			List<Resource> skosConcepts) {
		for (Resource skosConcept : skosConcepts) {
			// Root calculation
			ThesaurusConcept concept = conceptBuilder.buildConceptRoot(
					skosConcept, thesaurus);
			thesaurusConceptDAO.update(concept);
		}
	}

	/**
	 * Builds the parent/child and relationship associations
	 * 
	 * @param thesaurus
	 * @param skosConcepts
	 */
	public void buildConceptsAssociations(Thesaurus thesaurus,
			List<Resource> skosConcepts, List<ObjectProperty> broaderTypes) {
		List<AssociativeRelationship> allRelations = new ArrayList<AssociativeRelationship>();
		for (Resource skosConcept : skosConcepts) {

			Map<ThesaurusConcept, List<ConceptHierarchicalRelationship>> relationsThesaurusConcept = conceptBuilder
					.buildConceptHierarchicalRelationships(skosConcept,
							thesaurus, broaderTypes);
			if (relationsThesaurusConcept.keySet().iterator().hasNext()) {
				ThesaurusConcept concept = relationsThesaurusConcept.keySet()
						.iterator().next();
				conceptHierarchicalRelationshipServiceUtil
						.saveHierarchicalRelationship(concept,
								relationsThesaurusConcept.get(concept),
								new ArrayList<ThesaurusConcept>());

				thesaurusConceptDAO.update(concept);
			}

			Set<AssociativeRelationship> associativeRelationships = conceptBuilder
					.buildConceptAssociativerelationship(skosConcept, thesaurus);
			for (AssociativeRelationship relation : associativeRelationships) {
				Boolean isRelationAdded = false;
				for (AssociativeRelationship existedRelation : allRelations) {
					if (existedRelation.getConceptLeft().equals(
							relation.getConceptRight())
							&& existedRelation.getConceptRight().equals(
									relation.getConceptLeft())
							|| existedRelation.equals(relation)) {
						isRelationAdded = true;
					}
				}
				if (!isRelationAdded) {
					allRelations.add(relation);
					associativeRelationshipDAO.update(relation);
				}
			}
		}
	}

	/**
	 * Builds the concept with minimal informations and it's terms and notes
	 * 
	 * @param thesaurus
	 * @param skosConcepts
	 */
	public Set<Alignment> buildConcepts(Thesaurus thesaurus,
			List<Resource> skosConcepts) {
		Set<Alignment> bannedAlignments = new HashSet<Alignment>();
		for (Resource skosConcept : skosConcepts) {
			// Minimal concept informations
			ThesaurusConcept concept = conceptBuilder.buildConcept(skosConcept,
					thesaurus);

			thesaurusConceptDAO.update(concept);
			ThesaurusTerm preferredTerm = null;

			// Concept terms
			List<ThesaurusTerm> terms = termBuilder.buildTerms(skosConcept,
					thesaurus, concept);
			for (ThesaurusTerm term : terms) {
				if (term.getPrefered())
					preferredTerm = term;
				thesaurusTermDAO.update(term);
			}

			// Concept notes
			List<Note> conceptNotes = conceptNoteBuilder.buildConceptNotes(
					skosConcept, concept, preferredTerm, thesaurus);
			for (Note conceptNote : conceptNotes) {
				noteDAO.update(conceptNote);
			}

			// Concept alignments
			List<Alignment> alignments = alignmentsBuilder.buildAlignments(
					skosConcept, concept);
			for (Alignment alignment : alignments) {
				if (alignment.getExternalTargetThesaurus() != null) {
					externalThesaurusDAO.update(alignment
							.getExternalTargetThesaurus());
				}
				if (alignment.getInternalTargetThesaurus() != null
						|| alignment.getExternalTargetThesaurus() != null) {
					for (AlignmentConcept alignmentConcept : alignment
							.getTargetConcepts()) {
						alignmentConceptDAO.update(alignmentConcept);
					}
					alignmentDAO.update(alignment);
				} else {
					bannedAlignments.add(alignment);
				}

			}
		}
		return bannedAlignments;
	}

}
