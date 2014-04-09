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

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayConceptView;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.utils.DateUtil;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Small class responsible for converting real {@link fr.mcc.ginco.beans.ThesaurusArray} object
 * into its view {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView}.
 */
@Component("thesaurusArrayViewConverter")
public class ThesaurusArrayViewConverter {
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Inject
	@Named("thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("nodeLabelService")
	private INodeLabelService nodeLabelService;

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;

	/**
	 * Convert from {@link fr.mcc.ginco.extjs.view.pojo.ThesaurusArrayView}
	 * to {@link fr.mcc.ginco.beans.ThesaurusArray}
	 *
	 * @param source
	 * @return
	 */
	public ThesaurusArray convert(ThesaurusArrayView source) {
		ThesaurusArray hibernateRes;
		if (StringUtils.isEmpty(source.getIdentifier())) {
			hibernateRes = new ThesaurusArray();
			hibernateRes.setIdentifier(generatorService
					.generate(ThesaurusArray.class));

		} else {
			hibernateRes = thesaurusArrayService.getThesaurusArrayById(source
					.getIdentifier());
		}
		if (StringUtils.isEmpty(source.getThesaurusId())) {
			throw new BusinessException(
					"ThesaurusId is mandatory to save a concept",
					"mandatory-thesaurus");
		} else {
			Thesaurus thesaurus = thesaurusService.getThesaurusById(source
					.getThesaurusId());
			hibernateRes.setThesaurus(thesaurus);
		}

		if (StringUtils.isNotEmpty(source.getSuperOrdinateId())) {
			hibernateRes.setSuperOrdinateConcept(thesaurusConceptService
					.getThesaurusConceptById(source.getSuperOrdinateId()));
		} else {
			hibernateRes.setSuperOrdinateConcept(null);
		}

		hibernateRes.setOrdered(source.getOrder());

		if (source.getParentArrayId() != null) {
			hibernateRes.setParent(thesaurusArrayService.getThesaurusArrayById(source.getParentArrayId()));
		}

		return hibernateRes;
	}

	/**
	 * Convert from {@link ThesaurusArray}
	 * to {@link ThesaurusArrayView}
	 *
	 * @param source
	 * @return
	 */
	public ThesaurusArrayView convert(final ThesaurusArray source) {
		ThesaurusArrayView thesaurusArrayView = new ThesaurusArrayView();

		thesaurusArrayView.setIdentifier(source.getIdentifier());

		if (source.getSuperOrdinateConcept() != null) {
			thesaurusArrayView.setSuperOrdinateId(source
					.getSuperOrdinateConcept().getIdentifier());
			thesaurusArrayView.setSuperOrdinateConceptLabel(thesaurusConceptService
					.getConceptLabel(source.getSuperOrdinateConcept()
							.getIdentifier()));

		}

		List<ThesaurusArrayConceptView> arrayConcepts = new ArrayList<ThesaurusArrayConceptView>();
		for (ThesaurusArrayConcept arrayConcept : source.getConcepts()) {
			ThesaurusArrayConceptView conceptView = new ThesaurusArrayConceptView();
			conceptView.setIdentifier(arrayConcept.getIdentifier()
					.getConceptId());
			conceptView.setOrder(arrayConcept.getArrayOrder());
			conceptView.setLabel((thesaurusConceptService
					.getConceptLabel(arrayConcept.getIdentifier()
							.getConceptId())));
			arrayConcepts.add(conceptView);
		}
		thesaurusArrayView.setConcepts(arrayConcepts);

		NodeLabel label = nodeLabelService
				.getByThesaurusArrayAndLanguage(source.getIdentifier());

		thesaurusArrayView.setLabel(label.getLexicalValue());
		thesaurusArrayView.setLanguage(label.getLanguage().getId());
		thesaurusArrayView.setNodeLabelId(label.getIdentifier());
		thesaurusArrayView.setCreated(DateUtil.toString(label.getCreated()));
		thesaurusArrayView.setModified(DateUtil.toString(label.getModified()));
		thesaurusArrayView.setThesaurusId(source.getThesaurus()
				.getThesaurusId());
		thesaurusArrayView.setOrder(source.getOrdered());

		if (source.getParent() != null) {
			//We set id and label of parent array
			thesaurusArrayView.setParentArrayId(source.getParent().getIdentifier());
			NodeLabel labelOfParent = nodeLabelService.getByThesaurusArray(source.getParent().getIdentifier());
			thesaurusArrayView.setParentArrayLabel(labelOfParent.getLexicalValue());
		}

		return thesaurusArrayView;
	}

	/**
	 * This method converts a list of {@link ThesaurusArray} into a list of
	 * {@link ThesaurusArrayView}
	 *
	 * @param {@link ThesaurusArray} arrays
	 * @return {@link ThesaurusArrayView} array views
	 */
	public List<ThesaurusArrayView> convert(List<ThesaurusArray> arrays) {
		List<ThesaurusArrayView> returnedArrayViews = new ArrayList<ThesaurusArrayView>();
		for (ThesaurusArray thesaurusArray : arrays) {
			returnedArrayViews.add(convert(thesaurusArray));
		}
		return returnedArrayViews;
	}
}