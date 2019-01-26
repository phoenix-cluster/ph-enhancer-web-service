package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.tools.internal.ws.wsdl.framework.Identifiable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.ExportService;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.HistogramService;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.IdentifierService;
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
@RequestMapping(value = "/v1/export")
@CrossOrigin(origins = "*")
@Api(tags = {"export"})
public class ExportController extends AbstractRestHandler {

    @Autowired
    private ExportService exportService;
    @Autowired
    private IdentifierService identifierService;

    @RequestMapping(value = "",
            method = RequestMethod.GET
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Export the PSMs to an JSON object, based on the parameters.", notes = "")
    public
    @ResponseBody
    String exportImpl(
            @ApiParam(value = "Identifier", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = Configure.DEFAULT_PROJECT_ID) String identifier,
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

        String accessionId = this.identifierService.getJobAccession(identifier);
        if(accessionId == null){
            System.out.println("Null accessionId");
            return "{\"error\":\" Null accessionId or not public \"}";
        }
        System.out.println("identifier " + identifier  + " ---> " + accessionId + "(accessionId)");
        List<ScoredPSM> exportedRecommBetterPsms = this.exportService.getExportedPsmSingleType(accessionId, "negscore", recommendScRange,
                hasAccept, defaultAcceptType, hasRejected);
        List<ScoredPSM> exportedNewIdentPsms= this.exportService.getExportedPsmSingleType(accessionId, "newid", recommendScRange,
                hasAccept, defaultAcceptType, hasRejected);
        List<ScoredPSM> exportedHighScPsms = this.exportService.getExportedPsmSingleType(accessionId, "posscore", recommendScRange,
                hasAccept, defaultAcceptType, hasRejected);

        List<ScoredPSM> exportedPsms = new ArrayList<>();
        if (exportedRecommBetterPsms != null ) exportedPsms.addAll(exportedRecommBetterPsms);
        if (exportedNewIdentPsms != null ) exportedPsms.addAll(exportedNewIdentPsms);
        if (exportedHighScPsms != null ) exportedPsms.addAll(exportedHighScPsms);

        System.out.println(exportedPsms.size() + " psm has been exported");

        String baseFilePath = context.getRealPath("");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateString = df.format(new Date());

        String relativePath = accessionId + File.separator + "export" + File.separator
                + dateString + File.separator;
        String pathname = baseFilePath + File.separator + relativePath;
        File dir = new File(pathname);

        dir.mkdirs();
        String fileName = accessionId + "_export_file.json";
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
