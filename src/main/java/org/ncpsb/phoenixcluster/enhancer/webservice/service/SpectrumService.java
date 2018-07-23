package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.apache.avro.data.Json;
import org.mortbay.util.ajax.JSON;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Spectrum;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class SpectrumService{
    private String spectrumTableName = "T_SPECTRUM";

    @Autowired
    private HBaseDao hBaseDao;


//    public List<Spectrum> getSpectra(Integer page, Integer size, String clusterID) {
//
//        StringBuffer querySql = new StringBuffer("SELECT * FROM " + spectrumTableName);
//        querySql.append(" LIMIT " + size);
//        querySql.append(" OFFSET " + (page - 1) * size);
//
//        System.out.println("Going to execute: " + querySql);
//        List<Spectrum> scoredPSMs = (List<Spectrum>) hBaseDao.getSpectra(querySql.toString(), null, new RowMapper<Spectrum>() {
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
        querySql.append("SPECTRUM_TITLE = '" + title + "'");
        System.out.println(querySql);

        Spectrum spectrum = (Spectrum) hBaseDao.getSpectrum(querySql.toString(), null, new RowMapper<Spectrum>() {
            @Override
            public Spectrum mapRow(ResultSet rs, int rowNum) throws SQLException {
                Spectrum spectrum = new Spectrum();
                spectrum.setTitle(rs.getString("SPECTRUM_TITLE"));
                spectrum.setCharge(rs.getInt("CHARGE"));
                spectrum.setPrecursorMz(rs.getFloat("PRECURSOR_MZ"));
                spectrum.setPrecursorIntens(rs.getFloat("PRECURSOR_INTENS"));
                spectrum.setPeaklistMz(ClusterUtils.getFloatListFromString(rs.getString("PEAKLIST_MZ"), ","));
                spectrum.setPeaklistIntens(ClusterUtils.getFloatListFromString(rs.getString("PEAKLIST_INTENS"), ","));
                return spectrum;
            }
        });
        return spectrum;
    }

//    public Integer totalSpectrum(String clusterID) {
//        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK='" + clusterID + "'");
//        Integer totalElement = (Integer) hBaseDao.queryTotal(querySql.toString());
//        return totalElement;
//    }

    public List<Spectrum> getSpectraByTitles(String titlesStr) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM \"" + spectrumTableName + "\" WHERE SPECTRUM_TITLE in (");
        String[] titles = titlesStr.split("\\|\\|");
        for (String title : titles){
            title = title.trim();
            querySql.append("'" + title + "',");
        }
        querySql.setLength(querySql.length() - 1);
        querySql.append(")");

//        System.out.println("Going to execute: " + querySql);
        List<Spectrum> spectra = (List<Spectrum>) hBaseDao.getSpectra(querySql.toString(), null, new RowMapper<Spectrum>() {
            @Override
            public Spectrum mapRow(ResultSet rs, int rowNum) throws SQLException {
                Spectrum spectrum = new Spectrum();
                spectrum.setTitle(rs.getString("SPECTRUM_TITLE"));
                spectrum.setCharge(rs.getInt("CHARGE"));
                spectrum.setPrecursorMz(rs.getFloat("PRECURSOR_MZ"));
                spectrum.setPrecursorIntens(rs.getFloat("PRECURSOR_INTENS"));
                spectrum.setPeaklistMz(ClusterUtils.getFloatListFromString(rs.getString("PEAKLIST_MZ"), ","));
                spectrum.setPeaklistIntens(ClusterUtils.getFloatListFromString(rs.getString("PEAKLIST_INTENS"), ","));
                return spectrum;
            }
        });
        return (spectra != null && spectra.size() > 0) ? (List) spectra: null;
    }
    public String getSpecPeptideSeqByTitle(String title){
        String projectId = title.substring(0,title.indexOf(";"));
        String psmTableName = "T_"+projectId+"_PSM";
        StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName + " WHERE ");
        title = title.trim();
        querySql.append("SPECTRUM_TITLE = '" + title + "'");
        System.out.println(querySql);
        String peptideSequence = hBaseDao.getSpecPeptideSeq(querySql.toString());
        String rs = peptideSequence;
        return rs;
    }
}


