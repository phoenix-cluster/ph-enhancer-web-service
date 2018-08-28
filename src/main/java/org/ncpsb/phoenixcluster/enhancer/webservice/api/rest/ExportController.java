package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.ExportService;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.HistogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/example/v1/export")
@CrossOrigin(origins = "*")
@Api(tags = {"export"})
public class ExportController extends AbstractRestHandler {

    @Autowired
    private ExportService exportService;

    @RequestMapping(value = "",
            method = RequestMethod.GET
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Export the PSMs to an JSON object, based on the parameters.", notes = "")
    public
    @ResponseBody
    String exportImpl(
            @ApiParam(value = "Project Id", required = true)
            @RequestParam(value = "projectId", required = true, defaultValue = Configure.DEFAULT_PROJECT_ID) String projectId,
            @ApiParam(value = "recommed confident score Range, [0,0] means it is not included", required = true)
            @RequestParam(value = "recommendRange", required = true, defaultValue = "[0,1]") String recommendScRange,
            @ApiParam(value = "new Identiying PSMs Score range, [0,0] means it is not included", required = true)
            @RequestParam(value = "newIdentScRange", required = true, defaultValue = "[0,0]") String newIdentScRange,
            @ApiParam(value = "highConfScRange, [0,0] means it is not included", required = true)
            @RequestParam(value = "highConfScRange", required = true, defaultValue = "[0,0]") String highConfScRange,
            @ApiParam(value = "include the mannually checked PSMs or not", required = true)
            @RequestParam(value = "hasAccept", required = true, defaultValue = "true") Boolean hasAccept,
            @ApiParam(value = "include the default acceptance as reject or accept or none", required = true)
            @RequestParam(value = "defaultAcceptType", required = true, defaultValue = "none") String defaultAcceptType,
            @ApiParam(value = "include the mannually rejected or not", required = true)
            @RequestParam(value = "hasRejected", required = true, defaultValue = "false") Boolean hasRejected,
            HttpServletRequest request, HttpServletResponse response) {

        List<ScoredPSM> exportedRecommBetterPsms = this.exportService.getExportedPsmSingleType(projectId, "negscore", recommendScRange,
                hasAccept, defaultAcceptType, hasRejected);
        List<ScoredPSM> exportedNewIdentPsms= this.exportService.getExportedPsmSingleType(projectId, "newid", recommendScRange,
                hasAccept, defaultAcceptType, hasRejected);
        List<ScoredPSM> exportedHighScPsms = this.exportService.getExportedPsmSingleType(projectId, "posscore", recommendScRange,
                hasAccept, defaultAcceptType, hasRejected);

        List<ScoredPSM> exportedPsms = new ArrayList<>();
        if (exportedRecommBetterPsms != null ) exportedPsms.addAll(exportedRecommBetterPsms);
        if (exportedNewIdentPsms != null ) exportedPsms.addAll(exportedNewIdentPsms);
        if (exportedHighScPsms != null ) exportedPsms.addAll(exportedHighScPsms);

        System.out.println(exportedPsms.size() + " psm has been exported");

        String baseFilePath = context.getRealPath("");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateString = df.format(new Date());

        String relativePath = projectId  + File.separator + "export" + File.separator
                + dateString + File.separator;
        String pathname = baseFilePath + File.separator + relativePath;
        File dir = new File(pathname);

        dir.mkdirs();
        String fileName = projectId + "_export_file.json";
        File file = new File(pathname + fileName);

        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            String jsonString = gson.toJson(exportedPsms);
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }

        System.out.println(file.toString());
        return "{\"filePath\":\"" + relativePath.toString() + fileName + "\"}";
    }

    @Autowired
    ServletContext context;

}
