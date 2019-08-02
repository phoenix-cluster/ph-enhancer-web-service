package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpeciesInProjectRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpeciesInProject speciesInProject = new SpeciesInProject();
        speciesInProject.setId(rs.getString("TAXID"));
        speciesInProject.setPsmNo(rs.getInt("PSM_NO"));
        return speciesInProject;
    }
}
