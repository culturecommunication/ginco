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

import java.io.InputStream;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.imports.NodeLabelBuilder;

public class NodeLabelBuilderTest {
	@Inject
	@Mock(name = "languagesDAO")
	private ILanguageDAO languagesDAO;

	@InjectMocks
	private NodeLabelBuilder nodeLabelBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(nodeLabelBuilder, "defaultLang", "fr-FR");
	}

	@Test
	public void testBuildNodeLabel() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("thesaurus-uri");

		Language french = new Language();
		french.setId("fr-FR");
		Mockito.when(languagesDAO.getByPart1("fr")).thenReturn(french);


		Model model = ModelFactory.createDefaultModel();
		InputStream is = ConceptBuilderTest.class
				.getResourceAsStream("/imports/concept_collections.rdf");
		model.read(is, null);

		Resource skosArray = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69-C5");
		ThesaurusArray array = new ThesaurusArray();
		NodeLabel actualNodeLabel = nodeLabelBuilder.buildNodeLabel(skosArray, fakeThesaurus, array);

		Assert.assertEquals("récipients pour le service et la consommation des aliments", actualNodeLabel.getLexicalValue());



	}

}
