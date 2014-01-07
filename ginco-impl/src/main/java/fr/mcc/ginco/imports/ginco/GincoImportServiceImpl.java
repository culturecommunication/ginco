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
package fr.mcc.ginco.imports.ginco;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.hpl.jena.util.FileManager;

import fr.mcc.ginco.audit.utils.AuditContext;
import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.exports.result.bean.GincoExportedBranch;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.imports.IGincoImportService;

/**
 * This class gives methods to import a thesaurus from a XML file (custom Ginco format)
 *
 */
@Transactional
@Service("gincoImportService")
public class GincoImportServiceImpl implements IGincoImportService {
	
	private static Logger logger = LoggerFactory.getLogger(GincoImportServiceImpl.class);
	
	@Inject
	@Named("gincoConceptBranchBuilder")
	private GincoConceptBranchBuilder gincoConceptBranchBuilder;
	
	@Inject
	@Named("gincoThesaurusBuilder")
	private GincoThesaurusBuilder gincoThesaurusBuilder;
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.imports.IGincoImportService#importGincoXmlThesaurusFile(java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public Map<Thesaurus,Set<Alignment>> importGincoXmlThesaurusFile(String content, String fileName, File tempDir) throws TechnicalException, BusinessException {
		
		URI fileURI = writeTempFile(content, fileName, tempDir);
		InputStream in = FileManager.get().open(fileURI.toString());
		GincoExportedThesaurus unmarshalledExportedThesaurus = new GincoExportedThesaurus();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GincoExportedThesaurus.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshalledExportedThesaurus = (GincoExportedThesaurus) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			AuditContext.enableAudit();
			throw new BusinessException("Error when trying to deserialize the thesaurus from XML with JAXB :"+e.getMessage(),
					"import-unable-to-read-file", e);
			
		} 
		Map<Thesaurus,Set<Alignment>> returnValue = gincoThesaurusBuilder.storeGincoExportedThesaurus(unmarshalledExportedThesaurus);
		return returnValue;
	}
	
	/* (non-Javadoc)
	 * @see fr.mcc.ginco.imports.IGincoImportService#importGincoXmlThesaurusFile(java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public Map<ThesaurusConcept,Set<Alignment>> importGincoBranchXmlFile(String content, String fileName, File tempDir, String thesaurusId) throws TechnicalException, BusinessException {
		URI fileURI = writeTempFile(content, fileName, tempDir);
		InputStream in = FileManager.get().open(fileURI.toString());
		GincoExportedBranch unmarshalledExportedThesaurus = new GincoExportedBranch();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GincoExportedBranch.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshalledExportedThesaurus = (GincoExportedBranch) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			throw new BusinessException("Error when trying to deserialize the concept branch from XML with JAXB :"+e.getMessage(),
					"import-unable-to-read-file", e);
		}
		return gincoConceptBranchBuilder.storeGincoExportedBranch(unmarshalledExportedThesaurus, thesaurusId);
	}

	private URI writeTempFile(String fileContent, String fileName, File tempDir) {
		logger.debug("Writing temporary file for import");
		String prefix = fileName.substring(0, fileName.lastIndexOf("."));
		logger.debug("Filename : " + prefix);
		File file;
		try {
			file = File.createTempFile(prefix, ".tmp", tempDir);

			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(fileContent);
			fileWriter.close();
		} catch (IOException e) {
			throw new BusinessException(
					"Error storing temporary file for import " + prefix,
					"import-unable-to-write-temporary-file", e);
		}
		return file.toURI();
	}
}
