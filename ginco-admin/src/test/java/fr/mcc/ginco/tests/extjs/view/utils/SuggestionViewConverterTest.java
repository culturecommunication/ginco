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
package fr.mcc.ginco.tests.extjs.view.utils;

import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Suggestion;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.extjs.view.pojo.SuggestionView;
import fr.mcc.ginco.extjs.view.utils.SuggestionViewConverter;

public class SuggestionViewConverterTest {

	@InjectMocks
	private SuggestionViewConverter converter = new SuggestionViewConverter();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertFromSuggestion() {
		Suggestion suggestion = new Suggestion();
		suggestion.setIdentifier(1);
		suggestion.setContent("suggestion content");
		suggestion.setCreator("creator@culture.gouv.fr");
		suggestion.setRecipient("recipient@culture.gouv.fr");
		suggestion.setCreated((new GregorianCalendar(2013, 10, 15, 14, 0, 0))
				.getTime());

		ThesaurusConcept concept = new ThesaurusConcept();
		concept.setIdentifier("http://data/culture.gouv.fr/123/c1");
		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://data/culture.gouv.fr/123/th1");
		concept.setThesaurus(th);

		suggestion.setConcept(concept);

		SuggestionView actualView = converter.convert(suggestion);
		Assert.assertEquals(1, actualView.getIdentifier().intValue());
		Assert.assertEquals("suggestion content", actualView.getContent());
		Assert.assertEquals("creator@culture.gouv.fr", actualView.getCreator());
		Assert.assertEquals("recipient@culture.gouv.fr",
				actualView.getRecipient());
		Assert.assertEquals("2013-11-15 14:00:00", actualView.getCreated());
		Assert.assertEquals("http://data/culture.gouv.fr/123/c1",
				actualView.getConceptId());
		Assert.assertEquals("http://data/culture.gouv.fr/123/th1",
				actualView.getThesaurusId());
		Assert.assertNull(actualView.getTermId());

		suggestion.setConcept(null);

		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("http://data/culture.gouv.fr/123/ter1");
		Thesaurus th2 = new Thesaurus();
		th2.setIdentifier("http://data/culture.gouv.fr/123/th2");
		term.setThesaurus(th2);
		suggestion.setTerm(term);

		SuggestionView actualView2 = converter.convert(suggestion);

		Assert.assertEquals("http://data/culture.gouv.fr/123/ter1",
				actualView2.getTermId());
		Assert.assertEquals("http://data/culture.gouv.fr/123/th2",
				actualView2.getThesaurusId());
		Assert.assertNull(actualView2.getConceptId());

	}

}
