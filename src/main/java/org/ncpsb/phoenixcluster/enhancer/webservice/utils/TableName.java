package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableName {
    /***
     *
     * @param title
     * @return
     */
    public static String getSpectrumTableName(String title, String spectrumTableNamePrefix){
        //todo to modify this for Mysql Dao
        String spectrumTableName = "";
        if(title.startsWith("PXD") || title.startsWith("PRD")) {
            String analysisId = getProjectId(title);
            spectrumTableName = spectrumTableNamePrefix + "_" + analysisId;
        }else {
            if (title.startsWith("E")){
                String analysisId = title.substring(0,7);
                spectrumTableName = spectrumTableNamePrefix + "_" + analysisId;// + "_TEST";
            }else{
                System.out.println("ERROR, the spectrum title neither starts with P?D nor E");
                return null;
            }
        }
        return spectrumTableName;
    }

    public static String getProjectId(String spectrumTitle) {
        Pattern p= Pattern.compile("(^P[X,R]D\\d{6})");
        Pattern p2 = Pattern.compile("(^E\\d{6})");
        Matcher m = p.matcher(spectrumTitle);
        Matcher m2 = p2.matcher(spectrumTitle);
        if (m.find()) {
            String projectId = m.group(1);
            return  projectId;
        }
        else if(m2.find()){
                String projectId = m2.group(1);
                return  projectId;
        }
        else{
            System.out.println("Error, no project Id found from " + spectrumTitle);
            return null;
        }

    }

}
