package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import org.ncpsb.phoenixcluster.enhancer.webservice.service.TaxonomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaxonomyUtils {
     /**
     * convert from string to list of ids
     * @param taxidsString
     * @return
     */

    public static List<String> taxidString2List(String taxidsString) {
        //remove the "[]" and split to strings


        if(taxidsString.length() <= 2 || taxidsString.equalsIgnoreCase("none")){
            System.out.println("Error: empty seq_taxids" + taxidsString);
            return null;
        }
        String withoutBracketString = taxidsString.substring(1, taxidsString.length()-1).replaceAll("\\s+","");
        List<String> taxids= Arrays.asList(withoutBracketString.split(",", 0));
        return taxids;
    }




}
