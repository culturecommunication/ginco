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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Role;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exceptions.TechnicalException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.ImportedBranchResponse;
import fr.mcc.ginco.extjs.view.ImportedTermsResponse;
import fr.mcc.ginco.extjs.view.ImportedThesaurusResponse;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusViewConverter;
import fr.mcc.ginco.imports.IGincoImportService;
import fr.mcc.ginco.imports.ISKOSImportService;
import fr.mcc.ginco.services.IIndexerService;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.services.IThesaurusTermService;
import fr.mcc.ginco.services.IUserRoleService;

/**
 * Base REST service intended to be used for SKOS Import the @Produces({
 * MediaType.TEXT_HTML}) is no mistake : this rest service is used by an ajax
 * call and IE cannot display result if JSOn is returned
 */
@Service
@Path("/importservice")
@Produces({ MediaType.TEXT_HTML })
public class ImportRestService {
	@Inject
	@Context
	private javax.servlet.ServletContext servletContext;

	@Inject
	@Named("skosImportService")
	private ISKOSImportService skosImportService;

	@Inject
	@Named("gincoImportService")
	private IGincoImportService gincoImportService;

	@Inject
	@Named("indexerService")
	private IIndexerService indexerService;

	@Inject
	@Named("thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;

	@Inject
	@Named("thesaurusConceptViewConverter")
	private ThesaurusConceptViewConverter thesaurusConceptViewConverter;

	@Inject
	@Named("thesaurusTermService")
	private IThesaurusTermService thesaurusTermService;

	@Inject
    @Named("userRoleService")
  	private IUserRoleService userRoleService;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Inject
	@Named("languagesService")
	private ILanguagesService languageService;

	@Value("${ginco.default.language}")
	private String defaultLang;

	/**
	 * This method is called to import a SKOS thesaurus the @Produces({
	 * MediaType.TEXT_HTML}) is no mistake : this rest service is used by an
	 * ajax call and IE cannot display result if JSOn is returned
	 *
	 * @param body
	 * @param request
	 * @return The imported thesaurus in JSOn string representig a
	 *         ExtJsonFormLoadData
	 * @throws BusinessException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@POST
	@Path("/import")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String uploadFile(MultipartBody body,
			@Context HttpServletRequest request) throws BusinessException,
			JsonGenerationException, JsonMappingException, IOException {
		Attachment file = body.getAttachment("import-file-path");
		String content = file.getObject(String.class);
		String fileName = file.getDataHandler().getName();
		File tempdir = (File) servletContext
				.getAttribute("javax.servlet.context.tempdir");

		Map<Thesaurus, Set<Alignment>> importResult = skosImportService
				.importSKOSFile(content, fileName, tempdir);
		Thesaurus thesaurus = importResult.keySet().iterator().next();

		indexerService.indexThesaurus(thesaurus);

		ImportedThesaurusResponse response = new ImportedThesaurusResponse();
		response.setThesaurusTitle(thesaurus.getTitle());
		List<String> conceptsMissingAlignments = new ArrayList<String>();
		List<String> externalConceptIds = new ArrayList<String>();
		Set<Alignment> bannedAlignments = importResult.get(thesaurus);
		for (Alignment alignment : bannedAlignments) {
			conceptsMissingAlignments.add(thesaurusConceptService
					.getConceptLabel(alignment.getSourceConcept()
							.getIdentifier()));
			externalConceptIds.add(alignment.getTargetConcepts().iterator()
					.next().getExternalTargetConcept());
		}
		response.setConceptsMissingAlignments(conceptsMissingAlignments);
		response.setExternalConceptIds(externalConceptIds);
		ObjectMapper mapper = new ObjectMapper();
		String serialized = mapper.writeValueAsString(new ExtJsonFormLoadData(
				response));
		return StringEscapeUtils.unescapeHtml4(serialized);
	}

	/**
	 * This method is called to import a Ginco XML thesaurus. The
	 *
	 * @Produces({MediaType.TEXT_HTML ) is not a mistake : this rest service is
	 *                                used by an ajax call and IE cannot display
	 *                                result if JSOn is returned
	 *
	 * @param body
	 * @param request
	 * @return The imported thesaurus in JSOn string representing a
	 *         ExtJsonFormLoadData
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws TechnicalException
	 * @throws BusinessException
	 */
	@POST
	@Path("/importGincoXml")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String uploadGincoXmlThesaurusFile(MultipartBody body,
			@Context HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException,
			TechnicalException, BusinessException {
		Attachment file = body.getAttachment("import-file-path");
		String content = file.getObject(String.class);
		String fileName = file.getDataHandler().getName();
		File tempDir = (File) servletContext
				.getAttribute("javax.servlet.context.tempdir");

		Map<Thesaurus, Set<Alignment>> importResult = gincoImportService
				.importGincoXmlThesaurusFile(content, fileName, tempDir);
		Thesaurus thesaurus = importResult.keySet().iterator().next();

		indexerService.indexThesaurus(thesaurus);

		ImportedThesaurusResponse response = new ImportedThesaurusResponse();
		response.setThesaurusTitle(thesaurus.getTitle());
		List<String> missingExternalConcepts = new ArrayList<String>();
		Set<Alignment> bannedAlignments = importResult.get(thesaurus);
		for (Alignment ali : bannedAlignments) {
			missingExternalConcepts.add(ali.getSourceConcept().getIdentifier());
		}
		response.setConceptsMissingAlignments(missingExternalConcepts);
		ObjectMapper mapper = new ObjectMapper();
		String serialized = mapper.writeValueAsString(new ExtJsonFormLoadData(
				response));
		return serialized;
	}

	/**
	 * This method is called to import a Ginco XML concept branch. The
	 *
	 * @Produces({MediaType.TEXT_HTML ) is not a mistake : this rest service is
	 *                                used by an ajax call and IE cannot display
	 *                                result if JSOn is returned
	 *
	 * @param body
	 * @param request
	 * @return The imported concept branch in JSOn string representing a
	 *         ExtJsonFormLoadData
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws TechnicalException
	 * @throws BusinessException
	 */
	@POST
	@Path("/importGincoBranchXml")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	@PreAuthorize("hasPermission(#thesaurusId, '0')")
	public String uploadGincoBranchXmlFile(MultipartBody body,
			@QueryParam("thesaurusId") String thesaurusId,
			@Context HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException,
			TechnicalException, BusinessException {
		Attachment file = body.getAttachment("import-file-path");

		String content = file.getObject(String.class);
		String fileName = file.getDataHandler().getName();
		File tempDir = (File) servletContext
				.getAttribute("javax.servlet.context.tempdir");

		Map<ThesaurusConcept, Set<Alignment>> importResult = gincoImportService
				.importGincoBranchXmlFile(content, fileName, tempDir,
						thesaurusId);
		indexerService.indexThesaurus(thesaurusService
				.getThesaurusById(thesaurusId));
		ThesaurusConcept concept = importResult.keySet().iterator().next();
		ObjectMapper mapper = new ObjectMapper();
		ImportedBranchResponse response = new ImportedBranchResponse();
		response.setTitle(thesaurusConceptService.getConceptTitle(concept));
		response.setConceptView(thesaurusConceptViewConverter.convert(concept));
		if (!importResult.get(concept).isEmpty()) {
			response.setTargetInternalConceptsMissing(true);
		} else {
			response.setTargetInternalConceptsMissing(false);
		}
		String serialized = mapper.writeValueAsString(new ExtJsonFormLoadData(
				response));
		return serialized;
	}

	@POST
	@Path("/importSandBoxTerms")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	@PreAuthorize("hasPermission(#thesaurusId, '0') or hasPermission(#thesaurusId, '1')")
    public String importSandBoxTerms(MultipartBody body,
			@QueryParam("thesaurusId") String thesaurusId,
			@Context HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException,
			TechnicalException, BusinessException {
		try {
			Attachment file = body.getAttachment("import-file-path");
			String content = file.getObject(String.class);

			String[] termsSplit = content.split("\n|\r\n");
			List<String> termsLines = Arrays.asList(termsSplit);
			Map<String, Language> terms = new HashMap<String, Language>();
			List<String> termsInError = new ArrayList<String>();
			for (String termLine:termsLines) {
				String[] termSplitted = termLine.split("@");
				if (termSplitted.length==2) {
					String lexValue = termSplitted[0];
					if (StringUtils.isNotEmpty(lexValue)){
						Language lang = languageService.getLanguageById(termSplitted[1]);
						if (lang == null) {
							lang = languageService.getLanguageByPart1(termSplitted[1]);
						}
						if (lang != null) {
							terms.put(lexValue, lang);
						} else {
							termsInError.add(termLine);
						}
					} else {
						termsInError.add(termLine);
					}
				} else {
					String lexValue = termLine;
					Language lang = languageService.getLanguageById(defaultLang);
					terms.put(lexValue, lang);
				}
			}

			int defaultStatus = TermStatusEnum.VALIDATED.getStatus();
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			String username = auth.getName();

			if (userRoleService.hasRole(username, thesaurusId, Role.EXPERT)) {
				defaultStatus = TermStatusEnum.CANDIDATE.getStatus();
			}


			List<ThesaurusTerm> sandboxedTerms = thesaurusTermService
					.importSandBoxTerms(terms, thesaurusId, defaultStatus);
			for (ThesaurusTerm sandboxedTerm : sandboxedTerms) {
				indexerService.addTerm(sandboxedTerm);
			}
			ImportedTermsResponse response = new ImportedTermsResponse();
			response.setTermsInError(termsInError);
			ObjectMapper mapper = new ObjectMapper();
			String serialized = mapper.writeValueAsString(new ExtJsonFormLoadData(
					response));
			return serialized;

		} catch (BusinessException ex) {
			throw ex;
		} catch (Exception re) {
			throw new BusinessException("Error reading imported file :"
					+ re.getMessage(), "import-unable-to-read-file", re);
		}
	}
}
