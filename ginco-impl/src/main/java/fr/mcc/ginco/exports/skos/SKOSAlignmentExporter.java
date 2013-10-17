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
package fr.mcc.ginco.exports.skos;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.semanticweb.skos.AddAssertion;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataRelationAssertion;
import org.semanticweb.skos.SKOSDataset;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.services.IAlignmentService;

/**
 * This component is in charge of exporting concept alignments to SKOS
 *
 */

@Component("skosAlignmentExporter")
public class SKOSAlignmentExporter {

	@Inject
	@Named("alignmentService")
	private IAlignmentService alignmentService;

	/**
	 * Export concept alignments to SKOS using the skos API
	 *
	 * @param conceptId
	 * @param factory
	 * @param conceptSKOS
	 * @param vocab
	 *
	 * @return
	 */
	public List<SKOSChange> exportAlignments(String conceptId, SKOSDataFactory factory,
			SKOSConcept conceptSKOS, SKOSDataset vocab) {
		List<SKOSChange> addList = new ArrayList<SKOSChange>();
		List<Alignment> alignments = alignmentService.getAlignmentsBySourceConceptId(conceptId);
		for (Alignment alignment : alignments){
			ThesaurusConcept internalTargetConcept = alignment.getTargetConcepts().iterator().next()
					.getInternalTargetConcept();
			String targetConceptId = "";
			if (internalTargetConcept != null){
				targetConceptId = internalTargetConcept.getIdentifier();
			} else {
				targetConceptId = alignment.getTargetConcepts().iterator().next()
						.getExternalTargetConcept();
			}

			String alignmentType = alignment.getAlignmentType().getIsoCode();
			if ("=EQ".equals(alignmentType)){
				SKOSDataRelationAssertion alignmentAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSExactMatchProperty().getURI()), targetConceptId);
				addList.add(new AddAssertion(vocab, alignmentAssertion));
			} else if ("~EQ".equals(alignmentType)) {
				SKOSDataRelationAssertion alignmentAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSCloseMatchProperty().getURI()), targetConceptId);
				addList.add(new AddAssertion(vocab, alignmentAssertion));
			} else if ("BM".equals(alignmentType)
					|| "BMG".equals(alignmentType)
					|| "BMP".equals(alignmentType)
					|| "BMI".equals(alignmentType)) {
				SKOSDataRelationAssertion alignmentAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSBroadMatchProperty().getURI()), targetConceptId);
				addList.add(new AddAssertion(vocab, alignmentAssertion));
			} else if ("NM".equals(alignmentType)
					|| "NMG".equals(alignmentType)
					|| "NMP".equals(alignmentType)
					|| "NMI".equals(alignmentType)) {
				SKOSDataRelationAssertion alignmentAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSNarrowMatchProperty().getURI()), targetConceptId);
				addList.add(new AddAssertion(vocab, alignmentAssertion));
			} else if ("RM".equals(alignmentType)){
				SKOSDataRelationAssertion alignmentAssertion = factory
						.getSKOSDataRelationAssertion(conceptSKOS, factory
								.getSKOSDataProperty(factory
										.getSKOSRelatedMatchProperty().getURI()), targetConceptId);
				addList.add(new AddAssertion(vocab, alignmentAssertion));
			}
		}
		return addList;
	}
}
