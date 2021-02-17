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
package fr.mcc.ginco.exports.ginco;

import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.exports.IGincoExportServiceUtil;
import fr.mcc.ginco.exports.result.bean.GincoExportedBranch;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

@Service("gincoExportServiceUtil")
public class GincoExportServiceUtilImpl implements IGincoExportServiceUtil {

	private Logger logger = LoggerFactory.getLogger(GincoExportServiceUtilImpl.class);

	@Override
	public String serializeThesaurusToXmlWithJaxb(
			GincoExportedThesaurus thesaurusToExport) throws TechnicalException {
		// This method encodes a GincoExportedThesaurus in XML
		logger.debug("Serializing thesaurus to XML with JAXB");
		String result = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GincoExportedThesaurus.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			marshaller.marshal(thesaurusToExport, output);
			result = output.toString("UTF-8");
		} catch (JAXBException | UnsupportedEncodingException e) {
			logger.error("Error when trying to serialize a thesaurus to XML with JAXB", e);
			throw new TechnicalException(
					"Error when trying to serialize a thesaurus to XML with JAXB",
					e);
		}
		return result;
	}

	@Override
	public String serializeBranchToXmlWithJaxb(
			GincoExportedBranch branchToExport) throws TechnicalException {
		// This method encodes a GincoExportedBranch in XML
		logger.debug("Serializing concept branch to XML with JAXB");
		String result = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GincoExportedBranch.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			marshaller.marshal(branchToExport, output);
			result = output.toString("UTF-8");
		} catch (JAXBException | UnsupportedEncodingException e) {
			logger.error("Error when trying to serialize a thesaurus to XML with JAXB", e);
			throw new TechnicalException(
					"Error when trying to serialize a concept branch to XML with JAXB",
					e);
		}
		return result;
	}
}
