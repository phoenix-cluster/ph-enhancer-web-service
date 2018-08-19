package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import java.util.List;

/**
 * Created by baimi on 2017/10/26.
 */


public class ScoredPSM {
    private Integer Id;

    private String peptideSequence;
    private String peptideModsStr;

    private String clusterId;
    private Float clusterRatio;
    private String clusterRatioStr;
    private Integer clusterSize;

    private Float confidentScore;
    private Float recommConfidentScore;
    private String recommendPeptide;
    private String recommendPepModsStr;

    private Integer spectraNum;
    private List<String> spectraTitles;

    private Integer acceptance;

    public ScoredPSM() {
    }

    public String getPeptideModsStr() {
        return peptideModsStr;
    }

    public void setPeptideModsStr(String peptideModsStr) {
        this.peptideModsStr = peptideModsStr;
    }

    public String getRecommendPepModsStr() {
        return recommendPepModsStr;
    }

    public void setRecommendPepModsStr(String recommendPepMods) {
        this.recommendPepModsStr = recommendPepMods;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId){
        this.clusterId = clusterId;
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

    public Float getConfidentScore() {
        return confidentScore;
    }

    public void setConfidentScore(Float confidentScore) {
        this.confidentScore = confidentScore;
    }

    public Float getRecommConfidentScore() {
        return recommConfidentScore;
    }

    public void setRecommConfidentScore(Float recommConfidentScore) {
        this.recommConfidentScore = recommConfidentScore;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getPeptideSequence() {
        return peptideSequence;
    }

    public void setPeptideSequence(String peptideSequence) {
        this.peptideSequence = peptideSequence;
    }

    public String getRecommendPeptide() {
        return recommendPeptide;
    }

    public void setRecommendPeptide(String recommendPeptide) {
        this.recommendPeptide = recommendPeptide;
    }

    public Integer getSpectraNum() {
        return spectraNum;
    }

    public void setSpectraNum(Integer spectraNum) {
        this.spectraNum = spectraNum;
    }

    public List<String> getSpectraTitles() {
        return spectraTitles;
    }

    public void setSpectraTitles(List<String> spectraTitles) {
        this.spectraTitles = spectraTitles;
    }

    public Integer getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Integer acceptance) {
        this.acceptance = acceptance;
    }

    public String getClusterRatioStr() {
        return clusterRatioStr;
    }

    public void setClusterRatioStr(String clusterRatioStr) {
        this.clusterRatioStr = clusterRatioStr;
    }
}
