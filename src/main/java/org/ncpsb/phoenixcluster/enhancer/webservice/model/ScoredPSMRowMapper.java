package org.ncpsb.phoenixcluster.enhancer.webservice.model;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoredPSMRowMapper implements  RowMapper{
    private String resultType;

    public ScoredPSMRowMapper(String resultType) {
        this.resultType = resultType;
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSMForWeb();
                scoredPSM.setId(rs.getInt("ROW_ID"));
                scoredPSM.setClusterId(rs.getString("CLUSTER_ID"));
                scoredPSM.setClusterRatio(rs.getFloat("CLUSTER_RATIO"));
                scoredPSM.setClusterSize(rs.getInt("CLUSTER_SIZE"));

                if (resultType == "newid" || resultType == "negscore") {
                    scoredPSM.setRecommendPeptide(rs.getString("RECOMM_SEQ"));
                    scoredPSM.setRecommConfidentScore(rs.getFloat("RECOMM_SEQ_SC"));
                    scoredPSM.setRecommendPepModsStr(rs.getString("RECOMM_MODS"));
                }
                if (resultType == "posscore" || resultType == "negscore") {
                    scoredPSM.setPeptideSequence(rs.getString("PRE_SEQ"));
                    scoredPSM.setPeptideModsStr(rs.getString("PRE_MODS"));
                    scoredPSM.setConfidentScore(rs.getFloat("CONF_SC"));
                }

                scoredPSM.setSpectraNum(rs.getInt("NUM_SPEC"));
                scoredPSM.setClusterRatioStr(rs.getString("CLUSTER_RATIO_STR"));
                scoredPSM.setSpectraTitles(ClusterUtils.getStringListFromString(rs.getString("SPECTRA"),"\\|\\|"));
                scoredPSM.setAcceptance(rs.getInt("ACCEPTANCE"));

                return scoredPSM;

    }
}
