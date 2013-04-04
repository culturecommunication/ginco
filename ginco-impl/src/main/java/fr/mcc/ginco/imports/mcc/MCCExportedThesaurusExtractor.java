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
package fr.mcc.ginco.imports.mcc;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.beans.ThesaurusVersionHistory;
import fr.mcc.ginco.dao.INodeLabelDAO;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusTermDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exports.result.bean.MCCExportedThesaurus;

/**
 * This class extracts data from a {@link MCCExportedThesaurus} object
 * and stores it in beans.
 * It returns a {@link Thesaurus} object corresponding to the imported thesaurus
 *
 */
@Component("mccExportedThesaurusExtractor")
public class MCCExportedThesaurusExtractor {
	
	@Inject
	@Named("thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;
	
	@Inject
	@Named("thesaurusTermDAO")
	private IThesaurusTermDAO thesaurusTermDAO;
	
	@Inject
	@Named("thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;
	
	@Inject
	@Named("nodeLabelDAO")
	private INodeLabelDAO nodeLabelDAO;
	
	public Thesaurus storeMccExportedThesaurus(MCCExportedThesaurus exportedThesaurus) {
		Thesaurus thesaurus = thesaurusDAO.update(exportedThesaurus.getThesaurus());
		storeTerms(exportedThesaurus);
		//storeArraysAndLabels(exportedThesaurus);
		//storeVersions(exportedThesaurus);
		
		return thesaurus;
	}
	
	public List<ThesaurusTerm> storeTerms(MCCExportedThesaurus exportedThesaurus) {
		List<ThesaurusTerm> updatedTerms = new ArrayList<ThesaurusTerm>();
		for (ThesaurusTerm term : exportedThesaurus.getTerms()) {
			term.setThesaurus(thesaurusDAO.getById(exportedThesaurus.getThesaurus().getIdentifier()));
			updatedTerms.add(thesaurusTermDAO.update(term));
		}
		return updatedTerms;
	}
	
	/*public List<NodeLabel> storeArraysAndLabels(MCCExportedThesaurus exportedThesaurus) {
		List<NodeLabel> updatedLabelsAndArrays = new ArrayList<NodeLabel>();
		for (NodeLabel node : exportedThesaurus.getConceptsArrayLabels()) {
			node.getThesaurusArray().setThesaurus(thesaurusDAO.getById(exportedThesaurus.getThesaurus().getIdentifier()));
			updatedLabelsAndArrays.add(nodeLabelDAO.update(node));
		}
		return updatedLabelsAndArrays;
	}*/
	
	public List<ThesaurusVersionHistory> storeVersions(MCCExportedThesaurus exportedThesaurus) {
		List<ThesaurusVersionHistory> updatedVersion = new ArrayList<ThesaurusVersionHistory>();
		for (ThesaurusVersionHistory version : exportedThesaurus.getThesaurusVersions()) {
			updatedVersion.add(thesaurusVersionHistoryDAO.update(version));
		}
		return updatedVersion;
	}
	
}
