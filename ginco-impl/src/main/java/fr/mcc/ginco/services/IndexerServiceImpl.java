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

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.log.Log;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly=true, rollbackFor = BusinessException.class)
@Service("indexerService")
public class IndexerServiceImpl implements IIndexerService {

    @Log
    Logger logger;

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;

    @Inject
    @Named("thesaurusTermService")
    private IThesaurusTermService thesaurusTermService;

    @Value("${solr.url}")
    private String url;

    @Override
    public void addConcept(ThesaurusConcept thesaurusConcept) throws TechnicalException {

        HttpSolrServer solrCore;

        try {
            solrCore = new HttpSolrServer(url);
        } catch (RuntimeException ex) {
            logger.warn("Solr seems to be not launched!");
            return;
        }

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("thesaurusId", thesaurusConcept.getThesaurusId());
        doc.addField("identifier", thesaurusConcept.getIdentifier());

        String prefLabel = thesaurusConceptService.getConceptPreferredTerm(thesaurusConcept.getIdentifier())
                .getLexicalValue();

        doc.addField("lexicalValue", prefLabel);
        doc.addField("type", ThesaurusConcept.class.getSimpleName());

        try {
            solrCore.add(doc);
        } catch (SolrServerException e) {
            throw new TechnicalException("Error during adding to SOLR!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error!", e);
        }
    }

    @Override
    public void forceIndexing() {

        HttpSolrServer solrCore;

        try {
            solrCore = new HttpSolrServer(url);
        } catch (RuntimeException ex) {
            logger.warn("Solr seems to be not launched!");
            return;
        }

        List<SolrInputDocument> concepts = new ArrayList<SolrInputDocument>();
        for(ThesaurusConcept concept : thesaurusConceptService.getAllConcepts()) {

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
            concepts.add(doc);
        }

        List<SolrInputDocument> terms = new ArrayList<SolrInputDocument>();
        for(ThesaurusTerm term : thesaurusTermService.getAllTerms()) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("thesaurusId", term.getThesaurusId());
            doc.addField("identifier", term.getIdentifier());
            doc.addField("lexicalValue", term.getLexicalValue());
            doc.addField("type", ThesaurusTerm.class.getSimpleName());
            terms.add(doc);
        }

        try {
            solrCore.add(terms);
            solrCore.add(concepts);
        } catch (SolrServerException e) {
            throw new TechnicalException("Error during adding to SOLR!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error!", e);
        }
    }

    @Override
    public void addTerm(ThesaurusTerm thesaurusTerm) throws TechnicalException {

        HttpSolrServer solrCore;

        try {
            solrCore = new HttpSolrServer(url);
        } catch (RuntimeException ex) {
            logger.warn("Solr seems to be not launched!");
            return;
        }

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("thesaurusId", thesaurusTerm.getThesaurusId());
        doc.addField("identifier", thesaurusTerm.getIdentifier());
        doc.addField("lexicalValue", thesaurusTerm.getLexicalValue());
        doc.addField("type", ThesaurusTerm.class.getSimpleName());

        try {
            solrCore.add(doc);
        } catch (SolrServerException e) {
            throw new TechnicalException("Error during adding to SOLR!", e);
        } catch (IOException e) {
            throw new TechnicalException("IO error!", e);
        }
    }
}
