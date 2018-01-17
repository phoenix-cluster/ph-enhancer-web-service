package org.ncpsb.phoenixcluster.enhancer.webservice.model;

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

    public static final String NEW_PSM_TABLE = "T_PXD000021_NEW_PSM_20171211";
    public static final String SCORE_PSM_TABLE = "T_PXD000021_SCORE_PSM_20171211";
    public static final String POS_SCORE_PSM_VIEW= "V_PXD000021_P_SCORE_PSM";
    public static final String NEG_SCORE_PSM_VIEW= "V_PXD000021_N_SCORE_PSM";
    public static final String NEW_PSM_VIEW= "V_PXD000021_NEW_PSM";
    public static final String CLUSTER_TABLE = "V_CLUSTER";
    public static final String SPECTRUM_TABLE = "V_CLUSTER_SPEC";

    public static final HashMap<String, String> COLUMN_MAP = new HashMap<String, String>() {{
        put("confidentScore", "CONF_SC");
        put("recommConfidentScore", "RECOMM_SEQ_SC");
        put("clusterRatio", "CLUSTER_RATIO");
        put("clusterSize", "CLUSTER_SIZE");
        put("acceptance", "ACCEPTANCE");
    }};

}
