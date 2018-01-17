package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSMForWeb;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ScoredPSMService {


    @Autowired
    private HBaseDao hBaseDao;


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
                if (sortField.equals("confidentScore") && sortDirection == null) {
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

        StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName);
        if (sortField.equals("confidentScore") ||
                sortField.equals("recommConfidentScore") ||
                sortField.equals("clusterRatio") ||
                sortField.equals("clusterSize") ||
                sortField.equals("acceptance")
                ) {
            querySql.append(" ORDER BY " + Configure.COLUMN_MAP.get(sortField) + " " + sortDirection + " ");
        }
        querySql.append(" LIMIT " + size);
        querySql.append(" OFFSET " + (page - 1) * size);

        System.out.println("Going to execute: " + querySql);

        List<ScoredPSM> scoredPSMs = getScoredPSMs(querySql.toString(), resultType);
        List<ScoredPSMForWeb> scoredPSMsForWeb = new ArrayList<>();
        for (ScoredPSM scoredPSM : scoredPSMs) {
            ScoredPSMForWeb scoredPSMForWeb = (ScoredPSMForWeb) scoredPSM;
            scoredPSMForWeb.setPTMs();
            scoredPSMsForWeb.add(scoredPSMForWeb);
        }

        return (scoredPSMsForWeb != null && scoredPSMsForWeb.size() > 0) ? (List) scoredPSMsForWeb : null;
    }

    protected List<ScoredPSM> getScoredPSMs(String querySql, String resultType) {
        List<ScoredPSM> scoredPSMs = (List<ScoredPSM>) hBaseDao.getScoredPSMs(querySql.toString(), null, new RowMapper<ScoredPSM>() {
            @Override
            public ScoredPSM mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSMForWeb();
                scoredPSM.setId(rs.getInt("ID"));
                if (resultType != "newid") {
                    scoredPSM.setPeptideSequence(rs.getString("PEP_SEQ"));
                    scoredPSM.setPeptideModsStr(rs.getString("PEP_MODS"));
                }
                scoredPSM.setClusterId(rs.getString("CLUSTER_ID"));
                scoredPSM.setClusterRatio(rs.getFloat("CLUSTER_RATIO"));
                scoredPSM.setClusterSize(rs.getInt("CLUSTER_SIZE"));

                if (resultType == "newid" || resultType == "negscore") {
                    scoredPSM.setRecommConfidentScore(rs.getFloat("RECOMM_SEQ_SC"));
                }
                if (resultType == "posscore" || resultType == "negscore") {
                    scoredPSM.setConfidentScore(rs.getFloat("CONF_SC"));
                }
                scoredPSM.setRecommendPeptide(rs.getString("RECOMMEND_PEP"));
                scoredPSM.setRecommendPepModsStr(rs.getString("RECOMMEND_MODS"));

                scoredPSM.setSpectraNum(rs.getInt("NUM_SPEC"));
                scoredPSM.setSpectraTitles(ClusterUtils.getStringListFromString(rs.getString("SPECTRA"),"\\|\\|"));
                scoredPSM.setAcceptance(rs.getInt("ACCEPTANCE"));

                return scoredPSM;
            }
        });
        return scoredPSMs;
    }

    public ScoredPSM getPSMByTitle(String title, Object o) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM compare_5_clusters WHERE ");
        querySql.append("\"ROW\" = '" + title + "'");

        ScoredPSM scoredPSM = (ScoredPSM) hBaseDao.getScoredPSM(querySql.toString(), null, new RowMapper<ScoredPSM>() {
            @Override
            public ScoredPSM mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSM();
                return scoredPSM;
            }
        });
        return scoredPSM;

    }

    public Integer totalScoredPSM(String projectId, String type) {
        String psmTableName = "";
        switch (type) {
            case ("negscore"): {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
                break;
            }
            case ("posscore"): {
                psmTableName = Configure.POS_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
                break;
            }
            case ("newid"): {
                psmTableName = Configure.NEW_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
                break;
            }
            default: {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
            }
        }

        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + psmTableName);
        Integer totalElement = (Integer) hBaseDao.queryTotal(querySql.toString());
        return totalElement;
    }

    public ResponseEntity<String> updateUserAcceptance(String projectId, String psmType, Map<Integer, Integer> acceptanceMap) {
        String psmTableName = "";
        if (psmType.equalsIgnoreCase("newid")) {
            psmTableName = Configure.NEW_PSM_TABLE.replace(Configure.DEFAULT_PROJECT_ID, projectId);
        } else if (psmType.equalsIgnoreCase("negscore") || psmType.equalsIgnoreCase("posscore")) {
            psmTableName = Configure.SCORE_PSM_TABLE.replace(Configure.DEFAULT_PROJECT_ID, projectId);
        } else {
            return new ResponseEntity<String>("Wrong psm type: " + psmType, HttpStatus.BAD_REQUEST);
        }

        List<String> updateSqls = new ArrayList<String>();
        for (Integer id : acceptanceMap.keySet()) {
            Integer acceptanceStatus = acceptanceMap.get(id);
            StringBuffer updateSql = new StringBuffer("UPSERT INTO " + psmTableName + "(ID, ACCEPTANCE) VALUES (" + id + "," + acceptanceStatus + ")");
            updateSqls.add(updateSql.toString());
        }
        hBaseDao.batchUpdate(updateSqls);
        return new ResponseEntity(acceptanceMap, HttpStatus.OK);
    }
}


