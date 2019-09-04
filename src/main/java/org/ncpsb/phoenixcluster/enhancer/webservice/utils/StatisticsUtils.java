package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baimi on 2017/12/15.
 */
public class StatisticsUtils {

    public static List<HistogramBin> calcHistogram(List<Double> data, double min, double max, int numBins) {
        int[] result = new int[numBins];
        List<HistogramBin> binList = new ArrayList<HistogramBin>();
        double binSize = (max - min)/numBins;
//        System.out.println("max " + max + "- min" + min + "/numBins" + numBins + "got binsize" + binSize);
        Boolean isBinEQNumBin = false;
        Double valueBinEQNumBin = null;
        for (double d : data) {
            int bin = (int) ((d - min) / binSize); // changed this from numBins
            if (bin < 0) { /* this data is smaller than min */ }
            else if (bin > numBins) { /* this data point is bigger than max */ }
            else if (bin == numBins) {
                isBinEQNumBin = true;
                valueBinEQNumBin = d;
                result[bin-1] += 1;
            }
            else {
                result[bin] += 1;
            }
        }
        Double binWidth = (max - min)/(double) numBins;
//        for (int x : result) {
//            System.out.println(x);
//        }
        for(int i=0; i<result.length; i++) {
            HistogramBin bin = new HistogramBin(i+2, min+i*binWidth, min + (i+1)*binWidth, result[i]);//rank start from 2;
            binList.add(bin);
        }

        if(isBinEQNumBin){
            binList.get(binList.size() - 1).setUpperBound(valueBinEQNumBin);
        }
//        for (HistogramBin bin : binList) {
//            System.out.println(bin.getRank() + ":" + bin.getLowerBound() + "-" + bin.getUpperBound() + ":" + bin.getValue());
//        }

        return binList;
    }

    public static List<HistogramBin> calcIntHistogram(List<Integer> data, int min, int max, int numBins) {
        //in case of the int values between min-max is smaller than numBins
        if (max - min < numBins - 1) {
            numBins= (max - min) ;
        }
        int[] result = new int[numBins];
        List<HistogramBin> binList = new ArrayList<HistogramBin>();
        double binSize = (max - min)/numBins;
        Boolean isBinEQNumBin = false;
        Double valueBinEQNumBin = null;
//        System.out.println("max " + max + "- min" + min + "/numBins" + numBins + "got binsize" + binSize);



        for (double d : data) {
            int bin = (int) ((d - min) / binSize); // changed this from numBins
            if (bin < 0) { /* this data is smaller than min */ }
            else if (bin > numBins) { /* this data point is bigger than max */ }
            else if (bin == numBins) {
                result[bin-1] += 1;
                isBinEQNumBin = true;
                valueBinEQNumBin = d;
            }
            else {
                result[bin] += 1;
            }
        }

        Integer binWidth = (max - min)/ numBins;

        for(int i=0; i<result.length; i++) {
            HistogramBin bin = new HistogramBin(i+2, (double)min+i*binWidth, (double )min + (i+1)*binWidth, result[i]);//rank start from 2;
            binList.add(bin);
        }
        if(isBinEQNumBin){
            binList.get(binList.size()-1).setUpperBound(valueBinEQNumBin);
        }
//        for (HistogramBin bin : binList) {
//            System.out.println(bin.getRank() + ":" + bin.getLowerBound() + "-" + bin.getUpperBound() + ":" + bin.getValue());
//        }
        return binList;
    }


    public static double getPercentileFromSorted(int p,  List<Double> sortedData) {
        int length = sortedData.size();
        int i = 0;
        if(length >= 1){
            i = (int)(length * p)/100;
            return sortedData.get(i);
        }else{
            return 0.0;
        }
    }

    public static int getIntPercentileFromSorted(int p,  List<Integer> sortedData) {
        int length = sortedData.size();
        int i = 0;
        if(length >= 1){
            i = (int)(length * p)/100;
            return sortedData.get(i);
        }else{
            return 0;
        }
    }

}
