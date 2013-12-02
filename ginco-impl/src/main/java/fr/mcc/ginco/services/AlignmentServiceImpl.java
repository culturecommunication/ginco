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
package fr.mcc.ginco.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentConcept;
import fr.mcc.ginco.beans.ExternalThesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.IAlignmentDAO;
import fr.mcc.ginco.dao.IExternalThesaurusDAO;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.exceptions.BusinessException;

//@Transactional(readOnly = true, rollbackFor = BusinessException.class)
@Service("alignmentService")
public class AlignmentServiceImpl implements IAlignmentService {

	@Inject
	private IAlignmentDAO alignmentDAO;

	@Inject
	private IGenericDAO<AlignmentConcept, Integer> alignmentConceptDAO;

	@Inject
	private IExternalThesaurusDAO externalThesaurusDAO;

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;	

	@Override
	public Alignment getAlignmentById(String identifier) {
		return alignmentDAO.getById(identifier);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.mcc.ginco.services.IAlignmentService#getAlignmentsBySourceConceptId
	 * (java.lang.String)
	 */
	@Override
	public List<Alignment> getAlignmentsBySourceConceptId(
			String conceptIdentifier) {
		return alignmentDAO.findBySourceConceptId(conceptIdentifier);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.mcc.ginco.services.IAlignmentService#saveAlignments(fr.mcc.ginco.beans
	 * .ThesaurusConcept, java.util.List)
	 */
	public ThesaurusConcept saveAlignments(ThesaurusConcept concept,
			List<Alignment> alignments) {

		List<Alignment> existingAlignments  = alignmentDAO.findBySourceConceptId(concept.getIdentifier());

		for (Alignment existingAlignment:existingAlignments) {
			if (!alignments.contains(existingAlignment)) {
				Set<AlignmentConcept> concepts  = existingAlignment.getTargetConcepts();
				for (AlignmentConcept alignmentConcept: concepts) {
					alignmentConceptDAO.delete(alignmentConcept);
				}
				alignmentDAO.delete(existingAlignment);
			}
		}
		for (Alignment alignment : alignments) {
			if (StringUtils.isEmpty(alignment.getIdentifier())) {
				alignment.setIdentifier(generatorService
						.generate(Alignment.class));
			}
			Set<AlignmentConcept> concepts  = alignment.getTargetConcepts();
			for (AlignmentConcept alignmentConcept: concepts) {
				alignmentConceptDAO.update(alignmentConcept);
			}

			alignmentDAO.update(alignment);
		}

		return concept;
	}

	public void saveExternalThesauruses(List<Alignment> alignments) {
		List<ExternalThesaurus> externalThesaurusesToSave = new ArrayList<ExternalThesaurus>();

		for (Alignment alignment:alignments) {
			ExternalThesaurus externalThesaurus = alignment.getExternalTargetThesaurus();

			if (externalThesaurus != null) {
				if (! externalThesaurusesToSave.contains(externalThesaurus)) {
					externalThesaurusesToSave.add(externalThesaurus);
				} else 
				{
					ExternalThesaurus existingExternalThes = externalThesaurusesToSave.get(externalThesaurusesToSave.indexOf(externalThesaurus));
					alignment.setExternalTargetThesaurus(existingExternalThes);
				}
			}
		}
		for (ExternalThesaurus extThesaurus:externalThesaurusesToSave) {
			externalThesaurusDAO.update(extThesaurus);
		}
	}

}
