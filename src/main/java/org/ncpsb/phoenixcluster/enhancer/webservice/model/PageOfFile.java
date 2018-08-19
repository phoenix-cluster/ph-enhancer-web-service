package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.ArrayList;

public class PageOfFile {
    ArrayList<String> lines;
    Integer startLineNo;
    Integer endLineNo;
    Integer length;

    public PageOfFile(ArrayList<String> lines, Integer startLineNo, Integer endLineNo, Integer length) {
        this.lines = lines;
        this.startLineNo = startLineNo;
        this.endLineNo = endLineNo;
        this.length = length;
    }

    public ArrayList<String> getLines() {

        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }

    public Integer getStartLineNo() {
        return startLineNo;
    }

    public void setStartLineNo(Integer startLineNo) {
        this.startLineNo = startLineNo;
    }

    public Integer getEndLineNo() {
        return endLineNo;
    }

    public void setEndLineNo(Integer endLineNo) {
        this.endLineNo = endLineNo;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for(String line : lines){
            stringBuffer.append(line + "\n");
        }
        stringBuffer.append(startLineNo + " -- " + endLineNo + ": " + length +" lines.\n");
        return stringBuffer.toString();
    }
}
