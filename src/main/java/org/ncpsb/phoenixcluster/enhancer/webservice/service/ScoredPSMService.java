package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.apache.avro.generic.GenericData;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.ScoredPSMForWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ScoredPSMService {
    private String posScoredPSMTableName = "V_PXD000021_P_SCORED_PSM";
    private String negScoredPSMTableName = "V_PXD000021_N_SCORED_PSM";
    private String recommIdPSMTableName = "V_PXD000021_RECOMM_ID";
    private String clusterTableName = "V_CLUSTER";
    private String spectrumTableName = "V_CLUSTER_SPEC";

    HashMap<String, String> columnMap = new HashMap<String, String>() {{
        put("confidentScore", "CONF_SC");
        put("clusterRatio", "CLUSTER_RATIO");
        put("clusterSize", "CLUSTER_SIZE");
    }};

    @Autowired
    private HBaseDao hBaseDao;


    public List<ScoredPSMForWeb> getScoredPSMsForWeb(Integer page, Integer size, String sortField, String sortDirection, String resultType) {
        String psmTableName = "";
        switch (resultType) {
            case ("negscore"): {
                psmTableName = negScoredPSMTableName;
                if (sortField.equals("confidentScore") && sortDirection == null) {
                    sortDirection = "ASC";
                }
                break;
            }
            case ("posscore"): {
                psmTableName = posScoredPSMTableName;
                if (sortField.equals("confidentScore") && sortDirection == null) {
                    sortDirection = "DESC";
                }
                break;
            }
            case ("recomm"): {
                psmTableName = recommIdPSMTableName;
                if (sortField.equals("confidentScore") && sortDirection == null) {
                    sortDirection = "DESC";
                }
                break;
            }
            default: {
                psmTableName = negScoredPSMTableName;
                if (sortField.equals("confidentScore") && sortDirection == null) {
                    sortDirection = "ASC";
                }
            }
        }
        if (sortDirection == null) {
            sortDirection = "DESC";
        }

        StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName);
        if (sortField.equals("confidentScore") ||
                sortField.equals("clusterRatio") ||
                sortField.equals("clusterSize")
                ) {
            querySql.append(" ORDER BY " + columnMap.get(sortField) + " " + sortDirection + " ");
        }
        querySql.append(" LIMIT " + size);
        querySql.append(" OFFSET " + (page - 1) * size);

        System.out.println("Going to execute: " + querySql);

        List<ScoredPSM> scoredPSMs = getScoredPSMs(querySql.toString(), resultType);
        List<ScoredPSMForWeb> scoredPSMsForWeb = new ArrayList<>();
        for (ScoredPSM scoredPSM : scoredPSMs) {
            ScoredPSMForWeb scoredPSMForWeb = (ScoredPSMForWeb) scoredPSM;
            scoredPSMForWeb.setPTMs();
            scoredPSMsForWeb.add(scoredPSMForWeb);
        }

        return (scoredPSMsForWeb != null && scoredPSMsForWeb.size() > 0) ? (List) scoredPSMsForWeb : null;
    }

    private List<ScoredPSM> getScoredPSMs(String querySql, String resultType) {
        List<ScoredPSM> scoredPSMs = (List<ScoredPSM>) hBaseDao.getScoredPSMs(querySql.toString(), null, new RowMapper<ScoredPSM>() {
            @Override
            public ScoredPSM mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSMForWeb();
                scoredPSM.setId(rs.getInt("ID"));
                if (resultType != "recomm") {
                    scoredPSM.setPeptideSequence(rs.getString("PEP_SEQ"));
                    scoredPSM.setPeptideModsStr(rs.getString("PEP_MODS"));
                }
                scoredPSM.setClusterId(rs.getString("CLUSTER_ID"));
                scoredPSM.setClusterRatio(rs.getFloat("CLUSTER_RATIO"));
                scoredPSM.setClusterSize(rs.getInt("CLUSTER_SIZE"));

                scoredPSM.setConfidentScore(rs.getFloat("CONF_SC"));
                scoredPSM.setRecommendPeptide(rs.getString("RECOMMEND_PEP"));
                scoredPSM.setRecommendPepModsStr(rs.getString("RECOMMEND_MODS"));

                scoredPSM.setSpectraNum(rs.getInt("NUM_SPEC"));
                scoredPSM.setSpectraTitles(rs.getString("SPECTRA"));

                return scoredPSM;
            }
        });
        return scoredPSMs;
    }

    public ScoredPSM getPSMByTitle(String title, Object o) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM compare_5_clusters WHERE ");
        querySql.append("\"ROW\" = '" + title + "'");

        ScoredPSM scoredPSM = (ScoredPSM) hBaseDao.getScoredPSM(querySql.toString(), null, new RowMapper<ScoredPSM>() {
            @Override
            public ScoredPSM mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScoredPSM scoredPSM = new ScoredPSM();
                return scoredPSM;
            }
        });
        return scoredPSM;

    }

    public Integer totalScoredPSM(String type) {
        String psmTableName = "";
        switch (type) {
            case ("negscore"): {
                psmTableName = negScoredPSMTableName;
                break;
            }
            case ("posscore"): {
                psmTableName = posScoredPSMTableName;
                break;
            }
            case ("recomm"): {
                psmTableName = recommIdPSMTableName;
                break;
            }
            default: {
                psmTableName = negScoredPSMTableName;
            }
        }

        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + psmTableName);
        Integer totalElement = (Integer) hBaseDao.queryTotal(querySql.toString());
        return totalElement;
    }
}


