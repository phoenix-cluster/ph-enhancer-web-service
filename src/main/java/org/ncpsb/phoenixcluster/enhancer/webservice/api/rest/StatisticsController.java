package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.HistogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/example/v1/statistics")
@CrossOrigin(origins = "*")
@Api(tags = {"statistics"})
public class StatisticsController extends AbstractRestHandler {

    @Autowired
//    private StatisticsService statisticsService;
    private HistogramService histogramService;

    @RequestMapping(value = "/histogram",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all clusters.", notes = "The list is paginated. You can provide a page number (default 1) and a page size (default 100)")
    public
    @ResponseBody
    //Page<Cluster> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
    List<HistogramBin> getHistData(
            @ApiParam(value = "Project Id", required = true)
            @RequestParam(value = "projectId", required = true, defaultValue = Configure.DEFAULT_PROJECT_ID) String projectId,
            @ApiParam(value = "The bin number for histogram", required = true)
            @RequestParam(value = "numBins", required = true, defaultValue = "20") Integer numBins,
            @ApiParam(value = "psmType", required = true)
            @RequestParam(value = "psmType", required = true, defaultValue = "newid")  String psmType,
            @ApiParam(value = "fieldType", required = true)
            @RequestParam(value = "fieldType", required = true, defaultValue = "confScore")  String fieldType,
            HttpServletRequest request, HttpServletResponse response) {
        assert (numBins>0);
        List<HistogramBin> histogramBins = this.histogramService.getHistData(projectId, psmType, fieldType, numBins);
        return histogramBins;
    }
}
