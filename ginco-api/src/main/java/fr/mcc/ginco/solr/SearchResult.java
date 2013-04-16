package fr.mcc.ginco.solr;

import java.util.List;

public class SearchResult {
	
	private String identifier;
	private String thesaurusId;
	private String thesaurusTitle;
	private String type;
	private String lexicalValue;
    private String typeExt;
    private String modified;
    private String created;
    private String status;
	private List<String> languages; 
	
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

	public String getThesaurusTitle() {
		return thesaurusTitle;
	}

	public void setThesaurusTitle(String thesaurusTitle) {
		this.thesaurusTitle = thesaurusTitle;
	}

    public String getTypeExt() {
        return typeExt;
    }

    public void setTypeExt(String typeExt) {
        this.typeExt = typeExt;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
}
