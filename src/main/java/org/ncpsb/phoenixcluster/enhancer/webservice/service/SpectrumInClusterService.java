package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.jcodings.util.Hash;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.SpectrumInClusterDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.SpectrumInCluster;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.TableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class SpectrumInClusterService {

    @Autowired
    private SpectrumInClusterDaoMysqlImpl spectrumInClusterDao;


    public List<SpectrumInCluster> findSpectraInClusterByTitles(String titlesStr) {
        String[] titles = titlesStr.split("\\|\\|");
        List<String> titleList = new ArrayList<String>(Arrays.asList(titles));
        HashMap<String, ArrayList<String>> projectTitlesMap = new HashMap<>();
        for (String title : titleList) {
            String projectId = TableName.getProjectId(title);
            if (projectTitlesMap.containsKey(projectId)) {
                ArrayList<String> titlesInProject = projectTitlesMap.get(projectId);
                titlesInProject.add(title);
                projectTitlesMap.put(projectId, titlesInProject);
            }
            else{
                ArrayList<String> titlesInProject = new ArrayList<>();
                titlesInProject.add(title);
                projectTitlesMap.put(projectId, titlesInProject);
            }

        }

        ArrayList<SpectrumInCluster> spectraInCluster = new ArrayList<>();

        for (String projectId : projectTitlesMap.keySet()) {
            List<String> titleListTemp = projectTitlesMap.get(projectId);
            String tableName = "T_CLUSTER_SPEC_" + projectId;
            spectraInCluster.addAll(spectrumInClusterDao.findSpectraInClusterByTitles(titleListTemp, tableName));
        }

        return  spectraInCluster;
//        return spectrumInClusterDao.findSpectraInClusterByTitles(titleList, );
    }


    public SpectrumInCluster findSpectrumInClusterByTitle(String title) {
        String tableName = TableName.getSpectrumTableName(title, "T_CLUSTER_SPEC_");
        return spectrumInClusterDao.findSpectrumInClusterByTitle(title, tableName);
    }
//    public List<Spectrum> getSpectraInCluster(Integer page, Integer size, String clusterID) {
//
//        StringBuffer querySql = new StringBuffer("SELECT * FROM " + spectrumTableName);
//        querySql.append(" LIMIT " + size);
//        querySql.append(" OFFSET " + (page - 1) * size);
//
//        System.out.println("Going to execute: " + querySql);
//        List<Spectrum> scoredPSMs = (List<Spectrum>) hBaseDao.getSpectraInCluster(querySql.toString(), null, new RowMapper<Spectrum>() {
//            @Override
//            public Spectrum mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Spectrum spectrum = new Spectrum();
//                spectrum.setTitle(rs.getString(""));
//                spectrum.setClusterId(rs.getString("CLUSTER_FK"));
//                spectrum.setPrecursorMz(rs.getFloat(""));
//                spectrum.setCharge(rs.getInt(""));
//                spectrum.setSequence(rs.getString(""));
//
//                return spectrum;
//            }
//        });
//        return (scoredPSMs != null && scoredPSMs.size() > 0) ? (List) scoredPSMs : null;
//    }


}


