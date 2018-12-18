package org.ncpsb.phoenixcluster.enhancer.webservice.model;

public class ResultFile {
    String fileName=null;
    String fileType=null;

    public ResultFile() {
    }

    public ResultFile(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
