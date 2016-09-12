package fr.mcc.ginco.soap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.data.ReducedThesaurusArray;
import fr.mcc.ginco.data.ReducedThesaurusTerm;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.IThesaurusConceptService;


/**
 * This class is the implementation of all SOAP services related to thesaurus objects
 */
@WebService(endpointInterface = "fr.mcc.ginco.soap.ISOAPThesaurusArrayService")
public class SOAPThesaurusArrayServiceImpl implements ISOAPThesaurusArrayService {
	
	@Inject
	private IThesaurusArrayService thesaurusArrayService;
	
	@Inject
	@Named("nodeLabelService")
	private INodeLabelService nodeLabelService;
	
	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;
	
	

	@Override
	public List<ReducedThesaurusArray> getThesaurusArraysByThesaurusId(String thesaurusId) {
		List<ReducedThesaurusArray> results = new ArrayList<ReducedThesaurusArray>();
		List<ThesaurusArray> arrays = thesaurusArrayService.getAllThesaurusArrayByThesaurusId(null, thesaurusId);
		for (ThesaurusArray array : arrays) {
			NodeLabel label = nodeLabelService
					.getByThesaurusArrayAndLanguage(array.getIdentifier());
			ReducedThesaurusArray reducedArray = new ReducedThesaurusArray();
			reducedArray.setIdentifier(array.getIdentifier());
			reducedArray.setTitle(label.getLexicalValue());
			reducedArray.setOrdered(array.getOrdered());
			results.add(reducedArray);
		}
		return results;
	}
	
	public void getTermsByThesaurusArray(ReducedThesaurusArray redThesArray) {
		ThesaurusArray thesArray = thesaurusArrayService.getThesaurusArrayById(redThesArray.getIdentifier());
		List<ThesaurusArrayConcept> thesConcepts = new ArrayList<ThesaurusArrayConcept>(thesArray.getConcepts());
		List<ReducedThesaurusTerm> list = new ArrayList<ReducedThesaurusTerm> ();

		if (thesArray.getOrdered()) {
			Collections.sort(thesConcepts, new Comparator<ThesaurusArrayConcept>() {
				public int compare(ThesaurusArrayConcept t1, ThesaurusArrayConcept t2) {
					return Integer.compare(t1.getArrayOrder(), t2.getArrayOrder());
				}
				});
		}
		for (ThesaurusArrayConcept thesConcept : thesConcepts) {
			
			ThesaurusTerm prefTerm = thesaurusConceptService.getConceptPreferredTerm(thesConcept.getIdentifier().getConceptId());
			if (prefTerm != null) {
				list.add(ReducedThesaurusTerm.getReducedThesaurusTerm(prefTerm));
			}

		}
		redThesArray.setTerms(list);
		
	}



	@Override
	public List<ReducedThesaurusArray> getThesaurusArraysWithTermsByThesaurusId(String thesaurusId) {
		List<ReducedThesaurusArray> results = getThesaurusArraysByThesaurusId(thesaurusId);
		for (ReducedThesaurusArray array : results) {
			getTermsByThesaurusArray(array);
		}
		return results;
	}

	@Override
	public ReducedThesaurusArray getThesaurusArrayWithTerms(String thesaurusArrayId) {
		// TODO Auto-generated method stub
		ReducedThesaurusArray reducedArray = new ReducedThesaurusArray();
		ThesaurusArray array = thesaurusArrayService.getThesaurusArrayById(thesaurusArrayId);
		if (array != null)
		{
			NodeLabel label = nodeLabelService
					.getByThesaurusArrayAndLanguage(array.getIdentifier());
			reducedArray.setIdentifier(array.getIdentifier());
			reducedArray.setTitle(label.getLexicalValue());
			reducedArray.setOrdered(array.getOrdered());
			getTermsByThesaurusArray(reducedArray);
		} 
		return reducedArray;
	}

}
