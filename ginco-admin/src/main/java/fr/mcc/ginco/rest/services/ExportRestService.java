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
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.IExportService;
import fr.mcc.ginco.exports.ISKOSExportService;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.services.IThesaurusService;
import fr.mcc.ginco.utils.DateUtil;
import fr.mcc.ginco.utils.EncodedControl;

/**
 * REST service to get exported objects.
 */
@Service
@Path("/exportservice")
@PreAuthorize("isAuthenticated()")
public class ExportRestService {

    @Inject
    @Named("exportService")
    private IExportService exportService;
    
    
    @Inject
    @Named("skosExportService")
    private ISKOSExportService skosExportService;


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

        File result = writeExportFile(targetThesaurus, false);

        Response.ResponseBuilder response = Response.ok(result);
        response.header("Content-Disposition",
                "attachment; filename=\""
                        + targetThesaurus.getTitle()
                        + " "
                        + DateUtil.toString(DateUtil.nowDate())
                        + ".txt"
                        + "\"");

        return response.build();
    }


    @GET
    @Path("/getSKOS")
    @Produces("text/plain")
    public Response getSKOS(@QueryParam("thesaurusId") String thesaurusId) throws BusinessException {
        Thesaurus targetThesaurus = thesaurusService.getThesaurusById(thesaurusId);
        File results = skosExportService.getSKOSExport(targetThesaurus);

        Response.ResponseBuilder response = Response.ok(results);
        response.header("Content-Disposition",
                "attachment; filename=\"SKOS "
                        + targetThesaurus.getTitle()
                        + " "
                        + DateUtil.toString(DateUtil.nowDate())
                        + ".rdf"
                        + "\"");
        return response.build();
    }

    /**
     * Return file in .txt format; name begins with current DateTime.
     * @param thesaurusId
     * @return
     * @throws BusinessException
     */
    @GET
    @Path("/getAlphabetical")
    @Produces("text/plain")
    public Response getAlphabetical(@QueryParam("thesaurusId") String thesaurusId) throws BusinessException {
        Thesaurus targetThesaurus = thesaurusService.getThesaurusById(thesaurusId);

        File result = writeExportFile(targetThesaurus, true);

        Response.ResponseBuilder response = Response.ok(result);
        response.header("Content-Disposition",
                "attachment; filename=\""
                        + targetThesaurus.getTitle()
                        + " "
                        + DateUtil.toString(DateUtil.nowDate())
                        + ".txt"
                        + "\"");

        return response.build();
    }

    private File writeExportFile(Thesaurus targetThesaurus, boolean alphabetical) throws BusinessException{

        ResourceBundle res = ResourceBundle.getBundle("labels", new EncodedControl("UTF-8"));

        File temp;
        BufferedWriter out;
        try {
            temp = File.createTempFile("pattern", ".suffix");
            temp.deleteOnExit();
            out = new BufferedWriter(new FileWriter(temp));

            if(alphabetical) {
                out.write(res.getString("export-alphabetical").concat(" "));
            } else {
                out.write(res.getString("export-hierarchical").concat(" "));
            }
            out.write(targetThesaurus.getTitle());
            out.newLine();
            out.newLine();
            out.flush();

            List<FormattedLine> result;
            if(alphabetical) {
                result = exportService.getAlphabeticalText(targetThesaurus);
            } else {
                result = exportService.getHierarchicalText(targetThesaurus);
            }

            for(FormattedLine results : result) {
                for(int i=0;i<results.getTabs();i++) {
                    out.write(TABULATION_DELIMITER);
                }
                out.write(results.getText());
                out.newLine();
                out.flush();
            }

        } catch (IOException e) {
            throw new BusinessException("Cannot create temp file!", "cannot-create-file", e);
        }

        return temp;
    }
}

