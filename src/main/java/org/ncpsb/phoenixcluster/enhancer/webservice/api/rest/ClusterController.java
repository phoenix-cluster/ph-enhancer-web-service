package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import org.ncpsb.phoenixcluster.enhancer.webservice.domain.Cluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.ClusterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping(value = "/example/v1/clusters")
@Api(tags = {"clusters"})
public class ClusterController extends AbstractRestHandler {

    @Autowired
    private ClusterService clusterService;

    @RequestMapping(value = "",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all clusters.", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public
    @ResponseBody
    //Page<Cluster> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
    List<String> getAllCluster(@ApiParam(value = "The page number (zero-based)", required = true)
                                      @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                               @ApiParam(value = "Tha page size", required = true)
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                               HttpServletRequest request, HttpServletResponse response) {
//        return this.clusterService.getAllClusterIDs(page, size,null, null);
        List<String> allClusterIDs = this.clusterService.getAllClusterIDs(page, size,null, null);

        for (String clusterID : allClusterIDs) {
            System.out.println("::" + clusterID);
        }
        return allClusterIDs;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single cluster.", notes = "You have to provide a valid cluster ID.")
    public
    @ResponseBody
    Cluster getCluster(@ApiParam(value = "The ID of the cluster.", required = true)
                             @PathVariable("id") String id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cluster cluster = this.clusterService.getClusterByID(id, null);
        checkResourceFound(cluster);
        //todo: http://goo.gl/6iNAkz
        return cluster;
    }

}
