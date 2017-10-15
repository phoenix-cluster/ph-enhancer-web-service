package org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa;

import org.ncpsb.phoenixcluster.enhancer.webservice.domain.Cluster;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */
public interface HBaseDao {


    public Cluster getCluster(String querySql, Object[] params, RowMapper<Cluster> mapper);

    public List query(String querySql,  Object[] params, RowMapper<String> mapper);

    public void update(String updateSql, Object[] params);

    public void batchUpdate(List<String> updateSQLs);

}
