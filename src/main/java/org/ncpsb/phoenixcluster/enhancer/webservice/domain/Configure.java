package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

import java.util.HashMap;

/**
 * Created by baimi on 2017/12/15.
 */
public class Configure {
    public static final String DEFAULT_PAGE_SIZE = "50";
    public static final String DEFAULT_PAGE_NUM = "1";
    public static final String DEFAULT_SORT_FIELD = "confidentScore";
    public static final String DEFAULT_SORT_DIRECTION = "ASC";
    public static final String DEFAULT_PROJECT_ID = "PXD000021";

    public static final String RECOMMEND_PSM_TABLE = "T_PXD000021_RECOMM_ID_20171211";
    public static final String SCORED_PSM_TABLE = "T_PXD000021_SCORED_PSM_20171211";
    public static final String POS_SCORED_PSM_VIEW= "V_PXD000021_P_SCORED_PSM";
    public static final String NEG_SCORED_PSM_VIEW= "V_PXD000021_N_SCORED_PSM";
    public static final String RECOMM_PSM_VIEW= "V_PXD000021_RECOMM_ID";
    public static final String CLUSTER_TABLE = "V_CLUSTER";
    public static final String SPECTRUM_TABLE = "V_CLUSTER_SPEC";

    public static final HashMap<String, String> COLUMN_MAP = new HashMap<String, String>() {{
        put("confidentScore", "CONF_SC");
        put("clusterRatio", "CLUSTER_RATIO");
        put("clusterSize", "CLUSTER_SIZE");
        put("acceptance", "ACCEPTANCE");
    }};

}
