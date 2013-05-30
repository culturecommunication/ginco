package fr.mcc.ginco.solr;

public class SortCriteria {
	
	private String sortField;
	private String sortDir = SolrConstants.DESCENDING;
	private String defaultSortString = SolrConstants.SCORE + " "+ SolrConstants.DESCENDING;
	
	public SortCriteria(String sortfield, String direction)
	{
		if (sortfield != null && direction != null)  {
				sortField = SolrField.getCheckedValue(sortfield);
				if (sortField.equals(SolrField.LEXICALVALUE))
				{
					sortField += "_sort";
				}
				if (direction.toLowerCase().equals(SolrConstants.DESCENDING) ||
						direction.toLowerCase().equals(SolrConstants.ASCENDING)	) {
					sortDir = direction.toLowerCase();
				}
		}
		
	}
	
	public String getSolrSortString()
	{
		if (sortField != null  && sortDir!=null)
		{
			return sortField + " "+ sortDir+" ,"+ defaultSortString;
		} else	
		return defaultSortString;
	}

}
