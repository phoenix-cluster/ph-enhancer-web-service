package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.apache.commons.text.RandomStringGenerator;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
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

    public AnalysisJob initAnalysisJob() {
        StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " ORDER BY ID DESC LIMIT 1");

        Integer analysisRecordId = (Integer) hBaseDao.getLastAnalysisRecordId(clusterSql.toString(), null, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Integer(rs.getInt("ID"));
            }
        });
        String token = generateToken();
        AnalysisJob analysisJob = new AnalysisJob(analysisRecordId +1, null, null, null, "initialized", false, token);
        this.upsertAnalysisRecord(analysisJob);
        return analysisJob;
    }

    private String generateToken() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(ASCII_LOWERCASE_LETTERS, DIGITS).build();
        String token = generator.generate(10);
        return token;
    }

    public void upsertAnalysisRecord(AnalysisJob analysisJob) {
        StringBuffer clusterSql = new StringBuffer("UPSERT INTO " + analysisRecoredTableName + " VALUES(");
        clusterSql.append(analysisJob.getId()+ ",");
        clusterSql.append("'" + analysisJob.getFilePath()+ "',");
        clusterSql.append("'" + analysisJob.getUploadDate()+ "',");
        clusterSql.append(analysisJob.getUserId()+ ",");
        clusterSql.append("'" + analysisJob.getStatus()+ "',");
        clusterSql.append("'" + analysisJob.getToken()+ "',");
        clusterSql.append(analysisJob.getPublic()+ ")");

        hBaseDao.update(clusterSql.toString(), null);
    }


    //to check if all the files uploaded by user are list in the right path
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

    public AnalysisJob getAnalysisJob(Integer myAnalysisId){
            StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE ID = " + myAnalysisId);
            AnalysisJob analysisJob = (AnalysisJob) hBaseDao.getAnalysisJob(clusterSql.toString(), null, new RowMapper<AnalysisJob>() {
            @Override
            public AnalysisJob mapRow(ResultSet rs, int rowNum) throws SQLException {
                AnalysisJob analysisJob1 = new AnalysisJob();
                analysisJob1.setId(rs.getInt("ID"));
                analysisJob1.setFilePath(rs.getString("FILE_PATH"));
                analysisJob1.setUploadDate(rs.getString("UPLOAD_DATE"));
                analysisJob1.setStatus(rs.getString("STATUS"));
                analysisJob1.setUserId(rs.getInt("USER_ID"));
                analysisJob1.setPublic(rs.getBoolean("ISPUBLIC"));
                analysisJob1.setToken(rs.getString("TOKEN"));
                return analysisJob1;
            }
        });
            return analysisJob;
    }

    public AnalysisJob getAnalysisJobByToken(String analysisJobToken){
            StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE TOKEN = '" + analysisJobToken + "'");
            AnalysisJob analysisJob = (AnalysisJob) hBaseDao.getAnalysisJob(clusterSql.toString(), null, new RowMapper<AnalysisJob>() {
            @Override
            public AnalysisJob mapRow(ResultSet rs, int rowNum) throws SQLException {
                AnalysisJob analysisJob1 = new AnalysisJob();
                analysisJob1.setId(rs.getInt("ID"));
                analysisJob1.setFilePath(rs.getString("FILE_PATH"));
                analysisJob1.setUploadDate(rs.getString("UPLOAD_DATE"));
                analysisJob1.setStatus(rs.getString("STATUS"));
                analysisJob1.setUserId(rs.getInt("USER_ID"));
                analysisJob1.setPublic(rs.getBoolean("ISPUBLIC"));
                analysisJob1.setToken(rs.getString("TOKEN"));
                return analysisJob1;
            }
        });
            return analysisJob;
    }

    public List<AnalysisJob> getAnalysisJobsToSentEmail(){
            StringBuffer clusterSql = new StringBuffer("SELECT * FROM  " + analysisRecoredTableName + " WHERE  STATUS LIKE 'finished%' AND IS_EMAIL_SENT=false " +
            " AND EMAIL_ADD IS NOT NULL");
            List<AnalysisJob> analysisJobs = (List<AnalysisJob>) hBaseDao.getAnalysisJobs(clusterSql.toString(), null, new RowMapper<AnalysisJob>() {
            @Override
            public AnalysisJob mapRow(ResultSet rs, int rowNum) throws SQLException {
                AnalysisJob analysisJob1 = new AnalysisJob();
                analysisJob1.setId(rs.getInt("ID"));
                analysisJob1.setStatus(rs.getString("STATUS"));
                analysisJob1.setUserId(rs.getInt("USER_ID"));
                analysisJob1.setToken(rs.getString("TOKEN"));
                analysisJob1.setEmailAdd(rs.getString("EMAIL_ADD"));
                analysisJob1.setEmailSent(rs.getBoolean("IS_EMAIL_SENT"));
                return analysisJob1;
            }
        });

        return (analysisJobs != null && analysisJobs.size() > 0) ? (List) analysisJobs: null;
    }



    public void upsertAnalysisRecordInfo(Integer myId, String filePath, String uploadDate, int userId, String status) {
        StringBuffer clusterSql = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                " (ID, FILE_PATH, UPLOAD_DATE, USER_ID, STATUS) VALUES(");
        clusterSql.append(myId + ",");
        clusterSql.append("'" + filePath + "',");
        clusterSql.append("'" + uploadDate + "',");
        clusterSql.append(userId + ",");
        clusterSql.append("'" + status + "')");

        hBaseDao.update(clusterSql.toString(), null);
    }
    public void upsertAnalysisRecordStatus(Integer myId, String status) {
        StringBuffer clusterSql = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                " (ID, STATUS) VALUES(");
        clusterSql.append(myId + ",");
        clusterSql.append("'" + status + "')");

        hBaseDao.update(clusterSql.toString(), null);
    }

    public void upsertAnalysisRecordMore(Integer myId, String emailAdd, Boolean isPublic) {
        StringBuffer clusterSql = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                " (ID, EMAIL_ADD, ISPUBLIC) VALUES(");
        clusterSql.append(myId + ",");
        clusterSql.append("'" + emailAdd + "',");
        clusterSql.append("" + isPublic + ")");

        hBaseDao.update(clusterSql.toString(), null);
    }


    public void upsertAnalysisRecordEmailSentStatus(Integer myId, boolean emailSentStatus) {
        StringBuffer clusterSql = new StringBuffer("UPSERT INTO " + analysisRecoredTableName +
                " (ID, IS_EMAIL_SENT) VALUES(");
        clusterSql.append(myId + ",");
        clusterSql.append(" "+ emailSentStatus+ ")");
        hBaseDao.update(clusterSql.toString(), null);
    }
}
