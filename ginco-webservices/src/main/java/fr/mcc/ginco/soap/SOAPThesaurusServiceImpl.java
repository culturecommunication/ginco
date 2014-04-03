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
package fr.mcc.ginco.soap;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;


import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.data.FullThesaurus;
import fr.mcc.ginco.data.ReducedThesaurus;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This class is the implementation of all SOAP services related to thesaurus objects
 * 
 */
@WebService(endpointInterface="fr.mcc.ginco.soap.ISOAPThesaurusService")
public class SOAPThesaurusServiceImpl implements ISOAPThesaurusService{	
	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	/**
	 * This service returns a list of all existing thesaurus in a simplified
	 * format (id and title)
	 * 
	 * @return the list of all existing thesaurus
	 */
	@Override
	public final List<ReducedThesaurus> getAllThesaurus() {
		List<ReducedThesaurus> results = new ArrayList<ReducedThesaurus>();

		for (Thesaurus thesaurus : thesaurusService.getThesaurusList()) {
			ReducedThesaurus reducedThesaurus = new ReducedThesaurus();
			reducedThesaurus.setIdentifier(thesaurus.getIdentifier());
			reducedThesaurus.setTitle(thesaurus.getTitle());
			results.add(reducedThesaurus);
		}
		return results;
	}

	/**
	 * This service returns the details of a thesaurus
	 * 
	 * @param id
	 *            {@link String} identifier of the thesaurus to get - mandatory
	 * 
	 * @return full thesaurus informations
	 */
	@Override
	public final FullThesaurus getThesaurusById(String id) {
		Thesaurus thesaurus = thesaurusService.getThesaurusById(id);
		FullThesaurus fullThesaurus = new FullThesaurus();
		fullThesaurus.setContributor(thesaurus.getContributor());
		fullThesaurus.setCoverage(thesaurus.getCoverage());
		fullThesaurus.setCreated(DateUtil.toString(thesaurus.getCreated()));
		if (thesaurus.getCreator() != null) {
			fullThesaurus.setCreatorName(thesaurus.getCreator().getName());
			fullThesaurus.setCreatorHomepage(thesaurus.getCreator()
					.getHomepage());
			fullThesaurus.setCreatorEmail(thesaurus.getCreator().getEmail());
		}
		fullThesaurus.setModified(DateUtil.toString(thesaurus.getDate()));
		fullThesaurus.setDescription(thesaurus.getDescription());
		
		if (thesaurus.getFormat() != null){
			List<String> formatList = new ArrayList<String>();
			for (ThesaurusFormat format : thesaurus.getFormat()) {
				formatList.add(format.getLabel());
			}
			fullThesaurus.setFormats(formatList);
		}
		fullThesaurus.setIdentifier(thesaurus.getIdentifier());
		List<String> langList = new ArrayList<String>();
		for (Language lang : thesaurus.getLang()) {
			langList.add(lang.getId());
		}
		fullThesaurus.setLanguages(langList);
		fullThesaurus.setPublisher(thesaurus.getPublisher());
		fullThesaurus.setRelation(thesaurus.getRelation());
		fullThesaurus.setRights(thesaurus.getRights());
		fullThesaurus.setSource(thesaurus.getSource());
		fullThesaurus.setSubject(thesaurus.getSubject());
		fullThesaurus.setTitle(thesaurus.getTitle());
		if (thesaurus.getType() != null) {
			fullThesaurus.setType(thesaurus.getType().getLabel());
		}
		return fullThesaurus;
	}	

	public final void setThesaurusService(IThesaurusService thesaurusService) {
		this.thesaurusService = thesaurusService;
	}

}
