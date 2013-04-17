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
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.log.Log;

/**
 * This class :
 * - extracts data from a {@link GincoExportedThesaurus} object,
 * - stores it in beans
 * - persists beans
 *
 */
@Component("gincoThesaurusBuilder")
public class GincoThesaurusBuilder {

	@Inject
	@Named("gincoArrayImporter")
	private GincoArrayImporter gincoArrayImporter;
	
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

	@Inject
	@Named("thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;
	
	@Log
	private Logger logger;
	
	/**
	 * This method stores a Ginco Thesaurus with all its objects (concepts, terms, arrays, groups etc.).
	 * 
	 * The order of the import is important :
	 * - Thesaurus, concepts, terms, arrays, arrayslabels, groups, grouplabels, thesaurus versions,
	 * hierarchical relationship, associative relationship, term notes and concepts notes
	 * 
	 * @param {@GincoExportedThesaurus} : the previously exported thesaurus we want to import
	 * @return The imported {@link Thesaurus}
	 */
	public Thesaurus storeGincoExportedThesaurus(GincoExportedThesaurus exportedThesaurus) {
		Thesaurus thesaurus = new Thesaurus();
		if (exportedThesaurus.getThesaurus() != null) {
			if (thesaurusDAO.getById(exportedThesaurus.getThesaurus().getIdentifier()) == null) {
				thesaurus = thesaurusDAO.update(exportedThesaurus.getThesaurus());
			} else {
				throw new BusinessException(
						"Trying to import an existing thesaurus", "import-already-existing-thesaurus");
			}
			gincoConceptImporter.storeConcepts(exportedThesaurus.getConcepts(), exportedThesaurus.getThesaurus());
			gincoTermImporter.storeTerms(exportedThesaurus.getTerms(), exportedThesaurus.getThesaurus());
			
			gincoArrayImporter.storeArrays(exportedThesaurus);
			gincoArrayImporter.storeArrayLabels(exportedThesaurus);
			
			gincoGroupImporter.storeGroups(exportedThesaurus);
			gincoGroupImporter.storeGroupLabels(exportedThesaurus);
			
			storeVersions(exportedThesaurus);
			
			gincoRelationshipImporter.storeHierarchicalRelationship(exportedThesaurus.getHierarchicalRelationship());
			gincoRelationshipImporter.storeAssociativeRelationship(exportedThesaurus);
			
			gincoTermImporter.storeTermNotes(exportedThesaurus.getTermNotes());
			gincoConceptImporter.storeConceptNotes(exportedThesaurus.getConceptNotes());
		}
		return thesaurus;
	}
		
	/**
	 * This method stores all the versions of the thesaurus included in the {@link GincoExportedThesaurus} object given in parameter
	 * @param exportedThesaurus
	 * @return The list of stored versions
	 */
	public List<ThesaurusVersionHistory> storeVersions(GincoExportedThesaurus exportedThesaurus) {
		List<ThesaurusVersionHistory> updatedVersion = new ArrayList<ThesaurusVersionHistory>();
		for (ThesaurusVersionHistory version : exportedThesaurus.getThesaurusVersions()) {
			version.setThesaurus(exportedThesaurus.getThesaurus());
			updatedVersion.add(thesaurusVersionHistoryDAO.update(version));
		}
		return updatedVersion;
	}
}
