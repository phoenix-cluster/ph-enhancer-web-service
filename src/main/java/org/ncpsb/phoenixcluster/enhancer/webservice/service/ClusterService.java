package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.ClusterDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class ClusterService {
    @Autowired
    private ClusterDaoMysqlImpl clusterDao;

    @Autowired
    private TaxonomyService taxonomyService;

    /***
     * find a cluster by clusterId
     * @param clusterId
     * @return
     */
    public Cluster findByClusterId(String clusterId) {
        Cluster cluster = clusterDao.findByClusterId(clusterId);
        List<String> taxidWithNameList = taxonomyService.addNameForTaxidStringList(cluster.getTaxIds());
            if (taxidWithNameList != null) {
                cluster.setTaxIds(taxidWithNameList);
            }
        return cluster;
//        return (clusters != null && clusters.size() > 0) ? clusters : null;
    }

    /***
     * find All clusterIds, with sorting and pagination
     * @param page
     * @param size
     * @param sortField
     * @param sortDirection
     * @return
     */
    public List<String> findClusterIds(Integer page, Integer size, String sortField, String sortDirection) {
        return clusterDao.findClusterIds(page, size, sortField, sortDirection);
    }
}


