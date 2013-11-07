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
package fr.mcc.ginco.exports.skos.skosapi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.cxf.helpers.IOUtils;
import org.semanticweb.skos.AddAssertion;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSChangeException;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skos.SKOSEntityAssertion;
import org.semanticweb.skos.SKOSStorageException;
import org.semanticweb.skosapibinding.SKOSFormatExt;
import org.semanticweb.skosapibinding.SKOSManager;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.ISKOSExportService;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.ICustomConceptAttributeTypeService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.utils.DateUtil;

@Service("skosExportServiceOld")
public class SKOSExportServiceImpl implements ISKOSExportService {

	private static final String RDF_END_TAG = "</rdf:RDF>";

	private static final String XMLNS_GINCO = "xmlns:ginco=\"http://data.culture.fr/thesaurus/ginco/ns/\"";

	private static final String XMLNS_GINCO_OLD = "xmlns:ginco=\"http://data.culture.fr/thesaurus/ginco/\"";

	private static final String XMLNS_ISO_THES = "xmlns:iso-thes=\"http://www.niso.org/schemas/iso25964/skos-thes#\"";

	private static final String XMLNS_ISO_THES_OLD = "xmlns:iso-thes=\"http://www.niso.org/schemas/iso25964/iso-thes#\"";

	private static final String XML_HEAD = "xml version=\"1.0\" encoding=\"UTF-8\"";

	private static final String XML_HEAD_OLD = "xml version=\"1.0\"";

	private static final String XMLNS_DCT = "xmlns:dct=\"http://purl.org/dc/terms/\"";

	private static final String XMLNS_DCT_OLD = "xmlns:dct=\"http://purl.org/dct#\"";

	private static final String XMLNS_DC = "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"";

	private static final String XMLNS_FOAF = "xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"";

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("customConceptAttributeTypeService")
	private ICustomConceptAttributeTypeService customConceptAttributeTypeService;

	@Inject
	@Named("skosArrayExporter")
	private SKOSArrayExporter skosArrayExporter;

	@Inject
	@Named("skosConceptExporter")
	private SKOSConceptExporter skosConceptExporter;

	@Inject
	@Named("skosThesaurusExporter")
	private SKOSThesaurusExporter skosThesaurusExporter;

	@Inject
	@Named("skosCustomConceptAttributeTypesExporter")
	private SKOSCustomConceptAttributeTypesExporter skosCustomConceptAttributeTypesExporter;

	@Inject
	@Named("skosGroupExporter")
	private SKOSGroupExporter skosGroupExporter;

	@Inject
	@Named("skosHierarshicalRelationshipRolesExporter")
	private SKOSHierarshicalRelationshipRolesExporter skosHierarshicalRelationshipRolesExporter;

	@Inject
	@Named("skosAssociativeRelationshipRolesExporter")
	private SKOSAssociativeRelationshipRolesExporter skosAssociativeRelationshipRolesExporter;

	@Inject
	@Named("skosComplexConceptExporter")
	private SKOSComplexConceptExporter skosComplexConceptExporter;

	@Log
	Logger logger;

	@Override
	public File getSKOSExport(Thesaurus thesaurus) throws BusinessException {

		final String baseURI = thesaurus.getIdentifier();

		SKOSManager man;
		SKOSDataset vocab;
		SKOSConceptScheme scheme;
		SKOSDataFactory factory;

		try {
			man = new SKOSManager();
			vocab = man.createSKOSDataset(URI.create(baseURI));
		} catch (SKOSCreationException e) {
			throw new BusinessException("Error creating dataset from URI.",
					"error-in-skos-objects", e);
		}

		List<ThesaurusConcept> tt = thesaurusConceptService
				.getTopTermThesaurusConcepts(thesaurus.getIdentifier());

		factory = man.getSKOSDataFactory();

		scheme = factory.getSKOSConceptScheme(URI.create(baseURI));

		SKOSEntityAssertion schemaAssertion = factory
				.getSKOSEntityAssertion(scheme);

		List<SKOSChange> addList = new ArrayList<SKOSChange>();
		addList.add(new AddAssertion(vocab, schemaAssertion));

		addList.addAll(skosThesaurusExporter.exportThesaurusSKOS(thesaurus, factory,  vocab,  scheme));

		String termModels = "";
		for (ThesaurusConcept conceptTT : tt) {
			MixedSKOSModel mixedModel = skosConceptExporter.exportConceptSKOS(conceptTT, null, scheme, factory, vocab);
			addList.addAll(mixedModel.getSkosChanges());
			if (mixedModel != null && mixedModel.getModels() != null) {
				for (Model termModel: mixedModel.getModels()) {
					StringWriter sw = new StringWriter();
					termModel.write(sw, "RDF/XML-ABBREV");
					String result = sw.toString();
					int start = result.lastIndexOf("skos-xl#\">") + "skos-xl#\">".length()
							+ 2;
					int end = result.lastIndexOf("</rdf:RDF>");
					termModels = termModels.concat(result.substring(start, end));
				}
			}
		}
		logger.debug("termModels = " + termModels);

		try {

			String collections = skosArrayExporter.exportCollections(thesaurus);
			String groups = skosGroupExporter.exportGroups(thesaurus);
			String complexConcepts = skosComplexConceptExporter.exportComplexConcept(thesaurus);

			man.applyChanges(addList);


			File temp = File.createTempFile("skosExport"
					+ DateUtil.nowDate().getTime(), ".rdf");
			temp.deleteOnExit();

			man.save(vocab, SKOSFormatExt.RDFXML, temp.toURI());

			FileInputStream fis = new FileInputStream(temp);

			String content = IOUtils.toString(fis);
			fis.close();

			content = content.replaceAll(XMLNS_DC, XMLNS_DC + "\n" + XMLNS_FOAF + "\n" + XMLNS_GINCO)
					.replaceAll(XMLNS_DCT_OLD, XMLNS_DCT)
					.replaceAll(RDF_END_TAG, collections + RDF_END_TAG)
					.replaceAll(RDF_END_TAG, groups + RDF_END_TAG)
					.replaceAll(RDF_END_TAG, complexConcepts + RDF_END_TAG)
					.replaceAll(XML_HEAD_OLD, XML_HEAD)
					.replaceAll(XMLNS_ISO_THES_OLD, XMLNS_ISO_THES)
					.replaceAll(XMLNS_GINCO_OLD, "")
					.replaceAll(RDF_END_TAG, termModels + RDF_END_TAG);

			Map<String, String> customAttributesOWL = skosCustomConceptAttributeTypesExporter
					.exportCustomConceptAttributeTypes(thesaurus);
			for (String code : customAttributesOWL.keySet()) {
				String datatype_old = "<owl:DatatypeProperty rdf:about=\"http://data.culture.fr/thesaurus/ginco/"
						+ code + "\"/>";
				content = content.replaceAll(datatype_old,
						customAttributesOWL.get(code)
								+ "</ginco:CustomConceptAttribute>");
			}

			Map<String, String> hierarshicalRelationshipRolesOWL = skosHierarshicalRelationshipRolesExporter
					.exportHierarshicalRelationshipRoles();
			for (String code : hierarshicalRelationshipRolesOWL.keySet()) {
				String object_property_old = "<owl:ObjectProperty rdf:about=\"http://data.culture.fr/thesaurus/ginco/"
						+ code + "\"/>";
				content = content.replaceAll(object_property_old,
						hierarshicalRelationshipRolesOWL.get(code) + "</owl:ObjectProperty>");
			}

			Map<String, String> associativeRelationshipRolesOWL = skosAssociativeRelationshipRolesExporter.exportAssociativeRelationshipRoles();
			for (String code : associativeRelationshipRolesOWL.keySet()) {
				String object_property_old = "<owl:ObjectProperty rdf:about=\"http://data.culture.fr/thesaurus/ginco/"
						+ code + "\"/>";
				content = content.replaceAll(object_property_old,
						associativeRelationshipRolesOWL.get(code) + "</owl:ObjectProperty>");
			}

			if (thesaurus.getCreator() != null) {

				String creatorName = thesaurus.getCreator().getName();
				String creatorHomepage = thesaurus.getCreator().getHomepage();
				String creatorEmail = thesaurus.getCreator().getEmail();

				String org = "\n\t\t<foaf:Organization>\n";

				if (creatorName != null && !creatorName.isEmpty()){
					org = org + "\t\t\t<foaf:name>" + creatorName + "</foaf:name>\n";
				}
				if (creatorHomepage != null && !creatorHomepage.isEmpty()){
					org = org + "\t\t\t<foaf:homepage>" + creatorHomepage + "</foaf:homepage>\n";
				}
				if (creatorEmail != null && !creatorEmail.isEmpty()){
					org = org + "\t\t\t<foaf:mbox>" + creatorEmail + "</foaf:mbox>\n";
				}

				org = org + "\t\t</foaf:Organization>";

				String creator = "<dc:creator rdf:datatype=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral\">";

				content = content.replaceAll(creator, "<dc:creator>")
						.replaceAll("_X_CREATOR_", org);
			}

			BufferedOutputStream bos;
			FileOutputStream fos = new FileOutputStream(temp);

			bos = new BufferedOutputStream(fos);
			bos.write(content.getBytes());
			bos.flush();
			fos.close();

			return temp;

		} catch (SKOSChangeException e) {
			throw new BusinessException("Error saving data into dataset.",
					"error-in-skos-objects", e);
		} catch (SKOSStorageException e) {
			throw new BusinessException("Error saving dataset to temp file.",
					"error-in-skos-objects", e);
		} catch (IOException e) {
			throw new BusinessException(
					"Error storing temporarty file for export SKOS",
					"export-unable-to-write-temporary-file", e);
		}
	}


}
