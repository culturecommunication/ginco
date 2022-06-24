package fr.mcc.ginco.cli;


import fr.mcc.ginco.solr.IIndexerService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Cli indexer service.
 */
@Service
public class CliIndexer {

	@Inject
	@Named("indexerService")
	private IIndexerService indexerService;



	/**
	 * SKOS import from file.

	 */
	public void reindex() {
			indexerService.forceIndexing();
	}
}
