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
package fr.mcc.ginco.extjs.view.utils;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentConcept;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.AlignmentConceptView;
import fr.mcc.ginco.services.IThesaurusConceptService;

/**
 *
 */
@Component("alignmentConceptViewConverter")
public class AlignmentConceptViewConverter {
	
	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	private Logger logger = LoggerFactory.getLogger(AlignmentConceptViewConverter.class);
	/**
	 * convert an Alignment object to an AlignmentView suitable for display
	 * 
	 * @param alignment
	 * @return
	 */
	public AlignmentConceptView convertAlignmentConcept(
			AlignmentConcept alignmentConcept) {
		AlignmentConceptView view = new AlignmentConceptView();

		view.setIdentifier(alignmentConcept.getIdentifier());
		if (alignmentConcept.getInternalTargetConcept() != null) {
			view.setInternalTargetConcept(alignmentConcept
					.getInternalTargetConcept().getIdentifier());
		}
		view.setExternalTargetConcept(alignmentConcept
				.getExternalTargetConcept());

		return view;
	}

	/**
	 * This method convert the view of an alignment to the {@link Alignment}
	 * object
	 * 
	 * @param alignmentView
	 * @param convertedConcept
	 * @return
	 */
	public AlignmentConcept convertAlignmentConceptView(
			AlignmentConceptView alignmentConceptView, Alignment alignment) {
		logger.debug("AlignmentConceptView to store : " + alignmentConceptView.toString());
		AlignmentConcept alignmentConcept = new AlignmentConcept();
		alignmentConcept.setAlignment(alignment);
		
		if (StringUtils.isNotEmpty(alignmentConceptView.getExternalTargetConcept())) {
			alignmentConcept.setExternalTargetConcept(alignmentConceptView.getExternalTargetConcept());
		}
		if (StringUtils.isNotEmpty(alignmentConceptView.getInternalTargetConcept())) {
			ThesaurusConcept target = thesaurusConceptService.getThesaurusConceptById(alignmentConceptView.getInternalTargetConcept().trim());
			if (target == null) {
				throw new BusinessException("Alignment internal target concept does not exist",
						"missing-alignment-internal-target-concept", new Object[]{alignmentConceptView.getInternalTargetConcept()});
			}
			
			alignmentConcept.setInternalTargetConcept(target);
		}
		return alignmentConcept;
	}
}
