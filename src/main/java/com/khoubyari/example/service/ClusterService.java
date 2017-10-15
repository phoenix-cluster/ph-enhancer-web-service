package com.khoubyari.example.service;


import com.khoubyari.example.dao.jpa.HBaseDao;
import com.khoubyari.example.domain.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClusterService {
    @Autowired
    private HBaseDao hBaseDao;

    public Cluster getClusterByID(String clusterID, Map<String, String> map) {
//        StringBuffer clusterSql = new StringBuffer("SELECT * FROM compare_5_clusters");
//        clusterSql.append("\"ROW\" = '" + clusterID + "'");
        StringBuffer clusterSql = new StringBuffer("SELECT * FROM compare_5_clusters WHERE ");
        clusterSql.append("\"ROW\" = '" + clusterID + "'");

        Cluster cluster = (Cluster) hBaseDao.getCluster(clusterSql.toString(), null, new RowMapper<Cluster>() {
            @Override
            public Cluster mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cluster cluster = new Cluster();
                cluster.setId(rs.getString("row"));
                cluster.setAvPrecursorMz(rs.getString("apm"));
                cluster.setAvPrecursorIntens(rs.getString("api"));
                cluster.setSpecCount(rs.getString("sc"));
                cluster.setRatio(rs.getString("r"));
                cluster.setMzValues(rs.getString("conm"));
                cluster.setIntensValues(rs.getString("coni"));
                return cluster;
            }
        });
        return cluster;
//        return (clusters != null && clusters.size() > 0) ? clusters : null;
    }
    public List<String> getAllClusterIDs(Integer page, Integer size, String sortField, String sortDirection) {
        StringBuffer clusterSql = new StringBuffer("SELECT * FROM compare_5_clusters tb ");
        clusterSql.append(" LIMIT " + size);
        clusterSql.append(" OFFSET " + page * size);

        List <String> clusterIDs = (List <String>) hBaseDao.query(clusterSql.toString(), null, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String clusterId = rs.getString("row");
                return clusterId;
            }
        });
        for (String clusterID : clusterIDs) {
            System.out.println(":" + clusterID);
        }
        return (clusterIDs != null && clusterIDs.size() > 0) ? (List) clusterIDs : null;
    }
}


