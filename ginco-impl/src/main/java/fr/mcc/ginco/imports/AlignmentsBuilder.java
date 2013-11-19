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

package fr.mcc.ginco.imports;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.AlignmentType;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.IAlignmentTypeService;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * Builder in charge of building concept alignments
 *
 */

@Service("skosAlignmentsBuilder")
public class AlignmentsBuilder extends AbstractBuilder {

	private static Logger logger = LoggerFactory.getLogger(AlignmentsBuilder.class);

	@Inject
	@Named("alignmentTypeService")
	private IAlignmentTypeService alignmentTypeService;
	
	
	@Inject
	@Named("skosAlignmentBuilder")
	private AlignmentBuilder alignmentBuilder;

	/**
	 * Returns the list of alignments for the given source concept
	 *
	 * @param skosConcept
	 * @param concept
	 * @return
	 */

	public List<Alignment> buildAlignments(Resource skosConcept,
			ThesaurusConcept concept) throws BusinessException {
		logger.debug("Building alignments for concept " + skosConcept.getURI());
		List<Alignment> alignments = new ArrayList<Alignment>();
		List<AlignmentType> alignmentTypes = alignmentTypeService
				.getAlignmentTypeList();

		for (AlignmentType alignmentType : alignmentTypes) {
			String isoCode = alignmentType.getIsoCode();
			if (SKOS.SKOS_ALIGNMENTS.containsKey(isoCode)) {
				StmtIterator stmtItr = skosConcept
						.listProperties(SKOS.SKOS_ALIGNMENTS.get(isoCode));
				while (stmtItr.hasNext()) {
					Statement stmtAlignment = stmtItr.next();
					alignments.add(alignmentBuilder.buildAlignment(stmtAlignment, alignmentType,
							concept));
				}
			}
		}
		return alignments;
	}	
}
