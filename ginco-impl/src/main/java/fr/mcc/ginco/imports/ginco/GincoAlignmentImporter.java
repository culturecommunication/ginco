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
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentConcept;
import fr.mcc.ginco.beans.ExternalThesaurus;
import fr.mcc.ginco.dao.IAlignmentDAO;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.log.Log;

/**
 * This class gives methods to import alignments
 * 
 */
@Component("gincoAlignmentImporter")
public class GincoAlignmentImporter {

	@Inject
	@Named("alignmentDAO")
	private IAlignmentDAO alignmentDAO;

	@Inject
	@Named("alignmentConceptDAO")
	private IGenericDAO<AlignmentConcept, Integer> alignmentConceptDAO;

	@Inject
	@Named("externalThesaurusDAO")
	private IGenericDAO<ExternalThesaurus, String> externalThesaurusDAO;

	@Inject
	@Named("thesaurusConceptDAO")
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Log
	private Logger logger;

	/**
	 * This method stores all the alignments
	 * 
	 * @param alignments
	 */
	public void storeAlignments(Map<String, JaxbList<Alignment>> alignments) {
		List<ExternalThesaurus> externalThesaurusesToSave = new ArrayList<ExternalThesaurus>();

		for (String conceptKey : alignments.keySet()) {
			JaxbList<Alignment> alignmentImported = alignments.get(conceptKey);
			for (Alignment ali : alignmentImported.getList()) {
				ExternalThesaurus externalThesaurus = ali
						.getExternalTargetThesaurus();
				if (externalThesaurus != null
						&& !externalThesaurusesToSave
								.contains(externalThesaurus)) {
					//TODO : check if this external thesaurus already exists
					externalThesaurusesToSave.add(externalThesaurus);
				}
			}

		}

		for (ExternalThesaurus extThesaurus : externalThesaurusesToSave) {
			externalThesaurusDAO.update(extThesaurus);
		}

		for (String conceptKey : alignments.keySet()) {
			JaxbList<Alignment> alignmentImported = alignments.get(conceptKey);
			for (Alignment ali : alignmentImported.getList()) {				
				Set<AlignmentConcept> concepts = ali.getTargetConcepts();
				for (AlignmentConcept alignmentConcept : concepts) {
					//TODO : if internal concept, check if the target concept exists
					// if no, list the error to the user
					//otherwise fill in the internal target thesaurus
					alignmentConcept.setAlignment(ali);
					alignmentConceptDAO.update(alignmentConcept);
				}
				ali.setSourceConcept(thesaurusConceptDAO.getById(conceptKey));
				alignmentDAO.update(ali);
			}
		}

	}

}
