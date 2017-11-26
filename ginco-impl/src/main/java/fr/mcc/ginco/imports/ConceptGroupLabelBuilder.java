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

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * Builder in charge of building the thesaurus concept groups node labels
 */
@Service("skosConceptGroupLabelBuilder")
public class ConceptGroupLabelBuilder extends AbstractBuilder {

	private static Logger logger = LoggerFactory.getLogger(ConceptGroupLabelBuilder.class);

	@Inject
	private ILanguageDAO languagesDAO;

	@Value("${ginco.default.language}")
	private String defaultLang;

	@Inject
	@Named("skosImportUtils")
	private SKOSImportUtils skosImportUtils;

	public ConceptGroupLabelBuilder() {
		super();
	}

	/**
	 * Builds a ThesaurusConceptGroupLabel object for the given group
	 *
	 * @param skosCollection
	 * @param array
	 * @return
	 */
	public ThesaurusConceptGroupLabel buildNodeLabel(Resource skosCollection, Thesaurus thesaurus, ThesaurusConceptGroup conceptGroup) {
		logger.debug("Building concept group label for group " + conceptGroup.getIdentifier());
		ThesaurusConceptGroupLabel groupLabel = new ThesaurusConceptGroupLabel();

		Statement stmtCreated = skosCollection.getProperty(DCTerms.created);
		Statement stmtModified = skosCollection.getProperty(DCTerms.modified);

		if (stmtCreated != null) {
			Date nodeLabelCreatedDate = skosImportUtils.getSkosDate(stmtCreated.getString());
			groupLabel.setCreated(nodeLabelCreatedDate);
			if (stmtModified != null) {
				groupLabel.setModified(skosImportUtils.getSkosDate(stmtModified.getString()));
			} else {
				groupLabel.setModified(nodeLabelCreatedDate);
			}
		} else {
			groupLabel.setCreated(thesaurus.getCreated());
			groupLabel.setModified(thesaurus.getDate());
		}

		// Statement stmtLabel = skosCollection.getProperty(SKOS.PREF_LABEL);
		Statement stmtLabel = null;
		
		// 1. first try to find a skos:prefLabel with the default language
		stmtLabel = getPropertyWithLang(skosCollection, SKOS.PREF_LABEL, defaultLang);
		
		// 2. second try to find a skos:prefLabel with the default language - language only
		if(stmtLabel == null) {
			stmtLabel = getPropertyWithLang(skosCollection, SKOS.PREF_LABEL, Locale.forLanguageTag(defaultLang).getLanguage());
		}
		
		// 3. default to a random value in any language
		if(stmtLabel == null) {
			stmtLabel = skosCollection.getProperty(SKOS.PREF_LABEL);
		}

		RDFNode prefLabel = stmtLabel.getObject();
		String lang = prefLabel.asLiteral().getLanguage();
		if (StringUtils.isEmpty(lang)) {
			Language defaultLangL = languagesDAO.getById(defaultLang);
			groupLabel.setLanguage(defaultLangL);
		} else {
			Language language = languagesDAO.getByPart1(lang);
			if (language == null) {
				language = languagesDAO.getById(lang);
			}

			if (language != null) {
				groupLabel.setLanguage(language);
			} else {
				throw new BusinessException("Specified language " + lang + " is unknown : "
						+ stmtLabel.getString(),
						"import-unknown-term-lang", new Object[]{ lang, stmtLabel.getString() }
				);
			}
		}

		groupLabel.setLexicalValue(stmtLabel.getString());

		groupLabel.setConceptGroup(conceptGroup);

		return groupLabel;

	}

}
