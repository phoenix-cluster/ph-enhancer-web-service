package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpectrumRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Spectrum spectrum = new Spectrum();
        spectrum.setTitle(rs.getString("SPECTRUM_TITLE"));
        spectrum.setCharge(rs.getInt("CHARGE"));
        spectrum.setPrecursorMz(rs.getFloat("PRECURSOR_MZ"));
        spectrum.setPrecursorIntens(rs.getFloat("PRECURSOR_INTENS"));
        spectrum.setPeaklistMz(ClusterUtils.getFloatListFromString(rs.getString("PEAKLIST_MZ"), ","));
        spectrum.setPeaklistIntens(ClusterUtils.getFloatListFromString(rs.getString("PEAKLIST_INTENS"), ","));
        return spectrum;
    }
}
