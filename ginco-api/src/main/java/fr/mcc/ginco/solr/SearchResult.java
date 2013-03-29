package fr.mcc.ginco.solr;

public class SearchResult {
	
	private String identifier;
	private String thesaurusId;
	private String type;
	private String lexicalValue;
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getThesaurusId() {
		return thesaurusId;
	}
	
	public void setThesaurusId(String thesaurusId) {
		this.thesaurusId = thesaurusId;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getLexicalValue() {
		return lexicalValue;
	}
	
	public void setLexicalValue(String lexicalValue) {
		this.lexicalValue = lexicalValue;
	}

}
