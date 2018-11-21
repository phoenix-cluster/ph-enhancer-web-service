package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class StatisticsDaoMysqlImpl {
    private String spectrumTableName = "V_CLUSTER_SPEC";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Integer findTotalSpectrumInCluster(String clusterID) {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK= ? ");
        Integer totalElement = (Integer) jdbcTemplate.queryForObject(querySql.toString(), new Object[]{clusterID}, Integer.class);
        return totalElement;
    }


    public VennData findVennDataByProjectId(String projectId) {
        StringBuffer vennSql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE + " WHERE ");
        vennSql.append("PROJECT_ID = ? ");
        System.out.println(vennSql.toString() + projectId);
        VennData vennData = (VennData) jdbcTemplate.queryForObject(vennSql.toString(), new Object[]{projectId}, new VennDataRowMapper());
        return vennData;
    }

    public Thresholds findThresholdsByProjectId(String projectId) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE + " WHERE ");
        querySql.append("PROJECT_ID = ? ");
        Thresholds thresholds= (Thresholds) jdbcTemplate.queryForObject(querySql.toString(), new Object[]{projectId}, new ThresholdsRowMapper());
        return thresholds;
    }

    public List<String> findProjects() {
        StringBuffer querySql = new StringBuffer("SELECT PROJECT_ID FROM  " + Configure.PROJECT_RESULT_TABLE);
        List<String> projects = (List<String>)jdbcTemplate.query(querySql.toString(), (rs, rowNum)->rs.getString("PROJECT_ID"));
        return projects;
    }

    public List<VennData> getVennDataList() {
        String vennSql = new String("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE);
        List<VennData> vennDataList = (List<VennData>) jdbcTemplate.query(vennSql, new VennDataRowMapper());
        return vennDataList;
    }

}
