package org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by baimi on 2017/10/11.
 */
public interface HBaseDao {


    public int queryTotal(String querySql);

    public Cluster getCluster(String querySql, Object[] params, RowMapper<Cluster> mapper);

    public Object query(String querySql,  Object[] params, RowMapper<Object> mapper);

    Object getVennData(String querySql, Object[] params, RowMapper<VennData> rowMapper);

    Object getThresholds(String querySql, Object[] params, RowMapper<Thresholds> rowMapper);

    public void update(String updateSql, Object[] params);
    public void batchUpdate(List<String> updateSQLs);

    public List<Map> queryMap(String querySql, RowMapper<Map> rowMapper) ;
    public List getScoredPSMs(String querySql, Object[] params, RowMapper<ScoredPSM> mapper);
    public ScoredPSM getScoredPSM(String querySql, Object[] params, RowMapper<ScoredPSM> mapper);

    public List getSpectraInCluster(String querySql, Object[] params, RowMapper<SpectrumInCluster> mapper);
    public List getSpectra(String querySql, Object[] params, RowMapper<Spectrum> mapper);
    public List getVennDataList(String querySql, Object[] params, RowMapper<VennData> mapper);
    public SpectrumInCluster getSpectrumInCluster(String querySql, Object[] params, RowMapper<SpectrumInCluster> mapper);
    public Spectrum getSpectrum(String querySql, Object[] params, RowMapper<Spectrum> mapper);
    public String getSpecPeptideSeq(String querySql);
    List<Object> queryList(String s, RowMapper rowMapper);

//    List<Map> queryList(String s, RowMapper<Map> rowMapper);
}
