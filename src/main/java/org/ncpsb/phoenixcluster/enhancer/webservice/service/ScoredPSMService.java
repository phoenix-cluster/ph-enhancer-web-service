package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.ScoredPSMDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSMForWeb;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ScoredPSMService {


    @Autowired
    private ScoredPSMDaoMysqlImpl scoredPSMDao;

    /***
     * DAO
     * find scored PSMs for web
     * @param psmTableName
     * @param page
     * @param size
     * @param sortField
     * @param sortDirection
     * @param resultType
     * @return
     */
    public  List<ScoredPSMForWeb> findScoredPSMsForWeb(String psmTableName, Integer page, Integer size, String sortField, String sortDirection, String resultType) {
        List<ScoredPSM> scoredPSMs = scoredPSMDao.findScoredPSMs(psmTableName, page, size, sortField, sortDirection, resultType);
        List<ScoredPSMForWeb> scoredPSMsForWeb = new ArrayList<>();
        for (ScoredPSM scoredPSM : scoredPSMs) {
            ScoredPSMForWeb scoredPSMForWeb = (ScoredPSMForWeb) scoredPSM;
            scoredPSMForWeb.setPTMs();
            scoredPSMsForWeb.add(scoredPSMForWeb);
        }
        return scoredPSMsForWeb;
    }


    public List<ScoredPSMForWeb> getScoredPSMsForWeb(String projectId, Integer page, Integer size, String sortField, String sortDirection, String resultType) {
        String psmTableName = "";
        switch (resultType) {
            case ("negscore"): {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID,projectId);
                if (sortField.equals("confidentScore") && sortDirection == null) {
                    sortDirection = "ASC";
                }
                break;
            }
            case ("posscore"): {
                psmTableName = Configure.POS_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID,projectId);
                if (sortField.equals("confidentScore") && sortDirection == null) {
                    sortDirection = "DESC";
                }
                break;
            }
            case ("newid"): {
                psmTableName = Configure.NEW_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID,projectId);
                if (sortField.equals("RECOMM_SEQ_SC") && sortDirection == null) {
                    sortDirection = "DESC";
                }
                break;
            }
            default: {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID,projectId);
                if (sortField.equals("confidentScore") && sortDirection == null) {
                    sortDirection = "ASC";
                }
            }
        }
        if (sortDirection == null) {
            sortDirection = "DESC";
        }

        List<ScoredPSMForWeb> scoredPSMsForWeb = findScoredPSMsForWeb(psmTableName, page, size, sortField, sortDirection, resultType);
        return (scoredPSMsForWeb != null && scoredPSMsForWeb.size() > 0) ? (List) scoredPSMsForWeb : null;
    }


    public Integer findTotalScoredPSM(String projectId, String type) {
        return scoredPSMDao.findTotalScoredPSM(projectId, type);
    }


    public ResponseEntity<String> updateUserAcceptance(String projectId, String psmType, Map<Integer, Integer> acceptanceMap) {
        return scoredPSMDao.updateUserAcceptance(projectId, psmType, acceptanceMap);
    }
}


