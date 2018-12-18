package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.apache.commons.text.RandomStringGenerator;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.AnalysisJobDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.FileUploadResponse;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ResultFile;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ResultFileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static org.apache.commons.text.CharacterPredicates.ASCII_LOWERCASE_LETTERS;
import static org.apache.commons.text.CharacterPredicates.DIGITS;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class FileUploadService {
    @Autowired
    private AnalysisJobDaoMysqlImpl analysisJobDao;

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
     * initialize a new analysis job
     * @return
     */
    public AnalysisJob initAnalysisJob() {
        String token = generateToken();
        AnalysisJob analysisJob = analysisJobDao.initAnalysisJob(token);
        return analysisJob;
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
    public void updateAnalysisJob(Integer myId, String filePath, String uploadDate, int userId, String status, String accessionId) {
        analysisJobDao.updateAnalysisJob(myId, filePath, uploadDate, userId, status, accessionId);
    }

     /***
     * update the status of a analysis job
     * @param id
     * @param status
     */
    public void updateAnalysisJobStatus(Integer id, String status) {
        analysisJobDao.updateAnalysisJobStatus(id, status);
    }


     /***
     *
     * @param myAnalysisId
     * @return
     */
    public AnalysisJob getAnalysisJob(Integer myAnalysisId){
        return analysisJobDao.getAnalysisJob(myAnalysisId);
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

        for (String fileName : resultFileList.getFileNameList()) {
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
            writer.println("fileName" + "\t" + "fileType");
            for (int i=0; i<resultFileList.getFileNameList().size(); i++) {
                String fileName = resultFileList.getFileNameList().get(i);
                String fileType = resultFileList.getFileTypeList().get(i);
                writer.println(fileName + "\t" + fileType);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
