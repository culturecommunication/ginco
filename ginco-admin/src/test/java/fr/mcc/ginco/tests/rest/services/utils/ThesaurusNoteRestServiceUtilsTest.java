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
package fr.mcc.ginco.tests.rest.services.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.enums.ConceptStatusEnum;
import fr.mcc.ginco.rest.services.utils.ThesaurusNoteRestServiceUtils;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.services.IUserRoleService;

public class ThesaurusNoteRestServiceUtilsTest {

	@InjectMocks
	private ThesaurusNoteRestServiceUtils thesaurusNoteRestServiceUtil;

	@Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name="thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Mock(name="userRoleService")
	private IUserRoleService userRoleService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected=AccessDeniedException.class)
	public final void testCheckExpertAccessToConceptNote(){

		Authentication auth = new TestingAuthenticationToken("username",
				"password");
		SecurityContextHolder.getContext().setAuthentication(auth);

		Thesaurus thesaurus = new Thesaurus();
		thesaurus.setIdentifier("http://thesaurus");

		ThesaurusConcept concept = new ThesaurusConcept();
		concept.setIdentifier("http://concept");
		concept.setThesaurus(thesaurus);
		concept.setStatus(ConceptStatusEnum.VALIDATED.getStatus());

		Note fakeNote = new Note();
		fakeNote.setConcept(concept);

		Mockito.when(
				userRoleService.hasRole("username",
						"http://thesaurus",
						Role.EXPERT)).thenReturn(true);

		Mockito.when(thesaurusConceptService
				.getThesaurusConceptById("http://concept")).thenReturn(concept);

		thesaurusNoteRestServiceUtil.checkExpertAccessToNote(fakeNote);

	}

	@Test(expected=AccessDeniedException.class)
	public final void testCheckExpertAccessToTermNote(){

		Authentication auth = new TestingAuthenticationToken("username",
				"password");
		SecurityContextHolder.getContext().setAuthentication(auth);

		Thesaurus thesaurus = new Thesaurus();
		thesaurus.setIdentifier("http://thesaurus");

		ThesaurusTerm term = new ThesaurusTerm();
		term.setIdentifier("http://term");
		term.setThesaurus(thesaurus);
		term.setStatus(ConceptStatusEnum.VALIDATED.getStatus());

		Note fakeNote = new Note();
		fakeNote.setTerm(term);

		Mockito.when(
				userRoleService.hasRole("username",
						"http://thesaurus",
						Role.EXPERT)).thenReturn(true);

		Mockito.when(thesaurusTermService
				.getThesaurusTermById("http://term")).thenReturn(term);

		thesaurusNoteRestServiceUtil.checkExpertAccessToNote(fakeNote);
	}
}

