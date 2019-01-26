package org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJobRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnalysisJobDaoMysqlImpl {
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
                e.printStackTrace();
                return null;
            }
    }

    /***
     * update analysis job, the status is init
     * @param myId
     * @param filePath
     * @param uploadDate
     * @param userId
     * @param status
     * @param accessionId
     */
    public void updateAnalysisJob(Integer myId, String filePath, String uploadDate, int userId, String status, String accessionId) {
        StringBuffer sqlString = new StringBuffer("UPDATE " + analysisRecoredTableName + " SET " +
                "FILE_PATH = ?, UPLOAD_DATE = ?, USER_ID = ? , STATUS = ?, ACCESSION = ? WHERE ID = ?");
        System.out.println(sqlString.toString()+ filePath+ uploadDate+ userId+ status+ accessionId + myId);
        jdbcTemplate.update(sqlString.toString(), filePath, uploadDate, userId, status, accessionId, myId);
    }

    /***
     * update the status of a analysis job
     * @param id
     * @param status
     */
    public void updateAnalysisJobStatus(Integer id, String status) {
        String sqlString = "UPDATE " + analysisRecoredTableName + " SET STATUS = ? WHERE ID = ?";
        System.out.println(sqlString+ status+ id);
        jdbcTemplate.update(sqlString, status, id);
    }

    /***
     * update more information in to a analysis job, email address and ispublic
     * @param id
     * @param emailAdd
     * @param isPublic
     */
    public void updateAnalysisJobMore(Integer id, String emailAdd, Boolean isPublic) {
        String sqlString = "UPDATE " + analysisRecoredTableName + " SET EMAIL_ADD = ?, ISPUBLIC = ? WHERE ID = ?";
        System.out.println(sqlString+ emailAdd+ isPublic+ id);
        jdbcTemplate.update(sqlString, emailAdd, isPublic, id);
    }

    /***
     * update the email sent status
     * @param id
     * @param emailSentStatus
     */
    public void updateAnalysisRecordEmailSentStatus(Integer id, boolean emailSentStatus) {
        StringBuffer sqlString = new StringBuffer("UPDATE " + analysisRecoredTableName +
                " SET IS_EMAIL_SENT = ? WHERE ID = ?");
        System.out.println(sqlString.toString()+emailSentStatus+ id);
        jdbcTemplate.update(sqlString.toString(),emailSentStatus, id);
    }

        /***
     * initialize a new analysis job
     * @return
     */
    public AnalysisJob initAnalysisJob(String token) {
        StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " ORDER BY ID DESC LIMIT 1");

        //getLastAnalysisRecordId
        AnalysisJob analysisJob = null;
        Integer analysisRecordId = 0;
        try {
            analysisRecordId = (Integer) jdbcTemplate.queryForObject(sqlString.toString(),
                    (rs, rowNum) -> new Integer(rs.getInt("ID")));
        }catch (EmptyResultDataAccessException e){
            analysisRecordId = 0;
        }finally {
            analysisJob = new AnalysisJob(analysisRecordId + 1, null, null, null, "initialized", false, token);
        }
        this.upsertAnalysisRecord(analysisJob);
        return analysisJob;
    }

     /***
     * insert a new analysis record
     * @param analysisJob
     */
    public void upsertAnalysisRecord(AnalysisJob analysisJob) {
        StringBuffer sqlString = new StringBuffer("INSERT INTO " + analysisRecoredTableName +
                "(ID, FILE_PATH, UPLOAD_DATE, USER_ID, STATUS, TOKEN, ISPUBLIC)" +
                " VALUES(");
        sqlString.append(analysisJob.getId()+ ",");
        sqlString.append("'" + analysisJob.getFilePath()+ "',");
        sqlString.append("'" + analysisJob.getUploadDate()+ "',");
        sqlString.append(analysisJob.getUserId()+ ",");
        sqlString.append("'" + analysisJob.getStatus()+ "',");
        sqlString.append("'" + analysisJob.getToken()+ "',");
        sqlString.append(analysisJob.getPublic()+ ")");

        System.out.println(sqlString.toString());
        jdbcTemplate.update(sqlString.toString());
    }

}
