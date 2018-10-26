package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VennDataRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        VennData vennData = new VennData(
                rs.getString("PROJECT_ID"),
                rs.getInt("PREPSM_NO"),
                rs.getInt("PREPSM_NOT_MATCHED_NO"),
                rs.getInt("PREPSM_HIGH_CONF_NO"),
                rs.getInt("PREPSM_LOW_CONF_NO"),
                rs.getInt("BETTER_PSM_NO"),
                rs.getInt("NEW_PSM_NO"),
                rs.getInt("MATCHED_SPEC_NO"),
                rs.getInt("MATCHED_ID_SPEC_NO")
        );
        return vennData;
    }
}
