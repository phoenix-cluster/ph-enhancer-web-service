package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.Cluster;
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
    private String clusterTableName ="\"cluster_table_test_24102017\"";


    public Cluster getClusterByID(String clusterID, Map<String, String> map) {
//        StringBuffer clusterSql = new StringBuffer("SELECT * FROM compare_5_clusters");
//        clusterSql.append("\"ROW\" = '" + clusterID + "'");
        StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + clusterTableName + " tb WHERE ");
        clusterSql.append("\"CLUSTER_ID\" = '" + clusterID + "'");

        Cluster cluster = (Cluster) hBaseDao.getCluster(clusterSql.toString(), null, new RowMapper<Cluster>() {
            @Override
            public Cluster mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cluster cluster = new Cluster();
                cluster.setId(rs.getString("CLUSTER_ID"));
//                cluster.setAvPrecursorMz(rs.getFloat("apm"));
//                cluster.setAvPrecursorIntens(rs.getFloat("api"));
                cluster.setSpecCount(rs.getInt("N_SPEC"));
                cluster.setRatio(rs.getFloat("CLUSTER_RATIO"));
//                cluster.setMzValues(rs.get("conm"));
//                cluster.setIntensValues(rs.getString("coni"));
                return cluster;
            }
        });
        return cluster;
//        return (clusters != null && clusters.size() > 0) ? clusters : null;
    }

    public List<String> getClusterIDs(Integer page, Integer size, String sortField, String sortDirection) {
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

        List<String> clusterIDs = (List<String>) hBaseDao.query(clusterSql.toString(), null, new RowMapper<Object>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String clusterId = rs.getString("CLUSTER_ID");
                return clusterId;
            }
        });
        for (String clusterID : clusterIDs) {
            System.out.println(":" + clusterID);
        }
        return (clusterIDs != null && clusterIDs.size() > 0) ? (List) clusterIDs : null;
    }
}


