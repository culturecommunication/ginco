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
import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * Builder in charge of building the thesaurus arrays node labels
 *
 */
@Service("skosNodeLabelBuilder")
public class NodeLabelBuilder extends AbstractBuilder {

	private static Logger logger = LoggerFactory.getLogger(NodeLabelBuilder.class);

	@Inject
	@Named("languagesDAO")
	private ILanguageDAO languagesDAO;

	@Value("${ginco.default.language}")
	private String defaultLang;
	
	@Inject
	@Named("skosImportUtils")
	private SKOSImportUtils skosImportUtils;

	public NodeLabelBuilder() {
		super();
	}

	/**
	 * Builds a NodeLabel object for the given array
	 * @param skosCollection
	 * @param array
	 * @return
	 * @throws BusinessException
	 */
	public NodeLabel buildNodeLabel(Resource skosCollection, Thesaurus thesaurus, ThesaurusArray array)
			throws BusinessException {
		logger.debug("Building node label for thesaurus array " + array.getIdentifier());
		NodeLabel nodeLabel = new NodeLabel();

		Statement stmtCreated = skosCollection.getProperty(DCTerms.created);
		if (stmtCreated != null){
			nodeLabel.setCreated(skosImportUtils.getSkosDate(stmtCreated.getString()));
		} else {
			nodeLabel.setCreated(thesaurus.getCreated());
		}

		Statement stmtModified = skosCollection.getProperty(DCTerms.modified);
		if (stmtModified != null){
			nodeLabel.setModified(skosImportUtils.getSkosDate(stmtModified.getString()));
		} else {
			nodeLabel.setModified(thesaurus.getDate());
		}

		Statement stmtLabel = skosCollection.getProperty(SKOS.PREF_LABEL);

		RDFNode prefLabel = stmtLabel.getObject();
		String lang = prefLabel.asLiteral().getLanguage();
		if (StringUtils.isEmpty(lang)) {
			Language defaultLangL = languagesDAO.getById(defaultLang);
			nodeLabel.setLanguage(defaultLangL);
		} else {
			Language language = languagesDAO.getByPart1(lang);
			if (language == null){
				language = languagesDAO.getById(lang);
			}

			if (language != null) {
				nodeLabel.setLanguage(language);
			} else {
				throw new BusinessException("Specified language " + lang + " is unknown : "
						+ stmtLabel.getString(),
						"import-unknown-term-lang", new Object[] {lang, stmtLabel.getString()});
			}
		}

		nodeLabel.setLexicalValue(stmtLabel.getString());

		nodeLabel.setThesaurusArray(array);

		return nodeLabel;

	}

}
