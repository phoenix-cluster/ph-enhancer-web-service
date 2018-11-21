package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.ScoredPSMDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class HistogramService {
    @Autowired
    private ScoredPSMDaoMysqlImpl scoredPSMDao;
    @Autowired
    private IdentifierService identifierService;

    /***
     * get Histogram Data For Differenet Scores(PSM) And Fileds
     * @param identifier
     * @param type
     * @param fieldType
     * @param numBins
     * @return
     */
    public List<HistogramBin> getHistDataForDifferenetScAndFiled(String identifier, String type, String fieldType, Integer numBins) {
        String psmTableName = "";
        String accessionId = this.identifierService.getJobAccession(identifier);
        switch (type) {
            case ("negscore"): {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, accessionId);
                break;
            }
            case ("posscore"): {
                psmTableName = Configure.POS_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, accessionId);
                break;
            }
            case ("newid"): {
                psmTableName = Configure.NEW_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, accessionId);
                break;
            }
            default: {
                psmTableName = Configure.NEG_SCORE_PSM_VIEW.replace(Configure.DEFAULT_PROJECT_ID, accessionId);
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
        List<Double> scoreList = scoredPSMDao.findRecommConfidentScore(psmTableName);
        return getDoubleHistList(scoreList, numBins);
    }

    private List<HistogramBin>getConfidentScoreHistData(String psmTableName, Integer numBins) {
        List<Double> scoreList = scoredPSMDao.findConfidentScore(psmTableName);
        return getDoubleHistList(scoreList, numBins);
    }

    private List<HistogramBin>getClusterRatioHistData(String psmTableName, Integer numBins) {
        List<Double> sizeList = scoredPSMDao.findClusterRatio(psmTableName);
        return getDoubleHistList(sizeList, numBins);
    }

    private List<HistogramBin>getClusterSizeHistData(String psmTableName, Integer numBins) {
        List<Integer> sizeList = scoredPSMDao.findClusterSize(psmTableName);
        return getIntHistList(sizeList, numBins);
    }



    private List<HistogramBin> getIntHistList(List<Integer> integerList, Integer numBins) {
        if (integerList.size() < 1) {
            List<HistogramBin> binList = new ArrayList<HistogramBin>();
            return binList;
        }

        integerList.sort((a, b) -> Integer.compare(a, b));

        Integer onePercentile = StatisticsUtils.getIntPercentileFromSorted(1, integerList);
        Integer lastOnePercentile = StatisticsUtils.getIntPercentileFromSorted(99, integerList);

        HistogramBin firstBin = new HistogramBin(1, (double) 0, (double)onePercentile,  0);
        HistogramBin lastBin =  new HistogramBin(numBins, (double) lastOnePercentile, (double) Integer.MAX_VALUE, 0);

        for (int i=integerList.size()-1; i>=0; i--) {
            Integer itemI = integerList.get(i);
            if (itemI > lastBin.getLowerBound() && itemI <= lastBin.getUpperBound()) {
                lastBin.addOne();
                integerList.remove(itemI);
            }
            if (itemI >= firstBin.getLowerBound() && itemI <= firstBin.getUpperBound()) {
                firstBin.addOne();
                integerList.remove(itemI);
            }
        }
        if (integerList.size() <= 0) {
            List<HistogramBin> binList = new ArrayList<HistogramBin>();
            binList.add(0, firstBin);
            binList.add(binList.size(), lastBin);
            return binList;
        }else {
            Integer min = integerList.get(0);
            Integer max = integerList.get(integerList.size() - 1);
            List<HistogramBin> binList = StatisticsUtils.calcIntHistogram(integerList, min, max, numBins - 2);
            binList.add(0, firstBin);
            binList.add(binList.size(), lastBin);
            return binList;
        }
    }


    /***
     * calculate the histogram bins based on the double list
     * @param doubleList
     * @param numBins
     * @return
     */
    private List<HistogramBin> getDoubleHistList(List<Double> doubleList, Integer numBins){

        if (doubleList.size() < 1) {
            List<HistogramBin> binList = new ArrayList<HistogramBin>();
            return binList;
        }

        doubleList.sort((a, b) -> Double.compare(a, b));

        Double onePercentile = StatisticsUtils.getPercentileFromSorted(1, doubleList);
        Double lastOnePercentile = StatisticsUtils.getPercentileFromSorted(99, doubleList);

        Double lowerBoud = doubleList.get(0) <0.0 ? -1.0 : 0.0 ;
        Double upperBoud = doubleList.get(doubleList.size() - 1) > 0.0 ? 1.0 : 0.0 ;
        HistogramBin firstBin = new HistogramBin(1, lowerBoud, onePercentile,  0);
        HistogramBin lastBin =  new HistogramBin(numBins, lastOnePercentile, upperBoud, 0);

        for (int i=doubleList.size()-1; i>=0; i--) {
            Double itemF = doubleList.get(i);
            if (itemF > lastBin.getLowerBound() && itemF <= lastBin.getUpperBound()) {
                lastBin.addOne();
                doubleList.remove(itemF);
            }
            if (itemF >= firstBin.getLowerBound() && itemF <= firstBin.getUpperBound()) {
                firstBin.addOne();
                doubleList.remove(itemF);
            }
        }
        if (doubleList.size() <= 0) {
            List<HistogramBin> binList = new ArrayList<HistogramBin>();
            binList.add(0, firstBin);
            binList.add(binList.size(), lastBin);
            return binList;
        }else {
            Double min = doubleList.get(0);
            Double max = doubleList.get(doubleList.size() - 1);
            List<HistogramBin> binList = StatisticsUtils.calcHistogram(doubleList, min, max, numBins - 2);
            binList.add(0, firstBin);
            binList.add(binList.size(), lastBin);
            return binList;
        }
    }

}


