package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Thresholds;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.VennData;
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


    public Integer totalSpectrumInCluster(String clusterID) {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK='" + clusterID + "'");
        Integer totalElement = (Integer) hBaseDao.queryTotal(querySql.toString());
        return totalElement;
    }

    public List<HistogramBin> getHistData(Integer binNum) {
        return null;
    }

    public VennData getVennData(String projectId) {
        StringBuffer vennSql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RECORD_TABLE + " WHERE ");
        vennSql.append("\"PROJECT_ID\" = '" + projectId + "'");
        VennData vennData = (VennData) hBaseDao.getVennData(vennSql.toString(), null, new RowMapper<VennData>() {
            @Override
            public VennData mapRow(ResultSet rs, int rowNum) throws SQLException {
                VennData vennData = new VennData(
                        rs.getString("PROJECT_ID"),
                        rs.getInt("PREPSM_NO"),
                        rs.getInt("PREPSM_NOT_MATCHED_NO"),
                        rs.getInt("PREPSM_HIGH_CONF_NO"),
                        rs.getInt("PREPSM_LOW_CONF_NO"),
                        rs.getInt("BETTER_PSM_NO"),
                        rs.getInt("NEW_PSM_NO"),
                        rs.getInt("MATCHED_SPEC_NO"),
                        rs.getInt("MATCHED_ID_SPEC_NO")
                );
                return vennData;
            }
        });
        return vennData;

    }

    public Thresholds getThresholds(String projectId) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RECORD_TABLE + " WHERE ");
        querySql.append("\"PROJECT_ID\" = '" + projectId + "'");
        Thresholds thresholds= (Thresholds) hBaseDao.getThresholds(querySql.toString(), null, new RowMapper<Thresholds>() {
            @Override
            public Thresholds mapRow(ResultSet rs, int rowNum) throws SQLException {
                Thresholds thresholds = new Thresholds(
                        rs.getString("PROJECT_ID"),
                        rs.getInt("CLUSTER_SIZE_THRESHOLD"),
                        rs.getFloat("CLUSTER_RATIO_THRESHOLD"),
                        rs.getFloat("CONF_SC_THRESHOLD"),
                        rs.getFloat("SPECTRAST_FVAL_THRESHOLD")
                );
                return thresholds;
            }
        });
        return thresholds;

    }

    public List<String> getProjects() {
        StringBuffer querySql = new StringBuffer("SELECT PROJECT_ID FROM  " + Configure.PROJECT_RECORD_TABLE);
        List<String> projects = (List<String>)hBaseDao.query(querySql.toString(), null, new RowMapper() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                String project = rs.getString("PROJECT_ID");
                return project;
            }
        });
        return projects;
    }

    public List<VennData> getVennDataList() {
        String vennSql = new String("SELECT * FROM  " + Configure.PROJECT_RECORD_TABLE);
        List<VennData> vennDataList = (List<VennData>) hBaseDao.getVennDataList(vennSql, null, new RowMapper<VennData>() {
            @Override
            public VennData mapRow(ResultSet rs, int rowNum) throws SQLException {
                VennData vennData = new VennData(
                        rs.getString("PROJECT_ID"),
                        rs.getInt("PREPSM_NO"),
                        rs.getInt("PREPSM_NOT_MATCHED_NO"),
                        rs.getInt("PREPSM_HIGH_CONF_NO"),
                        rs.getInt("PREPSM_LOW_CONF_NO"),
                        rs.getInt("BETTER_PSM_NO"),
                        rs.getInt("NEW_PSM_NO"),
                        rs.getInt("MATCHED_SPEC_NO"),
                        rs.getInt("MATCHED_ID_SPEC_NO")
                );
                return vennData;
            }
        });
        return vennDataList;

    }
}


