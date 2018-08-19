package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.FileUploadResponse;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.PageOfFile;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ResultFileList;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.DoAnalysisService;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.catalina.servlet4preview.ServletContext;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("example/v1/analysis")
@CrossOrigin(origins = "*")
public class AnalysisController {

    @Autowired
    DoAnalysisService doAnalysisService;

    @RequestMapping(value = "/do", method = RequestMethod.POST, produces = {"application/json"})

    public String doAnalysis(HttpServletRequest request,
    @RequestHeader("analysisId") Integer analysisId,
    @RequestHeader("minClusterSize") Integer minClusterSize
//    @RequestHeader("userEmail") String userEmail,
//    @RequestBody String resultFileList
//    @RequestBody ResultFileList resultFileList
    )
    {
        String returnMessage = doAnalysisService.doAnalysis(analysisId, minClusterSize);
        return returnMessage;
    }

    @RequestMapping(value = "/getAnalysisJob", method = RequestMethod.GET, produces = {"application/json"})
    public AnalysisJob getAnalysisJob(HttpServletRequest request,
    @RequestHeader("analysisId") Integer analysisId
//    @RequestHeader("userEmail") String userEmail,
//    @RequestBody String resultFileList
//    @RequestBody ResultFileList resultFileList
    )
    {
        AnalysisJob analysisJob = doAnalysisService.getAnalysisJob(analysisId);
        return analysisJob;
    }

    @RequestMapping(value = "/getAnalysisJobByToken", method = RequestMethod.GET, produces = {"application/json"})
    public AnalysisJob getAnalysisJob(HttpServletRequest request,
    @ApiParam(value = "analysis Job Token", required = true)
    @RequestParam(value = "token", required = true, defaultValue = "8kmv54bhed") String analysisJobToken
    )
    {
        AnalysisJob analysisJob = doAnalysisService.getAnalysisJobByToken(analysisJobToken);
        return analysisJob;
    }

    @RequestMapping(value = "/getPageOfLogByToken", method = RequestMethod.GET, produces = {"application/json"})
    public PageOfFile getPageOfLogByToken(HttpServletRequest request,
        @ApiParam(value = "analysis Job Token", required = true)
        @RequestParam(value = "token", required = true, defaultValue = "8kmv54bhed") String analysisJobToken,
        @RequestParam(value = "startLineNo", required = true, defaultValue = "1")
        @ApiParam(value = "start line No.", required = true)Integer startLine
    ) throws FileNotFoundException {
        AnalysisJob analysisJob = this.doAnalysisService.getAnalysisJobByToken(analysisJobToken);
        PageOfFile pageOfLogFile = doAnalysisService.getPageFromLog(analysisJob, startLine);
        return pageOfLogFile;
    }

    @Autowired
    ServletContext context;
}
