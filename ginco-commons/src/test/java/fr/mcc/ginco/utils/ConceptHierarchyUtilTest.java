/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and abiding
 * by the rules of distribution of free software. You can use, modify and/ or
 * redistribute the software under the terms of the CeCILL license as circulated
 * by CEA, CNRS and INRIA at the following URL "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systemsand/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.utils;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;

public class ConceptHierarchyUtilTest {

	@Test
	public void testGetSuperOrdinateStandard() {

		ThesaurusConcept c1 = new ThesaurusConcept();
		ThesaurusConcept c2 = new ThesaurusConcept();
		ThesaurusConcept c3 = new ThesaurusConcept();

		ThesaurusConcept c21 = new ThesaurusConcept();
		Set<ThesaurusConcept> c21Parents = new HashSet<ThesaurusConcept>();
		c21Parents.add(c1);
		c21Parents.add(c2);
		c21.setParentConcepts(c21Parents);

		ThesaurusConcept c22 = new ThesaurusConcept();
		Set<ThesaurusConcept> c22Parents = new HashSet<ThesaurusConcept>();
		c22Parents.add(c2);
		c22.setParentConcepts(c22Parents);

		ThesaurusConcept c23 = new ThesaurusConcept();
		Set<ThesaurusConcept> c23Parents = new HashSet<ThesaurusConcept>();
		c23Parents.add(c2);
		c23Parents.add(c3);
		c23.setParentConcepts(c23Parents);

		Set<ThesaurusConcept> membersConcepts = new HashSet<ThesaurusConcept>();
		membersConcepts.add(c21);
		membersConcepts.add(c22);
		membersConcepts.add(c23);

		ThesaurusConcept actualSuperOrdinateConcept = ConceptHierarchyUtil
				.getSuperOrdinate(membersConcepts);
		Assert.assertEquals(c2, actualSuperOrdinateConcept);

		Set<ThesaurusConcept> rootsConcepts = new HashSet<ThesaurusConcept>();
		membersConcepts.add(c1);
		membersConcepts.add(c2);
		membersConcepts.add(c3);
		ThesaurusConcept rootsSuperOrdinateConcept = ConceptHierarchyUtil
				.getSuperOrdinate(rootsConcepts);

		Assert.assertNull(rootsSuperOrdinateConcept);
	}
	
	@Test(expected = BusinessException.class)
	public void testGetSuperOrdinateNoSuperOrdinate() {
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("c1");
		
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("c2");

		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("c3");

		ThesaurusConcept c21 = new ThesaurusConcept();
		c21.setIdentifier("c21");
		Set<ThesaurusConcept> c21Parents = new HashSet<ThesaurusConcept>();
		c21Parents.add(c1);
		c21Parents.add(c2);
		c21.setParentConcepts(c21Parents);

		ThesaurusConcept c22 = new ThesaurusConcept();
		c22.setIdentifier("c22");
		Set<ThesaurusConcept> c22Parents = new HashSet<ThesaurusConcept>();
		c22Parents.add(c2);
		c22.setParentConcepts(c22Parents);

		ThesaurusConcept c23 = new ThesaurusConcept();
		c23.setIdentifier("c23");
		Set<ThesaurusConcept> c23Parents = new HashSet<ThesaurusConcept>();
		c23Parents.add(c3);
		c23.setParentConcepts(c23Parents);

		Set<ThesaurusConcept> membersConcepts = new HashSet<ThesaurusConcept>();
		membersConcepts.add(c21);
		membersConcepts.add(c22);
		membersConcepts.add(c23);
		try {
		ConceptHierarchyUtil
				.getSuperOrdinate(membersConcepts);
		} catch (BusinessException be) {
			Assert.assertEquals("import-no-superordinate", be.getUserMessageKey());
			throw be;
		}
	}
	
	@Test(expected = BusinessException.class)
	public void testMoreThanOneSuperOrdinate() {
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("c1");
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("c2");
		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("c3");


		ThesaurusConcept c21 = new ThesaurusConcept();
		c21.setIdentifier("c21");
		Set<ThesaurusConcept> c21Parents = new HashSet<ThesaurusConcept>();
		c21Parents.add(c1);
		c21Parents.add(c2);
		c21.setParentConcepts(c21Parents);

		ThesaurusConcept c22 = new ThesaurusConcept();
		c22.setIdentifier("c22");
		Set<ThesaurusConcept> c22Parents = new HashSet<ThesaurusConcept>();
		c22Parents.add(c2);
		c22Parents.add(c1);
		c22Parents.add(c3);
		c22.setParentConcepts(c22Parents);

		ThesaurusConcept c23 = new ThesaurusConcept();
		c23.setIdentifier("c23");
		Set<ThesaurusConcept> c23Parents = new HashSet<ThesaurusConcept>();
		c23Parents.add(c2);
		c23Parents.add(c1);
		c23Parents.add(c3);
		c23.setParentConcepts(c23Parents);

		Set<ThesaurusConcept> membersConcepts = new HashSet<ThesaurusConcept>();
		membersConcepts.add(c21);
		membersConcepts.add(c22);
		membersConcepts.add(c23);
		try {
			ConceptHierarchyUtil
				.getSuperOrdinate(membersConcepts);
		} catch (BusinessException be) {
			Assert.assertEquals("import-too-many-superordinate", be.getUserMessageKey());
			throw be;
			
		}
	}
	
	@Test
	public void testOneSuperOrdinate() {
		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("c1");
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("c2");
		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("c3");


		ThesaurusConcept c21 = new ThesaurusConcept();
		c21.setIdentifier("c21");
		Set<ThesaurusConcept> c21Parents = new HashSet<ThesaurusConcept>();
		c21Parents.add(c1);
		c21Parents.add(c2);
		c21.setParentConcepts(c21Parents);

		ThesaurusConcept c22 = new ThesaurusConcept();
		c22.setIdentifier("c22");
		Set<ThesaurusConcept> c22Parents = new HashSet<ThesaurusConcept>();
		c22Parents.add(c2);
		c22Parents.add(c1);
		c22Parents.add(c3);
		c22.setParentConcepts(c22Parents);

		ThesaurusConcept c23 = new ThesaurusConcept();
		c23.setIdentifier("c23");
		Set<ThesaurusConcept> c23Parents = new HashSet<ThesaurusConcept>();
		c23Parents.add(c2);
		c23.setParentConcepts(c23Parents);

		Set<ThesaurusConcept> membersConcepts = new HashSet<ThesaurusConcept>();
		membersConcepts.add(c21);
		membersConcepts.add(c22);
		membersConcepts.add(c23);

		ThesaurusConcept superOrdinate = ConceptHierarchyUtil
				.getSuperOrdinate(membersConcepts);
		Assert.assertEquals("c2", superOrdinate.getIdentifier());
		
	}

}
