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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSLiteral;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusFormat;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.dao.IGenericDAO;
import fr.mcc.ginco.dao.ILanguageDAO;
import fr.mcc.ginco.dao.IThesaurusTypeDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.log.Log;

@Service("skosThesaurusBuilder")
public class ThesaurusBuilder {

	@Log
	private Logger logger;

	@Inject
	@Named("thesaurusFormatDAO")
	private IGenericDAO<ThesaurusFormat, Integer> thesaurusFormatDAO;

	@Inject
	@Named("thesaurusTypeDAO")
	private IThesaurusTypeDAO thesaurusTypeDAO;

	@Inject
	@Named("languagesDAO")
	private ILanguageDAO languagesDAO;

	@Value("${import.dublin.core.namespace}title")
	private String DC_TITLE_ELEMENT;

	@Value("${import.dublin.core.namespace}contributor")
	private String DC_CONTRIBUTOR_ELEMENT;

	@Value("${import.dublin.core.namespace}coverage")
	private String DC_COVERAGE_ELEMENT;

	@Value("${import.dublin.core.namespace}description")
	private String DC_DESCRIPTION_ELEMENT;

	@Value("${import.dublin.core.namespace}publisher")
	private String DC_PUBLISHER_ELEMENT;

	@Value("${import.dublin.core.namespace}rights")
	private String DC_RIGHTS_ELEMENT;

	@Value("${import.dublin.core.namespace}subject")
	private String DC_SUBJECT_ELEMENT;

	@Value("${import.dublin.core.namespace}type")
	private String DC_TYPE_ELEMENT;

	@Value("${import.dublin.core.namespace}relation")
	private String DC_RELATION_ELEMENT;

	@Value("${import.dublin.core.namespace}source")
	private String DC_SOURCE_ELEMENT;

	@Value("${import.dublin.coreterms.namespace}created")
	private String DC_CREATED_ELEMENT;

	@Value("${import.dublin.coreterms.namespace}modified")
	private String DC_MODIFIED_ELEMENT;

	@Value("${import.dublin.core.namespace}language")
	private String DC_LANGUAGE_ELEMENT;

	@Value("${import.default.top.concept}")
	private boolean defaultTopConcept;

	@Value("#{'${import.skos.date.formats}'.split(',')}")
	private List<String> skosDefaultDateFormats;

	@Value("${import.skos.default.format}")
	private Integer defaultThesaurusFormat;

	public ThesaurusBuilder() {
		super();
	}

	public Thesaurus buildThesaurus(String URI, Set<SKOSAnnotation> annotations)
			throws BusinessException {
		Thesaurus thesaurus = new Thesaurus();
		thesaurus.setIdentifier(URI);

		String tempTitle = "";
		String tempSubject = "";
		String tempContributor = "";
		String tempCoverage = "";
		String tempDescription = "";
		String tempPublisher = "";
		String tempRights = "";
		String tempRelation = "";
		String tempSource = "";
		Date createdDate = new Date();
		Date modifiedDate = new Date();
		ThesaurusType type = null;
		Set<Language> langs = new HashSet<Language>();

		for (SKOSAnnotation annotation : annotations) {
			SKOSLiteral literal = annotation.getAnnotationValueAsConstant();
			if (DC_TITLE_ELEMENT.equals(annotation.getURI().toString())) {
				tempTitle += literal.getAsSKOSUntypedLiteral().getLiteral();
			}
			if (DC_SUBJECT_ELEMENT.equals(annotation.getURI().toString())) {
				tempSubject = getMultilineThesaurusInfo(tempSubject, literal);
			}
			if (DC_CONTRIBUTOR_ELEMENT.equals(annotation.getURI().toString())) {
				tempContributor = getMultilineThesaurusInfo(tempContributor,
						literal);
			}
			if (DC_COVERAGE_ELEMENT.equals(annotation.getURI().toString())) {
				tempCoverage = getMultilineThesaurusInfo(tempCoverage, literal);
			}
			if (DC_DESCRIPTION_ELEMENT.equals(annotation.getURI().toString())) {
				tempDescription = getMultilineThesaurusInfo(tempDescription,
						literal);
			}
			if (DC_PUBLISHER_ELEMENT.equals(annotation.getURI().toString())) {
				tempPublisher += literal.getAsSKOSUntypedLiteral().getLiteral();
			}
			if (DC_RIGHTS_ELEMENT.equals(annotation.getURI().toString())) {
				tempRights = getMultilineThesaurusInfo(tempRights, literal);

			}
			if (DC_TYPE_ELEMENT.equals(annotation.getURI().toString())) {
				type = thesaurusTypeDAO.getByLabel(literal
						.getAsSKOSUntypedLiteral().getLiteral());
			}
			if (DC_RELATION_ELEMENT.equals(annotation.getURI().toString())) {
				tempRelation = getMultilineThesaurusInfo(tempRelation, literal);
			}
			if (DC_SOURCE_ELEMENT.equals(annotation.getURI().toString())) {
				tempSource += literal.getAsSKOSUntypedLiteral().getLiteral();
			}
			if (DC_MODIFIED_ELEMENT.equals(annotation.getURI().toString())) {
				logger.debug("Modified date string = "
						+ literal.getAsSKOSUntypedLiteral().getLiteral());
				modifiedDate = getSkosDate(literal.getAsSKOSUntypedLiteral()
						.getLiteral());
			}
			if (DC_CREATED_ELEMENT.equals(annotation.getURI().toString())) {
				logger.debug("Created date string = "
						+ literal.getAsSKOSUntypedLiteral().getLiteral());
				createdDate = getSkosDate(literal.getAsSKOSUntypedLiteral()
						.getLiteral());

			}
			if (DC_LANGUAGE_ELEMENT.equals(annotation.getURI().toString())) {
				Language lang = languagesDAO.getById(literal
						.getAsSKOSUntypedLiteral().getLiteral());
				langs.add(lang);
			}
		}
		thesaurus.setTitle(tempTitle);
		thesaurus.setSubject(tempSubject);
		thesaurus.setContributor(tempContributor);
		thesaurus.setCoverage(tempCoverage);
		thesaurus.setDescription(tempDescription);
		thesaurus.setPublisher(tempPublisher);
		thesaurus.setRights(tempRights);
		thesaurus.setType(type);
		thesaurus.setRelation(tempRelation);
		thesaurus.setSource(tempSource);
		thesaurus.setCreated(createdDate);
		thesaurus.setDate(modifiedDate);
		thesaurus.setLang(langs);

		ThesaurusFormat format = thesaurusFormatDAO
				.getById(defaultThesaurusFormat);
		thesaurus.setFormat(format);

		thesaurus.setDefaultTopConcept(defaultTopConcept);
		/*
		 * thesaurus.setCreator(creator);
		 */
		return thesaurus;
	}

	private Date getSkosDate(String skosDate) throws BusinessException {
		for (String skosDefaultDateFormat : skosDefaultDateFormats) {
			SimpleDateFormat sdf = new SimpleDateFormat(skosDefaultDateFormat);
			try {
				return sdf.parse(skosDate);
			} catch (ParseException e) {
				logger.warn("Invalid date format for skosDate : " + skosDate);
			}
		}
		throw new BusinessException("InvalidDateFormat for input string "
				+ skosDate, "import-invalid-date-format");
	}

	private String getMultilineThesaurusInfo(String data, SKOSLiteral literal) {
		String newData = data;
		if (StringUtils.isNotEmpty(newData)) {
			newData += "\n";
		}
		newData += literal.getAsSKOSUntypedLiteral().getLiteral();
		return newData;
	}
}
