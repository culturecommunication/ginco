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

import org.apache.solr.client.solrj.SolrServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.services.INoteService;
import fr.mcc.ginco.services.ISplitNonPreferredTermService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.solr.IComplexConceptIndexerService;
import fr.mcc.ginco.solr.IConceptIndexerService;
import fr.mcc.ginco.solr.INoteIndexerService;
import fr.mcc.ginco.solr.ITermIndexerService;
import fr.mcc.ginco.solr.IndexerServiceImpl;

public class IndexerServiceTest {

	@InjectMocks
	private IndexerServiceImpl indexerService;

	@Mock(name = "thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Mock(name = "thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Mock(name = "noteService")
	private INoteService noteService;

	@Mock(name = "splitNonPreferredTermService")
	private ISplitNonPreferredTermService splitNonPreferredTermService;

	@Mock(name = "termIndexerService")
	private ITermIndexerService termIndexerService;

	@Mock(name = "conceptIndexerService")
	private IConceptIndexerService conceptIndexerService;

	@Mock(name = "noteIndexerService")
	private INoteIndexerService noteIndexerService;

	@Mock(name = "complexConceptIndexerService")
	private IComplexConceptIndexerService complexConceptIndexerService;

	@Mock(name = "solrServer")
	private SolrServer solrServer;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testForceIndexing() throws TechnicalException {

		indexerService.forceIndexing();

		verify(termIndexerService).addTerms(Mockito.anyListOf(ThesaurusTerm.class));
		verify(conceptIndexerService).addConcepts(Mockito.anyListOf(ThesaurusConcept.class));
		verify(noteIndexerService).addNotes(Mockito.anyListOf(Note.class));
		verify(complexConceptIndexerService).addComplexConcepts(Mockito.anyListOf(SplitNonPreferredTerm.class));
	}

}
