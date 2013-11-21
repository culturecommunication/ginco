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
package fr.mcc.ginco.extjs.view;

import java.io.File;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringEscapeUtils;

import fr.mcc.ginco.utils.DateUtil;

/**
 * Class for JSON representation of a data object for extjs.
 */
public class FileResponse {
	private File file;
	private String extension;
	private String title;

	public FileResponse(File file, String extension, String title) {
		this.file = file;
		this.extension = extension;
		this.title = StringEscapeUtils.unescapeHtml4(title).replaceAll("[^a-zA-Z0-9\\._]+", "_");
	
	}

	public Response toResponse() {
		Response.ResponseBuilder response = Response.ok(file);
		response.header("Content-Disposition", "attachment; filename=\""
				+ title + " " + DateUtil.toString(DateUtil.nowDate())
				+ extension + "\"");

		return response.build();
	}
}
