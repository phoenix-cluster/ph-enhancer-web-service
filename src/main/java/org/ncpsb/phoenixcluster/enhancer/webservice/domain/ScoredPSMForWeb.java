package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ModificationUtils;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baimi on 2017/12/8.
 */
public class ScoredPSMForWeb extends ScoredPSM {
    private List <ModificationForWeb> peptideMods;
    private List <ModificationForWeb> recommendPepMods;

    public ScoredPSMForWeb() {
        super();
    }

    public List<ModificationForWeb> getPeptideMods() {
        return peptideMods;
    }

    public List<ModificationForWeb> getRecommendPepMods() {
        return recommendPepMods;
    }

    public void setPTMs() {
        this.peptideMods = ModificationUtils.retrieveMods(super.getPeptideModsStr(), super.getPeptideSequence());
        this.recommendPepMods = ModificationUtils.retrieveMods(super.getRecommendPepModsStr(), super.getRecommendPeptide());
    }



    @Override
    public String toString() {
        return "ScoredPSMForWeb{" +
                "peptideMods=" + peptideMods +
                ", recommendPepMods=" + recommendPepMods +
                '}';
    }
}
