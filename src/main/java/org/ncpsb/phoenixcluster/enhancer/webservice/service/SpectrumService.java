package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.apache.hadoop.hbase.ClusterId;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.Spectrum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class SpectrumService {
    private String clusterTableName = "cluster_table_test_24102017";
    private String spectrumTableName = "cluster_table_test_24102017_spec";

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

    public Spectrum getSpectrumByTitle(String title) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM \"" + spectrumTableName + "\" WHERE ");
        title = title.trim();
        querySql.append("SPEC_TITLE = '" + title + "'");
        System.out.println(querySql);

        Spectrum scoredPSM = (Spectrum) hBaseDao.getSpectrum(querySql.toString(), null, new RowMapper<Spectrum>() {
            @Override
            public Spectrum mapRow(ResultSet rs, int rowNum) throws SQLException {
                Spectrum spectrum = new Spectrum();
                spectrum.setTitle(rs.getString("SPEC_TITLE"));
                spectrum.setCharge(rs.getInt("CHARGE"));
                spectrum.setPrecursorMz(rs.getFloat("PRECURSOR_MZ"));
                spectrum.setSequence(rs.getString("ID_SEQUENCES"));
                spectrum.setSpecies(rs.getString("TAXIDS"));
                return spectrum;
            }
        });
        return scoredPSM;
    }

    public Integer totalSpectrumInCluster(String clusterID) {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK='" + clusterID + "'");
        Integer totalElement = (Integer) hBaseDao.queryTotal(querySql.toString());
        return totalElement;
    }

    public List<Spectrum> getSpectraByTitles(String titlesStr) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM \"" + spectrumTableName + "\" WHERE SPEC_TITLE in (");
        String[] titles = titlesStr.split("\\|\\|");
        for (String title : titles){
            title = title.trim();
            querySql.append("'" + title + "',");
        }
        querySql.setLength(querySql.length() - 1);
        querySql.append(")");

        System.out.println("Going to execute: " + querySql);
        List<Spectrum> spectra = (List<Spectrum>) hBaseDao.getSpectra(querySql.toString(), null, new RowMapper<Spectrum>() {
            @Override
            public Spectrum mapRow(ResultSet rs, int rowNum) throws SQLException {
                Spectrum spectrum = new Spectrum();
                spectrum.setTitle(rs.getString("SPEC_TITLE"));
                spectrum.setCharge(rs.getInt("CHARGE"));
                spectrum.setPrecursorMz(rs.getFloat("PRECURSOR_MZ"));
                spectrum.setSequence(rs.getString("ID_SEQUENCES"));
                spectrum.setSpecies(rs.getString("TAXIDS"));
                return spectrum;
            }
        });
        return (spectra != null && spectra.size() > 0) ? (List) spectra: null;
    }
}


