package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.Taxonomy;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.TaxonomyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaxonomyDaoMysqlImpl {
    private String taxonomyTableName ="T_TAXONOMY";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * find a taxonomy by taxonomyId
     * @param taxonomyId
     * @return
     */
    public Taxonomy findByTaxonomyId(String taxonomyId) {
        StringBuffer taxonomySql = new StringBuffer("SELECT NAME FROM  " + taxonomyTableName + " WHERE ");
        taxonomySql.append("ID = ? ");

        try {
            String name = jdbcTemplate.queryForObject(taxonomySql.toString(),  new Object[]{taxonomyId}, String.class);
            return new Taxonomy(taxonomyId, name);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("got no taxonomy name for taxonomyid " + taxonomyId);
            return null;
        }
    }

    /***
     * insert a taxonomy
     * @param taxonomy
     * @return int status
     */
    public Integer insertTaxonomy(Taxonomy taxonomy) {
        StringBuffer taxonomySql = new StringBuffer("INSERT INTO " + taxonomyTableName + " (ID, NAME) ");
        taxonomySql.append("VALUES (?, ?) ");
        return jdbcTemplate.update(taxonomySql.toString(), taxonomy.getId(), taxonomy.getName());
    }

}
