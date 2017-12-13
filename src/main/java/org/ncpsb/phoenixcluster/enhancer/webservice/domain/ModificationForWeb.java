package org.ncpsb.phoenixcluster.enhancer.webservice.domain;

/**
 * Created by baimi on 2017/12/8.
 */
public class ModificationForWeb {
    int location;
    Double deltaMass;
    String residue;

    public ModificationForWeb(int location, Double deltaMass, String residue) {
        this.location = location;
        this.deltaMass = deltaMass;
        this.residue = residue;
    }

    public int getLocation() {
        return location;
    }

    public Double getDeltaMass() {
        return deltaMass;
    }

    public String getResidue() {
        return residue;
    }

    @Override
    public String toString() {
        return "ModificationForWeb{" +
                "location=" + location +
                ", deltaMass=" + deltaMass +
                ", residue='" + residue + '\'' +
                '}';
    }
}
