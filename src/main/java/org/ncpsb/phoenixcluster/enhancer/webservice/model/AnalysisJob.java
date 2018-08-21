package org.ncpsb.phoenixcluster.enhancer.webservice.model;

public class AnalysisJob {
    Integer id;
    String filePath;
    String uploadDate;
    Integer userId;
    String status;
    Boolean isPublic;
    String token;
    String emailAdd;
    Boolean isEmailSent;

    public AnalysisJob() {
    }

    public AnalysisJob(Integer id, String filePath, String uploadDate, Integer userId, String status, Boolean isPublic, String token) {


        this.id = id;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.userId = userId;
        this.status = status;
        this.isPublic = isPublic;
        this.token = token;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public Boolean getEmailSent() {
        return isEmailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        isEmailSent = emailSent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
