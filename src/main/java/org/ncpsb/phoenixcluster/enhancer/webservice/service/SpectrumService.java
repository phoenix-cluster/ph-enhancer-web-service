package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.SpectrumDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Spectrum;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.TableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class SpectrumService{

    @Autowired
    private SpectrumDaoMysqlImpl spectrumDao;

    public Spectrum getSpectrumByTitle(String title) {
        return spectrumDao.findSpectrumByTitle(title);
    }

    public String findSpecPeptideSeqByTitle(String title){
        return spectrumDao.findSpecPeptideSeqByTitle(title);
    }

    public List<Spectrum> findSpectraByTitles(String titlesStr) {
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

        ArrayList<Spectrum> spectraInCluster = new ArrayList<>();

        for (String projectId : projectTitlesMap.keySet()) {
            List<String> titleListTemp = projectTitlesMap.get(projectId);
            String tableName = "T_SPECTRUM_" + projectId;
            List<Spectrum> spectrums = spectrumDao.findSpectraByTitles(titleListTemp, tableName);
            if (spectrums != null) {
                System.out.println("Got " + spectrums.size() + " spectra for " + projectId);
                spectraInCluster.addAll(spectrums);
            }
        }

        return  spectraInCluster;
//        return spectrumInClusterDao.findSpectraInClusterByTitles(titleList, );
    }

}


