package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSMRowMapper;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.TaxonomyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScoredPSMDaoMysqlImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Double> findRecommConfidentScore(String psmTableName){
        StringBuffer querySql = new StringBuffer("SELECT RECOMM_SEQ_SC FROM " + psmTableName);
        System.out.println(querySql);
        List<Double> scoreList = (List<Double>) jdbcTemplate.query(querySql.toString(), (rs, rowNum) -> rs.getDouble("RECOMM_SEQ_SC"));
        return scoreList;
    }

    public List<Double> findConfidentScore(String psmTableName) {
        StringBuffer querySql = new StringBuffer("SELECT CONF_SC FROM " + psmTableName);
        System.out.println(querySql);
        List<Double> scoreList = (List<Double>) jdbcTemplate.query(querySql.toString(), (rs, rowNum) -> rs.getDouble("CONF_SC"));
        return scoreList;
    }

    public List<Double> findClusterRatio(String psmTableName) {
        StringBuffer querySql = new StringBuffer("SELECT CLUSTER_RATIO FROM " + psmTableName);
        System.out.println(querySql);
        List<Double> scoreList = (List<Double>) jdbcTemplate.query(querySql.toString(), (rs, rowNum) -> rs.getDouble("CLUSTER_RATIO"));
        return scoreList;
    }

    public List<Integer> findClusterSize(String psmTableName) {
        StringBuffer querySql = new StringBuffer("SELECT CLUSTER_SIZE FROM " + psmTableName);
        System.out.println(querySql);
        List<Integer> scoreList = (List<Integer>) jdbcTemplate.query(querySql.toString(), (rs, rowNum) -> rs.getInt("CLUSTER_SIZE"));
        return scoreList;
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
        public List<ScoredPSM> findScoredPSMs(String psmTableName, Integer page, Integer size, String sortField, String sortDirection, String filterByTaxonomyId, String resultType) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName);
        if (!filterByTaxonomyId.equalsIgnoreCase("ALL")){
                querySql.append(" WHERE seq_taxids like '%" + filterByTaxonomyId + "%'");
            }
            System.out.println("sortField:" + sortField);
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

        List<ScoredPSM> scoredPSMs = (List<ScoredPSM>) jdbcTemplate.query(querySql.toString(), new ScoredPSMRowMapper(resultType));
        return scoredPSMs;
    }

    public List<ScoredPSM> findScoredPSMsByAcceptance(String psmTableName,
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


        List<ScoredPSM> scoredPSMs = (List<ScoredPSM>) jdbcTemplate.query(querySql.toString(), new ScoredPSMRowMapper(resultType));
        return scoredPSMs;
    }


    /***
     * Dao??? the table name is wrong?
     * find PSM by title
     */
    public ScoredPSM findPSMByTitle(String projectId, Integer id) {
        //todo need to check on this method, what for???

        StringBuffer querySql = new StringBuffer("SELECT * FROM compare_5_clusters WHERE ");
        querySql.append("ROW = ? ");

        ScoredPSM scoredPSM = (ScoredPSM) jdbcTemplate.queryForObject(querySql.toString(), null, new RowMapper<ScoredPSM>() {
            @Override
            public ScoredPSM mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSM();
                return scoredPSM;
            }
        });
        return scoredPSM;

    }

    public Integer findTotalScoredPSM(String projectId, String filterByTaxonomyId, String type) {
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

        if (!filterByTaxonomyId.equalsIgnoreCase("ALL")){
            querySql.append(" WHERE seq_taxids like '%" + filterByTaxonomyId + "%'");
        }

        Integer totalElement = (Integer) jdbcTemplate.queryForObject(querySql.toString(), Integer.class);
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

        StringBuffer updateSql = new StringBuffer("UPDATE " + psmTableName + " SET ACCEPTANCE = ? WHERE ROW_ID = ?");
        jdbcTemplate.batchUpdate(updateSql.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, acceptanceMap.get((Integer) acceptancePsmIds[i]));
                ps.setInt(2, (Integer) acceptancePsmIds[i]);
            }

            @Override
            public int getBatchSize() {
                return acceptancePsmIds.length;
            }
        });

        return new ResponseEntity(acceptanceMap, HttpStatus.OK);
    }

}
