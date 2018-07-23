package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.minidev.json.JSONObject;
import org.apache.avro.data.Json;
import org.apache.hadoop.yarn.webapp.WebApp;
import org.mortbay.util.ajax.JSON;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Spectrum;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.SpectrumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/example/v1/spectrum")
@CrossOrigin(origins = "*")
@Api(tags = {"spectrum"})
public class SpectrumController extends AbstractRestHandler {

    @Autowired
    private SpectrumService spectrumService;

//    @RequestMapping(value = "",
//            method = RequestMethod.GET
////            ,produces = {"application/json", "application/xml"}
//            ,produces = {"application/json"}
//    )
//    @ResponseStatus(HttpStatus.OK)
//    @ApiOperation(value = "Get a paginated list of all scored PSMs.", notes = "The list is paginated. You can provide a page number (default 1) and a page size (default 100)")
//    public
//    @ResponseBody
//    PageOfScoredPSM getScoredPSMs(@ApiParam(value = "The page number, start at 1", required = true)
//                                      @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
//                               @ApiParam(value = "Tha page size", required = true)
//                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
//                                  @ApiParam(value = "The sortField", required = true)
//                                  @RequestParam(value = "sortField", required = true, defaultValue = DEFAULT_SORT_FIELD) String sortField,
//                                  @ApiParam(value = "The sort direction", required = true)
//                                  @RequestParam(value = "sortDirection", required = true, defaultValue = DEFAULT_SORT_DIRECTION) String sortDirection,
//                               HttpServletRequest request, HttpServletResponse response) {
////        return this.clusterService.getAllClusterIDs(page, size,null, null);
////        List<ScoredPSM> scoredPSMs = this.scoredPSMService.getScoredPSMs(page, size,null, null);
//        List<ScoredPSM> scoredPSMs = this.scoredPSMService.getScoredPSMs(page, size, sortField, sortDirection);
//        Integer totalElements = this.scoredPSMService.totalScoredPSM();
//        Integer totalPages = (int) Math.ceil((totalElements + 0.0)/size);
//        PageOfScoredPSM pageOfScoredPSM = new PageOfScoredPSM(size, page, totalElements, totalPages, sortField, sortDirection,scoredPSMs);
//
//        return pageOfScoredPSM;
//    }

    @RequestMapping(value = "/titles/{titles}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a collection of spectrum.", notes = "You have to provide valid spectrum titles.")
    public
    @ResponseBody
    List<Spectrum> getSpectraByTitles(@ApiParam(value = "The titles of the spectra.", required = true)
                             @PathVariable("titles") String titles,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Spectrum> spectra = this.spectrumService.getSpectraByTitles(titles);
//        checkResourceFound(cluster);
        //todo: http://goo.gl/6iNAkz
        return spectra;
    }

    @RequestMapping(value = "/peptide/{title}",method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a peptide sequence of spectrum by title.",notes = "You have to provide valid spectrum title.")
    @ResponseBody
    public Object getSpectrumPeptide(@ApiParam(value = "The title of the spectrum." ,required = true)
            @PathVariable("title") String title,
                                   HttpServletRequest request, HttpServletResponse response)throws Exception{
        System.out.println("peptide");
        System.out.println(title);
        String peptide = this.spectrumService.getSpecPeptideSeqByTitle(title);
        JSONObject rs  = new  JSONObject();
        rs.put("peptide_sequence",peptide);
        System.out.println(rs.toJSONString());
        return rs;
    }
    @RequestMapping(value = "/{title}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single spectrum.", notes = "You have to provide a valid spectrum title.")
    public
    @ResponseBody
    Spectrum getSpectrum(@ApiParam(value = "The title of the spectrum.", required = true)
                         @PathVariable("title") String title,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        Spectrum spectrum= this.spectrumService.getSpectrumByTitle(title);
//        checkResourceFound(cluster);
        //todo: http://goo.gl/6iNAkz
        return spectrum;
    }

}
