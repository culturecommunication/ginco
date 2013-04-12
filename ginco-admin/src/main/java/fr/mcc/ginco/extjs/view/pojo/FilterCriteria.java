package fr.mcc.ginco.extjs.view.pojo;

public class FilterCriteria {

	private String query;
	private String thesaurus;
	private Integer type;
	private Integer status;
	private String creationdate;
	private String language;
	private String modificationdate;
	private int start;
	private int limit;
	
	public String getModificationdate() {
		return modificationdate;
	}
	public void setModificationdate(String modificationdate) {
		this.modificationdate = modificationdate;
	}
	public String getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getThesaurus() {
		return thesaurus;
	}
	public void setThesaurus(String thesaurus) {
		this.thesaurus = thesaurus;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
