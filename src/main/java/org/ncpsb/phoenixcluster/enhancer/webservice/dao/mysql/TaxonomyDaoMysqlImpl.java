package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpeciesInProject;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpeciesInProjectRowMapper;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Taxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaxonomyDaoMysqlImpl { private String taxonomyTableName ="T_TAXONOMY";
    private String spieciesInProjectTableName ="T_TAXIDS_IN_PROJECT";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * find a taxonomy by taxonomyId
     * @param taxonomyId
     * @return
     */
    public Taxonomy findByTaxonomyId(String taxonomyId) {
        StringBuffer taxonomySql = new StringBuffer("SELECT NAME FROM  " + taxonomyTableName + " WHERE ");
        taxonomySql.append("ID = '" + taxonomyId + "'");
        System.out.println(taxonomySql.toString());
        try {
            List<String> nameList = jdbcTemplate.queryForList(taxonomySql.toString(),  String.class);
            if (nameList.size() >= 1) {
                return new Taxonomy(taxonomyId, nameList.get(0));
            } else {
                System.out.println("got no taxonomy name for taxonomyid from DB table " + taxonomyId);
                return null;
            }
        } catch (Exception e) {
            System.out.println("got no taxonomy name for taxonomyid from DB table" + taxonomyId);
            e.printStackTrace();
            return null;
        }
    }

    /***
     * insert a taxonomy
     * @param taxonomy
     * @return int status
     */
    public Integer insertTaxonomy(Taxonomy taxonomy) {
        if (taxonomy == null) {
            return null;
        }
        StringBuffer taxonomySql = new StringBuffer("INSERT INTO " + taxonomyTableName + " (ID, NAME) ");
        taxonomySql.append("VALUES (?, ?) ");
        return jdbcTemplate.update(taxonomySql.toString(), taxonomy.getId(), taxonomy.getName());
    }


    public List<SpeciesInProject> findSpeciesInProject(String projectId, String scoreType) {
        StringBuffer speciesInProjectSql = new StringBuffer("SELECT TAXID, PSM_NO FROM  " + spieciesInProjectTableName + " WHERE ");
        speciesInProjectSql.append(" PROJECT_ID = '" + projectId + "' ");

        if (scoreType.equals("newid") || scoreType.equals("negscore") || scoreType.equals("posscore")) {
                speciesInProjectSql.append(" AND SCORE_TYPE = '" + scoreType + "'");
        }else if (!scoreType.equals("all")) {
            return null; //the scoreType could only be all/newid/negscore/posscore
        }
        return (List<SpeciesInProject>)jdbcTemplate.query(speciesInProjectSql.toString(), new SpeciesInProjectRowMapper());
    }

}
