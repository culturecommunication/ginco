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
package fr.mcc.ginco.exports.skos;

import javax.inject.Inject;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting a thesaurus data SKOS
 * 
 */
@Component("skosThesaurusExporter")
public class SKOSThesaurusExporter {

	@Inject
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;

	private static final String SEPARATOR = "\\r?\\n";

	/**
	 * Export the thesaurus data skos API
	 * 
	 * @param thesaurus
	 * @return
	 */
	public Model exportThesaurusSKOS(Thesaurus thesaurus, Model model) {

		Resource thesaurusResource = model.createResource(
				thesaurus.getIdentifier(), SKOS.CONCEPTSCHEME);

		model.add(thesaurusResource, DCTerms.created,
				DateUtil.toISO8601String(thesaurus.getCreated()));
		model.add(thesaurusResource, DCTerms.modified,
				DateUtil.toISO8601String(thesaurus.getDate()));

		model.add(thesaurusResource, DC.title, thesaurus.getTitle());

		if (thesaurus.getCreator() != null) {

			Resource foafOrgResource = model.createResource(FOAF.Organization);

			String creatorName = thesaurus.getCreator().getName();
			String creatorHomepage = thesaurus.getCreator().getHomepage();
			String creatorEmail = thesaurus.getCreator().getEmail();

			if (creatorName != null && !creatorName.isEmpty()) {
				foafOrgResource.addProperty(FOAF.name, creatorName);
			}
			if (creatorHomepage != null && !creatorHomepage.isEmpty()) {
				foafOrgResource.addProperty(FOAF.homepage, creatorHomepage);
			}
			if (creatorEmail != null && !creatorEmail.isEmpty()) {
				foafOrgResource.addProperty(FOAF.mbox, creatorEmail);
			}
			thesaurusResource.addProperty(DC.creator, foafOrgResource);

		}

		model.add(thesaurusResource, DC.rights, thesaurus.getRights());

		model.add(thesaurusResource, DC.description, thesaurus.getDescription());

		model.add(thesaurusResource, DC.relation, thesaurus.getRelation());

		model.add(thesaurusResource, DC.source, thesaurus.getSource());

		model.add(thesaurusResource, DC.publisher, thesaurus.getPublisher());

		if (thesaurus.getType() != null) {
			model.add(thesaurusResource, DC.type, thesaurus.getType()
					.getLabel());
		}

		String[] contributors = thesaurus.getContributor().split(SEPARATOR);
		for (String contributor : contributors) {
			model.add(thesaurusResource, DC.contributor, contributor);
		}

		String[] coverages = thesaurus.getCoverage().split(SEPARATOR);
		for (String coverage : coverages) {
			model.add(thesaurusResource, DC.coverage, coverage);
		}

		String[] subjects = thesaurus.getSubject().split(SEPARATOR);
		for (String subject : subjects) {
			model.add(thesaurusResource, DC.subject, subject);
		}

		for (Language lang : thesaurus.getLang()) {

			model.add(thesaurusResource, DC.language, lang.getId());
		}

		String currentVersionValue = thesaurusVersionHistoryDAO
				.findThisVersionByThesaurusId(thesaurus.getIdentifier())
				.getVersionNote();
		if (StringUtils.isNotEmpty(currentVersionValue)) {
			model.add(thesaurusResource, DCTerms.issued, currentVersionValue);
		}

		return model;

	}

}
