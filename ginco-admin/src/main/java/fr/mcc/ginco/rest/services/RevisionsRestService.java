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

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.FileResponse;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.IGincoRevService;
import fr.mcc.ginco.services.ILanguagesService;
import fr.mcc.ginco.services.IThesaurusService;

/**
 * REST service to get the revisions command file.
 */
@Service
@Path("/revisionservice")
@PreAuthorize("isAuthenticated()")
public class RevisionsRestService {

	@Inject
	@Named("gincoRevService")
	private IGincoRevService gincoRevService;

	@Inject
	@Named("thesaurusService")
	private IThesaurusService thesaurusService;

	@Inject
	@Named("languagesService")
	private ILanguagesService languagesService;

	@Value("${ginco.default.language}")
	private String defaultLang;

	@Log
	private Logger log;

	/**
	 * Return revisions command file in .txt format; name begins with current DateTime.
	 * 
	 * @param thesaurusId
	 * @return
	 * @throws BusinessException
	 * @throws IOException
	 */
	@GET
	@Path("/exportRevisions")
	@Produces("text/plain")
	public Response exportRevisions(
			@QueryParam("thesaurusId") String thesaurusId,
			@QueryParam("timestamp") long timestamp,
			@QueryParam("lang") String language) throws BusinessException,
			IOException {
		Thesaurus thesaurus = null;
		if (StringUtils.isNotEmpty(thesaurusId)) {
			thesaurus = thesaurusService.getThesaurusById(thesaurusId);
		}
		if (thesaurus == null) {
			throw new BusinessException("Invalid thesaurusId " + thesaurusId,
					"revision-export-invalid-thesaurus-id");
		}
		Language lang = null;
		if (StringUtils.isNotEmpty(language)) {	
			lang = languagesService.getLanguageById(language);
		}
		if (lang == null) {
			log.info("No language set in exportREvisions, defaulting to default language "
					+ defaultLang);
			lang = languagesService.getLanguageById(defaultLang);
		}

		File resFile = gincoRevService.getRevisions(thesaurus, timestamp, lang);

		return new FileResponse(resFile, ".txt", thesaurus.getTitle())
				.toResponse();
	}
}
