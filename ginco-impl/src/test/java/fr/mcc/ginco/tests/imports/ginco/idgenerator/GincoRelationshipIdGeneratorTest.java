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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ConceptHierarchicalRelationship;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoIdMapParser;
import fr.mcc.ginco.imports.ginco.idgenerator.GincoRelationshipIdGenerator;

public class GincoRelationshipIdGeneratorTest {

	@Mock(name = "gincoIdMapParser")
	private GincoIdMapParser gincoIdMapParser;

	@InjectMocks
	private GincoRelationshipIdGenerator gincoRelationshipIdGenerator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetIdsForHierarchicalRelations() {
		Map<String, JaxbList<ConceptHierarchicalRelationship>> relations = new HashMap<String, JaxbList<ConceptHierarchicalRelationship>>();
		JaxbList<ConceptHierarchicalRelationship> relation1 = new JaxbList<ConceptHierarchicalRelationship>();

		ConceptHierarchicalRelationship conceptHierarchicalRelationship1 = new ConceptHierarchicalRelationship();
		ConceptHierarchicalRelationship.Id idOfRelation1 = new ConceptHierarchicalRelationship.Id();
		idOfRelation1.setChildconceptid("childconceptid1");
		idOfRelation1.setParentconceptid("parentconceptid1");
		conceptHierarchicalRelationship1.setIdentifier(idOfRelation1);

		ConceptHierarchicalRelationship conceptHierarchicalRelationship2 = new ConceptHierarchicalRelationship();
		ConceptHierarchicalRelationship.Id idOfRelation2 = new ConceptHierarchicalRelationship.Id();
		idOfRelation2.setChildconceptid("childconceptid2");
		idOfRelation2.setParentconceptid("parentconceptid2");
		conceptHierarchicalRelationship2.setIdentifier(idOfRelation2);

		relation1.getList().add(conceptHierarchicalRelationship1);
		relation1.getList().add(conceptHierarchicalRelationship2);

		JaxbList<ConceptHierarchicalRelationship> relation2 = new JaxbList<ConceptHierarchicalRelationship>();
		ConceptHierarchicalRelationship conceptHierarchicalRelationship3 = new ConceptHierarchicalRelationship();
		ConceptHierarchicalRelationship.Id idOfRelation3 = new ConceptHierarchicalRelationship.Id();
		idOfRelation3.setChildconceptid("childconceptid3");
		idOfRelation3.setParentconceptid("parentconceptid3");
		conceptHierarchicalRelationship3.setIdentifier(idOfRelation3);

		relation2.getList().add(conceptHierarchicalRelationship3);

		relations.put("relationId1", relation1);
		relations.put("relationId2", relation2);

		Map<String, String> idMapping = new HashMap<String, String>();
		Mockito.when(gincoIdMapParser.getNewId("relationId1", idMapping))
				.thenReturn("newRelationId1");
		Mockito.when(gincoIdMapParser.getNewId("relationId2", idMapping))
				.thenReturn("newRelationId2");

		Mockito.when(gincoIdMapParser.getNewId("childconceptid2", idMapping))
				.thenReturn("childconceptid2");
		Mockito.when(gincoIdMapParser.getNewId("childconceptid1", idMapping))
				.thenReturn("childconceptid1");

		Mockito.when(gincoIdMapParser.getNewId("parentconceptid1", idMapping))
				.thenReturn("parentconceptid1");
		Mockito.when(gincoIdMapParser.getNewId("parentconceptid2", idMapping))
				.thenReturn("parentconceptid2");

		Mockito.when(gincoIdMapParser.getNewId("childconceptid3", idMapping))
				.thenReturn("newChildconceptid3");
		Mockito.when(gincoIdMapParser.getNewId("parentconceptid3", idMapping))
				.thenReturn("newParentconceptid3");

		Map<String, JaxbList<ConceptHierarchicalRelationship>> updatedRelations = gincoRelationshipIdGenerator
				.getIdsForHierarchicalRelations(relations, idMapping);

		Assert.assertNotNull(updatedRelations.get("newRelationId1"));
		List<ConceptHierarchicalRelationship> relations1 = updatedRelations
				.get("newRelationId1").getList();
		Assert.assertEquals(2, relations1.size());
		ConceptHierarchicalRelationship actualPair1 = relations1.get(0);
		Assert.assertTrue(actualPair1.getIdentifier().getChildconceptid()
				.equals("childconceptid1")
				|| actualPair1.getIdentifier().getChildconceptid()
						.equals("childconceptid2"));
		Assert.assertTrue(actualPair1.getIdentifier().getParentconceptid()
				.equals("parentconceptid1")
				|| actualPair1.getIdentifier().getParentconceptid()
						.equals("parentconceptid2"));

		Assert.assertNotNull(updatedRelations.get("newRelationId2"));
		List<ConceptHierarchicalRelationship> relations2 = updatedRelations
				.get("newRelationId2").getList();
		Assert.assertEquals(1, relations2.size());
		ConceptHierarchicalRelationship actualPair3 = relations2.get(0);
		Assert.assertEquals("newChildconceptid3", actualPair3.getIdentifier()
				.getChildconceptid());
		Assert.assertEquals("newParentconceptid3", actualPair3.getIdentifier()
				.getParentconceptid());

	}

}
