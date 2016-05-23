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
package fr.mcc.ginco.rest.services;

import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.FilterCriteria;
import fr.mcc.ginco.solr.ISearcherService;
import fr.mcc.ginco.solr.SearchResult;
import fr.mcc.ginco.solr.SearchResultList;
import fr.mcc.ginco.solr.SortCriteria;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Base REST service intended to be used for Solr search
 */

@Service
@Path("/searcherservice")
public class SearcherRestService {

	@Inject
	@Named("searcherService")
	private ISearcherService searcherService;

	@POST
	@Path("/search")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ExtJsonFormLoadData<List<SearchResult>> search(FilterCriteria filter) {
		try {
			SortCriteria sort = new SortCriteria(filter.getSortfield(), filter.getSortdir());
			SearchResultList searchResults =
					searcherService.search(StringEscapeUtils.unescapeXml(filter.getQuery()), filter.getType(),
					filter.getThesaurus(), filter.getStatus(),
					filter.getCreationdate(), filter.getModificationdate(),
					filter.getLanguage(), sort, filter.getStart(), filter.getLimit());

			ExtJsonFormLoadData<List<SearchResult>> extSearchResults = new ExtJsonFormLoadData<List<SearchResult>>(searchResults);
			extSearchResults.setTotal(searchResults.getNumFound());
			return extSearchResults;
		} catch (SolrServerException e) {
			throw new TechnicalException("Search exception", e);
		}
	}
}
