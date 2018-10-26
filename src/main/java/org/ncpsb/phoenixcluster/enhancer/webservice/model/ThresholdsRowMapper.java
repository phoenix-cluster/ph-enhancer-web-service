package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ThresholdsRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Thresholds thresholds = new Thresholds(
                rs.getString("PROJECT_ID"),
                rs.getInt("CLUSTER_SIZE_THRESHOLD"),
                rs.getFloat("CLUSTER_RATIO_THRESHOLD"),
                rs.getFloat("CONF_SC_THRESHOLD"),
                rs.getFloat("SPECTRAST_FVAL_THRESHOLD")
        );
        return thresholds;
    }
}
