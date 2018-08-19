package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baimi on 2017/11/3.
 */
public class ClusterUtils {

    static public List<Float> getFloatListFromString(String str, String splitSymbol) {
        ArrayList<Float> floats = new ArrayList<Float>();
        String[] parts = str.split(splitSymbol);
        Float floatNo = null ;
        for (String part : parts){
            try{
                if(part.equalsIgnoreCase("nan") )
                    floatNo = Float.valueOf(0);
                else
                    floatNo =Float.parseFloat(part);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }

            if(floatNo !=null)
                floats.add(floatNo);
        }
        return floats;
    }

    static public List<String> getStringListFromString(String str, String splitSymbol) {
        ArrayList<String> strings = new ArrayList<String>();
        String[] parts = str.split(splitSymbol);
        for (String part : parts){
            strings.add(part);
        }
//        System.out.println(strings);
        return strings;
    }
}
