package org.ncpsb.phoenixcluster.enhancer.webservice.service;

import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.AnalysisJobDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class IdentifierService {
    @Autowired
    private AnalysisJobDaoMysqlImpl analysisJobDao;
    public String getJobAccession(String identifier) {
        String idType = getIdType(identifier);
        String accessionId = null;
        Integer analysisJobId = 0;
        if (idType.equalsIgnoreCase("ex")) {
            analysisJobId = Integer.valueOf(identifier.substring(1, identifier.length()));
            AnalysisJob analysisJob = analysisJobDao.getAnalysisJob(analysisJobId);
            if (!analysisJob.getPublic()) {
                System.out.println("This analysis job " + analysisJobId + " is not public!");
                return null;
            }
            accessionId = identifier;
        }

        if (idType.equalsIgnoreCase("token")) {
            AnalysisJob analysisJob = analysisJobDao.findAnalysisJobByToken(identifier);
            analysisJobId = analysisJob.getId();
            accessionId = String.format("E%06d", analysisJobId);
        }
        if (idType.equalsIgnoreCase("px")) {
            accessionId = identifier;
        }
        return accessionId;
    }

    public static String getIdType(String resultId) {
        if (Pattern.matches("[\\w,\\d]{10}", resultId)) {
            return "token";
        }
        if (Pattern.matches("P[X,R]D\\d{6}", resultId)) {
            return "px";
        }
        if (Pattern.matches("E\\d{6}", resultId)) {
            return "ex";
        }
        return "error";
    }

}
