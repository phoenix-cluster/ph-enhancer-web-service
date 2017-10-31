package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.ScoredPSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ScoredPSMService {
    private String scoredPSMTableName = "LIB_SEARCH_SR_PXD000021TEST_27102017";
    private String clusterTableName = "cluster_table_test_24102017";
    private String spectrumTableName = "cluster_table_test_24102017_spec";

    @Autowired
    private HBaseDao hBaseDao;


    public List<ScoredPSM> getScoredPSMs(Integer page, Integer size, String sortField, String sortDirection) {
        if (sortField != null && sortField != "") {
            sortField = sortField.toUpperCase();
        }
        if (sortDirection == null) {
            sortDirection = "ASC";
        }

        StringBuffer querySql = new StringBuffer("SELECT * FROM " + scoredPSMTableName);
        if (sortField == "CONF_SC" || sortField == "CLUSTER_RATIO" || sortField == "CLUSTER_SIZE") {
            querySql.append(" ORDER BY " + sortField + " " + sortDirection + " ");
        }
        querySql.append(" LIMIT " + size);
        querySql.append(" OFFSET " + (page - 1) * size);

        List<ScoredPSM> scoredPSMs = (List<ScoredPSM>) hBaseDao.getScoredPSMs(querySql.toString(), null, new RowMapper<ScoredPSM>() {
            @Override
            public ScoredPSM mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSM();
                scoredPSM.setQuerySpectrumTitle(rs.getString("SPEC_TITLE"));
                scoredPSM.setPreviousIdentification(rs.getString("PREV_ID_PEP"));

                scoredPSM.setClusterID(rs.getString("CLUSTER_ID"));
                scoredPSM.setClusterRatio(rs.getFloat("CLUSTER_RATIO"));
                scoredPSM.setClusterSize(rs.getInt("CLUSTER_SIZE"));

                scoredPSM.setConfidentiScore(rs.getFloat("CONF_SC"));
                scoredPSM.setRecommendIdentification(rs.getString("RECOMMEND_PEP"));

                return scoredPSM;
            }
        });
        return (scoredPSMs != null && scoredPSMs.size() > 0) ? (List) scoredPSMs : null;
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

    public Integer totalScoredPSM() {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + scoredPSMTableName);
        Integer totalElement = (Integer) hBaseDao.queryTotal(querySql.toString());
        return totalElement;
    }
}


