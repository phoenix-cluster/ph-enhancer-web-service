package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class HistogramService {
    @Autowired
    private HBaseDao hBaseDao;


    public List<HistogramBin> getHistData(String projectId, String type, String fieldType, Integer numBins) {
        String psmTableName = "";
        switch (type) {
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

//        StringBuffer querySql = new StringBuffer("SELECT  ID, CLUSTER_SIZE FROM " + psmTableName);

        switch (fieldType){
            case ("confScore"):{
                return getConfidentScoreHistData(psmTableName, numBins);
            }
            case ("recommConfScore"):{
                return getRecommConfidentScoreHistData(psmTableName, numBins);
            }
            case ("clusterRatio"):{
                return getClusterRatioHistData(psmTableName, numBins);
            }
            case ("clusterSize"):{
                return getClusterSizeHistData(psmTableName, numBins);
            }
        }
        return  null;
//        return (clusters != null && clusters.size() > 0) ? clusters : null;
    }

    private List<HistogramBin>getRecommConfidentScoreHistData(String psmTableName, Integer numBins) {
        StringBuffer querySql = new StringBuffer("SELECT RECOMM_SEQ_SC FROM " + psmTableName);

        List<Object> objectList = (List<Object>) hBaseDao.queryList(querySql.toString(), new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ((Object) rs.getDouble("RECOMM_SEQ_SC"));
            }
        });
        return getFloatHistList(objectList, numBins);
    }

    private List<HistogramBin>getConfidentScoreHistData(String psmTableName, Integer numBins) {
        StringBuffer querySql = new StringBuffer("SELECT CONF_SC FROM " + psmTableName);

        List<Object> objectList = (List<Object>) hBaseDao.queryList(querySql.toString(), new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ((Object) rs.getDouble("CONF_SC"));
            }
        });
        return getFloatHistList(objectList, numBins);
    }

    private List<HistogramBin>getClusterRatioHistData(String psmTableName, Integer numBins) {
        StringBuffer querySql = new StringBuffer("SELECT CLUSTER_RATIO FROM " + psmTableName);

        List<Object> objectList = (List<Object>) hBaseDao.queryList(querySql.toString(), new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ((Object) rs.getDouble("CLUSTER_RATIO"));
            }
        });
        return getFloatHistList(objectList, numBins);
    }

    private List<HistogramBin>getClusterSizeHistData(String psmTableName, Integer numBins) {
        StringBuffer querySql = new StringBuffer("SELECT CLUSTER_SIZE FROM " + psmTableName);

        List<Object> objectList = (List<Object>) hBaseDao.queryList(querySql.toString(), new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ((Object) rs.getInt("CLUSTER_SIZE"));
            }
        });
        List<Integer> sizeScoreList = new ArrayList<>();
        for (Object o1 : objectList
                ) {
            Integer i1 = (Integer) o1;
            sizeScoreList.add(i1);
        }

        sizeScoreList.sort((a, b) -> Integer.compare(a, b));

        Integer onePercentile = StatisticsUtils.getIntPercentileFromSorted(1, sizeScoreList);
        Integer lastOnePercentile = StatisticsUtils.getIntPercentileFromSorted(99, sizeScoreList);

        HistogramBin firstBin = new HistogramBin(1, (double) 0, (double)onePercentile,  0);
        HistogramBin lastBin =  new HistogramBin(numBins, (double) lastOnePercentile, (double) Integer.MAX_VALUE, 0);

        for (int i=sizeScoreList.size()-1; i>=0; i--) {
            Integer itemI = sizeScoreList.get(i);
            if (itemI > lastBin.getLowerBound() && itemI <= lastBin.getUpperBound()) {
                lastBin.addOne();
                sizeScoreList.remove(itemI);
            }
            if (itemI >= firstBin.getLowerBound() && itemI <= firstBin.getUpperBound()) {
                firstBin.addOne();
                sizeScoreList.remove(itemI);
            }
        }

        Integer min = sizeScoreList.get(0);
        Integer max = sizeScoreList.get(sizeScoreList.size() - 1);
        List<HistogramBin> binList = StatisticsUtils.calcIntHistogram(sizeScoreList, min, max, numBins-2);
        binList.add(0, firstBin);
        binList.add(binList.size(), lastBin);

        return binList;
    }


    private List<HistogramBin> getFloatHistList(List<Object> objectList, Integer numBins){
        List<Double> floatScoreList = new ArrayList<>();
        for (Object o1 : objectList
                ) {
            Double f1 = (Double) o1;
            floatScoreList.add(f1);
        }

        floatScoreList.sort((a, b) -> Double.compare(a, b));

        Double onePercentile = StatisticsUtils.getPercentileFromSorted(1, floatScoreList);
        Double lastOnePercentile = StatisticsUtils.getPercentileFromSorted(99, floatScoreList);

        Double lowerBoud = floatScoreList.get(0) <0.0 ? -1.0 : 0.0 ;
        Double upperBoud = floatScoreList.get(floatScoreList.size() - 1) > 0.0 ? 1.0 : 0.0 ;
        HistogramBin firstBin = new HistogramBin(1, lowerBoud, onePercentile,  0);
        HistogramBin lastBin =  new HistogramBin(numBins, lastOnePercentile, upperBoud, 0);

        for (int i=floatScoreList.size()-1; i>=0; i--) {
            Double itemF = floatScoreList.get(i);
            if (itemF > lastBin.getLowerBound() && itemF <= lastBin.getUpperBound()) {
                lastBin.addOne();
                floatScoreList.remove(itemF);
            }
            if (itemF >= firstBin.getLowerBound() && itemF <= firstBin.getUpperBound()) {
                firstBin.addOne();
                floatScoreList.remove(itemF);
            }
        }
        Double min = floatScoreList.get(0);
        Double max = floatScoreList.get(floatScoreList.size() - 1);
        List<HistogramBin> binList = StatisticsUtils.calcHistogram(floatScoreList, min, max, numBins-2);
        binList.add(0, firstBin);
        binList.add(binList.size(), lastBin);
        return binList;
    }

}


