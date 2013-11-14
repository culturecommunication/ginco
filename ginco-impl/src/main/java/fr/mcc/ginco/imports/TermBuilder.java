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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.ark.IIDGeneratorService;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.dao.IThesaurusTermRoleDAO;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * Builder in charge of building ThesaurusTerm
 *
 */
@Service("skosTermBuilder")
public class TermBuilder extends AbstractBuilder {

	private static Logger logger = LoggerFactory.getLogger(TermBuilder.class);

	@Inject
	@Named("generatorService")
	private IIDGeneratorService generatorService;

	@Inject
	@Named("thesaurusTermRoleDAO")
	private IThesaurusTermRoleDAO thesaurusTermRoleDAO;

	@Inject
	@Named("languagesDAO")
	private ILanguageDAO languagesDAO;

	@Value("${ginco.default.language}")
	private String defaultLang;

	public TermBuilder() {
		super();
	}

	private ThesaurusTerm buildTerm(Statement stmt,
			Thesaurus thesaurus, ThesaurusConcept concept, boolean preferred, boolean hidden)
			throws BusinessException {
		logger.debug("building term " + stmt.getString());
		ThesaurusTerm term = new ThesaurusTerm();
		term.setConcept(concept);
		term.setCreated(thesaurus.getCreated());
		term.setIdentifier(generatorService.generate(ThesaurusTerm.class));
		term.setLexicalValue(StringEscapeUtils.escapeXml(stmt.getString().trim()));
		term.setModified(thesaurus.getDate());
		term.setPrefered(preferred);
        term.setHidden(hidden);
		term.setRole(thesaurusTermRoleDAO.getDefaultThesaurusTermRole());
		term.setStatus(TermStatusEnum.VALIDATED.getStatus());
		term.setThesaurus(thesaurus);

		RDFNode prefLabel = stmt.getObject();
		String lang = prefLabel.asLiteral().getLanguage();
		if (StringUtils.isEmpty(lang)) {
			Language defaultLangL = languagesDAO.getById(defaultLang);
			term.setLanguage(defaultLangL);
		} else {
			Language language = languagesDAO.getByPart1(lang);
			if (language == null){
				language = languagesDAO.getById(lang);
			}

			if (language != null) {
				term.setLanguage(language);			
			} else {
				throw new BusinessException("Specified language " + lang + " is unknown : "  
						+ stmt.getString(),
						"import-unknown-term-lang", new Object[] {lang, stmt.getString()});
			}
		}
		return term;
	}

	/**
	 * Builds the list of ThesaurusTerm for a given SKOS resource concept
	 * @param skosConcept
	 * @param thesaurus
	 * @param concept
	 * @return
	 * @throws BusinessException
	 */
	public List<ThesaurusTerm> buildTerms(Resource skosConcept,
			Thesaurus thesaurus, ThesaurusConcept concept)
			throws BusinessException {
		
		logger.debug("building terms for concept " + concept.getIdentifier());

		List<ThesaurusTerm> terms = new ArrayList<ThesaurusTerm>();

		// Preferred term
		Statement stmtPreferred = skosConcept.getProperty(SKOS.PREF_LABEL);
		if (stmtPreferred==null)
		{
			throw new BusinessException("No preferred label for concept : "+concept.getIdentifier(),
					"import-no-prefferedlabelterm", new Object[] {concept.getIdentifier()});
		} else {
			StmtIterator stmtPrefItr = skosConcept.listProperties(SKOS.PREF_LABEL);
			while (stmtPrefItr.hasNext()) {
				Statement stmtPref = stmtPrefItr.next();
				terms.add(buildTerm(stmtPref,  thesaurus, concept, true, false));
	
			}
		}

		// Alt terms
		StmtIterator stmtAltItr = skosConcept.listProperties(SKOS.ALT_LABEL);
		while (stmtAltItr.hasNext()) {
			Statement stmtAlt = stmtAltItr.next();
			terms.add(buildTerm(stmtAlt,  thesaurus, concept, false, false));

		}
		// Hidden terms
		StmtIterator stmtHiddenItr = skosConcept
				.listProperties(SKOS.HIDDEN_LABEL);
		while (stmtHiddenItr.hasNext()) {
			Statement stmtHidden = stmtHiddenItr.next();
			terms.add(buildTerm(stmtHidden,  thesaurus, concept, false, true));
		}

		return terms;
	}

}
