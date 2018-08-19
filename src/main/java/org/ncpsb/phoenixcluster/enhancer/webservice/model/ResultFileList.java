package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.ArrayList;

public class ResultFileList {
    ArrayList<String> fileList ;
    Integer fileListLength = 0;

    public ResultFileList() { }

    public ArrayList<String> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<String> fileList) {
        this.fileList = fileList;
    }

    public Integer getFileListLength() {
        return fileListLength;
    }

    public void setFileListLength(Integer fileListLength) {
        this.fileListLength = fileListLength;
    }
}
