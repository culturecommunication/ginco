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
package fr.mcc.ginco.exports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.result.bean.AlphabeticalExportedItem;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.services.IThesaurusTermRoleService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.LabelUtil;
import fr.mcc.ginco.utils.ThesaurusTermUtils;

/**
 * This component gives methods to export concepts alphabetically
 * 
 */
@Component("alphabeticComplexConceptExporter")
public class AlphabeticComplexConceptExporter {

	@Value("${ginco.default.language}")
	private String defaultLang;

	@Inject
	@Named("splitNonPreferredTermService")
	private ISplitNonPreferredTermService splitNonPreferredTermService;
	

	@Inject
	@Named("thesaurusTermUtils")
	private ThesaurusTermUtils thesaurusTermUtils;

	@Inject
	@Named("thesaurusTermRoleService")
	private IThesaurusTermRoleService thesaurusTermRoleService;

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;
	
	@Inject
	@Named("alphabeticalExportedItemComparator")
	private AlphabeticalExportedItemComparator alphabeticalExportedItemComparator;

	public void addComplexConceptTitle(Integer base,
			List<FormattedLine> result, SplitNonPreferredTerm complexConcept) {
		result.add(new FormattedLine(base, complexConcept.getLexicalValue()));
	}

	public void addComplexConceptInfo(Integer base, List<FormattedLine> result,
			SplitNonPreferredTerm complexConcept) {
		
		List<AlphabeticalExportedItem> termsToExport = new ArrayList<AlphabeticalExportedItem>();
		for (ThesaurusTerm term : complexConcept.getPreferredTerms()) {
			AlphabeticalExportedItem item = new AlphabeticalExportedItem();
			item.setLexicalValue(term.getLexicalValue());
			item.setObjectToExport(term);
			termsToExport.add(item);
		}
		Collections.sort(termsToExport, alphabeticalExportedItemComparator);
		
		String resultString = LabelUtil.getResourceLabel("EM") + ": ";
		for (AlphabeticalExportedItem item : termsToExport) {
			resultString += thesaurusTermUtils
					.generatePrefTermText((ThesaurusTerm) item.getObjectToExport()) + " + ";	
		}
		resultString = resultString.substring(0, resultString.length() - 3);
		result.add(new FormattedLine(base, resultString));
	}
}