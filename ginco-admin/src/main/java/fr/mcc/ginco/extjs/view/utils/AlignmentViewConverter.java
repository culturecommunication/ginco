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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import fr.mcc.ginco.beans.*;
import fr.mcc.ginco.extjs.view.pojo.AlignmentResourceView;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.AlignmentConceptView;
import fr.mcc.ginco.extjs.view.pojo.AlignmentView;
import fr.mcc.ginco.extjs.view.pojo.ExternalThesaurusView;
import fr.mcc.ginco.services.IAlignmentService;
import fr.mcc.ginco.services.IAlignmentTypeService;
import fr.mcc.ginco.utils.DateUtil;

/**
 *
 */
@Component("alignmentViewConverter")
public class AlignmentViewConverter {

	@Inject
	@Named("alignmentService")
	private IAlignmentService alignmentService;

	@Inject
	@Named("alignmentTypeService")
	private IAlignmentTypeService alignmentTypeService;

	@Inject
	@Named("alignmentConceptViewConverter")
	private AlignmentConceptViewConverter alignmentConceptViewConverter;

	@Inject
	@Named("alignmentResourceViewConverter")
	private AlignmentResourceViewConverter alignmentResourceViewConverter;

	@Inject
	@Named("externalThesaurusViewConverter")
	private ExternalThesaurusViewConverter externalThesaurusViewConverter;

	private Logger logger = LoggerFactory.getLogger(AlignmentViewConverter.class);

	/**
	 * convert an Alignment object to an AlignmentView suitable for display
	 *
	 * @param alignment
	 * @return
	 */
	public AlignmentView convertAlignment(Alignment alignment) {
		AlignmentView view = new AlignmentView();
		view.setAlignmentType(alignment.getAlignmentType().getIdentifier());
		view.setAndRelation(alignment.isAndRelation());
		view.setCreated(DateUtil.toString(alignment.getCreated()));
		if (alignment.getExternalTargetThesaurus() != null) {
			List<ExternalThesaurusView> externalThesauruses = new ArrayList<ExternalThesaurusView>();
			externalThesauruses.add(externalThesaurusViewConverter.convertExternalThesaurus(alignment.getExternalTargetThesaurus()));
			view.setExternalThesaurus(externalThesauruses);
		}
		view.setIdentifier(alignment.getIdentifier());
		if (alignment.getInternalTargetThesaurus() != null) {
			view.setInternalThesaurusId(alignment.getInternalTargetThesaurus()
					.getIdentifier());
		}
		view.setModified(DateUtil.toString(alignment.getModified()));
		Set<AlignmentConcept> targetConcepts = alignment.getTargetConcepts();
		List<AlignmentConceptView> targetInternalConcepts = new ArrayList<AlignmentConceptView>();
		logger.debug("Found " + targetConcepts.size()
				+ " target concepts for alignment " + alignment.getIdentifier());
		for (AlignmentConcept targetConcept : targetConcepts) {
			AlignmentConceptView alignmentConceptView = new AlignmentConceptView();
			alignmentConceptView.setIdentifier(targetConcept.getIdentifier());
			if (targetConcept.getInternalTargetConcept() != null) {
				alignmentConceptView.setInternalTargetConcept(targetConcept
						.getInternalTargetConcept().getIdentifier());
			}
			alignmentConceptView.setExternalTargetConcept(targetConcept
					.getExternalTargetConcept());

			targetInternalConcepts.add(alignmentConceptView);
		}
		view.setTargetConcepts(targetInternalConcepts);
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
	public Alignment convertAlignmentView(AlignmentView alignmentView,
	                                      ThesaurusConcept convertedConcept) {
		Alignment alignment;

		if (StringUtils.isEmpty(alignmentView.getIdentifier())) {
			alignment = new Alignment();
			alignment.setCreated(DateUtil.nowDate());
			logger.info("Creating a new alignment");
		} else {
			alignment = alignmentService.getAlignmentById(alignmentView
					.getIdentifier());
			logger.info("Getting an existing alignment");
		}

		alignment.setAlignmentType(alignmentTypeService
				.getAlignmentTypeById(alignmentView.getAlignmentType()));
		alignment.setAndRelation(alignmentView.getAndRelation());

		if (alignmentView.getExternalThesaurus() != null
				&& alignmentView.getExternalThesaurus().size() > 0
				&& StringUtils.isNotEmpty(alignmentView.getExternalThesaurus().get(0).getExternalId())) {

			alignment.setExternalTargetThesaurus(externalThesaurusViewConverter
					.convertExternalThesaurusView(alignmentView
							.getExternalThesaurus().get(0)));
		}
		alignment.setModified(DateUtil.nowDate());
		alignment.setSourceConcept(convertedConcept);

		Set<AlignmentConcept> targets = new HashSet<AlignmentConcept>();
		Set<AlignmentResource> resourcesTargets = new HashSet<AlignmentResource>();
		if (alignmentView.getTargetConcepts() != null
				&& !alignmentView.getTargetConcepts().isEmpty()) {
			String targetInternalThesaurusId = "";
			for (AlignmentConceptView alignmentConceptview : alignmentView
					.getTargetConcepts()) {
				logger.debug("Found "
						+ alignmentView.getTargetConcepts().size()
						+ " target concepts view to convert");

				AlignmentConcept target = alignmentConceptViewConverter
						.convertAlignmentConceptView(alignmentConceptview,
								alignment);
				targets.add(target);

				if (StringUtils.isEmpty(targetInternalThesaurusId)
						&& target.getInternalTargetConcept() != null) {
					targetInternalThesaurusId = target
							.getInternalTargetConcept().getThesaurusId();
				} else if (target.getInternalTargetConcept() != null
						&& !targetInternalThesaurusId.equals(target
						.getInternalTargetConcept().getThesaurusId())) {
					throw new BusinessException(
							"Internal target concepts not in the same thesaurus",
							"no-unique-thesaurus-for-internal-target-concepts");
				}
			}
		} else if(alignmentView.getTargetResources() != null && !alignmentView.getTargetResources().isEmpty()){

			for (AlignmentResourceView alignmentResourceview : alignmentView.getTargetResources()) {
				logger.debug("Found "
						+ alignmentView.getTargetResources().size()
						+ " target resources view to convert");

				AlignmentResource target = alignmentResourceViewConverter.convertAlignmentResourceView(alignmentResourceview,alignment);
				resourcesTargets.add(target);
			}

		} else{
			throw new BusinessException("Missing target concept for alignment",
					"missing-target-concepts-for-alignment");
		}

		if(targets != null  && !targets.isEmpty()){
			AlignmentConcept firstTarget = targets.iterator().next();
			if (firstTarget.getInternalTargetConcept() != null) {
				Thesaurus internalTargetThesaurus = firstTarget
						.getInternalTargetConcept().getThesaurus();
				alignment.setInternalTargetThesaurus(internalTargetThesaurus);
			}
			alignment.setTargetConcepts(targets);
		}else if(resourcesTargets != null && !resourcesTargets.isEmpty()){
			alignment.setTargetResources(resourcesTargets);
		}

		return alignment;
	}
}
