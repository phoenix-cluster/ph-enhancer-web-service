package org.ncpsb.phoenixcluster.enhancer.webservice.model;

/**
 * Created by baimi on 2017/12/15.
 */
public class HistogramBin {
    private Integer rank;
    private Double lowerBound;
    private Double upperBound;
    private Integer value;

    public HistogramBin(Integer rank, Double lowerBound, Double upperBound, Integer value) {
        this.rank = rank;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.value = value;
    }

    public Integer getRank() {
        return rank;
    }

    public Double getLowerBound() {
        return lowerBound;
    }

    public Double getUpperBound() {
        return upperBound;
    }

    public Integer getValue() {
        return value;
    }

    public void addOne() {
        this.value ++;
    }
}
