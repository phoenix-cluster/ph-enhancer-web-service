package org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Created by baimi on 2017/10/11.
 */
@Repository
public class HBaseDaoImpl implements HBaseDao {

    private JdbcTemplate jdbcTemplate;

    public HBaseDaoImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    public HBaseDaoImpl() {
        super();
    }

    @Override
    public Object query(String querySql, Object[] params, RowMapper<Object> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.query(querySql, params, mapper);
        } else {
            return jdbcTemplate.query(querySql, mapper);
        }
    }

    @Override
    public int queryTotal(String querySql) {
        return jdbcTemplate.queryForObject(querySql, null, Integer.class);
    }
    @Override
    public List getScoredPSMs(String querySql, Object[] params, RowMapper<ScoredPSM> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.query(querySql, params, mapper);
        } else {
            return jdbcTemplate.query(querySql, mapper);
        }
    }

    public ScoredPSM getScoredPSM(String querySql, Object[] params, RowMapper<ScoredPSM> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.queryForObject(querySql, params, mapper);
        } else {
            return jdbcTemplate.queryForObject(querySql, mapper);
        }
    }

    @Override
    public List getSpectraInCluster(String querySql, Object[] params, RowMapper<SpectrumInCluster> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.query(querySql, params, mapper);
        } else {
            return jdbcTemplate.query(querySql, mapper);
        }
    }

    @Override
    public List getSpectra(String querySql, Object[] params, RowMapper<Spectrum> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.query(querySql, params, mapper);
        } else {
            return jdbcTemplate.query(querySql, mapper);
        }
    }

    @Override
    public List getVennDataList(String querySql, Object[] params, RowMapper<VennData> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.query(querySql, params, mapper);
        } else {
            return jdbcTemplate.query(querySql, mapper);
        }
    }

    @Override
    public SpectrumInCluster getSpectrumInCluster(String querySql, Object[] params, RowMapper<SpectrumInCluster> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.queryForObject(querySql, params, mapper);
        } else {
            return jdbcTemplate.queryForObject(querySql, mapper);
        }
    }

    @Override
    public Spectrum getSpectrum(String querySql, Object[] params, RowMapper<Spectrum> mapper) {
        if (params != null && params.length > 0) {
            return jdbcTemplate.queryForObject(querySql, params, mapper);
        } else {
            return jdbcTemplate.queryForObject(querySql, mapper);
        }
    }

    @Override
    public List<Map> queryMap(String querySql, RowMapper<Map> rowMapper) {
            return jdbcTemplate.query(querySql, rowMapper);
    }

    @Override
    public List<Object> queryList(String querySql, RowMapper rowMapper) {
        return jdbcTemplate.query(querySql, rowMapper);
    }

    @Override
    public Object getVennData(String querySql, Object[] params, RowMapper<VennData> rowMapper) {
        if (params != null && params.length > 0) {
            return (VennData) jdbcTemplate.queryForObject(querySql, params, rowMapper);
        } else {
            return (VennData) jdbcTemplate.queryForObject(querySql, rowMapper);
        }
    }

    @Override
    public Object getThresholds(String querySql, Object[] params, RowMapper<Thresholds> rowMapper) {
        if (params != null && params.length > 0) {
            return (Thresholds) jdbcTemplate.queryForObject(querySql, params, rowMapper);
        } else {
            return (Thresholds) jdbcTemplate.queryForObject(querySql, rowMapper);
        }
    }



    @Override
    public void update(String updateSQL, Object[] params) {
        // System.out.println("##########UPDATE SQL:" + updateSQL);
        jdbcTemplate.update(updateSQL, params);
    }

    @Override
    public void batchUpdate(List<String> updateSQL) {
        for (String sql : updateSQL) {
            // System.out.println("##########UPDATE SQL:" + sql);
            jdbcTemplate.update(sql);
        }
    }

//    @Override
//    public List<AgentInfo> queryAgentList(String querySql, Object[] params, RowMapper<AgentInfo> mapper) {
//        if (params != null && params.length > 0) {
//            return jdbcTemplate.query(querySql, params, mapper);
//        } else {
//            return jdbcTemplate.query(querySql, mapper);
//        }
//    }

    @Override
    public Cluster getCluster(String querySql, Object[] params, RowMapper<Cluster> mapper) {
       if (params != null && params.length > 0) {
            return (Cluster) jdbcTemplate.queryForObject(querySql, params, mapper);
        } else {
            return (Cluster) jdbcTemplate.queryForObject(querySql, mapper);
        }
    }

//    @Override
//    public FlumeInfo getFlumeInfo(String querySql, Object[] params, RowMapper<FlumeInfo> mapper) {
//        if (params != null && params.length > 0) {
//            return jdbcTemplate.queryForObject(querySql, params, mapper);
//        } else {
//            return jdbcTemplate.queryForObject(querySql, mapper);
//        }
//    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
