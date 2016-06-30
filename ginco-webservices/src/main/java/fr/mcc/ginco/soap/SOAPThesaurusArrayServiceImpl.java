package fr.mcc.ginco.soap;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.data.ReducedThesaurus;
import fr.mcc.ginco.data.ReducedThesaurusArray;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;


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
			results.add(reducedArray);
		}
		return results;
	}

}
