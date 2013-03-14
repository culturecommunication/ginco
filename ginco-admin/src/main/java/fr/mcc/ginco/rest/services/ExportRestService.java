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

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.IExportService;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.services.IThesaurusConceptService;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * REST service to get exported objects.
 */
@Service
@Path("/exportservice")
public class ExportRestService {

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;

    @Inject
    @Named("exportService")
    private IExportService exportService;

    private static final String TABULATION_DELIMITER = "\t";

    @Inject
    @Named("thesaurusService")
    private IThesaurusService thesaurusService;

    /**
     * Return file in .txt format; name begins with current DateTime.
     * @param thesaurusId
     * @return
     * @throws BusinessException
     */
    @GET
    @Path("/getHierarchical")
    @Produces("text/plain")
    public Response getHierarchical(@QueryParam("thesaurusId") String thesaurusId) throws BusinessException {

        Thesaurus targetThesaurus = thesaurusService.getThesaurusById(thesaurusId);

        File temp;
        BufferedWriter out;
        try {
            temp = File.createTempFile("pattern", ".suffix");
            temp.deleteOnExit();
            out = new BufferedWriter(new FileWriter(temp));

            out.write("Édition hiérarchique du vocabulaire de ");
            out.write(targetThesaurus.getTitle());
            out.newLine();
            out.newLine();
            out.flush();

            List<FormattedLine> result = exportService.getHierarchicalText(targetThesaurus);

            for(FormattedLine results : result) {
                for(int i=0;i<results.tabs;i++) {
                    out.write(TABULATION_DELIMITER);
                }
                out.write(results.text);
                out.newLine();
                out.flush();
            }

        } catch (IOException e) {
            throw new BusinessException("Cannot create temp file!", "cannot-create-file");
        }

        Response.ResponseBuilder response = Response.ok(temp);
        response.header("Content-Disposition",
                "attachment; filename=\""
                        + targetThesaurus.getTitle()
                        + " "
                        + DateUtil.toString(DateUtil.nowDate())
                        + ".txt"
                        + "\"");

        return response.build();
    }
}
