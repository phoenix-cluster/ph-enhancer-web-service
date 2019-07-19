package org.ncpsb.phoenixcluster.enhancer.webservice.service;


import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.ncpsb.phoenixcluster.enhancer.webservice.dao.mysql.TaxonomyDaoMysqlImpl;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Taxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baimi on 2017/10/11.
 */


@Service
public class TaxonomyService {

    @Autowired
    private TaxonomyDaoMysqlImpl taxonomyDao;

    public List<String> addNameForTaxidStringList(List<String> taxids) {
        List<String> taxidAndNameList = new ArrayList<>();
        for(String taxid: taxids) {
            Taxonomy taxonomy = getTaxonomyById(taxid);
            if (taxonomy == null) {
                System.out.println("ERROR, failed to get taxonomy name for id" + taxid);
                continue;
            }
            String name = taxonomy.getName();
            String taxidFullString = taxid + ":" + name;
            taxidAndNameList.add(taxidFullString);
        }
        return taxidAndNameList;
    }


    public Taxonomy getTaxonomyById(String id) {
        Taxonomy taxonomy = taxonomyDao.findByTaxonomyId(id);
        if (taxonomy == null){
            taxonomy = getTaxonomyOnline(id);
            taxonomyDao.insertTaxonomy(taxonomy);
        }
        return taxonomy;
    }

    private Taxonomy getTaxonomyOnline(String id) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            //用get方法发送http请求
            String urlString = "https://rest.ensembl.org/taxonomy/id/" + id + "?content-type=application/json";
            HttpGet get = new HttpGet(urlString);
            System.out.println("执行get请求:...."+get.getURI());
            CloseableHttpResponse httpResponse = null;
            //发送get请求
            httpResponse = httpClient.execute(get);
            try{
                //response实体
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("响应状态码:"+ httpResponse.getStatusLine());
                    if(httpResponse.getStatusLine().getStatusCode() != 200){
                        System.out.println("ERROR! Failed to get scientific name from :" + urlString );
                        throw new IOException("ERROR! Failed to get scientific name from :" + urlString );
                    }

                    String scientificName = getScientificNameFromString(EntityUtils.toString(entity));
//                    System.out.println("-------------------------------------------------");
//                    System.out.println("响应内容:" + EntityUtils.toString(entity));
//                    System.out.println("-------------------------------------------------");
                    return new Taxonomy(id, scientificName);
                }
            }
            finally{
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                closeHttpClient(httpClient);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getScientificNameFromString(String s) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
        String name = jsonObject.get("name").getAsString(); // returns a JsonElement for that name
        return name;
    }

    private void closeHttpClient(CloseableHttpClient client) throws IOException{
        if (client != null){
            client.close();
        }
    }

    private CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }

}


