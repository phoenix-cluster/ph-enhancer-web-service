package org.ncpsb.phoenixcluster.enhancer.webservice.service;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.AnalysisJobDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.PageOfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class DoAnalysisService {

    @Autowired
    private AnalysisJobDaoMysqlImpl analysisJobDao;

    @Autowired
    MailService mailService;

    String analysisRecoredTableName = "T_ANALYSIS_RECORD";
    //todo: checkTheFiles
    @Autowired
    FileUploadService fileUploadService;

    /***
     * start to do the analysis job, with these parameters
     * @param myAnalysisId
     * @param minClusterSize
     * @param userEmailAdd
     * @param isPublic
     * @return
     */
    public String doAnalysis(Integer myAnalysisId, Integer minClusterSize, String userEmailAdd, Boolean isPublic){
        AnalysisJob analysisJob = fileUploadService.getAnalysisJob(myAnalysisId);
        analysisJobDao.updateAnalysisJobMore(myAnalysisId, userEmailAdd, isPublic);
        Process proc = null;
        File analysisJobFilePath = new File(analysisJob.getFilePath());
        File workingDir = analysisJobFilePath.getParentFile();
        try {

            String accessionId = String.format("E%06d", myAnalysisId);

            if (isAnalysisStarted(myAnalysisId)) {
                System.out.println("The analysis job"  + accessionId + " has been started");
                return "The analysis job has been started";
            }
            String pythonPath = "/usr/bin/python3 ";
            String pipelinePath = "/home/ubuntu/mingze/tools/spectra-library-analysis/analysis_pipeline.py ";
            String parameterAccessionId = "-p " + accessionId + " ";
            String parameterClusterSize = "-s " + minClusterSize + " ";
            String parameterQuiet= "-t y ";
            String commandLine = pythonPath + pipelinePath + parameterAccessionId+ parameterClusterSize + parameterQuiet;

            //this part is used to test if the invoke are working fine or not
            commandLine = "/usr/bin/python3 /tmp/test.py";
            System.out.println("start to execute " + commandLine);
            proc = Runtime.getRuntime().exec(commandLine, null, workingDir);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

            //rename the resultFile.txt for running, avoid multiple jobs being invoked at the same time
            File resultFile = new File(analysisJob.getFilePath() + File.separator + "resultFiles.txt");
            File runningResultFile = new File(analysisJob.getFilePath() + File.separator + "resultFiles.txt.started");
            if (resultFile.exists()) {
                resultFile.renameTo(runningResultFile);
            }else{
                if (runningResultFile.exists()) {
                    System.out.println("The resultFile.txt.started file exists, this job " + myAnalysisId + "is in running status, cancel this analysis application.");
                    return "The analysis job " + myAnalysisId + " had been started before";
                }
                else {
                    System.out.println("This job " + myAnalysisId + " resultFile.txt is going wrong, please contact the administrator to have a check.");
                    System.out.println("file:" + resultFile.getAbsolutePath());
                    return "The analysis job " + myAnalysisId + " is going wrong";
                }
            }
            commandLine = pythonPath + pipelinePath + parameterAccessionId + parameterClusterSize + parameterQuiet;
            System.out.println("start to execute " + commandLine);
            proc = Runtime.getRuntime().exec(commandLine, null, workingDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(30000);//sleep 30s, then check if the project has been started.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isAnalysisStarted(myAnalysisId)) {
            System.out.println("The analysis job is started");
            return "The analysis job is started";
        } else {
            System.out.println("The analysis job fail to start, please contact the help group to have a check");
            return "The analysis job fail to start, please contact the help group to have a check";
        }

//        try {
//            proc.waitFor();//等待执行脚本的子程序执行完毕后再继续执行后面的代码
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    /***
     * check if this analysis job aleady started, by checking the status of the analysis job
     * @param analysisJobId
     * @return
     */
    private boolean isAnalysisStarted(Integer analysisJobId) {
        AnalysisJob analysisJob = fileUploadService.getAnalysisJob(analysisJobId);
        String status = analysisJob.getStatus();
        System.out.println("get analysis job's status: " + status);
        if (status.equalsIgnoreCase("started") || status.equalsIgnoreCase("finished") || status.equalsIgnoreCase("finished_with_error")) {
            return true;
        } else {
            return false;
        }
    }


    /***
     * get analysis job by invock the method in service
     * @param analysisId
     * @return
     */
    public AnalysisJob getAnalysisJob(Integer analysisId) {
        return this.fileUploadService.getAnalysisJob(analysisId);
    }

    /***
     * find  an analysis job by token (random 20 chars)
     * @param token
     * @return
     */
    public AnalysisJob getAnalysisJobByToken(String token) {
        return analysisJobDao.findAnalysisJobByToken(token);
    }


    /***
     * Read latest lines from the log file
     * @param analysisJob
     * @param startLineNo
     * @return
     * @throws FileNotFoundException
     */
    public PageOfFile getPageFromLog(AnalysisJob analysisJob, Integer startLineNo) throws FileNotFoundException {
        Integer logFileLength = 0, fileLength_toRead = 0;
        PageOfFile pageOfFile  = new PageOfFile(null, 0, 0, 0);

        String pathString = analysisJob.getFilePath();
        File file = new File(pathString);
        String parentDirString = file.getParent();
        String filePathName = parentDirString + File.separator + analysisJob.getAccessionId()+"_pipeline.log";
        File logFile = new File(filePathName);
        if (logFile.isFile()) {
            try {
                logFileLength = countLines(logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileLength_toRead = logFileLength - startLineNo +1;
        }else{
            System.out.println("ERROR, file not exists: " + filePathName);
            throw new FileNotFoundException("ERROR, file not exists: " + filePathName);
        }
        if (fileLength_toRead <= 0) {
            if (analysisJob.getStatus().equalsIgnoreCase("finished")) {
                pageOfFile.setLength(-1);//no more lines to get
            }
            return pageOfFile;
        }
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(filePathName))){
            ArrayList lines = new ArrayList();
            for(int i=0;i<fileLength_toRead;i++){
                String line=reader.readLine();
                if(line==null){
                    System.out.println("ERROR, read a null line at line -" + i);
                    break;}
                lines.add(line + "\n");
            }
            Collections.reverse(lines);
            pageOfFile.setLines(lines);
            pageOfFile.setLength(lines.size());
            pageOfFile.setStartLineNo(startLineNo);
            pageOfFile.setEndLineNo(logFileLength);
//            if (lines.size() == 0 && analysisJob.getStatus().equalsIgnoreCase("finished")) {
//                pageOfFile.setLength(-1);//no more lines to get
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageOfFile;
    }

    /***
     * get the No. of lines of a file
     * @param input
     * @return
     * @throws IOException
     */
    private static int countLines(File input) throws IOException {
        try (InputStream is = new FileInputStream(input)) {
            int count = 1;
            int lastChar = 0;
            for (int aChar = 0; aChar != -1;aChar = is.read()) {
                lastChar = aChar;
                count += lastChar == '\n' ? 1 : 0;
            }
            if (lastChar == '\n') {
                count--; //reduce the line number if the last line also has a \n at the end.
            }
            return count;
        }
    }
}
