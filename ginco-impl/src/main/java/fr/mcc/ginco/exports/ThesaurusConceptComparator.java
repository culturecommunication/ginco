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

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.utils.ThesaurusTermUtils;

/**
 * Comparator to use with two concepts - compares based on its lexicalValue.
 */
@Service("thesaurusConceptComparator")
public class ThesaurusConceptComparator implements Comparator<ThesaurusConcept> {

	@Inject
	@Named("thesaurusTermService") 
	private IThesaurusTermService thesaurusTermService;
	
	@Inject
	@Named("thesaurusTermUtils") 
	private ThesaurusTermUtils thesaurusTermUtils;

	@Value("${ginco.default.language}") 
	private String defaultLang;
	
	@Override
	public int compare(ThesaurusConcept o1, ThesaurusConcept o2) {
		try {
			String l1 = thesaurusTermUtils
					.getPreferedTermsByLang(
							thesaurusTermService.getTermsByConceptId(o1
									.getIdentifier()), defaultLang).get(0)
					.getLexicalValue();
			String l2 = thesaurusTermUtils
					.getPreferedTermsByLang(
							thesaurusTermService.getTermsByConceptId(o2
									.getIdentifier()), defaultLang).get(0)
					.getLexicalValue();
			Collator collator = Collator
					.getInstance(new Locale(defaultLang));
			return collator.compare(l1, l2);
		} catch (BusinessException e) {
			return 0;
		}
	}
}