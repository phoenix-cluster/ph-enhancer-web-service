package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

/**
 * Created by baimi on 2017/10/26.
 */


public class ScoredPSM {
    private String querySpectrumTitle;
    private String previousIdentification;

    private String clusterID;
    private Float clusterRatio;
    private Integer clusterSize;

    private Float confidentiScore;
    private String recommendIdentification;

    private String acceptence;

    public ScoredPSM() {
    }

    public String getQuerySpectrumTitle() {
        return querySpectrumTitle;
    }

    public void setQuerySpectrumTitle(String querySpectrumTitle) {
        this.querySpectrumTitle = querySpectrumTitle;
    }

    public String getPreviousIdentification() {
        return previousIdentification;
    }

    public void setPreviousIdentification(String previousIdentification) {
        this.previousIdentification = previousIdentification;
    }

    public String getClusterID() {
        return clusterID;
    }

    public void setClusterID(String clusterID) {
        this.clusterID = clusterID;
    }

    public Float getClusterRatio() {
        return clusterRatio;
    }

    public void setClusterRatio(Float clusterRatio) {
        this.clusterRatio = clusterRatio;
    }

    public Integer getClusterSize() {
        return clusterSize;
    }

    public void setClusterSize(Integer clusterSize) {
        this.clusterSize = clusterSize;
    }

    public Float getConfidentiScore() {
        return confidentiScore;
    }

    public void setConfidentiScore(Float confidentiScore) {
        this.confidentiScore = confidentiScore;
    }

    public String getRecommendIdentification() {
        return recommendIdentification;
    }

    public void setRecommendIdentification(String recommendIdentification) {
        this.recommendIdentification = recommendIdentification;
    }

    public String getAcceptence() {
        return acceptence;
    }

    public void setAcceptence(String acceptence) {
        this.acceptence = acceptence;
    }
}
