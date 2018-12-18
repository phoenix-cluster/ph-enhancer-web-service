package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.ArrayList;

public class ResultFileList {
    ArrayList<ResultFile> fileList;
    ArrayList<String> fileNameList ;
    ArrayList<String> fileTypeList;
    Integer fileListLength = 0;

    public ResultFileList() { }

    public ArrayList<ResultFile> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<ResultFile> fileList) {
        this.fileList = fileList;
    }

    public ArrayList<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(ArrayList<String> fileNameList) {
        this.fileNameList = fileNameList;
    }

    public ArrayList<String> getFileTypeList() {
        return fileTypeList;
    }

    public void setFileTypeList(ArrayList<String> fileTypeList) {
        this.fileTypeList = fileTypeList;
    }

    public Integer getFileListLength() {
        return fileListLength;
    }

    public void setFileListLength(Integer fileListLength) {
        this.fileListLength = fileListLength;
    }
}
