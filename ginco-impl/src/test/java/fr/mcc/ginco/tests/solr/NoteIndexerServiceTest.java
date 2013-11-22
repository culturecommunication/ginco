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
package fr.mcc.ginco.tests.solr;

import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.solr.NoteIndexerServiceImpl;
import fr.mcc.ginco.solr.NoteSolrConverter;

public class NoteIndexerServiceTest {

	@InjectMocks
	private  NoteIndexerServiceImpl noteIndexerService;

	@Mock
	private NoteSolrConverter noteSolrConverter;

	@Mock(name = "solrServer")
	private SolrServer solrServer;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddNote() throws SolrServerException, IOException {

		Note fakeConceptNote = new Note();
		fakeConceptNote.setIdentifier("http://note1");

		SolrInputDocument doc = new SolrInputDocument();
		Mockito.when(noteSolrConverter.convertSolrNote(fakeConceptNote)).thenReturn(doc);

		noteIndexerService.addNote(fakeConceptNote);

		verify(solrServer).add(doc);
		verify(solrServer).commit();
	}

	@Test
	public void testRemoveNote() throws SolrServerException, IOException {
		Note fakeConceptNote = new Note();
		fakeConceptNote.setIdentifier("http://note1");

		noteIndexerService.removeNote(fakeConceptNote);

		verify(solrServer).deleteById("http://note1");
		verify(solrServer).commit();
	}
}
