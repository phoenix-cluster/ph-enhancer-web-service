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
        for (String part : parts){
            floats.add(Float.parseFloat(part));
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
