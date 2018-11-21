package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumInCluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumInClusterRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpectrumInClusterDaoMysqlImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
        /***
     * Dao
     * @param title
     * @return
     */
    public SpectrumInCluster findSpectrumInClusterByTitle(String title, String spectrumInClusterTableName) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM " + spectrumInClusterTableName + " WHERE ");
        title = title.trim();
        querySql.append("SPEC_TITLE = ? ");
        System.out.println(querySql);
        SpectrumInCluster scoredPSM = (SpectrumInCluster) jdbcTemplate.queryForObject(querySql.toString(),
                new Object[] {title}, new SpectrumInClusterRowMapper());
        return scoredPSM;
    }

    /***
     * Dao
     * @param titles
     * @return
     */
    public List<SpectrumInCluster> findSpectraInClusterByTitles(List<String> titles, String spectrumInClusterTableName) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM " + spectrumInClusterTableName + " WHERE SPEC_TITLE in (");
        for (String title : titles){
            title = title.trim();
            querySql.append("'" + title + "',");
        }
        querySql.setLength(querySql.length() - 1);
        querySql.append(")");
        System.out.println("Going to execute: " + querySql);
        List<SpectrumInCluster> spectra = (List<SpectrumInCluster>) jdbcTemplate.query(querySql.toString(),
                                     new SpectrumInClusterRowMapper());

        return (spectra != null && spectra.size() > 0) ? (List) spectra: null;
    }

}
