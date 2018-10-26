package org.ncpsb.phoenixcluster.enhancer.webservice.model;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClusterRowMapper implements  RowMapper{
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cluster cluster = new Cluster();
                cluster.setId(rs.getString("CLUSTER_Id"));
                cluster.setSpecCount(rs.getInt("N_SPEC"));
                cluster.setRatio(rs.getFloat("CLUSTER_RATIO"));
                cluster.setSpectraTitles(ClusterUtils.getStringListFromString(rs.getString("SPECTRA_TITLES"),"\\|\\|"));
                cluster.setConsensusMz(ClusterUtils.getFloatListFromString(rs.getString("CONSENSUS_MZ"), ","));
                cluster.setConsensusIntens(ClusterUtils.getFloatListFromString(rs.getString("CONSENSUS_INTENS"), ","));
                cluster.setSequencesRatios(rs.getString("SEQUENCES_RATIOS"));
                return cluster;
    }
}
