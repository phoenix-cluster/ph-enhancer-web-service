package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.ScoredPSMService;

import org.ncpsb.phoenixcluster.enhancer.webservice.service.IdentifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure.*;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/example/v1/scoredpsms")
@CrossOrigin(origins = "*")
@Api(tags = {"scoredPSMs"})
public class ScoredPSMController extends AbstractRestHandler {

    @Autowired
    private ScoredPSMService scoredPSMService;
    @Autowired
    private IdentifierService identifierService;

    @RequestMapping(value = "/negscore",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            , produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all scored PSMs.", notes = "The list is paginated. You can provide a page number (default 1) and a page size (default 100)")
    public
    @ResponseBody
    PageOfScoredPSM getNegScoredPSMs(
            @ApiParam(value = "The Results Identifier, given by PX ID, Phoenix Enhancer ID or Token", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = DEFAULT_PROJECT_ID) String identifier,
            @ApiParam(value = "The page number, start at 1", required = true)
            @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "Tha page size", required = true)
            @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @ApiParam(value = "The sortField", required = true)
            @RequestParam(value = "sortField", required = true, defaultValue = DEFAULT_SORT_FIELD) String sortField,
            @ApiParam(value = "The sort direction", required = true)
            @RequestParam(value = "sortDirection", required = true, defaultValue = Configure.DEFAULT_SORT_DIRECTION) String sortDirection,
            HttpServletRequest request, HttpServletResponse response) {
//        return this.clusterService.getAllClusterIDs(page, size,null, null);
//        List<ScoredPSM> scoredPSMs = this.scoredPSMService.getScoredPSMs(page, size,null, null);
        String accessionId = this.identifierService.getJobAccession(identifier);
        List<ScoredPSMForWeb> scoredPSMsForWeb = this.scoredPSMService.getScoredPSMsForWeb(accessionId, page, size, sortField, sortDirection, "negscore");
        Integer totalElements = this.scoredPSMService.findTotalScoredPSM(accessionId, "negscore");
        Integer totalPages = (int) Math.ceil((totalElements + 0.0) / size);
        PageOfScoredPSM pageOfScoredPSM = new PageOfScoredPSM(accessionId, size, page, totalElements, totalPages, sortField, sortDirection, scoredPSMsForWeb);

        return pageOfScoredPSM;
    }

    /**
     * Retrive new identified psms from phenix
     *
     * @param page
     * @param size
     * @param sortField
     * @param sortDirection
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/posscore",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            , produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all scored PSMs.", notes = "The list is paginated. You can provide a page number (default 1) and a page size (default 100)")
    public
    @ResponseBody
    PageOfScoredPSM getPosScoredPSMs(
            @ApiParam(value = "The Project ID, given by PX ID or Phoenix ID", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = DEFAULT_PROJECT_ID) String identifier,
            @ApiParam(value = "The page number, start at 1", required = true)
            @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "Tha page size", required = true)
            @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @ApiParam(value = "The sortField", required = true)
            @RequestParam(value = "sortField", required = true, defaultValue = DEFAULT_SORT_FIELD) String sortField,
            @ApiParam(value = "The sort direction", required = true)
            @RequestParam(value = "sortDirection", required = true, defaultValue = DEFAULT_SORT_DIRECTION) String sortDirection,
            HttpServletRequest request, HttpServletResponse response) {
//        return this.clusterService.getAllClusterIDs(page, size,null, null);
//        List<ScoredPSM> scoredPSMs = this.scoredPSMService.getScoredPSMs(page, size,null, null);
        String accessionId = this.identifierService.getJobAccession(identifier);
        System.out.println("identifier " + identifier  + " ---> " + accessionId + "(accessionId)");
        List<ScoredPSMForWeb> scoredPSMsForWeb = this.scoredPSMService.getScoredPSMsForWeb(accessionId, page, size, sortField, sortDirection, "posscore");
        Integer totalElements = this.scoredPSMService.findTotalScoredPSM(accessionId, "posscore");
        Integer totalPages = (int) Math.ceil((totalElements + 0.0) / size);
        PageOfScoredPSM pageOfScoredPSM = new PageOfScoredPSM(accessionId, size, page, totalElements, totalPages, sortField, sortDirection, scoredPSMsForWeb);

        return pageOfScoredPSM;
    }


    /**
     * Retrive new identified psms from phenix
     *
     * @param page
     * @param size
     * @param sortField
     * @param sortDirection
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/newid",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            , produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all scored PSMs.", notes = "The list is paginated. You can provide a page number (default 1) and a page size (default 100)")
    public
    @ResponseBody
    PageOfScoredPSM getNewIdScoredPSMs(
            @ApiParam(value = "The Project ID, given by PX ID or Phoenix ID", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = DEFAULT_PROJECT_ID) String identifier,
            @ApiParam(value = "The page number, start at 1", required = true)
            @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "Tha page size", required = true)
            @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @ApiParam(value = "The sortField", required = true)
            @RequestParam(value = "sortField", required = true, defaultValue = "RECOMM_SEQ_SC") String sortField,
            @ApiParam(value = "The sort direction", required = true)
            @RequestParam(value = "sortDirection", required = true, defaultValue = DEFAULT_SORT_DIRECTION) String sortDirection,
            HttpServletRequest request, HttpServletResponse response) {
//        return this.clusterService.getAllClusterIDs(page, size,null, null);
//        List<ScoredPSM> scoredPSMs = this.scoredPSMService.getScoredPSMs(page, size,null, null);
        String accessionId = this.identifierService.getJobAccession(identifier);
        List<ScoredPSMForWeb> scoredPSMsForWeb = this.scoredPSMService.getScoredPSMsForWeb(accessionId, page, size, sortField, sortDirection, "newid");
        Integer totalElements = this.scoredPSMService.findTotalScoredPSM(accessionId, "newid");
        Integer totalPages = (int) Math.ceil((totalElements + 0.0) / size);
        PageOfScoredPSM pageOfScoredPSM = new PageOfScoredPSM(accessionId, size, page, totalElements, totalPages, sortField, sortDirection, scoredPSMsForWeb);

        return pageOfScoredPSM;
    }


/*
    @RequestMapping(value = "/{projectId}/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single scored PSM.", notes = "You have to provide a valid spectrum title.")
    public
    @ResponseBody
    ScoredPSM getScoredPSM(
            @ApiParam(value = "The Project ID, given by PX ID or Phoenix ID", required = true)
            @PathVariable("projectId") String projectId,
            @ApiParam(value = "The ID of the scoredPsm.", required = true)
            @PathVariable("id") String id,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScoredPSM scoredPSM = this.scoredPSMService.findPSMByTitle(projectId, id);
//        checkResourceFound(cluster);
        //todo: http://goo.gl/6iNAkz
        return scoredPSM;
    }
*/


    @RequestMapping(value = "/updateAcceptance",
            method = RequestMethod.PUT,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Put the user's acceptance statuses into database.", notes = "id:statusnumber, -1 for reject, 1 for accpeta")
    public
    @ResponseBody
    ResponseEntity<?> putUserAcceptance(
            @ApiParam(value = "The Project ID, given by PX ID or Phoenix ID", required = true)
            @RequestParam(value = "identifier", required = true, defaultValue = DEFAULT_PROJECT_ID) String identifier,
            @ApiParam(value = "The psmtype of this psm table.", required = true)
            @RequestParam(value = "psmtype", required = true, defaultValue = "newid") String psmtype,
            @RequestBody(required = true) Map<Integer, Integer> acceptanceMap,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (acceptanceMap.size() < 1) {
            return new ResponseEntity<String>("Empty acceptance map.", HttpStatus.BAD_REQUEST);
        }
        String accessionId = this.identifierService.getJobAccession(identifier);
        return (this.scoredPSMService.updateUserAcceptance(accessionId, psmtype, acceptanceMap));
    }


}
