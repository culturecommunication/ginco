package fr.mcc.ginco.solr;

import java.util.ArrayList;

public class SearchResultList  extends ArrayList<SearchResult> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long numFound;

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long l) {
		this.numFound = l;
	}


}
