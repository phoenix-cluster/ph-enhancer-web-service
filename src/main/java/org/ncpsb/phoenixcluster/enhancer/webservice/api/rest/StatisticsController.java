package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Thresholds;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.VennData;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.HistogramService;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.StatisticsService;
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
@RequestMapping(value = "/v1/statistics")
@CrossOrigin(origins = "*")
@Api(tags = {"statistics"})
public class StatisticsController extends AbstractRestHandler {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private HistogramService histogramService;

    @RequestMapping(value = "/histogram",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get histogram data of a chart.", notes = "")
    public
    @ResponseBody
    //Page<Cluster> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
    List<HistogramBin> getHistData(
            @ApiParam(value = "Identifier", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = Configure.DEFAULT_PROJECT_ID) String identifier,
            @ApiParam(value = "The bin number for histogram", required = true)
            @RequestParam(value = "numBins", required = true, defaultValue = "20") Integer numBins,
            @ApiParam(value = "psmType", required = true)
            @RequestParam(value = "psmType", required = true, defaultValue = "newid")  String psmType,
            @ApiParam(value = "fieldType", required = true)
            @RequestParam(value = "fieldType", required = true, defaultValue = "confScore")  String fieldType,
            HttpServletRequest request, HttpServletResponse response) {
        assert (numBins>0);
        List<HistogramBin> histogramBins = this.histogramService.getHistDataForDifferenetScAndFiled(identifier, psmType, fieldType, numBins);
        return histogramBins;
    }

    @RequestMapping(value = "/venndata",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get the numbers for venn diagram.", notes = "")
    public
    @ResponseBody
        //Page<Cluster> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
    VennData getVennData(
            @ApiParam(value = "Identifier", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = Configure.DEFAULT_PROJECT_ID) String identifier,
            HttpServletRequest request, HttpServletResponse response) {
        VennData vennData = this.statisticsService.findVennDataByProjectId(identifier);
        return vennData;
    }
    @RequestMapping(value = "/venndatalist",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get the venn data for all projects", notes = "")
    public
    @ResponseBody
        //Page<Cluster> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
    List<VennData> getVennDataList(
            HttpServletRequest request, HttpServletResponse response) {
        List<VennData> vennDataList = this.statisticsService.getVennDataList();
        return vennDataList;
    }


   @RequestMapping(value = "/thresholds",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get the threholds numbers of this project.", notes = "")
    public
    @ResponseBody
        //Page<Cluster> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
   Thresholds getThreholds(
            @ApiParam(value = "Identifier", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = Configure.DEFAULT_PROJECT_ID) String identifier,
            HttpServletRequest request, HttpServletResponse response) {
       Thresholds thresholds = this.statisticsService.findThresholdsByProjectId(identifier);
       return thresholds;
   }

   @RequestMapping(value = "/projects",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get the the project list.", notes = "")
    public
    @ResponseBody
        //Page<Cluster> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
   List<String> getProjects(HttpServletRequest request, HttpServletResponse response) {
       List<String> projects = this.statisticsService.findProjects();
       return projects;
   }

}
