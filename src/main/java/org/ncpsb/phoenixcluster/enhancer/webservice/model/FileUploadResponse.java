package org.ncpsb.phoenixcluster.enhancer.webservice.model;

public class FileUploadResponse {
    String status;
    Integer id;
    String message;

    public FileUploadResponse(String status, Integer id, String message) {
        this.status = status;
        this.id = id;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public  void appendMessage(String message){
        this.message = this.message + "\n" + message;
    }
}
