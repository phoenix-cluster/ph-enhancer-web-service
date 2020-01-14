package org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumInCluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumInClusterRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpectrumInClusterDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String spectrumTableName = Configure.SPECTRUM_TABLE;
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
        SpectrumInCluster scoredPSM = (SpectrumInCluster) jdbcTemplate.queryForObject(querySql.toString(),
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
        Integer totalElement = (Integer) jdbcTemplate.queryForObject(querySql.toString(), new Object[]{clusterID}, Integer.class);
        return totalElement;
    }

    /***
     * Dao
     * @param titles
     * @return
     */
    public List<SpectrumInCluster> findSpectraInClusterByTitles(List<String> titles) {
        StringBuffer querySql = new StringBuffer("SELECT * FROM \"" + spectrumTableName + "\" WHERE SPEC_TITLE in (");
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
