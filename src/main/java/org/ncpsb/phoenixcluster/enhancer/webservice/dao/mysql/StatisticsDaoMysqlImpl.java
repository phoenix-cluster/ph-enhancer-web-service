package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.apache.avro.generic.GenericData;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class StatisticsDaoMysqlImpl {
    private String spectrumTableName = Configure.SPECTRUM_TABLE;

    @Autowired
    private AnalysisJobDaoMysqlImpl analysisJobDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Integer findTotalSpectrumInCluster(String clusterID) {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK= ? ");
        try {
            Integer totalElement = (Integer) jdbcTemplate.queryForObject(querySql.toString(), new Object[]{clusterID}, Integer.class);
            return totalElement;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }


    public VennData findVennDataByProjectId(String projectId) {
        StringBuffer vennSql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE + " WHERE ");
        vennSql.append("PROJECT_ID = ? ");

        if (projectId.startsWith("E")) {
            int id = Integer.parseInt(projectId.substring(1));
            AnalysisJob analysisJob = analysisJobDao.getAnalysisJob(id);
            if (analysisJob == null || !analysisJob.getPublic()) {
                System.out.println("project " + projectId + "is not exists or is private.");
                return null;
            }
        }

        System.out.println(vennSql.toString() + projectId);
        try {
            VennData vennData = (VennData) jdbcTemplate.queryForObject(vennSql.toString(), new Object[]{projectId}, new VennDataRowMapper());
            return vennData;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public Thresholds findThresholdsByProjectId(String projectId) {

        if (projectId.startsWith("E")) {
            int id = Integer.parseInt(projectId.substring(1));
            AnalysisJob analysisJob = analysisJobDao.getAnalysisJob(id);
            if (analysisJob == null || !analysisJob.getPublic()) {
                System.out.println("project " + projectId + "is not exists or is private.");
                return null;
            }
        }

        StringBuffer querySql = new StringBuffer("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE + " WHERE ");
        querySql.append("PROJECT_ID = ? ");
        try {
            Thresholds thresholds= (Thresholds) jdbcTemplate.queryForObject(querySql.toString(), new Object[]{projectId}, new ThresholdsRowMapper());
            return thresholds;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<String> findProjects() {
        StringBuffer querySql = new StringBuffer("SELECT PROJECT_ID FROM  " + Configure.PROJECT_RESULT_TABLE);
        try {
            List<String> projects = (List<String>)jdbcTemplate.query(querySql.toString(), (rs, rowNum)->rs.getString("PROJECT_ID"));
//            System.out.println("Find original projects: " + projects.toString());
            List<String> filteredProjects = new ArrayList<>();
            for (String projectId: projects){
                if (projectId.startsWith("PXD") || projectId.startsWith("PRD")){
                    filteredProjects.add(projectId);
                }else if(projectId.startsWith("E")){
                    int id = Integer.parseInt(projectId.substring(1));
                    AnalysisJob analysisJob = analysisJobDao.getAnalysisJob(id);
                    if(analysisJob != null && analysisJob.getPublic()){
                        filteredProjects.add(projectId);
                    }
                }
            }
//            System.out.println("Filtered projects: " + filteredProjects.toString());
            return filteredProjects;
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<VennData> getVennDataList() {
        String vennSql = new String("SELECT * FROM  " + Configure.PROJECT_RESULT_TABLE);
        try {
            List<VennData> vennDataList = (List<VennData>) jdbcTemplate.query(vennSql, new VennDataRowMapper());
//            System.out.println("Found VennDataList: " + vennDataList);
            List<VennData> filteredVennDataList = new ArrayList<>();
            for (VennData vennData: vennDataList){
                String projectId = vennData.getProjectId();
                if (projectId.startsWith("PXD") || projectId.startsWith("PRD")){
                    filteredVennDataList.add(vennData);
                    continue;
                }else if(projectId.startsWith("E")){
                    int id = Integer.parseInt(projectId.substring(1));
                    AnalysisJob analysisJob = analysisJobDao.getAnalysisJob(id);
                    if(analysisJob != null && analysisJob.getPublic()){
                        filteredVennDataList.add(vennData);
                    }
                }
            }
//            System.out.println("Found filteredVennDataList: " + filteredVennDataList);
            return filteredVennDataList;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

}
