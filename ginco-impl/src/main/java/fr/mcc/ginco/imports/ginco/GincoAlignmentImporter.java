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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import fr.mcc.ginco.beans.*;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.dao.IAlignmentDAO;
import fr.mcc.ginco.dao.IExternalThesaurusDAO;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exports.result.bean.JaxbList;

/**
 * This class gives methods to import alignments
 */
@Component("gincoAlignmentImporter")
public class GincoAlignmentImporter {

	@Inject
	private IAlignmentDAO alignmentDAO;

	@Inject
	private IGenericDAO<AlignmentConcept, Integer> alignmentConceptDAO;

	@Inject
	private IGenericDAO<AlignmentResource, Integer> alignmentResourceDAO;

	@Inject
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Inject
	private IExternalThesaurusDAO externalThesaurusDAO;

	/**
	 * This method stores all the alignments
	 *
	 * @param alignments
	 */
	public Set<Alignment> storeAlignments(
			Map<String, JaxbList<Alignment>> alignments) {
		Map<Alignment, List<AlignmentConcept>> missingInternalconcepts = new HashMap<Alignment, List<AlignmentConcept>>();

		for (String conceptKey : alignments.keySet()) {
			JaxbList<Alignment> alignmentImported = alignments.get(conceptKey);
			for (Alignment ali : alignmentImported.getList()) {
				AlignmentType alignmentType = ali.getAlignmentType();
				if(alignmentType.isResource()){
					Set<AlignmentResource> resources = ali.getTargetResources();
					for (AlignmentResource alignmentResource : resources) {
						alignmentResource.setAlignment(ali);
						alignmentResourceDAO.update(alignmentResource);
					}
					alignmentDAO.update(ali);
				}else {
					Set<AlignmentConcept> concepts = ali.getTargetConcepts();
					List<ThesaurusConcept> existingInternalConcepts = new ArrayList<ThesaurusConcept>();
					for (AlignmentConcept alignmentConcept : concepts) {
						if (alignmentConcept.getInternalTargetConcept() != null) {
							ThesaurusConcept existingTarget = thesaurusConceptDAO
									.getById(alignmentConcept
											.getInternalTargetConcept()
											.getIdentifier());
							if (existingTarget == null) {
								if (missingInternalconcepts.containsKey(ali)) {
									List<AlignmentConcept> aliConcepts = missingInternalconcepts
											.get(ali);
									aliConcepts.add(alignmentConcept);
									missingInternalconcepts.put(ali, aliConcepts);

								} else {
									List<AlignmentConcept> aliConcepts = new ArrayList<AlignmentConcept>();
									aliConcepts.add(alignmentConcept);
									missingInternalconcepts.put(ali, aliConcepts);
								}

							} else {
								existingInternalConcepts.add(existingTarget);
								alignmentConcept
										.setInternalTargetConcept(existingTarget);
								ali.setInternalTargetThesaurus(existingTarget.getThesaurus());
								alignmentConcept.setAlignment(ali);
								alignmentConceptDAO.update(alignmentConcept);
							}
						} else {
							alignmentConcept.setAlignment(ali);
							alignmentConceptDAO.update(alignmentConcept);
						}
					}
					if (!missingInternalconcepts.containsKey(ali)) {
						alignmentDAO.update(ali);
					}
				}
			}
		}
		return missingInternalconcepts.keySet();
	}

	public void storeExternalThesauruses(
			Map<String, JaxbList<Alignment>> alignments,
			Set<Alignment> bannedAlignments) {
		List<ExternalThesaurus> externalThesaurusesToSave = new ArrayList<ExternalThesaurus>();

		for (String conceptKey : alignments.keySet()) {
			JaxbList<Alignment> alignmentImported = alignments.get(conceptKey);
			for (Alignment ali : alignmentImported.getList()) {
				if (!bannedAlignments.contains(ali)) {
					ExternalThesaurus externalThesaurus = ali
							.getExternalTargetThesaurus();
					if (externalThesaurus != null) {
						ExternalThesaurus existingThesaurus = externalThesaurusDAO
								.findBySourceExternalId(externalThesaurus
										.getExternalId());
						if (existingThesaurus != null) {
							ali.setExternalTargetThesaurus(existingThesaurus);
						} else {
							if (!externalThesaurusesToSave.contains(externalThesaurus)) {
								externalThesaurusesToSave.add(externalThesaurus);
							} else {
								ali.setExternalTargetThesaurus(externalThesaurusesToSave.get(externalThesaurusesToSave.indexOf(externalThesaurus)));
							}
						}
					}
				}
			}

		}

		for (ExternalThesaurus extThesaurus : externalThesaurusesToSave) {
			externalThesaurusDAO.update(extThesaurus);
		}

	}

}
