package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import io.swagger.models.auth.In;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSMForWeb;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSMRowMapper;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ScoredPSMService {


    @Autowired
    private HBaseDao hBaseDao;

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
        List<ScoredPSM> scoredPSMs = findScoredPSMs(psmTableName, page, size, sortField, sortDirection, resultType);
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

    /***
     * Dao
     * @param psmTableName
     * @param page
     * @param size
     * @param sortField
     * @param sortDirection
     * @param resultType
     * @return
     */
    protected List<ScoredPSM> findScoredPSMs(String psmTableName, Integer page, Integer size, String sortField, String sortDirection, String resultType) {
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
        List<ScoredPSM> scoredPSMs = (List<ScoredPSM>) hBaseDao.getJdbcTemplate().query(querySql.toString(), new ScoredPSMRowMapper(resultType));
        return scoredPSMs;
    }

    protected List<ScoredPSM> findScoredPSMsByAcceptance(String psmTableName,
                                                         String resultType,
                                                         HashMap<String, Double> scRangeHash, String confidenceScoreType,
                                                         Boolean hasAccept, String defaultAcceptType, Boolean hasRejected)
    {
            StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName);
            StringBuffer conditionsStr = new StringBuffer(" ");
            List<Integer> conditions = new ArrayList<>();
            if (hasAccept) conditions.add(1);
            if (hasRejected) conditions.add(-1);
            if (defaultAcceptType.equalsIgnoreCase("Accept") || defaultAcceptType.equalsIgnoreCase("Reject")) conditions.add(0);

            if (conditions.size() == 0) {
                System.out.println("Errors in the accept conditions, please have a type at least.");
                return null;
            } else if (conditions.size() != 3) {//not all accpetance be selected
                String listString = conditions.toString();
                listString = listString.substring(1, listString.length() - 1);
                conditionsStr.append("WHERE  ACCEPTANCE IN(" + listString + ") AND ");
            } else if (conditions.size() == 3) {
                conditionsStr.append("WHERE ");
            }
            conditionsStr.append(confidenceScoreType + " >= " + scRangeHash.get("start"));
            conditionsStr.append(" AND " + confidenceScoreType + " <= " + scRangeHash.get("end"));

            querySql.append(conditionsStr);
//            System.out.println("going to export psms by " + querySql.toString());
//            List<ScoredPSM> scoredPSMs = scoredPSMService.findScoredPSMs(querySql.toString(), psmType);


        List<ScoredPSM> scoredPSMs = (List<ScoredPSM>) hBaseDao.getJdbcTemplate().query(querySql.toString(), new ScoredPSMRowMapper(resultType));
        return scoredPSMs;
    }


    /***
     * Dao??? the table name is wrong?
     * find PSM by title
     */
    public ScoredPSM findPSMByTitle(String projectId, Integer id) {
        //todo need to check on this method, what for???

        StringBuffer querySql = new StringBuffer("SELECT * FROM compare_5_clusters WHERE ");
        querySql.append("\"ROW\" = ? ");

        ScoredPSM scoredPSM = (ScoredPSM) hBaseDao.getJdbcTemplate().queryForObject(querySql.toString(), null, new RowMapper<ScoredPSM>() {
            @Override
            public ScoredPSM mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSM();
                return scoredPSM;
            }
        });
        return scoredPSM;

    }

    public Integer findTotalScoredPSM(String projectId, String type) {
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
        Integer totalElement = (Integer) hBaseDao.getJdbcTemplate().queryForObject(querySql.toString(), Integer.class);
        return totalElement;
    }

    public ResponseEntity<String> updateUserAcceptance(String projectId, String psmType, Map<Integer, Integer> acceptanceMap) {
        String psmTableName = "";
        if (psmType.equalsIgnoreCase("newid")) {
            psmTableName = Configure.NEW_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
        } else if (psmType.equalsIgnoreCase("negscore")){
            psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
        } else if (psmType.equalsIgnoreCase("posscore")) {
            psmTableName = Configure.POS_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
        } else {
            return new ResponseEntity<String>("Wrong psm type: " + psmType, HttpStatus.BAD_REQUEST);
        }

        Object[] acceptancePsmIds = acceptanceMap.keySet().toArray();

        StringBuffer updateSql = new StringBuffer("UPSERT INTO " + psmTableName + "(ROW_ID, ACCEPTANCE) VALUES (?, ?)");
        hBaseDao.getJdbcTemplate().batchUpdate(updateSql.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, (Integer) acceptancePsmIds[i]);
                ps.setInt(2, acceptanceMap.get((Integer) acceptancePsmIds[i]));
            }

            @Override
            public int getBatchSize() {
                return acceptancePsmIds.length;
            }
        });

        return new ResponseEntity(acceptanceMap, HttpStatus.OK);
    }


}


