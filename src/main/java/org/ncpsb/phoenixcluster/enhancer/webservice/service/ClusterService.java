package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.Cluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ClusterService {
    @Autowired
    private HBaseDao hBaseDao;
    private String clusterTableName ="V_CLUSTER";


    public Cluster getClusterById(String clusterId) {
//        StringBuffer clusterSql = new StringBuffer("SELECT * FROM compare_5_clusters");
//        clusterSql.append("\"ROW\" = '" + clusterId + "'");
        StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + clusterTableName + " tb WHERE ");
        clusterSql.append("\"CLUSTER_ID\" = '" + clusterId + "'");

        Cluster cluster = (Cluster) hBaseDao.getCluster(clusterSql.toString(), null, new RowMapper<Cluster>() {
            @Override
            public Cluster mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cluster cluster = new Cluster();
                cluster.setId(rs.getString("CLUSTER_Id"));
                cluster.setSpecCount(rs.getInt("N_SPEC"));
                cluster.setRatio(rs.getFloat("CLUSTER_RATIO"));
                cluster.setSpectraTitles(ClusterUtils.getStringListFromString(rs.getString("SPECTRA_TITLES"),"\\|\\|"));
                cluster.setConsensusMz(ClusterUtils.getFloatListFromString(rs.getString("CONSENSUS_MZ"), ","));
                cluster.setConsensusIntens(ClusterUtils.getFloatListFromString(rs.getString("CONSENSUS_INTENS"), ","));
                return cluster;
            }
        });
        return cluster;
//        return (clusters != null && clusters.size() > 0) ? clusters : null;
    }

    public List<String> getClusterIds(Integer page, Integer size, String sortField, String sortDirection) {
        if (sortField != null) {
            sortField = sortField.toUpperCase();
        }
        if (sortDirection == null) {
            sortDirection = "ASC";
        }
        StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + clusterTableName + " ");
        if (sortField == "CONF_SC" || sortField == "CLUSTER_RATIO" || sortField == "CLUSTER_SIZE") {
            clusterSql.append(" ORDER BY " + sortField + " " + sortDirection + " ");
        }
        clusterSql.append(" LIMIT " + size);
        clusterSql.append(" OFFSET " + (page - 1) * size);

        List<String> clusterIds = (List<String>) hBaseDao.query(clusterSql.toString(), null, new RowMapper<Object>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String clusterId = rs.getString("CLUSTER_Id");
                return clusterId;
            }
        });
        for (String clusterId : clusterIds) {
            System.out.println(":" + clusterId);
        }
        return (clusterIds != null && clusterIds.size() > 0) ? (List) clusterIds : null;
    }
}


