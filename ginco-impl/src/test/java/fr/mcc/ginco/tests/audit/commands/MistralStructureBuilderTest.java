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
package fr.mcc.ginco.tests.audit.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.audit.commands.MistralStructuresBuilder;
import fr.mcc.ginco.audit.utils.AuditHelper;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;

public class MistralStructureBuilderTest {

	@Mock(name = "auditHelper")
	private AuditHelper auditHelper;

	@InjectMocks
	private MistralStructuresBuilder mistralStructuresBuilder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuildHierarchyStructure() {
		Number revision = 0;
		String lang = "fr-FR";

		List<ThesaurusConcept> conceptsAtRevision = new ArrayList<ThesaurusConcept>();
		ThesaurusConcept parentConcept = new ThesaurusConcept();
		parentConcept.setIdentifier("parentConcept");
		conceptsAtRevision.add(parentConcept);

		List<ThesaurusConcept> childrenConcept = new ArrayList<ThesaurusConcept>();
		ThesaurusConcept childConcept1 = new ThesaurusConcept();
		childConcept1.setIdentifier("childConcept1");
		childrenConcept.add(childConcept1);
		ThesaurusConcept childConcept2 = new ThesaurusConcept();
		childConcept2.setIdentifier("childConcept2");
		childrenConcept.add(childConcept2);

		ThesaurusTerm parentTerm = new ThesaurusTerm();
		parentTerm.setLexicalValue("édifices religieux");
		Mockito.when(
				auditHelper.getPreferredTermAtRevision(revision,
						"parentConcept", lang)).thenReturn(parentTerm);

		Mockito.when(
				auditHelper.getConceptChildrenAtRevision(revision,
						parentConcept)).thenReturn(childrenConcept);

		ThesaurusTerm child1 = new ThesaurusTerm();
		child1.setLexicalValue("abbaye");

		ThesaurusTerm child2 = new ThesaurusTerm();
		child2.setLexicalValue("temple");

		Mockito.when(
				auditHelper.getPreferredTermAtRevision(revision,
						childConcept1.getIdentifier(), lang))
				.thenReturn(child1);

		Mockito.when(
				auditHelper.getPreferredTermAtRevision(revision,
						childConcept2.getIdentifier(), lang))
				.thenReturn(child2);

		Map<String, List<String>> hierarchies = mistralStructuresBuilder
				.buildHierarchyStructure(conceptsAtRevision, revision, lang);

		Assert.assertEquals(1, hierarchies.size());
		Assert.assertEquals("édifices religieux", hierarchies.keySet()
				.iterator().next());
		List<String> children = hierarchies.get("édifices religieux");
		Assert.assertEquals(2, children.size());
		ListAssert.assertContains(children, "abbaye");
		ListAssert.assertContains(children, "temple");
	}
	
	@Test
	public void testBuildSynonymsStructure() {
		Number revision = 0;
		String lang = "fr-FR";

		List<ThesaurusConcept> conceptsAtRevision = new ArrayList<ThesaurusConcept>();
		ThesaurusConcept parentConcept = new ThesaurusConcept();
		parentConcept.setIdentifier("parentConcept");
		conceptsAtRevision.add(parentConcept);

		ThesaurusTerm prefTerm = new ThesaurusTerm();
		prefTerm.setLexicalValue("liste");
		
		ThesaurusTerm syn1 = new ThesaurusTerm();
		syn1.setLexicalValue("catalogue");
		
		ThesaurusTerm syn2 = new ThesaurusTerm();
		syn2.setLexicalValue("énumération");
		
		List<ThesaurusTerm> allTerms = new ArrayList<ThesaurusTerm>();
		allTerms.add(prefTerm);
		allTerms.add(syn1);
		allTerms.add(syn2);

		Mockito.when(auditHelper
				.getPreferredTermAtRevision(revision,
						parentConcept.getIdentifier(), lang)).thenReturn(prefTerm);
		
		Mockito.when(auditHelper
				.getConceptTermsAtRevision(parentConcept, revision)).thenReturn(allTerms);
		
		Map<String, List<String>> synonyms = mistralStructuresBuilder.buildSynonymsStructure(conceptsAtRevision, revision, lang);
		
		Assert.assertEquals(1, synonyms.size());
		Assert.assertEquals("liste", synonyms.keySet()
				.iterator().next());
		List<String> children = synonyms.get("liste");
		Assert.assertEquals(2, children.size());
		ListAssert.assertContains(children, "catalogue");
		ListAssert.assertContains(children, "énumération");
	}
	
	@Test
	public void testGetTermVersionsViewMap() {
		
		List<ThesaurusTerm> currentTerms = new ArrayList<ThesaurusTerm>();
		ThesaurusTerm term1 = new ThesaurusTerm();
		term1.setLexicalValue("term1");
		currentTerms.add(term1);
		
		ThesaurusTerm term2 = new ThesaurusTerm();
		term2.setLexicalValue("term2");
		currentTerms.add(term2);	
		
		Map<String, ThesaurusTerm> terms = mistralStructuresBuilder.getTermVersionsView(currentTerms);
		
		Assert.assertEquals(2,  terms.size());
		Assert.assertEquals(term1, terms.get("term1"));
		Assert.assertEquals(term2, terms.get("term2"));

	}

}
