package fr.mcc.ginco.data;

import java.util.List;

public class ReducedThesaurusArray {
	
	private String identifier;
	
	private String title;
	
	private Boolean ordered;
	
	private List<ReducedThesaurusTerm> terms;
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ReducedThesaurusTerm> getTerms() {
		return terms;
	}

	public void setTerms(List<ReducedThesaurusTerm> terms) {
		this.terms = terms;
	}

	public Boolean getOrdered() {
		return ordered;
	}

	public void setOrdered(Boolean ordered) {
		this.ordered = ordered;
	}



}
