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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ExternalThesaurus;
import fr.mcc.ginco.beans.ExternalThesaurusType;
import fr.mcc.ginco.extjs.view.pojo.ExternalThesaurusView;
import fr.mcc.ginco.extjs.view.utils.ExternalThesaurusViewConverter;
import fr.mcc.ginco.services.IExternalThesaurusService;
import fr.mcc.ginco.services.IExternalThesaurusTypeService;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class ExternalThesaurusViewConverterTest {

	@Mock(name = "externalThesaurusTypeService")
	private IExternalThesaurusTypeService externalThesaurusTypeService;

	@Mock(name = "externalThesaurusService")
	private IExternalThesaurusService externalThesaurusService;

	@InjectMocks
	private ExternalThesaurusViewConverter converter;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(converter);
	}

	@Test
	public void testConvertExternalThesaurus() {
		ExternalThesaurus externalThesaurus = new ExternalThesaurus();
		externalThesaurus.setIdentifier(1);
		externalThesaurus
				.setExternalId("http://catalogue.bnf.fr/ark:/12148/cb15929801j");

		ExternalThesaurusType type = new ExternalThesaurusType();
		type.setIdentifier(3);
		externalThesaurus.setExternalThesaurusType(type);

		ExternalThesaurusView actualView = converter
				.convertExternalThesaurus(externalThesaurus);

		Assert.assertEquals(new Integer(1), actualView.getIdentifier());
		Assert.assertEquals("http://catalogue.bnf.fr/ark:/12148/cb15929801j",
				actualView.getExternalId());
		Assert.assertEquals(new Integer(3),
				actualView.getExternalThesaurusType());

	}

	@Test
	public void testConvertExternalThesaurusViewExisting() {

		ExternalThesaurusView externalThesaurusView = new ExternalThesaurusView();
		externalThesaurusView.setIdentifier(1);
		externalThesaurusView
				.setExternalId("http://catalogue.bnf.fr/ark:/12148/cb15929801j");
		externalThesaurusView.setExternalThesaurusType(3);
		ExternalThesaurusType type = new ExternalThesaurusType();
		ExternalThesaurus existingThesaurus = new ExternalThesaurus();
		Mockito.when(externalThesaurusService.getExternalThesaurusById(1))
				.thenReturn(existingThesaurus);
		;
		Mockito.when(
				externalThesaurusTypeService.getExternalThesaurusTypeById(3))
				.thenReturn(type);

		ExternalThesaurus actualExternalThesaurus = converter
				.convertExternalThesaurusView(externalThesaurusView);

		Assert.assertEquals(new Integer(1),
				actualExternalThesaurus.getIdentifier());
		Assert.assertEquals("http://catalogue.bnf.fr/ark:/12148/cb15929801j",
				actualExternalThesaurus.getExternalId());
		Assert.assertEquals(type,
				actualExternalThesaurus.getExternalThesaurusType());

	}

	@Test
	public void testConvertExternalThesaurusViewNotExisting() {

		ExternalThesaurusView externalThesaurusView = new ExternalThesaurusView();
		externalThesaurusView
				.setExternalId("http://catalogue.bnf.fr/ark:/12148/cb15929801j");
		externalThesaurusView.setExternalThesaurusType(3);
		ExternalThesaurusType type = new ExternalThesaurusType();
		Mockito.when(
				externalThesaurusService
						.getThesaurusByExternalId("http://catalogue.bnf.fr/ark:/12148/cb15929801j"))
				.thenReturn(null);
		Mockito.when(
				externalThesaurusTypeService.getExternalThesaurusTypeById(3))
				.thenReturn(type);

		ExternalThesaurus actualExternalThesaurus = converter
				.convertExternalThesaurusView(externalThesaurusView);

		Assert.assertEquals("http://catalogue.bnf.fr/ark:/12148/cb15929801j",
				actualExternalThesaurus.getExternalId());
		Assert.assertEquals(type,
				actualExternalThesaurus.getExternalThesaurusType());

	}

	@Test
	public void testConvertExternalThesaurusViewValueExisting() {

		ExternalThesaurusView externalThesaurusView = new ExternalThesaurusView();
		externalThesaurusView
				.setExternalId("http://catalogue.bnf.fr/ark:/12148/cb15929801j");
		externalThesaurusView.setExternalThesaurusType(3);
		ExternalThesaurusType type = new ExternalThesaurusType();
		ExternalThesaurus existingThesaurus = new ExternalThesaurus();
		existingThesaurus.setIdentifier(1);
		Mockito.when(
				externalThesaurusService
						.getThesaurusByExternalId("http://catalogue.bnf.fr/ark:/12148/cb15929801j"))
				.thenReturn(existingThesaurus);
		Mockito.when(
				externalThesaurusTypeService.getExternalThesaurusTypeById(3))
				.thenReturn(type);

		ExternalThesaurus actualExternalThesaurus = converter
				.convertExternalThesaurusView(externalThesaurusView);

		Assert.assertEquals(new Integer(1),
				actualExternalThesaurus.getIdentifier());

		Assert.assertEquals("http://catalogue.bnf.fr/ark:/12148/cb15929801j",
				actualExternalThesaurus.getExternalId());
		Assert.assertEquals(type,
				actualExternalThesaurus.getExternalThesaurusType());
	}

}
