package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Cluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ClusterRowMapper;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ClusterService {
    @Autowired
    private HBaseDao hBaseDao;
    private String clusterTableName ="V_CLUSTER";

    /***
     * find a cluster by clusterId
     * @param clusterId
     * @return
     */
    public Cluster findByClusterId(String clusterId) {
//        StringBuffer clusterSql = new StringBuffer("SELECT * FROM compare_5_clusters");
//        clusterSql.append("\"ROW\" = '" + clusterId + "'");
        StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + clusterTableName + " WHERE ");
        clusterSql.append("\"CLUSTER_ID\" = ? ");

        Cluster cluster = (Cluster) hBaseDao.getJdbcTemplate().queryForObject(clusterSql.toString(), new Object[] {clusterId},new ClusterRowMapper());
        return cluster;
//        return (clusters != null && clusters.size() > 0) ? clusters : null;
    }

    /***
     * find All clusterIds, with sorting and pagination
     * @param page
     * @param size
     * @param sortField
     * @param sortDirection
     * @return
     */
    public List<String> findClusterIds(Integer page, Integer size, String sortField, String sortDirection) {
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

        List<String> clusterIds = (List<String>) hBaseDao.getJdbcTemplate().query(clusterSql.toString(), (rs, rowNum) ->
            new String(rs.getString("CLUSTER_Id")));

        return (clusterIds != null && clusterIds.size() > 0) ? (List) clusterIds : null;
    }
}


