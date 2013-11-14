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
package fr.mcc.ginco.tests.imports;

import java.util.ArrayList;
import java.util.List;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.dao.IThesaurusTermRoleDAO;
import fr.mcc.ginco.imports.TermBuilder;
import fr.mcc.ginco.skos.namespaces.SKOS;

public class TermBuilderTest {	

	@Mock(name = "generatorService")
	private IIDGeneratorService generatorService;

	@Mock(name = "thesaurusTermRoleDAO")
	private IThesaurusTermRoleDAO thesaurusTermRoleDAO;

	@Mock(name = "languagesDAO")
	private ILanguageDAO languagesDAO;

	
	@InjectMocks
	private TermBuilder termBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuildTerms() {
		Language french = new Language();
		french.setId("fr-FR");
		Mockito.when(languagesDAO.getByPart1("fr")).thenReturn(french);
		
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Model model = ModelFactory.createDefaultModel();	

		Resource skosConcept = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1937");
		skosConcept.addProperty(SKOS.PREF_LABEL, "selle");
		skosConcept.addProperty(SKOS.ALT_LABEL, "selles");
		skosConcept.addProperty(SKOS.HIDDEN_LABEL, "selle à traire");
		
		ThesaurusConcept concept = new ThesaurusConcept();
		concept.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-1937");
		List<ThesaurusTerm> actualTerms = termBuilder.buildTerms(skosConcept, fakeThesaurus, concept);	
		Assert.assertEquals(3, actualTerms.size());
		List<String> lexicalvalues = new ArrayList<String>();
		for (ThesaurusTerm actualTerm:actualTerms) {
			lexicalvalues.add(actualTerm.getLexicalValue());
			if ("selle".equals(actualTerm.getLexicalValue())) {
				Assert.assertEquals(true, actualTerm.getPrefered());
			} else  {
				Assert.assertEquals(false, actualTerm.getPrefered());
			}
		}
		ListAssert.assertContains(lexicalvalues, "selle");
		ListAssert.assertContains(lexicalvalues, "selles");
		ListAssert.assertContains(lexicalvalues, "selle à traire");
		
		
	}

	
}
