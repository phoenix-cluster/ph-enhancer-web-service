package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.apache.avro.generic.GenericData;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ExportService {
    @Autowired
    private HBaseDao hBaseDao;
    @Autowired
    private ScoredPSMService scoredPSMService;


    public List<ScoredPSM> getExportedPsmData(String projectId, String recommendScRange, String newIdentScRange,
                String highConfScRange, Boolean hasAccept, String defaultAcceptType, Boolean hasRejected) {

        HashMap<String, Double> recommendScRangeHash = getRangeFromString(recommendScRange);
        HashMap<String, Double> newIdentScRangeHash = getRangeFromString(newIdentScRange);
        HashMap<String, Double> highConfScRangeHash = getRangeFromString(highConfScRange);

        if(recommendScRangeHash.get("start") < 0.0 || recommendScRangeHash.get("end") > 1.0  ||
            newIdentScRangeHash.get("start") < 0.0 || newIdentScRangeHash.get("end") > 1.0 ||
            highConfScRangeHash.get("start") < 0.0 || highConfScRangeHash.get("end") > 1.0 ||
            recommendScRangeHash.get("start") > recommendScRangeHash.get("end") ||
            newIdentScRangeHash.get("start") > newIdentScRangeHash.get("end")  ||
            highConfScRangeHash.get("start") > highConfScRangeHash.get("end")
          ){
            System.out.println("Errors in the ranges, please have a check");
            return null;
        }

        if (recommendScRangeHash.get("start") < recommendScRangeHash.get("end")) {
            String psmTableName = Configure.NEW_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
            StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName);
            StringBuffer conditionsStr = new StringBuffer(" ");
            List<Integer> conditions = new ArrayList<>();
            if (hasAccept) conditions.add(1);
            if (hasRejected) conditions.add(-1);
            if (defaultAcceptType.equals("Accept") || defaultAcceptType.equals("Reject")) conditions.add(0);

            if (conditions.size() == 0) {
                System.out.println("Errors in the accept conditions, please have a type at least.");
                return null;
            } else if (conditions.size() != 3) {//not all accpetance be selected
                String listString = conditions.toString();
                listString = listString.substring(1, listString.length() - 1);
                conditionsStr.append("WHERE  ACCEPTANCE IN(" + listString + ") AND ");
            } else if (conditions.size() == 3) {
                conditionsStr.append("WHERE ");
            }
            conditionsStr.append(" CONF_SC >= " + recommendScRangeHash.get("start"));
            conditionsStr.append(" AND CONF_SC <= " + recommendScRangeHash.get("end"));

            querySql.append(conditionsStr);
            List<ScoredPSM> scoredPSMs = scoredPSMService.getScoredPSMs(querySql.toString(), "newid");
            return scoredPSMs;

        }else {
            return null;
        }
    }

    public List<ScoredPSM> getExportedPsmSingleType(String projectId, String psmType, String scRange,
                Boolean hasAccept, String defaultAcceptType, Boolean hasRejected) {

        HashMap<String, Double> scRangeHash = getRangeFromString(scRange);

        if (scRangeHash.get("start") < -1.0 || scRangeHash.get("end") > 1.0 ||
                scRangeHash.get("start") > scRangeHash.get("end")
                ) {
            System.out.println("Errors in the ranges, please have a check:" + scRangeHash.toString());
            return null;
        }
        String psmTableName = "";
        switch (psmType) {
            case ("negscore"): {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
                break;
            }
            case ("posscore"): {
                psmTableName = Configure.POS_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
                break;
            }
            case ("newid"): {
                psmTableName = Configure.NEW_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
                break;
            }
            default: {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, projectId);
            }
        }

        if (scRangeHash.get("start") < scRangeHash.get("end")) {
            StringBuffer querySql = new StringBuffer("SELECT * FROM " + psmTableName);
            StringBuffer conditionsStr = new StringBuffer(" ");
            List<Integer> conditions = new ArrayList<>();
            if (hasAccept) conditions.add(1);
            if (hasRejected) conditions.add(-1);
            if (defaultAcceptType.equals("Accept") || defaultAcceptType.equals("Reject")) conditions.add(0);

            if (conditions.size() == 0) {
                System.out.println("Errors in the accept conditions, please have a type at least.");
                return null;
            } else if (conditions.size() != 3) {//not all accpetance be selected
                String listString = conditions.toString();
                listString = listString.substring(1, listString.length() - 1);
                conditionsStr.append("WHERE  ACCEPTANCE IN(" + listString + ") AND ");
            } else if (conditions.size() == 3) {
                conditionsStr.append("WHERE ");
            }
            conditionsStr.append(" CONF_SC >= " + scRangeHash.get("start"));
            conditionsStr.append(" AND CONF_SC <= " + scRangeHash.get("end"));

            querySql.append(conditionsStr);

            List<ScoredPSM> scoredPSMs = scoredPSMService.getScoredPSMs(querySql.toString(), psmType);

            return scoredPSMs;

        }
            return null;
    }
    private HashMap<String,Double> getRangeFromString(String scRangeString) {
        if (scRangeString.startsWith("[") && scRangeString.endsWith("]")) {
            HashMap<String, Double> rangeMap = new HashMap<>();
            try {
                scRangeString = scRangeString.substring(1, scRangeString.length() - 1);
                String[] strings = scRangeString.split(",");

                Double start = Double.parseDouble(strings[0]);
                Double end = Double.parseDouble(strings[1]);

                rangeMap.put("start", start);
                rangeMap.put("end", end);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            } finally {
                return rangeMap;
            }
        } else {
            System.out.println("String is not like [*]");
            return null;
        }
    }


}






