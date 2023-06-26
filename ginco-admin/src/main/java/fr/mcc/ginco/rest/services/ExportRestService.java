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
package fr.mcc.ginco.rest.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.IExportService;
import fr.mcc.ginco.exports.IGincoBranchExportService;
import fr.mcc.ginco.exports.IGincoThesaurusExportService;
import fr.mcc.ginco.exports.ISKOSExportService;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.extjs.view.FileResponse;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.utils.LabelUtil;

/**
 * REST service to get exported objects.
 */
@Service
@Path("/exportservice")
@PreAuthorize("isAuthenticated()")
public class ExportRestService {

	private static final String THESAURUS_ID_PARAMETER = "thesaurusId";

	private static final String XML_EXTENSION = ".xml";

	@Inject
	@Named("exportService")
	private IExportService exportService;

	@Inject
	@Named("skosExportService")
	private ISKOSExportService skosExportService;

	@Inject
	@Named("gincoThesaurusExportService")
	private IGincoThesaurusExportService gincoThesaurusExportService;

	@Inject
	@Named("gincoBranchExportService")
	private IGincoBranchExportService gincoBranchExportService;
	
	private static final String TABULATION_DELIMITER = "\t";

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;
	
	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;


	/**
	 * Return file in .txt format; name begins with current DateTime.
	 * 
	 * @param thesaurusId
	 * @return
	 */
	@GET
	@Path("/getHierarchical")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getHierarchical(
			@QueryParam(THESAURUS_ID_PARAMETER) String thesaurusId) {

		Thesaurus targetThesaurus = thesaurusService
				.getThesaurusById(thesaurusId);

		File result = writeExportFile(targetThesaurus, false);

		return new FileResponse(result, ".txt", "HIER " + targetThesaurus.getTitle())
				.toResponse();
	}

	/**
	 * Get a SKOS export of a thesaurus
	 * @param thesaurusId
	 * @return A RDF file that contains the thesaurus in SKOS format
	 */
	@GET
	@Path("/getSKOS")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getSKOS(@QueryParam(THESAURUS_ID_PARAMETER) String thesaurusId) {
		Thesaurus targetThesaurus = thesaurusService
				.getThesaurusById(thesaurusId);
		File results = skosExportService.getSKOSExport(targetThesaurus);

		return new FileResponse(results, ".rdf", "SKOS " + targetThesaurus.getTitle())
				.toResponse();
	}

	/**
	 * Return file in .txt format; name begins with current DateTime.
	 * 
	 * @param thesaurusId
	 * @return
	 */
	@GET
	@Path("/getAlphabetical")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAlphabetical(
			@QueryParam(THESAURUS_ID_PARAMETER) String thesaurusId) {
		Thesaurus targetThesaurus = thesaurusService
				.getThesaurusById(thesaurusId);

		File result = writeExportFile(targetThesaurus, true);

		return new FileResponse(result, ".txt", "ALPH " + targetThesaurus.getTitle())
				.toResponse();
	}

	private File writeExportFile(Thesaurus targetThesaurus, boolean alphabetical) {

		File temp;
		BufferedWriter out = null;
		try {
			temp = File.createTempFile("pattern", ".suffix");
			temp.deleteOnExit();

			out = new BufferedWriter
					(new OutputStreamWriter(new FileOutputStream(temp.getPath()), StandardCharsets.UTF_8));

			if (alphabetical) {
				out.write(LabelUtil.getResourceLabel("export-alphabetical")
						.concat(" "));
			} else {
				out.write(LabelUtil.getResourceLabel("export-hierarchical")
						.concat(" "));
			}
			out.write(targetThesaurus.getTitle());
			out.newLine();
			out.newLine();
			out.flush();

			List<FormattedLine> result;
			if (alphabetical) {
				result = exportService.getAlphabeticalText(targetThesaurus);
			} else {
				result = exportService.getHierarchicalText(targetThesaurus);
			}

			for (FormattedLine results : result) {
				for (int i = 0; i < results.getTabs(); i++) {
					out.write(TABULATION_DELIMITER);
				}
				out.write(StringEscapeUtils.unescapeHtml4(results.getText().replace("&apos;", "'")).replaceAll("<br>", ""));
				out.newLine();
				out.flush();
			}

			out.close();

		} catch (IOException e) {
			throw new BusinessException("Cannot create temp file!",
					"cannot-create-file", e);
		}
		return temp;
	}

	/**
	 * Returns a XML file that contains exported thesaurus and all related objects, in Ginco export format
	 * @param thesaurusId
	 * @return	
	 */
	@GET
	@Path("/getGincoThesaurusExport")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getGincoThesaurusExport(
			@QueryParam(THESAURUS_ID_PARAMETER) String thesaurusId) {
		Thesaurus targetThesaurus = thesaurusService
				.getThesaurusById(thesaurusId);
		File temp;
		try {
			temp = File.createTempFile("GINCO ", XML_EXTENSION);
			temp.deleteOnExit();
			String result = gincoThesaurusExportService
					.getThesaurusExport(targetThesaurus);
			FileUtils.write(temp,result,"UTF-8");
		} catch (IOException e) {
			throw new BusinessException("Cannot create temp file!",
					"cannot-create-file", e);
		}

		return new FileResponse(temp, XML_EXTENSION, "GINCO "
				+ targetThesaurus.getTitle()).toResponse();
	}
	
	/**
	 * Returns a XML file that contains exported branch (the concept given in parameter + all its children), in Ginco export format
	 * @param conceptId
	 * @return
	 */
	@GET
	@Path("/getGincoBranchExport")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getGincoBranchExport(
			@QueryParam("conceptId") String conceptId) {
		ThesaurusConcept targetConcept = thesaurusConceptService.getThesaurusConceptById(conceptId);
		File temp;
		BufferedWriter out = null;
		try {
			temp = File.createTempFile("GINCO ", XML_EXTENSION);
			temp.deleteOnExit();
			out = new BufferedWriter
					(new OutputStreamWriter(new FileOutputStream(temp.getPath()), StandardCharsets.UTF_8));
			String result = gincoBranchExportService.getBranchExport(targetConcept);
			out.write(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new BusinessException("Cannot create temp file!",
					"cannot-create-file", e);
		}
		return new FileResponse(temp, XML_EXTENSION, "GINCO Branch "
				+ thesaurusConceptService.getConceptTitle(targetConcept)).toResponse();
	}
}