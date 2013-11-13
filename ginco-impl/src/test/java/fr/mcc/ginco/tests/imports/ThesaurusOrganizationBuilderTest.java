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

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;

import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.imports.ThesaurusOrganizationBuilder;

public class ThesaurusOrganizationBuilderTest {

	private ThesaurusOrganizationBuilder thesaurusOrganizationBuilder = new ThesaurusOrganizationBuilder();

	@Test
	public void testGetCreator() throws ParseException {
		Model model = ModelFactory.createDefaultModel();

		Resource skosThesaurus = model
				.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");

		Resource foafOrgResource = model.createResource(FOAF.Organization);
		foafOrgResource.addProperty(FOAF.name,
				"Ministère de la culture et de la communication");

		foafOrgResource
				.addProperty(FOAF.homepage, "http://www.culture.gouv.fr");

		foafOrgResource.addProperty(FOAF.mbox, "contact@culture.gouv.fr");

		skosThesaurus.addProperty(DC.creator, foafOrgResource);

		ThesaurusOrganization actualOrg = thesaurusOrganizationBuilder
				.getCreator(skosThesaurus, model);

		Assert.assertEquals("http://www.culture.gouv.fr",
				actualOrg.getHomepage());
		Assert.assertEquals("Ministère de la culture et de la communication",
				actualOrg.getName());
		Assert.assertEquals("contact@culture.gouv.fr", actualOrg.getEmail());

	}

	@Test
	public void testGetCreatorLiteral() throws ParseException {
		Model model = ModelFactory.createDefaultModel();

		Resource skosThesaurus = model
				.createResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");

		skosThesaurus.addProperty(DC.creator, "text value");

		ThesaurusOrganization actualOrg = thesaurusOrganizationBuilder
				.getCreator(skosThesaurus, model);

		Assert.assertEquals("text value", actualOrg.getName());

	}

}
