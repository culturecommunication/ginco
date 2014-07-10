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
package fr.mcc.ginco.tests.imports.ginco.idgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoCustomAttributesIdGenerator;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoIdMapParser;

public class GincoCustomAttributesIdGeneratorTest {	
	@Mock
	private GincoIdMapParser gincoIdMapParser;	
	
	@InjectMocks
	private GincoCustomAttributesIdGenerator gincoCustomAttributesIdGenerator;	

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@Test
	public void testGetIdsForCustomConceptAttributes() {
		
		Map<String, JaxbList<CustomConceptAttribute>>  customConceptAttributes = new HashMap<String, JaxbList<CustomConceptAttribute>>();

		
		ThesaurusConcept concept1 = new ThesaurusConcept();
		concept1.setIdentifier("oldId1");
		
		CustomConceptAttribute attr1 = new CustomConceptAttribute();
		attr1.setEntity(concept1);
		CustomConceptAttribute attr2 = new CustomConceptAttribute();
		attr2.setEntity(concept1);
		
		List<CustomConceptAttribute> concept1Attributes = new ArrayList<CustomConceptAttribute>();
		concept1Attributes.add(attr1);
		concept1Attributes.add(attr2);
		customConceptAttributes.put("oldId1", new JaxbList(concept1Attributes));
		
		ThesaurusConcept concept2 = new ThesaurusConcept();
		concept2.setIdentifier("oldId2");
		
		CustomConceptAttribute attr3 = new CustomConceptAttribute();
		attr3.setEntity(concept2);	
		
		List<CustomConceptAttribute> concept2Attributes = new ArrayList<CustomConceptAttribute>();
		concept2Attributes.add(attr3);
		
		customConceptAttributes.put("oldId2", new JaxbList(concept2Attributes));

		
		Map<String, String> idMapping = new HashMap<String, String>();
		
		Mockito.when(gincoIdMapParser.getNewId("oldId1", idMapping)).thenReturn("id1");
		Mockito.when(gincoIdMapParser.getNewId("oldId2", idMapping)).thenReturn("id2");

		
		Map<String, JaxbList<CustomConceptAttribute>> actualIds= gincoCustomAttributesIdGenerator.getIdsForCustomConceptAttributes(customConceptAttributes, idMapping);
		
		Assert.assertEquals(2, actualIds.size());
		
		Assert.assertNotNull(actualIds.get("id1"));
		Assert.assertEquals(2, actualIds.get("id1").getList().size());
		Assert.assertEquals("id1", actualIds.get("id1").getList().get(0).getEntity().getIdentifier());
		Assert.assertEquals("id1", actualIds.get("id1").getList().get(1).getEntity().getIdentifier());

		
		Assert.assertNotNull(actualIds.get("id2"));
		Assert.assertEquals(1, actualIds.get("id2").getList().size());	
		Assert.assertEquals("id2", actualIds.get("id2").getList().get(0).getEntity().getIdentifier());	
		
	}
	
	@Test
	public void testGetIdsForCustomTermAttributes() {
		
		Map<String, JaxbList<CustomTermAttribute>>  customTermAttributes = new HashMap<String, JaxbList<CustomTermAttribute>>();

		
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setIdentifier("oldId1");
		
		CustomTermAttribute attr1 = new CustomTermAttribute();
		attr1.setEntity(term1);
		CustomTermAttribute attr2 = new CustomTermAttribute();
		attr2.setEntity(term1);
		
		List<CustomTermAttribute> term1Attributes = new ArrayList<CustomTermAttribute>();
		term1Attributes.add(attr1);
		term1Attributes.add(attr2);
		customTermAttributes.put("oldId1", new JaxbList(term1Attributes));
		
		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setIdentifier("oldId2");
		
		CustomTermAttribute attr3 = new CustomTermAttribute();
		attr3.setEntity(term2);	
		
		List<CustomTermAttribute> term2Attributes = new ArrayList<CustomTermAttribute>();
		term2Attributes.add(attr3);
		
		customTermAttributes.put("oldId2", new JaxbList(term2Attributes));

		
		Map<String, String> idMapping = new HashMap<String, String>();
		
		Mockito.when(gincoIdMapParser.getNewId("oldId1", idMapping)).thenReturn("id1");
		Mockito.when(gincoIdMapParser.getNewId("oldId2", idMapping)).thenReturn("id2");

		
		Map<String, JaxbList<CustomTermAttribute>> actualIds= gincoCustomAttributesIdGenerator.getIdsForCustomTermAttributes(customTermAttributes, idMapping);
		
		Assert.assertEquals(2, actualIds.size());
		
		Assert.assertNotNull(actualIds.get("id1"));
		Assert.assertEquals(2, actualIds.get("id1").getList().size());
		Assert.assertEquals("id1", actualIds.get("id1").getList().get(0).getEntity().getIdentifier());
		Assert.assertEquals("id1", actualIds.get("id1").getList().get(1).getEntity().getIdentifier());

		
		Assert.assertNotNull(actualIds.get("id2"));
		Assert.assertEquals(1, actualIds.get("id2").getList().size());	
		Assert.assertEquals("id2", actualIds.get("id2").getList().get(0).getEntity().getIdentifier());	
		
	}
	
}
