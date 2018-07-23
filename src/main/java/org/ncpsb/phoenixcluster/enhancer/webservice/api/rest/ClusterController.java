package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.Cluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
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
@CrossOrigin(origins = "*")
@Api(tags = {"clusters"})
public class ClusterController extends AbstractRestHandler {
    @Autowired
    private ClusterService clusterService;
    @RequestMapping(value = "", method = RequestMethod.GET,produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all clusters.", notes = "The list is paginated. You can provide a page number (default 1) and a page size (default 100)")
    @ResponseBody
    public List<String> getClusters(@ApiParam(value = "The page number ", required = true)
                                        @RequestParam(value = "page", required = true, defaultValue = Configure.DEFAULT_PAGE_NUM) Integer page,
                               @ApiParam(value = "Tha page size", required = true)
                                      @RequestParam(value = "size", required = true, defaultValue = Configure.DEFAULT_PAGE_SIZE) Integer size,
                               HttpServletRequest request, HttpServletResponse response) {
        assert (page>0 && size>0);
        List<String> allClusterIds = this.clusterService.getClusterIds(page, size,null, null);
        for (String clusterId : allClusterIds) {
            System.out.println("::::" + clusterId);
        }
        return allClusterIds;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single cluster.", notes = "You have to provide a valid cluster Id.")
    public
    @ResponseBody
    Cluster getCluster(@ApiParam(value = "The Id of the cluster.", required = true)
                             @PathVariable("id") String id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cluster cluster = this.clusterService.getClusterById(id);
        checkResourceFound(cluster);
        //todo: http://goo.gl/6iNAkz
        return cluster;
    }

}
