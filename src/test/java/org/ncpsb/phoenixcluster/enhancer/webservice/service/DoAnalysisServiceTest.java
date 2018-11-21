package org.ncpsb.phoenixcluster.enhancer.webservice.service;

import org.junit.Before;
import org.junit.Test;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.PageOfFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class DoAnalysisServiceTest {
    DoAnalysisService doAnalysisService = new DoAnalysisService();
    @Before
    public void setUp(){
        DoAnalysisService doAnalysisService = new DoAnalysisService();
    }
    @Test
    public void doAnalysis() {
    }

    @Test
    public void getAnalysisJob() {
    }

    @Test
    public void getAnalysisJobByToken() {
    }

    @Test
    public void getPageFromLog() {
        AnalysisJob analysisJob = new AnalysisJob(27, "/tmp/data/20180808/27/", "20180808", 0, "finished", false, null);
//        AnalysisJob analysisJob = new AnalysisJob(25, "C:\\Users\\baimi\\AppData\\Local\\Temp\\data\\20180808\\25", "20180808", 0, "finished", false, null);
        PageOfFile pageOfFile = null;
        try {
            pageOfFile = this.doAnalysisService.getPageFromLog(analysisJob, 5);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(pageOfFile);
    }
}