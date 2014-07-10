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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.beans.CustomTermAttributeType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.ICustomConceptAttributeDAO;
import fr.mcc.ginco.dao.ICustomConceptAttributeTypeDAO;
import fr.mcc.ginco.dao.ICustomTermAttributeDAO;
import fr.mcc.ginco.dao.ICustomTermAttributeTypeDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedBranch;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.imports.ginco.GincoCustomAttributeImporter;
import fr.mcc.ginco.services.ICustomConceptAttributeTypeService;
import fr.mcc.ginco.services.ICustomTermAttributeTypeService;

public class GincoCustomAttributeImporterTest {

	@Mock
	private ICustomTermAttributeTypeDAO customTermAttributeTypeDAO;

	@Mock
	private ICustomConceptAttributeTypeDAO customConceptAttributeTypeDAO;

	@Mock
	private ICustomConceptAttributeDAO customConceptAttributeDAO;

	@Mock
	private ICustomTermAttributeDAO customTermAttributeDAO;

	@Mock
	private ICustomConceptAttributeTypeService customConceptAttributeTypeService;

	@Mock
	private ICustomTermAttributeTypeService customTermAttributeTypeService;

	@InjectMocks
	private GincoCustomAttributeImporter gincoCustomAttributeImporter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testStoreCustomTermAttributeTypes() {

		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");

		CustomTermAttributeType t1 = new CustomTermAttributeType();
		t1.setIdentifier(1);
		t1.setThesaurus(th1);
		t1.setCode("ty1");
		t1.setValue("fakeType1");

		CustomTermAttributeType t2 = new CustomTermAttributeType();
		t2.setIdentifier(2);
		t2.setThesaurus(th1);
		t2.setCode("ty2");
		t2.setValue("fakeType2");

		CustomTermAttributeType t3 = new CustomTermAttributeType();
		t3.setIdentifier(3);
		t3.setThesaurus(th1);
		t3.setCode("ty3");
		t3.setValue("fakeType3");

		List<CustomTermAttributeType> types = new ArrayList<CustomTermAttributeType>();
		types.add(t1);
		types.add(t2);
		types.add(t3);

		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setThesaurus(th1);
		exportedThesaurus.setTermAttributeTypes(types);

		Mockito.when(customTermAttributeTypeDAO.update(t1)).thenReturn(t1);

		Mockito.when(customTermAttributeTypeDAO.update(t2)).thenReturn(t2);

		Mockito.when(customTermAttributeTypeDAO.update(t3)).thenReturn(t3);
		Map<String, CustomTermAttributeType> resultedAttributeTypes = gincoCustomAttributeImporter
				.storeCustomTermAttributeTypes(
						exportedThesaurus.getTermAttributeTypes(),
						exportedThesaurus.getThesaurus());

		Assert.assertEquals(3, resultedAttributeTypes.size());
		Assert.assertTrue(resultedAttributeTypes.containsKey("ty1"));
		Assert.assertEquals("fakeType1", resultedAttributeTypes.get("ty1")
				.getValue());

		Assert.assertTrue(resultedAttributeTypes.containsKey("ty2"));
		Assert.assertEquals("fakeType2", resultedAttributeTypes.get("ty2")
				.getValue());

		Assert.assertTrue(resultedAttributeTypes.containsKey("ty3"));
		Assert.assertEquals("fakeType3", resultedAttributeTypes.get("ty3")
				.getValue());

	}

	@Test
	public void testGetBranchCustomTermAttributeTypes() {

		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");

		CustomTermAttributeType existingType1 = new CustomTermAttributeType();
		existingType1.setIdentifier(1);
		existingType1.setThesaurus(th1);
		existingType1.setCode("ty1");
		existingType1.setValue("fakeType1");

		CustomTermAttributeType existingType2 = new CustomTermAttributeType();
		existingType2.setIdentifier(2);
		existingType2.setThesaurus(th1);
		existingType2.setCode("ty2");
		existingType2.setValue("fakeType2");

		CustomTermAttributeType t1 = new CustomTermAttributeType();
		t1.setIdentifier(1);
		t1.setCode("ty1");
		t1.setValue("fakeType1");

		CustomTermAttributeType t2 = new CustomTermAttributeType();
		t2.setIdentifier(2);
		t2.setCode("ty2");
		t2.setValue("otherValue");

		CustomTermAttributeType t3 = new CustomTermAttributeType();
		t2.setIdentifier(3);
		t2.setCode("otherCode");
		t2.setValue("otherValue");

		List<CustomTermAttributeType> types = new ArrayList<CustomTermAttributeType>();
		types.add(t1);
		types.add(t2);
		types.add(t3);

		Mockito.when(customTermAttributeTypeDAO.getAttributeByCode(
				th1, t1.getCode())).thenReturn(existingType1);
		Mockito.when(customTermAttributeTypeDAO.getAttributeByValue(
				th1, t2.getCode())).thenReturn(existingType2);
		Mockito.when(customTermAttributeTypeDAO.getAttributeByValue(
				th1, t3.getCode())).thenReturn(null);

		GincoExportedBranch exportedBranch = new GincoExportedBranch();
		exportedBranch.setTermAttributeTypes(types);

		Map<String, CustomTermAttributeType> resultedAttributeTypes = gincoCustomAttributeImporter
				.getBranchCustomTermAttributeTypes(
						exportedBranch.getTermAttributeTypes(), th1);

		Assert.assertEquals(1, resultedAttributeTypes.size());
		Assert.assertTrue(resultedAttributeTypes.containsKey("ty1"));
		Assert.assertEquals("fakeType1", resultedAttributeTypes.get("ty1")
				.getValue());

		Assert.assertTrue(!resultedAttributeTypes.containsKey("ty2"));
		Assert.assertTrue(!resultedAttributeTypes.containsKey("ty3"));

	}

	@Test
	public void testStoreCustomConceptAttributeTypes() {

		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");

		CustomConceptAttributeType t1 = new CustomConceptAttributeType();
		t1.setIdentifier(1);
		t1.setCode("ty1");
		t1.setThesaurus(th1);
		t1.setValue("fakeType1");

		CustomConceptAttributeType t2 = new CustomConceptAttributeType();
		t2.setIdentifier(2);
		t2.setCode("ty2");
		t2.setThesaurus(th1);
		t2.setValue("fakeType2");

		CustomConceptAttributeType t3 = new CustomConceptAttributeType();
		t3.setIdentifier(3);
		t3.setCode("ty3");
		t3.setThesaurus(th1);
		t3.setValue("fakeType3");

		List<CustomConceptAttributeType> types = new ArrayList<CustomConceptAttributeType>();
		types.add(t1);
		types.add(t2);
		types.add(t3);

		GincoExportedThesaurus exportedThesaurus = new GincoExportedThesaurus();
		exportedThesaurus.setThesaurus(th1);
		exportedThesaurus.setConceptAttributeTypes(types);

		Mockito.when(customConceptAttributeTypeDAO.update(t1)).thenReturn(t1);
		Mockito.when(customConceptAttributeTypeDAO.update(t2)).thenReturn(t2);
		Mockito.when(customConceptAttributeTypeDAO.update(t3)).thenReturn(t3);

		Map<String, CustomConceptAttributeType> resultedAttributeTypes = gincoCustomAttributeImporter
				.storeCustomConceptAttributeTypes(
						exportedThesaurus.getConceptAttributeTypes(),
						exportedThesaurus.getThesaurus());

		Assert.assertEquals(3, resultedAttributeTypes.size());
		Assert.assertTrue(resultedAttributeTypes.containsKey("ty1"));
		Assert.assertEquals("fakeType1", resultedAttributeTypes.get("ty1")
				.getValue());

		Assert.assertTrue(resultedAttributeTypes.containsKey("ty2"));
		Assert.assertEquals("fakeType2", resultedAttributeTypes.get("ty2")
				.getValue());

		Assert.assertTrue(resultedAttributeTypes.containsKey("ty3"));
		Assert.assertEquals("fakeType3", resultedAttributeTypes.get("ty3")
				.getValue());
		;
	}

	@Test
	public void testGetBranchCustomConceptAttributeTypes() {
		Thesaurus th1 = new Thesaurus();
		th1.setIdentifier("http://th1");

		CustomConceptAttributeType existingType1 = new CustomConceptAttributeType();
		existingType1.setIdentifier(1);
		existingType1.setThesaurus(th1);
		existingType1.setCode("ty1");
		existingType1.setValue("fakeType1");

		CustomConceptAttributeType existingType2 = new CustomConceptAttributeType();
		existingType2.setIdentifier(2);
		existingType2.setThesaurus(th1);
		existingType2.setCode("ty2");
		existingType2.setValue("fakeType2");

		CustomConceptAttributeType t1 = new CustomConceptAttributeType();
		t1.setIdentifier(1);
		t1.setCode("ty1");
		t1.setValue("fakeType1");

		CustomConceptAttributeType t2 = new CustomConceptAttributeType();
		t2.setIdentifier(2);
		t2.setCode("ty2");
		t2.setValue("otherValue");

		CustomConceptAttributeType t3 = new CustomConceptAttributeType();
		t2.setIdentifier(3);
		t2.setCode("otherCode");
		t2.setValue("otherValue");

		List<CustomConceptAttributeType> types = new ArrayList<CustomConceptAttributeType>();
		types.add(t1);
		types.add(t2);
		types.add(t3);

		Mockito.when(customConceptAttributeTypeDAO.getAttributeByCode(
				th1, t1.getCode())).thenReturn(existingType1);

		Mockito.when(customConceptAttributeTypeDAO.getAttributeByCode(
				th1, t2.getCode())).thenReturn(existingType2);

		Mockito.when(customConceptAttributeTypeDAO.getAttributeByCode(
				th1, t3.getCode())).thenReturn(null);

		GincoExportedBranch exportedBranch = new GincoExportedBranch();
		exportedBranch.setConceptAttributeTypes(types);

		Map<String, CustomConceptAttributeType> resultedAttributeTypes = gincoCustomAttributeImporter
				.getBranchCustomConceptAttributeTypes(
						exportedBranch.getConceptAttributeTypes(), th1);

		Assert.assertEquals(1, resultedAttributeTypes.size());
		Assert.assertTrue(resultedAttributeTypes.containsKey("ty1"));
		Assert.assertEquals("fakeType1", resultedAttributeTypes.get("ty1")
				.getValue());

		Assert.assertTrue(!resultedAttributeTypes.containsKey("ty2"));
		Assert.assertTrue(!resultedAttributeTypes.containsKey("ty3"));

	}

	@Test
	public void testStoreCustomConceptAttribute() {

		List<CustomConceptAttribute> conceptAttributes = new ArrayList<CustomConceptAttribute>();
		ThesaurusConcept concept = new ThesaurusConcept();
		CustomConceptAttribute attr1 = new CustomConceptAttribute();
		CustomConceptAttributeType fakeType1 = new CustomConceptAttributeType();
		fakeType1.setCode("code1");
		attr1.setType(fakeType1);

		CustomConceptAttribute attr2 = new CustomConceptAttribute();
		CustomConceptAttributeType fakeType2 = new CustomConceptAttributeType();
		fakeType2.setCode("code2");
		attr2.setType(fakeType2);

		conceptAttributes.add(attr1);
		conceptAttributes.add(attr2);

		Map<String, CustomConceptAttributeType> savedTypes = new HashMap<String, CustomConceptAttributeType>();
		CustomConceptAttributeType type1 = new CustomConceptAttributeType();
		savedTypes.put("code1", type1);
		CustomConceptAttributeType type2 = new CustomConceptAttributeType();
		savedTypes.put("code2", type2);

		gincoCustomAttributeImporter.storeCustomConceptAttribute(
				conceptAttributes, concept, savedTypes);

		ListAssert.assertContains(conceptAttributes, attr1);
		Assert.assertEquals(concept, attr1.getEntity());
		Assert.assertEquals(type1, attr1.getType());

		ListAssert.assertContains(conceptAttributes, attr2);
		Assert.assertEquals(concept, attr2.getEntity());
		Assert.assertEquals(type2, attr2.getType());

	}

	@Test
	public void testStoreCustomTermAttribute() {

		List<CustomTermAttribute> termAttributes = new ArrayList<CustomTermAttribute>();
		ThesaurusTerm concept = new ThesaurusTerm();
		CustomTermAttribute attr1 = new CustomTermAttribute();
		CustomTermAttributeType fakeType1 = new CustomTermAttributeType();
		fakeType1.setCode("code1");
		attr1.setType(fakeType1);

		CustomTermAttribute attr2 = new CustomTermAttribute();
		CustomTermAttributeType fakeType2 = new CustomTermAttributeType();
		fakeType2.setCode("code2");
		attr2.setType(fakeType2);

		termAttributes.add(attr1);
		termAttributes.add(attr2);

		Map<String, CustomTermAttributeType> savedTypes = new HashMap<String, CustomTermAttributeType>();
		CustomTermAttributeType type1 = new CustomTermAttributeType();
		savedTypes.put("code1", type1);
		CustomTermAttributeType type2 = new CustomTermAttributeType();
		savedTypes.put("code2", type2);

		gincoCustomAttributeImporter.storeCustomTermAttribute(termAttributes,
				concept, savedTypes);

		ListAssert.assertContains(termAttributes, attr1);
		Assert.assertEquals(concept, attr1.getEntity());
		Assert.assertEquals(type1, attr1.getType());

		ListAssert.assertContains(termAttributes, attr2);
		Assert.assertEquals(concept, attr2.getEntity());
		Assert.assertEquals(type2, attr2.getType());

	}

}
