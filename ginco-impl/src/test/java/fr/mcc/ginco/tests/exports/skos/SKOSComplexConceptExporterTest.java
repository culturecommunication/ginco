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
package fr.mcc.ginco.tests.exports.skos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.exports.skos.skosapi.SKOSComplexConceptExporter;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.utils.DateUtil;

public class SKOSComplexConceptExporterTest {

	@Mock(name="splitNonPreferredTermService")
	private ISplitNonPreferredTermService splitNonPreferredTermService;

	@InjectMocks
	SKOSComplexConceptExporter skosComplexConceptExporter;

	@Before
	public void init() {
			MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportComplexConcept(){

		Language lang = new Language();
		lang.setPart1("fr");

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");

		List<SplitNonPreferredTerm> complexConceptsInThesaurus = new ArrayList<SplitNonPreferredTerm>();

		SplitNonPreferredTerm complexConcept = new SplitNonPreferredTerm();
		complexConcept.setThesaurus(th);
		complexConcept.setIdentifier("http://cc1");
		complexConcept.setCreated(DateUtil.nowDate());
		complexConcept.setModified(DateUtil.nowDate());
		complexConcept.setStatus(ConceptStatusEnum.CANDIDATE.getStatus());
		complexConcept.setLexicalValue("fake complex concept");
		complexConcept.setSource("fake source");
		complexConcept.setLanguage(lang);

		ThesaurusTerm t1 = new ThesaurusTerm();
		t1.setIdentifier("http://t1");

		ThesaurusTerm t2 = new ThesaurusTerm();
		t2.setIdentifier("http://t1");

		Set<ThesaurusTerm> terms = new HashSet<ThesaurusTerm>();
		terms.add(t1);
		terms.add(t2);
		complexConcept.setPreferredTerms(terms);

		complexConceptsInThesaurus.add(complexConcept);

		Mockito.when(splitNonPreferredTermService
				.getSplitNonPreferredTermList(Mockito.anyInt(), Mockito.anyInt(),
						Mockito.eq(th.getThesaurusId()))).thenReturn(complexConceptsInThesaurus);

		skosComplexConceptExporter.exportComplexConcept(th);
	}
}
