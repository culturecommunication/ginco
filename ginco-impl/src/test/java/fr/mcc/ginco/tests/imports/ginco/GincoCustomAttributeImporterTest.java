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
package fr.mcc.ginco.tests.imports.ginco;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.CustomTermAttributeType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.dao.ICustomConceptAttributeTypeDAO;
import fr.mcc.ginco.dao.ICustomTermAttributeTypeDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.imports.ginco.GincoCustomAttributeImporter;

public class GincoCustomAttributeImporterTest {

	@Mock
	private ICustomTermAttributeTypeDAO customTermAttributeTypeDAO;

	@Mock
	private ICustomConceptAttributeTypeDAO customConceptAttributeTypeDAO;

	@InjectMocks
	GincoCustomAttributeImporter gincoCustomAttributeImporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void storeCustomTermAttributeTypes() {

		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");

		CustomTermAttributeType t1 = new CustomTermAttributeType();
		t1.setIdentifier(1);
		t1.setThesaurus(th1);
		t1.setValue("fakeType1");

		CustomTermAttributeType t2 = new CustomTermAttributeType();
		t2.setIdentifier(1);
		t2.setThesaurus(th1);
		t2.setValue("fakeType2");

		CustomTermAttributeType t3 = new CustomTermAttributeType();
		t3.setIdentifier(1);
		t3.setThesaurus(th1);
		t3.setValue("fakeType3");

		List<CustomTermAttributeType> resultedAttributeTypes = new ArrayList<CustomTermAttributeType>();
		List<CustomTermAttributeType> types = new ArrayList<CustomTermAttributeType>();
		types.add(t1);
		types.add(t2);
		types.add(t3);

		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setThesaurus(th1);
		exportedThesaurus.setTermAttributeTypes(types);

		Mockito.when(
				customTermAttributeTypeDAO.update(Mockito
						.any(CustomTermAttributeType.class))).thenReturn(t1);
		resultedAttributeTypes = gincoCustomAttributeImporter
				.storeCustomTermAttributeTypes(
						exportedThesaurus.getTermAttributeTypes(),
						exportedThesaurus.getThesaurus());

		Assert.assertEquals(resultedAttributeTypes.size(), types.size());
		Assert.assertEquals(resultedAttributeTypes.get(0).getIdentifier(),
				types.get(0).getIdentifier());
	}

	@Test
	public void storeCustomConceptAttributeTypes() {

		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");

		CustomConceptAttributeType t1 = new CustomConceptAttributeType();
		t1.setIdentifier(1);
		t1.setThesaurus(th1);
		t1.setValue("fakeType1");

		CustomConceptAttributeType t2 = new CustomConceptAttributeType();
		t2.setIdentifier(1);
		t2.setThesaurus(th1);
		t2.setValue("fakeType2");

		CustomConceptAttributeType t3 = new CustomConceptAttributeType();
		t3.setIdentifier(1);
		t3.setThesaurus(th1);
		t3.setValue("fakeType3");

		List<CustomConceptAttributeType> resultedAttributeTypes = new ArrayList<CustomConceptAttributeType>();
		List<CustomConceptAttributeType> types = new ArrayList<CustomConceptAttributeType>();
		types.add(t1);
		types.add(t2);
		types.add(t3);

		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setThesaurus(th1);
		exportedThesaurus.setConceptAttributeTypes(types);

		Mockito.when(
				customConceptAttributeTypeDAO.update(Mockito
						.any(CustomConceptAttributeType.class))).thenReturn(t1);
		resultedAttributeTypes = gincoCustomAttributeImporter
				.storeCustomConceptAttributeTypes(
						exportedThesaurus.getConceptAttributeTypes(),
						exportedThesaurus.getThesaurus());

		Assert.assertEquals(resultedAttributeTypes.size(), types.size());
		Assert.assertEquals(resultedAttributeTypes.get(0).getIdentifier(),
				types.get(0).getIdentifier());
	}
}
