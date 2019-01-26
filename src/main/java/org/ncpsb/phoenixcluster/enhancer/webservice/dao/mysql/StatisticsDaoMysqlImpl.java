package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
        try {
            Integer totalElement = (Integer) jdbcTemplate.queryForObject(querySql.toString(), new Object[]{clusterID}, Integer.class);
            return totalElement;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }


    public VennData findVennDataByProjectId(String projectId) {
        StringBuffer vennSql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE + " WHERE ");
        vennSql.append("PROJECT_ID = ? ");
        System.out.println(vennSql.toString() + projectId);
        try {
            VennData vennData = (VennData) jdbcTemplate.queryForObject(vennSql.toString(), new Object[]{projectId}, new VennDataRowMapper());
            return vennData;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public Thresholds findThresholdsByProjectId(String projectId) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE + " WHERE ");
        querySql.append("PROJECT_ID = ? ");
        try {
            Thresholds thresholds= (Thresholds) jdbcTemplate.queryForObject(querySql.toString(), new Object[]{projectId}, new ThresholdsRowMapper());
            return thresholds;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<String> findProjects() {
        StringBuffer querySql = new StringBuffer("SELECT PROJECT_ID FROM  " + Configure.PROJECT_RESULT_TABLE);
        try {
            List<String> projects = (List<String>)jdbcTemplate.query(querySql.toString(), (rs, rowNum)->rs.getString("PROJECT_ID"));
            return projects;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<VennData> getVennDataList() {
        String vennSql = new String("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE);
        try {
            List<VennData> vennDataList = (List<VennData>) jdbcTemplate.query(vennSql, new VennDataRowMapper());
            return vennDataList;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

}
