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

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;

/**
 * builder in charge of building thesaurus arrays
 * 
 */
@Service("skosArrayBuilder")
public class ThesaurusArrayBuilder extends AbstractBuilder {

	@Log
	private Logger logger;

	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	public ThesaurusArrayBuilder() {
		super();
	}

	/**
	 * Builds a ThesaurusArray from the given resource
	 * 
	 * @param skosCollection
	 * @param model
	 * @param thesaurus
	 * @return
	 * @throws BusinessException
	 */
	public ThesaurusArray buildArray(Resource skosCollection, Model model,
			Thesaurus thesaurus) throws BusinessException {

		ThesaurusArray array = new ThesaurusArray();
		array.setIdentifier(skosCollection.getURI());
		array.setThesaurus(thesaurus);

		StmtIterator stmtMembersItr = skosCollection
				.listProperties(SKOS.MEMBER);
		Set<ThesaurusConcept> membersConcepts = new HashSet<ThesaurusConcept>();
		while (stmtMembersItr.hasNext()) {
			Statement stmt = stmtMembersItr.next();
			Resource memberConceptRes = stmt.getObject().asResource();
			String relatedURI = memberConceptRes.getURI();
			ThesaurusConcept memberConcept = thesaurusConceptDAO
					.getById(relatedURI);
			membersConcepts.add(memberConcept);
		}
		array.setConcepts(membersConcepts);

		array.setSuperOrdinateConcept(getSuperOrdinate(membersConcepts));

		array.setOrdered(false);

		return array;
	}

	/**
	 * Calculates the common parent of a list of ThesaurusConcept
	 * 
	 * @param membersConcepts
	 * @return
	 * @throws BusinessException
	 *             if more than one parent is found, or if no parent is found
	 *             and the concepts are not root concepts
	 */
	private ThesaurusConcept getSuperOrdinate(
			Set<ThesaurusConcept> membersConcepts) throws BusinessException {
		Set<ThesaurusConcept> result = new HashSet<ThesaurusConcept>();
		boolean allRoot = true;
		for (ThesaurusConcept thesaurusConcept : membersConcepts) {
			if (!thesaurusConcept.getParentConcepts().isEmpty()) {
				allRoot = false;
				Set<ThesaurusConcept> parentConcepts = thesaurusConcept
						.getParentConcepts();
				if (result.isEmpty()) {
					result.addAll(parentConcepts);
				} else {
					result.retainAll(parentConcepts);
				}
			}
		}
		if (result.isEmpty() && !allRoot) {
			throw new BusinessException(
					"Unable to find thesaurus array concepts common parent",
					"import-no-superordinate");
		} else if (result.isEmpty() && allRoot) {
			logger.debug("Root concept constituting the array");
			return null;
		} else if (result.size() > 1) {
			throw new BusinessException(
					"Unable to get thesaurus array concepts common parent",
					"import-too-many-superordinate");
		}

		return result.iterator().next();
	}

}
