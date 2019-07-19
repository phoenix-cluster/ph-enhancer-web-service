package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaxonomyRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(rs.getString("ID"));
        taxonomy.setName(rs.getString("NAME"));
        return taxonomy;
    }
}
