package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.jpa.HBaseDao;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.HistogramBin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class StatisticsService {
    private String spectrumTableName = "V_CLUSTER_SPEC";

    @Autowired
    private HBaseDao hBaseDao;


    public Integer totalSpectrumInCluster(String clusterID) {
        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS total FROM " + spectrumTableName + "WHERE CLUSTER_FK='" + clusterID + "'");
        Integer totalElement = (Integer) hBaseDao.queryTotal(querySql.toString());
        return totalElement;
    }

    public  List<HistogramBin> getHistData(Integer binNum){
        return null;
    }
}


