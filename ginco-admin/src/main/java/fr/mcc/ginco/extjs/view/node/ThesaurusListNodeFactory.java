package fr.mcc.ginco.extjs.view.node;

import java.text.Collator;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "thesaurusListNodeFactory")
public class ThesaurusListNodeFactory {

	@Value("${ginco.default.language}")
	private String defaultLang;
    
    private Collator collator;
    
    private Collator getCollator()
    {
    	if (collator==null)
    	{
    		collator = Collator.getInstance(new Locale(defaultLang));
    	} 
		return collator;
    }
    
    public ThesaurusListBasicNode getListBasicNode()
    {
    	return new ThesaurusListBasicNode(getCollator());
    }
}
