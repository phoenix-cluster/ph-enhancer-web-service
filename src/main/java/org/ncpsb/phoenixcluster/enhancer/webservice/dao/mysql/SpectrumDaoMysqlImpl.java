package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.Spectrum;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumRowMapper;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.TableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
@Repository
public class SpectrumDaoMysqlImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String spectrumTableNamePrefix = "T_SPECTRUM";

    public Spectrum findSpectrumByTitle(String title) {
        String spectrumTableName = TableName.getSpectrumTableName(title, spectrumTableNamePrefix);
        StringBuffer querySql = new StringBuffer("SELECT * FROM " + spectrumTableName + " WHERE ");
        title = title.trim();
        querySql.append("SPECTRUM_TITLE = '" + title + "'");
        System.out.println(querySql);

        Spectrum spectrum = (Spectrum) jdbcTemplate.queryForObject(querySql.toString(), new SpectrumRowMapper());
        return spectrum;
    }


     /***
     * Dao
     * @param titles
     * @return
     */
    public List<Spectrum> findSpectraByTitles(List<String> titles, String spectrumTableName) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM " + spectrumTableName + " WHERE SPECTRUM_TITLE in (");
        for (String title : titles){
            title = title.trim();
            querySql.append("'" + title + "',");
        }
        querySql.setLength(querySql.length() - 1);
        querySql.append(")");
        try {
            System.out.println("Going to execute: " + querySql);
            List<Spectrum> spectra = (List<Spectrum>) jdbcTemplate.query(querySql.toString(), new SpectrumRowMapper());
            return (spectra != null && spectra.size() > 0) ? (List) spectra: null;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    /***
     * Dao
     * @param title
     * @return
     */
    public String findSpecPeptideSeqByTitle(String title){
        String projectId = title.substring(0,title.indexOf(";"));
        String psmTableName = "T_"+projectId+"_PSM";
        StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName + " WHERE ");
        title = title.trim();
        querySql.append("SPECTRUM_TITLE = ? ");
        System.out.println(querySql);
        try {
            String peptideSequence = jdbcTemplate.queryForObject(querySql.toString(), new Object[]{title}, (rs, rowNum) -> rs.getString("PEPTIDE_SEQUENCE"));
            return peptideSequence;
        }catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

}
