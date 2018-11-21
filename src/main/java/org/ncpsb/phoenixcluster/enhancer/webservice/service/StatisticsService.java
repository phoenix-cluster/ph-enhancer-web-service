package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.StatisticsDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class StatisticsService {

    @Autowired
    private StatisticsDaoMysqlImpl statisticsDao;


    public Integer findTotalSpectrumInCluster(String clusterID) {
        return statisticsDao.findTotalSpectrumInCluster(clusterID);
    }

    public VennData findVennDataByProjectId(String projectId) {
        return statisticsDao.findVennDataByProjectId(projectId);
    }

    public Thresholds findThresholdsByProjectId(String projectId) {
        return statisticsDao.findThresholdsByProjectId(projectId);
    }

    public List<String> findProjects() {
        return statisticsDao.findProjects();
    }

    public List<VennData> getVennDataList() {
        return statisticsDao.getVennDataList();
    }
}


