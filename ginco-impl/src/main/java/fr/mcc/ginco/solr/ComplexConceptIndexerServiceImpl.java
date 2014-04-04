package fr.mcc.ginco.solr;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.exceptions.TechnicalException;

@Service("complexConceptIndexerService")
public class ComplexConceptIndexerServiceImpl implements IComplexConceptIndexerService {

	private static Logger logger = LoggerFactory.getLogger(ComplexConceptIndexerServiceImpl.class);

	@Inject
	private ComplexConceptSolrConverter complexConceptSolrConverter;

	@Inject
	@Named("solrServer")
	private SolrServer solrServer;

	@Override
	@Async
	public void addComplexConcept(SplitNonPreferredTerm complexConcept) throws TechnicalException {
		try {
			addComplexConcepts(Arrays.asList(complexConcept));
		} catch (TechnicalException ex) {
			logger.error(ex.getMessage(), ex.getCause().getMessage());
			throw ex;
		}
	}

	@Override
	public void addComplexConcepts(List<SplitNonPreferredTerm> complexConcepts) throws TechnicalException {
		for (SplitNonPreferredTerm complexConcept : complexConcepts) {
			SolrInputDocument doc = complexConceptSolrConverter.convertSolrComplexConcept(complexConcept);
			if (doc != null) {
				try {
					logger.info("Indexing complex concept :" + complexConcept.getIdentifier());
					solrServer.add(doc);
				} catch (SolrServerException e) {
					throw new TechnicalException(
							"Error during adding to SOLR!", e);
				} catch (IOException e) {
					throw new TechnicalException("IO error!", e);
				}
			}
		}
		try {
			solrServer.commit();
		} catch (SolrServerException e) {
			throw new TechnicalException("Error during adding to SOLR!", e);
		} catch (IOException e) {
			throw new TechnicalException("IO error!", e);
		}
	}

	@Override
	@Async
	public void removeComplexConcept(SplitNonPreferredTerm complexConcept) throws TechnicalException {
		try {
			solrServer.deleteById(complexConcept.getIdentifier());
			solrServer.commit();
		} catch (SolrServerException e) {
			throw new TechnicalException(
					"Error executing query for removing complex concept from index!", e);
		} catch (IOException e) {
			throw new TechnicalException(
					"IO error during executing query for removing complex concept from index!",
					e);
		}
	}
}
