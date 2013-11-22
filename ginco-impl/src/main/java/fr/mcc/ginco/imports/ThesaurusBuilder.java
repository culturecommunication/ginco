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
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.dao.IThesaurusTypeDAO;
import fr.mcc.ginco.exceptions.BusinessException;

/**
 * Builder in charge of building a thesaurus
 *
 */
@Service("skosThesaurusBuilder")
public class ThesaurusBuilder extends AbstractBuilder {

	@Inject
	private IGenericDAO<ThesaurusFormat, Integer> thesaurusFormatDAO;

	@Inject
	private IThesaurusTypeDAO thesaurusTypeDAO;
	
	@Inject
	@Named("skosThesaurusOrganizationBuilder")
	private ThesaurusOrganizationBuilder thesaurusOrganizationBuilder;

	@Inject
	private ILanguageDAO languagesDAO;

	@Value("${import.default.top.concept}")
	private boolean defaultTopConcept;

	@Value("${import.skos.default.format}")
	private Integer defaultThesaurusFormat;

    @Value("${import.skos.default.type}")
    private Integer defaultThesaurusType;
    
    @Inject
	@Named("skosImportUtils")
    private SKOSImportUtils skosImportUtils;

	public ThesaurusBuilder() {
		super();
	}

	/**
	 * Builds a Thesaurus object from the given resource and model
	 *
	 * @param skosThesaurus
	 * @param model
	 * @return
	 */
	public Thesaurus buildThesaurus(Resource skosThesaurus, Model model){
		Thesaurus thesaurus = new Thesaurus();
		thesaurus.setIdentifier(skosThesaurus.getURI());
		String title = getSimpleStringInfo(skosThesaurus, DC.title, DCTerms.title);
		if (StringUtils.isEmpty(title)) {
			throw new BusinessException(
					"Missing title for imported thesaurus ",
					"import-missing-title-thesaurus");
		}
		thesaurus.setTitle(title);
		thesaurus.setSubject(getMultipleLineStringInfo(skosThesaurus,
				DC.subject, DCTerms.subject));
		thesaurus.setContributor(getMultipleLineStringInfo(skosThesaurus,
				DC.contributor,DCTerms.contributor));
		thesaurus.setCoverage(getMultipleLineStringInfo(skosThesaurus,
				DC.coverage,DCTerms.coverage));
		thesaurus.setDescription(getMultipleLineStringInfo(skosThesaurus,
				DC.description,DCTerms.description));
		thesaurus
				.setPublisher(getSimpleStringInfo(skosThesaurus, DC.publisher,DCTerms.publisher));
		thesaurus
				.setRights(getMultipleLineStringInfo(skosThesaurus, DC.rights));
		ThesaurusType thesaurusType = thesaurusTypeDAO
				.getByLabel(getSimpleStringInfo(skosThesaurus, DC.type, DCTerms.type));
        if (thesaurusType == null) {
            thesaurusType = thesaurusTypeDAO.getById(defaultThesaurusType);
		}
		thesaurus.setType(thesaurusType);
		thesaurus.setRelation(getMultipleLineStringInfo(skosThesaurus,
				DC.relation,DCTerms.relation));
		thesaurus.setSource(getSimpleStringInfo(skosThesaurus, DC.source,DCTerms.source));
        thesaurus.setPolyHierarchical(true);

		String thesaurusCreated = getSimpleStringInfo(skosThesaurus,
				DCTerms.created);
		if (thesaurusCreated!=null) {
			thesaurus.setCreated(skosImportUtils.getSkosDate(thesaurusCreated));
		} else
		{
			thesaurus.setCreated(new Date());
		}
		thesaurus.setDate(skosImportUtils.getSkosDate(getSimpleStringInfo(skosThesaurus,
				DCTerms.modified,DCTerms.date)));

		thesaurus.setLang(getLanguages(skosThesaurus,DC.language));
		if (thesaurus.getLang().isEmpty())
		{
			throw new BusinessException("Missing language for imported thesaurus ",
					"import-missing-lang-thesaurus");
		}

		ThesaurusFormat format = thesaurusFormatDAO
				.getById(defaultThesaurusFormat);
		if (format == null) {
			throw new BusinessException(
					"Configuration problem : the default import format "
							+ defaultThesaurusFormat + " is unknown",
					"import-unknown-default-format");
		}
		thesaurus.addFormat(format);

		thesaurus.setDefaultTopConcept(defaultTopConcept);

		thesaurus.setCreator(thesaurusOrganizationBuilder.getCreator(skosThesaurus, model));

		thesaurus.setArchived(false);
		return thesaurus;
	}

	
	/**
	 * Gets the list of defined languages for the given thesaurus
	 *
	 * @param skosThesaurus
	 * @return
	 */
	private Set<Language> getLanguages(Resource skosThesaurus, Property prop) {
		Set<Language> langs = new HashSet<Language>();
		StmtIterator stmtIterator = skosThesaurus.listProperties(prop);
		while (stmtIterator.hasNext()) {
			Statement stmt = stmtIterator.next();
			Language lang = languagesDAO.getById(stmt.getString());
			if (lang == null) {
				lang = languagesDAO.getByPart1(stmt.getString());
				if (lang == null) {
					throw new BusinessException(
						"Specified thesaurus language is unknown :  "
								+ stmt.getString(),
						"import-unknown-thesaurus-lang");
				}
			}
			langs.add(lang);
		}

		return langs;
	}

	
}
