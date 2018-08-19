package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.ModificationForWeb;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by baimi on 2017/12/8.
 */
public class ModificationUtils {

    /**
     * Retrive modifications object from modification list string and the peptide sequence string.
     * @param modListStr
     * @param peptide
     * @return modifications for web using (with the mono delta mass value).
     */
    public static List<ModificationForWeb> retrieveMods(String modListStr, String peptide) {
        if (modListStr == null) {
            return null;
        }
        peptide = peptide.replace("R_Better_", "");
        peptide = peptide.replace("R_NEW_", "");
        peptide = peptide.replace("PRE_", "");
        ModReader modReader = ModReader.getInstance();
        List<ModificationForWeb> mods = new ArrayList<>();
        List<String> modStrList = getUnimodStrs(modListStr);
        for (String modStr : modStrList) {
            String[] strings = modStr.split("-");
            int location = Integer.parseInt(strings[0]);
            PTM ptm = modReader.getPTMbyAccession(strings[1]);

            Double deltaMass = ptm.getMonoDeltaMass();
            String residue = "";
            if(location > 0 ){
                try {
                    residue = String.valueOf(peptide.charAt(location - 1));
                }
                catch (Exception exception){
                    System.out.println("modStr: " + modStr);
                    System.out.println("location " + location + " out of string 's index: " + peptide);
                }
            }
            ModificationForWeb mod = new ModificationForWeb(location, deltaMass, residue);
            mods.add(mod);
        }
        return mods;
    }

    public static boolean isUnimodStr(String modStr){
        String unimodPattern = "\\d+\\-UNIMOD\\:\\d+";
        Pattern p = Pattern.compile(unimodPattern);
        Matcher m = p.matcher(modStr);
        return m.find();
    }

    public static List<String> getUnimodStrs(String modsStr){
        String unimodPattern = "(\\d+\\-UNIMOD\\:\\d+)";
        Pattern p = Pattern.compile(unimodPattern);
        Matcher m = p.matcher(modsStr);

        List modStrList =  new ArrayList<String>();

        while(m.find()){
            modStrList.add(m.group());
        }

        return modStrList;
    }

}
