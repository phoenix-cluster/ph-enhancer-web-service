package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
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
public class StatisticsService {
    private String spectrumTableName = "V_CLUSTER_SPEC";

    @Autowired
    private HBaseDao hBaseDao;


    public Integer findTotalSpectrumInCluster(String clusterID) {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK= ? ");
        Integer totalElement = (Integer) hBaseDao.getJdbcTemplate().queryForObject(querySql.toString(), new Object[]{clusterID}, Integer.class);
        return totalElement;
    }

    public List<HistogramBin> getHistData(Integer binNum) {
        return null;
    }

    public VennData findVennData(String projectId) {
        StringBuffer vennSql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RECORD_TABLE + " WHERE ");
        vennSql.append("\"PROJECT_ID\" = ? ");
        VennData vennData = (VennData) hBaseDao.getJdbcTemplate().queryForObject(vennSql.toString(), new Object[]{projectId}, new VennDataRowMapper());
        return vennData;
    }

    public Thresholds findThresholdsByProjectId(String projectId) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RECORD_TABLE + " WHERE ");
        querySql.append("\"PROJECT_ID\" = ? ");
        Thresholds thresholds= (Thresholds) hBaseDao.getJdbcTemplate().queryForObject(querySql.toString(), new Object[]{projectId}, new ThresholdsRowMapper());
        return thresholds;
    }

    public List<String> findProjects() {
        StringBuffer querySql = new StringBuffer("SELECT PROJECT_ID FROM  " + Configure.PROJECT_RECORD_TABLE);
        List<String> projects = (List<String>)hBaseDao.getJdbcTemplate().query(querySql.toString(), (rs, rowNum)->rs.getString("PROJECT_ID"));
        return projects;
    }

    public List<VennData> getVennDataList() {
        String vennSql = new String("SELECT * FROM  " + Configure.PROJECT_RECORD_TABLE);
        List<VennData> vennDataList = (List<VennData>) hBaseDao.getJdbcTemplate().query(vennSql, new VennDataRowMapper());
        return vennDataList;

    }
}


