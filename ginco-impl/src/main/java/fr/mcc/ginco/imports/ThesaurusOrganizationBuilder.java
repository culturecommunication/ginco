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
package fr.mcc.ginco.imports;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.ThesaurusOrganization;
import fr.mcc.ginco.log.Log;

/**
 * Builder in charge of building a thesaurus
 * 
 */
@Service("skosThesaurusOrganizationBuilder")
public class ThesaurusOrganizationBuilder extends AbstractBuilder {	

	/**
	 * Gets the thesaurus creator from the FOAF elements
	 * 
	 * @param skosThesaurus
	 * @param model
	 * @return
	 */
	public ThesaurusOrganization getCreator(Resource skosThesaurus, Model model) {
		Statement stmt = skosThesaurus.getProperty(DC.creator);
		if (stmt == null) {
			stmt = skosThesaurus.getProperty(DCTerms.creator);
		}
		if (stmt != null) {
			RDFNode node = stmt.getObject();
			if (node.isResource()) {
				Resource creatorResource = node.asResource();
				SimpleSelector organizationSelector = new SimpleSelector(
						creatorResource, null, (RDFNode) null) {
					public boolean selects(Statement s) {
						if (s.getObject().isResource()) {
							Resource res = s.getObject().asResource();
							return res.equals(FOAF.Organization);
						} else {
							return false;
						}
					}
				};

				StmtIterator iter = model.listStatements(organizationSelector);
				while (iter.hasNext()) {
					Statement orgStms = iter.next();
					Resource organizationRes = orgStms.getSubject()
							.asResource();
					Statement foafName = organizationRes.getProperty(FOAF.name);
					Statement foafHomepage = organizationRes
							.getProperty(FOAF.homepage);
					Statement foafEmail = organizationRes
							.getProperty(FOAF.mbox);
					ThesaurusOrganization org = new ThesaurusOrganization();
					if (foafName != null) {
						org.setName(foafName.getString());
					}
					if (foafHomepage != null) {
						org.setHomepage(foafHomepage.getString());
					}
					if (foafEmail != null) {
						org.setEmail(foafEmail.getString());
					}
					return org;
				}
			} else if (node.isLiteral()) {
				ThesaurusOrganization org = new ThesaurusOrganization();
				org.setName(stmt.getString());
				return org;
			}
		}

		return null;
	}

}
