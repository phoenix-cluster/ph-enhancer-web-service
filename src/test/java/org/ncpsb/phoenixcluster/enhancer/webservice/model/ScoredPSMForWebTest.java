package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.junit.Before;
import org.junit.Test;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by baimi on 2017/12/8.
 */
public class ScoredPSMForWebTest {
    ScoredPSMForWeb psmForWeb = new ScoredPSMForWeb();

    @Before
    public void setUp() throws Exception {
        psmForWeb.setPeptideSequence("LVDTFLEDTKK");
        psmForWeb.setPeptideModsStr("4-MOD:00696,9-MOD:00696");
        psmForWeb.setRecommendPeptide("YSITAKNKAGQK");
        psmForWeb.setRecommendPepModsStr("2-MOD:00696,4-MOD:00696");
    }

    @Test
    public void setPTMs() throws Exception {
        psmForWeb.setPTMs();
        System.out.println(psmForWeb.getPeptideMods());
        System.out.println(psmForWeb.getRecommendPepMods());
    }
}