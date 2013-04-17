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
package fr.mcc.ginco.imports.ginco;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.GincoExportedBranch;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoConceptBranchIdGenerator;
import fr.mcc.ginco.log.Log;

/**
 * This class : - extracts data from a {@link GincoExportedBranch} object, -
 * stores it in beans - persists beans
 * 
 */
@Component("gincoConceptBranchBuilder")
public class GincoConceptBranchBuilder {

	@Inject
	@Named("gincoArrayImporter")
	private GincoArrayImporter gincoArrayImporter;

	@Inject
	@Named("gincoConceptBranchIdGenerator")
	private GincoConceptBranchIdGenerator gincoConceptBranchIdGenerator;

	@Inject
	@Named("gincoGroupImporter")
	private GincoGroupImporter gincoGroupImporter;

	@Inject
	@Named("gincoConceptImporter")
	private GincoConceptImporter gincoConceptImporter;

	@Inject
	@Named("gincoRelationshipImporter")
	private GincoRelationshipImporter gincoRelationshipImporter;

	@Inject
	@Named("gincoTermImporter")
	private GincoTermImporter gincoTermImporter;

	@Inject
	@Named("thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

	@Log
	private Logger logger;

	/**
	 * This method stores a Ginco Concept branch with all its objects (concept
	 * children, terms, notes) but not arrays / groups.
	 * 
	 * @param {@GincoExportedBranch} : the previously
	 *        exported branch we want to import
	 * @return The imported {@link ThesaurusConcept} root
	 */
	public ThesaurusConcept storeGincoExportedBranch(
			GincoExportedBranch exportedBranch, String thesaurusId) {
		ThesaurusConcept result = new ThesaurusConcept();
		Thesaurus targetedThesaurus = thesaurusDAO.getById(thesaurusId);

		if (targetedThesaurus == null) {
			throw new BusinessException("Unknown thesaurus",
					"unknown-thesaurus");
		} else {

			// We replace all existing ids by new generated ids
			gincoConceptBranchIdGenerator
					.checkIdsForExportedBranch(exportedBranch);

			// We import the concept branch in specified thesaurus
			List<ThesaurusConcept> rootConcept = new ArrayList<ThesaurusConcept>();
			exportedBranch.getRootConcept().setTopConcept(false);
			rootConcept.add(exportedBranch.getRootConcept());

			List<ThesaurusConcept> resultOfStore = gincoConceptImporter
					.storeConcepts(rootConcept, targetedThesaurus);
			if (resultOfStore != null && !resultOfStore.isEmpty()) {
				result = resultOfStore.get(0);
			}

			gincoConceptImporter.storeConcepts(exportedBranch.getConcepts(),
					targetedThesaurus);
			gincoTermImporter.storeTerms(exportedBranch.getTerms(),
					targetedThesaurus);
			gincoConceptImporter.storeConceptNotes(exportedBranch
					.getConceptNotes());
			gincoTermImporter.storeTermNotes(exportedBranch.getTermNotes());
			gincoRelationshipImporter
					.storeHierarchicalRelationship(exportedBranch
							.getHierarchicalRelationship());
		}
		return result;
	}
}
