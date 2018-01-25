package org.ncpsb.phoenixcluster.enhancer.webservice.model;

/**
 * The threaholds numbers of a project for venn diagram
 */

public class Thresholds {
    private String  projectId;
    private  Integer cluster_size_threshold;
    private  Float cluster_ratio_threshold;
    private  Float conf_sc_threshold;
    private  Float spectrast_fval_threshold;

    public Thresholds(String projectId,Integer cluster_size_threshold, Float cluster_ratio_threshold, Float conf_sc_threshold, Float spectrast_fval_threshold) {
        this.projectId = projectId;
        this.cluster_size_threshold = cluster_size_threshold;
        this.cluster_ratio_threshold = cluster_ratio_threshold;
        this.conf_sc_threshold = conf_sc_threshold;
        this.spectrast_fval_threshold = spectrast_fval_threshold;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getCluster_size_threshold() {
        return cluster_size_threshold;
    }

    public void setCluster_size_threshold(Integer cluster_size_threshold) {
        this.cluster_size_threshold = cluster_size_threshold;
    }

    public Float getCluster_ratio_threshold() {
        return cluster_ratio_threshold;
    }

    public void setCluster_ratio_threshold(Float cluster_ratio_threshold) {
        this.cluster_ratio_threshold = cluster_ratio_threshold;
    }

    public Float getConf_sc_threshold() {
        return conf_sc_threshold;
    }

    public void setConf_sc_threshold(Float conf_sc_threshold) {
        this.conf_sc_threshold = conf_sc_threshold;
    }

    public Float getSpectrast_fval_threshold() {
        return spectrast_fval_threshold;
    }

    public void setSpectrast_fval_threshold(Float spectrast_fval_threshold) {
        this.spectrast_fval_threshold = spectrast_fval_threshold;
    }
}
