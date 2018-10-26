package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.apache.commons.text.RandomStringGenerator;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJobRowMapper;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.FileUploadResponse;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ResultFileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.text.CharacterPredicates.ASCII_LOWERCASE_LETTERS;
import static org.apache.commons.text.CharacterPredicates.DIGITS;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class FileUploadService {
    @Autowired
    private HBaseDao hBaseDao;
    String analysisRecoredTableName = "T_ANALYSIS_RECORD";

    /***
     * initialize a new analysis job
     * @return
     */
    public AnalysisJob initAnalysisJob() {
        StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " ORDER BY ID DESC LIMIT 1");

        //getLastAnalysisRecordId
        Integer analysisRecordId = (Integer) hBaseDao.getJdbcTemplate().queryForObject(sqlString.toString(),
                (rs, rowNum) -> new Integer(rs.getInt("ID")));
        String token = generateToken();
        AnalysisJob analysisJob = new AnalysisJob(analysisRecordId +1, null, null, null, "initialized", false, token);
        this.upsertAnalysisRecord(analysisJob);
        return analysisJob;
    }

    /***
     * generate randam 10 char string, with digits(0-9), an 'a-z' in lower case.
     * @return
     */
    private String generateToken() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(ASCII_LOWERCASE_LETTERS, DIGITS).build();
        String token = generator.generate(10);
        return token;
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

        hBaseDao.getJdbcTemplate().update(sqlString.toString());
    }


    /***
     * to check if all the files uploaded by user are list in the right path
     * @param resultFileList
     * @param myAnalysisId
     * @param fileUploadResponse
     * @param analysisJob
     * @return
     */
    public boolean isFileListCorrect(ResultFileList resultFileList, Integer myAnalysisId, FileUploadResponse fileUploadResponse, AnalysisJob analysisJob) {
        boolean isCorrect = true;
        File analysisJobDir = new File(analysisJob.getFilePath());
        if (!analysisJobDir.exists()) {
            System.out.println("Error, the path for analysis job " + myAnalysisId + " does not exist: " + analysisJob.getFilePath());
            fileUploadResponse.setMessage("Error, the path for analysis job " + myAnalysisId + " does not exist: " + analysisJob.getFilePath());
            isCorrect = false;
            return isCorrect;
        }

        for (String fileName : resultFileList.getFileList()) {
            File file = new File(analysisJob.getFilePath() + File.separator + fileName);
            if (!file.exists() || !file.isFile() || file.length() < 1) {
                System.out.println("Error, the file in analysis job " + myAnalysisId + " does not exist or is empty: " + file);
                fileUploadResponse.appendMessage("Error, the path for analysis job " + myAnalysisId + " does not exist: " + analysisJob.getFilePath());
                isCorrect = false;
            }
        }
        return isCorrect;
    }

    /***
     * write the file names to resultFiles.txt
     * @param analysisJobDir
     * @param resultFileList
     */
    public void writeToResultFile(String analysisJobDir, ResultFileList resultFileList) {
        File targetFile = new File(analysisJobDir + File.separator +"resultFiles.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(targetFile, "UTF-8");
            for (String fileName:resultFileList.getFileList()) {
                writer.println(fileName);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param myAnalysisId
     * @return
     */
    public AnalysisJob getAnalysisJob(Integer myAnalysisId){
        StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE ID = " + myAnalysisId);
//            System.out.println(sqlString.toString());
        AnalysisJob analysisJob = (AnalysisJob) hBaseDao.getJdbcTemplate().queryForObject(sqlString.toString(), new AnalysisJobRowMapper());
        return analysisJob;
    }

    /***
     * find analysis job by token
     * @param analysisJobToken
     * @return
     */
    public AnalysisJob findAnalysisJobByToken(String analysisJobToken){
            StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE TOKEN = '" + analysisJobToken + "'");
            AnalysisJob analysisJob = (AnalysisJob) hBaseDao.getJdbcTemplate().queryForObject(sqlString.toString(), new AnalysisJobRowMapper());
            return analysisJob;
    }

    /***
     * find analysis jobs by email status( not sent yet and email address not null)
     * @return
     */
    public List<AnalysisJob> getAnalysisJobsToSentEmail(){
            StringBuffer sqlString = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE  STATUS LIKE 'finished%' AND IS_EMAIL_SENT=false " +
            " AND EMAIL_ADD IS NOT NULL");
            List<AnalysisJob> analysisJobs = (List<AnalysisJob>) hBaseDao.getJdbcTemplate().query(sqlString.toString(), new AnalysisJobRowMapper());
        return (analysisJobs != null && analysisJobs.size() > 0) ? (List) analysisJobs: null;
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
        hBaseDao.getJdbcTemplate().update(sqlString.toString());
    }

    /***
     * update the status of a analysis job
     * @param id
     * @param status
     */
    public void updateAnalysisJobStatus(Integer id, String status) {
        String sqlString = "UPSERT INTO " + analysisRecoredTableName + " (ID, STATUS) VALUES(?,?)";
        hBaseDao.getJdbcTemplate().update(sqlString, id, status);
    }

    /***
     * update more information in to a analysis job, email address and ispublic
     * @param id
     * @param emailAdd
     * @param isPublic
     */
    public void updateAnalysisJobMore(Integer id, String emailAdd, Boolean isPublic) {
        String sqlString = "UPSERT INTO " + analysisRecoredTableName + " (ID, EMAIL_ADD, ISPUBLIC) VALUES(?,?,?)";
        hBaseDao.getJdbcTemplate().update(sqlString, id, emailAdd, isPublic);
    }

    /***
     * update the email sent status
     * @param id
     * @param emailSentStatus
     */
    public void updateAnalysisRecordEmailSentStatus(Integer id, boolean emailSentStatus) {
        StringBuffer sqlString = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                " (ID, IS_EMAIL_SENT) VALUES(?, ?)");
        hBaseDao.getJdbcTemplate().update(sqlString.toString(), id,emailSentStatus);
    }
}
