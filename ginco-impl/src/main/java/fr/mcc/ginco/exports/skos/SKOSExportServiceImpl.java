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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroupType;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.ISKOSExportService;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IThesaurusConceptGroupTypeService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.skos.namespaces.GINCO;
import fr.mcc.ginco.skos.namespaces.ISOTHES;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.skos.namespaces.SKOSXL;
import fr.mcc.ginco.utils.DateUtil;

@Service("skosExportService")
public class SKOSExportServiceImpl implements ISKOSExportService {
	
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

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
	@Named("skosGroupsExporter")
	private SKOSGroupsExporter skosGroupsExporter;
	
	@Inject
	@Named("skosAssociativeRelationshipRolesExporter")
	private SKOSAssociativeRelationshipRolesExporter skosAssociativeRelationshipRolesExporter;
	
	@Inject
	@Named("skosHierarchicalRelationshipRolesExporter")
	private SKOSHierarchicalRelationshipRolesExporter skosHierarshicalRelationshipRolesExporter;

	@Inject
	@Named("thesaurusConceptGroupTypeService")
	private IThesaurusConceptGroupTypeService thesaurusConceptGroupTypeService;
	
	@Inject
	@Named("skosComplexConceptExporter")
	private SKOSComplexConceptExporter skosComplexConceptExporter;

	@Log
	Logger logger;

	@Override
	public File getSKOSExport(Thesaurus thesaurus) throws BusinessException {

		List<ThesaurusConcept> tt = thesaurusConceptService
				.getTopTermThesaurusConcepts(thesaurus.getIdentifier());

		Model model = ModelFactory.createDefaultModel();
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

		skosThesaurusExporter.exportThesaurusSKOS(thesaurus, model);

		for (ThesaurusConcept conceptTT : tt) {
			skosConceptExporter.exportConceptSKOS(conceptTT, null, model, ontModel);			
			
		}
		skosArrayExporter.exportCollections(thesaurus, model);
		skosGroupsExporter.exportGroups(thesaurus, model, ontModel);
		skosAssociativeRelationshipRolesExporter.exportAssociativeRelationshipRoles(model, ontModel);
		skosHierarshicalRelationshipRolesExporter.exportHierarchicalRelationshipRoles(model, ontModel);
		skosComplexConceptExporter.exportComplexConcept(thesaurus, model);
		
		model.setNsPrefix("skos", SKOS.getURI());
		model.setNsPrefix("dct", DCTerms.getURI());
		model.setNsPrefix("iso-thes", ISOTHES.getURI());
		model.setNsPrefix("ginco", GINCO.getURI() );
		model.setNsPrefix("xl", SKOSXL.getURI());
		model.setNsPrefix("foaf", FOAF.getURI());
		
		List<Resource> rootTypes = new ArrayList<Resource>();
		rootTypes.add(OWL.Ontology);
		rootTypes.add(RDFS.Datatype);
		rootTypes.add(RDFS.Class);
		rootTypes.add(OWL.Class);
		rootTypes.add(OWL.ObjectProperty);
		rootTypes.add(RDF.Property);
		rootTypes.add(OWL.DatatypeProperty);
		rootTypes.add(OWL.TransitiveProperty);
		rootTypes.add(OWL.SymmetricProperty);
		rootTypes.add(OWL.FunctionalProperty);
		rootTypes.add(OWL.InverseFunctionalProperty);
		rootTypes.add(SKOS.CONCEPT);
		rootTypes.add(SKOS.CONCEPTSCHEME);
		rootTypes.add(ISOTHES.PREFERRED_TERM);
		rootTypes.add(ISOTHES.SIMPLE_NON_PREFERRED_TERM);
		rootTypes.add(SKOS.COLLECTION);
		
		for (ThesaurusConceptGroupType groupType:thesaurusConceptGroupTypeService.getConceptGroupTypeList()) {
			rootTypes.add(GINCO.getResource(groupType.getSkosLabel()));
		}


		RDFWriter w =  model.getWriter("RDF/XML-ABBREV");
		w.setProperty("tab", 4);
		w.setProperty("prettyTypes",rootTypes.toArray(new Resource[rootTypes.size()]));		
		StringWriter sw = new StringWriter();
		sw.write(XML_HEADER);
		w.write(model, sw, null);		
		String res =sw.toString();
		
		logger.debug("termModels = " + res);

		try {			

			File temp = File.createTempFile("skosExport"
					+ DateUtil.nowDate().getTime(), ".rdf");
			temp.deleteOnExit();
			
			FileInputStream fis = new FileInputStream(temp);

			fis.close();

			BufferedOutputStream bos;
			
			FileOutputStream fos = new FileOutputStream(temp);

			bos = new BufferedOutputStream(fos);
			bos.write(res.getBytes());
			bos.flush();			
			fos.close();

			return temp;
		
		} catch (IOException e) {
			throw new BusinessException(
					"Error storing temporarty file for export SKOS",
					"export-unable-to-write-temporary-file", e);
		}
	}

}
