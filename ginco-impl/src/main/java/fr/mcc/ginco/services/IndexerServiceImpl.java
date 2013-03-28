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
package fr.mcc.ginco.services;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.log.Log;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("indexerService")
public class IndexerServiceImpl implements IIndexerService {

    @Log
    private Logger logger;

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;

    @Inject
    @Named("thesaurusTermService")
    private IThesaurusTermService thesaurusTermService;

    @Inject
    @Named("noteService")
    private INoteService noteService;

    @Value("${solr.url}")
    private String url;

    @Override
    public void removeTerm(ThesaurusTerm thesaurusTerm) throws TechnicalException {
        HttpSolrServer solrCore;

        try {
            solrCore = new HttpSolrServer(url);
        } catch (RuntimeException ex) {
            logger.warn("Solr seems to be not launched!");
            return;
        }

        try {
            solrCore.deleteById(thesaurusTerm.getIdentifier());
        } catch (SolrServerException e) {
            throw new TechnicalException("Error executing query for removing Term from index!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error during executing query for removing Term from index!", e);
        }
    }

    @Override
    public String search(String request) throws SolrServerException {

        HttpSolrServer solrCore;

        try {
            solrCore = new HttpSolrServer(url);
        } catch (RuntimeException ex) {
            logger.warn("Solr seems to be not launched!");
            return null;
        }

        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", "text:"+request);
        params.set("start", "0");

        QueryResponse response = solrCore.query(params);
        SolrDocumentList results = response.getResults();

        String result = "";

        for (int i = 0; i < results.size(); ++i) {
            result = result.concat(results.get(i).getFieldValue("identifier").toString()).concat("\r\n");
        }

        return result;
    }

    @Override
    public void removeConcept(ThesaurusConcept thesaurusConcept) throws TechnicalException {
        HttpSolrServer solrCore;

        try {
            solrCore = new HttpSolrServer(url);
        } catch (RuntimeException ex) {
            logger.warn("Solr seems to be not launched!");
            return;
        }

        try {
            solrCore.deleteById(thesaurusConcept.getIdentifier());
        } catch (SolrServerException e) {
            throw new TechnicalException("Error executing query for removing Concept from index!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error during executing query for removing Concept from index!", e);
        }
    }

    @Override
    public void addConcept(ThesaurusConcept thesaurusConcept) throws TechnicalException {
        addConcepts(Arrays.asList(thesaurusConcept), null);
    }

    private void addConcepts(List<ThesaurusConcept> thesaurusConcepts, HttpSolrServer server) throws TechnicalException {

        HttpSolrServer solrCore = server;

        if(solrCore == null) {
            try {
                solrCore = new HttpSolrServer(url);
            } catch (RuntimeException ex) {
                logger.warn("Solr seems to be not launched!");
                return;
            }
        }

        List<SolrInputDocument> concepts = new ArrayList<SolrInputDocument>();
        for(ThesaurusConcept concept : thesaurusConcepts) {

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("thesaurusId", concept.getThesaurusId());
            doc.addField("identifier", concept.getIdentifier());

            String prefLabel;

            try {
                prefLabel = thesaurusConceptService.getConceptPreferredTerm(concept.getIdentifier())
                        .getLexicalValue();
            } catch (BusinessException ex) {
                logger.warn(ex.getMessage());
                continue;
            }
            doc.addField("lexicalValue", prefLabel);

            List<String> notes = new ArrayList<String>();

            for(Note note : noteService.getConceptNotePaginatedList(concept.getIdentifier(), 0, 0)) {
                notes.add(note.getLexicalValue());
            }

            if(!notes.isEmpty()) {
                doc.addField("notes", notes);
            }

            concepts.add(doc);
        }

        try {
            if(!concepts.isEmpty()) {
                solrCore.add(concepts);
                solrCore.commit();
            }
        } catch (SolrServerException e) {
            throw new TechnicalException("Error during adding to SOLR!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error!", e);
        }
    }

    @Override
    public void forceIndexing() throws TechnicalException{

        HttpSolrServer solrCore;

        try {
            solrCore = new HttpSolrServer(url);
        } catch (RuntimeException ex) {
            logger.warn("Solr seems to be not launched!");
            return;
        }


        try {
            solrCore.deleteByQuery("*:*");
            solrCore.commit();
        } catch (SolrServerException e) {
            throw new TechnicalException("Error executing query for clearing SOLR index!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error during executing query for clearing SOLR index!", e);
        }

        addConcepts(thesaurusConceptService.getAllConcepts(), solrCore);
        addTerms(thesaurusTermService.getAllTerms(), solrCore);
    }

    @Override
    @Async
    public void addTerm(ThesaurusTerm thesaurusTerm) throws TechnicalException {
    	try {
    		addTerms(Arrays.asList(thesaurusTerm), null);
    	} catch (TechnicalException ex)
    	{
    		logger.error(ex.getMessage(),ex.getCause().getMessage());
    	}
    }

    private void addTerms(List<ThesaurusTerm> thesaurusTerms, HttpSolrServer server) {

        HttpSolrServer solrCore = server;

        if(solrCore == null) {
            try {
                solrCore = new HttpSolrServer(url);
            } catch (RuntimeException ex) {
                logger.warn("Solr seems to be not launched!");
                return;
            }
        }

        List<SolrInputDocument> terms = new ArrayList<SolrInputDocument>();
        for(ThesaurusTerm term : thesaurusTerms) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("thesaurusId", term.getThesaurusId());
            doc.addField("identifier", term.getIdentifier());
            doc.addField("lexicalValue", term.getLexicalValue());
            doc.addField("type", ThesaurusTerm.class.getSimpleName());

            List<String> notes = new ArrayList<String>();

            for(Note note : noteService.getTermNotePaginatedList(term.getIdentifier(), 0, 0)) {
                notes.add(note.getLexicalValue());
            }

            if(!notes.isEmpty()) {
                doc.addField("notes", notes);
            }

            terms.add(doc);
        }

        try {
            if(!terms.isEmpty()) {
                solrCore.add(terms);
                solrCore.commit();
            }
        } catch (SolrServerException e) {
            throw new TechnicalException("Error during adding to SOLR!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error!", e);
        }
    }
}
