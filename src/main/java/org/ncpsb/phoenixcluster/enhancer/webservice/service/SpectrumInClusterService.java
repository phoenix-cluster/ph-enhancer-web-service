package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumInCluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumInClusterRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class SpectrumInClusterService {
    private String clusterTableName = "V_CLUSTER";
    private String spectrumTableName = "V_CLUSTER_SPEC";

    @Autowired
    private HBaseDao hBaseDao;


//    public List<Spectrum> getSpectraInCluster(Integer page, Integer size, String clusterID) {
//
//        StringBuffer querySql = new StringBuffer("SELECT * FROM " + spectrumTableName);
//        querySql.append(" LIMIT " + size);
//        querySql.append(" OFFSET " + (page - 1) * size);
//
//        System.out.println("Going to execute: " + querySql);
//        List<Spectrum> scoredPSMs = (List<Spectrum>) hBaseDao.getSpectraInCluster(querySql.toString(), null, new RowMapper<Spectrum>() {
//            @Override
//            public Spectrum mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Spectrum spectrum = new Spectrum();
//                spectrum.setTitle(rs.getString(""));
//                spectrum.setClusterId(rs.getString("CLUSTER_FK"));
//                spectrum.setPrecursorMz(rs.getFloat(""));
//                spectrum.setCharge(rs.getInt(""));
//                spectrum.setSequence(rs.getString(""));
//
//                return spectrum;
//            }
//        });
//        return (scoredPSMs != null && scoredPSMs.size() > 0) ? (List) scoredPSMs : null;
//    }

    /***
     * Dao
     * @param title
     * @return
     */
    public SpectrumInCluster findSpectrumInClusterByTitle(String title) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM \"" + spectrumTableName + "\" WHERE ");
        title = title.trim();
        querySql.append("SPEC_TITLE = ? ");
        System.out.println(querySql);
        SpectrumInCluster scoredPSM = (SpectrumInCluster) hBaseDao.getJdbcTemplate().queryForObject(querySql.toString(),
                new Object[] {title}, new SpectrumInClusterRowMapper());
        return scoredPSM;
    }

    /***
     * Dao
     * @param clusterID
     * @return
     */
    public Integer findTotalSpectrumInCluster(String clusterID) {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK= ?");
        Integer totalElement = (Integer) hBaseDao.getJdbcTemplate().queryForObject(querySql.toString(), new Object[]{clusterID}, Integer.class);
        return totalElement;
    }

    /***
     * Dao
     * @param titlesStr
     * @return
     */
    public List<SpectrumInCluster> findSpectraInClusterByTitles(String titlesStr) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM \"" + spectrumTableName + "\" WHERE SPEC_TITLE in (");
        String[] titles = titlesStr.split("\\|\\|");
        for (String title : titles){
            title = title.trim();
            querySql.append("'" + title + "',");
        }
        querySql.setLength(querySql.length() - 1);
        querySql.append(")");
        System.out.println("Going to execute: " + querySql);
        List<SpectrumInCluster> spectra = (List<SpectrumInCluster>) hBaseDao.getJdbcTemplate().query(querySql.toString(),
                                     new SpectrumInClusterRowMapper());
        return (spectra != null && spectra.size() > 0) ? (List) spectra: null;
    }
}


