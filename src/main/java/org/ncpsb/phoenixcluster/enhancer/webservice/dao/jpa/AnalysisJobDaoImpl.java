package org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJobRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnalysisJobDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    String analysisRecoredTableName = "T_ANALYSIS_RECORD";
        /***
     *
     * @param myAnalysisId
     * @return
     */
    public AnalysisJob getAnalysisJob(Integer myAnalysisId){
        StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE ID = " + myAnalysisId);
            System.out.println(sqlString.toString());
        AnalysisJob analysisJob = (AnalysisJob) jdbcTemplate.queryForObject(sqlString.toString(), new AnalysisJobRowMapper());
        return analysisJob;
    }

    /***
     * find analysis job by token
     * @param analysisJobToken
     * @return
     */
    public AnalysisJob findAnalysisJobByToken(String analysisJobToken){
            StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE TOKEN = '" + analysisJobToken + "'");
            AnalysisJob analysisJob = (AnalysisJob) jdbcTemplate.queryForObject(sqlString.toString(), new AnalysisJobRowMapper());
            return analysisJob;
    }

    /***
     * find analysis jobs by email status( not sent yet and email address not null)
     * @return
     */
    public List<AnalysisJob> getAnalysisJobsToSentEmail() {
            StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE  STATUS LIKE 'finished%' AND IS_EMAIL_SENT=false " +
            " AND EMAIL_ADD IS NOT NULL");
            try {
                List<AnalysisJob> analysisJobs = (List<AnalysisJob>) jdbcTemplate.query(sqlString.toString(), new AnalysisJobRowMapper());
                return (analysisJobs != null && analysisJobs.size() > 0) ? (List) analysisJobs: null;
            }catch (Exception e)
            {
                System.out.println(e.getStackTrace());
                return null;
            }
    }

    /***
     * insert analysis job, the status is init
     * @param myId
     * @param filePath
     * @param uploadDate
     * @param userId
     * @param status
     * @param accessionId
     */
    public void insertAnalysisJob(Integer myId, String filePath, String uploadDate, int userId, String status, String accessionId) {
        StringBuffer sqlString = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                " (ID, FILE_PATH, UPLOAD_DATE, USER_ID, STATUS, ACCESSION) VALUES(");
        sqlString.append(myId + ",");
        sqlString.append("'" + filePath + "',");
        sqlString.append("'" + uploadDate + "',");
        sqlString.append(userId + ",");
        sqlString.append("'" + status + "',");
        sqlString.append("'" + accessionId + "')");
        jdbcTemplate.update(sqlString.toString());
    }

    /***
     * update the status of a analysis job
     * @param id
     * @param status
     */
    public void updateAnalysisJobStatus(Integer id, String status) {
        String sqlString = "UPSERT INTO " + analysisRecoredTableName + " (ID, STATUS) VALUES(?,?)";
        jdbcTemplate.update(sqlString, id, status);
    }

    /***
     * update more information in to a analysis job, email address and ispublic
     * @param id
     * @param emailAdd
     * @param isPublic
     */
    public void updateAnalysisJobMore(Integer id, String emailAdd, Boolean isPublic) {
        String sqlString = "UPSERT INTO " + analysisRecoredTableName + " (ID, EMAIL_ADD, ISPUBLIC) VALUES(?,?,?)";
        jdbcTemplate.update(sqlString, id, emailAdd, isPublic);
    }

    /***
     * update the email sent status
     * @param id
     * @param emailSentStatus
     */
    public void updateAnalysisRecordEmailSentStatus(Integer id, boolean emailSentStatus) {
        StringBuffer sqlString = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                " (ID, IS_EMAIL_SENT) VALUES(?, ?)");
        jdbcTemplate.update(sqlString.toString(), id,emailSentStatus);
    }

        /***
     * initialize a new analysis job
     * @return
     */
    public AnalysisJob initAnalysisJob(String token) {
        StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " ORDER BY ID DESC LIMIT 1");

        //getLastAnalysisRecordId
        Integer analysisRecordId = (Integer) jdbcTemplate.queryForObject(sqlString.toString(),
                (rs, rowNum) -> new Integer(rs.getInt("ID")));
        AnalysisJob analysisJob = new AnalysisJob(analysisRecordId +1, null, null, null, "initialized", false, token);
        this.upsertAnalysisRecord(analysisJob);
        return analysisJob;
    }

        /***
     * insert a new analysis record
     * @param analysisJob
     */
    public void upsertAnalysisRecord(AnalysisJob analysisJob) {
        StringBuffer sqlString = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                "(ID, FILE_PATH, UPLOAD_DATE, USER_ID, STATUS, TOKEN, ISPUBLIC)" +
                " VALUES(");
        sqlString.append(analysisJob.getId()+ ",");
        sqlString.append("'" + analysisJob.getFilePath()+ "',");
        sqlString.append("'" + analysisJob.getUploadDate()+ "',");
        sqlString.append(analysisJob.getUserId()+ ",");
        sqlString.append("'" + analysisJob.getStatus()+ "',");
        sqlString.append("'" + analysisJob.getToken()+ "',");
        sqlString.append(analysisJob.getPublic()+ ")");

        jdbcTemplate.update(sqlString.toString());
    }

}
