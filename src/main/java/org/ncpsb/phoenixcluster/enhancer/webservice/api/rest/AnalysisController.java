package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.PageOfFile;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.DoAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.apache.catalina.servlet4preview.ServletContext;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/v1/analysis")
@CrossOrigin(origins = "*")
public class AnalysisController {

    @Autowired
    DoAnalysisService doAnalysisService;

//    @RequestMapping(value = "/do", method = RequestMethod.POST, produces = {"application/json"})
//
//    public String doAnalysis(HttpServletRequest request,
//    @RequestHeader("analysisId") Integer analysisId,
//    @RequestHeader("minClusterSize") Integer minClusterSize,
//    @RequestHeader("userEmailAdd") String userEmailAdd,
//    @RequestHeader("isPublic") Boolean isPublic
////    @RequestBody String resultFileList
////    @RequestBody ResultFileList resultFileList
//    )
//    {
//        System.out.println(userEmailAdd);
//        String returnMessage = doAnalysisService.doAnalysis(analysisId, minClusterSize, userEmailAdd, isPublic);
//        return returnMessage;
//    }
//
//    @RequestMapping(value = "/getAnalysisJob", method = RequestMethod.GET, produces = {"application/json"})
//    public AnalysisJob getAnalysisJob(HttpServletRequest request,
//    @RequestHeader("analysisId") Integer analysisId
////    @RequestHeader("userEmail") String userEmail,
////    @RequestBody String resultFileList
////    @RequestBody ResultFileList resultFileList
//    )
//    {
//        AnalysisJob analysisJob = doAnalysisService.getAnalysisJob(analysisId);
//        return analysisJob;
//    }

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
    ) {
        AnalysisJob analysisJob = this.doAnalysisService.getAnalysisJobByToken(analysisJobToken);
        PageOfFile pageOfLogFile;
        try {
            pageOfLogFile = doAnalysisService.getPageFromLog(analysisJob, startLine);
        } catch (FileNotFoundException e) {
            return new PageOfFile(new ArrayList<String>(), 0, 0, 0);
        }
        return pageOfLogFile;
    }


    @RequestMapping(value = "/apply", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AnalysisJob applyForAnalysisJob(
            ) throws IOException {
        AnalysisJob analysisJob = doAnalysisService.initAnalysisJob();
        return analysisJob;
    }

    @Autowired
    ServletContext context;
}
