package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import org.junit.Test;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ModificationForWeb;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ModificationUtils;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;

import java.util.List;

import static org.junit.Assert.*;

public class ModificationUtilsTest {

    @Test
    public void retrieveMods() {
        String modListStr = "3-[PSI-MS,MS:1001524,fragment neutral loss,63.998283],3-UNIMOD:35";
        String peptide = "YVMGNNPADLLAVDSR";

        List<ModificationForWeb> modList = ModificationUtils.retrieveMods(modListStr, peptide);
        System.out.println(modList);
    }


    @Test
    public void testModReader() {
        ModReader modReader = ModReader.getInstance();
        String modListStr = "3-[PSI-MS,MS:1001524,fragment neutral loss,63.998283],3-UNIMOD:35";
        List<PTM> ptms = modReader.getPTMListByEqualName(modListStr);
        System.out.println(ptms);
    }

    @Test
    public void testIsUniModStr() {
        String testStr1 = "3-[PSI-MS,MS:1001524,fragment neutral loss,63.998283]";
        String testStr2 = "3-UNIMOD:35";

        assertFalse(ModificationUtils.isUnimodStr(testStr1));
        assertTrue(ModificationUtils.isUnimodStr(testStr2));
    }

    @Test
    public void testGetMods() {
        String modListStr = "3-[PSI-MS,MS:1001524,fragment neutral loss,63.998283],3-UNIMOD:35, 4-UNIMOD:45";
        List<String> modStrList = ModificationUtils.getUnimodStrs(modListStr);
        System.out.println(modStrList);
        assertEquals(modStrList.size(), 2);
        assertEquals(modStrList.get(0), "3-UNIMOD:35");
    }

}