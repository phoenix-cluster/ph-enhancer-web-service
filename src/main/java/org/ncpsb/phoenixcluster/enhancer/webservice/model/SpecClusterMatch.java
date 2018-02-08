package org.ncpsb.phoenixcluster.enhancer.webservice.model;

public class SpecClusterMatch {
    Integer PsmId;//2
    String SpecTitle;//PXD000535;PRIDE_Exp_Complete_Ac_31837.xml;spectrum=16146
    String ClusterId;     //b070060e-4f5e-4698-9277-9fa6c0d44e53
    String ClusterSize; //15
    String ClusterRatio;//1.0
    String PreSeq;//DLTPAVTDNDEADK
    String PreMods;
    String RecommSeq;//R_Better_DLTPAVTDNDEADK
    String RecommMods;//
    Float ConfSc;//0.39203042
    Float RecommSeqSc;//0.39203042


    public Integer getPsmId() {
        return PsmId;
    }

    public void setPsmId(Integer psmId) {
        PsmId = psmId;
    }

    public String getSpecTitle() {
        return SpecTitle;
    }

    public void setSpecTitle(String specTitle) {
        SpecTitle = specTitle;
    }

    public String getClusterId() {
        return ClusterId;
    }

    public void setClusterId(String clusterId) {
        ClusterId = clusterId;
    }

    public String getClusterSize() {
        return ClusterSize;
    }

    public void setClusterSize(String clusterSize) {
        ClusterSize = clusterSize;
    }

    public String getClusterRatio() {
        return ClusterRatio;
    }

    public void setClusterRatio(String clusterRatio) {
        ClusterRatio = clusterRatio;
    }

    public String getPreSeq() {
        return PreSeq;
    }

    public void setPreSeq(String preSeq) {
        PreSeq = preSeq;
    }

    public String getPreMods() {
        return PreMods;
    }

    public void setPreMods(String preMods) {
        PreMods = preMods;
    }

    public String getRecommSeq() {
        return RecommSeq;
    }

    public void setRecommSeq(String recommSeq) {
        RecommSeq = recommSeq;
    }

    public String getRecommMods() {
        return RecommMods;
    }

    public void setRecommMods(String recommMods) {
        RecommMods = recommMods;
    }

    public Float getConfSc() {
        return ConfSc;
    }

    public void setConfSc(Float confSc) {
        ConfSc = confSc;
    }

    public Float getRecommSeqSc() {
        return RecommSeqSc;
    }

    public void setRecommSeqSc(Float recommSeqSc) {
        RecommSeqSc = recommSeqSc;
    }
}
