package org.ncpsb.phoenixcluster.enhancer.webservice.model;

/**
 * The Cluster ratio information class
 */
public class ClusterRatioStr {
    private String clusterId;
    private String clusterRatioStr;

    public ClusterRatioStr(String clusterId, String clusterRatioStr) {
        this.clusterId = clusterId;
        this.clusterRatioStr = clusterRatioStr;
    }

    public String getClusterId() {
        return clusterId;
    }

    public String getClusterRatioStr() {
        return clusterRatioStr;
    }
}
