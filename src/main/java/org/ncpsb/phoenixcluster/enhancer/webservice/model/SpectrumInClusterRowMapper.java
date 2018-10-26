package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpectrumInClusterRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpectrumInCluster spectrumInCluster = new SpectrumInCluster();
        spectrumInCluster.setTitle(rs.getString("SPEC_TITLE"));
        spectrumInCluster.setCharge(rs.getInt("CHARGE"));
        spectrumInCluster.setPrecursorMz(rs.getFloat("PRECURSOR_MZ"));
        spectrumInCluster.setSequence(rs.getString("ID_SEQUENCES"));
        spectrumInCluster.setSpecies(rs.getString("TAXIDS"));
        return spectrumInCluster;
    }
}
